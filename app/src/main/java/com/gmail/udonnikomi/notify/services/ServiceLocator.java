package com.gmail.udonnikomi.notify.services;

import com.gmail.udonnikomi.notify.services.impl.LocationServiceImpl;

public class ServiceLocator {
    private static ServiceLocator instance = null;
    private ServiceLocator() {}

    public static ServiceLocator getInstance() {
        if (instance == null) {
            synchronized (ServiceLocator.class) {
                instance = new ServiceLocator();
            }
        }
        return instance;
    }

    public LocationService getLocationService() {
        return new LocationServiceImpl();
    }
}
