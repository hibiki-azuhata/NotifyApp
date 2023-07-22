package com.gmail.udonnikomi.notify.services;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.gmail.udonnikomi.notify.entities.Item;
import com.gmail.udonnikomi.notify.entities.LocationData;
import com.gmail.udonnikomi.notify.services.dao.ItemDao;
import com.gmail.udonnikomi.notify.services.dao.LocationDataDao;

@androidx.room.Database(entities = {Item.class, LocationData.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {
    public abstract ItemDao itemDao();
    public abstract LocationDataDao locationDataDao();

    private static volatile Database instance = null;

    public static Database getInstance(Context context) {
        synchronized (Database.class) {
            if(instance == null) {
                instance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        Database.class,
                        "movement-notify-database"
                ).build();
            }
            return instance;
        }
    }
}
