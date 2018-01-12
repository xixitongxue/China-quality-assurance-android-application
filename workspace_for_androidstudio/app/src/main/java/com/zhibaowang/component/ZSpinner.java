package com.zhibaowang.component;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Spinner;

import com.zhibaowang.app.HongboheshiApplication;
import com.zhibaowang.app.R;
import com.zhibaowang.tools.B;
import com.zhibaowang.tools.S;

import java.security.PermissionCollection;


/**
 * Created by zhaoyuntao on 2017/12/29.
 */

public class ZSpinner extends android.support.v7.widget.AppCompatSpinner {

    private Drawable drawable_right;
    private float percent_drawable_right = 1;
    private int color_back;
    int w_border;
    float radius;
    private Bitmap bitmap_right;

    public ZSpinner(Context context) {
        super(context);
        initView(context, null);
    }

    public ZSpinner(Context context, int mode) {
        super(context, mode);
        initView(context, null);
    }

    public ZSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ZSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public ZSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
        initView(context, attrs);
    }

    public ZSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
        initView(context, attrs);
    }
    public float[] getRadiusArray() {
        return radiusArray;
    }

    public void setRadiusArray(float[] radiusArray) {
        this.radiusArray = radiusArray;
        postInvalidate();
    }
    private float[] radiusArray = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
    private void initView(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.zspinner);//
            this.drawable_right = context.getResources().getDrawable(R.drawable.triangle_icon);
            this.bitmap_right = B.drawableToBitmap(drawable_right);
            this.percent_drawable_right = typedArray.getFloat(R.styleable.zspinner_percent_img_right_zspinner, 1);//
            this.color_back = typedArray.getColor(R.styleable.zspinner_color_back_spinner,Color.WHITE);
            float radius_spinner=typedArray.getDimension(R.styleable.zspinner_radius_spinner, 10);
            this.radiusArray[0] = radius_spinner;
            this.radiusArray[1] = radius_spinner;
            this.radiusArray[2] = radius_spinner;
            this.radiusArray[3] = radius_spinner;
            this.radiusArray[4] = radius_spinner;
            this.radiusArray[5] = radius_spinner;
            this.radiusArray[6] = radius_spinner;
            this.radiusArray[7] = radius_spinner;
            this.radius=radius_spinner;
        }
        this.percent_drawable_right = 0.35f;
        w_border = B.dip2px(context, 1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        Path path = new Path();
        path.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), radiusArray, Path.Direction.CW);
        canvas.clipPath(path);

        Paint paint=new Paint();
        canvas.drawColor(color_back);

        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0 || h > w) {
            return;
        }
        Bitmap bitmap = bitmap_right;
        if (bitmap != null) {
            int w_bitmap = bitmap.getWidth();
            int h_bitmap = bitmap.getHeight();
            Rect rect_src = new Rect();
            rect_src.set(0, 0, w_bitmap, h_bitmap);

            int h_bitmap_draw = (int) (h * percent_drawable_right);
            int scale = (int) ((1 - percent_drawable_right) * h / 2);
            int w_bitmap_draw = (int) (h_bitmap_draw * w_bitmap / h_bitmap);

            Rect rect_des = new Rect();
            rect_des.set(w - w_bitmap_draw - scale, scale, w - scale, h - scale);
            canvas.drawBitmap(bitmap, rect_src, rect_des, paint);
        }


        int color_border = HongboheshiApplication.color;

        if (w_border > 0) {
            float radius_min = (w > h ? h : w) / 2f;
            if (radius > radius_min) {
                radius = radius_min;
            }
            int w_rect = (int) (w - w_border);
            int h_rect = (int) (h - w_border);

            Paint paint_border = new Paint();
            paint_border.setStyle(Paint.Style.STROKE);
            paint_border.setStrokeWidth(w_border);
            paint_border.setColor(color_border);
            paint_border.setAntiAlias(true);
            paint_border.setColor(color_border);
            Path path_border = new Path();
            path_border.moveTo(w_border, radius);//0
            path_border.arcTo(new RectF(w_border, w_border, (int) (radius * 2), (int) (radius * 2)), 180, 90);//1
            path_border.lineTo(w_rect - radius, w_border);//2
            path_border.arcTo(new RectF((int) (w_rect - (radius * 2)), w_border, w_rect, (int) (radius * 2)), 270, 90);
            path_border.lineTo(w_rect, h_rect - radius);//4
            path_border.arcTo(new RectF((int) (w_rect - (radius * 2)), (int) (h_rect - (radius * 2)), w_rect, h_rect), 0, 90);
            path_border.lineTo(radius, h_rect);//6
            path_border.arcTo(new RectF(w_border, (int) (h_rect - radius * 2), (int) (radius * 2), h_rect), 90, 90);
            path_border.close();
            canvas.drawPath(path_border, paint_border);
        }

    }
}
