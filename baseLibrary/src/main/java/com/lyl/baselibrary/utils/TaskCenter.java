package com.lyl.baselibrary.utils;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * @Author lyl
 * @Date 2022/06/08
 */
public class TaskCenter {
    private static TaskCenter instance;

    //    Socket
    private Socket socket;
    //    IP地址
    private String ipAddress;
    //    端口号
    private int port;
    //    Socket输出流
    private OutputStream outputStream;
    //    Socket输入流
    private InputStream inputStream;
    //    连接回调
    private OnServerConnectedCallbackBlock connectedCallback;
    //    断开连接回调(连接失败)
    private OnServerDisconnectedCallbackBlock disconnectedCallback;
    //    接收信息回调
    private OnReceiveCallbackBlock receivedCallback;

    //    构造函数私有化
    private TaskCenter() {
        super();
    }

    //    提供一个全局的静态方法
    public static TaskCenter sharedCenter() {
        if (instance == null) {
            synchronized (TaskCenter.class) {
                if (instance == null) {
                    instance = new TaskCenter();
                }
            }
        }
        return instance;
    }

    /**
     * 通过IP地址(域名)和端口进行连接
     *
     * @param ipAddress IP地址(域名)
     * @param port      端口
     */
    public void connect(final String ipAddress, final int port) {

        ThreadUtils.executeByIo(new ThreadUtils.SimpleTask<Boolean>() {
            @Override
            public Boolean doInBackground() throws Throwable {
                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(ipAddress, port), 60 * 1000);//设置超时时间
                    if (isConnected()) {
                        TaskCenter.sharedCenter().ipAddress = ipAddress;
                        TaskCenter.sharedCenter().port = port;
                        outputStream = socket.getOutputStream();
                        inputStream = socket.getInputStream();
                        LogUtils.i("TCP：连接成功" + ipAddress + ":" + port);
                        return true;
                    } else {
                        LogUtils.i("TCP：连接失败");
                        return false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    LogUtils.i("TCP：" + "连接异常");
                    return false;
                }
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    receive();

                    if (connectedCallback != null) {
                        connectedCallback.callback();
                    }
                } else {
                    if (disconnectedCallback != null) {
                        disconnectedCallback.callback(new IOException("连接失败"));
                    }
                    ThreadUtils.executeByIoWithDelay(new ThreadUtils.SimpleTask<String>() {
                        @Override
                        public String doInBackground() throws Throwable {
                            return null;
                        }

                        @Override
                        public void onSuccess(String result) {
                            TaskCenter.sharedCenter().connect(ipAddress, port);
                            if (disconnectedCallback != null) {
                                disconnectedCallback.callback(new IOException("重新连接"));
                            }
                        }
                    }, 2000, TimeUnit.MILLISECONDS);
                }
            }
        });

    }

    /**
     * 判断是否连接
     */
    public boolean isConnected() {
        return socket.isConnected();
    }

    /**
     * 连接
     */
    public void connect() {
        connect(ipAddress, port);
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        if (isConnected()) {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                socket.close();
                if (socket.isClosed()) {
                    if (disconnectedCallback != null) {
                        disconnectedCallback.callback(new IOException("断开连接"));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 接收数据
     */
    public void receive() {
        ThreadUtils.executeByIo(new ThreadUtils.SimpleTask<Boolean>() {
            @Override
            public Boolean doInBackground() throws Throwable {
                while (isConnected()) {
                    try {
                        /**得到的是16进制数，需要进行解析*/
                        byte[] bt = new byte[1024];
//                获取接收到的字节和字节数
                        int length = inputStream.read(bt);
                        if (length < 0) {
                            continue;
                        }
//                获取正确的字节
                        byte[] bs = new byte[length];
                        System.arraycopy(bt, 0, bs, 0, length);

                        String str = new String(bs, "UTF-8");
                        if (str != null) {
                            if (receivedCallback != null) {
                                ThreadUtils.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String[] lines = str.split("\\n");
                                        for (String line : lines) {
                                            receivedCallback.callback(line);
                                            LogUtils.i("TCP：接收数据成功：" + line);
                                        }

                                    }
                                });

                            }
                        }

                    } catch (IOException e) {
                        return false;
                    }
                }
                return false;
            }

            @Override
            public void onSuccess(Boolean result) {
                if (!result) {
                    connect(ipAddress, port);
                }
            }
        });

    }

    /**
     * 发送数据
     *
     * @param data 数据
     */
    public void send(final String data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (socket != null) {
                    try {
                        outputStream.write(data.getBytes());
                        outputStream.flush();
                        LogUtils.i("TCP：发送成功 数据:" + data);
                    } catch (IOException e) {
                        e.printStackTrace();
                        LogUtils.i("TCP：发送失败");
                        connect();
                    }
                } else {
                    connect();
                }
            }
        }).start();

    }

    /**
     * 回调声明
     */
    public interface OnServerConnectedCallbackBlock {
        void callback();
    }

    public interface OnServerDisconnectedCallbackBlock {
        void callback(IOException e);
    }

    public interface OnReceiveCallbackBlock {
        void callback(String receicedMessage);
    }

    public void setConnectedCallback(OnServerConnectedCallbackBlock connectedCallback) {
        this.connectedCallback = connectedCallback;
    }

    public void setDisconnectedCallback(OnServerDisconnectedCallbackBlock disconnectedCallback) {
        this.disconnectedCallback = disconnectedCallback;
    }

    public void setReceivedCallback(OnReceiveCallbackBlock receivedCallback) {
        this.receivedCallback = receivedCallback;
    }

    /**
     * 移除回调
     */
    public void removeCallback() {
        TaskCenter.sharedCenter().connectedCallback = null;
        TaskCenter.sharedCenter().disconnectedCallback = null;

    }
}
