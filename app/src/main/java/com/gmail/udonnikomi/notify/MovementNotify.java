package com.gmail.udonnikomi.notify;

import android.os.Bundle;

import com.gmail.udonnikomi.notify.workers.LocationWorker;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.Constraints;
import androidx.work.OutOfQuotaPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.gmail.udonnikomi.notify.databinding.ActivityMainBinding;

import java.util.concurrent.TimeUnit;

public class MovementNotify extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_notifications, R.id.navigation_itemboard, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        WorkRequest locationRequest = new PeriodicWorkRequest.Builder(
                LocationWorker.class,
                15, TimeUnit.MINUTES
        ).setConstraints(
                new Constraints.Builder().setRequiresBatteryNotLow(true).build()
        ).build();
        WorkManager.getInstance(getApplicationContext()).enqueue(locationRequest);
    }

}