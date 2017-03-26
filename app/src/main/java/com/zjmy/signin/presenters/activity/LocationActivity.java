package com.zjmy.signin.presenters.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zjmy.signin.R;
import com.zjmy.signin.presenters.view.LocationView;

import java.lang.ref.WeakReference;

public class LocationActivity extends BaseActivity<LocationView> {
    private RxPermissions rxPermissions;
    private LocationClient locationClient ;
    private final MyHandler handler = new MyHandler(this);

    @Override
    public Class<LocationView> getRootViewClass() {
        return LocationView.class;
    }

    @Override
    public void inCreat(Bundle savedInstanceState) {
        activityComponent.inject(this);

        rxPermissions = new RxPermissions(this); // where this is an Activity instance

    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //申请定位权限
        rxPermissions
                .request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        showLocation(getApplicationContext());
                    } else {
                        //v.setPermissions("获取定位权限失败");
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(locationClient!=null){
            locationClient.stop();
        }
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

    private static class MyHandler extends Handler {

        private final WeakReference<LocationActivity> mActivity;
        private BDLocation location;

        public MyHandler(LocationActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LocationActivity activity = mActivity.get();
            location= (BDLocation) msg.obj;
            if(activity!=null) {
                String loc = location.getLocationDescribe().replaceFirst("在", "");
                loc = loc.replace("附近", "");
                activity.v.getTv_location().setText(location.getAddrStr() + loc);
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
                activity.v.getTv_accuracy().setText(type + " — " + location.getRadius());

                activity.v.getMap().setMyLocationEnabled(true);
                MyLocationData data = new MyLocationData.Builder()
                        .latitude(location.getLatitude()).longitude(location.getLongitude()).build();

                activity.v.getMap().setMyLocationData(data);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.mipmap.icon_gcoding);
                MyLocationConfiguration config = new MyLocationConfiguration(null, true, bitmapDescriptor);
                activity.v.getMap().setMyLocationConfigeration(config);
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
                // 移动到某经纬度
                activity.v.getMap().animateMapStatus(update);
                update = MapStatusUpdateFactory.zoomBy(5f);
                // 放大
                activity.v.getMap().animateMapStatus(update);
            }
        }
    };
}
