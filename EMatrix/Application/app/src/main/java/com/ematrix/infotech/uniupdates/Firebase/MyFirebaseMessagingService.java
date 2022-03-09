package com.ematrix.infotech.uniupdates.Firebase;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.ematrix.infotech.uniupdates.Activity.ActivityLetestUpdatesDetails;
import com.ematrix.infotech.uniupdates.R;
import com.franmontiel.fcmnotificationhandler.RemoteMessageNotifier;
import com.franmontiel.fcmnotificationhandler.RemoteMessageToNotificationMapper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "Bestmarts";
    private static final String CHANNEL_NAME = "Bestmarts";
    private NotificationManager mManager;
    private NotificationCompat.Builder notificationBuilder = null;
    private NotificationManager notificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //new RemoteMessageNotifier(getApplicationContext()).notify(remoteMessage);
        try {
            if (remoteMessage.getData() != null) {
                Log.d("Data : ", "" + remoteMessage.getData());
                String message = remoteMessage.getData().get("body");
                String post_id = remoteMessage.getData().get("icon");
                Log.d("message", "" + message);
                sendNotification(message, post_id);
            } else if (remoteMessage.getNotification() != null) {
                Log.d("Notification : ", "" + remoteMessage.getNotification());
                String message = remoteMessage.getNotification().getBody();
                String post_id = remoteMessage.getNotification().getIcon();
                Log.d("message", "" + message);
                sendNotification(message, post_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //This method is only generating push notification_icon
    //It is same as we did in earlier posts
    @SuppressLint("WakelockTimeout")
    public void sendNotification(String message, String post_id) {
        Log.d("post_id", "" + post_id);
        Intent intent = new Intent(this, ActivityLetestUpdatesDetails.class);
        intent.putExtra("PostID", post_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), new Random().nextInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        String notificationChannelId = getApplicationContext().getPackageName();
        String notificationChannelName = getApplicationContext().getString(R.string.app_name);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(
                    notificationChannelId, notificationChannelName, NotificationManager.IMPORTANCE_HIGH
            );

            //notificationChannel.setDescription("no sound");
            //notificationChannel.setSound(null,null);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);

            getManager().createNotificationChannel(notificationChannel);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notificationBuilder = new NotificationCompat.Builder(this, notificationChannelId)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setTicker(message)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pendingIntent);
        } else {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notificationBuilder = new NotificationCompat.Builder(this, notificationChannelId)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setTicker(message)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pendingIntent);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        notificationManager.notify(NotificationID.getID(), notificationBuilder.build());
        {
            // Wake Android Device when notification_icon received
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            @SuppressLint("InvalidWakeLockTag") @SuppressWarnings("deprecation") final PowerManager.WakeLock mWakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "FCM_PUSH");
            mWakelock.acquire();

            // Timer before putting Android Device to sleep mode.
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    mWakelock.release();
                }
            };
            timer.schedule(task, 5000);
        }
    }

    private NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN = = == = = =", s);
    }

    public static class NotificationID {
        private final static AtomicInteger c = new AtomicInteger(0);
        private static int UniqID = 0;

        public static int getID() {
            UniqID = c.incrementAndGet();
            Log.d("UniqID", "" + UniqID);
            return UniqID;
        }

    }
}