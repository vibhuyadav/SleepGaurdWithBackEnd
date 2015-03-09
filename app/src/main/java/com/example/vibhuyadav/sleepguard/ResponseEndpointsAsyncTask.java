package com.example.vibhuyadav.sleepguard;

/**
 * Created by zohaibakmal on 3/4/2015.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

import backend.sleepguard.vibhuyadav.example.com.responseEndpoint.ResponseEndpoint;
import backend.sleepguard.vibhuyadav.example.com.responseEndpoint.model.Response;
import dartmouth.edu.sleepguard.util.Constants;

public class ResponseEndpointsAsyncTask extends AsyncTask<Response, Void, Response> {
    private static ResponseEndpoint myApiService = null;
    private Context context;
    UserPreferences mUserPreferences;

    ResponseEndpointsAsyncTask(Context context) {
        this.context = context;
        mUserPreferences = new UserPreferences(context);
    }

    @Override
    protected Response doInBackground(Response... response) {
        if(myApiService == null) { // Only do this once
            ResponseEndpoint.Builder builder = new ResponseEndpoint.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl(Constants.MACHINE_ADDRESS)
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
// end options for devappserver

            myApiService = builder.build();
        }
        try {
            myApiService.insertResponse(response[0]).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Response result) {
            Log.d("ResponseAsyncTask", "in Post Execute");
   //         Toast.makeText(context, result.getDeviceId() + " : " + result.getStatus(), Toast.LENGTH_LONG).show();

    }
}