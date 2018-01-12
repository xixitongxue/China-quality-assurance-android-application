package com.zhibaowang.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zhibaowang.tools.S;

/**
 * Created by zhaoyuntao on 2018/1/4.
 */

public class ZShowView extends View {
    public ZShowView(Context context) {
        super(context);
        init();
    }

    public ZShowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZShowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    boolean flag = true;
    Thread a;

    private void init() {
        a = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                int direction = 1;
                while (flag) {
                    if (distance_o1 >= 900) {
                        direction = -1;
                    } else if (distance_o1 <= 300) {
                        direction = 1;
                    }
                    distance_o1 += direction;
                    distance_o2 += direction;
                    postInvalidate();
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

    float x_origin=0;
    float y_origin=0;
    float x_o1=300;
    float y_o1=300;
    float x_o2=300;
    float y_o2=100;
    float x_eye=300;
    float y_eye=400;
    int fps = 0;
    float distance_eye = 100;
    Bitmap bitmap_back;
    float distance_o1 = 500;
    float distance_o2 = 400;
    float radius_o1_real = 300;
    float radius_o2_real = 200;

    @Override
    public void onDraw(Canvas canvas) {
        int w_view = getWidth();
        int h_view = getHeight();
        if (w_view <= 0 || h_view <= 0) {
            return;
        }

        float x_o1_absolute = x_origin+x_o1;
        float y_o1_absolute = y_origin+y_o1;
        float x_o2_absolute = x_origin+x_o2;
        float y_o2_absolute = y_origin+y_o2;
        float x_eye_absolue=x_origin+x_eye;
        float y_eye_absolue=y_origin+y_eye;

        Paint paint = new Paint();
        if (bitmap_back == null) {
            bitmap_back = Bitmap.createBitmap(w_view, h_view, Bitmap.Config.ARGB_8888);
            Canvas canvas_back = new Canvas(bitmap_back);
            int color_back = Color.parseColor("#66ccff");
            canvas_back.drawColor(color_back);
            paint.setColor(Color.WHITE);
            float w_line = 10;
            float count_line_x = w_view / w_line;
            float count_line_y = h_view / w_line;
            for (int x = 0; x < count_line_x; x++) {
                for (int y = 0; y < count_line_y; y++) {
                    float x_line = x * w_line;
                    float y_line = y * w_line;
                    canvas_back.drawLine(0, y_line, w_view, y_line, paint);
                    canvas_back.drawLine(x_line, 0, x_line, h_view, paint);
                }
            }
        } else {
            Rect rect_src = new Rect();
            rect_src.set(0, 0, bitmap_back.getWidth(), bitmap_back.getHeight());
            Rect rect_des = new Rect();
            rect_src.set(0, 0, w_view, h_view);
            canvas.drawBitmap(bitmap_back, rect_src, rect_des, paint);
        }


        float radius_o1_draw = distance_eye * radius_o1_real / distance_o1;
        float radius_o2_draw = distance_eye * radius_o2_real / distance_o2;




        Paint paint_o1 = new Paint();
        paint_o1.setColor(Color.argb(200, 0, 255, 0));
        canvas.drawCircle(x_o1_absolute, y_o1_absolute, radius_o1_draw, paint_o1);

        Paint paint_o2 = new Paint();
        paint_o2.setColor(Color.argb(200, 0, 255, 255));
        canvas.drawCircle(x_o2_absolute, y_o2_absolute, radius_o2_draw, paint_o2);

        Paint paint_text = new Paint();
        paint_text.setColor(Color.BLACK);
        paint_text.setTextSize(30);
        canvas.drawText("[" + fps++ + "]", 30, 30, paint_text);
    }

    public void close() {
        flag = false;
        if (a != null) {
            a.interrupt();
            a = null;
        }
    }
}
