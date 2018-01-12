package com.zhibaowang.tools;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Surface;

/**
 * Created by zhaoyuntao on 2017/12/1.
 */

public class ZCameraTool {

    private Camera camera;
    private int cameraId;

    public Camera initCamera(Activity activity,int resultCode) {
        S.s("初始化Camera权限");
        if (!checkCameraHardware(activity)) {
            S.e("Camera不支持");
           return null;
        } else {
            if (Build.VERSION.SDK_INT > 22) {
                if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    S.s("abcd","开始获取Camera权限");
                    //先判断有没有权限 ，没有就在这里进行权限的申请
                    ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.CAMERA}, resultCode);
                }
            }
            S.s("abcd","获取Camera权限完毕");
            cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
            camera = getCameraInstance();
            setCameraDisplayOrientation(activity, cameraId, camera);
            //说明已经获取到摄像头权限了
            return camera;
        }
    }
    // 判断相机是否支持
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    // 获取相机
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            S.e(e);
        }
        return c;
    }
    // 设置相机横竖屏
    public void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        if(camera==null){
            return;
        }
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    // 切换前置和后置摄像头
    public void switchCamera(Activity context,int resultCode) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, cameraInfo);
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        initCamera(context,resultCode);
    }
    // 旋转图片
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }
    // 释放相机
    public void releaseCamera() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
}
