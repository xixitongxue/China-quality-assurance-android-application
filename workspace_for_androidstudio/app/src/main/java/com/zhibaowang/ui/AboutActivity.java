package com.zhibaowang.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.zhibaowang.app.R;
import com.zhibaowang.component.ZButton;
import com.zhibaowang.tools.S;
import com.zhibaowang.tools.T;

/**
 * Created by zhaoyuntao on 2017/12/29.
 */

public class AboutActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initView();
    }

    private void initView() {
    }


    public void back(View view) {
        finish();
    }

}
