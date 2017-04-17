package com.zjmy.signin.presenters.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import com.zjmy.signin.presenters.fragments.CheckWorkRecordFragment;
import com.zjmy.signin.presenters.fragments.VisitRecordFragment;


/**
 * Created by Administrator on 2017/4/1 0001.
 */

public class RecordFragmentAdapter extends FragmentPagerAdapter {

    private Context context;
    private String[] names={"考勤记录","拜访记录"};
    private SparseArray<Fragment> fragments = new SparseArray<>();

    public RecordFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context=context;

        fragments.put(0,new CheckWorkRecordFragment());
        fragments.put(1,new VisitRecordFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return names[position];
    }

    public Fragment getFragment(int position) {
        return fragments.get(position);
    }
}
