package com.lyl.baselibrary.utils;

import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.RomUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ThrowableUtils;
import com.blankj.utilcode.util.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 全局异常捕获工具类
 *
 * @author alm
 */
public class MyCrashUtils {

    private static final String FILE_SEP = System.getProperty("file.separator");

    private MyCrashUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Initialization.
     */
    public static void init() {
        init("");
    }

    /**
     * Initialization
     *
     * @param crashDir The directory of saving crash information.
     */
    public static void init(@NonNull final File crashDir) {
        init(crashDir.getAbsolutePath(), null);
    }

    /**
     * Initialization
     *
     * @param crashDirPath The directory's path of saving crash information.
     */
    public static void init(final String crashDirPath) {
        init(crashDirPath, null);
    }

    /**
     * Initialization
     *
     * @param onCrashListener The crash listener.
     */
    public static void init(final OnCrashListener onCrashListener) {
        init("", onCrashListener);
    }

    /**
     * Initialization
     *
     * @param crashDir The directory of saving crash information.
     * @param onCrashListener The crash listener.
     */
    public static void init(@NonNull final File crashDir, final OnCrashListener onCrashListener) {
        init(crashDir.getAbsolutePath(), onCrashListener);
    }

    /**
     * Initialization
     *
     * @param crashDirPath The directory's path of saving crash information.
     * @param onCrashListener The crash listener.
     */
    public static void init(final String crashDirPath, final OnCrashListener onCrashListener) {
        String dirPath;
        if (StringUtils.isSpace(crashDirPath)) {
            if (SDCardUtils.isSDCardEnableByEnvironment()
                    && Utils.getApp().getExternalFilesDir(null) != null) {
                dirPath = Utils.getApp().getExternalFilesDir(null) + FILE_SEP + "crash" + FILE_SEP;
            } else {
                dirPath = Utils.getApp().getFilesDir() + FILE_SEP + "crash" + FILE_SEP;
            }
        } else {
            dirPath = crashDirPath.endsWith(FILE_SEP) ? crashDirPath : crashDirPath + FILE_SEP;
        }
        Thread.setDefaultUncaughtExceptionHandler(
                getUncaughtExceptionHandler(dirPath, onCrashListener));
    }

    private static Thread.UncaughtExceptionHandler getUncaughtExceptionHandler(final String dirPath,
                                                                               final OnCrashListener onCrashListener) {
        return new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull final Thread t, @NonNull final Throwable e) {
                final String time = new SimpleDateFormat("yyyy_MM_dd-HH_mm_ss").format(new Date());
                MyCrashUtils.CrashInfo info = new MyCrashUtils.CrashInfo(time, e);
                final String crashFile = dirPath + time + ".txt";
                FileIOUtils.writeFileFromString(crashFile, info.toString(), true);

                if (onCrashListener != null) {
                    onCrashListener.onCrash(info);
                }
            }
        };
    }

    ///////////////////////////////////////////////////////////////////////////
    // interface
    ///////////////////////////////////////////////////////////////////////////

    public interface OnCrashListener {
        void onCrash(CrashInfo crashInfo);
    }

    public static final class CrashInfo {
        private FileHead mFileHeadProvider;
        private Throwable mThrowable;

        private CrashInfo(String time, Throwable throwable) {
            mThrowable = throwable;
            mFileHeadProvider = new FileHead("Crash");
            mFileHeadProvider.addFirst("Time Of Crash", time);
        }

        public final void addExtraHead(Map<String, String> extraHead) {
            mFileHeadProvider.append(extraHead);
        }

        public final void addExtraHead(String key, String value) {
            mFileHeadProvider.append(key, value);
        }

        public final Throwable getThrowable() {
            return mThrowable;
        }

        @Override
        public String toString() {
            return mFileHeadProvider.toString() + ThrowableUtils.getFullStackTrace(mThrowable);
        }
    }
    static final class FileHead {

        private String                        mName;
        private LinkedHashMap<String, String> mFirst = new LinkedHashMap<>();
        private LinkedHashMap<String, String> mLast  = new LinkedHashMap<>();

        FileHead(String name) {
            mName = name;
        }

        void addFirst(String key, String value) {
            append2Host(mFirst, key, value);
        }

        void append(Map<String, String> extra) {
            append2Host(mLast, extra);
        }

        void append(String key, String value) {
            append2Host(mLast, key, value);
        }

        private void append2Host(Map<String, String> host, Map<String, String> extra) {
            if (extra == null || extra.isEmpty()) {
                return;
            }
            for (Map.Entry<String, String> entry : extra.entrySet()) {
                append2Host(host, entry.getKey(), entry.getValue());
            }
        }

        private void append2Host(Map<String, String> host, String key, String value) {
            if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
                return;
            }
            int delta = 19 - key.length(); // 19 is length of "Device Manufacturer"
            if (delta > 0) {
                key = key + "                   ".substring(0, delta);
            }
            host.put(key, value);
        }

        public String getAppended() {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : mLast.entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            return sb.toString();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            String border = "************* " + mName + " Head ****************\n";
            sb.append(border);
            for (Map.Entry<String, String> entry : mFirst.entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }

            sb.append("Rom Info           : ").append(RomUtils.getRomInfo()).append("\n");
            sb.append("Device Manufacturer: ").append(Build.MANUFACTURER).append("\n");
            sb.append("Device Model       : ").append(Build.MODEL).append("\n");
            sb.append("Android Version    : ").append(Build.VERSION.RELEASE).append("\n");
            sb.append("Android SDK        : ").append(Build.VERSION.SDK_INT).append("\n");
            sb.append("App VersionName    : ").append(AppUtils.getAppVersionName()).append("\n");
            sb.append("App VersionCode    : ").append(AppUtils.getAppVersionCode()).append("\n");

            sb.append(getAppended());
            return sb.append(border).append("\n").toString();
        }
    }
}
