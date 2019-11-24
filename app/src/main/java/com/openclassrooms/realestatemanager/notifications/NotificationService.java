package com.openclassrooms.realestatemanager.notifications;

import android.content.Context;
import android.media.RingtoneManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.openclassrooms.realestatemanager.R;

public class NotificationService {

    private static final String CHANNEL_ID = "1";

    public void createNotification(Context context, String summary) {
//        Intent intent = new Intent(this, FlatDetailActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getResources().getString(R.string.notification_title))
                .setContentText(context.getResources().getString(R.string.notification_text, summary))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                ;

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        int notificationId = 1;
        notificationManager.notify(notificationId, builder.build());
    }
}
