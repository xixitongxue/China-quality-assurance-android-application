package com.zhibaowang.component;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.view.Display;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.zhibaowang.tools.S;
import com.zhibaowang.tools.ZScreenTool;

import java.io.IOException;
import java.util.List;

/**
 * Created by zhaoyuntao on 2017/11/24.
 */

public class ZCameraView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera camera;

    private float w_camera;


    private float h_camera;

    public ZCameraView(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        this.mHolder = getHolder();
        this.mHolder.addCallback(this);
        this.mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        int wh[] = ZScreenTool.getWH(context);
        int w_screen = wh[0]; // 竖屏状态下屏幕宽（像素）
        int h_screen = wh[1];// 竖屏状态下屏幕长（像素）
        float proportion_screen = h_screen / (float) w_screen;//长和宽比
        float proportion_photo_tmp = 0;//长宽的比例,临时记录合适的选项
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        List<Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();
        float w_camera_ok = 1, h_camera_ok = 1;
        for (int i = 0; i < previewSizes.size(); i++) {
            Camera.Size size_tmp = previewSizes.get(i);
            float w_camera = size_tmp.width;
            float h_camera = size_tmp.height;
            float proportion_camera = w_camera / h_camera;//相机横屏状态下的宽与长比
            float value_last = Math.abs(proportion_screen - proportion_camera);
            float value_now = Math.abs(proportion_screen - proportion_photo_tmp);
            if (value_last < value_now) {//如果本次比例更加接近屏幕长宽比
                proportion_photo_tmp = proportion_camera;
                w_camera_ok = w_camera;
                h_camera_ok = h_camera;
            } else if (value_last == value_now && w_camera_ok < w_camera) {//如果本次比例和之前的比例相同,
                // 但是本次的分辨率更高,则取本次的比例
                proportion_photo_tmp = proportion_camera;
                w_camera_ok = w_camera;
                h_camera_ok = h_camera;
            }
        }
        float w_photo_ok = 1, h_photo_ok = 1;
        for (int i = 0; i < pictureSizes.size(); i++) {
            Camera.Size size_tmp = pictureSizes.get(i);
            float w_photo = size_tmp.width;
            float h_photo = size_tmp.height;
            if (w_photo > 2500 || h_photo > 2500) {
                continue;
            }
            float proportion_photo = w_photo / h_photo;//相机横屏状态下所支持的照片宽与长比
            float value_last = Math.abs(proportion_screen - proportion_photo);
            float value_now = Math.abs(proportion_screen - proportion_photo_tmp);
            if (value_last < value_now) {//如果本次比例更加接近屏幕长宽比
                proportion_photo_tmp = proportion_photo;
                w_photo_ok = w_photo;
                h_photo_ok = h_photo;
            } else if (value_last == value_now && w_photo_ok < w_photo) {//如果本次比例和之前的比例相同,
                // 但是本次的分辨率更高,则取本次的比例
                proportion_photo_tmp = proportion_photo;
                w_photo_ok = w_photo;
                h_photo_ok = h_photo;
            }
        }
        S.s("已找到接近屏幕宽度(" + w_screen + " | " + h_screen + ")的最佳相机比例:" + w_camera_ok + " | " + h_camera_ok);
        S.s("已找到接近屏幕宽度(" + w_screen + " | " + h_screen + ")的最佳照片比例:" + w_photo_ok + " | " + h_photo_ok);
        parameters.setPreviewSize((int) w_camera_ok, (int) h_camera_ok);
        parameters.setPictureSize((int) w_photo_ok, (int) h_photo_ok);
        w_camera = w_camera_ok;
        h_camera = h_camera_ok;
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        camera.setParameters(parameters);
    }

    public float getH_camera() {
        return h_camera;
    }

    public float getW_camera() {
        return w_camera;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            float w_view = getWidth();
            float h_view = getHeight();
            float percent_view = w_view / h_view;
            float percent_camera = h_camera / w_camera;
            if (percent_view > percent_camera) {
                w_view = h_view * percent_camera;
            } else {
                h_view = w_view / percent_camera;
            }
//            FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams((int) w_view, (int) h_view);
//            fl.gravity = Gravity.CENTER_HORIZONTAL;
//            setLayoutParams(fl);
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (mHolder.getSurface() == null) {
            return;
        }
        try {
            camera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            camera.setPreviewDisplay(mHolder);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
