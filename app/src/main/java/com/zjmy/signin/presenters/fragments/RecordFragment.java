package com.zjmy.signin.presenters.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.utopia.mvp.presenter.BaseFragmentPresenter;
import com.zjmy.signin.R;
import com.zjmy.signin.inject.component.DaggerFragmentComponent;
import com.zjmy.signin.inject.module.FragmentModule;
import com.zjmy.signin.presenters.activity.MainActivity;
import com.zjmy.signin.presenters.view.RecordFragmentView;
import com.zjmy.signin.utils.files.SPHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;


/**
 * Created by Administrator on 2017/4/1 0001.
 */

public class RecordFragment extends BaseFragmentPresenter<RecordFragmentView> {
    private MainActivity activity;
    private AlertDialog alertDialog;
    private int currrentMonth = 0;
    private int identity;

    @Override
    public Class<RecordFragmentView> getRootViewClass() {
        return RecordFragmentView.class;
    }

    @Override
    public FragmentActivity getContext() {
        return getActivity();
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        initInject();
        if (currrentMonth == 0) {
            SimpleDateFormat sDateFormat = new SimpleDateFormat(
                    "M");
            String month = sDateFormat.format(new Date());
            v.getTvChooseDate().setText(month + "月");
        } else {
            v.getTvChooseDate().setText(currrentMonth + "月");
        }

        //根据身份切换Toolbar显示内容
        identity = (int) SPHelper.getInstance(activity).getParam(SPHelper.IDENTITY, 0);
        if (identity == 0) {
            //普通身份
            v.initData(getChildFragmentManager(), getActivity());
        } else {
            v.initDataForLeader(getChildFragmentManager(), getActivity(), "我的记录");
            v.showDepartment(false);
        }
        initEvent();
    }

