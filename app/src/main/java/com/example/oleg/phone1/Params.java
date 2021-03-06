package com.example.oleg.phone1;

/*
 * Phone with large buttons for calls and sms. Created by oleg on 7/21/18.
 */

// read file with parametes and create an object
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Params
{
    //public String[][] Phones = new String [30][2] ;
    public    HashMap<String, String>  PhoneNames = new HashMap<String, String>();
    public    HashMap<String, String>  PhoneNumbers = new HashMap<String, String>();
    public Map<Integer, List<String>> SmsIds = new HashMap<Integer, List<String>>();


    public String[] CallId = new String [30] ;
    public String[] CallColors = new String [30] ;
    public String[] SmsColors = new String [30] ;
    public String[] SmsLabels = new String [30] ;
    public String[] SmsMessages = new String [30] ;
    //public Integer[][] SmsIds = new Integer [10][20] ;
    public String msg = "ok";
    public String HelpMessage = "";
    public    HashMap<String, String>  ColorCodes = new HashMap<String, String>();
    public    HashMap<String, String>  Options = new HashMap<String, String>();

    public Params () {
    }
    public void readConfig(InputStream in_file) {

        //File in_file = new File(cust_conf_name);

        ColorCodes.put("red", "#ff6d18");
        ColorCodes.put("green", "#41ba7a");
        ColorCodes.put("blue", "#5e9aff");
        ColorCodes.put("orange", "#ffe85f");
        ColorCodes.put("yellow", "#ddff31");
        ColorCodes.put("pink", "#f67eff");
        ColorCodes.put("grey", "#c8c8c8");

        // default values
        Options.put("test", "0");
        Options.put("sms_delay", "0");


        try {
            //BufferedReader br = new BufferedReader(new FileReader(in_file));
            BufferedReader br = new BufferedReader(new InputStreamReader(in_file));
            String line;
            int line_nb = 1;
            while ((line = br.readLine()) != null) {
                //replace multiple spaces by a single space
                line = line.trim().replaceAll(" +", " ");
                //Log.d("=== r",line);
                msg = parseInput(line);
                if (!msg.equals("ok")) {
                    msg=msg+" line "+Integer.toString(line_nb);
                    break;
                }
                line_nb = line_nb + 1;
            }
            br.close();
        }

        catch (IOException e) {
            msg="-- error: can't read file";
            Log.d("==", e.getMessage());
        }

    }

    public String parseInput(String line) {
        String msg = "ok";
        //System.out.println("==parse "+line);
        String[] a_line = line.split(" ");
        Integer n;
        switch (a_line[0] ) {
            case "o" :
                // options
                Options.put(a_line[1],a_line[2]);
                break;
            case "h" :
                // text message
                HelpMessage = line.substring(1);
                break;
            case "p" :
                //System.out.println("== p");
                PhoneNames.put(a_line[1],a_line[2]);
                PhoneNumbers.put(a_line[1],a_line[3]);

                break;
            case "c" :
                n = Integer.parseInt(a_line[1]);

                    CallId[n]  = a_line[2];
                    if (ColorCodes.get(a_line[3]) != null) {
                        CallColors[n] = ColorCodes.get(a_line[3]);
                    } else {
                        CallColors[n]  = a_line[3];
                    }
                break;

            case "s" :
                n = Integer.parseInt(a_line[1]);
                    SmsIds.put(n, Arrays.asList(a_line[2].split(",")));
                    if (ColorCodes.get(a_line[3]) != null) {
                        SmsColors[n] = ColorCodes.get(a_line[3]);
                    } else {
                        SmsColors[n]  = a_line[3];
                    }
                    SmsLabels[n]  = a_line[4];
                    SmsMessages[n]  = a_line[5];

            default:
                break;
        }

        return msg;
    }
}
