package com.example.vibhuyadav.sleepguard;


import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import backend.sleepguard.vibhuyadav.example.com.responseEndpoint.model.Response;
import dartmouth.edu.sleepguard.util.Constants;


public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    UserPreferences mUserPreferences;

    public GcmIntentService() {

        super("GcmIntentService");

    }

    public static final String TAG = "GCM Demo";


    @Override
    protected void onHandleIntent(Intent intent) {
        mUserPreferences = new UserPreferences(this.getApplication().getApplicationContext());
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

//        Intent dialogIntent = new Intent(getBaseContext(), NotificationAlertActivity.class);
//        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        dialogIntent.putExtra("servermessage", extras);
//        getApplication().startActivity(dialogIntent);


        if (!extras.isEmpty() && !extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
                Log.d("GCMIntentService", extras.getString("message"));
           //     showToast(extras.getString("message"));
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
//                for (int i = 0; i < 5; i++) {
//                    Log.i(TAG, "Working... " + (i + 1)
//                            + "/5 @ " + SystemClock.elapsedRealtime());
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                    }
//                }
            }
            Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
            // Post notification of received message.
            if (extras.getString("message_type").equals("request")){
                Response response = new Response();
                response.setMDeviceId(mUserPreferences.getMyDeviceId());
                response.setRequestId(extras.getString("request"));
                response.setAverage(mUserPreferences.getmAverage());
                new ResponseEndpointsAsyncTask(this.getApplicationContext()).execute(response);
            }
            if (extras.getString("message_type").equals("response")){
                sendNotification("Please Do not Disturb: " + extras.getString("message"));
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotification(final String message) {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String svcName=Context.NOTIFICATION_SERVICE;
        NotificationManager notificationManager;
        notificationManager = (NotificationManager) this.getApplicationContext().getSystemService(svcName);

        int icon=R.drawable.abc_ic_menu_paste_mtrl_am_alpha;
        String tickerText="Noise Alert";
        long when=System.currentTimeMillis();
        Notification.Builder builder=new Notification.Builder(this.getApplicationContext());
        builder.setSmallIcon(R.drawable.ic_launcher)
                .setContentText(message)
                .setContentTitle("Noise Alert!!!")
                .setSound(soundUri)
                .setWhen(when);
        Notification notification=builder.build();

        int NOTIFICATION_REF=1;
        notificationManager.notify(NOTIFICATION_REF,notification);
        Log.d(Constants.SleepGuardTag,"Calling Notfication Service");
        Intent i = new Intent(this.getApplicationContext(), NotificationService.class);
        this.getApplicationContext().startService(i);

//        DisturbingNotification disturbingNotification=new DisturbingNotification();
//        FragmentManager fm = getFragmentManager();
//        disturbingNotification.show(fm,"Notification!");
    }

    protected void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }
//public GcmIntentService() {
//    super("GcmIntentService");
//}
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        Bundle extras = intent.getExtras();
//        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
//        // The getMessageType() intent parameter must be the intent you received
//        // in your BroadcastReceiver.
//        String messageType = gcm.getMessageType(intent);
//
//        if (extras != null && !extras.isEmpty()) {  // has effect of unparcelling Bundle
//            // Since we're not using two way messaging, this is all we really to check for
//            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
//                Logger.getLogger("GCM_RECEIVED").log(Level.INFO, extras.toString());
//
//                showToast(extras.getString("message"));
//            }
//        }
//        GcmBroadcastReceiver.completeWakefulIntent(intent);
//    }
//
//    protected void showToast(final String message) {
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//            }
//        });
//    }
}