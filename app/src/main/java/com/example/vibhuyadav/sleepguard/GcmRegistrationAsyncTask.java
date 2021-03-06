package com.example.vibhuyadav.sleepguard;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vibhuyadav.sleepguard.backend.registration.Registration;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import dartmouth.edu.sleepguard.util.Constants;

/**
 * Created by vibhuyadav on 2/16/15.
 */
public class GcmRegistrationAsyncTask extends AsyncTask<Void, Void, String> {
    private static Registration regService = null;
    private GoogleCloudMessaging gcm;
    private Context context;
    UserPreferences mUserPreferences ;
    // TODO: change to your own sender ID to Google Developers Console project number, as per instructions above
    private static final String SENDER_ID = "530139348531";//Vibhu:"530139348531" Wei:"454255453333"

    public GcmRegistrationAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        mUserPreferences = new UserPreferences(context);
        if (regService == null) {
            Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    //.setRootUrl("https://stable-synapse-857.appspot.com/_ah/api/");
                    .setRootUrl(Constants.SERVER_ADDRESS)
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                @Override
                public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                    abstractGoogleClientRequest.setDisableGZipContent(true);
                }
            });
            // end of optional local run code
            //Vibhu:"https://praxis-practice-856.appspot.com/_ah/api/"
            //Wei  :"https://stable-synapse-857.appspot.com/_ah/api/"
            regService = builder.build();
        }

        String msg = "";
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            String regId = gcm.register(SENDER_ID);
            msg = regId;
            new UserEndpointsAsyncTask(context).execute(Constants.USER_INSERT_TASK);
            mUserPreferences.setMyDeviceId(regId);
            // You should send the registration ID to your server over HTTP,
            // so it can use GCM/HTTP or CCS to send messages to your app.
            // The request to your server should be authenticated if your app
            // is using accounts.
            regService.register(regId).execute();

        } catch (IOException ex) {
            ex.printStackTrace();
            msg = "Error: " + ex.getMessage();
        }
        return msg;
    }

    @Override
    protected void onPostExecute(String msg) {
       // Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

        Logger.getLogger("REGISTRATION").log(Level.INFO, msg);
    }
}
