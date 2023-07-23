package com.gmail.udonnikomi.notify.services.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gmail.udonnikomi.notify.entities.Item;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Item... items);

    @Delete
    Completable delete(Item item);

    @Update
    Completable update(Item item);

    @Query("SELECT * FROM items")
    Flowable<Item[]> findAll();

    @Query("SELECT * FROM items WHERE status = 0")
    Flowable<Item[]> findAllByStatus();

    @Query("UPDATE items SET status = :status WHERE id = :id")
    Completable updateStatus(int id, boolean status);
}
