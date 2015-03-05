package com.example.vibhuyadav.sleepguard;

import android.os.AsyncTask;
import android.util.Log;

import com.example.vibhuyadav.sleepguard.backend.sleepReceiverApi.SleepReceiverApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

/**
 * Created by WeiHuang on 3/3/2015.
 */
public class NoiseSleepAsyncTask extends AsyncTask<String[], Void, String> {
    private static SleepReceiverApi sleepReceiverApi = null;

    @Override
    protected String doInBackground(String[]... params) {
        if (sleepReceiverApi == null) {
            SleepReceiverApi.Builder builder = new SleepReceiverApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://praxis-practice-856.appspot.com/_ah/api/");

            // end of optional local run code
            //Vibhu:"https://praxis-practice-856.appspot.com/_ah/api/"
            //Wei  :"https://stable-synapse-857.appspot.com/_ah/api/"
            sleepReceiverApi = builder.build();
        }
        String timeStamp=params[0][0];
        Log.d("params [0][0]",params[0][0]);

        String regId=params[0][1];
        Log.d("params [0][1]",params[0][1]);

        try {
            sleepReceiverApi.sendTimeStamp(timeStamp,regId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Successful";
    }
}
