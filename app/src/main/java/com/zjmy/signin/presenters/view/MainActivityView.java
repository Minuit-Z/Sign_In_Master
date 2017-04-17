package com.zjmy.signin.presenters.view;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.presenters.fragments.CheckWorkFragment;
import com.zjmy.signin.presenters.fragments.MyInfoFragment;
import com.zjmy.signin.presenters.fragments.RecordFragment;

import butterknife.Bind;


public class MainActivityView extends BaseViewImpl implements RadioGroup.OnCheckedChangeListener{

    @Bind(R.id.rb_kaoqin)
    protected RadioButton rb_kaoqin;
    @Bind(R.id.rb_tongji)
    protected RadioButton rb_tongji;
    @Bind(R.id.rb_mine)
    protected RadioButton rb_mine;
    @Bind(R.id.rd_group)
    protected RadioGroup rGroub;
    private AppCompatActivity activity;
    private Fragment checkWorkFragment = new CheckWorkFragment();
    private Fragment recordFragment = new RecordFragment();
    private Fragment myInfoFragment = new MyInfoFragment();
    private FragmentTransaction transaction;
    @Override
    public int getRootViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void setActivityContext(AppCompatActivity appCompatActivity) {
        activity = appCompatActivity;

        transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container,checkWorkFragment);
        transaction.add(R.id.fragment_container,recordFragment);
        transaction.hide(recordFragment);
        transaction.add(R.id.fragment_container,myInfoFragment);
        transaction.hide(myInfoFragment);
        transaction.commit();

        rGroub.setOnCheckedChangeListener(this);
        rb_kaoqin.setChecked(true);
    }

    @Override
    public void onPresenterDestory() {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.hide(checkWorkFragment);
        transaction.hide(recordFragment);
        transaction.hide(myInfoFragment);
        switch (checkedId) {
            case R.id.rb_kaoqin:
                transaction.show(checkWorkFragment);
                break;
            case R.id.rb_tongji:
                transaction.show(recordFragment);
                break;
            case R.id.rb_mine:
                transaction.show(myInfoFragment);
                break;
        }

        transaction.commit();
    }

}
