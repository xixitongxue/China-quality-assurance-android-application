package com.zhibaowang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zhibaowang.app.R;
import com.zhibaowang.component.ZButton;
import com.zhibaowang.tools.T;

public class UserCenterActivity extends ZBaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercenter);

        initView();
    }

    private void initView() {
        ZButton zButton=findViewById(R.id.button_sharethisapptomyfriend);
        zButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.t(getApplicationContext(),"向好友推荐此应用");
            }
        });
    }

    /**
     * 点击事件: 右上角的用户头像
     * @param view
     */
    public void goToUserPage(View view) {
        Intent intent=new Intent(UserCenterActivity.this,UserActivity.class);
        startActivity(intent);
    }
    public void onBackClick(View v){
        finish();
    }
}
