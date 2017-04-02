package com.zjmy.signin.presenters.view;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.presenters.adapters.Adapter_frag2;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/2 0002.
 */

public class Frag2View extends BaseViewImpl{

    @Bind(R.id.tab_frag2)
    protected TabLayout layout;
    @Bind(R.id.vp_frag2)
    protected ViewPager vp;

    Adapter_frag2 adapter;

    @Override
    public void onPresenterDestory() {

    }

    @Override
    public int getRootViewId() {
        return R.layout.frag2;
    }

    @Override
    public void setActivityContext(AppCompatActivity activity) {
    }

    public void initData(FragmentManager manager, Context context){
        adapter = new Adapter_frag2(manager, context);
        vp.setAdapter(adapter);
        layout.setupWithViewPager(vp);
    }

}
