package com.zjmy.signin.model.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class Sign extends BmobObject{
    String user; //用户
    String name;  //姓名
    String date; //日期  2017-01-01
    String week; //星期  星期六
    String daytype; //日期类型  工作日,假日
    String startTime; //上班时间
    String endTime; //下班时间
    String signinPlace;//签到地点
    String signoutPlace;//签退地点
    String early_late;//迟到早退及时间  迟到25;早退10
    String absence; //旷工
    String signinPic;//签到图片url
    String signoutPic;//签退图片url

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDaytype() {
        return daytype;
    }

    public void setDaytype(String daytype) {
        this.daytype = daytype;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSigninPlace() {
        return signinPlace;
    }

    public void setSigninPlace(String signinPlace) {
        this.signinPlace = signinPlace;
    }

    public String getSignoutPlace() {
        return signoutPlace;
    }

    public void setSignoutPlace(String signoutPlace) {
        this.signoutPlace = signoutPlace;
    }

    public String getEarly_late() {
        return early_late;
    }

    public void setEarly_late(String early_late) {
        this.early_late = early_late;
    }

    public String getAbsence() {
        return absence;
    }

    public void setAbsence(String absence) {
        this.absence = absence;
    }

    public String getSigninPic() {
        return signinPic;
    }

    public void setSigninPic(String signinPic) {
        this.signinPic = signinPic;
    }

    public String getSignoutPic() {
        return signoutPic;
    }

    public void setSignoutPic(String signoutPic) {
        this.signoutPic = signoutPic;
    }
}
