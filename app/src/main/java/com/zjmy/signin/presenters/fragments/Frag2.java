package com.zjmy.signin.presenters.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.utopia.mvp.presenter.BaseFragmentPresenter;
import com.zjmy.signin.R;
import com.zjmy.signin.inject.component.DaggerFragmentComponent;
import com.zjmy.signin.inject.module.FragmentModule;
import com.zjmy.signin.presenters.activity.MainActivity;
import com.zjmy.signin.presenters.adapters.Adapter_frag2;
import com.zjmy.signin.presenters.view.Frag2View;


/**
 * Created by Administrator on 2017/4/1 0001.
 */

public class Frag2 extends BaseFragmentPresenter<Frag2View> {
    private MainActivity activity;

    @Override
    public Class<Frag2View> getRootViewClass() {
        return Frag2View.class;
    }

    @Override
    public FragmentActivity getContext() {
        return getActivity();
    }

    public void initInject() {
        activity = (MainActivity) getActivity();
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule())
                .activityComponent(activity.getActivityComponent())
                .build()
                .inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInject();
        v.initData(getChildFragmentManager(),getActivity());
    }
}
