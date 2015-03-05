package com.example.vibhuyadav.sleepguard;

import android.content.Context;
import android.content.SharedPreferences;

import dartmouth.edu.sleepguard.util.Constants;

/**
 * Created by zohaibakmal on 3/3/2015.
 */
public class UserPreferences {

    private SharedPreferences.Editor editor;
    SharedPreferences sharedPref;
    Boolean mSleepStatus;
    String mDeviceId;

    public UserPreferences (Context context){
        sharedPref = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
    }

    public Boolean getMySleepStatus() {
        return sharedPref.getBoolean(Constants.MY_SLEEP_STATUS, false);
    }
    public void setMyStatus(Boolean mSleepStatus) {
        editor = sharedPref.edit();
        editor.putBoolean(Constants.MY_SLEEP_STATUS, mSleepStatus);
        editor.commit();
    }

    public String getMyDeviceId() {
        return sharedPref.getString(Constants.MY_DEVICE_ID, "None");
    }
    public void setMyDeviceId(String mDeviceId) {
        editor = sharedPref.edit();
        editor.putString(Constants.MY_DEVICE_ID, mDeviceId);
        editor.commit();
    }

}
