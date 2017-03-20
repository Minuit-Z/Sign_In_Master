package ziye.mzsign;

import android.app.Application;

import cn.bmob.v3.Bmob;


/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class ZiyeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(getApplicationContext(),"f654080ef415d1b3dd5b65a2bbf75ee3");
    }
}
