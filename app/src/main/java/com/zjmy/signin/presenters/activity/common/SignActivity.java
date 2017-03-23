package com.zjmy.signin.presenters.activity.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

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

        v.showLocation(getApplicationContext());
    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sign,menu);
        return true;
    }
}
