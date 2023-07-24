package com.gmail.udonnikomi.notify.services.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;

import com.gmail.udonnikomi.notify.workers.LocationWorker;

public class RemoveNotificationReceiver extends BroadcastReceiver {

    public static final String REMOVE_KEY = "REMOVE_NOTIFICATION_RECEIVER_REMOVE";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(LocationWorker.NOTIFICATION_TAG, intent.getIntExtra(REMOVE_KEY, 0));
    }
}