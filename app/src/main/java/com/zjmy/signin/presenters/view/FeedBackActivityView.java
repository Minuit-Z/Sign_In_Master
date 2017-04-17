package com.zjmy.signin.presenters.view;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.inject.qualifier.model.bean.Feedback;
import com.zjmy.signin.utils.files.SPHelper;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


/**
 * @Description: 反馈
 * @authors: utopia
 * @Create time: 16-12-19 上午10:43
 * @Update time: 16-12-19 上午10:43
 */
public class FeedBackActivityView extends BaseViewImpl {
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.til_feedback_content)
    protected TextInputLayout til_feedback_content;
    @Bind(R.id.btn_feedback)
    protected Button _loginButton;
    @Bind(R.id.tv_phone)
    protected EditText tv_phone;
    @Bind(R.id.tv_title)
    protected TextView tv_title;
    private AppCompatActivity activity;

    @Override
    public int getRootViewId() {
        return R.layout.activity_feedback_layout;
    }

    @Override
    public void setActivityContext(AppCompatActivity activity) {
        this.activity=activity;
        toolbar.setTitle("问题反馈");
        toolbar.setTitleTextColor(activity.getResources().getColor(R.color.colorPrimary));
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable upArrow = activity.getResources().getDrawable(R.mipmap.back);
        upArrow.setColorFilter(activity.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        activity.getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener((View v) -> {
            activity.finish();
        });

        til_feedback_content.setCounterEnabled(true);
        til_feedback_content.setCounterMaxLength(200);

        tv_title.setText("");
    }

    @Override
    public void onPresenterDestory() {

    }

    public String getContent() {
        String content = til_feedback_content.getEditText().getText().toString();
        if (content == null || content.trim().equals("")) {
            til_feedback_content.setError("反馈内容不能为空");
            return "";
        } else {
            til_feedback_content.setError("");
        }
        return content;
    }

    @OnClick(R.id.btn_feedback)
    protected void feedback(View view){
        //提交反馈
        String content=getContent();
        if ("".equals(content)){
            return ;
        }else {
            //可以开始提交
            Feedback feedback=new Feedback();
            feedback.setName((String) SPHelper.getInstance(activity).getParam(SPHelper.NAME,""));
            feedback.setUser((String) SPHelper.getInstance(activity).getParam(SPHelper.USER,""));
            feedback.setFeedback(content);

            String phone="".equals(tv_phone.getText().toString())?"":tv_phone.getText().toString();
            feedback.setPhone(phone);
            feedback.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e==null){
                        //保存完成
                        Toast.makeText(activity, "反馈完成", Toast.LENGTH_SHORT).show();
                        activity.finish();
                    }else {
                        Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}
