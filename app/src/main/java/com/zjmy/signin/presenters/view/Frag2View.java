package com.zjmy.signin.presenters.view;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.presenters.adapters.Adapter_frag2;
import com.zjmy.signin.presenters.fragments.FragInner_1;
import com.zjmy.signin.presenters.fragments.FragInner_2;

import butterknife.Bind;
import butterknife.OnClick;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/4/2 0002.
 */

public class Frag2View extends BaseViewImpl {

    @Bind(R.id.tab_frag2)
    protected TabLayout layout;
    @Bind(R.id.vp_frag2)
    protected ViewPager vp;
    @Bind(R.id.toolbar)
    protected Toolbar tb;
    Adapter_frag2 adapter;

    private AppCompatActivity activity;

    @Override
    public void onPresenterDestory() {

    }

    @Override
    public int getRootViewId() {
        return R.layout.frag2;
    }

    @Override
    public void setActivityContext(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void initData(FragmentManager manager, Context context) {
        adapter = new Adapter_frag2(manager, context);
        vp.setAdapter(adapter);
        layout.setupWithViewPager(vp);

        activity.setSupportActionBar(tb);
    }

    /**
     * @author 张子扬
     * @time 2017/4/6 0006 10:31
     * @desc 根据选择的月份更前当前数据
     */
    public void update(String month) {
        Log.e(TAG, "update: " + vp.getCurrentItem());
        Adapter_frag2 adapter = (Adapter_frag2) vp.getAdapter();
        switch (vp.getCurrentItem()) {
            case 0:
                FragInner_1 fragInner_1 = (FragInner_1) adapter.getFragment(0);
                fragInner_1.update(month);
                break;
            case 1:
                FragInner_2 fragInner_2 = (FragInner_2) adapter.getFragment(1);
                fragInner_2.update(month);
                break;
        }
    }
}
