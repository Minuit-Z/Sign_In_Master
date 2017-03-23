package com.zjmy.signin.presenters.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zjmy.signin.presenters.activity.BaseActivity;
import com.zjmy.signin.presenters.adapters.TabAdapter;
import com.zjmy.signin.view.LoginActivityView;

public class LoginActivity extends BaseActivity<LoginActivityView> {
    @Override
    public Class<LoginActivityView> getRootViewClass() {
        return LoginActivityView.class;
    }

    @Override
    public void inCreat(Bundle bundle) {
        activityComponent.inject(this);
    }

    @Override
    public AppCompatActivity getContext() {
        return this;
    }

}
