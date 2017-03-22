package com.zjmy.signin.presenters.activity.common;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.zjmy.signin.R;
import com.zjmy.signin.presenters.activity.BaseActivity;
import com.zjmy.signin.view.SignView;

public class SignActivity extends BaseActivity<SignView> {

    private Toolbar toolbar;

    @Override
    public Class<SignView> getRootViewClass() {
        return SignView.class;
    }

    @Override
    public void inCreat(Bundle savedInstanceState) {
        activityComponent.inject(this);



    }

    @Override
    public Activity getContext() {
        return this;
    }

}
