package com.zjmy.signin.presenters.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.zjmy.signin.R;
import com.zjmy.signin.presenters.view.MainActivityView;
import com.zjmy.signin.utils.app.AppManager;
import com.zjmy.signin.utils.app.UpdateManager;

public class MainActivity extends BaseActivity<MainActivityView> {
    private static AppCompatActivity activity;

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
        v.initClock();
        v.init();

        //检测更新
        new Handler().post(() -> UpdateManager.checkUpdate(this));
    }

    @Override
    public Activity getContext() {
        return this;
    }

    //设置菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_feedback:
                Intent intent = new Intent(this, FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提醒");
                builder.setMessage("确定要退出当前APP吗 ?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppManager.getAppManager().AppExit();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
                break;
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        v.initUser();
    }
}
