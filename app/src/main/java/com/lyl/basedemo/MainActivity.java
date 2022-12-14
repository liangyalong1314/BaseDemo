package com.lyl.basedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
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

        progressBar = findViewById(R.id.pb_progress);
        tvTotalM = findViewById(R.id.tv_total_m);
        tvDownloadM = findViewById(R.id.tv_download_m);
        tvProgress = findViewById(R.id.tv_progress);

        findViewById(R.id.btn_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ((EditText) findViewById(R.id.et_source)).getText().toString();
                if (!TextUtils.isEmpty(url)) {
                    downloadFile(url);
                }
            }
        });

        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitFactory.cancel(mDownloadTask);
                Toast.makeText(MainActivity.this, "停止下载", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void downloadFile(String url) {
        String path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "weather.apk";
        RxNet.download(url, path, new DownloadCallback() {
            @Override
            public void onStart(Disposable d) {
                mDownloadTask = d;
                LogUtils.d("onStart " + d);
                Toast.makeText(MainActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(long totalByte, long currentByte, int progress) {
                LogUtils.d("onProgress " + progress);
                progressBar.setProgress(progress);
                tvProgress.setText(progress + "%");
                tvTotalM.setText(byteFormat(totalByte));
                tvDownloadM.setText(byteFormat(currentByte));
            }

            @Override
            public void onFinish(File file) {
                LogUtils.d("onFinish " + file.getAbsolutePath());
                Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String msg) {
                LogUtils.d("onError " + msg);
            }
        });
    }

    private String byteFormat(long bytes) {
        BigDecimal fileSize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = fileSize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();
        if (returnValue > 1) {
            return (returnValue + "MB");
        }
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = fileSize.divide(kilobyte, 2, BigDecimal.ROUND_UP).floatValue();
        return (returnValue + "KB");
    }

}
