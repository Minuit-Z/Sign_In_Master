package com.zjmy.signin.presenters.view;

import android.support.v7.app.AppCompatActivity;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;


public class SplashView extends BaseViewImpl {


    @Override
    public void onPresenterDestory() {

    }

    @Override
    public int getRootViewId() {
        return R.layout.activity_splash;
    }

    @Override
    public void setActivityContext(AppCompatActivity activity) {

    }
}
