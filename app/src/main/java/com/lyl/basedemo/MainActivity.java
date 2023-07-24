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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }



}
