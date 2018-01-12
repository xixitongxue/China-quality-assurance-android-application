package com.zhibaowang.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhibaowang.app.HongboheshiApplication;
import com.zhibaowang.app.R;
import com.zhibaowang.asynctask.ZAsyncTask_resetPassword;
import com.zhibaowang.model.User;
import com.zhibaowang.tools.T;
import com.zhibaowang.tools.Ztimer;

public class PasswordActivity extends ZBaseActivity {

    private User user;
    private Ztimer ztimer;
    private int seconds_auth = 20;
    private Button button_authcode;
    private Button button_resetpassword;
    private String password_tmp;
    private EditText edittext_password;
    private EditText edittext_authcode;
    private ZAsyncTask_resetPassword task;
    private int length_auth = 6;

    private void handleResult_resetPassword(int func, boolean isSuccess, String msg) {
        final String content = msg;
        switch (func) {
            case ZAsyncTask_resetPassword.FUNC_USER_REQUEST_AUTHCODE:
                if (isSuccess) {
                    ztimer = Ztimer.getInstance(seconds_auth);
                    ztimer.setCallBack(new Ztimer.CallBack() {
                        @Override
                        public void whenRequestOk() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Resources resources = getResources();
                                    int color_auth = resources.getColor(R.color.color_auth);
                                    String text = resources.getString(R.string.string_get_auth);
                                    PasswordActivity.this.button_authcode.setTextColor(color_auth);
                                    PasswordActivity.this.button_authcode.setBackground(resources.getDrawable(R.drawable.button_authcode_unpress));
                                    PasswordActivity.this.button_authcode.setText(text);
                                    PasswordActivity.this.button_authcode.setEnabled(true);
                                }
                            });
                        }

                        @Override
                        public void whenRest(int time_rest) {
                            final int time_tmp = time_rest;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Resources resources = getResources();
                                    String text = resources.getString(R.string.string_after_fiew_seconds);
                                    PasswordActivity.this.button_authcode.setTextColor(Color.WHITE);
                                    PasswordActivity.this.button_authcode.setBackground(resources.getDrawable(R.drawable.button_authcode_press));
                                    PasswordActivity.this.button_authcode.setText(new String(time_tmp + text));
                                    PasswordActivity.this.button_authcode.setEnabled(false);
                                }
                            });
                        }
                    });
                    ztimer.start();
                    T.t(PasswordActivity.this, getString(R.string.string_auth_success) + ":" + content);
                } else {
                    T.t(PasswordActivity.this, getString(R.string.string_auth_failed) + ":" + content);
                    PasswordActivity.this.button_authcode.setEnabled(true);
                }
                break;
            case ZAsyncTask_resetPassword.FUNC_USER_RESETPASSWORD:
                if (isSuccess) {
                    T.t(PasswordActivity.this, getString(R.string.string_resetpassword_success) + ":" + content);
                    user.setPassword(password_tmp);
                    Intent intent = new Intent(PasswordActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    T.t(PasswordActivity.this, getString(R.string.string_resetpassword_failed) + ":" + content);
                }
                break;
        }
        showProgress(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        init();
    }

    private void init() {
        user = HongboheshiApplication.getUser();
        progressBar = findViewById(R.id.resetpassword_progress);
        button_authcode = findViewById(R.id.button_authcode_changepassword);
        button_authcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordActivity.this.button_authcode.setEnabled(false);
                user.setTimestamp(System.currentTimeMillis());
                //请求验证码操作
                task = new ZAsyncTask_resetPassword(new ZAsyncTask_resetPassword.CallBack() {
                    @Override
                    public void whenGetResult(final int func, final boolean isSuccess, final String msg) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handleResult_resetPassword(func, isSuccess, msg);
                            }
                        });
                    }

                    @Override
                    public void onCancelled() {
                        showProgress(false);
                    }
                });
                task.execute(user, ZAsyncTask_resetPassword.FUNC_USER_REQUEST_AUTHCODE);
            }
        });
        edittext_authcode = findViewById(R.id.edittext_authcode_changepassword);
        edittext_password = findViewById(R.id.edittext_password_changepassword);

        button_resetpassword = findViewById(R.id.button_resetpassword);
        button_resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先判断验证码长度是否符合要求
                String authcode = edittext_authcode.getText().toString();
                if (authcode == null || authcode.equals("") || authcode.length() < length_auth) {
                    T.t(getApplicationContext(), "请输入正确的验证码");
                    return;
                }
                //如果验证码长度符合要求,再判断密码长度是否符合要求
                PasswordActivity.this.password_tmp = edittext_password.getText().toString();
                if (PasswordActivity.this.password_tmp != null && !PasswordActivity.this.password_tmp.equals("")) {
                    if (PasswordActivity.this.password_tmp.length() < 6) {
                        T.t(getApplicationContext(), "密码长度不能小于6位");
                    } else {
                        user.setPassword(PasswordActivity.this.password_tmp);
                        user.setTimestamp(System.currentTimeMillis());
                        //修改密码操作
                        task = new ZAsyncTask_resetPassword(new ZAsyncTask_resetPassword.CallBack() {
                            @Override
                            public void whenGetResult(final int func, final boolean isSuccess, final String msg) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        handleResult_resetPassword(func, isSuccess, msg);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled() {
                                showProgress(false);
                            }
                        });
                        task.execute(user, ZAsyncTask_resetPassword.FUNC_USER_RESETPASSWORD);
                    }
                } else {
                    T.t(getApplicationContext(), "密码不能为空!");
                    return;
                }
            }
        });
    }

    public void onBackClick(View view) {
        //被点击后变色
        view.setBackgroundColor(Color.argb(33, 0, 0, 0));
        finish();
    }

    @Override
    protected void onDestroy() {
        if (task != null) {
            task.cancel(true);
        }
        if (ztimer != null) {
            ztimer.close();
        }
        super.onDestroy();
    }
}
