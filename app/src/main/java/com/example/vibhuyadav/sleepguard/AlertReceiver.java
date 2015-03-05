package com.example.vibhuyadav.sleepguard;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import dartmouth.edu.sleepguard.util.Constants;

/**
 * Created by Zohaib Akmal on 2015/2/14 0014.
 */
public class AlertReceiver extends BroadcastReceiver {
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onReceive(Context context, Intent intent) {
        Log.d("Receiver", "Alert!!!!!!!!!!!");
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String svcName=Context.NOTIFICATION_SERVICE;
        NotificationManager notificationManager;
        notificationManager = (NotificationManager) context.getSystemService(svcName);

        int icon=R.drawable.abc_ic_menu_paste_mtrl_am_alpha;
        String tickerText="Noise Alert";
        long when=System.currentTimeMillis();
        Notification.Builder builder=new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.ic_launcher)
               .setContentText("Your roommate is Sleeping")
               .setContentTitle("Noise Alert!!!")
               .setSound(soundUri)
               .setWhen(when);
        Notification notification=builder.build();

        int NOTIFICATION_REF=1;
        notificationManager.notify(NOTIFICATION_REF,notification);
        Log.d(Constants.SleepGuardTag,"Calling Notfication Service");
        Intent i = new Intent(context, NotificationService.class);
        context.startService(i);
    }
}
