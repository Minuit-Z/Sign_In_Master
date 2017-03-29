package com.zjmy.signin.presenters.view;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.model.bean.Feedback;
import com.zjmy.signin.model.bean.User;
import com.zjmy.signin.utils.app.AppManager;
import com.zjmy.signin.utils.app.IdManager;
import com.zjmy.signin.utils.files.SPHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static android.content.ContentValues.TAG;


/**
 * @Description: 反馈
 * @authors: utopia
 * @Create time: 16-12-19 上午10:43
 * @Update time: 16-12-19 上午10:43
 */
public class BindActivityView extends BaseViewImpl {
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.tv_title)
    protected TextView tv_title;
    private AppCompatActivity activity;
    @Bind(R.id.tv_bindaty_phone)
    protected TextView tv_phone;
    @Bind(R.id.tv_bindaty_id)
    protected TextView tv_id;
    @Bind(R.id.btn_bind)
    protected Button btn_bind;

    private int mark = 0; //标记为1为已经绑定,2为未绑定

    @Override
    public int getRootViewId() {
        return R.layout.activity_bind_layout;
    }

    @Override
    public void setActivityContext(AppCompatActivity activity) {
        this.activity = activity;
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable upArrow = activity.getResources().getDrawable(R.mipmap.back);
        upArrow.setColorFilter(activity.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        activity.getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener((View v) -> {
            activity.finish();
        });

        tv_title.setText("绑定设备");

        // 设置设备ID和用户id
        tv_id.setText(IdManager.getAndroidId(activity));
        tv_phone.setText(SPHelper.getInstance(activity).getParam(SPHelper.USER, "").toString());

        // 设置解绑还是绑定
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("user", SPHelper.getInstance(activity).getParam(SPHelper.USER, ""));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null && list.size() > 0) {
                    if (list.get(0).getAndroidId() == null||list.get(0).getAndroidId() == "") {
                        btn_bind.setText("绑定");
                        mark = 2;
                    } else {
                        btn_bind.setText("解绑");
                        mark = 1;
                    }
                }else {

                }
            }
        });
    }

    @Override
    public void onPresenterDestory() {

    }

    @OnClick(R.id.btn_bind)
    protected void feedback(View view) {

        if (mark == 2) {
            //未绑定,开始绑定
            User user = new User();
            user.setAndroidId(IdManager.getAndroidId(activity));
            user.update((String) SPHelper.getInstance(activity).getParam(SPHelper.OBJID, ""), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        AppManager.getAppManager().finishActivity();
                        Toast.makeText(activity, "绑定", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if (mark==1){
            //已经绑定,开始解绑
            User user = new User();
            user.setAndroidId("");
            user.update((String) SPHelper.getInstance(activity).getParam(SPHelper.OBJID, ""), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        AppManager.getAppManager().finishActivity();
                        Toast.makeText(activity, "解绑完成", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if (mark==0){
            Log.e(TAG, "feedback: error" );
        }
    }
}
