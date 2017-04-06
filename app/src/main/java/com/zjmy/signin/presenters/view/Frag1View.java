package com.zjmy.signin.presenters.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.model.bean.Sign;
import com.zjmy.signin.model.bean.Visit;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/4/2 0002.
 */

public class Frag1View extends BaseViewImpl {

    @Bind(R.id.btn_tb_sign)
    protected Button btn_sign;
    @Bind(R.id.btn_tb_visit)
    protected Button btn_visit;
    @Bind(R.id.layout_main_sign)
    protected LinearLayout ll_sign;
    @Bind(R.id.layout_main_visit)
    protected LinearLayout ll_visit;
    @Bind(R.id.tv_ifsign)
    protected TextView tv_ifsign;  //是否签到,记录的条数
    @Bind(R.id.root_frag1)
    protected RelativeLayout root; //根布局,用来切换颜色

    //签到签退
    @Bind(R.id.img_signin)
    protected ImageView img_signin;
    @Bind(R.id.img_signout)
    protected ImageView img_signout;
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
    private MyHandler handler = new MyHandler(this);

    //签到时需要的字段
    private String location; //位置
    private String date;  //日期
    private String times; //时间

    private AppCompatActivity activity;

    //是否签到,拜访记录条数
    private String signDAta;
    private String visitDAta;


    @Override
    public void onPresenterDestory() {
    }

    @Override
    public int getRootViewId() {
        return R.layout.frag1;
    }

    @Override
    public void setActivityContext(AppCompatActivity activity) {
        this.activity = activity;
    }

    @OnClick(R.id.btn_tb_sign)
    protected void changeToSign() {
        ll_sign.setVisibility(View.VISIBLE);
        ll_visit.setVisibility(View.INVISIBLE);
        initIfSign(signDAta);
        root.setBackground(activity.getResources().getDrawable(R.mipmap.bg_sign));
    }

    @OnClick(R.id.btn_tb_visit)
    protected void changeToVisit() {
        ll_visit.setVisibility(View.VISIBLE);
        ll_sign.setVisibility(View.INVISIBLE);
        initIfSign(visitDAta);
        root.setBackground(activity.getResources().getDrawable(R.mipmap.bg_visit));
    }

    public void init() {
        // 每1分钟更新一次时间
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

        getSignRecord();
        getVisitRecord();
    }

