package com.zhibaowang.tools;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

public class B {

    private static Map<String, Bitmap> bitmaps = new HashMap<String, Bitmap>();

    public static void close() {
        bitmaps.clear();
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static void b(View v, int drawableId, Context context, int num) {
        Bitmap drawable = getBitmapById_Percent(context, drawableId, num);
        if (drawable != null) {
            v.setBackground(new BitmapDrawable(drawable));
        }
    }

    public static void b(View v, int drawableId, Context context) {
        b(v, drawableId, context, 0);
    }

    public static void b_debug(View v, int drawableId, Context context, int linenumber) {
        S.s("line number:" + linenumber);
        b(v, drawableId, context, 0);
    }

    /**
     * 获取圆形图片方法
     *
     * @param bitmap_src
     * @return Bitmap
     * @author caizhiming
     */
    public static Bitmap getBitmap_circle(Bitmap bitmap_src) {
        if(bitmap_src==null){
            return null;
        }
        Paint paint = new Paint();
        int w_bitmap = bitmap_src.getWidth();
        int h_bitmap = bitmap_src.getHeight();
        int doubleRadius=w_bitmap;
        if (w_bitmap > h_bitmap) {
            doubleRadius = h_bitmap;
        }
        Bitmap bitmap_des = Bitmap.createBitmap(w_bitmap, h_bitmap, Bitmap.Config.ARGB_8888);
        Canvas canvas_des = new Canvas(bitmap_des);
        final Rect rect = new Rect(0, 0, bitmap_src.getWidth(), bitmap_src.getHeight());
        paint.setAntiAlias(true);
        canvas_des.drawARGB(0, 0, 0, 0);

        canvas_des.drawCircle(w_bitmap / 2, h_bitmap / 2, doubleRadius/2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas_des.drawBitmap(bitmap_src, rect, rect, paint);

        //二次裁剪把图片裁剪为一个正方形
        Bitmap bitmap_des_final=Bitmap.createBitmap(bitmap_des,(w_bitmap-doubleRadius)/2,(h_bitmap-doubleRadius)/2,doubleRadius,doubleRadius);
        bitmap_des.recycle();
        return bitmap_des_final;
    }

    public static Bitmap getBitmap_circle_addBorder(Bitmap bitmap_src){
        if(bitmap_src==null){
            return null;
        }
        Paint paint = new Paint();
        int w_bitmap = bitmap_src.getWidth();
        int h_bitmap = bitmap_src.getHeight();
        int doubleRadius=w_bitmap;
        if (w_bitmap > h_bitmap) {
            doubleRadius = h_bitmap;
        }
        Bitmap bitmap_des = Bitmap.createBitmap(w_bitmap, h_bitmap, Bitmap.Config.ARGB_8888);
        Canvas canvas_des = new Canvas(bitmap_des);
        Rect rect_src=new Rect();
        rect_src.set(0,0,w_bitmap,h_bitmap);
        Rect rect_des=new Rect();
        rect_des.set(0,0,w_bitmap,h_bitmap);
        canvas_des.drawBitmap(bitmap_src,rect_src,rect_des,paint);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        canvas_des.drawCircle(w_bitmap / 2, h_bitmap / 2, doubleRadius/2, paint);
        return bitmap_des;
    }

    /**
     * 产生一个多边形的图片
     *
     * @param bitmap_src
     * @param positions
     * @return
     */
    public static Bitmap getBitmap_polygon(Bitmap bitmap_src, int[][] positions) {
        if(bitmap_src==null){
            return null;
        }
        Bitmap bitmap_des = Bitmap.createBitmap(bitmap_src.getWidth(), bitmap_src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas_des = new Canvas(bitmap_des);
        Paint p = new Paint();
        p.setAntiAlias(true);
        Path path = new Path();
        for (int i = 0; i < positions.length; i++) {
            if (positions[i].length > 1) {
                float x = positions[i][0];
                float y = positions[i][1];
                if (i == 0) {
                    path.moveTo(x, y);
                } else if (i == (positions.length - 1)) {
                    path.lineTo(x, y);
                }
            }
        }
        path.close();
        p.setStyle(Paint.Style.FILL);
        p.setStrokeWidth(3);
        canvas_des.drawPath(path, p);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas_des.drawBitmap(bitmap_src, 0, 0, p);
        p.setXfermode(null);
        return bitmap_des;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void b(View v, String name, Context context, int num) {
        b(v, getIdByName(context, name), context, num);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        return new BitmapDrawable(bitmap);
    }

    /**
     * 放大或者缩小一张图片
     *
     * @param bitmap
     * @param percent
     * @return
     */
    public static Bitmap getBitmapByPercent(Bitmap bitmap, float percent) {
        return getBitmapByPercent(bitmap, percent, percent);
    }

    public static Bitmap getBitmapByPercent(Bitmap bitmap, float percent_w, float percent_h) {
        Matrix matrix = new Matrix();
        matrix.postScale(percent_w, percent_h); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    public static Bitmap getBitmapByWH(Bitmap bitmap, int w_des, int h_des) {
        int w_bitmap = bitmap.getWidth();
        int h_bitmap = bitmap.getHeight();
        float percnet_w = (float) w_des / w_bitmap;
        float percnet_h = (float) h_des / h_bitmap;
        return getBitmapByPercent(bitmap, percnet_w, percnet_h);
    }

    public static Bitmap getBitmapById_Percent(Context context, int drawableId, int num) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inPreferredConfig = Bitmap.Config.RGB_565;
        option.inPurgeable = true;
        option.inInputShareable = true;
        if (num > 0) {
            option.inSampleSize = num;
        }
        if (bitmaps.containsKey(String.valueOf(drawableId))) {
            return bitmaps.get(String.valueOf(drawableId));
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId, option);
            bitmaps.put(String.valueOf(drawableId), bitmap);
            return bitmap;
        }
    }

    public static Drawable getDrawableById_Percent(Context context, int drawableId) {
        return context.getResources().getDrawable(drawableId);
    }

    public static Bitmap rotate(Bitmap bitmap, float angle) {
        if (bitmap == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    public static int getIdByName(Context context, String name) {
        int resID = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        return resID;
    }

    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }
}
