package com.zjmy.signin.inject.component;

import com.zjmy.signin.inject.module.FragmentModule;
import com.zjmy.signin.inject.scope.FragmentLife;

import dagger.Component;

@FragmentLife
@Component(modules = FragmentModule.class, dependencies = ActivityComponent.class)
public interface FragmentComponent {

}
