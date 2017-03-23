package com.zjmy.signin.inject.component;

import android.app.Activity;

import com.zjmy.signin.inject.module.ActivityModule;
import com.zjmy.signin.inject.scope.ActivityLife;
import com.zjmy.signin.presenters.activity.HistoryActivity;
import com.zjmy.signin.presenters.activity.LocationActivity;
import com.zjmy.signin.presenters.activity.LoginActivity;
import com.zjmy.signin.presenters.activity.MainActivity;
import com.zjmy.signin.presenters.activity.SignActivity;

import dagger.Component;


@ActivityLife
@Component(modules = ActivityModule.class, dependencies = ApplicationComponent.class)
public interface ActivityComponent {
    void inject(LoginActivity activity);//登录界面
    void inject(MainActivity activity);
    void inject(SignActivity activity);
    void inject(HistoryActivity activity);
    void inject(LocationActivity activity);
    Activity provideActivity();
}

