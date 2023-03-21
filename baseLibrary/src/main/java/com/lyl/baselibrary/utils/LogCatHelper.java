package com.lyl.baselibrary.utils;

/**
 * @author LiangYaLong
 * 描述:
 * 时间:     2020/6/18
 * 版本:     1.0
 */


import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.TimeUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Administrator
 * <p>
 * log打印日志保存,文件的保存以小时为单位
 * permission:<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.READ_LOGS" />
 */
public class LogCatHelper {
    private static LogCatHelper instance = null;
    /**
     * 保存路径
     */
    private final String dirPath;
    private final String cmds;
    private LogCatHelper(String path) {
        dirPath = path;
        cmds = "logcat *:v | grep \"(" + android.os.Process.myPid() + ")\"";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * @param path log日志保存根目录
     * @return 本类单例
     */
    public static LogCatHelper getInstance(String path) {
        if (instance == null) {

            instance = new LogCatHelper(path);
        }
        return instance;
    }
    private Process mProcess;
    private BufferedReader mReader;
    /**
     * 启动log日志保存
     */
    public void init() {
        ThreadUtils.executeByIo(task);

    }

    ThreadUtils.SimpleTask<String> task = new ThreadUtils.SimpleTask<String>() {
        @Override
        public String doInBackground() throws Throwable {
            try {
                mProcess = Runtime.getRuntime().exec(cmds);
                mReader = new BufferedReader(new InputStreamReader(mProcess.getInputStream()), 1024);
                String line;
                while ((line = mReader.readLine()) != null) {
                    if (line.length() == 0) {
                        continue;
                    }
                    saveToFile(line);

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mProcess != null) {
                    mProcess.destroy();
                    mProcess = null;
                }
                try {
                    if (mReader != null) {
                        mReader.close();
                        mReader = null;
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            return null;
        }

        @Override
        public void onSuccess(String result) {

        }
    };
    private void saveToFile(String line) {
        FileWriter fileWriter = null;
        try {
            File file = new File(dirPath, "log-" +  TimeUtils.getSafeDateFormat("yyyy-MM-dd-HH").format(TimeUtils.getNowDate())+ ".log");
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter = new FileWriter(file, true);
            fileWriter.append(TimeUtils.getSafeDateFormat("yyyy-MM-dd HH:mm:ss").format(TimeUtils.getNowDate()))
                    .append("	")
                    .append(line)
                    .append("\r\n");
            fileWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
