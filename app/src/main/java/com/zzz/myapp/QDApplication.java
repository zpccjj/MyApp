package com.zzz.myapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;

public class QDApplication extends Application {
    @SuppressLint("StaticFieldLeak") private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        QMUISwipeBackActivityManager.init(this);
    }

    @Override
    public Resources.Theme getTheme() {
        return super.getTheme();
    }
}
