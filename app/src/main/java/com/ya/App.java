package com.ya;

import android.app.Application;

import com.ya.anavigationlib.BuildConfig;
import com.ya.anavigationlib.core.ANavigation;

/**
 * *****************************
 *
 * @Anthor yazhou
 * @Email 1184545990@qq.com
 * @Time 2019/12/25 12:52
 * @DESC *****************************
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ANavigation.init(this, BuildConfig.DEBUG, null);
    }
}
