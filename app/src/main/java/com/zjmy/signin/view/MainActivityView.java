package com.zjmy.signin.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.presenters.activity.LoginActivity;
import com.zjmy.signin.utils.files.SPHelper;

import butterknife.Bind;

/**
 * @Description: 登录
 * @authors: utopia
 * @Create time: 16-12-19 上午10:43
 * @Update time: 16-12-19 上午10:43
 */
public class MainActivityView extends BaseViewImpl {


    @Bind(R.id.tv_showuser)
    protected TextView tv_showuser;
    @Bind(R.id.tv_clickme)
    protected TextView tv_clickme;


    private AppCompatActivity activity;

    @Override
    public int getRootViewId() {
        return R.layout.main_activity;
    }

    @Override
    public void setActivityContext(AppCompatActivity appCompatActivity) {
        activity = appCompatActivity;
        String name = (String) SPHelper.getInstance(activity).getParam(SPHelper.NAME, "");
        if ("".equals(name)) {
            tv_showuser.setText("用户尚未登录");

            //登录
            tv_clickme.setText(appCompatActivity.getResources().getString(R.string.clickmelogin));
            tv_clickme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity, LoginActivity.class));
                    activity.finish();
                }
            });

        } else {
            tv_showuser.setText("用户 " + name + " 已登录");

            //注销
            tv_clickme.setText(appCompatActivity.getResources().getString(R.string.clickmelogout));
            tv_clickme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //移除Sp数据
                    SPHelper.getInstance(appCompatActivity).remove(SPHelper.NAME);
                    SPHelper.getInstance(appCompatActivity).remove(SPHelper.USER);
                    activity.startActivity(new Intent(activity, LoginActivity.class));
                    activity.finish();
                }
            });
        }
    }

    @Override
    public void onPresenterDestory() {

    }
}