    private void initEvent() {
        //按月份查找
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = (String) SPHelper.getInstance(activity).getParam("date", "");
                String year = date.split("-")[0];
                if (currrentMonth == 0) {
                    String month = (String) SPHelper.getInstance(activity).getParam("month", "");
                    showPicker(month, year);
                } else {
                    showPicker(currrentMonth + "", year);
                }
            }
        }, R.id.tv_choose_date);

        //按月份查找
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = (String) SPHelper.getInstance(activity).getParam("date", "");
                String year = date.split("-")[0];
                if (currrentMonth == 0) {
                    String month = (String) SPHelper.getInstance(activity).getParam("month", "");
                    showPicker(month, year);
                } else {
                    showPicker(currrentMonth + "", year);
                }
            }
        }, R.id.tv_choose_day_date);

        //按指定日期查找
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
                String[] dates = date.split("-");
                showPicker(dates[0], dates[1], dates[2]);
            }
        }, R.id.tv_choose_day);

        //按部门查找
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicker(identity);
            }
        },R.id.tv_choose_department);

        //我的记录
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.initDataForLeader(getChildFragmentManager(), getActivity(), "我的记录");
            }
        }, R.id.btn_tb_mine);

        //员工记录
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.initDataForLeader(getChildFragmentManager(), getActivity(), "员工记录");
            }
        }, R.id.btn_tb_staff);
    }

    /**
    *@author 张子扬
    *@time 2017/5/10 0010 9:36
    *@desc 初始化部门的选择器
    */
    private void showPicker(int identity) {
        String[] value;
        if (identity==10) {
             value= new String[]{"销售一部", "销售二部", "销售三部", "销售四部", "销售五部"
                    , "销售六部", "营销办", "山西办", "其他办事处", "技术部", "售后部"};
        }else if (identity==5){
            value = new String[]{"销售一部", "销售二部", "销售三部", "销售四部", "销售五部"
                    , "销售六部", "营销办", "山西办", "其他办事处"};
        }else {
            //identity==4
            value = new String[]{"技术部", "售后部"};
        }
        LayoutInflater inflater=activity.getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_department_picker,null);
        NumberPickerView picker_department= (NumberPickerView) view.findViewById(R.id.picker_department);
        picker_department.setDisplayedValues(value);
        picker_department.setMinValue(1);
        picker_department.setMaxValue(value.length);
        picker_department.setValue(1);

        AppCompatButton btn = (AppCompatButton) view.findViewById(R.id.btn_get);
        btn.setOnClickListener((View)->{
            Log.e("LOG", "showPicker: "+picker_department.getValue() );
            v.update(value[picker_department.getValue()-1],0);
            alertDialog.dismiss();
        });
        alertDialog=new AlertDialog.Builder(activity).create();
        alertDialog.setView(view);
        alertDialog.show();
    }

    /**
     * @param month 当前月份
     * @param year  当前年份
     * @param day   当前日
     * @author 张子扬
     * @desc 根据当前时间来初始化时间选择器
     */
    private void showPicker(String year, String month, String day) {
        String[] value = initMonth(12 + "");
        LayoutInflater inflater = activity.getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_day_picker,null);
        NumberPickerView picker_month=(NumberPickerView) view.findViewById(R.id.picker_month);
        picker_month.setDisplayedValues(value);
        picker_month.setMinValue(1);
        picker_month.setMaxValue(12);
        picker_month.setValue(Integer.parseInt(month)); //显示当前月份

        NumberPickerView picker_year = (NumberPickerView) view.findViewById(R.id.picker_year);
        picker_year.setMinValue(Integer.parseInt(year));
        picker_year.setMaxValue(Integer.parseInt(year));
        picker_year.setDisplayedValues(new String[]{year});
        picker_year.setValue(Integer.parseInt(year));

        NumberPickerView picker_day = (NumberPickerView) view.findViewById(R.id.picker_day);
        String[] dayValue=initMonth(31+"");
        picker_day.setDisplayedValues(dayValue);
        picker_day.setMinValue(1);
        picker_day.setMaxValue(31);
        picker_day.setValue(Integer.parseInt(day));

        AppCompatButton btn = (AppCompatButton) view.findViewById(R.id.btn_get);
        btn.setOnClickListener((View view1) -> {
            String curYear=picker_year.getValue()+"";
            String curMonth = picker_month.getValue() + "";
            String curDay=picker_day.getValue()+"";
            currrentMonth = picker_month.getValue();

            v.getTvChooseDay().setText(curMonth+"."+curDay);
            v.update(curYear,curMonth,curDay);
            alertDialog.dismiss();
        });
        alertDialog = new AlertDialog.Builder(activity)
                .setView(view)
                .create();
        alertDialog.show();

    }

    /**
     * @param month 当前月份
     * @author 张子扬
     * @time 2017/3/26 0026 11:34
     * @desc 根据当前月份来初始化时间选择器
     */
    private void showPicker(String month, String year) {
        String[] value = initMonth(12 + "");
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_picker, null);
        NumberPickerView picker_month = (NumberPickerView) view.findViewById(R.id.picker_month);
        picker_month.setDisplayedValues(value);
        picker_month.setMinValue(1);
        picker_month.setMaxValue(12);
        picker_month.setValue(Integer.parseInt(month)); //显示当前月份
        NumberPickerView picker_year = (NumberPickerView) view.findViewById(R.id.picker_year);
        picker_year.setMinValue(Integer.parseInt(year));
        picker_year.setMaxValue(Integer.parseInt(year));
        picker_year.setDisplayedValues(new String[]{year});
        picker_year.setValue(Integer.parseInt(year));
        AppCompatButton btn = (AppCompatButton) view.findViewById(R.id.btn_get);
        btn.setOnClickListener((View view1) -> {
            String curMonth = picker_month.getValue() + "";
            currrentMonth = picker_month.getValue();
            v.getTvChooseDate().setText(currrentMonth + "月");
            v.update(curMonth);
            alertDialog.dismiss();
        });

        alertDialog = new AlertDialog.Builder(activity)
                .setView(view)
                .create();
        alertDialog.show();
    }

    /**
     * @param mo 最大月份
     * @author 张子扬
     * @time 2017/3/27 0027 17:17
     * @desc 初始化月份
     */
    private String[] initMonth(String mo) {
        List<String> lists = new ArrayList<>();
        for (int i = 1; i <= Integer.parseInt(mo); i++) {
            lists.add(i + "");
        }
        Log.e("LOG", "initMonth: "+lists.size() );
        return lists.toArray(new String[lists.size()]);
    }
}
