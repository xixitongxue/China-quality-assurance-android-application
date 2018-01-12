package com.zhibaowang.component;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ZBoxView extends GLSurfaceView {

    private float xpos = -1;
    private float ypos = -1;
    ZBoxRender mRander;

    public ZBoxView(Context context, AttributeSet attrs) {
        super(context);
        // TODO Auto-generated constructor stub  

        // 使用自己实现的 EGLConfigChooser,该实现必须在setRenderer(renderer)之前
        // 如果没有setEGLConfigChooser方法被调用，则默认情况下，视图将选择一个与当前android.view.Surface兼容至少16位深度缓冲深度EGLConfig。
        setEGLConfigChooser(new EGLConfigChooser() {
            public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
                int[] attributes = new int[]{EGL10.EGL_DEPTH_SIZE, 16, EGL10.EGL_NONE};
                EGLConfig[] configs = new EGLConfig[1];
                int[] result = new int[1];
                egl.eglChooseConfig(display, attributes, configs, 1, result);
                return configs[0];
            }
        });

        mRander = new ZBoxRender(context);
        setRenderer(mRander);

        a = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (!touch) {
                        if (mRander.rotateY == 0 && mRander.rotateX == 0) {
                            mRander.rotateY = 0.001f;
                            mRander.rotateX = -0.001f;
                        }
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        a.start();
    }

    boolean touch;

    float x_press, y_press;
    long time_press;

    public boolean onTouchEvent(MotionEvent me) {

        if (me.getAction() == MotionEvent.ACTION_DOWN) {
            xpos = me.getX();
            ypos = me.getY();
            touch = true;
            x_press = me.getX();
            y_press = me.getY();
            time_press = System.currentTimeMillis();
            mRander.rotateY = 0;
            mRander.rotateX = 0;
            return true;
        }

        if (me.getAction() == MotionEvent.ACTION_UP) {
            xpos = -1;
            ypos = -1;
//            mRander.rotateY = 0;
//            mRander.rotateX = 0;
            touch = false;
            long during = System.currentTimeMillis() - time_press;
            float distance_x = (me.getX() - x_press) / -100f;
            float distance_y = (me.getY() - y_press) / -100f;
            float rate_x = distance_x / during * 10;
            float rate_y = distance_y / during * 10;
            mRander.rotateY = rate_x;
            mRander.rotateX = rate_y;

            return true;
        }

        if (me.getAction() == MotionEvent.ACTION_MOVE) {
            float xd = me.getX() - xpos;
            float yd = me.getY() - ypos;

            xpos = me.getX();
            ypos = me.getY();

            mRander.rotateY = xd / -100f;
            mRander.rotateX = yd / -100f;
            return true;
        }

        try {
            Thread.sleep(15);
        } catch (Exception e) {
            // No need for this...  
        }

        return super.onTouchEvent(me);
    }

    boolean flag = true;
    Thread a;

    public void close() {
        flag = false;
        if (a != null) {
            a.interrupt();
            a = null;
        }
    }
}
