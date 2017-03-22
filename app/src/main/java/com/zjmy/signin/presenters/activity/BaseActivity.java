package com.zjmy.signin.presenters.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.utopia.mvp.base.IView;
import com.utopia.mvp.presenter.BaseActivityPresenter;
import com.zjmy.signin.inject.component.ActivityComponent;
import com.zjmy.signin.inject.component.DaggerActivityComponent;
import com.zjmy.signin.inject.module.ActivityModule;
import com.zjmy.signin.presenters.SignInApplication;
import com.zjmy.signin.utils.app.AppManager;
import com.zjmy.signin.utils.ui.StatusBarUtil;

import static com.zjmy.signin.utils.ui.StatusBarUtil.StatusBarLightMode;


/**
 * @Description:所有activity的基类，处理activity的共性内容和逻辑
 * @authors: utopia
 * @Create time: 16-12-21 上午10:52
 * @Update time: 16-12-21 上午10:52
 */

public abstract class BaseActivity<V extends IView> extends BaseActivityPresenter<V> {
    protected final String TAG = getClass().getSimpleName();
    protected ActivityComponent activityComponent;

    public void initInject() {
        SignInApplication app = SignInApplication.getApplicationContext2();
        activityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(app.getAppComponent())
                .build();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initInject();
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
//        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private boolean init() {
        StatusBarLightMode(this);
        return isInitSystemBar()?initSystemBar():false;
    }

    private boolean initSystemBar() {//修改状态栏颜色
        return StatusBarUtil.setStatusBarColor(this);
    }

    /**
     * 是否初始化状态栏
     */
    protected boolean isInitSystemBar() {
        return true;
    }


    @Override
    public void inDestory() {

    }

    @Override
    public void initTheme() {

    }
}
