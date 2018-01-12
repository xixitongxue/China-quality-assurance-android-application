package com.zhibaowang.asynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import com.zhibaowang.tools.S;
import com.zhibaowang.tools.ZCameraTool;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhaoyuntao on 2017/12/1.
 */

public class ZAsyncTask_TakePhoto2 extends AsyncTask<Camera, Void, Void> {
    private CallBack callBack;

    public ZAsyncTask_TakePhoto2(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected Void doInBackground(Camera[] objects) {
        final Camera camera = objects[0];
        S.s("Camera is Null?" +(camera == null));
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(final byte[] data, android.hardware.Camera camera) {

                S.s("获取到照片原始信息data==null?:[ "+(data==null)+(data.length)+" ]");
//                File pictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//                final String picturePath = pictureDir + File.separator + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis())).toString() + ".jpg";
//                File file = new File(picturePath);
                try {
                    // 获取当前旋转角度, 并旋转图片
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    bitmap = ZCameraTool.rotateBitmapByDegree(bitmap, 90);
                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    byteArrayOutputStream.write(data);
                    byteArrayOutputStream.flush();
                    BufferedOutputStream bos = new BufferedOutputStream(byteArrayOutputStream);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                    byteArrayOutputStream.close();
                    callBack.whenGetPhoto(bitmap);
//                  bitmap_tmp.recycle();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    S.e(e);
                } catch (IOException e) {
                    e.printStackTrace();
                    S.e(e);
                }
//                camera.startPreview();
            }
        });
        return null;
    }


    @Override
    protected void onPostExecute(Void bitmap) {
    }

    public interface CallBack {
        void whenGetPhoto(Bitmap bitmap);
    }
}
