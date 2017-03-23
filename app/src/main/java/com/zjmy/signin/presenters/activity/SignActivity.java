package com.zjmy.signin.presenters.activity;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.zjmy.signin.R;
import com.zjmy.signin.presenters.view.SignView;

public class SignActivity extends BaseActivity<SignView> {

    @Override
    public Class<SignView> getRootViewClass() {
        return SignView.class;
    }

    @Override
    public void inCreat(Bundle savedInstanceState) {
        activityComponent.inject(this);

        String type = getIntent().getStringExtra("type");
        if(type.equals("sign")){//签到
            v.initViewBySign();
        }else{//访问
            v.initViewByVisit();
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        RxPermissions rxPermissions = new RxPermissions(this); // where this is an Activity instance
        //申请定位权限
        rxPermissions
                .request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        v.showLocation(getApplicationContext());
                    } else {
                        v.setPermissions("获取定位权限失败");
                    }
                });
    }
}
