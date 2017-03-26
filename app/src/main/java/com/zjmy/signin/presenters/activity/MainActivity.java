package com.zjmy.signin.presenters.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.zjmy.signin.R;
import com.zjmy.signin.presenters.view.MainActivityView;

public class MainActivity extends BaseActivity<MainActivityView>{
    @Override
    public Class<MainActivityView> getRootViewClass() {
        return MainActivityView.class;
    }

    @Override
    public void inCreat(Bundle savedInstanceState) {
        activityComponent.inject(this);
        v.init();
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
        switch (item.getItemId())
        {
            //TODO
            case R.id.menu_about:
                Toast.makeText(this, "研发中心制作", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_feedback:
                Intent intent = new Intent(this,FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_exit:
                finish();
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
