package com.zjmy.signin.presenters.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.utopia.mvp.presenter.BaseListAdapterPresenter;
import com.utopia.mvp.view.BaseListViewHolderImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.inject.qualifier.model.bean.Visit;

import java.util.List;

/**
 * Created by Administrator on 2017/4/5 0005.
 */
public class StuffVisitRecordAdapter extends BaseListAdapterPresenter<Visit> {
    public StuffVisitRecordAdapter(@NonNull Context mContext) {
        super(mContext);
    }

    @Override
    public BaseListViewHolderImpl OnCreatViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.item_visit_record);
    }

    public void setDatas(List<Visit> datas) {
        clear();
        addAll(datas);
        notifyDataSetChanged();
    }

    public void updateDatas(List<Visit> data) {
        addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void OnBindViewHloder(BaseListViewHolderImpl holder, int position) {
        holder.setData(mDatas.get(position));
    }

    public class ViewHolder extends BaseListViewHolderImpl<Visit> {
        private TextView tvDate; //日期格式: 年-月-日 时:分:秒
        private TextView tvDesc; //描述
        private TextView tvLocation; //地点

        public ViewHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
        }

        @Override
        protected void initView() {
            tvDate = $(R.id.tv_visit_date);
            tvDesc = $(R.id.tv_visit_desc);
            tvLocation = $(R.id.tv_visit_location);
        }

        @Override
        public void setData(Visit data) {
            data.setTime(null==data.getTime()?"":data.getTime());
            tvDate.setText(data.getName() +"\n"+data.getTime());
            tvDesc.setText(data.getSummary());
            tvLocation.setText(data.getLocation());
        }
    }
}

