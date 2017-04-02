package com.zjmy.signin.presenters.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zjmy.signin.presenters.fragments.FragInner_1;
import com.zjmy.signin.presenters.fragments.FragInner_2;


/**
 * Created by Administrator on 2017/4/1 0001.
 */

public class Adapter_frag2 extends FragmentPagerAdapter {

    private Context context;
    private String[] names={"考勤记录","拜访记录"};

    public Adapter_frag2(FragmentManager fm, Context context) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new FragInner_1();
            case 1:
                return new FragInner_2();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return names[position];
    }
}
