package com.zhibaowang.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.zhibaowang.app.HongboheshiApplication;
import com.zhibaowang.component.SwipeBackLayout;
import com.zhibaowang.tools.S;
import com.zhibaowang.tools.ZScreenTool;

import java.util.regex.Pattern;

/**
 * Created by zhaoyuntao on 2017/12/14.
 */

public class ZBaseActivity extends SwipeBackActivity {
    private String regex_phoneNumber = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0-9]))" +
            "\\d{8}$";//phoneNumber regex
    private int length_password_access = 4;//min length of password

    protected View progressBar;
    private SwipeBackLayout mSwipeBackLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setScrollThresHold(0.5f);
        //设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL, EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            View v = getCurrentFocus();

            //如果不是落在EditText区域，则需要关闭输入法
            if (HideKeyboard(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    protected boolean isUsernameValid(String email) {
        return email != null && Pattern.matches(regex_phoneNumber, email);
    }

    protected boolean isPasswordValid(String password) {
        return password.length() > length_password_access;
    }
    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private boolean HideKeyboard(View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {

            int[] location = { 0, 0 };
            view.getLocationInWindow(location);

            //获取现在拥有焦点的控件view的位置，即EditText
            int left = location[0], top = location[1], bottom = top + view.getHeight(), right = left + view.getWidth();
            //判断我们手指点击的区域是否落在EditText上面，如果不是，则返回true，否则返回false
            boolean isInEt = (event.getX() > left && event.getX() < right && event.getY() > top
                    && event.getY() < bottom);
            return !isInEt;
        }
        return false;
    }
    protected void showProgress(final boolean show) {
        S.s("showprogress:"+show);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressBar!=null){
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            }
        });

    }
    public void onBackClick(View view) {
        finish();
    }
}
