package com.example.vibhuyadav.sleepguard;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.view.WindowManager.LayoutParams;

/**
 * Created by zohaibakmal on 3/3/2015.
 */
public class NotificationService extends Service {

    private WindowManager windowManager;
    private ImageView chatHead2;
    LayoutParams params;
    LayoutParams params2;
    View notificationView;
    LayoutInflater mInflater;
    CircularImageView circularImageView;
    Button mCloseButton;
    int screenWidth;
    public static final int NOTIFICATION_ID = 1;
    int screenHeight;
    NotificationManager mNotificationManager;
    UserPreferences mUserPref;

    @Override public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    @Override public void onCreate() {
        super.onCreate();
        mUserPref = new UserPreferences(getApplicationContext());

    }

    private void sendNotification() {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("SleepGuard")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Your Roomate is trying to sleep"))
                        .setContentText("Your Roomate is trying to sleep")
                        .setSound(soundUri);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        //sendNotification();

        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;



        LayoutInflater  mInflater = (LayoutInflater)this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        notificationView = mInflater.inflate(R.layout.floating_notification, null, false);
        circularImageView = (CircularImageView)notificationView.findViewById(R.id.profile_picture);
        circularImageView.setImageResource(R.drawable.ic_launcher);
        mCloseButton = (Button)notificationView.findViewById(R.id.delete_button);
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //windowManager.removeView(notificationView);
           //     mUserPref.setNotificationServiceOn(false);
                stopSelf();
            }
        });

        chatHead2 = new ImageView(this);
        chatHead2.setImageResource(R.drawable.ic_launcher);


        params = new LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        params2 = new LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,

                PixelFormat.TRANSLUCENT);

        params2.gravity = Gravity.BOTTOM | Gravity.CENTER;

        windowManager.addView(notificationView, params);

        notificationView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                    //    windowManager.addView(chatHead2, params2);

                        return true;
                    case MotionEvent.ACTION_UP:
  /*                    //  windowManager.removeView(chatHead2);
                        if (Math.abs(params.x - screenWidth/2) < 100 && Math.abs(params.y - screenHeight) < 100){
                            Log.d("NotificationService", "Outside Action");

                            windowManager.removeView(notificationView);
                            return true;
                        }*/
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        if (params.x < 0){
                            params.x = 0;
                        }
                        if (params.x > screenWidth){
                            params.x = screenWidth;
                        }
                        if (params.y< 0){
                            params.y = 100;
                        }
                        if (params.y > screenHeight){
                            params.y = screenHeight;
                        }
//					Log.d("NotificationService", "Param.x = " + params.x+ "Width = "+ screenWidth+ " Height = "+ screenHeight);
                        //Log.d("NotificationService", "params.x - screenWidth/2 = " +(params.x - screenWidth/2) + " params.y - screenHeight: = "+ (params.y - screenHeight));


                        windowManager.updateViewLayout(notificationView, params);
                        return true;
                    case MotionEvent.ACTION_HOVER_EXIT:
                        Log.d("NotificationService", "Outside Action");
                     //   windowManager.removeView(notificationView);
                        return true;
                }
                return false;
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (notificationView != null) windowManager.removeView(notificationView);
    }




}