package com.zjmy.signin.presenters.activity.common;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.zjmy.signin.R;
import com.zjmy.signin.presenters.activity.BaseActivity;
import com.zjmy.signin.presenters.adapters.TabAdapter;
import com.zjmy.signin.presenters.fragments.SignFragment;
import com.zjmy.signin.view.LoginActivityView;
import com.zjmy.signin.view.MainActivityView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

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
