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

    public UserPreferences (Context context){
        sharedPref = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
    }

    public Boolean getMySleepStatus() {
        return sharedPref.getBoolean(Constants.MY_SLEEP_STATUS, false);
    }
    public void setMyId(Boolean mSleepStatus) {
        editor = sharedPref.edit();
        editor.putBoolean(Constants.MY_SLEEP_STATUS, mSleepStatus);
        editor.commit();
    }

}
