package com.zjmy.signin.presenters.view;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.presenters.adapters.VisitRecordAdapter;

import butterknife.Bind;


/**
 * 该Fragment为统计->考勤记录
 */
public class VisitRecordFragmentView extends BaseViewImpl {
    private AppCompatActivity activity;
    @Bind(R.id.rcv_list)
    protected XRecyclerView xRecyclerView;

    @Override
    public void onPresenterDestory() {
    }

    public XRecyclerView getxRecyclerView() {
        return xRecyclerView;
    }

    @Override
    public int getRootViewId() {
        return R.layout.xrecycleview_common;
    }

    @Override
    public void setActivityContext(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void initRecycleView(VisitRecordAdapter adapter, LinearLayoutManager manager) {
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLayoutManager(manager);
        xRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void refreshComplete() {
        xRecyclerView.refreshComplete();
        xRecyclerView.loadMoreComplete();
    }

}
