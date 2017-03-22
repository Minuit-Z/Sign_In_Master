package com.zjmy.signin.presenters.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.zjmy.signin.presenters.fragments.SignFragment;
import com.zjmy.signin.presenters.fragments.VisitFragment;
import com.zjmy.signin.view.MainActivityView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class TabAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[]{"                        签到打卡                        ","                        拜访打卡                        "};
    private List<Fragment> list=new ArrayList<>();
    private Context context;

    public TabAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        list.add(new SignFragment());
        list.add(new VisitFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
