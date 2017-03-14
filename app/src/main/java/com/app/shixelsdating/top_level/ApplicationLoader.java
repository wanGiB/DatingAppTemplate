package com.app.shixelsdating.top_level;

import android.app.Application;
import android.content.Context;

/**
 * Created by wan on 3/13/17.
 */

public class ApplicationLoader extends Application {

    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationLoader.applicationContext = getApplicationContext();
    }

    public static Context getInstance(){
        return applicationContext;
    }

}
