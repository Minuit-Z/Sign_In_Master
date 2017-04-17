package com.zjmy.signin.inject.qualifier.model.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class User extends BmobObject {
    String user;  // 用户(手机号)
    String name;   // 姓名
    String password; //密码
    String department; //部门
    String center; //中心
    String joinDate;//入职时间
    String androidId; //绑定的ANDROID_ID

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
