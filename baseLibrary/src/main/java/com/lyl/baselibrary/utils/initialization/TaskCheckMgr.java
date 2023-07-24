package com.lyl.baselibrary.utils.initialization;


import com.blankj.utilcode.util.ThreadUtils;
import com.lyl.baselibrary.BaseApplication;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class TaskCheckMgr {
    private static TaskCheckMgr instance = new TaskCheckMgr();
    private ICheckTaskCallBack iCheckTaskCallBack;

    private TaskCheckMgr() {
    }

    public static TaskCheckMgr getInstance() {
        return instance;
    }

    private ArrayList<BaseCheckTask> blockingQueue = new ArrayList<>();
    private int blockingQueueI = 0;
    public void addTask(ArrayList<BaseCheckTask> baseCheckTaskList) {
        blockingQueue.addAll(baseCheckTaskList);
    }
    public void clearAll(){
        for (BaseCheckTask baseCheckTask : blockingQueue) {
            baseCheckTask.clear();
        }
    }
    public void startCheckTask(ICheckTaskCallBack iCheckTaskCallBack) {
        this.iCheckTaskCallBack = iCheckTaskCallBack;
        blockingQueueI = 0;
        ThreadUtils.getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                blockingQueueFor();
            }
        }, 100);
    }

    private void blockingQueueFor() {
        if (blockingQueueI >= blockingQueue.size()) {
            iCheckTaskCallBack.onAllTaskCheckEnd();
            return;
        }
        BaseCheckTask baseCheckTask = blockingQueue.get(blockingQueueI);

        baseCheckTask.setICheckCallBack(new ICheckCallBack() {
            @Override
            public void onCheckStart(CheckResultBean checkDeviceBean) {
                iCheckTaskCallBack.onCheckStart(checkDeviceBean);
            }

            @Override
            public void onCheckEnd(CheckResultBean checkDeviceBean) {
                if (checkDeviceBean.isState()) {
                    iCheckTaskCallBack.onCheckEnd(checkDeviceBean);
                    blockingQueueI++;
                    ThreadUtils.getMainHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            blockingQueueFor();
                        }
                    }, 100);

                } else {
                    iCheckTaskCallBack.onAllTaskCheckError(checkDeviceBean);
                }

            }
        });
        baseCheckTask.checkStart();
    }


    public void stopCheCkTask() {
        blockingQueueI = blockingQueue.size();
    }
}
