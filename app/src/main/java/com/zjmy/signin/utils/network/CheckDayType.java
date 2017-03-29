package com.zjmy.signin.utils.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * @author 张子扬
 * @time 2017/3/29 0029 11:07
 * @desc 节假日请求API: http://www.easybots.cn/holiday_api.net
 */

public class CheckDayType {
   public static String[] dayType=new String[]{"工作日","休息日","节假日"};
    /**
    *@author 张子扬
    *@time 2017/3/29 0029 11:21
    *@param response json数据
    *@param date json数据的键
    *@desc 解析json
    *@return 日期类型
    */
    public static String parseJson(String response,String date) {
        try {
            JSONObject obj=new JSONObject(response);
            int type=Integer.parseInt(obj.getString(date));
            Log.e(TAG, "parseJson: "+dayType[type] );
            return dayType[type];
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
