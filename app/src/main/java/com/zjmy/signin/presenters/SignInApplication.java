package com.zjmy.signin.presenters;

import android.app.Activity;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.baidu.mapapi.SDKInitializer;
import com.osama.firecrasher.CrashListener;
import com.osama.firecrasher.FireCrasher;
import com.zjmy.signin.BuildConfig;
import com.zjmy.signin.inject.component.ApplicationComponent;
import com.zjmy.signin.inject.component.DaggerApplicationComponent;
import com.zjmy.signin.inject.module.ApplicationModule;
import com.zjmy.signin.utils.app.JUtils;

import cn.bmob.v3.Bmob;

public class SignInApplication extends MultiDexApplication {
    private static SignInApplication application;
    public static String userName ;
    @Override
    public void onCreate() {

        //严苛模式
        if (BuildConfig.DEBUG) {
            //警告在主线程中执行耗时操作
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
        //初始化崩溃管理
        FireCrasher.install(this, new CrashListener() {
            @Override
            public void onCrash(Throwable throwable, final Activity activity) {
                // show your own message
                //Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                // start the recovering process
                recover(activity);
                //you need to add your crash reporting tool here
                //Ex: Crashlytics.logException(throwable);
            }
        });

        super.onCreate();
        application = this;

        JUtils.initialize(this);

        Bmob.initialize(getApplicationContext(),"f654080ef415d1b3dd5b65a2bbf75ee3");
        SDKInitializer.initialize(getApplicationContext());//百度地图初始化
    }

    public ApplicationComponent getAppComponent() {
        return DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public static SignInApplication getApplicationContext2() {
        return application;
    }
}
