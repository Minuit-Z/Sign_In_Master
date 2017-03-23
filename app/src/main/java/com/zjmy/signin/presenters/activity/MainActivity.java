package com.zjmy.signin.presenters.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.zjmy.signin.R;
import com.zjmy.signin.presenters.view.MainActivityView;
import com.zjmy.signin.utils.files.SPHelper;

public class MainActivity extends BaseActivity<MainActivityView>{
    @Override
    public Class<MainActivityView> getRootViewClass() {
        return MainActivityView.class;
    }

    @Override
    public void inCreat(Bundle savedInstanceState) {
        activityComponent.inject(this);
        v.init();
    }

    @Override
    public Activity getContext() {
        return this;
    }

    //设置菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        String userName = (String)SPHelper.getInstance(this).getParam(SPHelper.NAME,"");
        v.initUser(userName);
    }
}
