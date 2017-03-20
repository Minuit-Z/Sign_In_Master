package com.zjmy.signin.inject.module;

import android.app.Activity;
import android.content.Context;

import com.zjmy.signin.inject.qualifier.ContextType;
import com.zjmy.signin.inject.scope.ActivityLife;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Zane on 16/2/14.
 */
@Module
public class ActivityModule {

    private Activity activity;

    public ActivityModule(Activity activity){
        this.activity = activity;
    }

    @Provides
    @ActivityLife
    Activity providesActivity(){
        return activity;
    }

    @Provides
    @ActivityLife
    @ContextType("activity")
    Context providesContext(){
        return activity;
    }

}
