package com.gmail.udonnikomi.notify.services.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.gmail.udonnikomi.notify.entities.LocationData;

@Dao
public interface LocationDataDao {
    @Insert
    void insert(LocationData ld);

    @Query("DELETE FROM location_data")
    void deleteAll();

    @Query("SELECT * FROM location_data")
    LocationData[] findAll();
}
