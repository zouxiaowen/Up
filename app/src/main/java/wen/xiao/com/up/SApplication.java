package wen.xiao.com.up;

import android.app.Application;

import com.lzy.okgo.OkGo;

/**
 * Created by Administrator on 2017/9/21.
 */

public class SApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OkGo.getInstance().init(this);
    }
}
