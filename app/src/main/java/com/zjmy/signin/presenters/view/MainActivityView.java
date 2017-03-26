package com.zjmy.signin.presenters.view;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.presenters.SignInApplication;
import com.zjmy.signin.presenters.activity.LoginActivity;
import com.zjmy.signin.presenters.activity.SignActivity;
import com.zjmy.signin.utils.app.JUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;


public class MainActivityView extends BaseViewImpl {
    @Bind(R.id.tv_signtime)
    protected TextView tv_time;
    @Bind(R.id.btn_sign)
    protected AppCompatButton btn_sign;
    @Bind(R.id.btn_visit)
    protected AppCompatButton btn_visit;
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.tv_showuser)
    protected TextView tv_showuser;
    @Bind(R.id.tv_clickme)
    protected TextView tv_clickme;
    @Bind(R.id.tv_title)
    protected TextView tv_title;

    private AppCompatActivity activity;
    private String date;
    @Override
    public int getRootViewId() {
        return R.layout.main_activity;
    }

    @Override
    public void setActivityContext(AppCompatActivity appCompatActivity) {
        activity = appCompatActivity;

        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_main);
        appCompatActivity.setSupportActionBar(toolbar);

        tv_title.setText("移动考勤");
    }

    public void init() {
        // 每1分钟更新一次时间
        CountDownTimer countDownTimer = new CountDownTimer(1000 * 60 * 10, 1000 * 60) {
            @Override
            public void onTick(long millisUntilFinished) {
                initData();
            }

            @Override
            public void onFinish() {
                activity.finish();
            }
        };
        countDownTimer.start();


        btn_sign.setOnClickListener((View v) -> {
            if (SignInApplication.userName != null && !SignInApplication.userName.isEmpty()) {
                Intent i = new Intent(activity, SignActivity.class);
                i.putExtra("type", "sign");
                i.putExtra("time",tv_time.getText().toString());
                i.putExtra("date",date);
                activity.startActivity(i);
            } else {
                JUtils.Toast("请先登录");
            }

        });

        btn_visit.setOnClickListener((View v) -> {
            if (SignInApplication.userName != null && !SignInApplication.userName.isEmpty()) {
                Intent i = new Intent(activity, SignActivity.class);
                i.putExtra("type", "visit");
                i.putExtra("time",tv_time.getText().toString());
                i.putExtra("date",date);
                activity.startActivity(i);
            } else {
                JUtils.Toast("请先登录");
            }

        });
    }

    private void initData() {
        Bmob.getServerTime(new QueryListener<Long>() {
            @Override
            public void done(Long aLong, BmobException e) {
                if (e == null) {
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                    String times = formatter.format(new Date(aLong * 1000L));
                    tv_time.setText(times);

                    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
                    date = formatter2.format(new Date(aLong * 1000L));

                } else {
                    Log.i("bmob", "获取服务器时间失败:" + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onPresenterDestory() {

    }

    //点击登录或点击注销
    @OnClick(R.id.tv_clickme)
    protected void clickme() {
        SignInApplication.userName = null;
        tv_clickme.setText("登录");
        tv_showuser.setText("用户尚未登录  ");

        activity.startActivity(new Intent(activity, LoginActivity.class));
    }

    public void initUser() {
        String userName = SignInApplication.userName;
        if (userName != null && !userName.isEmpty()) {
            tv_clickme.setText("注销");
            tv_showuser.setText(userName + " 已登录  ");
        } else {
            tv_clickme.setText("登录");
            tv_showuser.setText("用户尚未登录  ");
        }
    }

    public void initClock(){
        SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("HH:mm");
        Date curDate =  new Date(System.currentTimeMillis());
        tv_time.setText(formatter.format(curDate));
    }
}
