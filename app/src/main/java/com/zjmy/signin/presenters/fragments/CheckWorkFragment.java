package com.zjmy.signin.presenters.fragments;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.utopia.mvp.presenter.BaseFragmentPresenter;
import com.zjmy.signin.inject.component.DaggerFragmentComponent;
import com.zjmy.signin.inject.module.FragmentModule;
import com.zjmy.signin.presenters.activity.MainActivity;
import com.zjmy.signin.presenters.view.CheckWorkFragmentView;


/**
 * Created by Administrator on 2017/4/1 0001.
 */

public class CheckWorkFragment extends BaseFragmentPresenter<CheckWorkFragmentView> {

    private MainActivity activity;

    @Override
    public Class<CheckWorkFragmentView> getRootViewClass() {
        return CheckWorkFragmentView.class;
    }

    @Override
    public FragmentActivity getContext() {
        return getActivity();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInject();
        v.initClock();
        v.init();
        RxPermissions rxPermissions = new RxPermissions(activity);
        //申请定位权限
        //Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
                .subscribe(granted -> {
                    if (granted) {
                        v.showLocation(activity.getApplicationContext(), activity.getApplication());
                    } else {
                        v.setPermissions("获取权限失败");
                    }
                });
    }

    public void initInject() {
        activity = (MainActivity) getActivity();
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule())
                .activityComponent(activity.getActivityComponent())
                .build()
                .inject(this);
    }

}
