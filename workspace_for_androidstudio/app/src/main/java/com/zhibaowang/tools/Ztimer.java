package com.zhibaowang.tools;

/**
 * Created by zhaoyuntao on 2017/11/17.
 */

public class Ztimer extends Thread {

    /**
     * 倒计时时间
     */
    private int time_during = 60;
    /**
     * 设置回调
     */
    private CallBack callBack;
    /**
     * 防止已有的线程被启动两次
     */
    private boolean isStart = false;

    private Ztimer(int time_during) {
        this.time_during = time_during;
    }

    @Override
    public synchronized void start() {
        if (isStart) {
            return;
        } else {
            isStart = true;
            super.start();
        }
    }

    private static Ztimer zRequester;

    public static Ztimer getInstance(int time_during) {
        synchronized (Ztimer.class) {
            if (zRequester == null) {
                synchronized (Ztimer.class) {
                    zRequester = new Ztimer(time_during);
                    return zRequester;
                }
            } else {
                return null;
            }
        }
    }

    @Override
    public void run() {
        if (callBack != null) {
            while (time_during > 0) {

                callBack.whenRest(time_during);
                time_during--;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        callBack.whenRequestOk();
        close();
    }

    public void setCallBack(CallBack callBack) {
        if (zRequester != null) {
            zRequester.callBack = callBack;
        }
    }

    public void close() {
        if (zRequester != null) {
            zRequester.interrupt();
            zRequester = null;
        }
    }

    public interface CallBack {
        public void whenRequestOk();

        public void whenRest(int time_rest);
    }
}
