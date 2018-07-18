package com.example.oleg.phone1;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

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
        Button b_call1 = (Button) findViewById(R.id.call1);
        b_call1.setOnClickListener(b_call1_OnClickListener);

        Button b_call2 = (Button) findViewById(R.id.call2);
        b_call2.setOnClickListener(b_call2_OnClickListener);

    }

    View.OnClickListener b_call1_OnClickListener =
            new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "call1 clicked", Toast.LENGTH_LONG).show();

                    // call
                    //Intent intent = new Intent(Intent.ACTION_CALL);
                    //intent.setData(Uri.parse("tel:89871449251"));
                    //startActivity(intent);

                    //sms
                    //SmsManager sms = SmsManager.getDefault();
                    //sms.sendTextMessage("89871449251", null, "test1", null, null);
                }};

    View.OnClickListener b_call2_OnClickListener =
            new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "call2 clicked", Toast.LENGTH_LONG).show();

                }};

}
