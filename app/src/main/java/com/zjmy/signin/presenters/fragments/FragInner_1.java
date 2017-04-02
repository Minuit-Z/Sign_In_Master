package com.zjmy.signin.presenters.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.utopia.mvp.presenter.BaseFragmentPresenter;
import com.zjmy.signin.R;
import com.zjmy.signin.inject.component.DaggerFragmentComponent;
import com.zjmy.signin.inject.module.FragmentModule;
import com.zjmy.signin.presenters.activity.MainActivity;
import com.zjmy.signin.presenters.view.FragInner1View;


/**
 * Created by Administrator on 2017/4/2 0002.
 */

public class FragInner_1 extends BaseFragmentPresenter<FragInner1View> {

    private MainActivity activity;

    @Override
    public Class<FragInner1View> getRootViewClass() {
        return FragInner1View.class;
    }

    @Override
    public FragmentActivity getContext() {
        return getActivity();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInject();
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
