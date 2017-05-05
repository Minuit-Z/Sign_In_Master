package com.zjmy.signin.presenters.view;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;

import butterknife.Bind;


public class SplashView extends BaseViewImpl {

    @Bind(R.id.tv_version)
    protected TextView tv_version;

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

    public TextView getTv_version() {
        return tv_version;
    }
}
