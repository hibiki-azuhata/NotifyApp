package com.gmail.udonnikomi.notify.services.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;

import com.gmail.udonnikomi.notify.services.Database;
import com.gmail.udonnikomi.notify.services.dao.ItemDao;
import com.gmail.udonnikomi.notify.workers.LocationWorker;

import io.reactivex.rxjava3.schedulers.Schedulers;

public class UpdateStatusReceiver extends BroadcastReceiver {

    public static final String REMOVE_KEY = "NOTIFICATION_UPDATE_STATUS_RECEIVER_REMOVE";
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println(intent.getIntExtra(REMOVE_KEY, 0));
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(LocationWorker.NOTIFICATION_TAG, intent.getIntExtra(REMOVE_KEY, 0));
        ItemDao idao = Database.getInstance(context).itemDao();
        idao.updateStatusAll(false).subscribeOn(Schedulers.newThread()).blockingAwait();
    }
}
