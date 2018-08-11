package com.example.oleg.phone1;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
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

    private List<Button> sms_buttons;
    private static final int[] SMS_BUTTON_IDS = {
            R.id.sms1,
            R.id.sms2,
            R.id.sms3,
    };

    File sdcard = Environment.getExternalStorageDirectory();
    public String in_file_name = sdcard+"/DCIM/PHONE/phone_list.txt";
    //File in_file = new File(sdcard,in_file_name);
    public Params params = new Params(in_file_name);

    // timers. in seconds to limit number of sms
    public long [] sms_t = {0,0,0,0};
    // minimal time interval between sms messages in seconds (900 = 15 min)
    public long sms_interval = 900;


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

        if (params.Options.get("test").equals("1")) {
            // test
            Toast.makeText(getApplicationContext(), "== test mode ==", Toast.LENGTH_LONG).show();
        }

        buttons = new ArrayList<Button>();

        if ("ok".equals(params.msg)) {
            Integer k = 0;
            for (int id : BUTTON_IDS) {
                Button button = (Button) findViewById(id);
                //button.setOnClickListener(this); // maybe
                button.setOnClickListener(b_OnClickListener);
                button.setTag(k + 1);
                //Log.d("===k ",Integer.toString(k));
                button.setText(params.Phones[params.CallId[k + 1]][0]);
                button.setBackgroundColor(Color.parseColor(params.CallColors[k + 1]));
                k = k + 1;
                buttons.add(button);
            }

            k = 0;
            for (int id : SMS_BUTTON_IDS) {
                Button button = (Button) findViewById(id);
                button.setOnClickListener(b_smsOnClickListener);
                button.setTag(k + 1);
                //Log.d("===",params.SmsColors[k+1]);
                //button.setBackgroundResource(R.drawable.roundedbutton_green);
                //GradientDrawable drawable = (GradientDrawable) button.getDrawableState();
                //drawable.setColor(Color.RED);


                button.setBackgroundColor(Color.parseColor(params.SmsColors[k + 1]));
                button.setText(params.SmsLabels[k + 1]);
                k = k + 1;

            }
        } else {
            Button button = (Button) findViewById(R.id.call1);
            button.setTextSize(8);
            button.setText(params.msg);
            //Toast.makeText(getApplicationContext(), params.msg, Toast.LENGTH_LONG).show();
        }
    }

    View.OnClickListener b_OnClickListener =
            new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int n = (int) v.getTag();
                    if (params.Options.get("test").equals("1")) {
                        // test
                        Toast.makeText(getApplicationContext(), "== test call tel: "+params.Phones[params.CallId[n]][1], Toast.LENGTH_LONG).show();
                    } else {
                        // call
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + params.Phones[params.CallId[n]][1]));
                        startActivity(intent);
                    }
                }};
    View.OnClickListener b_smsOnClickListener =
            new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int n = (int) v.getTag();
                    //Toast.makeText(getApplicationContext(), "sms "+n+ " clicked", Toast.LENGTH_LONG).show();
                    // check timer
                    Boolean timer_ok = false;
                    if ((System.currentTimeMillis()/1000)-sms_t[n] > sms_interval) {
                        sms_t[n] = (System.currentTimeMillis()/1000);
                        timer_ok = true;
                    }
                    if (timer_ok) {
                        //Log.d("===","ok");
                        String phone;
                        String message;
                        SmsManager sms = SmsManager.getDefault();

                        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.click1);
                        mp.start();

                        for (Integer sms_id : params.SmsIds[n]) {
                            if (sms_id != null) {
                                phone = params.Phones[sms_id][1];
                                message = params.SmsMessages[n];
                                //Log.d("===",message);
                                if (params.Options.get("test").equals("1")) {
                                    // test
                                    Toast.makeText(getApplicationContext(), "== test sms: "+phone, Toast.LENGTH_LONG).show();
                                } else {
                                    //send sms
                                    sms.sendTextMessage(phone, null, message, null, null);
                                }
                            }
                        }

                    }

                }};

}
