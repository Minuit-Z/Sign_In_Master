package com.zjmy.signin.presenters.view;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.model.bean.Sign;
import com.zjmy.signin.model.bean.Visit;
import com.zjmy.signin.presenters.adapters.Adapter_sign;
import com.zjmy.signin.presenters.adapters.Adapter_visit;
import com.zjmy.signin.utils.files.SPHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * 该Fragment为统计->考勤记录
 */
public class FragInner2View extends BaseViewImpl {
    private AppCompatActivity activity;
    @Bind(R.id.rcy_visit)
    protected RecyclerView recyclerView;

    @Override
    public void onPresenterDestory() {
    }

    @Override
    public int getRootViewId() {
        return R.layout.fraginner_2;
    }

    @Override
    public void setActivityContext(AppCompatActivity activity) {
        this.activity=activity;
    }

    public void init() {
        //获取当前月份
        String str = new SimpleDateFormat("MM").format(new Date(System.currentTimeMillis()));
        BmobQuery<Visit> query = new BmobQuery<>();
        query.addWhereEqualTo("user", SPHelper.getInstance(activity).getParam(SPHelper.USER, ""));
        query.addWhereEqualTo("month", str);
        query.order("-createdAt");
        query.findObjects(new FindListener<Visit>() {
            @Override
            public void done(List<Visit> list, BmobException e) {
                if (e == null) {
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(activity);
                    recyclerView.setAdapter(new Adapter_visit(activity,list));
                    recyclerView.setLayoutManager(manager);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
    *@author 张子扬
    *@time 2017/4/6 0006 11:46
    *@param str 月份
    *@desc 根据月份查询
    */
    public void init(String str) {
        //获取当前月份
        BmobQuery<Visit> query = new BmobQuery<>();
        query.addWhereEqualTo("user", SPHelper.getInstance(activity).getParam(SPHelper.USER, ""));
        query.addWhereEqualTo("month", str);
        query.order("-createdAt");
        query.findObjects(new FindListener<Visit>() {
            @Override
            public void done(List<Visit> list, BmobException e) {
                if (e == null) {
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(activity);
                    recyclerView.setAdapter(new Adapter_visit(activity,list));
                    recyclerView.setLayoutManager(manager);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
