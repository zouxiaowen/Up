package wen.xiao.com.up;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button button;
    private  String   folder = "/sdcard/download/";
    private FlikerProgressBar roundProgressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button= (Button) findViewById(R.id.button);
        roundProgressbar = (FlikerProgressBar) findViewById(R.id.round_flikerbar);
        roundProgressbar.setOnClickListener(this);
        button.setOnClickListener(this);

    }


    @Override
    public void onClick(final View view) {
        switch (view.getId()){
            case  R.id.button:

                OkGo.<File>get("http://p3.exmmw.cn/p1/wq/360yingshidaquan.apk")
                        .tag(this)
                        .execute(new FileCallback(folder,"xiaomo.apk") {
                            @Override
                            public void onSuccess(Response<File> response) {
                                installAPK(folder+"/xiaomo.apk");
                                button.setVisibility(View.VISIBLE);
                                roundProgressbar.finishLoad();
                                roundProgressbar.setVisibility(View.GONE);
                                button.setText("下载完成");
                                button.setEnabled(false);
                            }

                            @Override
                            public void onError(Response<File> response) {
                                super.onError(response);
                                roundProgressbar.setVisibility(View.GONE);
                                button.setText("下载出错，请重试");
                                button.setVisibility(View.VISIBLE);

                            }

                            @Override
                            public void downloadProgress(Progress progress) {
                                super.downloadProgress(progress);
                                button.setVisibility(View.GONE);
                                roundProgressbar.setVisibility(View.VISIBLE);
                                roundProgressbar.setProgress( (int)(progress.fraction * 100));
                            }
                        });
                break;
            case R.id.round_flikerbar:
                break;
        }
    }
    /** 下载完成后自动安装apk */
    public void installAPK(String Apkpath) {
        File apkFile = new File( Apkpath);

        if (!apkFile.exists()) {
            return;
        }
        if (Build.VERSION.SDK_INT>=24){

            Uri apkUri = FileProvider.getUriForFile(this, "com.jph.takephoto.fileprovider", apkFile);
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.addCategory(Intent.CATEGORY_DEFAULT);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            startActivity(install);
        } else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setType("application/vnd.android.package-archive");
            intent.setData(Uri.fromFile(apkFile));
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }
    //打开APK程序代码

    private void openFile(File file) {
        // TODO Auto-generated method stub
        Log.e("OpenFile", file.getName());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }


}
