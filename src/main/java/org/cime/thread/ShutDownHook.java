/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package org.cime.thread;

import java.util.List;

/**
 * <h1>程序关闭钩子线程</h1>
 * <p>
 * 负责在程序中断或者退出后，执行清理工作
 * </p>
 */
public class ShutDownHook extends Thread {

    /**
     * 存放启动的线程对象
     */
    private List<DataGenRunnable> runList;

    public ShutDownHook(List<DataGenRunnable> runList) {
        this.runList = runList;
    }

    @Override
    public void run() {
        try {

            //通知所有线程停止运行
            if (null != runList) {
                for (DataGenRunnable dataGenRunnable : runList) {
                    dataGenRunnable.setShutDown(true);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
