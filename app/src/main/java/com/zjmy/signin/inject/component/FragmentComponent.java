package com.zjmy.signin.inject.component;

import com.zjmy.signin.inject.module.FragmentModule;
import com.zjmy.signin.inject.scope.FragmentLife;
import com.zjmy.signin.presenters.fragments.CheckWorkFragment;
import com.zjmy.signin.presenters.fragments.CheckWorkRecordFragment;
import com.zjmy.signin.presenters.fragments.MyInfoFragment;
import com.zjmy.signin.presenters.fragments.RecordFragment;
import com.zjmy.signin.presenters.fragments.StuffSignRecordFragment;
import com.zjmy.signin.presenters.fragments.StuffVisitRecordFragment;
import com.zjmy.signin.presenters.fragments.VisitRecordFragment;

import dagger.Component;

@FragmentLife
@Component(modules = FragmentModule.class, dependencies = ActivityComponent.class)
public interface FragmentComponent {
    void inject(CheckWorkFragment fragment); //考勤

    void inject(RecordFragment fragment); //统计

    void inject(MyInfoFragment fragment); //我的

    void inject(CheckWorkRecordFragment fragment); //统计->考勤记录

    void inject(VisitRecordFragment fragment); //统计->拜访记录

    void inject(StuffSignRecordFragment fragment); //统计->员工考勤记录

    void inject(StuffVisitRecordFragment fragment); //统计->员工拜访记录
}
