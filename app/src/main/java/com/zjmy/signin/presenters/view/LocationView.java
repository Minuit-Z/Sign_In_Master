package com.zjmy.signin.presenters.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;

import butterknife.Bind;

/**
 * @Description: 登录
 * @authors: utopia
 * @Create time: 16-12-19 上午10:43
 * @Update time: 16-12-19 上午10:43
 */
public class LocationView extends BaseViewImpl {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.tv_title)
    protected TextView tv_title;
    @Bind(R.id.tv_accuracy)
    protected TextView tv_accuracy;//定位精度
    @Bind(R.id.tv_location)
    protected TextView tv_location;//定位地址
    @Bind(R.id.radarView)
    protected RadarView radarView;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BDLocation location = (BDLocation) msg.obj;
            String loc = location.getLocationDescribe().replaceFirst("在", "");
            loc = loc.replace("附近", "");
            tv_location.setText(location.getAddrStr() + loc);
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
            tv_accuracy.setText(type+" — "+location.getRadius());
        }
    };
    private AppCompatActivity activity;
    private LocationClient locationClient = null;

    @Override
    public int getRootViewId() {
        return R.layout.activity_location;
    }

    @Override
    public void setActivityContext(AppCompatActivity appCompatActivity) {
        activity = appCompatActivity;
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener((View v) -> {
            appCompatActivity.finish();
        });

        tv_title.setText("定位预览");

        //设置雷达扫描方向
        radarView.setDirection(RadarView.ANTI_CLOCK_WISE);
        radarView.start();

    }


    @Override
    public void onPresenterDestory() {
    }



    public void showLocation(Context context) {
        locationClient = new LocationClient(context);
        //设置定位条件
        locationClient = new LocationClient(context);
        locationClient.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                Message msg = Message.obtain();
                msg.obj = bdLocation;
                handler.sendMessage(msg);
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setIsNeedAddress(true); //需要地址信息
        option.setOpenGps(true);
        option.setScanSpan(1000);//每秒定位一次
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy); // 设置GPS优先  // 设置GPS优先
        option.disableCache(true);//禁止启用缓存定位
        option.setIsNeedLocationDescribe(true); //设置语义化结果
        locationClient.setLocOption(option);
        locationClient.start();
        locationClient.requestLocation();
    }
}
