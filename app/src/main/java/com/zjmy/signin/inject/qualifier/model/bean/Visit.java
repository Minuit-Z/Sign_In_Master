package com.zjmy.signin.inject.qualifier.model.bean;

import com.utopia.mvp.base.IListModel;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class Visit extends BmobObject implements IListModel{
    String name; // 用户名
    String user; // 用户(手机号)
    String clientName; //客户姓名
    String location; //地理位置
    String pic; //现场图片
    String summary;// 摘要
    String sendee;//接收人
    String date ;//日期
    String month;//月份
    String time; //时间(时分秒)

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSendee() {
        return sendee;
    }

    public void setSendee(String sendee) {
        this.sendee = sendee;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override

    public int getModelViewType() {
        return 0;
    }
}
