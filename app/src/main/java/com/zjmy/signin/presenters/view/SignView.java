package com.zjmy.signin.presenters.view;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.zjmy.signin.model.bean.Sign;
import com.zjmy.signin.model.bean.Visit;
import com.zjmy.signin.presenters.activity.LocationActivity;
import com.zjmy.signin.utils.files.SPHelper;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class SignView extends BaseViewImpl {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.img_sign)
    protected ImageView img;
    @Bind(R.id.tv_sign_location)
    protected TextView tv_loc;
    @Bind(R.id.tv_sign_loctype)
    protected TextView tv_loc_type;

    @Bind(R.id.content)
    protected RippleBackground rippleBackground;
    @Bind(R.id.til_feedback_content)
    protected TextInputLayout til_feedback_content;
    @Bind(R.id.tv_behavior)
    protected TextView tv_behavior;

    private int status = 1000 ;
    private String time = "" , date = "" , objId = "";
    private BDLocation location;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            location= (BDLocation) msg.obj;
            String loc=location.getLocationDescribe().replaceFirst("在","");
            loc=loc.replace("附近","");
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

            tv_loc_type.setText(type+" — "+location.getRadius());
            tv_loc.setText(location.getAddrStr()+loc);
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
        activity = appCompatActivity;

        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener((View v) -> {
            appCompatActivity.finish();
        });


        rippleBackground.startRippleAnimation();

    }


    @Override
    public void onPresenterDestory() {
    }



    /**
     * @author 张子扬
     * @time 2017/3/23 0023 16:45
     * @desc 签退 17~19
     */
    private void doLogout() {
        String[] times = time.split(":");  // [时][分]
        if (Integer.parseInt(times[0]) >= 17 && Integer.parseInt(times[0]) <= 19 && status == 1) {
            if (date.isEmpty() || time.isEmpty() || objId == null) {
                return;
            }

            Sign sign = new Sign();
            sign.setEndTime(time);
            sign.setSignoutPlace(tv_loc.getText().toString());
            sign.update(objId, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        tv_behavior.setText("已签退");
                        status = 2;
                        Toast.makeText(activity, "已签退", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(activity, "非签退时间,签退失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @author 张子扬
     * @time 2017/3/23 0023 16:45
     * @desc 签到 8:00 到10点之间
     */
    private void doLogin() {
        String[] times = time.split(":");  // [时][分]
        if (Integer.parseInt(times[0]) >= 8 && Integer.parseInt(times[0]) <= 10 && status == 0) {
            //在签到时间,可以进行签到
            Sign sign = new Sign();
            sign.setDate(date);
            sign.setUser((String) SPHelper.getInstance(activity).getParam(SPHelper.USER, ""));
            sign.setName((String) SPHelper.getInstance(activity).getParam(SPHelper.NAME, ""));
            sign.setSigninPlace(tv_loc.getText().toString());
            sign.setStartTime(time);

            sign.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    tv_behavior.setText("下班签退");
                    status = 1 ;
                    Toast.makeText(activity, "签到完成", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "非签到时间,签到失败", Toast.LENGTH_SHORT).show();
        }
    }


    public void showLocation(Context context) {
        //设置定位条件
        locationClient = new LocationClient(context);
        locationClient.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                Log.e("test",bdLocation.getTime());
                if(bdLocation != null) {
                    Message msg = Message.obtain();
                    msg.obj = bdLocation;
                    handler.sendMessage(msg);
                }else{
                    locationClient.requestLocation();
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
        option.setScanSpan(1000);
        option.setIsNeedLocationDescribe(true); //设置语义化结果
        locationClient.setLocOption(option);
        locationClient.start();
        locationClient.requestLocation();
    }

    public void setPermissions(String error) {
        this.tv_loc_type.setText(error);
    }

    public void initViewBySign() {
        til_feedback_content.setVisibility(View.GONE);
        img.setOnClickListener((View view)-> {
                switch (status){
                    case 0 : doLogin();break;
                    case 1 : doLogout();break;
                    default: ;//异常处理
            }
        });
    }

    public void initViewByVisit() {
        til_feedback_content.setVisibility(View.VISIBLE);
        tv_behavior.setText("拜访记录");

        img.setOnClickListener((View view) ->{
            if(status==4) {
                String msg = til_feedback_content.getEditText().getText().toString();
                if (msg == null || msg.isEmpty()) {
                    til_feedback_content.setError("访问记录不能为空");
                } else {
                    doVisitIn(msg);
                }
            }else{
                Toast.makeText(activity, "今日已有拜访记录", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *@author 张子扬
     *@time 2017/3/23 0023 19:30
     *@desc 拜访打卡
     */
    private void doVisitIn(String msg) {

        if(status == 4) {
            Visit visit = new Visit();
            visit.setDate(date);
            visit.setMonth(date.split("-")[1]);
            visit.setName((String) SPHelper.getInstance(activity).getParam(SPHelper.NAME, ""));
            visit.setUser((String) SPHelper.getInstance(activity).getParam(SPHelper.USER, ""));
            visit.setLocation(tv_loc.getText().toString());
            visit.setSummary(msg);
            visit.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        tv_behavior.setText("已记录");
                        status = 5;
                        til_feedback_content.setError("数据已提交，禁止修改");
                        til_feedback_content.getEditText().setEnabled(false);
                        Toast.makeText(activity, "拜访完成", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(activity, "今日已有拜访记录", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_refresh)
    protected void refreshLocation(){
        Intent intent = new Intent(activity, LocationActivity.class);
        //intent.putExtra("latitude",location.getLatitude());
        //intent.putExtra("longitude",location.getLongitude());
        activity.startActivity(intent);
    }

    public void setSignBehavior(int status ,String date ,String time , String objId){
        this.status = status;
        this.time = time;
        this.date = date;
        this.objId = objId;
        switch (status){
            case 0 : tv_behavior.setText("上班签到"); break;//今日未签到
            case 1 : tv_behavior.setText("下班签退"); break;//今日未签退
            case 2 : tv_behavior.setText("已签退"); break;//今日已签退
            case 3 : tv_behavior.setText("未签到"); break;//今日未签到

            case 4 : tv_behavior.setText("拜访记录"); break;//今日未签到
            case 5 : tv_behavior.setText("已记录");
                til_feedback_content.getEditText().setText(objId);
                til_feedback_content.setError("数据已提交，禁止修改");
                til_feedback_content.getEditText().setEnabled(false);
                break;//今日未签到

            default:tv_behavior.setText("服务器异常"); break;//今日未签到
        }
    }

    public BDLocation getLocation(){
        return location;
    }
}
