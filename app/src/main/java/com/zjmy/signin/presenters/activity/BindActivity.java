package com.zjmy.signin.presenters.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zjmy.signin.presenters.view.BindActivityView;


public class BindActivity extends BaseActivity<BindActivityView> {

    @Override
    public Class<BindActivityView> getRootViewClass() {
        return BindActivityView.class;
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
