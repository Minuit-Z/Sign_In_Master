package com.zjmy.signin.view;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.presenters.adapters.TabAdapter;


import butterknife.Bind;

/**
 * @Description: 登录
 * @authors: utopia
 * @Create time: 16-12-19 上午10:43
 * @Update time: 16-12-19 上午10:43
*/
public class MainActivityView extends BaseViewImpl {

    @Bind(R.id.viewpager)
    protected ViewPager viewPager;
    @Bind(R.id.tablayout)
    protected TabLayout tabLayout;

    TabAdapter adapter;

    private AppCompatActivity activity;
    @Override
    public int getRootViewId() {
        return R.layout.main_activity;
    }

    @Override
    public void setActivityContext(AppCompatActivity appCompatActivity) {
        activity  = appCompatActivity;
    }

    public void initAdapter(FragmentManager manager,Context context){
        adapter = new TabAdapter(manager, context);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    public void onPresenterDestory() {

    }
}