    //获取拜访的数据,并初始化UI
    private void getVisitRecord() {
        BmobQuery<Visit> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("user", SPHelper.getInstance(activity).getParam(SPHelper.USER, ""));
        query2.addWhereEqualTo("date", tv_date.getText().toString());
        query2.count(Visit.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    visitDAta = "今日" + integer + "条数据";
                }
            }
        });
    }

    //获取签到记录,并初始化UI
    private void getSignRecord() {
        BmobQuery<Sign> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user", SPHelper.getInstance(activity));
        query1.addWhereEqualTo("date", tv_date.getText().toString());
        query1.findObjects(new FindListener<Sign>() {
            @Override
            public void done(List<Sign> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        //未签到,显示尚未签到
                        signDAta = "今日未签到";
                    } else if (null == list.get(0).getSignoutPlace() || "".equals(list.get(0).getSigninPlace())) {
                        //未签退时
                        signDAta = "今日未签退";
                    } else {
                        //已经签退
                        signDAta = "已签退";
                    }
                    initIfSign(signDAta); //初始化一次记录
                }
            }
        });
    }

    /**
     * @author 张子扬
     * @time 2017/4/2 0002 17:13
     * @desc 获取服务器时间
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
                    SPHelper.getInstance(activity).setParam("date",date);
                } else {
                    Log.i("bmob", "获取服务器时间失败:" + e.getMessage());
                }
            }
        });
    }

    /**
     * @author 张子扬
     * @time 2017/4/2 0002 17:13
     * @desc 获取系统时间
     */
    public void initClock() {
        Log.e(TAG, "initClock: ");
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        tv_time.setText(formatter.format(curDate));
    }

    public void showLocation(Context context) {
        if (NetworkUtil.checkNetWorkAvaluable(context)) {
            //设置定位条件
            locationClient = new LocationClient(context);
            locationClient.registerLocationListener(new BDLocationListener() {

                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    if (bdLocation != null) {
                        Log.e(TAG, "onReceiveLocation: " + Thread.currentThread().getName());
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
            option.setIsNeedLocationDescribe(true); //设置语义化结果
            locationClient.setLocOption(option);
            locationClient.start();
        }
    }

    public void setPermissions(String error) {
        tv_loc_type.setText(error);
    }


    /**
     * @author 张子扬
     * @time 2017/4/2 0002 17:36
     * @desc 内部类Handler, 处理定位结果
     */
    private static class MyHandler extends Handler {
        private final WeakReference<Frag1View> mView;
        private BDLocation location;


        public MyHandler(Frag1View view) {
            mView = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            Frag1View view = mView.get();
            location = (BDLocation) msg.obj;
            if (location != null && location.getLocationDescribe() != null) {
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
                if(mView.get()!= null) {
                    view.tv_loc_type.setText(type + " — " + location.getRadius());
                    view.tv_location.setText(location.getAddrStr() + loc);
                }
            }
        }
    }

    /**
     * @param
     * @author 张子扬
     * @time 2017/4/5 0005 10:24
     * @desc
     */
    private void initIfSign(String msg) {
        tv_ifsign.setText(msg);
    }

    /**
     * @author 张子扬
     * @time 2017/4/5 0005 16:57
     * @desc 签到操作
     */
    @OnClick(R.id.img_signin)
    protected void signin() {
        //定位失败则无法签到
        if (tv_location.getText().toString().equals("定位中...")) {
            Toast.makeText(activity, "定位失败,无法签到", Toast.LENGTH_SHORT).show();
            return;
        }
        //首先请求当日的类型,判断 工作日;节假日;休息日
        //TODO 此处可以用OkHttp优化
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
                //获取日期类型
                String dayType = CheckDayType.parseJson(json.toString(), date.replaceAll("-", ""));

                Sign sign = new Sign();
                sign.setDate(date);
                sign.setUser((String) SPHelper.getInstance(activity).getParam(SPHelper.USER, ""));
                sign.setName((String) SPHelper.getInstance(activity).getParam(SPHelper.NAME, ""));
                sign.setSigninPlace(tv_location.getText().toString());
                sign.setDaytype("".equals(dayType) ? " " : dayType);
                sign.setMonth(date.split("-")[1]);
                sign.setStartTime(tv_time.getText().toString());

                sign.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e==null){
                            Toast.makeText(activity, "签到完成", Toast.LENGTH_SHORT).show();
                            img_signin.setClickable(false);
                            initIfSign("今日未签退");
                        }
                    }
                });
                //开始封装数据
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
    *@author 张子扬
    *@time 2017/4/5 0005 18:35
    *@desc 签退操作
    */
    @OnClick(R.id.img_signout)
    protected void signout(){
        BmobQuery<Sign> query=new BmobQuery<>();
        query.addWhereEqualTo("user",SPHelper.getInstance(activity).getParam(SPHelper.USER,""));
        query.addWhereEqualTo("date",tv_date.getText());
        query.findObjects(new FindListener<Sign>() {
            @Override
            public void done(List<Sign> list, BmobException e) {
                if (e==null){
                    if (list.size()==0){
                        //未签到状态
                        return ;
                    }else {
                        //可以签退
                        String objectId = list.get(0).getObjectId();
                        Sign sign=new Sign();
                        sign.setEndTime(tv_time.getText().toString());
                        sign.setSignoutPlace(tv_location.getText().toString());
                        sign.update(objectId, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                //TODO 已签退 后续操作
                                img_signout.setEnabled(false);
                                Toast.makeText(activity, "签退完成", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
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

    public String getDate(){
        return tv_date.getText().toString();
    }
}
