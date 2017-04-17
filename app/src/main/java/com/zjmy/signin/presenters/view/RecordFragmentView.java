package com.zjmy.signin.presenters.view;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.presenters.adapters.RecordFragmentAdapter;
import com.zjmy.signin.presenters.fragments.CheckWorkRecordFragment;
import com.zjmy.signin.presenters.fragments.VisitRecordFragment;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/4/2 0002.
 */

public class RecordFragmentView extends BaseViewImpl {

    @Bind(R.id.tablayout)
    protected TabLayout layout;
    @Bind(R.id.vp)
    protected ViewPager vp;
    @Bind(R.id.toolbar)
    protected Toolbar tb;
    @Bind(R.id.tv_choose_date)
    protected TextView tvChooseDate;
    RecordFragmentAdapter adapter;

    public TextView getTvChooseDate() {
        return tvChooseDate;
    }

    private AppCompatActivity activity;

    @Override
    public void onPresenterDestory() {

    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_record_layout;
    }

    @Override
    public void setActivityContext(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void initData(FragmentManager manager, Context context) {
        adapter = new RecordFragmentAdapter(manager, context);
        vp.setAdapter(adapter);
        layout.setupWithViewPager(vp);
        activity.setSupportActionBar(tb);
    }

    /**
     * @author 张子扬
     * @time 2017/4/6 0006 10:31
     * @desc 根据选择的月份更前当前数据
     */
    public void update(String month) {
        RecordFragmentAdapter adapter = (RecordFragmentAdapter) vp.getAdapter();
        CheckWorkRecordFragment checkWorkRecordFragment = (CheckWorkRecordFragment) adapter.getFragment(0);
        checkWorkRecordFragment.setCurrentMonth(month);
        VisitRecordFragment visitRecordFragment = (VisitRecordFragment) adapter.getFragment(1);
        visitRecordFragment.setCurrentMonth(month);
    }
}
