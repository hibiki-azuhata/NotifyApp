package com.gmail.udonnikomi.notify.services;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {
    public static final String PREFERENCE_NAME = "MOVEMENT_NOTIFY_PREFERENCE";

    public enum Key {
        NOTIFY_RANGE("PREFERENCE_RANGE", 5f);

        private String name;
        private Object defaultValue;
        Key(String name, Object defaultValue) {
            this.name = name;
            this.defaultValue = defaultValue;
        }

        public Object getDefaultValue() {
            return this.defaultValue;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public Preference(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        this.editor = this.sharedPreferences.edit();
    }

    public void setFloat(Key key, float value) {
        this.editor.putFloat(key.toString(), value);
        this.editor.commit();
    }

    public float getFloat(Key key) {
        return this.sharedPreferences.getFloat(key.toString(), (Float) key.getDefaultValue());
    }
}
