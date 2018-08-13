package com.example.oleg.phone1;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by oleg on 8/12/18.
 */


public class SmsRunnable implements Runnable {
    private Context context;
    private String phone;
    private String message;
    private Boolean test;
    public SmsRunnable(Context context, String phone, String message, Boolean test) {
        this.phone = phone;
        this.message = message;
        this.context = context;
        this.test = test;
    }
    @Override
    public void run() {
        String sent = "SMS_SENT";
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                new Intent(sent), 0);

        //---when the SMS has been sent---
        context.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                if(getResultCode() == Activity.RESULT_OK)
                {
                    Toast.makeText(context, "SMS sent",
                            Toast.LENGTH_SHORT).show();
                    MediaPlayer mp_sms_sent = MediaPlayer.create(context, R.raw.bell1);
                    mp_sms_sent.start();
                }
                else
                {
                    Toast.makeText(context, "SMS could not sent",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }, new IntentFilter(sent));



        //String phone = params.Phones[global_i[count]][1];
        //Log.d("=== r ",phone);
        if (test) {
            Toast.makeText(context, "== test sms " + phone + " " + message, Toast.LENGTH_LONG).show();
        } else {
            //send sms
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phone, null, message, sentPI, null);
            Toast.makeText(context, "== sms " + phone + " " + message, Toast.LENGTH_LONG).show();

        }
        MediaPlayer mp = MediaPlayer.create(context, R.raw.click1);
        mp.start();
    }
}