package com.zjmy.signin.presenters.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.utopia.mvp.presenter.BaseListAdapterPresenter;
import com.utopia.mvp.view.BaseListViewHolderImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.inject.qualifier.model.bean.Sign;

import java.util.List;

/**
 * Created by Administrator on 2017/4/5 0005.
 * 员工签到记录Adapter
 */

public class StuffSignRecordAdapter extends BaseListAdapterPresenter<Sign> {
    public StuffSignRecordAdapter(@NonNull Context mContext) {
        super(mContext);
    }

    @Override
    public BaseListViewHolderImpl OnCreatViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_check_work_record);
    }

    public void setDatas(List<Sign> datas) {
        clear();
        addAll(datas);
        notifyDataSetChanged();
    }

    public void updateDatas(List<Sign> data) {
        addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void OnBindViewHloder(BaseListViewHolderImpl holder, int position) {
        holder.setData(mDatas.get(position));
    }

    public class ViewHolder extends BaseListViewHolderImpl<Sign> {
        private TextView tvDate;
        private TextView tvSignin;
        private TextView tvSignout;
        private TextView tvSigninTip;
        private TextView tvSignoutTip;

        public ViewHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
        }

        @Override
        protected void initView() {
            tvDate = $(R.id.tv_sign_date);
            tvSignin = $(R.id.tv_sign_signin);
            tvSignout = $(R.id.tv_sign_signout);
            tvSigninTip = $(R.id.tv_signin_tip);
            tvSignoutTip = $(R.id.tv_signout_tip);
        }

        @Override
        public void setData(Sign data) {
            tvDate.setText(data.getName());
            Log.e("LOG", "setData: "+data.getName() );
            tvSignin.setText(data.getStartTime() + "   " + data.getSigninPlace());
            if (data.getEndTime() == "" || data.getEndTime() == null) {
                tvSignoutTip.setVisibility(View.GONE);
                tvSignout.setText("未签退");
                tvSignout.setTextColor(mContext.getResources().getColor(R.color.red));
            } else {
                tvSignoutTip.setVisibility(View.VISIBLE);
                tvSignout.setText(data.getEndTime() + "   " + data.getSignoutPlace());
                tvSignout.setTextColor(mContext.getResources().getColor(R.color.black));
            }
        }
    }
}
