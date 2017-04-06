package com.zjmy.signin.presenters.view;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.model.bean.Sign;
import com.zjmy.signin.presenters.adapters.Adapter_sign;
import com.zjmy.signin.utils.files.SPHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.content.ContentValues.TAG;


/**
 * 该Fragment为统计->考勤记录
 */
public class FragInner1View extends BaseViewImpl {

    private AppCompatActivity activity;
    @Bind(R.id.rcy_sign)
    protected RecyclerView recyclerView;

    @Override
    public void onPresenterDestory() {

    }

    @Override
    public int getRootViewId() {
        return R.layout.fraginner_1;
    }

    @Override
    public void setActivityContext(AppCompatActivity activity) {
        this.activity = activity;
    }

    /**
    *@author 张子扬
    *@time 2017/4/6 0006 11:27
    *@desc 初始化数据
    */
    public void init() {
        //获取当前月份
        String str = new SimpleDateFormat("MM").format(new Date(System.currentTimeMillis()));
        BmobQuery<Sign> query = new BmobQuery<>();
        query.addWhereEqualTo("user", SPHelper.getInstance(activity).getParam(SPHelper.USER, ""));
        query.addWhereEqualTo("month", str);
        query.order("-createdAt");
        query.findObjects(new FindListener<Sign>() {
            @Override
            public void done(List<Sign> list, BmobException e) {
                if (e == null) {

                    Adapter_sign adapter=new Adapter_sign(activity,list);
                    RecyclerView.LayoutManager manager=new LinearLayoutManager(activity);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(manager);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
    *@author 张子扬
    *@time 2017/4/6 0006 11:27
    *@param str 当前月份
    *@desc 初始化指定月份的数据
    */
    public void init(String str) {
        BmobQuery<Sign> query = new BmobQuery<>();
        query.addWhereEqualTo("user", SPHelper.getInstance(activity).getParam(SPHelper.USER, ""));
        query.addWhereEqualTo("month", str);
        query.order("-createdAt");
        query.findObjects(new FindListener<Sign>() {
            @Override
            public void done(List<Sign> list, BmobException e) {
                if (e == null) {
                    Adapter_sign adapter=new Adapter_sign(activity,list);
                    RecyclerView.LayoutManager manager=new LinearLayoutManager(activity);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(manager);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
