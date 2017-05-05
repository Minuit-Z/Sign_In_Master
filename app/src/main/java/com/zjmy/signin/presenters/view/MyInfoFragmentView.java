package com.zjmy.signin.presenters.view;

import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/4/2 0002.
 */

public class MyInfoFragmentView extends BaseViewImpl {
    @Bind(R.id.tv_username)
    protected TextView tvUsername;
    @Bind(R.id.tv_department)
    protected TextView tvDepartment;
    private AppCompatActivity activity;

    @Override
    public void onPresenterDestory() {
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_my_info_layout;
    }

    @Override
    public void setActivityContext(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void initData(String username, String center, String department) {
        tvUsername.setText("当前用户：" + username);
        tvDepartment.setText("所属部门：" + center + "-" + department);
    }

}
