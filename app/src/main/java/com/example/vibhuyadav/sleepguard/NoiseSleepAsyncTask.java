package com.example.vibhuyadav.sleepguard;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;


import java.io.IOException;

import backend.sleepguard.vibhuyadav.example.com.messaging.Messaging;
import backend.sleepguard.vibhuyadav.example.com.messaging.model.Request;
import dartmouth.edu.sleepguard.util.Constants;

/**
 * Created by WeiHuang on 3/3/2015.
 */
public class NoiseSleepAsyncTask extends AsyncTask<String[], Void, String> {
    private static Messaging messagingAPI = null;
    Context mContext;
    UserPreferences mUserPreferences;


    NoiseSleepAsyncTask(Context context){
        mContext = context;
        mUserPreferences = new UserPreferences(context);
    }

    @Override
    protected String doInBackground(String[]... params) {
        if (messagingAPI == null) {
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
            messagingAPI = builder.build();
        }
        String regId=params[0][0];
        Log.d("params [0][0]",params[0][0]);

        String timeStamp=params[0][1];
        Long timeStampLong = Long.valueOf(timeStamp);
        Log.d("params [0][1]",params[0][1]);

        Request request = new Request();
        request.setDeviceId(mUserPreferences.getMyDeviceId());
        request.setLatitude(mUserPreferences.getMyLatitude());
        request.setLongitude(mUserPreferences.getMyLongitude());
        request.setTimeStamp(timeStampLong);
/*
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("timeStamp", timeStamp);
            jsonObject.put("longitude", mUserPreferences.getMyLongitude());
            jsonObject.put("latitude", mUserPreferences.getMyLatitude());
            jsonObject.put("regID", mUserPreferences.getMyDeviceId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

*/
        try {

            messagingAPI.messagingEndpoint().sendRequest(request).execute();
            //messagingAPI.(regId,timeStamp).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

      /*  try {
            messagingAPI.messagingEndpoint().sendRequest(jsonObject).execute();
            //messagingAPI.(regId,timeStamp).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return "Successful";
    }
}
