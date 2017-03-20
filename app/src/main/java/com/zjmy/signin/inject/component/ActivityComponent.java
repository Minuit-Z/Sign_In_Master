package com.zjmy.signin.inject.component;

import android.app.Activity;

import com.zjmy.signin.inject.module.ActivityModule;
import com.zjmy.signin.inject.scope.ActivityLife;
import com.zjmy.signin.presenters.activity.common.LoginActivity;

import dagger.Component;


@ActivityLife
@Component(modules = ActivityModule.class, dependencies = ApplicationComponent.class)
public interface ActivityComponent {
    void inject(LoginActivity activity);//登录界面

    Activity provideActivity();
}

