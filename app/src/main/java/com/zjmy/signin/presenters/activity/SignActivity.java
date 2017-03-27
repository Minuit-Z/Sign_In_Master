package com.zjmy.signin.presenters.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.zjmy.signin.R;
import com.zjmy.signin.model.bean.Sign;
import com.zjmy.signin.model.bean.Visit;
import com.zjmy.signin.presenters.view.SignView;
import com.zjmy.signin.utils.app.IdManager;
import com.zjmy.signin.utils.files.SPHelper;

import java.util.List;
import java.util.UUID;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SignActivity extends BaseActivity<SignView> {
    private String type = "";
    String date = "";
    String time = "";

    @Override
    public Class<SignView> getRootViewClass() {
        return SignView.class;
    }

    @Override
    public void inCreat(Bundle savedInstanceState) {
        activityComponent.inject(this);
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        type = getIntent().getStringExtra("type");

        if (type.equals("sign")) {//签到
            v.initViewBySign();
            doSignInOrOut();
        } else {//访问
            v.initViewByVisit();
            doVisitInOrOut();
        }

        Log.e(TAG, "inCreat: "+ IdManager.getAndroidId(this));
    }

    @Override
    protected void onResume() {
        super.onResume();

        RxPermissions rxPermissions = new RxPermissions(this); // where this is an Activity instance
        //申请定位权限
        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        v.showLocation(getApplicationContext());
                    } else {
                        v.setPermissions("获取定位权限失败");
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();

        v.stopLocation();
    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sign, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_detail) {
            Intent intent = new Intent(this, HistoryActivity.class);
            intent.putExtra("where", type);
            intent.putExtra("date", date);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @author 张子扬
     * @time 2017/3/23 0023 17:15
     * @desc 拜访的签到签退
     */
    private void doVisitInOrOut() {
        BmobQuery<Visit> query = new BmobQuery<>();
        query.addWhereEqualTo("date", date);
        query.addWhereEqualTo("user", SPHelper.getInstance(this).getParam(SPHelper.USER, ""));
        query.findObjects(new FindListener<Visit>() {
            @Override
            public void done(List<Visit> list, BmobException e) {
                if (e == null) {
                    //没有当日数据,进行打卡
                    v.setSignBehavior(4, date, time, null);
                } else if (e == null && list.size() > 0) {
                    //已经进行过打卡,无法修改
                    //v.setSignBehavior(5,date,time,list.get(0).getDate()+"  "+list.get(0).getSummary());
                }
            }
        });
    }


    /**
     * @author 张子扬
     * @time 2017/3/23 0023 16:30
     * @desc 打卡的签到签退
     */
    private void doSignInOrOut() {
        //判断是签到还是签退
        BmobQuery<Sign> query = new BmobQuery<>();
        query.addWhereEqualTo("date", date);
        query.addWhereEqualTo("user", SPHelper.getInstance(this).getParam(SPHelper.USER, ""));
        query.findObjects(new FindListener<Sign>() {
            @Override
            public void done(List<Sign> list, BmobException e) {
                if (e == null && list.size() == 0) {
                    //数据库中没有当日数据,进行签到
                    v.setSignBehavior(0, date, time, null);
                } else if (e == null && list.size() > 0) {
                    //数据库有当日数据,签退
                    if (list.get(0).getSignoutPlace() != null) {
                        // 已经签退,无法更新数据
                        v.setSignBehavior(2, date, time, null);
                    } else {
                        String objId = list.get(0).getObjectId();
                        v.setSignBehavior(1, date, time, objId);
                    }
                } else {
                    v.setSignBehavior(999, date, time, null);
                }
            }
        });
    }

}
