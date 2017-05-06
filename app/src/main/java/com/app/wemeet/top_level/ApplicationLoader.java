package com.app.wemeet.top_level;

import android.app.Application;
import android.content.Context;

/**
 * @author Wan Clem
 */

public class ApplicationLoader extends Application {

    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationLoader.applicationContext = getApplicationContext();
    }

    public static Context getInstance() {
        return applicationContext;
    }

}
