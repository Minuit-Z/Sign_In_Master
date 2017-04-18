package com.zjmy.signin.presenters.view;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.inject.qualifier.model.bean.Sign;
import com.zjmy.signin.inject.qualifier.model.bean.Visit;
import com.zjmy.signin.utils.app.JUtils;
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


/**
 * Created by Administrator on 2017/4/2 0002.
 */

public class CheckWorkFragmentView extends BaseViewImpl {

    @Bind(R.id.btn_tb_sign)
    protected RadioButton btn_sign;
    @Bind(R.id.btn_tb_visit)
    protected RadioButton btn_visit;
    @Bind(R.id.layout_main_sign)
    protected LinearLayout ll_sign;
    @Bind(R.id.layout_main_visit)
    protected LinearLayout ll_visit;
    @Bind(R.id.tv_ifsign)
    protected TextView tv_ifsign;  //是否签到,记录的条数
    @Bind(R.id.tv_week)
    protected TextView tvWeek;
    @Bind(R.id.ll_root)
    protected LinearLayout llRoot;
    @Bind(R.id.til_visit_record)
    protected TextInputLayout tilVisitRecord;
    @Bind(R.id.et_visit_record)
    protected EditText etVisitRecord;
    @Bind(R.id.tv_submit_visit)
    protected TextView tvSubmitVisit;
    @Bind(R.id.ll_signin)
    protected LinearLayout llSignin;
    @Bind(R.id.ll_signout)
    protected LinearLayout llSignout;
    //签到签退
    @Bind(R.id.tv_signin_status)
    protected TextView tv_signin;
    @Bind(R.id.tv_signout_status)
    protected TextView tv_signout;
    //时间相关
    @Bind(R.id.tv_clock)
    protected TextView tv_time;
    @Bind(R.id.tv_date)
    protected TextView tv_date;

    //位置相关
    @Bind(R.id.tv_main_loctype)
    protected TextView tv_loc_type;
    @Bind(R.id.tv_location)
    protected TextView tvLocation;
    private MyHandler handler = new MyHandler(this);

    //签到时需要的字段
    private String date;  //日期
    private String times; //时间
    private String month;//月

    private AppCompatActivity activity;

    //是否签到,拜访记录条数
    private String signData;
    private String visitData;
    private int flag = 1;//1代表打卡签到页2代表拜访记录页

    @Override
    public void onPresenterDestory() {
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_check_work_layout;
    }

    @Override
    public void setActivityContext(AppCompatActivity activity) {
        this.activity = activity;
    }

    @OnClick(R.id.btn_tb_sign)
    protected void changeToSign() {
        ll_sign.setVisibility(View.VISIBLE);
        ll_visit.setVisibility(View.INVISIBLE);
        flag = 1;
        initIfSign(signData);
        llRoot.setBackground(activity.getResources().getDrawable(R.mipmap.bg_sign));
    }

