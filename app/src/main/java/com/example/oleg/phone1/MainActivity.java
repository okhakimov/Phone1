package com.example.oleg.phone1;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    //File sdcard = Environment.getExternalStorageDirectory();
    //public String cust_conf_name = sdcard+"/DCIM/PHONE/phone_list.txt";

    public String cust_conf_name = "";

    //File in_file = new File(sdcard,cust_conf_name);
    public Params params = new Params();

    // timers. in seconds to limit number of sms
    public long [] sms_t = {0,0,0,0};
    // minimal time interval between sms messages in seconds (900 = 15 min)
    public long sms_interval = 900;
    // global array to pass variables into inner classes
    public String [] global_s = new String [10];
    public Integer [] global_i = new Integer [10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String cust_conf_path = getFilesDir().toString();
        // custom configuration file
        cust_conf_name = cust_conf_path + "/custom_config.txt";

        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hide status bar. end
        //set landscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //set landscape orientation. end

        try {
            loadMainActivity();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
    private void loadMainActivity() throws FileNotFoundException {

        // read default config
        InputStream def_config = getResources().openRawResource(R.raw.default_config);
        params.readConfig(def_config);

        // read custom config
        //String cust_conf_path = getApplicationContext().getFilesDir().toString();
        //String cust_conf_name = cust_conf_path + "/custom_config.txt";
        Log.d("== f",cust_conf_name);
        //create custom_config file if it doens't exist
        File f = new File(cust_conf_name);
        if(!f.exists()){
            //f.createNewFile();
            InputStream fis = getResources().openRawResource(R.raw.default_config);
            OutputStream fos = new FileOutputStream(new File(cust_conf_name));
            try {
                copyFile(fis,fos);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("== f","file doesn't exits");
        }else{
            Log.d("== f","file exist");
        }

        try (InputStream cust_config = new FileInputStream(new File(cust_conf_name))) {
            //InputStream cust_config = new FileInputStream(new File(cust_conf_name));
            params.readConfig(cust_config);
        } catch (IOException e) {
            Log.d("==", e.getMessage());

        }

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
    // use volume down and up keys to open the configuration activity
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){
            //Do something
            LinearLayout lay = (LinearLayout) findViewById(R.id.sms_frame);
            ColorDrawable viewColor = (ColorDrawable) lay.getBackground();
            Integer colorId = viewColor.getColor();
            Log.d("==","volume down"+colorId.toString());
            // starting color is white = -1
            lay.setBackgroundColor(colorId+1);
        }
        return true;
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)){
            //Do something
            LinearLayout lay = (LinearLayout) findViewById(R.id.sms_frame);
            ColorDrawable viewColor = (ColorDrawable) lay.getBackground();
            Integer colorId = viewColor.getColor();
            Log.d("==","volume up"+colorId.toString());
            if (colorId == 2) {
                // reset the color to white = -1
                lay.setBackgroundColor(-1);
                Intent configIntent = new Intent(this, ConfigActivity.class);
                configIntent.putExtra("cust_conf_name", cust_conf_name);
                startActivity(configIntent);
            }
        }
        return true;
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
                        Handler handler1 = new Handler();

                        // sending sms with delay
                        int count = 0;
                        for (Integer sms_id: params.SmsIds[n]) {
                            if (sms_id != null) {
                                phone = params.Phones[sms_id][1];;
                                message = params.SmsMessages[n];
                                //Log.d("===", String.valueOf(sms_id));
                                handler1.postDelayed(
                                        new SmsRunnable(getApplicationContext(),
                                                phone
                                                ,message,
                                                params.Options.get("test").equals("1")),
                                        Integer.parseInt(params.Options.get("sms_delay"))*1000 * count);
                            }
                            count += 1;
                        }
                        // click sound
                        //MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.click1);
                        //mp.start();
                        // end sending sms with delay
                    }

                }};

}
