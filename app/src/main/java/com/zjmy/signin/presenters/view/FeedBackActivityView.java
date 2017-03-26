package com.zjmy.signin.presenters.view;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;

import butterknife.Bind;



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


    @Override
    public int getRootViewId() {
        return R.layout.activity_feedback_layout;
    }

    @Override
    public void setActivityContext(AppCompatActivity activity) {
        toolbar.setTitle("问题反馈");
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener((View v) -> {
            activity.finish();
        });

        til_feedback_content.setCounterEnabled(true);
        til_feedback_content.setCounterMaxLength(200);

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
}
