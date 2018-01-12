package com.zhibaowang.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zhibaowang.app.HongboheshiApplication;
import com.zhibaowang.app.R;
import com.zhibaowang.component.ZButton;
import com.zhibaowang.tools.S;
import com.zhibaowang.tools.T;
import com.zhibaowang.tools.ZScreenTool;

/**
 * Created by zhaoyuntao on 2017/12/29.
 */

public class SystemSettingsActivity extends ZBaseActivity {

    private FrameLayout dialog;
    private FrameLayout back_dialog;
    private Spinner spinner;
    private String[] arr = {"人民币", "美元", "英镑"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemsettings);

        initView();
    }

    private void initView() {
        dialog = findViewById(R.id.dialog_exitlogin);
        back_dialog = findViewById(R.id.back_dialog_exitlogin);
        back_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialogVisibility(false);
            }
        });
        dialog.setVisibility(View.INVISIBLE);
//        dialog.setEnabled(false);
        back_dialog.setVisibility(View.INVISIBLE);
//        back_dialog.setEnabled(false);
        ZButton button_yes_dialog_exitlogin = dialog.findViewById(R.id.button_yes_dialog_exitlogin);
        button_yes_dialog_exitlogin.setOnClickListener(new ZButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.t(SystemSettingsActivity.this, "确定");
                setDialogVisibility(false);
            }
        });
        ZButton button_no_dialog_exitlogin = dialog.findViewById(R.id.button_no_dialog_exitlogin);
        button_no_dialog_exitlogin.setOnClickListener(new ZButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.t(SystemSettingsActivity.this, "已取消");
                setDialogVisibility(false);
            }
        });
        spinner = findViewById(R.id.spinner_systemsettings);
        final String arr[] = this.arr;
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return arr.length;
            }

            @Override
            public Object getItem(int position) {
                return arr[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder;
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    viewHolder.textView = new TextView(SystemSettingsActivity.this);
                    viewHolder.textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50));
                    viewHolder.textView.setGravity(Gravity.CENTER);
                    convertView = viewHolder.textView;
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                    convertView = viewHolder.textView;
                }
                viewHolder.textView.setText(arr[position]);
                return convertView;
            }

            class ViewHolder {
                TextView textView;
            }
        };
        spinner.setAdapter(adapter);
    }


    public void back(View view) {
        finish();
    }

    public void exitLogin(View view) {
        S.s("onclick");
        setDialogVisibility(true);
    }

    public void guanyu(View view) {
        S.s("onclick");
        Intent intent = new Intent(SystemSettingsActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    private void setDialogVisibility(final boolean visibility) {
        if(visibility) {
            back_dialog.setVisibility(View.VISIBLE);
            dialog.setVisibility(View.VISIBLE);
        }
        float x_start = 0.8f;
        float x_end = 1.02f;
        float y_start = 0.8f;
        float y_end = 1.02f;
        float x_start2 = 1.02f;
        float x_end2 = 1.0f;
        float y_start2 = 1.02f;
        float y_end2 = 1.0f;
        int x_relative = Animation.RELATIVE_TO_SELF;
        float x_center_scale = 0.5f;
        int y_relative = Animation.RELATIVE_TO_SELF;
        float y_center_scale = 0.5f;

        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                S.s("start-------------");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                S.s("end -------------" + visibility);
                if (visibility) {
                    dialog.setVisibility(View.VISIBLE);
//                    dialog.setEnabled(true);
                    back_dialog.setVisibility(View.VISIBLE);
//                    back_dialog.setEnabled(true);
                } else {
                    dialog.setVisibility(View.INVISIBLE);
//                    dialog.setEnabled(false);
                    back_dialog.setVisibility(View.INVISIBLE);
//                    back_dialog.setEnabled(false);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        if (!visibility) {
            x_start = 1.0f;
            x_end = 0.8f;
            y_start = 1.0f;
            y_end = 0.8f;
            x_start2 = 1.0f;
            x_end2 = 1.0f;
            y_start2 = 1.0f;
            y_end2 = 1.0f;
            x_relative = Animation.RELATIVE_TO_SELF;
            x_center_scale = 0.5f;
            y_relative = Animation.RELATIVE_TO_SELF;
            y_center_scale = 0.5f;
        }

        //以下参数全部为百分比,相对于自身来说
        //fromX 动画开始时的大小
        //toX 动画结束时的大小
        //fromY 动画开始时大小
        //toY 动画结束时大小
        //pivotXType 相对于哪个物体
        //pivotXValue相对于该物体的哪个位置
        //pivotXType 相对于哪个物体
        //pivotYValue相对于该物体的哪个位置
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(x_start, x_end, y_start, y_end, x_relative, x_center_scale, y_relative, y_center_scale);
        ScaleAnimation scaleAnimation2 = new ScaleAnimation(x_start2, x_end2, y_start2, y_end2, x_relative, x_center_scale, y_relative, y_center_scale);
        scaleAnimation.setDuration(100);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(scaleAnimation2);
        scaleAnimation.setAnimationListener(animationListener);
        dialog.startAnimation(animationSet);
    }
}
