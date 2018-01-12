package com.zhibaowang.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.zhibaowang.app.R;
import com.zhibaowang.tools.B;
import com.zhibaowang.tools.S;

/**
 * Created by zhaoyuntao on 2017/11/13.
 */

public class ZButton extends FrameLayout {
    private String text_center;
    private String text_left;
    private String text_right;
    private String textarr;
    /**
     * false: 点击时被选中,再次点击不会取消选中状态,需要用户主动去掉被选中的状态(默认)
     * true: 点击时被选中, 再次点击时去除被选中的状态
     */
    private boolean isAutoChange;
    private boolean isChoosen = false;
    private boolean isClick = false;
    private float textSize = 20;
    private float percent_bitmap_center = 0.5f;
    private float percent_bitmap_left = 0.5f;
    private float percent_bitmap_right = 0.5f;
    private float radius;
    private float w_border;
    private int textColor = Color.GRAY;
    private int textColor_choose = Color.BLACK;
    private int textColor_click = Color.BLACK;
    private int color_text_border;
    private Bitmap drawable_center;
    private Bitmap drawable_left;
    private Bitmap drawable_right;
    private Bitmap drawable_center_choose;
    private Bitmap drawable_center_click;
    private Bitmap drawable_back;
    private Bitmap drawable_back_choose;
    private Bitmap drawable_back_click;
    private float w_space;
    private float w_space_text;
    public static final String orientation_horizontal = "horizontal";
    public static final String orientation_vertical = "vertical";
    public static final String orientation_center = "center";
    private String orientation;
    private int color_back = Color.argb(0, 0, 0, 0);
    private int color_back_choose = Color.GREEN;
    private int color_back_click = Color.GREEN;
    private int color_border_choose;
    private int color_border_click;

    private int color_border;
    private boolean drawTextBorder = false;
    private boolean drawCircleBorder;

    private int color_circle_border;
    private boolean drawCircleImg;
    private float percent_w;
    private float percent_h;
    public static final String vertical = "vertical";
    public static final String horizontal = "horizontal";

    public ZButton(Context context) {
        super(context);
        init(context, null);
    }

