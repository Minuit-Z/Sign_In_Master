package com.zjmy.signin.inject.component;

import android.content.Context;

import com.zjmy.signin.inject.module.ApplicationModule;
import com.zjmy.signin.inject.qualifier.ContextType;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    @ContextType("application")Context context();
}
