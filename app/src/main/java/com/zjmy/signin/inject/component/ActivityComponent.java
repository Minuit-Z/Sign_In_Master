package com.zjmy.signin.inject.component;

import android.app.Activity;

import com.zjmy.signin.inject.module.ActivityModule;
import com.zjmy.signin.inject.scope.ActivityLife;
import com.zjmy.signin.presenters.activity.BindActivity;
import com.zjmy.signin.presenters.activity.FeedBackActivity;
import com.zjmy.signin.presenters.activity.HistoryActivity;
import com.zjmy.signin.presenters.activity.LoginActivity;
import com.zjmy.signin.presenters.activity.MainActivity;
import com.zjmy.signin.presenters.activity.SplashActivity;

import dagger.Component;


@ActivityLife
@Component(modules = ActivityModule.class, dependencies = ApplicationComponent.class)
public interface ActivityComponent {
    void inject(LoginActivity activity);//登录界面
    void inject(MainActivity activity);//主界面
    void inject(HistoryActivity activity);
    void inject(FeedBackActivity activity);
    void inject(BindActivity activity);
    void inject(SplashActivity activity);
    Activity provideActivity();
}

