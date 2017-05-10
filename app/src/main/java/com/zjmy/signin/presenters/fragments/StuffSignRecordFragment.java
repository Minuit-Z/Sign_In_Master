package com.zjmy.signin.presenters.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.utopia.mvp.presenter.BaseFragmentPresenter;
import com.zjmy.signin.inject.component.DaggerFragmentComponent;
import com.zjmy.signin.inject.module.FragmentModule;
import com.zjmy.signin.inject.qualifier.model.bean.Sign;
import com.zjmy.signin.inject.qualifier.model.bean.User;
import com.zjmy.signin.presenters.activity.MainActivity;
import com.zjmy.signin.presenters.adapters.CheckWorkRecordAdapter;
import com.zjmy.signin.presenters.adapters.StuffSignRecordAdapter;
import com.zjmy.signin.presenters.view.CheckWorkRecordFragmentView;
import com.zjmy.signin.utils.app.DynamicBoxUtil;
import com.zjmy.signin.utils.files.SPHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import mehdi.sakout.dynamicbox.DynamicBox;


/**
 * Created by Administrator on 2017/4/2 0002.
 * 所有员工的考勤记录Fragment
 */

public class StuffSignRecordFragment extends BaseFragmentPresenter<CheckWorkRecordFragmentView> {
    private StuffSignRecordAdapter adapter;
    private MainActivity activity;
    private int indexPage = 1;
    private String date = "";//记录当前月份
    private String department = "";
    private DynamicBox box;//加载框


    @Override
    public Class<CheckWorkRecordFragmentView> getRootViewClass() {
        return CheckWorkRecordFragmentView.class;
    }

    public void setDate(String date) {
        this.date = initDate(date);
        indexPage=1;
        myHeavyWork(true, true);
    }

    public void setDepartment(String department) {
        this.department = department;
        indexPage=1;
        myHeavyWork(true, true);
    }

    private String initDate(String date) {
        String[] lists = date.split("-");
        if (Integer.parseInt(lists[1]) < 10) {
            lists[1] = "0" + lists[1];
        }
        if (Integer.parseInt(lists[2]) < 10) {
            lists[2] = "0" + lists[2];
        }
        return lists[0] + "-" + lists[1] + "-" + lists[2];
    }

    @Override
    public FragmentActivity getContext() {
        return getActivity();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInject();

        Log.e("StuffSignRecord", "onViewCreated: ");
        adapter = new StuffSignRecordAdapter(activity);
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

    //TODO 级联查询
    private void initData(boolean isRefresh) {
        BmobQuery<User> query4User = new BmobQuery<>();
        int identity = (int) SPHelper.getInstance(activity).getParam(SPHelper.IDENTITY, 0);
        if (identity == 1 || identity == 2) {
            //普通权限:查找本中心同一部门下,级别小于自己的人员信息
            query4User.addWhereEqualTo("department", SPHelper.getInstance(activity).getParam(SPHelper.DEPARTMENT, ""));
            query4User.addWhereEqualTo("center", SPHelper.getInstance(activity).getParam(SPHelper.CENTER, ""));
            //查询比自己级别小的人员
            query4User.addWhereLessThan("identity", identity);
        } else if (identity == 10||identity==5) {
            Log.e("department", "initData: "+department );
            //特殊权限,无限制查询,默认查询为 营销一部
            if ("".equals(department)) {
                query4User.addWhereEqualTo("department", "销售一部");
            } else if (!department.equals("其他办事处")) {
                query4User.addWhereEqualTo("department", department);
            } else {
                //查询其他办事处
                List<String>  departments= Arrays.asList(new String[]{"陕西办","河北办","河南办","广西办","天津办","上海办","内蒙办","吉林办"});
                query4User.addWhereContainedIn("department",departments);
             }
        } else if (identity == 4) {
            //特殊权限,产品中心以下的所有权限
            if ("".equals(department)) {
                query4User.addWhereEqualTo("department", "技术部");
            } else {
                query4User.addWhereEqualTo("department", department);
            }
        }
        query4User.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    Log.e("TAG", "done: " + list.size());
                }
                List<String> names = new ArrayList<String>();
                for (User u : list) {
                    names.add(u.getUser());
                }
                BmobQuery<Sign> query = new BmobQuery<>();
                query.addWhereContainedIn("user", names);
                if ("" == date) {
                    query.addWhereEqualTo("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())));
                } else {
                    Log.e("DATE", "done: " + date);
                    query.addWhereEqualTo("date", date);
                }
                query.order("-createdAt");
                System.out.println("页数" + indexPage);
                query.setSkip((indexPage - 1) * 10);
                query.setLimit(10);
                query.findObjects(new FindListener<Sign>() {
                    @Override
                    public void done(List<Sign> list, BmobException e) {
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

    @Override
    public void onResume() {
        super.onResume();
        indexPage = 1;
    }
}
