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
import com.baidu.mapapi.map.BaiduMap;
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
    private LocationClient locationClient;
    private MyHandler handler;
    private BaiduMap map;
    @Override
    public Class<LocationView> getRootViewClass() {
        return LocationView.class;
    }

    @Override
    public void inCreat(Bundle savedInstanceState) {
        activityComponent.inject(this);
        rxPermissions = new RxPermissions(this); // where this is an Activity instance
        handler = new MyHandler(this);

        map = v.getMapView().getMap();

        map.setMyLocationEnabled(true);

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.icon_gcoding);
        MyLocationConfiguration config = new MyLocationConfiguration(null, true, bitmapDescriptor);
        map.setMyLocationConfigeration(config);


        MapStatusUpdate update = MapStatusUpdateFactory.zoomBy(5f);
        // 放大
        map.animateMapStatus(update);

        //申请定位权限
        rxPermissions
                .request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        showLocation(this);
                    } else {
                        //v.setPermissions("获取定位权限失败");
                    }
                });
    }

    @Override
    public Activity getContext() {
        return this;
    }

    @Override
    protected void onResume() {
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        v.getMapView().onResume();

        super.onResume();
    }

    @Override
    protected void onPause() {
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        v.getMapView().onPause();

        super.onPause();
    }

    public void showLocation(Context context) {
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
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setIsNeedAddress(true); //需要地址信息
        option.setOpenGps(true);
        option.setScanSpan(2000);//每秒定位一次
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy); // 设置GPS优先  // 设置GPS优先
        option.setIsNeedLocationDescribe(true); //设置语义化结果
        locationClient.setLocOption(option);
        locationClient.start();
        locationClient.requestLocation();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        locationClient.stop();
        // 关闭定位图层
        map.setMyLocationEnabled(false);
        v.getMapView().onDestroy();

        super.onDestroy();
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
            location = (BDLocation) msg.obj;
            if (activity != null && activity.v!=null && activity.map!=null) {
                activity.v.getTv_location().setText(location.getAddrStr() + location.getLocationDescribe());
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

                MyLocationData data = new MyLocationData.Builder()
                        .latitude(location.getLatitude()).longitude(location.getLongitude()).build();

                activity.map.setMyLocationData(data);

                // 移动到某经纬度
                activity.map.animateMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            }
        }
    }

    ;
}
