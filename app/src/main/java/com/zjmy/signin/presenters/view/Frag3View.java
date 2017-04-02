package com.zjmy.signin.presenters.view;

import android.support.v7.app.AppCompatActivity;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;

/**
 * Created by Administrator on 2017/4/2 0002.
 */

public class Frag3View extends BaseViewImpl{

    private AppCompatActivity activity;

    @Override
    public void onPresenterDestory() {
    }

    @Override
    public int getRootViewId() {
        return R.layout.frag3;
    }

    @Override
    public void setActivityContext(AppCompatActivity activity) {
        this.activity=activity;
    }

}
