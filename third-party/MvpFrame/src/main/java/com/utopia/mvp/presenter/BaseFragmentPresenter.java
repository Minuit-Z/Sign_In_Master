package com.utopia.mvp.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.utopia.mvp.base.IView;

public abstract class BaseFragmentPresenter<T extends IView> extends Fragment{

    protected T v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            v = getRootViewClass().newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        v.creatView(inflater, container, savedInstanceState);
        v.setActivityContext((AppCompatActivity) getContext());
        return v.getRootView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        v.initView();
    }

    @Override
    public void onDestroy() {
        try {
            super.onDestroy();
            if (v != null) {
                v.removeView();
                v.onPresenterDestory();
                v = null;
            }
        }catch (Exception e){
            Log.e("utopia","onDestroy not effective!");
        }
    }

    public abstract Class<T> getRootViewClass();
    public abstract FragmentActivity getContext();

}
