package com.zjmy.signin.presenters.view;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
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


    private AppCompatActivity activity;


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

        map.setMyLocationEnabled(true);

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_gcoding);
        MyLocationConfiguration config = new MyLocationConfiguration(null, true, bitmapDescriptor);
        map.setMyLocationConfigeration(config);


        MapStatusUpdate update = MapStatusUpdateFactory.zoomBy(5f);
        // 放大
        map.animateMapStatus(update);
    }


    @Override
    public void onPresenterDestory() {
    }

    public TextView getTv_accuracy() {
        return tv_accuracy;
    }

    public TextView getTv_location() {
        return tv_location;
    }

    public BaiduMap getMap() {
        return map;
    }

    public MapView getMapView() {
        return mapView;
    }
}
