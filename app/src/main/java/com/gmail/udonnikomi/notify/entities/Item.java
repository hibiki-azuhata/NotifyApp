package com.gmail.udonnikomi.notify.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "items")
public class Item {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "icon")
    public int icon;

    @ColumnInfo(name = "status")
    public boolean status;

    public static class ItemData {
        public final int id;
        public final String name;
        public final int icon;
        public final boolean status;
        public ItemData(Item item) {
            this.id = item.id;
            this.name = item.name;
            this.icon = item.icon;
            this.status = item.status;
        }
        public ItemData(int id, String name, int icon, boolean status) {
            this.id = id;
            this.name = name;
            this.icon = icon;
            this.status = status;
        }
    }
}