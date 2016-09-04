package com.helloworld.shadowsocksfreeaccountgetter;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by PinkD on 2016/9/4.
 * test App
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public Context createPackageContext(String packageName, int flags) throws PackageManager.NameNotFoundException {
        return super.createPackageContext(packageName, flags);
    }
}
