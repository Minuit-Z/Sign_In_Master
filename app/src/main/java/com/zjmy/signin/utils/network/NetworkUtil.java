package com.zjmy.signin.utils.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/3/26 0026.
 */

public class NetworkUtil {
    private Context context;

    public static boolean checkNetWorkAvaluable(Context context){
        // 1.获取系统服务
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 2.获取net信息
        NetworkInfo info = cm.getActiveNetworkInfo();
        // 3.判断网络是否可用
        if (info != null && info.isConnected()) {
            return true;
        } else {
            Toast.makeText(context, "网络当前不可用，请检查设置！",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
