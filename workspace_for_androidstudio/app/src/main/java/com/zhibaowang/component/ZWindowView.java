package com.zhibaowang.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import com.zhibaowang.app.R;
import com.zhibaowang.tools.B;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyuntao on 2017/11/13.
 */

public class ZWindowView extends View {
    private Context context;
    private CallBack callBack;
    private Bitmap bitmap_null;
    private List<Child> list = new ArrayList<>();
    private int textColor_unchoose = Color.parseColor("#fcffff");
    private int textColor_choose = Color.parseColor("#fcffff");
    private int color_back_choose = Color.parseColor("#5522c064");
    private int color_back_unchoose = Color.argb(66, 255, 255, 255);
    private int w, h;
    private float w_child;
    private float h_child;
    private float w_center;
    private float h_center;
    private float w_left_right;
    private float percent_textSize=0.04f;
    private float percent_bitmap=0.3f;

    public ZWindowView(Context context) {
        super(context);
        init(context);
    }

    public ZWindowView(Context context, Child... childs) {
        this(context);
        initDate(childs);
    }

    public class Child {
        private Bitmap drawable_choose;
        private Bitmap drawable_unchoose;

        private String text;
        private boolean isChoosen;

        public Child(String text, int drawableId_choose, int drawableId_unchoose) {
            this.text = text;
            this.drawable_choose = B.getBitmapById_Percent(context, drawableId_choose, 1);
            this.drawable_unchoose = B.getBitmapById_Percent(context, drawableId_unchoose, 1);
        }

        public void setChoosen(boolean isChoosen) {
            this.isChoosen = isChoosen;
        }

        public boolean isChoosen() {
            return isChoosen;
        }

        public Bitmap getDrawable() {
            return (isChoosen ? (drawable_choose != null ? drawable_choose : bitmap_null) : (drawable_unchoose != null ? drawable_unchoose : bitmap_null));
        }

        public int getColor() {
            return isChoosen ? textColor_choose : textColor_unchoose;
        }

        public String getText() {
            return this.text != null ? this.text : "";
        }

        public int getColor_back() {
            return isChoosen ? color_back_choose : color_back_unchoose;
        }
    }

    public void initDate(Child child) {
        list.add(child);
    }

    public void initDate(Child... childs) {
        for (int i = 0; i < childs.length; i++) {
            initDate(childs[i]);
        }
    }

    private void init(Context context) {
        this.context = context;
        this.bitmap_null = B.getBitmapById_Percent(context, R.drawable.img_null, 1);
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        w = getWidth();
        h = getHeight();
        if (w == 0 || h == 0) {
            return;
        }
        Paint paint = new Paint();
        float textSize = w *percent_textSize;
        w_center = w / 15f;
        w_left_right = w_center / 2f;
        h_center = w_center / 2f;
        w_child = (w - w_center - w_left_right * 2) / 2f;
        h_child = (h - (list.size() / 2 - 1) * h_center) / (list.size() / 2 + list.size() % 2);
        float tmp_h_child = w_child / 1.88f;
        if (h_child > tmp_h_child) {
            h_child = tmp_h_child;
        }
        float w_bitmap_draw = w_child *percent_bitmap;
        for (int i = 0; i < list.size(); i++) {
            int index = i;
            Child child = list.get(index);
            if (child == null) {
                continue;
            }
            float x_child = w_left_right + (index % 2) * (w_child + w_center);
            float y_child = index / 2 * h_child + index / 2 * h_center;

            Rect rect_back = new Rect();
            rect_back.set((int) x_child, (int) y_child, (int) (x_child + w_child), (int) (y_child + h_child));
            paint.setColor(child.getColor_back());
            canvas.drawRect(rect_back, paint);

            paint.setColor(Color.WHITE);
            String text = child.getText();
            Bitmap drawable = child.getDrawable();
            int w_bitmap = drawable.getWidth();
            int h_bitmap = drawable.getHeight();
            float h_bitmap_draw = w_bitmap_draw * (h_bitmap / (float) w_bitmap);

            paint.setTextSize(textSize);
            Rect rect_text = new Rect();
            paint.getTextBounds(text, 0, text.length(), rect_text);

            float w_text = rect_text.width();
            float h_text = rect_text.height();

            float x_text = x_child + (w_child - w_text) / 2f;
            float y_text = y_child + (h_child - (h_bitmap_draw + h_text * 1.0f)) / 2f + h_bitmap_draw + h_text * 1.0f;
            float x_bitmap = x_child + (w_child - w_bitmap_draw) / 2f;
            float y_bitmap = y_child + (h_child - (h_bitmap_draw + h_text * 1.0f)) / 2f;

            Rect rect = new Rect();
            rect.set(0, 0, w_bitmap, h_bitmap);

            Rect rect2 = new Rect();
            rect2.set((int) x_bitmap, (int) y_bitmap, (int) (x_bitmap + w_bitmap_draw), (int) (y_bitmap + h_bitmap_draw));
            canvas.drawBitmap(drawable, rect, rect2, paint);

            paint.setColor(child.isChoosen ? textColor_choose : textColor_unchoose);
            canvas.drawText(text, x_text, y_text, paint);
        }
    }

    private void click(float x, float y, boolean isClick) {
        if (callBack == null) {
            return;
        }
        found:
        for (int i = 0; i < list.size(); i++) {
            int index = i;
            final Child child = list.get(index);
            if (child == null) {
                continue;
            }
            child.setChoosen(false);
            float x_child = (index % 2) * (w_child + w_center);
            float y_child = index / 2 * h_child + index / 2 * h_center;
            if (x >= x_child && x <= x_child + w_child) {
                if (y >= y_child && y <= y_child + h_child) {
                    child.setChoosen(true);
                    if (isClick) {
                        callBack.whenClick(i);
                    }
                    break found;
                }
            }
        }
    }

    public void resetAllItem() {
        for (int i = 0; i < list.size(); i++) {
            int index = i;
            final Child child = list.get(index);
            if (child == null) {
                continue;
            }
            child.setChoosen(false);
        }
    }

    private float x_last, y_last;
    private long time_lastClick;
    private boolean isClick = false;
    private float maxdistance_click = 5f;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x_last = x;
                y_last = y;
                isClick = false;
                click(x, y, isClick);
                if (Math.abs(System.currentTimeMillis() - time_lastClick) <= 50) {
                    isClick = false;
                } else {
                    isClick = true;
                    time_lastClick = System.currentTimeMillis();
                }
                postInvalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isClick) {
                    if (Math.abs(x - x_last) >= maxdistance_click || Math.abs(y - y_last) >= maxdistance_click) {
                        isClick = false;
                        resetAllItem();
                        postInvalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isClick && Math.abs(System.currentTimeMillis() - time_lastClick) <= 700) {
                    time_lastClick = System.currentTimeMillis();
                    click(x, y, isClick);
                }
                resetAllItem();
                postInvalidate();
                break;
        }
        return true;
    }


    public interface CallBack {
        /**
         * 当点击的时候,返回当前是否被选中
         *
         * @return true:被选中的状态 false:未被选中的状态
         */
        boolean whenClick(int position);
    }
}
