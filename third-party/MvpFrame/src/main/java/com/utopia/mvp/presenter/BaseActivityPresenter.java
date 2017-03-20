package com.utopia.mvp.presenter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.utopia.mvp.base.IView;


public abstract class BaseActivityPresenter<V extends IView> extends AppCompatActivity{

    protected V v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        try {
            v = getRootViewClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        v.creatView(getLayoutInflater(), null, savedInstanceState);
        v.initView();
        v.setActivityContext((AppCompatActivity)getContext());

        setContentView(v.getRootView());

        inCreat(savedInstanceState);
    }
    //解除绑定
    @Override
    protected void onDestroy() {
        super.onDestroy();
        inDestory();
        v.removeView();
       // v.onPresenterDestory();
        v = null;
    }

    public abstract Class<V> getRootViewClass();
    public abstract void inCreat(Bundle savedInstanceState);
    public abstract void inDestory();
    public abstract void initTheme();
    public abstract Activity getContext();

}
