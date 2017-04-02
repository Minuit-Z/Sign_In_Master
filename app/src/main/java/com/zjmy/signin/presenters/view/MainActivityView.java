package com.zjmy.signin.presenters.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.presenters.SignInApplication;
import com.zjmy.signin.presenters.activity.LoginActivity;
import com.zjmy.signin.presenters.activity.SignActivity;
import com.zjmy.signin.presenters.fragments.Frag1;
import com.zjmy.signin.presenters.fragments.Frag2;
import com.zjmy.signin.presenters.fragments.Frag3;
import com.zjmy.signin.utils.app.JUtils;
import com.zjmy.signin.utils.files.SPHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

import static android.content.ContentValues.TAG;


public class MainActivityView extends BaseViewImpl implements RadioGroup.OnCheckedChangeListener{
//    @Bind(R.id.tv_signtime)
//    protected TextView tv_time;
//    @Bind(R.id.btn_sign)
//    protected AppCompatButton btn_sign;
//    @Bind(R.id.btn_visit)
//    protected AppCompatButton btn_visit;
//    @Bind(R.id.toolbar)
//    protected Toolbar toolbar;
//    @Bind(R.id.tv_showuser)
//    protected TextView tv_showuser;
//    @Bind(R.id.tv_clickme)
//    protected TextView tv_clickme;
//    @Bind(R.id.tv_title)
//    protected TextView tv_title;

    @Bind(R.id.rb_kaoqin)
    protected RadioButton rb_kaoqin;
    @Bind(R.id.rb_tongji)
    protected RadioButton rb_tongji;
    @Bind(R.id.rb_mine)
    protected RadioButton rb_mine;
    @Bind(R.id.rd_group)
    protected RadioGroup rGroub;
    private AppCompatActivity activity;
//    private String date;
    private Fragment fg1, fg2, fg3;

    @Override
    public int getRootViewId() {
        return R.layout.activity_text_for_radio_button;
    }

    @Override
    public void setActivityContext(AppCompatActivity appCompatActivity) {
        activity = appCompatActivity;
        rGroub.setOnCheckedChangeListener(this);
        rb_kaoqin.setChecked(true);


//        toolbar.getMenu().clear();
//        toolbar.inflateMenu(R.menu.menu_main);
//        appCompatActivity.setSupportActionBar(toolbar);
//
//        tv_title.setText("移动考勤");
    }

//
//
//        btn_sign.setOnClickListener((View v) -> {
//            if (SignInApplication.userName != null && !SignInApplication.userName.isEmpty()) {
//                Intent i = new Intent(activity, SignActivity.class);
//                i.putExtra("type", "sign");
//                i.putExtra("time", tv_time.getText().toString());
//                i.putExtra("date", date);
//                activity.startActivity(i);
//            } else {
//                JUtils.Toast("请先登录");
//            }
//
//        });
//
//        btn_visit.setOnClickListener((View v) -> {
//            if (SignInApplication.userName != null && !SignInApplication.userName.isEmpty()) {
//                Intent i = new Intent(activity, SignActivity.class);
//                i.putExtra("type", "visit");
//                i.putExtra("time", tv_time.getText().toString());
//                i.putExtra("date", date);
//                activity.startActivity(i);
//            } else {
//                JUtils.Toast("请先登录");
//            }
//
//        });
//    }
//

    @Override
    public void onPresenterDestory() {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        android.support.v4.app.FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        switch (checkedId) {
            case R.id.rb_kaoqin:
                if (fg1 == null) {
                    fg1 = new Frag1();
                }
                transaction.replace(R.id.fragment_container, fg1);
                break;
            case R.id.rb_tongji:
                if (fg2 == null) {
                    fg2 = new Frag2();
                }
                transaction.replace(R.id.fragment_container, fg2);
                break;
            case R.id.rb_mine:
                if (fg3 == null) {
                    fg3 = new Frag3();
                }
                transaction.replace(R.id.fragment_container, fg3);
                break;
        }
        transaction.commit();
    }

//    //点击登录或点击注销
//    @OnClick(R.id.tv_clickme)
//    protected void clickme() {
//        if (tv_clickme.getText().toString().equals("注销")) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//            builder.setTitle("提醒");
//            builder.setMessage("确定要注销当前账户吗?");
//            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    //退出当前账户
//                    SignInApplication.userName = null;
//                    SPHelper.getInstance(activity).remove(SPHelper.PASS_WORD);
//                    tv_clickme.setText("登录");
//                    tv_showuser.setText("用户尚未登录  ");
//                    activity.startActivity(new Intent(activity, LoginActivity.class));
//                }
//            });
//            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            });
//            builder.show();
//        } else {
//            activity.startActivity(new Intent(activity, LoginActivity.class));
//
//        }
//    }

//    public void initUser() {
//        String userName = SignInApplication.userName;
//        if (userName != null && !userName.isEmpty()) {
//            tv_clickme.setText("注销");
//            tv_showuser.setText(userName + " 已登录  ");
//        } else {
//            tv_clickme.setText("登录");
//            tv_showuser.setText("用户尚未登录  ");
//        }
//    }
}
