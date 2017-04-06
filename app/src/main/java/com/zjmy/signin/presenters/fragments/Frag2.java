package com.zjmy.signin.presenters.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.utopia.mvp.presenter.BaseFragmentPresenter;
import com.zjmy.signin.R;
import com.zjmy.signin.inject.component.DaggerFragmentComponent;
import com.zjmy.signin.inject.module.FragmentModule;
import com.zjmy.signin.presenters.activity.MainActivity;
import com.zjmy.signin.presenters.adapters.Adapter_frag2;
import com.zjmy.signin.presenters.view.Frag2View;
import com.zjmy.signin.utils.files.SPHelper;

import java.util.ArrayList;
import java.util.List;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;

import static android.content.ContentValues.TAG;


/**
 * Created by Administrator on 2017/4/1 0001.
 */

public class Frag2 extends BaseFragmentPresenter<Frag2View> {
    private MainActivity activity;
    private AlertDialog alertDialog;

    @Override
    public Class<Frag2View> getRootViewClass() {
        return Frag2View.class;
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
        v.initData(getChildFragmentManager(), getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_history,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_change_month) {
            String date = (String) SPHelper.getInstance(activity).getParam("date","");
            String month = date.split("-")[1];
            showPicker(month);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @param mo 当前月份
     * @author 张子扬
     * @time 2017/3/26 0026 11:34
     * @desc 根据当前月份来初始化时间选择器
     */
    private void showPicker(String mo) {
        String[] value=initMonth(mo);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_picker, null);
        NumberPickerView picker_month = (NumberPickerView) view.findViewById(R.id.picker_month);
        picker_month.setDisplayedValues(value);
        picker_month.setMinValue(1);
        picker_month.setMaxValue(Integer.parseInt(mo));
        picker_month.setValue(Integer.parseInt(mo)); //显示当前月份
        NumberPickerView picker_year = (NumberPickerView) view.findViewById(R.id.picker_year);
        picker_year.setMinValue(2017);
        picker_year.setMaxValue(2017);
        picker_year.setDisplayedValues(new String[]{"2017"});
        picker_year.setValue(2017);
        Button btn = (Button) view.findViewById(R.id.btn_get);
        btn.setOnClickListener((View view1) -> {
            String month = picker_month.getValue() + "";
            Log.e(TAG, "showPicker: "+month );
            v.update("0"+month);
            alertDialog.dismiss();
        });

        alertDialog = new AlertDialog.Builder(activity)
                .setView(view)
                .create();
        alertDialog.show();
    }

    /**
     *@author 张子扬
     *@time 2017/3/27 0027 17:17
     *@param mo 最大月份
     *@desc 初始化月份
     */
    private String[] initMonth(String mo) {
        List<String> lists=new ArrayList<>();
        for (int i=1;i<=Integer.parseInt(mo);i++){
            lists.add(i+"");
        }
        return lists.toArray(new String[lists.size()]);
    }
}
