package com.zjmy.signin.presenters.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zjmy.signin.R;
import com.zjmy.signin.presenters.activity.common.SignActivity;
import com.zjmy.signin.utils.files.SPHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;


/**
 * @author 张子扬
 * @time 2017/3/21 0021 14:17
 * @desc 签到Fragment
 */

public class SignFragment extends Fragment {

    private TextView tv_time;
    private Button btn_sign;
    private String date;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign, container, false);
        tv_time = (TextView) view.findViewById(R.id.tv_signtime);

        // 每1分钟更新一次时间
        CountDownTimer countDownTimer = new CountDownTimer(1000*60*10, 1000*60) {
            @Override
            public void onTick(long millisUntilFinished) {
                initData();
            }

            @Override
            public void onFinish() {
                getActivity().finish();
            }
        };
        countDownTimer.start();

        btn_sign = (Button) view.findViewById(R.id.btn_sign);
        if ("".equals(SPHelper.getInstance(getActivity()).getParam(SPHelper.NAME,""))){
            //为空,无法签到
            btn_sign.setText("未登录");
            btn_sign.setTextColor(getResources().getColor(R.color.black));
            btn_sign.setEnabled(false);
            btn_sign.setBackground(getResources().getDrawable(R.drawable.circular_shape_none));
        }else {
            btn_sign.setBackground(getResources().getDrawable(R.drawable.circular_shape));
            btn_sign.setEnabled(true);
            btn_sign.setText("外 出 打 卡");
        }

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SignActivity.class);
                i.putExtra("where", "sign");
                i.putExtra("time",tv_time.getText().toString());
                i.putExtra("date",date);
                getActivity().startActivity(i);
            }
        });
        return view;
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


}
