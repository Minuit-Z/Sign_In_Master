package com.zjmy.signin.view;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.skyfishjy.library.RippleBackground;
import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.presenters.activity.common.HistoryActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @Description: 登录
 * @authors: utopia
 * @Create time: 16-12-19 上午10:43
 * @Update time: 16-12-19 上午10:43
*/
public class SignView extends BaseViewImpl {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.img_sign)
    protected ImageView img;
    @Bind(R.id.tv_sign_location)
    protected TextView tv_loc;
    @Bind(R.id.tv_sign_loctype)
    protected TextView tv_loc_type;
    @Bind(R.id.btn_refresh)
    protected FloatingActionButton bt_refresh;
    @Bind(R.id.content)
    protected RippleBackground rippleBackground;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            BDLocation location= (BDLocation) msg.obj;
            String loc=location.getLocationDescribe().replaceFirst("在","");
            loc=loc.replace("附近","");
            loc=location.getAddrStr()+loc;
            switch (location.getLocType()){
                case BDLocation.TypeGpsLocation:
                    //GPS
                    tv_loc_type.setText("GPS定位");
                    break;
                case BDLocation.TypeNetWorkLocation:
                    tv_loc_type.setText("网络定位");
                    //网络定位
                    break;
                case BDLocation.TypeOffLineLocation:
                    tv_loc_type.setText("离线定位");
                    //离线定位
                    break;
                default:
                    tv_loc_type.setText("离线异常");
                    //异常
            }
            tv_loc.setText(loc);
        }
    };
    private AppCompatActivity activity;
    private LocationClient locationClient = null;
    @Override
    public int getRootViewId() {
        return R.layout.activity_sign;
    }

    @Override
    public void setActivityContext(AppCompatActivity appCompatActivity) {
        activity  = appCompatActivity;
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener((View v)-> {
                appCompatActivity.finish();
        });

        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_sign);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId()==R.id.menu_detail){
                    activity.startActivity(new Intent(activity, HistoryActivity.class));
                }
                return true;
            }
        });

        rippleBackground.startRippleAnimation();

    }


    @Override
    public void onPresenterDestory() {
    }

    @OnClick(R.id.img_sign)
    protected void sign(View v){
        Toast.makeText(activity, "ssss", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_refresh)
    protected void refresh(View v){
        showLocation(activity);
    }

    public void showLocation(Context context){
        locationClient = new LocationClient(context);
        //设置定位条件
        locationClient = new LocationClient(context);
        locationClient.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                Message msg=Message.obtain();
                msg.obj=bdLocation;
                handler.sendMessage(msg);

                Toast.makeText(activity, "已更新位置", Toast.LENGTH_SHORT).show();
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
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy); // 设置GPS优先  // 设置GPS优先
        option.disableCache(true);//禁止启用缓存定位
        option.setIsNeedLocationDescribe(true); //设置语义化结果
        locationClient.setLocOption(option);
        locationClient.start();
        locationClient.requestLocation();
    }
}
