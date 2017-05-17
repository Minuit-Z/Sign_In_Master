package com.zjmy.signin.presenters.view;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.presenters.adapters.RecordForLeaderFragmentAdapter;
import com.zjmy.signin.presenters.adapters.RecordFragmentAdapter;
import com.zjmy.signin.presenters.fragments.CheckWorkRecordFragment;
import com.zjmy.signin.presenters.fragments.StuffSignRecordFragment;
import com.zjmy.signin.presenters.fragments.StuffVisitRecordFragment;
import com.zjmy.signin.presenters.fragments.VisitRecordFragment;
import com.zjmy.signin.utils.files.SPHelper;

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
    @Bind(R.id.tv_choose_day)
    protected TextView tvChooseDay;
    @Bind(R.id.tv_choose_day_date)
    protected TextView tvChooseDayDate;
    @Bind(R.id.tv_choose_department)
    protected TextView tv_department;
    @Bind(R.id.rl_record_leader)
    protected RelativeLayout rl_leader;
    @Bind(R.id.rl_record_ordinary)
    protected RelativeLayout rl_ordinary;

    @Bind(R.id.btn_tb_mine)
    protected RadioButton rb_mine;
    @Bind(R.id.btn_tb_staff)
    protected RadioButton rb_staff;


    RecordFragmentAdapter adapter;
    RecordForLeaderFragmentAdapter adapter_leader;

    public TextView getTvChooseDate() {
        return tvChooseDate;
    }

    public TextView getTvChooseDay() {
        return tvChooseDay;
    }

    public TextView getTvChooseDayDate() {
        return tvChooseDayDate;
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
        rl_leader.setVisibility(View.GONE);
        rl_ordinary.setVisibility(View.VISIBLE);
        adapter = new RecordFragmentAdapter(manager, context);
        vp.setAdapter(adapter);
        layout.setupWithViewPager(vp);
        activity.setSupportActionBar(tb);
    }

    public void initDataForLeader(FragmentManager manager, Context context, String where) {
        rl_leader.setVisibility(View.VISIBLE);
        tvChooseDay.setVisibility(View.GONE);
        tvChooseDayDate.setVisibility(View.GONE);
        rl_ordinary.setVisibility(View.GONE);
        switch (where) {
            case "员工记录":
                int identity = (int) SPHelper.getInstance(activity).getParam(SPHelper.IDENTITY, 0);
                tvChooseDay.setVisibility(View.VISIBLE);
                if (identity == 5 || identity == 10||identity==4) {
                    showDepartment(true);
                } else {
                    showDepartment(false);
                }
                vp.removeAllViews();
                vp.setAdapter(null);
                if (adapter_leader != null) {
                    adapter_leader = null;
                }
                adapter_leader = new RecordForLeaderFragmentAdapter(manager, context, 2);
                vp.setAdapter(adapter_leader);
                layout.setupWithViewPager(vp);
                activity.setSupportActionBar(tb);
                break;
            case "我的记录":
                showDepartment(false);
                tvChooseDayDate.setVisibility(View.VISIBLE);
                vp.removeAllViews();
                vp.setAdapter(null);
                if (adapter_leader != null) {
                    adapter_leader = null;
                }
                adapter_leader = new RecordForLeaderFragmentAdapter(manager, context, 1);
                vp.setAdapter(adapter_leader);
                layout.setupWithViewPager(vp);
                activity.setSupportActionBar(tb);
                break;
        }
    }

    public void showDepartment(boolean show) {
        if (show)
            tv_department.setVisibility(View.VISIBLE);
        else
            tv_department.setVisibility(View.GONE);
    }

    /**
     * @author 张子扬
     * @time 2017/4/6 0006 10:31
     * @desc 根据选择的月份更前当前数据
     */
    public void update(String month) {
        if ((Integer) (SPHelper.getInstance(activity).getParam(SPHelper.IDENTITY, 0)) > 0) {
            //有特殊权限
            RecordForLeaderFragmentAdapter adapter = (RecordForLeaderFragmentAdapter) vp.getAdapter();
            CheckWorkRecordFragment checkWorkRecordFragment = (CheckWorkRecordFragment) adapter.getFragment(0);
            checkWorkRecordFragment.setCurrentMonth(month);
            VisitRecordFragment visitRecordFragment = (VisitRecordFragment) adapter.getFragment(1);
            visitRecordFragment.setCurrentMonth(month);
            tvChooseDayDate.setText(month + "月");
        } else {
            RecordFragmentAdapter adapter = (RecordFragmentAdapter) vp.getAdapter();
            CheckWorkRecordFragment checkWorkRecordFragment = (CheckWorkRecordFragment) adapter.getFragment(0);
            checkWorkRecordFragment.setCurrentMonth(month);
            VisitRecordFragment visitRecordFragment = (VisitRecordFragment) adapter.getFragment(1);
            visitRecordFragment.setCurrentMonth(month);
            tvChooseDay.setText(month + "月");
        }
    }

    /**
     * @author 张子扬
     * @time 2017/4/6 0006 10:31
     * @desc 根据选择的日期更前当前数据
     */
    public void update(String year, String month, String day) {
        RecordForLeaderFragmentAdapter adapter = (RecordForLeaderFragmentAdapter) vp.getAdapter();
        StuffSignRecordFragment stuffSignRecordFragment = (StuffSignRecordFragment) adapter.getFragment(0);
        stuffSignRecordFragment.setDate(year + "-" + month + "-" + day);
        StuffVisitRecordFragment stuffVisitRecordFragment = (StuffVisitRecordFragment) adapter.getFragment(1);
        stuffVisitRecordFragment.setDate(year + "-" + month + "-" + day);

        tvChooseDay.setText(month + "." + day);
    }

    /**
     * @param department 部门
     * @author 张子扬
     * @time 2017/5/10 0010 10:06
     * @desc 超级权限的人员可以使用按部门查询
     */
    public void update(String department, int code) {
        RecordForLeaderFragmentAdapter adapter = (RecordForLeaderFragmentAdapter) vp.getAdapter();
        StuffSignRecordFragment stuffSignRecordFragment = (StuffSignRecordFragment) adapter.getFragment(0);
        stuffSignRecordFragment.setDepartment(department);
        StuffVisitRecordFragment stuffVisitRecordFragment = (StuffVisitRecordFragment) adapter.getFragment(1);
        stuffVisitRecordFragment.setDepartment(department);
    }
}
