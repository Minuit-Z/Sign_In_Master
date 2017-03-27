package com.zjmy.signin.presenters.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.zjmy.signin.R;
import com.zjmy.signin.model.bean.Sign;
import com.zjmy.signin.model.bean.Visit;
import com.zjmy.signin.presenters.view.HistoryView;
import com.zjmy.signin.utils.app.DynamicBoxUtil;
import com.zjmy.signin.utils.files.SPHelper;

import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import mehdi.sakout.dynamicbox.DynamicBox;


public class HistoryActivity extends BaseActivity<HistoryView> {
    private AlertDialog alertDialog;
    private DynamicBox dynamicBox;

    @Bind(R.id.tv_signtime)
    protected TextView tv_month;
    @Bind(R.id.tv_signtime_year)
    protected TextView tv_year;
    final String[] months = new String[]{"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
    private String type = "";

    @Override
    public Class<HistoryView> getRootViewClass() {
        return HistoryView.class;
    }

    @Override
    public void inCreat(Bundle savedInstanceState) {
        activityComponent.inject(this);
        dynamicBox = DynamicBoxUtil.newInstance(this, v.get(R.id.rv));
        dynamicBox.showLoadingLayout();

        String date = getIntent().getStringExtra("date");
        String month = date.split("-")[1];
        String year = date.split("-")[0];

        v.setMouth(months[Integer.parseInt(month) - 1]);
        v.setYear(year);
        type = getIntent().getStringExtra("where");
        switch (type) {
            case "sign"://加载Sign表中的数据
                initSignData(month);
                v.initToolbar("打卡记录");
                break;
            case "visit"://加载Visit表中的数据
                initVisitData(month);
                v.initToolbar("拜访记录");
                break;
        }
    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_change_month) {
            String date = getIntent().getStringExtra("date");
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
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_picker, null);
        NumberPicker picker_month = (NumberPicker) view.findViewById(R.id.picker_month);
        picker_month.setMaxValue(Integer.parseInt(mo));
        picker_month.setMinValue(1);
        picker_month.setValue(Integer.parseInt(mo)); //显示当前月份
        picker_month.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);//禁止编辑
        NumberPicker picker_year = (NumberPicker) view.findViewById(R.id.picker_year);
        picker_year.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);//禁止编辑
        picker_year.setMinValue(2017);
        picker_year.setMaxValue(2017);
        Button btn = (Button) view.findViewById(R.id.btn_get);
        btn.setOnClickListener((View view1) -> {
            String month = picker_month.getValue() + "";
            v.setMouth(months[Integer.parseInt(month) - 1]);
            if ("sign".equals(type)) {
                initSignData("0" + month);
            } else if ("visit".equals(type)) {
                initVisitData("0" + month);
            }
            alertDialog.dismiss();
        });


        alertDialog = new AlertDialog.Builder(this)
                .setTitle("选择时间")
                .setView(view)
                .create();
        alertDialog.show();
    }


    private void initVisitData(String month) {
        BmobQuery<Visit> query = new BmobQuery<>();
        query.addWhereEqualTo("month", month);
        query.addWhereEqualTo("user",SPHelper.getInstance(this).getParam(SPHelper.USER,""));
        query.order("-createdAt");
        query.findObjects(new FindListener<Visit>() {

            @Override
            public void done(List<Visit> list, BmobException e) {
                if (e == null && list.size() != 0) {
                    LinearLayoutManager managers = new LinearLayoutManager(HistoryActivity.this);
                    managers.setOrientation(LinearLayoutManager.VERTICAL);
                    v.initRecyclerDataVisit(list, managers);
                    dynamicBox.hideAll();
                } else {
                    dynamicBox.showCustomView(DynamicBoxUtil.emptyView);
                }
            }
        });
    }

    private void initSignData(String month) {
        BmobQuery<Sign> query = new BmobQuery<>();
        query.addWhereEqualTo("month", month);

        query.order("-createdAt");
        query.addWhereEqualTo("user", SPHelper.getInstance(this).getParam(SPHelper.USER,""));
        query.findObjects(new FindListener<Sign>() {

            @Override
            public void done(List<Sign> list, BmobException e) {
                if (e == null && list.size() != 0) {
                    LinearLayoutManager managers = new LinearLayoutManager(HistoryActivity.this);
                    managers.setOrientation(LinearLayoutManager.VERTICAL);
                    v.initRecyclerData(list, managers);
                    dynamicBox.hideAll();
                } else {
                    dynamicBox.showCustomView(DynamicBoxUtil.emptyView);
                }
            }
        });
    }
}