    @OnClick(R.id.btn_tb_visit)
    protected void changeToVisit() {
        ll_visit.setVisibility(View.VISIBLE);
        ll_sign.setVisibility(View.INVISIBLE);
        initIfSign(visitData);
        flag = 2;
        llRoot.setBackground(activity.getResources().getDrawable(R.mipmap.bg_visit));
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
        tilVisitRecord.setCounterEnabled(true);
        tilVisitRecord.setCounterMaxLength(200);
        etVisitRecord.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50), JUtils.emojiFilter});
    }

    //获取拜访的数据,并初始化UI
    private void getVisitRecord(String date) {
        BmobQuery<Visit> queryVisit = new BmobQuery<>();
        queryVisit.addWhereEqualTo("user", SPHelper.getInstance(activity).getParam(SPHelper.USER, ""));
        queryVisit.addWhereEqualTo("date", date);
        queryVisit.count(Visit.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    visitData = "今日" + integer + "条记录";
                    if (flag == 2) {
                        initIfSign(visitData);
                    }
                }
            }
        });
    }

    //获取签到记录,并初始化UI
    private void getSignRecord(String date) {
        BmobQuery<Sign> querySignRecord = new BmobQuery<>();
        querySignRecord.addWhereEqualTo("user", SPHelper.getInstance(activity).getParam(SPHelper.USER, ""));
        querySignRecord.addWhereEqualTo("date", date);
        querySignRecord.findObjects(new FindListener<Sign>() {
            @Override
            public void done(List<Sign> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        //未签到,显示尚未签到
                        signData = "今日未签到";
                        tv_signin.setText("未签到");
                        tv_signout.setText("未签退");
                    } else if (null == list.get(0).getSignoutPlace() || "".equals(list.get(0).getSigninPlace())) {
                        //未签退时
                        signData = "今日未签退";
                        tv_signin.setText("已签到");
                        tv_signout.setText("未签退");
                    } else {
                        //已经签退
                        signData = "已签退";
                        tv_signin.setText("已签到");
                        tv_signout.setText("已签退");
                    }
                    if (flag == 1) {
                        initIfSign(signData);
                    }
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
                    SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
                    times = formatTime.format(new Date(aLong * 1000L));
                    tv_time.setText(times);

                    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
                    date = formatDate.format(new Date(aLong * 1000L));
                    SimpleDateFormat formatMonth = new SimpleDateFormat("M");
                    month = formatMonth.format(new Date(aLong * 1000L));
                    SimpleDateFormat formatDay = new SimpleDateFormat("E");
                    String week = formatDay.format(new Date(aLong * 1000L));
                    SimpleDateFormat formatter3 = new SimpleDateFormat("M月d日");
                    tv_date.setText(formatter3.format(new Date(aLong * 1000L)));
                    tvWeek.setText(week);
                    SPHelper.getInstance(activity).setParam("date", date);
                    SPHelper.getInstance(activity).setParam("month", month);
                    getSignRecord(date);
                    getVisitRecord(date);
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
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        tv_time.setText(formatter.format(curDate));
    }

    public void showLocation(Context context, Application application) {
        if (NetworkUtil.checkNetWorkAvaluable(context)) {
            //设置定位条件
            LocationClient locationClient = new LocationClient(context);
            locationClient.registerLocationListener(new BDLocationListener() {
                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    if (bdLocation != null) {
                        System.out.println("定位类型" + bdLocation.getLocType());
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
            //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
            option.setLocationNotify(true);
            //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
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
        private final WeakReference<CheckWorkFragmentView> mView;
        private BDLocation location;


        public MyHandler(CheckWorkFragmentView view) {
            mView = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            CheckWorkFragmentView view = mView.get();
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
                if (mView.get() != null) {
                    view.tv_loc_type.setText("已通过" + type);
                    view.tvLocation.setText(location.getAddrStr() + loc);
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
    @OnClick(R.id.ll_signin)
    protected void signin() {
        //定位失败则无法签到
        if (tvLocation.getText().toString().equals("获取定位中...")) {
            Toast.makeText(activity, "定位失败,无法签到", Toast.LENGTH_SHORT).show();
            return;
        }
        if (signData.equals("今日未签到")) {
            //首先请求当日的类型,判断 工作日;节假日;休息日
            //TODO 此处可以用OkHttp优化
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("提示");
            builder.setMessage("确认签到?");
            builder.setNegativeButton("取消", null);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    llSignin.setEnabled(false);
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
                            sign.setSigninPlace(tvLocation.getText().toString());
                            sign.setDaytype("".equals(dayType) ? " " : dayType);
                            if (Integer.parseInt(month) > 0 && Integer.parseInt(month) < 10) {
                                sign.setMonth("0" + month);
                            } else {
                                sign.setMonth(month);
                            }
                            sign.setStartTime(tv_time.getText().toString());

                            sign.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(activity, "签到完成", Toast.LENGTH_SHORT).show();
                                        signData = "今日未签退";
                                        tv_signin.setText("已签到");
                                        tv_signout.setText("未签退");
                                        initIfSign(signData);
                                    } else {
                                        Toast.makeText(activity, "签到失败", Toast.LENGTH_SHORT).show();
                                    }
                                    llSignin.setEnabled(true);
                                }
                            });
                            //开始封装数据
                        } catch (IOException e) {
                            e.printStackTrace();
                            llSignin.setEnabled(true);
                        }
                    }).start();
                }
            });
            builder.show();
        }

    }

    /**
     * @author 张子扬
     * @time 2017/4/5 0005 18:35
     * @desc 签退操作
     */
    @OnClick(R.id.ll_signout)
    protected void signout() {
        //定位失败则无法签退
        if (tvLocation.getText().toString().equals("获取定位中...")) {
            Toast.makeText(activity, "定位失败,无法签退", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!signData.equals("已签退")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("提示");
            builder.setMessage("确认签退?");
            builder.setNegativeButton("取消", null);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    llSignout.setEnabled(false);
                    BmobQuery<Sign> query = new BmobQuery<>();
                    query.addWhereEqualTo("user", SPHelper.getInstance(activity).getParam(SPHelper.USER, ""));
                    query.addWhereEqualTo("date", date);
                    query.findObjects(new FindListener<Sign>() {
                        @Override
                        public void done(List<Sign> list, BmobException e) {
                            if (e == null) {
                                //可以签退
                                String objectId = list.get(0).getObjectId();
                                Sign sign = new Sign();
                                sign.setEndTime(tv_time.getText().toString());
                                sign.setSignoutPlace(tvLocation.getText().toString());
                                sign.update(objectId, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        //TODO 已签退 后续操作
                                        if (e == null) {
                                            Toast.makeText(activity, "签退完成", Toast.LENGTH_SHORT).show();
                                            signData = "已签退";
                                            tv_signout.setText("已签退");
                                            initIfSign(signData);
                                        } else {
                                            Toast.makeText(activity, "签退失败", Toast.LENGTH_SHORT).show();
                                        }
                                        llSignout.setEnabled(true);
                                    }
                                });
                            }
                        }
                    });
                }
            });
            builder.show();
        }

    }

    @OnClick(R.id.tv_submit_visit)
    public void submitVisitRecord() {
        if (TextUtils.isEmpty(etVisitRecord.getText().toString().trim())) {
            tilVisitRecord.setError("请输入拜访记录！");
        } else {
            if (NetworkUtil.checkNetWorkAvaluable(activity)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("提示");
                builder.setMessage("确认提交拜访记录?");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tvSubmitVisit.setEnabled(false);
                        //设置定位条件
                        LocationClient locationClient = new LocationClient(activity);
                        locationClient.registerLocationListener(new BDLocationListener() {
                            @Override
                            public void onReceiveLocation(BDLocation bdLocation) {
                                if (bdLocation != null) {
                                    Visit visit = new Visit();
                                    visit.setName((String) SPHelper.getInstance(activity).getParam(SPHelper.NAME, ""));
                                    String loc = bdLocation.getLocationDescribe().replaceFirst("在", "");
                                    loc = loc.replace("附近", "");
                                    visit.setLocation(bdLocation.getAddrStr() + loc);
                                    visit.setUser((String) SPHelper.getInstance(activity).getParam(SPHelper.USER, ""));
                                    visit.setDate(date);
                                    if (Integer.parseInt(month) > 0 && Integer.parseInt(month) < 10) {
                                        visit.setMonth("0" + month);
                                    } else {
                                        visit.setMonth(month);
                                    }
                                    visit.setSummary(etVisitRecord.getText().toString().trim());
                                    visit.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if (e == null) {
                                                Toast.makeText(activity, "提交成功", Toast.LENGTH_SHORT).show();
                                                getVisitRecord(date);
                                            } else {
                                                Toast.makeText(activity, "提交失败", Toast.LENGTH_SHORT).show();
                                            }
                                            tvSubmitVisit.setEnabled(true);
                                        }
                                    });
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
                });
                builder.show();
            }
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

    public String getDate() {
        return tv_date.getText().toString();
    }
}