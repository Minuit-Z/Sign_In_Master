package com.zjmy.signin.presenters.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjmy.signin.R;
import com.zjmy.signin.model.bean.Sign;
import com.zjmy.signin.model.bean.Visit;

import java.util.List;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class Adapter_visit extends RecyclerView.Adapter<Adapter_visit.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<Visit> lists;


    public Adapter_visit(Context context, List<Visit> lists) {
        this.context = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_visit, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Visit visit = lists.get(position);
        holder.tv_date.setText(visit.getCreatedAt());
        holder.tv_desc.setText(visit.getSummary());
        holder.tv_location.setText(visit.getLocation());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_date; //日期格式: 年-月-日 时:分:秒
        TextView tv_desc; //描述
        TextView tv_location; //地点

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_date = (TextView) itemView.findViewById(R.id.tv_visit_date);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_visit_desc);
            tv_location= (TextView) itemView.findViewById(R.id.tv_visit_location);
        }
    }
}
