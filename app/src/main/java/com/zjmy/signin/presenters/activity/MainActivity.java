package com.zjmy.signin.presenters.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.zjmy.signin.R;
import com.zjmy.signin.inject.component.ActivityComponent;
import com.zjmy.signin.presenters.view.MainActivityView;

public class MainActivity extends BaseActivity<MainActivityView> {
    private static AppCompatActivity activity;

    public static AlertDialog.Builder getBuilder() {
        return new AlertDialog.Builder(activity, R.style.AlertDialog_AppCompat_Dialog);
    }

    @Override
    public Class<MainActivityView> getRootViewClass() {
        return MainActivityView.class;
    }

    @Override
    public void inCreat(Bundle savedInstanceState) {
        activityComponent.inject(this);
        activity = this;
    }

    @Override
    public Activity getContext() {
        return this;
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }
}
