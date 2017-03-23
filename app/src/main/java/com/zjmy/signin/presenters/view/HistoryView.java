package com.zjmy.signin.presenters.view;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.model.bean.Sign;
import com.zjmy.signin.presenters.adapters.HistoryAdapter;

import java.util.List;

import butterknife.Bind;

/**
 * @Description: 登录
 * @authors: utopia
 * @Create time: 16-12-19 上午10:43
 * @Update time: 16-12-19 上午10:43
*/
public class HistoryView extends BaseViewImpl {

    @Bind(R.id.toolbar_history)
    protected Toolbar toolbar;
    @Bind(R.id.rv)
    protected RecyclerView rv;
    private AppCompatActivity activity;
    private NumberPicker picker_month;
    private NumberPicker picker_year;
    private HistoryAdapter adapter;
    @Override
    public int getRootViewId() {
        return R.layout.activity_history;
    }

    @Override
    public void setActivityContext(AppCompatActivity appCompatActivity) {
        activity  = appCompatActivity;
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener((View v)-> {
                appCompatActivity.finish();
        });
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_history);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId()==R.id.menu_change_month){
                    showPicker();
                }
                return true;
            }
        });
    }

    private void showPicker() {
        LayoutInflater inflater=activity.getLayoutInflater();
        View v=inflater.inflate(R.layout.dialog_picker,null);
        picker_month= (NumberPicker) v.findViewById(R.id.picker_month);
        picker_month.setMaxValue(12);
        picker_month.setMinValue(1);
        picker_month.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);//禁止编辑
        picker_year= (NumberPicker) v.findViewById(R.id.picker_year);
        picker_year.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);//禁止编辑
        picker_year.setMinValue(2017);
        picker_year.setMaxValue(2017);
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle("选择时间");
        builder.setView(v);
        builder.show();
    }


    @Override
    public void onPresenterDestory() {
    }

    public void initRecyclerData(List<Sign> list, RecyclerView.LayoutManager manager){
        adapter=new HistoryAdapter(activity,list);
        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);
    }
}
