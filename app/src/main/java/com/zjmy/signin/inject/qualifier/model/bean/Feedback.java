package com.zjmy.signin.inject.qualifier.model.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/3/26 0026.
 */

public class Feedback extends BmobObject{

    private String user; // 登录名
    private String name; //姓名
    private String feedback; //反馈记录

    private String phone;
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

}