    public ZButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ZButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        drawCanvas(canvas);
        super.dispatchDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (this.isAutoChange) {
                    isChoosen = !isChoosen;
                } else {
                    isChoosen = true;
                }
                isClick = true;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                isClick = false;
                break;
        }
        postInvalidate();
        return super.onTouchEvent(event);
    }

    private void init(final Context context, AttributeSet attrs) {
        setClickable(true);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.zbutton);//

            this.isAutoChange = typedArray.getBoolean(R.styleable.zbutton_isautochange, false);

            this.text_center = typedArray.getString(R.styleable.zbutton_text_center);//
            this.textarr = typedArray.getString(R.styleable.zbutton_textarr);//
            this.text_left = typedArray.getString(R.styleable.zbutton_text_left);//
            this.text_right = typedArray.getString(R.styleable.zbutton_text_right);//
            this.textSize = typedArray.getDimension(R.styleable.zbutton_textsize, 15f);

            this.textColor = typedArray.getColor(R.styleable.zbutton_textcolor, textColor);//
            this.textColor_choose = typedArray.getColor(R.styleable.zbutton_textcolor_choose, textColor);//
            this.textColor_click = typedArray.getColor(R.styleable.zbutton_textcolor_click, textColor_choose);//

            this.color_back = typedArray.getColor(R.styleable.zbutton_color_back, color_back);//
            this.color_back_choose = typedArray.getColor(R.styleable.zbutton_color_back_choose, color_back);//
            this.color_back_click = typedArray.getColor(R.styleable.zbutton_color_back_click, color_back_choose);//

            this.color_border = typedArray.getColor(R.styleable.zbutton_color_border, color_border);//
            this.color_border_choose = typedArray.getColor(R.styleable.zbutton_color_border_choose, color_border);//
            this.color_border_click = typedArray.getColor(R.styleable.zbutton_color_border_click, color_border_choose);//

            this.drawable_center = B.drawableToBitmap(typedArray.getDrawable(R.styleable.zbutton_img_center));
            this.drawable_left = B.drawableToBitmap(typedArray.getDrawable(R.styleable.zbutton_img_left));
            this.drawable_right = B.drawableToBitmap(typedArray.getDrawable(R.styleable.zbutton_img_right));
            this.drawable_center_choose = B.drawableToBitmap(typedArray.getDrawable(R.styleable.zbutton_img_center_choose));
            if (drawable_center_choose == null) {
                drawable_center_choose = drawable_center;
            }
            this.drawable_center_click = B.drawableToBitmap(typedArray.getDrawable(R.styleable.zbutton_img_center_click));
            if (drawable_center_click == null) {
                drawable_center_click = drawable_center_choose;
            }
            this.drawable_back = B.drawableToBitmap(typedArray.getDrawable(R.styleable.zbutton_img_back));
            this.drawable_back_choose = B.drawableToBitmap(typedArray.getDrawable(R.styleable.zbutton_img_back_choose));
            if (drawable_back_choose == null) {
                drawable_back_choose = drawable_back;
            }
            this.drawable_back_click = B.drawableToBitmap(typedArray.getDrawable(R.styleable.zbutton_img_back_click));
            if (drawable_back_click == null) {
                drawable_back_click = drawable_back_choose;
            }
            this.orientation = typedArray.getString(R.styleable.zbutton_orientation);
            this.percent_bitmap_center = typedArray.getFloat(R.styleable.zbutton_percent_bitmap_center, 0.5f);
            this.percent_bitmap_left = typedArray.getFloat(R.styleable.zbutton_percent_bitmap_left, 0.5f);
            this.percent_bitmap_right = typedArray.getFloat(R.styleable.zbutton_percent_bitmap_right, 0.5f);
            this.percent_w = typedArray.getFloat(R.styleable.zbutton_percent_w, 0f);
            this.percent_h = typedArray.getFloat(R.styleable.zbutton_percent_h, 0f);
            this.w_space = typedArray.getDimension(R.styleable.zbutton_w_space, 0);
            this.w_space_text = typedArray.getDimension(R.styleable.zbutton_w_space_text, 0);
            this.radius = typedArray.getDimension(R.styleable.zbutton_radius, 0);
            this.w_border = typedArray.getDimension(R.styleable.zbutton_w_border, 0);
            this.drawTextBorder = typedArray.getBoolean(R.styleable.zbutton_drawtextborder, false);
            this.color_text_border = typedArray.getColor(R.styleable.zbutton_color_text_border, Color.BLACK);//
            this.drawCircleBorder = typedArray.getBoolean(R.styleable.zbutton_drawcircleborder, false);
            this.color_circle_border = typedArray.getColor(R.styleable.zbutton_color_circle_border, Color.WHITE);//
            this.drawCircleImg = typedArray.getBoolean(R.styleable.zbutton_drawcircleimg, false);
            this.radiusArray[0] = typedArray.getDimension(R.styleable.zbutton_radius_lefttop_x, 0);
            this.radiusArray[1] = typedArray.getDimension(R.styleable.zbutton_radius_lefttop_y, 0);
            this.radiusArray[2] = typedArray.getDimension(R.styleable.zbutton_radius_righttop_x, 0);
            this.radiusArray[3] = typedArray.getDimension(R.styleable.zbutton_radius_righttop_y, 0);
            this.radiusArray[4] = typedArray.getDimension(R.styleable.zbutton_radius_rightbottom_x, 0);
            this.radiusArray[5] = typedArray.getDimension(R.styleable.zbutton_radius_rightbottom_y, 0);
            this.radiusArray[6] = typedArray.getDimension(R.styleable.zbutton_radius_leftbottom_x, 0);
            this.radiusArray[7] = typedArray.getDimension(R.styleable.zbutton_radius_leftbottom_y, 0);
            if (orientation == null) {
                orientation = orientation_horizontal;
            }
            typedArray.recycle();
        }


    }


    //    @Override
    public void drawCanvas(Canvas canvas) {

        Path path = new Path();
        path.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), radiusArray, Path.Direction.CW);
        canvas.clipPath(path);
        int w = getWidth();
        int w_scale = (int) (w * percent_w);
        int h = getHeight();
        int h_scale = (int) (h * percent_h);
        if (w == 0 || h == 0) {
            return;
        }
        int padding = 20;

        Paint paint = new Paint();
        Paint paint_back = new Paint();
        Paint paint_border = new Paint();


        Bitmap drawable_back_draw = isChoosen ? drawable_back_choose : this.drawable_back;
        paint_back.setColor((isChoosen ? color_back_choose : color_back));
        Bitmap drawable_center_draw = isChoosen ? drawable_center_choose : drawable_center;
        int textColor_tmp = isChoosen ? textColor_choose : this.textColor;
        paint_border.setColor(isChoosen ? color_border_choose : color_border);

        if (!isAutoChange && isClick) {
            drawable_back_draw = this.drawable_back_click;
            paint_back.setColor(color_back_click);
            drawable_center_draw = drawable_center_click;
            textColor_tmp = this.textColor_click;
            paint_border.setColor(color_border_click);
        }

        RectF rect_destination_back = new RectF();
        rect_destination_back.set(0, 0, w, h);
        //绘制背景
        if (drawable_back_draw != null) {
            int w_bitmap_back = drawable_back_draw.getWidth();
            int h_bitmap_back = drawable_back_draw.getHeight();
            //绘制背景
            Rect rect_src_back = new Rect();
            rect_src_back.set(0, 0, w_bitmap_back, h_bitmap_back);
            canvas.drawBitmap(drawable_back_draw, rect_src_back, rect_destination_back, paint);
        } else {
            paint_back.setAntiAlias(true);
            paint_back.setStyle(Paint.Style.FILL);
            canvas.drawRoundRect(rect_destination_back, radius, radius, paint_back);
        }

        //绘制背景上层的图
        //获取要绘制的图标

        Bitmap drawable_left_draw = this.drawable_left;
        Bitmap drawable_right_draw = this.drawable_right;

        float x_bitmap_center_draw = 0;
        float y_bitmap_center_draw = 0;
        float x_text_center_draw = 0;
        float y_text_center_draw = 0;
        float w_text_center = 0;
        float h_text_center = 0;
        float w_arr_center = 0;
        float h_text_arr = 0;
        float h_bitmap_center_draw = 0;
        float w_bitmap_center_draw = 0;

        float x_bitmap_left_draw = 0;
        float y_bitmap_left_draw = 0;
        float x_text_left_draw = 0;
        float y_text_left_draw = 0;
        float w_text_left = 0;
        float h_text_left = 0;
        float w_bitmap_left_draw = 0;
        float h_bitmap_left_draw = 0;
        float w_bitmap_left_hold = 0;


        float x_bitmap_right_draw = 0;
        float y_bitmap_right_draw = 0;
        float x_text_right_draw = 0;
        float y_text_right_draw = 0;
        float w_text_right = 0;
        float h_text_right = 0;
        float h_bitmap_right_draw = 0;
        float w_bitmap_right_draw = 0;
        float w_bitmap_right_hold = 0;

        float h_item_textarr = 0;

        //计算文字的宽度
        Paint paint_text = new Paint();
        paint_text.setAntiAlias(true);
        if (text_center != null) {
            paint_text.setTextSize(textSize);
//            Rect rect_text = new Rect();
//            paint_text.getTextBounds(text_center, 0, text_center.length(), rect_text);
//            w_text_center = rect_text.width();
//            h_text_center = rect_text.height();
            float textWidth = paint_text.measureText(text_center);
            Paint.FontMetrics fontMetrics = paint_text.getFontMetrics();
            // top绝对值,最外层top,负数
            float top = Math.abs(fontMetrics.top);
            // ascent绝对值,文字上部,负数
            float ascent = Math.abs(fontMetrics.ascent);
            // descent，正值,文字下部
            float descent = fontMetrics.descent;
            // bottom，正值,最外层bottom
            float bottom = fontMetrics.bottom;
            w_text_center = textWidth;
            h_text_center = Math.abs(descent - ascent);
        }
        if (text_left != null) {
            paint_text.setTextSize(textSize);
            float textWidth = paint_text.measureText(text_left);
            Paint.FontMetrics fontMetrics = paint_text.getFontMetrics();
            float ascent = Math.abs(fontMetrics.ascent);
            float descent = fontMetrics.descent;
            w_text_left = textWidth;
            h_text_left = Math.abs(descent - ascent);
        }
        if (text_right != null) {paint_text.setTextSize(textSize);
            float textWidth = paint_text.measureText(text_right);
            Paint.FontMetrics fontMetrics = paint_text.getFontMetrics();
            float ascent = Math.abs(fontMetrics.ascent);
            float descent = fontMetrics.descent;
            w_text_right = textWidth;
            h_text_right = Math.abs(descent - ascent);
        }
        String[] arr = null;
        if (textarr != null) {
            arr = textarr.split("[ |\\|]");
            if (arr != null) {
                for (int i = 0; i < arr.length; i++) {
                    String text = arr[i];
                    paint_text.setTextSize(textSize);
                    float w_text_tmp = paint_text.measureText(text);
                    Paint.FontMetrics fontMetrics = paint_text.getFontMetrics();
                    float ascent = Math.abs(fontMetrics.ascent);
                    float descent = fontMetrics.descent;
                    h_item_textarr = Math.abs(descent - ascent);
                    w_arr_center = (w_arr_center > w_text_tmp ? w_arr_center : w_text_tmp);
                }
                float h_space_item=(w_space_text)*(arr.length-1);
                if(h_space_item<0){
                    h_space_item=0;
                }
                h_text_arr = h_item_textarr * arr.length + h_space_item;
            }
        }

        //如果要设置一个圆形的图片
        if (drawCircleImg) {
            drawable_center_draw = B.getBitmap_circle(drawable_center_draw);
        }
        if (drawable_center_draw != null) {
            int w_bitmap = drawable_center_draw.getWidth();
            int h_bitmap = drawable_center_draw.getHeight();

            if (percent_bitmap_center == 0) {
                percent_bitmap_center = 0.5f;
            }
            h_bitmap_center_draw = h * percent_bitmap_center;
            w_bitmap_center_draw = h_bitmap_center_draw * (w_bitmap / (float) h_bitmap);

            if (orientation.equals(orientation_vertical)) {
                x_bitmap_center_draw = (w - w_bitmap_center_draw) / 2f;
                y_bitmap_center_draw = (h - (h_bitmap_center_draw + (h_text_arr > 0 ? h_text_arr : h_text_center) + w_space)) / 2f;
            } else if (orientation.equals(orientation_horizontal)) {
                x_bitmap_center_draw = (w - (w_bitmap_center_draw + (w_arr_center > w_text_center ? w_arr_center : w_text_center) + w_space)) / 2f;
                y_bitmap_center_draw = (h - h_bitmap_center_draw) / 2f;
            } else {
                x_bitmap_center_draw = (w - w_bitmap_center_draw) / 2f;
                y_bitmap_center_draw = (h - h_bitmap_center_draw) / 2f;
            }
            x_bitmap_center_draw -= w_scale;
            y_bitmap_center_draw -= h_scale;
            //绘制图标
            Rect rect_src = new Rect();
            rect_src.set(0, 0, w_bitmap, h_bitmap);
            Rect rect_destination = new Rect();
            rect_destination.set((int) x_bitmap_center_draw, (int) y_bitmap_center_draw, (int) (x_bitmap_center_draw + w_bitmap_center_draw), (int) (y_bitmap_center_draw + h_bitmap_center_draw));
            canvas.drawBitmap(drawable_center_draw, rect_src, rect_destination, paint);

            if (drawCircleImg && drawCircleBorder) {
                float w_stroke = w_border;
                paint.setColor(color_circle_border);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(w_stroke);
                float radius_circle = w_bitmap_center_draw / 2;
                float x_circle = x_bitmap_center_draw + w_bitmap_center_draw / 2;
                float y_circle = y_bitmap_center_draw + h_bitmap_center_draw / 2;
                canvas.drawCircle(x_circle, y_circle, radius_circle - w_stroke / 2f, paint);
            }
        }else{
            w_space=0;
        }

        if (drawable_left_draw != null) {
            int w_bitmap = drawable_left_draw.getWidth();
            int h_bitmap = drawable_left_draw.getHeight();

            if (percent_bitmap_left == 0) {
                percent_bitmap_left = 0.5f;
            }
            h_bitmap_left_draw = h * percent_bitmap_left;
            w_bitmap_left_draw = h_bitmap_left_draw * (w_bitmap / (float) h_bitmap);
            w_bitmap_left_hold = (int) (w_bitmap_left_draw > h_bitmap_left_draw ? w_bitmap_left_draw : h_bitmap_left_draw) + padding;

            x_bitmap_left_draw = (0) + padding;
            y_bitmap_left_draw = (h - h_bitmap_left_draw) / 2f;

            //绘制图标
            Rect rect_src = new Rect();
            rect_src.set(0, 0, w_bitmap, h_bitmap);
            Rect rect_destination = new Rect();
            rect_destination.set((int) x_bitmap_left_draw, (int) y_bitmap_left_draw, (int) (x_bitmap_left_draw + w_bitmap_left_draw), (int) (y_bitmap_left_draw + h_bitmap_left_draw));
            canvas.drawBitmap(drawable_left_draw, rect_src, rect_destination, paint);
        }
        if (drawable_right_draw != null) {
            int w_bitmap = drawable_right_draw.getWidth();
            int h_bitmap = drawable_right_draw.getHeight();

            if (percent_bitmap_right == 0) {
                percent_bitmap_right = 0.5f;
            }
            h_bitmap_right_draw = h * percent_bitmap_right;
            w_bitmap_right_draw = h_bitmap_right_draw * (w_bitmap / (float) h_bitmap);
            w_bitmap_right_hold = (int) (w_bitmap_right_draw > h_bitmap_right_draw ? w_bitmap_right_draw : h_bitmap_right_draw) + padding;

            x_bitmap_right_draw = (w - w_bitmap_right_draw - padding);
            y_bitmap_right_draw = (h - h_bitmap_right_draw) / 2f;

            //绘制图标
            Rect rect_src = new Rect();
            rect_src.set(0, 0, w_bitmap, h_bitmap);
            Rect rect_destination = new Rect();
            rect_destination.set((int) x_bitmap_right_draw, (int) y_bitmap_right_draw, (int) (x_bitmap_right_draw + w_bitmap_right_draw), (int) (y_bitmap_right_draw + h_bitmap_right_draw));
            canvas.drawBitmap(drawable_right_draw, rect_src, rect_destination, paint);
        }

        //中间的文字是否需要绘制
        if (text_center != null && !text_center.equals("")) {

            if (orientation.equals(orientation_vertical)) {
                x_text_center_draw = (w - w_text_center) / 2f;
                y_text_center_draw = y_bitmap_center_draw + h_bitmap_center_draw + h_text_center + w_space;
            } else if (orientation.equals(orientation_horizontal)) {
                x_text_center_draw = (w - (w_bitmap_center_draw + w_text_center)) / 2 + w_bitmap_center_draw + w_space;
                y_text_center_draw = (h + h_text_center) / 2f;
            } else {
                x_text_center_draw = (w - w_text_center) / 2f;
                y_text_center_draw = (h + h_text_center) / 2f;
            }
            //绘制文字
            paint_text.setColor(textColor_tmp);
            canvas.drawText(text_center, x_text_center_draw, y_text_center_draw, paint_text);

            if (drawTextBorder) {
                paint_text.setStyle(Paint.Style.STROKE);
                paint_text.setStrokeWidth(1);
                paint_text.setColor(color_text_border);
                paint_text.setAntiAlias(true);
                canvas.drawText(text_center, x_text_center_draw, y_text_center_draw, paint_text);
            }
        }
        //左边的文字是否需要绘制
        if (text_left != null && !text_left.equals("")) {
            x_text_left_draw = 0 + h_text_left / 2f + w_bitmap_left_hold;
            y_text_left_draw = (h - h_text_left) / 2f + h_text_left;
            y_text_left_draw -= h_text_left / 8f;
            //绘制文字
            paint_text.setColor(textColor_tmp);
            canvas.drawText(text_left, x_text_left_draw, y_text_left_draw, paint_text);

            if (drawTextBorder) {
                paint_text.setStyle(Paint.Style.STROKE);
                paint_text.setStrokeWidth(1);
                paint_text.setColor(color_text_border);
                paint_text.setAntiAlias(true);
                canvas.drawText(text_left, x_text_left_draw, y_text_left_draw, paint_text);
            }
        }
        //右边的文字是否需要绘制
        if (text_right != null && !text_right.equals("")) {
            x_text_right_draw = w - w_text_right - +h_text_right / 2f - w_bitmap_right_hold;
            y_text_right_draw = (h - h_text_right) / 2f + h_text_right;
            y_text_right_draw -= h_text_right / 8f;
            //绘制文字
            paint_text.setColor(textColor_tmp);
            canvas.drawText(text_right, x_text_right_draw, y_text_right_draw, paint_text);

            if (drawTextBorder) {
                paint_text.setStyle(Paint.Style.STROKE);
                paint_text.setStrokeWidth(1);
                paint_text.setColor(color_text_border);
                paint_text.setAntiAlias(true);
                canvas.drawText(text_right, x_text_right_draw, y_text_right_draw, paint_text);
            }
        }

        float x_text_item_draw = 0;//x坐标
        float y_text_item_draw = 0;
        float w_item_arr_tmp = 0;//宽度
        if (textarr != null && arr != null) {
            for (int i = 0; i < arr.length; i++) {


                String text = arr[i];
                w_item_arr_tmp = paint_text.measureText(text);

                //文字是否需要绘制
                if (text != null && !text.equals("")) {
                    float h_y_item_now = (h_item_textarr  + w_space_text) * (i+1) -w_space_text;
                    if (orientation.equals(orientation_vertical)) {
                        x_text_item_draw = (w - w_item_arr_tmp) / 2f;
                        y_text_item_draw = y_bitmap_center_draw + h_bitmap_center_draw + w_space + h_y_item_now;
                    } else if (orientation.equals(orientation_horizontal)) {
                        x_text_item_draw = (w - (w_arr_center + w_bitmap_center_draw + w_space)) / 2f + w_bitmap_center_draw + w_space+(w_arr_center-w_item_arr_tmp)/2f;
                        y_text_item_draw = (h - h_text_arr) / 2f +  h_y_item_now;
                    } else {
                        x_text_item_draw = (w - w_item_arr_tmp) / 2f;
                        y_text_item_draw = (h - h_text_arr) / 2f +  h_y_item_now;
                    }
                    //绘制文字
                    paint_text.setStyle(Paint.Style.FILL);
                    paint_text.setColor(textColor_tmp);
                    canvas.drawText(text, x_text_item_draw, y_text_item_draw, paint_text);

                    if (drawTextBorder) {
                        paint_text.setStyle(Paint.Style.STROKE);
                        paint_text.setStrokeWidth(1);
                        paint_text.setColor(color_text_border);
                        paint_text.setAntiAlias(true);
                        canvas.drawText(text, x_text_item_draw, y_text_item_draw, paint_text);
                    }
                }
            }

        }

        if (w_border > 0) {
            float radius_min = (w > h ? h : w) / 2f;
            if (radius > radius_min) {
                radius = radius_min;
            }
            int w_rect = (int) (w - w_border);
            int h_rect = (int) (h - w_border);

            paint_border.setStyle(Paint.Style.STROKE);
            paint_border.setStrokeWidth(w_border);
            paint_border.setColor(color_border);
            paint_border.setAntiAlias(true);

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
        //中间参考线
//        paint.setColor(Color.RED);
//        paint.setStyle(Paint.Style.FILL);
//        paint.setStrokeWidth(1);
//        canvas.drawLine(0, canvas.getHeight() / 2, canvas.getWidth(), canvas.getHeight() / 2, paint);
//        canvas.drawLine(canvas.getWidth() / 2, 0, canvas.getWidth() / 2, canvas.getHeight(), paint);
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        this.isClick = click;
        postInvalidate();
    }

    public int getTextColor_click() {
        return textColor_click;
    }

    public void setTextColor_click(int textColor_click) {
        this.textColor_click = textColor_click;
        postInvalidate();
    }

    public Bitmap getDrawable_center_click() {
        return drawable_center_click;
    }

    public void setDrawable_center_click(Bitmap drawable_center_click) {
        if (drawable_center_click == null) {
            this.drawable_center_click = drawable_center_choose;
        } else {
            this.drawable_center_click = drawable_center_click;
        }
        postInvalidate();
    }

    public Bitmap getDrawable_back_click() {
        return drawable_back_click;
    }

    public void setDrawable_back_click(Bitmap drawable_back_click) {
        this.drawable_back_click = drawable_back_click;
        postInvalidate();
    }

    public int getColor_back_click() {
        return color_back_click;
    }

    public void setColor_back_click(int color_back_click) {
        this.color_back_click = color_back_click;
        postInvalidate();
    }

    public int getColor_border_click() {
        return color_border_click;
    }

    public void setColor_border_click(int color_border_click) {
        this.color_border_click = color_border_click;
        postInvalidate();
    }

    public void setChoosen(boolean isChoosen) {
        this.isChoosen = isChoosen;
        postInvalidate();
    }

    private float[] radiusArray = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};

    /**
     * 设置四个角的圆角半径
     */
    public void setRadius(float leftTop, float rightTop, float rightBottom, float leftBottom) {
        radiusArray[0] = leftTop;
        radiusArray[1] = leftTop;
        radiusArray[2] = rightTop;
        radiusArray[3] = rightTop;
        radiusArray[4] = rightBottom;
        radiusArray[5] = rightBottom;
        radiusArray[6] = leftBottom;
        radiusArray[7] = leftBottom;
        postInvalidate();
    }

    public String getText_left() {
        return text_left;
    }

    public void setText_left(String text_left) {
        this.text_left = text_left;
        postInvalidate();
    }

    public String getText_right() {
        return text_right;
    }

    public void setText_right(String text_right) {
        this.text_right = text_right;
        postInvalidate();
    }

    public String getTextarr() {
        return textarr;
    }

    public void setTextarr(String textarr) {
        this.textarr = textarr;
        postInvalidate();
    }

    public float getPercent_bitmap_left() {
        return percent_bitmap_left;
    }

    public void setPercent_bitmap_left(float percent_bitmap_left) {
        this.percent_bitmap_left = percent_bitmap_left;
        postInvalidate();
    }

    public float getPercent_bitmap_right() {
        return percent_bitmap_right;
    }

    public void setPercent_bitmap_right(float percent_bitmap_right) {
        this.percent_bitmap_right = percent_bitmap_right;
        postInvalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        postInvalidate();
    }

    public int getColor_text_border() {
        return color_text_border;
    }

    public void setColor_text_border(int color_text_border) {
        this.color_text_border = color_text_border;
        postInvalidate();
    }

    public Bitmap getDrawable_center() {
        return drawable_center;
    }

    public Bitmap getDrawable_left() {
        return drawable_left;
    }

    public void setDrawable_left(Bitmap drawable_left) {
        this.drawable_left = drawable_left;
        postInvalidate();
    }

    public Bitmap getDrawable_right() {
        return drawable_right;
    }

    public void setDrawable_right(Bitmap drawable_right) {
        this.drawable_right = drawable_right;
        postInvalidate();
    }

    public Bitmap getDrawable_back() {
        return drawable_back;
    }

    public float getW_space_text() {
        return w_space_text;
    }

    public void setW_space_text(float w_space_text) {
        this.w_space_text = w_space_text;
        postInvalidate();
    }

    public int getColor_back() {
        return color_back;
    }

    public boolean isDrawTextBorder() {
        return drawTextBorder;
    }

    public void setDrawTextBorder(boolean drawTextBorder) {
        this.drawTextBorder = drawTextBorder;
        postInvalidate();
    }

    public boolean isDrawCircleBorder() {
        return drawCircleBorder;
    }

    public void setDrawCircleBorder(boolean drawCircleBorder) {
        this.drawCircleBorder = drawCircleBorder;
        postInvalidate();
    }


    public int getColor_circle_border() {
        return color_circle_border;
    }

    public void setColor_circle_border(int color_circle_border) {
        this.color_circle_border = color_circle_border;
        postInvalidate();
    }

    public boolean isDrawCircleImg() {
        return drawCircleImg;
    }

    public void setDrawCircleImg(boolean drawCircleImg) {
        this.drawCircleImg = drawCircleImg;
        postInvalidate();
    }

    public float getPercent_w() {
        return percent_w;
    }

    public void setPercent_w(float percent_w) {
        this.percent_w = percent_w;
        postInvalidate();
    }

    public float getPercent_h() {
        return percent_h;
    }

    public void setPercent_h(float percent_h) {
        this.percent_h = percent_h;
        postInvalidate();
    }


    public float[] getRadiusArray() {
        return radiusArray;
    }

    public void setRadiusArray(float[] radiusArray) {
        this.radiusArray = radiusArray;
        postInvalidate();
    }

    public String getText_center() {
        return text_center;
    }

    public void setText_center(String text_center) {
        this.text_center = text_center;
        postInvalidate();
    }


    public boolean isChoosen() {
        return isChoosen;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        postInvalidate();
    }

    public float getPercent_bitmap_center() {
        return percent_bitmap_center;
    }

    public void setPercent_bitmap_center(float percent_bitmap_center) {
        this.percent_bitmap_center = percent_bitmap_center;
        postInvalidate();
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        postInvalidate();
    }

    public float getW_border() {
        return w_border;
    }

    public void setW_border(float w_border) {
        this.w_border = w_border;
        postInvalidate();
    }

    public int getTextColor_choose() {
        return textColor_choose;
    }

    public void setTextColor_choose(int textColor_choose) {
        this.textColor_choose = textColor_choose;
        postInvalidate();
    }


    public Bitmap getDrawable_center_choose() {
        return drawable_center_choose;
    }

    public void setDrawable_center_choose(Bitmap drawable_center_choose) {
        if (drawable_center_choose == null) {
            this.drawable_center_choose = drawable_center;
        } else {
            this.drawable_center_choose = drawable_center_choose;
        }
        postInvalidate();
    }


    public Bitmap getDrawable_back_choose() {
        return drawable_back_choose;
    }

    public void setDrawable_back_choose(Bitmap drawable_back_choose) {
        this.drawable_back_choose = drawable_back_choose;
        postInvalidate();
    }


    public void setDrawable_back(Bitmap drawable_back) {
        this.drawable_back = drawable_back;
        postInvalidate();
    }

    public float getW_space() {
        return w_space;
    }

    public void setW_space(float w_space) {
        this.w_space = w_space;
        postInvalidate();
    }

    public static String getOrientation_horizontal() {
        return orientation_horizontal;
    }

    public static String getOrientation_vertical() {
        return orientation_vertical;
    }

    public static String getOrientation_center() {
        return orientation_center;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
        postInvalidate();
    }


    public void setColor_back(int color_back) {
        this.color_back = color_back;
        postInvalidate();
    }

    public int getColor_back_choose() {
        return color_back_choose;
    }

    public void setColor_back_choose(int color_back_choose) {
        this.color_back_choose = color_back_choose;
        postInvalidate();
    }

    public int getColor_border_choose() {
        return color_border_choose;
    }

    public void setColor_border_choose(int color_border_choose) {
        this.color_border_choose = color_border_choose;
        postInvalidate();
    }

    public int getColor_border() {
        return color_border;
    }

    public void setColor_border(int color_border_unchoose) {
        this.color_border = color_border;
        postInvalidate();
    }

    public void setDrawable_center(Bitmap drawable_center) {
        this.drawable_center = drawable_center;
        if (drawable_center_choose == null) {
            this.drawable_center_choose = drawable_center;
        }
        if (drawable_center_click == null) {
            this.drawable_center_click = drawable_center_choose;
        }
        postInvalidate();
    }
}
