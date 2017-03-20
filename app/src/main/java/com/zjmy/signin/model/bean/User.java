package com.zjmy.signin.model.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class User extends BmobUser{
    String username;
    String userpass;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpass() {
        return userpass;
    }

    public void setUserpass(String userpass) {
        this.userpass = userpass;
    }
}
