package com.zjmy.signin.presenters.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjmy.signin.R;
import com.zjmy.signin.model.bean.Visit;

import java.util.List;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class HistoryAdapter4Visit extends RecyclerView.Adapter<HistoryAdapter4Visit.MyHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<Visit> lists;

    public HistoryAdapter4Visit(Context context, List<Visit> lists) {
        this.context = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_history4visit, parent,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.tv_day.setText(getDay(lists.get(position).getDate()));
        holder.tv_place.setText(lists.get(position).getLocation());
        holder.tv_summary.setText(lists.get(position).getSummary());
    }

    private String getDay(String date) {
        String[] dates=date.split("-");
        return dates[2];
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {
        TextView tv_day,tv_place,tv_summary;

        public MyHolder(View itemView) {
            super(itemView);

            tv_day= (TextView) itemView.findViewById(R.id.tv_history_day);
            tv_place= (TextView) itemView.findViewById(R.id.tv_item_visit_place);
            tv_summary= (TextView) itemView.findViewById(R.id.tv_item_visit_summary);
        }
    }
}
