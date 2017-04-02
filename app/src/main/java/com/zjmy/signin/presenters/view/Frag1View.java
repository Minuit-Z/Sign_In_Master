package com.zjmy.signin.presenters.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.utils.network.NetworkUtil;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/4/2 0002.
 */

public class Frag1View extends BaseViewImpl{

    @Bind(R.id.btn_tb_sign)
    protected Button btn_sign;
    @Bind(R.id.btn_tb_visit)
    protected Button btn_visit;
    @Bind(R.id.layout_main_sign)
    protected LinearLayout ll_sign;
    @Bind(R.id.layout_main_visit)
    protected LinearLayout ll_visit;

    //时间相关
    @Bind(R.id.tv_clock)
    protected TextView tv_time;
    @Bind(R.id.tv_date)
    protected TextView tv_date;

    //位置相关
    @Bind(R.id.tv_main_loctype)
    protected TextView tv_loc_type;
    @Bind(R.id.tv_location)
    protected TextView tv_location;
    private LocationClient locationClient = null;
    private MyHandler handler=new MyHandler(this);

    //签到时需要的字段
    private String location; //位置
    private String date;  //日期
    private String times; //时间


    private AppCompatActivity activity;
    @Override
    public void onPresenterDestory() {
    }

    @Override
    public int getRootViewId() {
        Log.e(TAG, "getRootViewId: ___Frag1View" );
        return R.layout.frag1;
    }

    @Override
    public void setActivityContext(AppCompatActivity activity) {
        this.activity=activity;
    }

    @OnClick(R.id.btn_tb_sign)
    protected void changeToSign(){
        ll_sign.setVisibility(View.VISIBLE);
        ll_visit.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.btn_tb_visit)
    protected void changeToVisit(){
        ll_visit.setVisibility(View.VISIBLE);
        ll_sign.setVisibility(View.INVISIBLE);
    }

    public void init() {
        // 每1分钟更新一次时间
        Log.e(TAG, "init: " );
        CountDownTimer countDownTimer = new CountDownTimer(1000 * 60 * 10, 1000 * 60) {
            @Override
            public void onTick(long millisUntilFinished) {
                initData();
            }
            @Override
            public void onFinish() {
                activity.finish();
            }
        };
        countDownTimer.start();
    }

    /**
    *@author 张子扬
    *@time 2017/4/2 0002 17:13
    *@desc 获取服务器时间
    */
    private void initData() {
        Bmob.getServerTime(new QueryListener<Long>() {
            @Override
            public void done(Long aLong, BmobException e) {
                if (e == null) {
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                    times = formatter.format(new Date(aLong * 1000L));
                    tv_time.setText(times);

                    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
                    date = formatter2.format(new Date(aLong * 1000L));
                    tv_date.setText(date);
                } else {
                    Log.i("bmob", "获取服务器时间失败:" + e.getMessage());
                }
            }
        });
    }

    /**
    *@author 张子扬
    *@time 2017/4/2 0002 17:13
    *@desc 获取系统时间
    */
    public void initClock() {
        Log.e(TAG, "initClock: " );
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        tv_time.setText(formatter.format(curDate));
    }

    public void showLocation(Context context){
        if (NetworkUtil.checkNetWorkAvaluable(context)) {
            //设置定位条件
            locationClient = new LocationClient(context);
            locationClient.registerLocationListener(new BDLocationListener() {

                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    if (bdLocation != null) {
                        Message msg = Message.obtain();
                        msg.obj = bdLocation;
                        handler.sendMessage(msg);
                    }
                }

                @Override
                public void onConnectHotSpotMessage(String s, int i) {

                }
            });
            LocationClientOption option = new LocationClientOption();
            option.setOpenGps(true); // 打开gps
            option.setCoorType("bd09ll"); // 设置坐标类型
            option.setIsNeedAddress(true); //需要地址信息
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy); // 设置GPS优先  // 设置GPS优先
            option.disableCache(true);//禁止启用缓存定位
            option.setScanSpan(2000);
            option.setIsNeedLocationDescribe(true); //设置语义化结果
            locationClient.setLocOption(option);
            locationClient.start();
            locationClient.requestLocation();
        }
    }

    public void setPermissions(String error) {
        tv_loc_type.setText(error);
    }


    /**
    *@author 张子扬
    *@time 2017/4/2 0002 17:36
    *@desc 内部类Handler,处理定位结果
    */
    private class MyHandler extends Handler {
        private final WeakReference<Frag1View> mView;
        private BDLocation location;

        public MyHandler(Frag1View view) {
            mView = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            Frag1View view = mView.get();
            location = (BDLocation) msg.obj;

            if (view != null && location.getLocationDescribe() != null) {
                String loc = location.getLocationDescribe().replaceFirst("在", "");
                loc = loc.replace("附近", "");
                String type = "离线异常";

                switch (location.getLocType()) {
                    case BDLocation.TypeGpsLocation:
                        type = "GPS定位";
                        break;
                    case BDLocation.TypeNetWorkLocation:
                        type = "网络定位";
                        break;
                    default:
                        type = "定位异常";
                }

                tv_loc_type.setText(type + " — " + location.getRadius());
                tv_location.setText(location.getAddrStr() + loc);
            }
        }
    }
}
