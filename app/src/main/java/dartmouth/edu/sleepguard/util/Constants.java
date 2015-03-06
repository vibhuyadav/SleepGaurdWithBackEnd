package dartmouth.edu.sleepguard.util;

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
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

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

    public static final String USER_INSERT_TASK = "TASK_INSERT_USER";
    public static final String USER_UPDATE_TASK = "USER_UPDATE_TASK";

//    public static final String SERVER_ADDRESS = "http://10.31.248.113:8080/_ah/api/";
//    public static final String MACHINE_ADDRESS = "http://10.31.248.113:8080/_ah/api/";

    public static final String SERVER_ADDRESS = "https://praxis-practice-856.appspot.com/_ah/api/";
    public static final String MACHINE_ADDRESS = "https://praxis-practice-856.appspot.com/_ah/api/";

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;


}
