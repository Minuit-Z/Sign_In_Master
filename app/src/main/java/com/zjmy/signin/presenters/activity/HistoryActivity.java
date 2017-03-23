package com.zjmy.signin.presenters.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;

import com.zjmy.signin.R;
import com.zjmy.signin.model.bean.Sign;
import com.zjmy.signin.view.HistoryView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class HistoryActivity extends BaseActivity<HistoryView> {


    @Override
    public Class<HistoryView> getRootViewClass() {
        return HistoryView.class;
    }

    @Override
    public void inCreat(Bundle savedInstanceState) {
        activityComponent.inject(this);

        BmobQuery<Sign> query=new BmobQuery<>();
        query.addWhereEqualTo("user", "15513881370");
        query.findObjects(new FindListener<Sign>() {
            @Override
            public void done(List<Sign> list, BmobException e) {
                if (e==null&&list.size()!=0)
                {
                    LinearLayoutManager managers = new LinearLayoutManager(HistoryActivity.this);
                    managers.setOrientation(LinearLayoutManager.VERTICAL);
                    v.initRecyclerData(list,managers);
                }
            }
        });

    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history,menu);
        return true;
    }
}
