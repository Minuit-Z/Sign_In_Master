package com.zjmy.signin.presenters.view;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.model.bean.Sign;
import com.zjmy.signin.model.bean.Visit;
import com.zjmy.signin.presenters.adapters.HistoryAdapter;
import com.zjmy.signin.presenters.adapters.HistoryAdapter4Visit;

import java.util.List;

import butterknife.Bind;

/**
 * @Description: 登录
 * @authors: utopia
 * @Create time: 16-12-19 上午10:43
 * @Update time: 16-12-19 上午10:43
 */
public class HistoryView extends BaseViewImpl {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.rv)
    protected RecyclerView rv;
    @Bind(R.id.tv_title)
    protected TextView tv_title;
    private AppCompatActivity activity;
    private HistoryAdapter adapter;
    private HistoryAdapter4Visit adapter2;

    @Bind(R.id.tv_signtime)
    protected TextView tv_signtime;
    @Bind(R.id.tv_signtime_year)
    protected TextView tv_signtime_year;

    @Override
    public int getRootViewId() {
        return R.layout.activity_history;
    }


    @Override
    public void setActivityContext(AppCompatActivity appCompatActivity) {
        activity = appCompatActivity;
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener((View v) -> {
            appCompatActivity.finish();
        });
        tv_title.setText("打卡记录");

    }

    @Override
    public void onPresenterDestory() {
    }

    public void initRecyclerData(List<Sign> list, RecyclerView.LayoutManager manager) {
        adapter = new HistoryAdapter(activity, list);
        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);
    }

    public void initRecyclerDataVisit(List<Visit> list, RecyclerView.LayoutManager manager) {
        adapter2 = new HistoryAdapter4Visit(activity, list);
        rv.setLayoutManager(manager);
        rv.setAdapter(adapter2);
    }

    public void setMouth(String msg){
        tv_signtime.setText(msg);
    }
    public void setYear(String msg){
        tv_signtime_year.setText(msg);
    }
}
