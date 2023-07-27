package com.gmail.udonnikomi.notify.ui.settings;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.preference.PreferenceFragmentCompat;

import com.gmail.udonnikomi.notify.R;
import com.gmail.udonnikomi.notify.services.Preference;

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