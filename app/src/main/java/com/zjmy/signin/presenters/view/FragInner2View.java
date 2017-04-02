package com.zjmy.signin.presenters.view;

import android.support.v7.app.AppCompatActivity;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;


/**
 * 该Fragment为统计->考勤记录
 */
public class FragInner2View extends BaseViewImpl{

    @Override
    public void onPresenterDestory() {
    }

    @Override
    public int getRootViewId() {
        return R.layout.fraginner_2;
    }

    @Override
    public void setActivityContext(AppCompatActivity activity) {
    }

}
