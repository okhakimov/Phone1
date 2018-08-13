package com.example.oleg.phone1;

import android.content.Context;
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
        //String phone = params.Phones[global_i[count]][1];
        //Log.d("=== r ",phone);
        if (test) {
            Toast.makeText(context, "== test sms " + phone + " " + message, Toast.LENGTH_LONG).show();
        } else {
            //send sms
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phone, null, message, null, null);
            Toast.makeText(context, "== sms " + phone + " " + message, Toast.LENGTH_LONG).show();

        }
        MediaPlayer mp = MediaPlayer.create(context, R.raw.click1);
        mp.start();
    }
}