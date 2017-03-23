package com.zjmy.signin.inject.component;

import android.app.Activity;

import com.zjmy.signin.inject.module.ActivityModule;
import com.zjmy.signin.inject.scope.ActivityLife;
import com.zjmy.signin.presenters.activity.common.HistoryActivity;
import com.zjmy.signin.presenters.activity.common.LoginActivity;
import com.zjmy.signin.presenters.activity.common.MainActivity;
import com.zjmy.signin.presenters.activity.common.SignActivity;

import dagger.Component;


@ActivityLife
@Component(modules = ActivityModule.class, dependencies = ApplicationComponent.class)
public interface ActivityComponent {
    void inject(LoginActivity activity);//登录界面
    void inject(MainActivity activity);
    void inject(SignActivity activity);
    void inject(HistoryActivity activity);
    Activity provideActivity();
}

