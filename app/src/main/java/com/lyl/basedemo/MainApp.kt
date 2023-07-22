package com.lyl.basedemo

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ThreadUtils
import com.blankj.utilcode.util.TimeUtils
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit


/**
 *   作者：梁亚龙
 *   时间：2023/7/19
 *   描述：
 **/
class MainApp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ThreadUtils.executeByFixedAtFixRate(2, task, 1000, TimeUnit.MILLISECONDS)
    }
    var SimpleDateFormatHM = SimpleDateFormat("HH:mm:ss")
    private var task: ThreadUtils.SimpleTask<String> = object : ThreadUtils.SimpleTask<String>() {

        override fun doInBackground(): String {

//            if (SimpleDateFormatHM.format(TimeUtils.getNowDate())=="16:07"){
//                AppUtils.launchApp("com.fubao.elderly")
//            }
            return SimpleDateFormatHM.format(TimeUtils.getNowDate())
        }


        override fun onSuccess(result: String) {

            findViewById<TextView>(R.id.tv).text=result
                if (result=="18:05:00"){
                    AppUtils.launchApp("com.alibaba.android.rimet")
                }
            if (result=="08:30:00"){
                AppUtils.launchApp("com.alibaba.android.rimet")
            }
        }
    }
    /*

 * 启动一个app

 */

    /*

 * 启动一个app

 */
    fun startAPP(appPackageName: String?) {
        try {
            val intent = this.packageManager.getLaunchIntentForPackage(appPackageName!!)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "没有安装", Toast.LENGTH_LONG).show()
        }
    }

}