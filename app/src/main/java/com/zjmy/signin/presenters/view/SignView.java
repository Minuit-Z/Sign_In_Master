package com.zjmy.signin.presenters.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.zjmy.signin.utils.network.CheckDayType;
import com.zjmy.signin.utils.network.NetworkUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
    @Bind(R.id.root_sign)
    protected RelativeLayout root;

    @Bind(R.id.tv_title)
    protected TextView tv_title;
    private int status = 1000;
    private String time = "", date = "", objId = "";


    private AppCompatActivity activity;
    private LocationClient locationClient = null;
    private final MyHandler handler = new MyHandler(this);

    @Override
    public int getRootViewId() {
        return R.layout.activity_sign;
    }

    @Override
    public void setActivityContext(AppCompatActivity appCompatActivity) {
        activity = appCompatActivity;

        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable upArrow = activity.getResources().getDrawable(R.mipmap.back);
        upArrow.setColorFilter(activity.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener((View v) -> {
            appCompatActivity.finish();
        });


        til_feedback_content.setCounterEnabled(true);
        til_feedback_content.setCounterMaxLength(200);

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
        if (status == 1) {
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
        }
    }

    /**
     * @author 张子扬
     * @time 2017/3/23 0023 16:45
     * @desc 签到 6:00 到13点之间
     */
    private void doLogin() throws MalformedURLException {
        if (tv_loc.getText().toString().equals("定位中...")) {
            Toast.makeText(activity, "定位失败,无法签到", Toast.LENGTH_SHORT).show();
            return;
        }
        //首先请求当日的类型,判断 工作日;节假日;休息日
        //TODO
        new Thread(() -> {
            //拼接URL
            URL url = getUrlFromDay(date);
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                InputStream in = connection.getInputStream();
                // 下面对获取到的输入流进行读取
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(in));
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }
                reader.close();
                in.close();
                String dayType = CheckDayType.parseJson(json.toString(), date.replaceAll("-", ""));

                String[] times = time.split(":");  // [时][分]
                if (status == 0) {
                    //在签到时间,可以进行签到
                    Sign sign = new Sign();
                    sign.setDate(date);
                    sign.setUser((String) SPHelper.getInstance(activity).getParam(SPHelper.USER, ""));
                    sign.setName((String) SPHelper.getInstance(activity).getParam(SPHelper.NAME, ""));
                    sign.setSigninPlace(tv_loc.getText().toString());
                    sign.setDaytype("".equals(dayType) ? " " : dayType);
                    sign.setMonth(date.split("-")[1]);
                    sign.setStartTime(time);

                    sign.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            tv_behavior.setText("下班签退");
                            status = 1;
                            Toast.makeText(activity, "签到完成", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ).start();
    }


    public void showLocation(Context context) {

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

    public void stopLocation() {
        if (locationClient != null) {
            locationClient.stop();
        }
    }

    private static class MyHandler extends Handler {
        private final WeakReference<SignView> mView;
        private BDLocation location;

        public MyHandler(SignView view) {
            mView = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            SignView view = mView.get();
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

                view.tv_loc_type.setText(type + " — " + location.getRadius());
                view.tv_loc.setText(location.getAddrStr() + loc);
            }
        }
    }

    public void setPermissions(String error) {
        this.tv_loc_type.setText(error);
    }

    public void initViewBySign() {
        root.setBackgroundColor(activity.getResources().getColor(R.color.white));
        til_feedback_content.setVisibility(View.GONE);
        img.setOnClickListener((View view) -> {
            switch (status) {
                case 0:
                    try {
                        doLogin();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    doLogout();
                    break;
                default:
                    ;//异常处理
            }
        });
    }

    public void initViewByVisit() {
        root.setBackgroundColor(activity.getResources().getColor(R.color.backgroundColor));
        til_feedback_content.setVisibility(View.VISIBLE);
        tv_behavior.setText("拜访记录");
        tv_title.setText("拜访记录");
        img.setOnClickListener((View view) -> {
            if (status == 4) {
                String msg = til_feedback_content.getEditText().getText().toString();
                if (msg == null || msg.isEmpty()) {
                    til_feedback_content.setError("访问记录不能为空");
                } else {
                    doVisitIn(msg);
                }
            } else {
                Toast.makeText(activity, "禁止连续提交记录", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * @author 张子扬
     * @time 2017/3/23 0023 19:30
     * @desc 拜访打卡
     */
    private void doVisitIn(String msg) {

        if (status == 4) {
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
        } else {
            Toast.makeText(activity, "禁止连续提交记录", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_refresh)
    protected void refreshLocation() {
        Intent intent = new Intent(activity, LocationActivity.class);
        //intent.putExtra("latitude",location.getLatitude());
        //intent.putExtra("longitude",location.getLongitude());
        activity.startActivity(intent);
    }

    public void setSignBehavior(int status, String date, String time, String objId) {
        this.status = status;
        this.time = time;
        this.date = date;
        this.objId = objId;
        switch (status) {
            case 0:
                tv_behavior.setText("上班签到");
                break;//今日未签到
            case 1:
                tv_behavior.setText("下班签退");
                break;//今日未签退
            case 2:
                tv_behavior.setText("已签退");
                break;//今日已签退
            case 3:
                tv_behavior.setText("未签到");
                break;//今日未签到

            case 4:
                tv_behavior.setText("拜访记录");
                break;//今日未签到
            case 5:
                tv_behavior.setText("已记录");
                til_feedback_content.getEditText().setText(objId);
                til_feedback_content.setError("数据已提交，禁止修改");
                til_feedback_content.getEditText().setEnabled(false);
                break;//今日未签到

            default:
                tv_behavior.setText("服务器异常");
                break;//今日未签到
        }
    }

    /**
     * @param day 日期
     * @author 张子扬
     * @time 2017/3/29 0029 14:05
     * @desc 拼接Url
     */
    private URL getUrlFromDay(String day) {
        day = day.replaceAll("-", "");
        try {
            return new URL("http://www.easybots.cn/api/holiday.php?d=" + day);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
