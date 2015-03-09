package dartmouth.edu.sleepguard.util;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Created by zohaibakmal on 2/12/2015.
 */
public class Constants {
    public static final String SleepGuardTag = "Sleep Guard";
    public static final String SleepGuardBackGroundServiceTag = "Sleep Guard Background Service";
    public static final String UserEndPointAsyncTaskTag = "UserEndPointAsyncTask";

    // Keys for storing activity state in the Bundle.
    public final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    public final static String LOCATION_KEY = "location-key";
    public final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";


    public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String TAG = "SleepGuard";
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String PROPERTY_APP_VERSION = "appVersion";
    public static final String SENDER_ID = "530139348531";
    public static final String NOISE_ALERT ="com.example.vibhuyadav.sleepguard.ALERT";

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */

    /*
    * Shared Preferences Stuff
    * */

    public static final String SHARED_PREFERENCES_FILE = "UserPreferences";
    public static final String MY_SLEEP_STATUS = "MySleepStatus";
    public static final String MY_DEVICE_ID = "DeviceId";
    public static final String MY_LONGITUDE= "LONGITUDE";
    public static final String MY_LATITUDE = "LATITUDE";
    public static final String MY_AVERAGE = "AVERAGE";

    public static final String USER_INSERT_TASK = "TASK_INSERT_USER";
    public static final String USER_UPDATE_TASK = "USER_UPDATE_TASK";
    public static final String USER_REMOVE_TASK = "USER_REMOVE_TASK";
    public static final String USER_RESONSE = "USER_RESPONSE";
    public static final String ID_OF_REQUESTING_DEVICE="ID of requesting device";
    public static final String REQUESTING_TIMESTAMP="Requesting timeStamp";
    public static final String IS_ON_REQUEST="Is on request";

//    public static final String SERVER_ADDRESS = "http://10.31.252.88:8080/_ah/api/";
//    public static final String MACHINE_ADDRESS = "http://10.31.252.88:8080/_ah/api/";

    public static final String SERVER_ADDRESS = "https://praxis-practice-856.appspot.com/_ah/api/";
    public static final String MACHINE_ADDRESS = "https://praxis-practice-856.appspot.com/_ah/api/";

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    public static final String PACKAGE_NAME = "com.google.android.gms.location.Geofence";

    public static final String SHARED_PREFERENCES_NAME = PACKAGE_NAME + ".SHARED_PREFERENCES_NAME";

    public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";

    /**
     * Used to set an expiration time for a geofence. After this amount of time Location Services
     * stops tracking the geofence.
     */
    public static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;

    /**
     * For this sample, geofences expire after twelve hours.
     */
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
    public static final float GEOFENCE_RADIUS_IN_METERS = 1609; // 1 mile, 1.6 km

    /**
     * Map for storing information about airports in the San Francisco bay area.
     */
    public static final HashMap<String, LatLng> BAY_AREA_LANDMARKS = new HashMap<String, LatLng>();
    static {
        // San Francisco International Airport.
        BAY_AREA_LANDMARKS.put("SFO", new LatLng(37.621313, -122.378955));

        // Googleplex.
        BAY_AREA_LANDMARKS.put("GOOGLE", new LatLng(37.422611,-122.0840577));
    }


}
