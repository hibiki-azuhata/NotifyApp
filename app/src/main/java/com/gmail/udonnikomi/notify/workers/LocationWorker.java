package com.gmail.udonnikomi.notify.workers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.gmail.udonnikomi.notify.entities.LocationData;
import com.gmail.udonnikomi.notify.services.Database;
import com.gmail.udonnikomi.notify.services.dao.LocationDataDao;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class LocationWorker extends Worker {

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
            if(isMove(5000d, locData)) {
                lddao.deleteAll();
                // 通知など
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
