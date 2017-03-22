package com.zjmy.signin.presenters.fragments;

import android.content.Intent;
import android.os.Bundle;
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

    boolean isFirst = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign, container, false);
        tv_time= (TextView) view.findViewById(R.id.tv_signtime);
        if (isFirst)
            initData();

        btn_sign= (Button) view.findViewById(R.id.btn_sign);
        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), SignActivity.class);
                i.putExtra("where","sign");
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
                    isFirst=false;
                } else {
                    Log.i("bmob", "获取服务器时间失败:" + e.getMessage());
                }
            }
        });
    }


}
