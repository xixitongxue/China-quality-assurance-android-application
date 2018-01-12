package com.zhibaowang.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhaoyuntao on 2017/12/27.
 */

public class ZTestView extends View {
    public ZTestView(Context context) {
        super(context);
    }

    public ZTestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ZTestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.RED);
        int w_border=4;
        int color_border= Color.BLACK;
        int radius=10;
        int w=600;
        int h=600;
        Paint paint_border=new Paint();
        paint_border.setStyle(Paint.Style.STROKE);
        paint_border.setStrokeWidth(w_border);
        paint_border.setColor(color_border);
        paint_border.setAntiAlias(true);
            paint_border.setColor(color_border);
        Path path_border=new Path();
        path_border.moveTo(0,radius);//0
        path_border.arcTo(new RectF(0,0,(int)(radius*2),(int)(radius*2)),180,90);//1
        path_border.lineTo(w-radius,0);//2
        path_border.arcTo(new RectF((int)(w-(radius*2)),0,w,(int)(radius*2)),270,90);
        path_border.lineTo(w,h-radius);//4
        path_border.arcTo(new RectF((int)(w-(radius*2)),(int)(h-(radius*2)),w,h),0,90);
        path_border.lineTo(radius,h);//6
        path_border.arcTo(new RectF(0,(int)(h-radius*2),(int)(radius*2),h),90,90);
//            path_border.lineTo(0,radius);//8
        path_border.close();
//            rect_destination_back.set(0, 0, w-10, h-10);
//            canvas.drawRoundRect(rect_destination_back, radius, radius, paint_border);
        canvas.drawPath(path_border,paint_border);
    }
}
