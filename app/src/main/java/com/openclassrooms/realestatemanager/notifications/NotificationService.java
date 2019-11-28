package com.openclassrooms.realestatemanager.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.openclassrooms.realestatemanager.FlatDetailActivity;
import com.openclassrooms.realestatemanager.MainActivity;
import com.openclassrooms.realestatemanager.R;

public class NotificationService {

    private static final String CHANNEL_ID = "1";
    final String FLATID = "flatId";
    final String SELECTEDFLAT = "selectedFlat";

    public void createNotification(Context context, String summary, long flatId, boolean isTablet) {
        Intent intent;

        if (isTablet) {
            Bundle args = new Bundle();
            args.putLong(FLATID, flatId);
            args.putInt(SELECTEDFLAT, (int) flatId);
            intent = new Intent(context, MainActivity.class);
            intent.putExtras(args);
        } else {
            Bundle args = new Bundle();
            args.putLong(FLATID, flatId);
            intent = new Intent(context, FlatDetailActivity.class);
            intent.putExtras(args);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,  PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getResources().getString(R.string.notification_title))
                .setContentText(context.getResources().getString(R.string.notification_text, summary))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        int notificationId = (int) flatId;
        notificationManager.notify(notificationId, builder.build());
    }
}
