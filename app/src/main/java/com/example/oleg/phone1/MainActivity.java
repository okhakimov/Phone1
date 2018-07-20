package com.example.oleg.phone1;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Button> buttons;
    private static final int[] BUTTON_IDS = {
            R.id.call1,
            R.id.call2,
            R.id.call3,
            R.id.call4,
    };

    File sdcard = Environment.getExternalStorageDirectory();
    String in_file_name = sdcard+"/DCIM/PHONE/phone_list.txt";
    //File in_file = new File(sdcard,in_file_name);
    public Params params = new Params(in_file_name);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hide status bar. end
        //set landscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //set landscape orientation. end

        setContentView(R.layout.activity_main);

        // read config file

        buttons = new ArrayList<Button>();
        // or slightly better
        // buttons = new ArrayList<Button>(BUTTON_IDS.length);
        Integer k = 0;
        for(int id : BUTTON_IDS) {
            Button button = (Button)findViewById(id);
            //button.setOnClickListener(this); // maybe
            button.setOnClickListener(b_OnClickListener);
            button.setTag(k+1);
            //Log.d("===k ",Integer.toString(k));
            button.setText(params.Phones[k+1][0]);
            button.setBackgroundColor(Color.parseColor(params.CallColors[k+1]));
            k = k+1;
            buttons.add(button);
        }
    }

    View.OnClickListener b_OnClickListener =
            new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int n = (int) v.getTag();
                    Toast.makeText(getApplicationContext(), "call "+n+ " clicked", Toast.LENGTH_LONG).show();
                    // call
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+params.Phones[n][1]));
                    startActivity(intent);

                    //sms
                    //SmsManager sms = SmsManager.getDefault();
                    //sms.sendTextMessage("89871449251", null, "test1", null, null);

                }};

}
