package com.gmail.udonnikomi.notify.ui.settings;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.preference.PreferenceFragmentCompat;
import androidx.work.ListenableWorker;

import com.gmail.udonnikomi.notify.MovementNotify;
import com.gmail.udonnikomi.notify.R;
import com.gmail.udonnikomi.notify.entities.Item;
import com.gmail.udonnikomi.notify.services.Database;
import com.gmail.udonnikomi.notify.services.Preference;
import com.gmail.udonnikomi.notify.services.dao.ItemDao;
import com.gmail.udonnikomi.notify.services.receivers.RemoveNotificationReceiver;
import com.gmail.udonnikomi.notify.services.receivers.UpdateStatusReceiver;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SharedPreferences.OnSharedPreferenceChangeListener listener = ((sharedPreferences, key) -> {
        if(getContext() != null) {
            if (key.equals(Preference.Key.NOTIFY_RANGE.toString())) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat(Preference.Key.NOTIFY_RANGE.toString(), 5.0f);
                editor.commit();
            }
        }
    });

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        addPreferencesFromResource(R.xml.settings);
        if(getActivity() != null && getActivity() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if(actionBar != null) {
                actionBar.setTitle(R.string.title_settings);
                actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A0CCC6")));
            }
            if(getContext() != null) {
                ImageViewCompat.setImageTintList(getActivity().findViewById(R.id.fab_notifications), ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.gray)));
            }
        }

        /* ======================================== */

        final String CHANNEL_ID = "MovementNotifyChannel";
        final String NOTIFICATION_TAG = "MovementNotifyTag";
        String ACTION_SNOOZE = "MovementNotifyActionSnooze";
        String ACTION_COMPLETE = "MovementNotifyActionComplete";
        String ACTION_START = "MovementNotifyActionStart";
        int NOTIFICATION_ID = 12345;


        Intent snoozeIntent = new Intent(getContext(), RemoveNotificationReceiver.class);
        snoozeIntent.setAction(ACTION_SNOOZE);
        snoozeIntent.putExtra(RemoveNotificationReceiver.REMOVE_KEY, NOTIFICATION_ID);
        PendingIntent snoozePending = PendingIntent.getBroadcast(getContext(), 100, snoozeIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent startIntent = new Intent(getContext(), MovementNotify.class);
        startIntent.setAction(ACTION_START);
        startIntent.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                        | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        );
        PendingIntent startPending = PendingIntent.getActivity(getContext(), 200, startIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent finishIntent = new Intent(getContext(), UpdateStatusReceiver.class);
        finishIntent.setAction(ACTION_COMPLETE);
        finishIntent.putExtra(UpdateStatusReceiver.REMOVE_KEY, NOTIFICATION_ID);
        PendingIntent finishPending = PendingIntent.getBroadcast(getContext(), 300, finishIntent, PendingIntent.FLAG_IMMUTABLE);

        ItemDao idao = Database.getInstance(getContext()).itemDao();
        Item[] items = idao.findAllByStatus().blockingFirst();
        if (items.length == 0) return ;
        StringBuilder content = new StringBuilder();
        for(int i = 0; i < items.length; i++) {
            content.append(items[i].name);
            if(i < items.length - 1) content.append(", ");
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_icon_foreground)
                .setContentTitle(getContext().getString(R.string.title_notifications))
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(Notification.DEFAULT_ALL)
                .addAction(R.drawable.ic_home_black_24dp, getContext().getString(R.string.notify_snooze), snoozePending)
                .addAction(R.drawable.ic_home_black_24dp, getContext().getString(R.string.notify_start), startPending)
                .addAction(R.drawable.ic_dashboard_black_24dp, getContext().getString(R.string.notify_complete), finishPending);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        if(sp != null) sp.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        if(sp != null) sp.unregisterOnSharedPreferenceChangeListener(listener);
    }
}