package com.sharkna.khaled.sharkna;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by Khaled on 18.06.17
 */
public class SharknaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
