package com.zjmy.signin.presenters.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjmy.signin.R;
import com.zjmy.signin.model.bean.Sign;

import java.util.List;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<Sign> lists;

    public HistoryAdapter(Context context, List<Sign> lists) {
        this.context = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_history, parent,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.tv_day.setText(getDay(lists.get(position).getDate())+"日");
        holder.tv_signin_time.setText("签到: "+lists.get(position).getStartTime());
        if (lists.get(position).getEndTime()==null){
            holder.tv_signout_time.setText("未签退");
            holder.tv_signout_time.setTextColor(context.getResources().getColor(R.color.red));
        }else {
        holder.tv_signout_time.setText("签退: "+lists.get(position).getEndTime());
        }
        holder.tv_signout_place.setText(lists.get(position).getSignoutPlace());
        holder.tv_signin_place.setText(lists.get(position).getSigninPlace());
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
        TextView tv_signin_time, tv_signout_time, tv_day, tv_signin_place, tv_signout_place;

        public MyHolder(View itemView) {
            super(itemView);

            tv_day= (TextView) itemView.findViewById(R.id.tv_history_day);
            tv_signin_time= (TextView) itemView.findViewById(R.id.tv_history_signin_time);
            tv_signin_place= (TextView) itemView.findViewById(R.id.tv_history_signin_place);
            tv_signout_time= (TextView) itemView.findViewById(R.id.tv_history_signout_time);
            tv_signout_place= (TextView) itemView.findViewById(R.id.tv_history_signout_place);
        }
    }
}
