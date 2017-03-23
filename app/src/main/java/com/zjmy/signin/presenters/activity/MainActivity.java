package com.zjmy.signin.presenters.activity;

import android.app.Activity;
import android.os.Bundle;

import com.zjmy.signin.view.MainActivityView;

public class MainActivity extends BaseActivity<MainActivityView>{
    @Override
    public Class<MainActivityView> getRootViewClass() {
        return MainActivityView.class;
    }

    @Override
    public void inCreat(Bundle savedInstanceState) {
        activityComponent.inject(this);
        v.initAdapter(getSupportFragmentManager(),this);
    }

    @Override
    public Activity getContext() {
        return this;
    }

}
