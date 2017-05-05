package com.zjmy.signin.presenters.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import com.zjmy.signin.presenters.fragments.CheckWorkRecordFragment;
import com.zjmy.signin.presenters.fragments.StuffSignRecordFragment;
import com.zjmy.signin.presenters.fragments.StuffVisitRecordFragment;
import com.zjmy.signin.presenters.fragments.VisitRecordFragment;


/**
 * Created by Administrator on 2017/4/1 0001.
 * 上级使用的2级Fragment,用于对本部门员工的查询
 */

public class RecordForLeaderFragmentAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private String[] names = {"考勤记录", "拜访记录"};
    private SparseArray<Fragment> fragments = new SparseArray<>();

    public RecordForLeaderFragmentAdapter(FragmentManager fm, Context context, int where) {
        super(fm);
        this.context = context;
        switch (where) {
            case 1: //Mine
                fragments.clear();
                fragments.put(0, new CheckWorkRecordFragment());
                fragments.put(1, new VisitRecordFragment());
                break;
            case 2: //Staff
                fragments.clear();
                fragments.put(0, new StuffSignRecordFragment());
                fragments.put(1, new StuffVisitRecordFragment());
        }
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
