package com.zjmy.signin.presenters.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;

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
        v.initData(getChildFragmentManager(), getActivity());
        initEvent();
    }

    private void initEvent() {
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
        picker_month.setWrapSelectorWheel(false);
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
        return lists.toArray(new String[lists.size()]);
    }
}
