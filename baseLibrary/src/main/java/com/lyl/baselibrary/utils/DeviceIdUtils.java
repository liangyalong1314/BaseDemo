package com.lyl.baselibrary.utils;

/**
 * @author LiangYaLong
 * 描述:
 * 时间:     2021/3/18
 * 版本:     1.0
 */

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.blankj.utilcode.util.EncryptUtils;

import java.io.File;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * 设备工具类
 */
public class DeviceIdUtils {



    /**
     * 获取设备ID
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {

        StringBuilder sbDeviceId = new StringBuilder();

        String imei = getIMEI(context);
        String androidId = getAndroidId(context);
        String serial = getSerial();
        String uuid = getDeviceUUID();

        //附加imei
        if (imei != null && imei.length() > 0) {
            sbDeviceId.append(imei);
            sbDeviceId.append("|");
        }
        //附加androidId
        if (androidId != null && androidId.length() > 0) {
            sbDeviceId.append(androidId);
            sbDeviceId.append("|");
        }
        //附加serial
        if (serial != null && serial.length() > 0) {
            sbDeviceId.append(serial);
            sbDeviceId.append("|");
        }
        //附加uuid
        if (uuid != null && uuid.length() > 0) {
            sbDeviceId.append(uuid);
        }

        if (sbDeviceId.length() > 0) {
            try {
                String sha1 = EncryptUtils.encryptSHA1ToString(sbDeviceId.toString());
                if (sha1 != null && sha1.length() > 0) {
                    //返回最终的DeviceId
                    return sha1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    /**
     * 获取硬件的UUID
     *
     * @return
     */
    private static String getDeviceUUID() {
        String deviceId = "9527" + Build.ID +
                Build.DEVICE +
                Build.BOARD +
                Build.BRAND +
                Build.HARDWARE +
                Build.PRODUCT +
                Build.MODEL +
                Build.SERIAL;
        return new UUID(deviceId.hashCode(), Build.SERIAL.hashCode()).toString().replace("-", "");
    }

    private static String getSerial() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Build.getSerial();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取AndroidId
     *
     * @param context 上下文
     * @return AndroidId
     */
    private static String getAndroidId(Context context) {
        try {
            String androidId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            return androidId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 获取IMEI
     *
     * @param context 上下文
     * @return IMEI
     */
    private static String getIMEI(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager)
                    context.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }



}