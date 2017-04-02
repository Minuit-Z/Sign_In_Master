package com.zjmy.signin.presenters.view;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * 该Fragment为统计->考勤记录
 */
public class FragInner1View extends BaseViewImpl{

    @Override
    public void onPresenterDestory() {
    }

    @Override
    public int getRootViewId() {
        return R.layout.fraginner_1;
    }

    @Override
    public void setActivityContext(AppCompatActivity activity) {
    }

}
