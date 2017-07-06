package com.vtime.qqalbum.base;

import android.app.Application;


/**
 * Created by Jue on 2016/4/13.
 */
public class BaseApplication extends Application {

    static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

    }

    public static BaseApplication getInstance() {
        return instance;
    }

}


