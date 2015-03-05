package com.example.vibhuyadav.sleepguard;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.vibhuyadav.sleepguard.backend.messaging.Messaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

import dartmouth.edu.sleepguard.util.Constants;

/**
 * Created by WeiHuang on 3/3/2015.
 */
public class NoiseSleepAsyncTask extends AsyncTask<String[], Void, String> {
    private static Messaging sleepReceiverApi = null;
    Context mContext;
    UserPreferences mUserPreferences;


    NoiseSleepAsyncTask(Context context){
        mContext = context;
        mUserPreferences = new UserPreferences(context);
    }

    @Override
    protected String doInBackground(String[]... params) {
        if (sleepReceiverApi == null) {
            Messaging.Builder builder = new Messaging.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(Constants.MACHINE_ADDRESS)
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });

            // end of optional local run code
            //Vibhu:"https://praxis-practice-856.appspot.com/_ah/api/"
            //Wei  :"https://stable-synapse-857.appspot.com/_ah/api/"
            sleepReceiverApi = builder.build();
        }
        String regId=params[0][0];
        Log.d("params [0][0]",params[0][0]);

        String timeStamp=params[0][1];
        Log.d("params [0][1]",params[0][1]);

        try {
            sleepReceiverApi.messagingEndpoint().sendMessage(timeStamp).execute();
            //sleepReceiverApi.(regId,timeStamp).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Successful";
    }
}
