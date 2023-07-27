package com.gmail.udonnikomi.notify;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.gmail.udonnikomi.notify.services.receivers.UpdateStatusReceiver;
import com.gmail.udonnikomi.notify.ui.itemboard.ItemboardFragment;
import com.gmail.udonnikomi.notify.ui.notifications.NotificationsFragment;
import com.gmail.udonnikomi.notify.ui.settings.SettingsFragment;
import com.gmail.udonnikomi.notify.workers.LocationWorker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.gmail.udonnikomi.notify.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MovementNotify extends AppCompatActivity {

    private ActivityMainBinding binding;

    private List<String> permissions = new ArrayList<String>(){
        {
            add(Manifest.permission.ACCESS_COARSE_LOCATION);
            add(Manifest.permission.ACCESS_FINE_LOCATION);
            add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
            add(Manifest.permission.ACCESS_NOTIFICATION_POLICY);
        }
    };

    private final int REQUEST_CODE = 900;

    private void requestPermission(List<String> permissions) {
        if(permissions.stream().anyMatch(
                permission -> ContextCompat.checkSelfPermission(getApplicationContext(), permission)
                        != PackageManager.PERMISSION_GRANTED)) {
            String[] permissionsArray = new String[permissions.size()];
            permissions.toArray(permissionsArray);
            ActivityCompat.requestPermissions(this, permissionsArray, REQUEST_CODE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS);
        }
        requestPermission(permissions);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new NotificationsFragment());
        binding.navView.setSelectedItemId(R.id.navigation_notifications);
        binding.navView.setBackground(null);

        binding.navView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.navigation_itemboard) {
                replaceFragment(new ItemboardFragment());
            } else if(id == R.id.navigation_settings) {
                replaceFragment(new SettingsFragment());
            }
            return true;
        });

        binding.fabNotifications.setOnClickListener(view -> {
            binding.navView.setSelectedItemId(R.id.navigation_notifications);
            replaceFragment(new NotificationsFragment());
        });

        WorkRequest locationRequest = new PeriodicWorkRequest.Builder(
                LocationWorker.class,
                15, TimeUnit.MINUTES
        ).setConstraints(
                new Constraints.Builder().setRequiresBatteryNotLow(true).build()
        ).build();
        WorkManager.getInstance(getApplicationContext()).enqueue(locationRequest);
        createNotificationChannel();
        registerReceiver(new UpdateStatusReceiver(), new IntentFilter("ACTION_COMPLETE"));
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

    private void createNotificationChannel() {
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(LocationWorker.CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

}