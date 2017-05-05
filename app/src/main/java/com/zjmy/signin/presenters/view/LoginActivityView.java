package com.zjmy.signin.presenters.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.utopia.mvp.view.BaseViewImpl;
import com.zjmy.signin.R;
import com.zjmy.signin.inject.qualifier.model.bean.User;
import com.zjmy.signin.presenters.SignInApplication;
import com.zjmy.signin.presenters.activity.MainActivity;
import com.zjmy.signin.presenters.activity.SplashActivity;
import com.zjmy.signin.utils.app.IdManager;
import com.zjmy.signin.utils.files.SPHelper;
import com.zjmy.signin.utils.network.NetworkUtil;

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
    protected EditText etUsername;
    @Bind(R.id.et_password)
    protected EditText etPassword;
    @Bind(R.id.bt_login)
    protected Button btLogin;

    private AppCompatActivity activity;

    @Override
    public int getRootViewId() {
        return R.layout.activity_login_layout;
    }

    @Override
    public void setActivityContext(AppCompatActivity appCompatActivity) {
        activity = appCompatActivity;

        String userName = (String) SPHelper.getInstance(appCompatActivity).getParam(SPHelper.USER, "");
        if (userName != null && !userName.trim().equals("")) {
            etUsername.setText(userName);
            etUsername.setSelection(userName.length());//调整光标位置
        } else {
            etUsername.setSelection(0);//调整光标位置
        }
    }

    @OnClick(R.id.bt_login)
    protected void onClickLogin() {
        String name = etUsername.getText().toString();
        String pass = etPassword.getText().toString();

        if (name.isEmpty()) {
            etUsername.setError("用户名不能为空");
        } else if (pass.isEmpty()) {
            etPassword.setError("密码不能为空");
        } else {
            doLogin(name, pass);
        }
    }

    private void doLogin(final String name, String pass) {
        if (NetworkUtil.checkNetWorkAvaluable(activity)) {
            BmobQuery<User> query = new BmobQuery<>();
            query.addWhereEqualTo("password", pass);
            query.addWhereEqualTo("user", name);
            query.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> list, BmobException e) {
                    if (e == null && !list.isEmpty()) {
                        if (list.get(0).getAndroidId() == null || list.get(0).getAndroidId() == ""
                                || list.get(0).getAndroidId().equals(IdManager.getAndroidId(activity))){
                            // 未绑定设备,或者绑定设备的id等于本设备id,登陆成功
                            SPHelper.getInstance(activity).setParam(SPHelper.USER, list.get(0).getUser());
                            SPHelper.getInstance(activity).setParam(SPHelper.NAME, list.get(0).getName());
                            SPHelper.getInstance(activity).setParam(SPHelper.OBJID, list.get(0).getObjectId());
                            SPHelper.getInstance(activity).setParam(SPHelper.PASS_WORD, list.get(0).getPassword());
                            SPHelper.getInstance(activity).setParam(SPHelper.IDENTITY, list.get(0).getIdentity());
                            SPHelper.getInstance(activity).setParam(SPHelper.DEPARTMENT, list.get(0).getDepartment());
                            SPHelper.getInstance(activity).setParam(SPHelper.CENTER, list.get(0).getCenter());

                            SignInApplication.userName = list.get(0).getName();
                            activity.startActivity(new Intent(activity, MainActivity.class));
                            Toast.makeText(activity, "登录完成", Toast.LENGTH_SHORT).show();
                            activity.finish();
                        } else {
                            if (!list.get(0).getAndroidId().equals(IdManager.getAndroidId(activity))) {
                                etPassword.setError("账号已被绑定,无法在其他设备登录");
                            }
                        }
                    } else {
                        etPassword.setError("密码错误");
                    }
                }
            });
        }
    }

    @Override
    public void onPresenterDestory() {

    }
}
