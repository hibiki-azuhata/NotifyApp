package com.gmail.udonnikomi.notify.services.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gmail.udonnikomi.notify.entities.Item;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Item... items);

    @Delete
    Completable delete(Item item);

    @Update
    Completable update(Item item);

    @Query("SELECT * FROM items")
    Item[] findAll();

    @Query("SELECT * FROM items WHERE status = 0")
    Item[] findAllByStatus();

    @Query("UPDATE items SET status = :status WHERE id = :id")
    void updateStatus(int id, boolean status);
}
