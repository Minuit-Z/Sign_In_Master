package com.zjmy.signin.presenters.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.zjmy.signin.presenters.view.FeedBackActivityView;


public class FeedBackActivity extends BaseActivity<FeedBackActivityView> {

    @Override
    public Class<FeedBackActivityView> getRootViewClass() {
        return FeedBackActivityView.class;
    }


    @Override
    public void inCreat(Bundle bundle) {
        activityComponent.inject(this);
    }

    private void feedBack(String content) {
        if (content.length() > 1) {

        }
    }

    @Override
    public AppCompatActivity getContext() {
        return this;
    }
}
