package com.lacourt.mapscase.firebasemessage;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.inlocomedia.android.engagement.InLocoEngagement;
import com.inlocomedia.android.engagement.PushMessage;
import com.inlocomedia.android.engagement.request.FirebasePushProvider;
import com.inlocomedia.android.engagement.request.PushProvider;
import com.lacourt.mapscase.R;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Random;

public class AppsMessageService extends FirebaseMessagingService {
    public static final String TAG = "mymgstag";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        final Map<String, String> data = remoteMessage.getData();

        if (data != null) {
            // Decoding the notification data HashMap
            final PushMessage pushContent = InLocoEngagement.decodeReceivedMessage(this, data);

            if (pushContent != null) {
                // Presenting the notification
                presentInLocoNotification(pushContent);
            } else {
                // It's your regular message. Do as you used to do.
                presentRegularFCMNotification(remoteMessage);
            }
        }


    }

    private void presentRegularFCMNotification(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null)
            showNotification(
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody()
            );

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "AppsMessageService, Message data payload: " + remoteMessage.getData());
        }
    }

    private void presentInLocoNotification(PushMessage pushContent) {
        InLocoEngagement.presentNotification(
                this, // Context
                pushContent,  // The notification message hash
                R.mipmap.ic_launcher, // The notification icon drawable resource to display on the status bar. Put your own icon here. You can also use R.drawable.ic_notification for testing.
                1111111  // Optional: The notification identifier
        );
    }

    private void showNotification(String title, String body) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final String NOTIFICATION_CHANNEL_ID = "com.lacourt.mapscase.message";

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Nofitication", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("description I had set myself");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0L, 1000L, 500L, 1000L});
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Info");

        if (notificationManager != null) {
            notificationManager.notify((new Random()).nextInt(), notificationBuilder.build());
        }
    }

    public void onNewToken(@NotNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "AppsMessageService, Refreshed token: " + token);
        if (!token.isEmpty()) {
            final PushProvider pushProvider = new FirebasePushProvider.Builder()
                    .setFirebaseToken(token)
                    .build();
            Log.d(TAG, "AppsMessageService, Firebase pushProvider: " + pushProvider);
            InLocoEngagement.setPushProvider(this, pushProvider);
        }
    }
}

