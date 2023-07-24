package com.gmail.udonnikomi.notify.workers;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.gmail.udonnikomi.notify.MovementNotify;
import com.gmail.udonnikomi.notify.R;
import com.gmail.udonnikomi.notify.entities.Item;
import com.gmail.udonnikomi.notify.entities.LocationData;
import com.gmail.udonnikomi.notify.services.Database;
import com.gmail.udonnikomi.notify.services.dao.ItemDao;
import com.gmail.udonnikomi.notify.services.dao.LocationDataDao;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class LocationWorker extends Worker {

    public static final String CHANNEL_ID = "MovementNotifyChannel";
    private String ACTION_SNOOZE = "MovementNotifyActionSnooze";
    private String ACTION_COMPLETE = "MovementNotifyActionComplete";

    public LocationWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        if (ActivityCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationDataDao lddao = Database.getInstance(getApplicationContext()).locationDataDao();
            Location loc = LocationServices.getFusedLocationProviderClient(getApplicationContext()).getLastLocation().getResult();
            LocationData newLocation = new LocationData();
            newLocation.currentTime = System.currentTimeMillis() / (15 * 60 * 1000);
            newLocation.longitude = loc.getLongitude();
            newLocation.latitude = loc.getLatitude();
            List<LocationData> locData = Arrays.asList(lddao.findAll());
            locData.add(newLocation);
            if(isMove(500d, locData)) {
                lddao.deleteAll();

                Intent snoozeIntent = new Intent(getApplicationContext(), MovementNotify.class);
                snoozeIntent.setAction(ACTION_SNOOZE);
                PendingIntent snoozePending = PendingIntent.getBroadcast(getApplicationContext(), 0, snoozeIntent, PendingIntent.FLAG_IMMUTABLE);

                Intent finishIntent = new Intent(getApplicationContext(), MovementNotify.class);
                finishIntent.setAction(ACTION_COMPLETE);
                PendingIntent finishPending = PendingIntent.getBroadcast(getApplicationContext(), 0, finishIntent, PendingIntent.FLAG_IMMUTABLE);

                ItemDao idao = Database.getInstance(getApplicationContext()).itemDao();
                Item[] items = idao.findAllByStatus().blockingFirst();
                if (items.length == 0) return Result.success();
                StringBuilder content = new StringBuilder();
                for(int i = 0; i < items.length; i++) {
                    content.append(items[i].name);
                    if(i < items.length - 1) content.append(", ");
                }
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("購入予定の品物")
                        .setContentText(content)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .addAction(R.drawable.ic_home_black_24dp, getApplicationContext().getString(R.string.notify_snooze), snoozePending)
                        .addAction(R.drawable.ic_dashboard_black_24dp, getApplicationContext().getString(R.string.notify_complete), finishPending);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                notificationManager.notify(114514, builder.build());
            } else {
                lddao.insert(newLocation);
            }
        }
        return Result.success();
    }

    private boolean isMove(double radius, List<LocationData> data) {
        LocationData center = getCenter(data);
        return data.stream().mapToDouble(d -> d.distance(center)).filter(d -> d >= radius).count() > 0;
    }

    private LocationData getCenter(List<LocationData> data) {
        LocationData center = new LocationData();
        center.latitude = data.stream().mapToDouble(d -> d.latitude).average().orElse(0d);
        center.longitude = data.stream().mapToDouble(d -> d.latitude).average().orElse(0d);
        return center;
    }
}
