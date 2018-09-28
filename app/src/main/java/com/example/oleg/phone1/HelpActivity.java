package com.example.oleg.phone1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        InputStream help_text = getResources().openRawResource(R.raw.help);
        String content = "";
        String msg = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(help_text));
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

        final TextView helpText = (TextView) findViewById(R.id.help_textview);
        helpText.setMovementMethod(new ScrollingMovementMethod());
        helpText.setText(content);
    }
}
