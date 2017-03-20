package com.zjmy.signin.inject.module;

import android.content.Context;

import com.zjmy.signin.presenters.SignInApplication;
import com.zjmy.signin.inject.qualifier.ContextType;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Zane on 16/2/14.
 */
@Module
public class ApplicationModule {

    private SignInApplication mApplication;

    public ApplicationModule(SignInApplication application){
        mApplication = application;
    }

    @Provides
    @Singleton
    SignInApplication providesApplication(){
        return mApplication;
    }

    @Provides
    @Singleton
    @ContextType("application")
    Context providesContext(){
        return SignInApplication.getApplicationContext2();
    }
}
