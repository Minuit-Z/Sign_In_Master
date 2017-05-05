package com.zjmy.signin.presenters.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.utopia.mvp.presenter.BaseFragmentPresenter;
import com.zjmy.signin.inject.component.DaggerFragmentComponent;
import com.zjmy.signin.inject.module.FragmentModule;
import com.zjmy.signin.inject.qualifier.model.bean.Visit;
import com.zjmy.signin.presenters.activity.MainActivity;
import com.zjmy.signin.presenters.adapters.VisitRecordAdapter;
import com.zjmy.signin.presenters.view.VisitRecordFragmentView;
import com.zjmy.signin.utils.app.DynamicBoxUtil;
import com.zjmy.signin.utils.files.SPHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import mehdi.sakout.dynamicbox.DynamicBox;


/**
 * Created by Administrator on 2017/4/2 0002.
 * 拜访记录Fragment
 */

public class VisitRecordFragment extends BaseFragmentPresenter<VisitRecordFragmentView> {
    private VisitRecordAdapter adapter;
    private MainActivity activity;
    private int indexPage = 1;
    private String currentMonth = "";//记录当前月份
    private DynamicBox box;//加载框

    @Override
    public Class<VisitRecordFragmentView> getRootViewClass() {
        return VisitRecordFragmentView.class;
    }

    public void setCurrentMonth(String currentMonth) {
        this.currentMonth = currentMonth;
        myHeavyWork(true, true);
    }

    @Override
    public FragmentActivity getContext() {
        return getActivity();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInject();
        adapter = new VisitRecordAdapter(activity);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        box = DynamicBoxUtil.newInstance(activity, v.getxRecyclerView());
        box.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                box.showLoadingLayout();
                indexPage = 1;
                myHeavyWork(true, true);
            }
        });
        v.initRecycleView(adapter, manager);
        v.getxRecyclerView().setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                indexPage = 1;
                myHeavyWork(true, false);
            }

            @Override
            public void onLoadMore() {
                ++indexPage;
                myHeavyWork(false, false);
            }
        });
        box.showLoadingLayout();
        myHeavyWork(true, true);
    }

    public void myHeavyWork(boolean isRefresh, boolean isLoading) {
        if (isLoading) {
            if (adapter != null) {
                adapter.clear();
                adapter.notifyDataSetChanged();
            }
        }
        new Handler().post(() -> initData(isRefresh));
    }

    private void initData(boolean isRefresh) {
        if (TextUtils.isEmpty(currentMonth)) {
            currentMonth = new SimpleDateFormat("M").format(new Date(System.currentTimeMillis()));
        }
        BmobQuery<Visit> query = new BmobQuery<>();
        query.addWhereEqualTo("user", SPHelper.getInstance(activity).getParam(SPHelper.USER, ""));
        if (Integer.parseInt(currentMonth) > 0 && Integer.parseInt(currentMonth) < 10) {
            query.addWhereEqualTo("month", "0" + currentMonth);
        } else {
            query.addWhereEqualTo("month", currentMonth);
        }
        query.order("-createdAt");
        query.setSkip((indexPage - 1) * 10);
        query.setLimit(10);
        query.findObjects(new FindListener<Visit>() {
            @Override
            public void done(List<Visit> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        if (isRefresh) {
                            adapter.setDatas(list);
                        } else {
                            adapter.updateDatas(list);
                        }
                        box.hideAll();
                    } else {
                        if (indexPage == 1) {
                            box.showCustomView(DynamicBoxUtil.emptyView);
                        }
                    }

                    if (v != null)
                        v.refreshComplete();
                } else {
                    e.printStackTrace();
                    v.refreshComplete();
                    box.showExceptionLayout();
                }
            }
        });
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
