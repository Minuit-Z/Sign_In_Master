package com.zjmy.signin.presenters.view;

import android.content.Context;
import android.content.Intent;
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
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
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
    @Bind(R.id.baidumap)
    protected MapView mapView;
    BaiduMap map;

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
            tv_accuracy.setText(type + " — " + location.getRadius());

            map.setMyLocationEnabled(true);
            MyLocationData data = new MyLocationData.Builder()
                    .latitude(location.getLatitude()).longitude(location.getLongitude()).build();

            map.setMyLocationData(data);
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                    .fromResource(R.mipmap.icon_gcoding);
            MyLocationConfiguration config = new MyLocationConfiguration(null, true, bitmapDescriptor);
            map.setMyLocationConfigeration(config);
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            // 移动到某经纬度
            map.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomBy(5f);
            // 放大
            map.animateMapStatus(update);
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
        map = mapView.getMap();
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
