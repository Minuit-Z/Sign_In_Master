package com.zjmy.signin.presenters.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.zjmy.signin.R;
import com.zjmy.signin.inject.component.ActivityComponent;
import com.zjmy.signin.model.bean.User;
import com.zjmy.signin.presenters.SignInApplication;
import com.zjmy.signin.presenters.view.MainActivityView;
import com.zjmy.signin.utils.app.AppManager;
import com.zjmy.signin.utils.app.UpdateManager;
import com.zjmy.signin.utils.files.SPHelper;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends BaseActivity<MainActivityView> {
    private static AppCompatActivity activity;
    private RxPermissions rxPermissions;

    public static AlertDialog.Builder getBuilder() {
        return new AlertDialog.Builder(activity, R.style.AlertDialog_AppCompat_Dialog);
    }

    @Override
    public Class<MainActivityView> getRootViewClass() {
        return MainActivityView.class;
    }

    @Override
    public void inCreat(Bundle savedInstanceState) {
        activityComponent.inject(this);
        activity = this;
        //自动登录
        new Handler().post(() -> initLogin());

//        //检测更新
//        rxPermissions=new RxPermissions(this);
//        rxPermissions.request(Manifest.permission.SYSTEM_ALERT_WINDOW)
//                .subscribe(granted -> {
//                    if (granted) {
//                        new Handler().post(() -> UpdateManager.checkUpdate(this));
//                    } else {
//                        new Handler().post(() -> UpdateManager.checkUpdate(this));
//                    }
//                });
    }

    /**
     * @author 张子扬
     * @time 2017/3/29 0029 10:32
     * @desc 自动登录
     */
    private void initLogin() {
        Log.e("login test", "initLogin: 自动登录");
        String pass = (String) SPHelper.getInstance(MainActivity.this).getParam(SPHelper.PASS_WORD, "");
        if (!"".equals(pass)) {
            //有保存密码,开始自动登录
            BmobQuery<User> query = new BmobQuery<>();
            query.addWhereEqualTo("user", SPHelper.getInstance(this).getParam(SPHelper.USER, ""));
            query.addWhereEqualTo("password", pass);
            query.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> list, BmobException e) {
                    if (e == null && list.size() > 0) {
                        SignInApplication.userName = list.get(0).getName();
                        Toast.makeText(MainActivity.this, "已自动登录", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "done: " + e.toString());
                    }
                }
            });
        }
    }

    @Override
    public Activity getContext() {
        return this;
    }

//    //设置菜单
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_feedback:
//                Intent intent = new Intent(this, FeedBackActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.menu_exit:
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("提醒");
//                builder.setMessage("确定要退出当前APP吗 ?");
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        AppManager.getAppManager().AppExit();
//                    }
//                });
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//                builder.show();
//                break;
//            case R.id.menu_bind:
//                //绑定设备
//                startActivity(new Intent(this, BindActivity.class));
//                break;
//        }
//
//        return true;
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        v.initUser();
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }
}
