package com.zjmy.signin.view;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.model.bean.User;
import com.zjmy.signin.presenters.activity.common.MainActivity;
import com.zjmy.signin.utils.files.SPHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @Description: 登录
 * @authors: utopia
 * @Create time: 16-12-19 上午10:43
 * @Update time: 16-12-19 上午10:43
*/
public class LoginActivityView extends BaseViewImpl {
    @Bind(R.id.et_username)
    protected EditText _etUsername;
    @Bind(R.id.et_password)
    protected EditText _etPassword;
    @Bind(R.id.bt_go)
    protected Button _btGo;
    @Bind(R.id.cv)
    protected CardView cv;
    @Bind(R.id.fab)
    protected FloatingActionButton fab;
    @Bind(R.id.bg_login)
    protected RelativeLayout layout;

    private AppCompatActivity activity;
    @Override
    public int getRootViewId() {
        return R.layout.activity_login_layout;
    }

    @Override
    public void setActivityContext(AppCompatActivity appCompatActivity) {
        activity  = appCompatActivity;

        String userName = (String)SPHelper.getInstance(appCompatActivity).getParam(SPHelper.USER,"");
        if(userName!=null && !userName.trim().equals("")) {
            _etUsername.setText(userName);
            _etUsername.setSelection(userName.length());//调整光标位置
        }else{
            _etUsername.setSelection(0);//调整光标位置
        }
    }

    @OnClick(R.id.bt_go)
    protected void onClickFab(){
        String name = _etUsername.getText().toString();
        String pass = _etPassword.getText().toString();
        doLogin(name, pass);
    }

    private void doLogin(final String name, String pass) {

        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("password", pass);
        query.addWhereEqualTo("user", name);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null&&!list.isEmpty()) {
                    Log.e("test","success");
                    SPHelper.getInstance(activity).setParam(SPHelper.USER,list.get(0).getUser());
                    SPHelper.getInstance(activity).setParam(SPHelper.NAME,list.get(0).getName());
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    activity.finish();
                }else {
                    Toast.makeText(activity, "登录失败", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    @Override
    public void onPresenterDestory() {

    }
}
