package com.lyl.basedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.impl.utils.ContextUtil;
import androidx.lifecycle.LifecycleOwner;

import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.lyl.baselibrary.net.DownloadCallback;
import com.lyl.baselibrary.net.RetrofitFactory;
import com.lyl.baselibrary.net.RxNet;
import com.lyl.tencent_face.manager.FaceManager;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;

import io.reactivex.rxjava3.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    private TextView tvTotalM;

    private TextView tvDownloadM;

    private TextView tvProgress;

    private Disposable mDownloadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
    /**
     * 根据包名关闭一个后台应用，正处于前台的应用关不了，带通知栏的服务也属于前台进程，关闭不了
     * 需要权限KILL_BACKGROUND_PROCESSES
     * @param context
     * @param packageName
     */
    public static void killApps(Context context, String packageName) {
        try {
            ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            manager.killBackgroundProcesses(packageName);
            System.out.println("TimerV kill background: "+packageName+" successful");
        }catch(Exception ex) {
            ex.printStackTrace();
            System.err.println("TimerV kill background: "+packageName+" error!");
        }
    }
    private static Process process;
    /**
     * 初始化进程
     */
    private static void initProcess() {
        if (process == null)
            try {
                process = Runtime.getRuntime().exec("su");
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    /**
     * 结束进程
     */
    private static void killProcess(String packageName) {
        OutputStream out = process.getOutputStream();
        String cmd = "am force-stop " + packageName + " \n";
        try {
            out.write(cmd.getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
