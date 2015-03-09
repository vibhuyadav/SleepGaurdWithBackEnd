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

import backend.sleepguard.vibhuyadav.example.com.usereEndpoint.UsereEndpoint;
import backend.sleepguard.vibhuyadav.example.com.usereEndpoint.model.User;
import dartmouth.edu.sleepguard.util.Constants;

public class UserEndpointsAsyncTask extends AsyncTask<String, Void, User> {
    private static UsereEndpoint myApiService = null;
    private Context context;
    UserPreferences mUserPreferences;

    UserEndpointsAsyncTask(Context context) {
        this.context = context;
        mUserPreferences = new UserPreferences(context);
    }

    @Override
    protected User doInBackground(String... taskType) {
        if (myApiService == null) { // Only do this once
            UsereEndpoint.Builder builder = new UsereEndpoint.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
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

        User mUser = new User();
        mUser.setDeviceId(mUserPreferences.getMyDeviceId());
        mUser.setStatus(mUserPreferences.getMySleepStatus());
        mUser.setLatitude(mUserPreferences.getMyLatitude());
        mUser.setLongitude(mUserPreferences.getMyLongitude());

        if (taskType[0].equals(Constants.USER_INSERT_TASK)) {
            try {
                //return myApiService.listUser().execute().getItems();

                Log.d(Constants.UserEndPointAsyncTaskTag, "About to Insert");
                User temp;
                temp = myApiService.insertUser(mUser).execute();
                return temp;
            } catch (IOException e) {
                Log.d(Constants.UserEndPointAsyncTaskTag, e.getMessage());
                return null;
            }
        } else if (taskType[0].equals(Constants.USER_UPDATE_TASK)) {
            try {
                //return myApiService.listUser().execute().getItems();

                Log.d(Constants.UserEndPointAsyncTaskTag, "About to Update");
                User temp;
                temp = myApiService.updateUser(mUser).execute();
                return temp;
            } catch (IOException e) {
                Log.d(Constants.UserEndPointAsyncTaskTag, e.getMessage());
                return null;
            }
        }

        else if (taskType[0].equals(Constants.USER_REMOVE_TASK)) {
            try {
                //return myApiService.listUser().execute().getItems();

                Log.d(Constants.UserEndPointAsyncTaskTag, "About to Remove User");
                myApiService.removeUser(mUser.getDeviceId()).execute();
                return null;
            } catch (IOException e) {
                Log.d(Constants.UserEndPointAsyncTaskTag, e.getMessage());
                return null;
            }
        }

        else if(taskType[0].equals(Constants.USER_RESONSE)){
            try {
                //return myApiService.listUser().execute().getItems();

                Log.d(Constants.UserEndPointAsyncTaskTag, "About to Send Response");
                User temp;
                temp = myApiService.updateUser(mUser).execute();
                return temp;
            } catch (IOException e) {
                Log.d(Constants.UserEndPointAsyncTaskTag, e.getMessage());
                return null;
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(User result) {
        Log.d(Constants.UserEndPointAsyncTaskTag, "in Post Execute");
        //         Toast.makeText(context, result.getDeviceId() + " : " + result.getStatus(), Toast.LENGTH_LONG).show();

    }
}