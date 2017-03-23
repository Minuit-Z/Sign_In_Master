package com.zjmy.signin.view;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.zjmy.signin.model.bean.Sign;
import com.zjmy.signin.model.bean.Visit;
import com.zjmy.signin.presenters.activity.common.HistoryActivity;
import com.zjmy.signin.utils.files.SPHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnLongClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.zjmy.signin.utils.app.JUtils.TAG;

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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            BDLocation location = (BDLocation) msg.obj;
            String loc = location.getLocationDescribe().replaceFirst("在", "");
            loc = loc.replace("附近", "");
            loc = location.getAddrStr() + loc;
            switch (location.getLocType()) {
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
        activity = appCompatActivity;
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener((View v) -> {
            appCompatActivity.finish();
        });

        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_sign);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_detail) {
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

    @OnLongClick(R.id.img_sign)
    protected boolean sign(View v) {
        Toast.makeText(activity, "longclick", Toast.LENGTH_SHORT).show();
        Intent intent = activity.getIntent();
        String where = intent.getStringExtra("where");
        switch (where) {
            case "sign":
                doSignInOrOut(activity.getIntent());
                break;
            case "visit":
                doVisitInOrOut(activity.getIntent());
                break;
            default:
        }
        return true;
    }


    /**
    *@author 张子扬
    *@time 2017/3/23 0023 17:15
    *@desc 拜访的签到签退
    */
    private void doVisitInOrOut(Intent intent) {
        String date = intent.getStringExtra("date");

        BmobQuery<Visit> query=new BmobQuery<>();
    }

    /**
     * @author 张子扬
     * @time 2017/3/23 0023 16:30
     * @desc 打卡的签到签退
     */
    private void doSignInOrOut(Intent intent) {
        String date = intent.getStringExtra("date");
        //判断是签到还是签退
        BmobQuery<Sign> query = new BmobQuery<>();
        query.addWhereEqualTo("date", date);
        query.addWhereEqualTo("user", SPHelper.getInstance(activity).getParam(SPHelper.USER, ""));
        query.findObjects(new FindListener<Sign>() {
            @Override
            public void done(List<Sign> list, BmobException e) {
                if (!"".equals(list.get(0).getSignoutPlace())) {
                    // 已经签退,无法更新数据
                    Toast.makeText(activity, "已经完成签退", Toast.LENGTH_SHORT).show();
                } else {
                    if (e == null && list.size() == 0) {
                        //数据库中没有当日数据,进行签到
                        doLogin(intent);
                    } else if (e == null) {
                        //数据库有当日数据,签退
                        String objId = list.get(0).getObjectId();
                        doLogout(intent, objId);
                    } else {
                        Log.e(TAG, "done: " + e.toString());
                    }
                }
            }
        });
    }

    /**
     * @author 张子扬
     * @time 2017/3/23 0023 16:45
     * @desc 签退
     */
    private void doLogout(Intent intent, String objId) {
        Toast.makeText(activity, objId, Toast.LENGTH_SHORT).show();
        Sign sign = new Sign();
        sign.setEndTime(intent.getStringExtra("time"));
        sign.setSignoutPlace(tv_loc.getText().toString());
        sign.update(objId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null)
                    Toast.makeText(activity, "签退完成", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * @author 张子扬
     * @time 2017/3/23 0023 16:45
     * @desc 签到
     */
    private void doLogin(Intent intent) {
        String time = intent.getStringExtra("time");
        String[] times = time.split(":");  // [时][分]
        if (!(Integer.parseInt(times[0]) >= 8 && Integer.parseInt(times[0]) <= 10)) {
            //在签到时间,可以进行签到
            Sign sign = new Sign();
            sign.setDate(intent.getStringExtra("date"));
            sign.setUser((String) SPHelper.getInstance(activity).getParam(SPHelper.USER, ""));
            sign.setName((String) SPHelper.getInstance(activity).getParam(SPHelper.NAME, ""));
            sign.setSigninPlace(tv_loc.getText().toString());
            sign.setStartTime(intent.getStringExtra("time"));

            sign.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    Toast.makeText(activity, "签到完成", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e(TAG, "doLogin: 时间已过" + times[0] + "" + times[1]);
            Toast.makeText(activity, "签到时间已过......无法签到", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_refresh)
    protected void refresh(View v) {
        showLocation(activity);
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
