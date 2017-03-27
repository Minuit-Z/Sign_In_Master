package com.zjmy.signin.utils.app;

import android.content.Context;
import android.provider.Settings;

/**
*@author 张子扬
*@time 2017/3/27 0027 14:40
*@desc  获取安卓设备引导
*/

public class IdManager {

    public static String getAndroidId(Context context){
        return Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
    }
}
