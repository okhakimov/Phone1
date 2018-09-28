package com.example.oleg.phone1;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        Bundle bundle = getIntent().getExtras();
        final String cust_conf_name = bundle.getString("cust_conf_name");
        final EditText editText = (EditText)findViewById(R.id.config_edittext);

        final File in_file = new File(cust_conf_name);
        String content = "";
        String msg = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(in_file));
            String line;
            int line_nb = 1;
            while ((line = br.readLine()) != null) {
                content = content + line + "\n";
            }
            br.close();
        }
        catch (IOException e) {
            msg="--error: can't read file";
        }


        editText.setText(content, TextView.BufferType.EDITABLE);

        Button saveButton = (Button) findViewById(R.id.config_save);//get id of button 1

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                try {
                    File file = new File(cust_conf_name);
                    FileOutputStream fileOutputStream = new FileOutputStream(file,false);
                    fileOutputStream.write((text + System.getProperty("line.separator")).getBytes());
                    Log.d("==","save");
                    fileOutputStream.close();
                }  catch(FileNotFoundException ex) {
                    Log.d("==", ex.getMessage());
                }  catch(IOException ex) {
                    Log.d("==", ex.getMessage());
                }
                //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();//display the text of button1
                Intent MainIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(MainIntent);

            }

        });

        Button cancelButton = (Button) findViewById(R.id.config_cancel);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MainIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(MainIntent);
            }
        });

        Button helpButton = (Button) findViewById(R.id.config_help);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HelpIntent = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(HelpIntent);
            }
        });


    }
}
