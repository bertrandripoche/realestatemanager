package com.openclassrooms.realestatemanager.notifications;

import android.content.Context;
import android.media.RingtoneManager;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.openclassrooms.realestatemanager.R;

public class NotificationService {

    private static final String CHANNEL_ID = "1";

    public void createNotification(Context context, String summary, long flatId) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getResources().getString(R.string.notification_title))
                .setContentText(context.getResources().getString(R.string.notification_text, summary))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                ;

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        int notificationId = (int) flatId;
        notificationManager.notify(notificationId, builder.build());
    }
}
