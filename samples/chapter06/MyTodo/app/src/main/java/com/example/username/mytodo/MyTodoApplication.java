package com.example.username.mytodo;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by user.name on 2017/01/22.
 */

public class MyTodoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfig
                = new RealmConfiguration.Builder().build();
        // テスト時にDBの構造を変更した時やデータを一度クリアしたい時などは一旦DBを削除すると良い。
        // Realm.deleteRealm(realmConfig);
        Realm.setDefaultConfiguration(realmConfig);
    }
}
