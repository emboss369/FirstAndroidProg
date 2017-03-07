package com.example.username.myscheduler;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by user.name on 2017/03/04.
 */

public class MySchedulerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfig
                = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfig);
    }

}
