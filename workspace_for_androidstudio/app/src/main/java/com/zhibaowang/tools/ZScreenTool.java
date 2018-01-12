package com.zhibaowang.tools;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by zhaoyuntao on 2018/1/2.
 */

public class ZScreenTool {
    public static int[] getWH(Context context){
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = windowmanager.getDefaultDisplay();
        Point p = new Point();
        d.getSize(p);
        int w_screen = p.x; // 竖屏状态下屏幕宽（像素）
        int h_screen = p.y;// 竖屏状态下屏幕长（像素）
        return new int[]{w_screen,h_screen};
    }
}
