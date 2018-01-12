package com.zhibaowang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import com.zhibaowang.app.R;
import com.zhibaowang.component.ZButton;

public class UserActivity extends ZBaseActivity {

    private ZButton zButton_user_icon;
    private Spinner spinner;
    private String[] arr = {"人民币", "美元", "英镑"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();
    }

    private void initView() {
        zButton_user_icon = findViewById(R.id.button_user_icon);
    }

    public void settings(View view) {
        Intent intent = new Intent(UserActivity.this, UserCenterActivity.class);
        startActivity(intent);
    }

    public void invoice(View view) {
        Intent intent = new Intent(UserActivity.this, MyInvoiceActivity.class);
        startActivity(intent);
    }

    public void onBackClick(View view) {
        finish();
    }
}
