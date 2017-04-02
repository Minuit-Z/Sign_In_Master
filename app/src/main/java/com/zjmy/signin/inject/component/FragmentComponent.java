package com.zjmy.signin.inject.component;

import com.zjmy.signin.inject.module.FragmentModule;
import com.zjmy.signin.inject.scope.FragmentLife;
import com.zjmy.signin.presenters.fragments.Frag1;
import com.zjmy.signin.presenters.fragments.Frag2;
import com.zjmy.signin.presenters.fragments.Frag3;
import com.zjmy.signin.presenters.fragments.FragInner_1;
import com.zjmy.signin.presenters.fragments.FragInner_2;

import dagger.Component;

@FragmentLife
@Component(modules = FragmentModule.class, dependencies = ActivityComponent.class)
public interface FragmentComponent {
    void inject(Frag1 frag); //考勤
    void inject(Frag2 frag); //统计
    void inject(Frag3 frag); //我的
    void inject(FragInner_1 frag); //统计->考勤记录
    void inject(FragInner_2 frag); //统计->拜访记录
}
