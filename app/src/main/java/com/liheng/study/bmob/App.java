package com.liheng.study.bmob;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * Created by dell on 2016/3/14.
 */
public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this,"c9c64044020354cdda7cc286d523ccb9");
    }
}
