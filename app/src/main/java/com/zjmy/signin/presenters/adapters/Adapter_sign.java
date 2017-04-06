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
 * Created by Administrator on 2017/4/5 0005.
 */

public class Adapter_sign extends RecyclerView.Adapter<Adapter_sign.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<Sign> lists;

    private String signin;
    private String signout;

    public Adapter_sign(Context context, List<Sign> lists) {
        this.context = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_sign, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //TODO 签退判断
        holder.tv_date.setText(lists.get(position).getDate());
        signin = "签到:  " + lists.get(position).getStartTime() + "   " + lists.get(position).getSigninPlace();
        holder.tv_signin.setText(signin);
        if (lists.get(position).getEndTime()==""||lists.get(position).getEndTime() == null) {
            holder.tv_signout.setText("未签退");
            holder.tv_signout.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            signout = "签退:  " + lists.get(position).getEndTime() + "   " + lists.get(position).getSignoutPlace();
            holder.tv_signout.setText(signout);
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_date; //日期
        TextView tv_signin; //签到信息,格式:签到:  <时间>  <地点>
        TextView tv_signout;//签退信息,格式:签退:  <时间>  <地点>

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_date = (TextView) itemView.findViewById(R.id.tv_sign_date);
            tv_signin = (TextView) itemView.findViewById(R.id.tv_sign_signin);
            tv_signout = (TextView) itemView.findViewById(R.id.tv_sign_signout);
        }
    }
}
