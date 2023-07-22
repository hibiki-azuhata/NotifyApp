package com.gmail.udonnikomi.notify.entities;

import android.location.Location;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "location_data")
public class LocationData {
    @PrimaryKey(autoGenerate = true)
    public Long id;
    @ColumnInfo(name = "current_time")
    public Long currentTime;

    @ColumnInfo(name = "latitude")
    public Double latitude;

    @ColumnInfo(name = "longitude")
    public Double longitude;

    @Ignore
    public Double distance(LocationData target) {
        float[] result = new float[3];
        Location.distanceBetween(this.latitude, this.longitude, target.latitude, target.longitude, result);
        return (double) result[0]; // メートル換算
    }
}
