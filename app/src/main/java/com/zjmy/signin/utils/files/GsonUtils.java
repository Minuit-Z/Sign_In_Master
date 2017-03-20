package com.zjmy.signin.utils.files;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/8/15.
 * 配置gson解析 json类
 */
public class GsonUtils extends Json {
    private Gson gson = new Gson();

    @Override
    public String toJson(Object src) {
        return gson.toJson(src);
    }


    @Override
    public <T> T toObject(String json, Class<T> claxx) {
        T t ;
        try {
            t = gson.fromJson(json, claxx);
        } catch (Exception e) {
            t = null;
        }
        return t;
    }

    @Override
    public <T> List<T> toList(String json, Class<T[]> claxx) {
        T[] list ;
        try {
            list = gson.fromJson(json, claxx);
        } catch (Exception e) {
            return null ;
        }
        return Arrays.asList(list);
    }
}
