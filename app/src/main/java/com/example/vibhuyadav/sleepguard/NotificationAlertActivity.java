package com.example.vibhuyadav.sleepguard;

import android.app.Activity;


/**
 * Created by zohaibakmal on 2/17/2015.
 */
public class NotificationAlertActivity extends Activity {

/*    private static final String LOG_TAG = "SMSReceiver";
    public static final int NOTIFICATION_ID_RECEIVED = 0x1221;
    static final String ACTION = "com.example.vibhuyadav.sleepguard.notifications";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter(ACTION);
        this.registerReceiver(mReceivedNotification, filter);
    }


    private void displayAlert(String notification) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(notification).setCancelable(
                false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private final BroadcastReceiver mReceivedNotification = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");

            displayAlert(message);
        }
    };*/
}
