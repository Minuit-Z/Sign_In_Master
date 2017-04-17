package com.zjmy.signin.presenters.fragments;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.utopia.mvp.presenter.BaseFragmentPresenter;
import com.zjmy.signin.R;
import com.zjmy.signin.inject.component.DaggerFragmentComponent;
import com.zjmy.signin.inject.module.FragmentModule;
import com.zjmy.signin.inject.qualifier.model.bean.User;
import com.zjmy.signin.presenters.activity.LoginActivity;
import com.zjmy.signin.presenters.activity.MainActivity;
import com.zjmy.signin.presenters.view.MyInfoFragmentView;
import com.zjmy.signin.utils.app.UpdateManager;
import com.zjmy.signin.utils.files.SPHelper;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by Administrator on 2017/4/1 0001.
 */

public class MyInfoFragment extends BaseFragmentPresenter<MyInfoFragmentView> {
    private MainActivity activity;
    private RxPermissions rxPermissions;

    @Override
    public Class<MyInfoFragmentView> getRootViewClass() {
        return MyInfoFragmentView.class;
    }

    @Override
    public FragmentActivity getContext() {
        return getActivity();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInject();
        initData();
        initEvents();
    }

    private void initData() {
        String objId = (String) SPHelper.getInstance(activity).getParam(SPHelper.OBJID, "");
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("objectId", objId);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> user, BmobException e) {
                if (e == null) {
                    v.initData(user.get(0).getName(), user.get(0).getCenter(), user.get(0).getDepartment());
                }
            }
        });


    }

    private void initEvents() {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //检测更新
                rxPermissions = new RxPermissions(activity);
                rxPermissions.request(Manifest.permission.SYSTEM_ALERT_WINDOW)
                        .subscribe(granted -> {
                            if (granted) {
                                new Handler().post(() -> UpdateManager.checkUpdate(activity));
                            } else {
                                new Handler().post(() -> UpdateManager.checkUpdate(activity));
                            }
                        });
            }
        }, R.id.tv_update_version);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPHelper.getInstance(activity).setParam(SPHelper.PASS_WORD, "");
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
                activity.finish();
            }
        }, R.id.tv_logout);
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
