package com.zjmy.signin.view;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.presenters.adapters.TabAdapter;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Description: 登录
 * @authors: utopia
 * @Create time: 16-12-19 上午10:43
 * @Update time: 16-12-19 上午10:43
*/
public class SignView extends BaseViewImpl {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.img_sign)
    protected ImageView img;

    private AppCompatActivity activity;
    @Override
    public int getRootViewId() {
        return R.layout.activity_sign;
    }

    @Override
    public void setActivityContext(AppCompatActivity appCompatActivity) {
        activity  = appCompatActivity;
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener((View v)-> {
                appCompatActivity.finish();
        });
    }


    @Override
    public void onPresenterDestory() {
    }

    @OnClick(R.id.img_sign)
    protected void sign(View v){
        Toast.makeText(activity, "ssss", Toast.LENGTH_SHORT).show();
    }
}
