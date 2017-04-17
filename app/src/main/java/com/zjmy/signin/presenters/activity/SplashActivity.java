package com.zjmy.signin.presenters.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.zjmy.signin.inject.qualifier.model.bean.User;
import com.zjmy.signin.presenters.SignInApplication;
import com.zjmy.signin.presenters.view.SplashView;
import com.zjmy.signin.utils.files.SPHelper;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SplashActivity extends BaseActivity<SplashView> {


    @Override
    public Class<SplashView> getRootViewClass() {
        return SplashView.class;
    }

    @Override
    public void inCreat(Bundle savedInstanceState) {
        activityComponent.inject(this);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String pass = (String) SPHelper.getInstance(SplashActivity.this).getParam(SPHelper.PASS_WORD, "");
                if (!"".equals(pass)) {
                    //有保存密码,开始自动登录
                    BmobQuery<User> query = new BmobQuery<>();
                    query.addWhereEqualTo("user", SPHelper.getInstance(SplashActivity.this).getParam(SPHelper.USER, ""));
                    query.addWhereEqualTo("password", pass);
                    query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            if (e == null && list.size() > 0) {
                                SignInApplication.userName = list.get(0).getName();
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                finish();
                            } else {
                                SPHelper.getInstance(SplashActivity.this).setParam(SPHelper.PASS_WORD, "");
                                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                finish();
                            }
                        }

                    });
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 2000);
    }

    @Override
    public Activity getContext() {
        return this;
    }
}
