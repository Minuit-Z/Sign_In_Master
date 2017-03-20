package com.zjmy.signin.inject.component;

import com.zjmy.signin.inject.module.ServiceModule;
import com.zjmy.signin.inject.scope.ServiceLife;

import dagger.Component;

@ServiceLife
@Component(modules = ServiceModule.class, dependencies = ApplicationComponent.class)
public interface ServiceComponent {

}
