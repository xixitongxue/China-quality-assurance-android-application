package com.zhibaowang.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhibaowang.app.HongboheshiApplication;
import com.zhibaowang.app.R;
import com.zhibaowang.asynctask.ZAsyncTask_loginByThirdParty;
import com.zhibaowang.asynctask.ZAsyncTask_loginByHTSPServer;
import com.zhibaowang.component.ZButton;
import com.zhibaowang.model.User;
import com.zhibaowang.tools.S;
import com.zhibaowang.tools.T;
import com.zhibaowang.tools.ZSharedPreferencesTool;
import com.zhibaowang.tools.Ztimer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends ZBaseActivity {

    private User user;
    private ZAsyncTask_loginByHTSPServer task;
    private ZAsyncTask_loginByThirdParty task_loginByThridParty;
    private Button button_authcode;
    private EditText edittext_username_login;
    private EditText edittext_username_register;
    private EditText edittext_authcode_register;
    private EditText edittext_password;
    private TextView textview_forgot;
    private ViewPager viewPager_login;
    private TabLayout tabLayout;
    private final String key_username_register = "username_register";
    private final String key_username_login = "username_login";
    private final String key_password_login = "password_login";
    private  String[] list_title ;


    private Ztimer ztimer;
    private ZSharedPreferencesTool zSharedPreferencesTool = new ZSharedPreferencesTool();
    private int seconds_auth = 20;

    private void handleResult_login(int func, boolean isSuccess, String msg) {
        final String content = msg;
        switch (func) {
            case ZAsyncTask_loginByHTSPServer.FUNC_USER_REQUEST_AUTHCODE:
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
                                    LoginActivity.this.button_authcode.setTextColor(color_auth);
                                    LoginActivity.this.button_authcode.setBackground(resources.getDrawable(R.drawable.button_authcode_unpress));
                                    LoginActivity.this.button_authcode.setText(text);
                                    LoginActivity.this.button_authcode.setEnabled(true);
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
                                    int color_auth = resources.getColor(R.color.color_auth);
                                    String text = resources.getString(R.string.string_after_fiew_seconds);
                                    LoginActivity.this.button_authcode.setTextColor(Color.WHITE);
                                    LoginActivity.this.button_authcode.setBackground(resources.getDrawable(R.drawable.button_authcode_press));
                                    LoginActivity.this.button_authcode.setText(new String(time_tmp + text));
                                    LoginActivity.this.button_authcode.setEnabled(false);
                                }
                            });
                        }
                    });
                    ztimer.start();
                    T.t(LoginActivity.this, getString(R.string.string_auth_success) + ":" + content);
                } else {
                    T.t(LoginActivity.this, getString(R.string.string_auth_failed) + ":" + content);
                    LoginActivity.this.button_authcode.setEnabled(true);
                }
                break;
            case ZAsyncTask_loginByHTSPServer.FUNC_USER_CHECK_AUTHCODE:
                if (isSuccess) {
                    T.t(LoginActivity.this, getString(R.string.string_register_success) + ":" + content);
                    user.setLogin(true);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    T.t(LoginActivity.this, getString(R.string.string_register_failed) + ":" + content);
                }
                break;
            case ZAsyncTask_loginByHTSPServer.FUNC_USER_LOGIN:
                if (isSuccess) {
                    T.t(LoginActivity.this, getString(R.string.string_login_success) + ":" + content);
                    user.setLogin(true);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    T.t(LoginActivity.this, getString(R.string.string_login_failed) + ":" + content);
                    user.setLogin(false);
                }
                break;
        }
        showProgress(false);
    }

    private float percent_line_tabview = 0.6f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        user = HongboheshiApplication.getUser();
        progressBar = findViewById(R.id.login_progress);
        viewPager_login = findViewById(R.id.viewpager_login);
        tabLayout = findViewById(R.id.tablayout_login);

        tabLayout.setupWithViewPager(viewPager_login);

        View layout_login = LayoutInflater.from(LoginActivity.this).inflate(R.layout.layout_login, null);
        edittext_password = layout_login.findViewById(R.id.edittext_password);
        edittext_username_login = layout_login.findViewById(R.id.edittext_username_login);
        textview_forgot = layout_login.findViewById(R.id.textview_forgot);
        ZButton zButton_qqlogin = findViewById(R.id.button_qqlogin);
        zButton_qqlogin.setOnClickListener(new ZButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                S.s("QQ登录");
                thirdLogin(QQ.NAME);
            }
        });
        ZButton zButton_weibologin = findViewById(R.id.button_weibologin);
        zButton_weibologin.setOnClickListener(new ZButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                S.s("新浪微博登录");
                thirdLogin(SinaWeibo.NAME);
            }
        });
        ZButton zButton_wxlogin = findViewById(R.id.button_wxlogin);
        zButton_wxlogin.setOnClickListener(new ZButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                S.s("微信登录");
                thirdLogin(Wechat.NAME);
            }
        });

        View layout_register = LayoutInflater.from(LoginActivity.this).inflate(R.layout.layout_register, null);
        edittext_username_register = layout_register.findViewById(R.id.edittext_username_register);
        edittext_authcode_register = layout_register.findViewById(R.id.edittext_authcode);
        button_authcode = layout_register.findViewById(R.id.button_authcode);
        button_authcode.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LoginActivity.this.button_authcode.setEnabled(false);
                String username = edittext_username_register.getText().toString();
                if (!isUsernameValid(username)) {
                    T.t(LoginActivity.this, "请输入一个有效的手机号!");
                    return;
                } else {
                    zSharedPreferencesTool.write(key_username_register, username, getApplicationContext());
                    T.t(LoginActivity.this, "正在获取验证码");
                }
                user.setUsername(username);
                user.setPassword("");
                user.setTimestamp(System.currentTimeMillis());
                //注册操作
                task = new ZAsyncTask_loginByHTSPServer(new ZAsyncTask_loginByHTSPServer.CallBack() {
                    @Override
                    public void whenGetResult(final int func, final boolean isSuccess, final String msg) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handleResult_login(func, isSuccess, msg);
                            }
                        });
                    }

                    @Override
                    public void onCancelled() {
                        showProgress(false);
                    }
                });
                task.execute(user, ZAsyncTask_loginByHTSPServer.FUNC_USER_REQUEST_AUTHCODE);
                //--------
            }
        });

        Button button_register = layout_register.findViewById(R.id.button_register);
        button_register.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String authcode = edittext_authcode_register.getText().toString();
                if (authcode == null || authcode.equals("")) {
                    T.t(LoginActivity.this, "请输入验证码");
                    return;
                }
                showProgress(true);
                String username = edittext_username_register.getText().toString();
                user.setUsername(username);
                user.setPassword("");
                user.setAuthcode(authcode);
                user.setTimestamp(System.currentTimeMillis());
                task = new ZAsyncTask_loginByHTSPServer(new ZAsyncTask_loginByHTSPServer.CallBack() {
                    @Override
                    public void whenGetResult(final int func, final boolean isSuccess, final String msg) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showProgress(false);
                                handleResult_login(func, isSuccess, msg);
                            }
                        });
                    }

                    @Override
                    public void onCancelled() {
                        showProgress(false);
                    }
                });
                task.execute(user, ZAsyncTask_loginByHTSPServer.FUNC_USER_CHECK_AUTHCODE);
                //--------
            }
        });


        final List<View> list = new ArrayList<>();
        list.add(layout_register);
        list.add(layout_login);

        viewPager_login.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == list.get((int) Integer.parseInt(object.toString()));
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(list.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(list.get(position));
                return position;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if(list_title==null){
                    list_title=getResources().getStringArray(R.array.arr_title_viewpager_login);
                }
                return list_title[position];
            }
        });
        setIndicator(LoginActivity.this, tabLayout, percent_line_tabview);

        edittext_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        Button button_login = layout_login.findViewById(R.id.button_login);
        button_login.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        //test
        if (S.debug_input) {
            String username_login = zSharedPreferencesTool.getString(getApplicationContext(), key_username_login, "");
            String password_login = zSharedPreferencesTool.getString(getApplicationContext(), key_password_login, "");

            if (username_login == null || username_login.equals("")) {
                username_login = "18210597531";
                password_login = "18210597531";
                zSharedPreferencesTool.write(key_username_login, username_login, getApplicationContext());
                zSharedPreferencesTool.write(key_password_login, password_login, getApplicationContext());
            }
            edittext_username_login.setText(username_login);
            edittext_password.setText(password_login);

            String username_register = zSharedPreferencesTool.getString(getApplicationContext(), key_username_register, "");
            if (username_register == null || username_register.equals("")) {
                username_register = "18210597531";
                zSharedPreferencesTool.write(key_username_register, username_register, getApplicationContext());
            }

            edittext_username_register.setText(username_register);
        }

        SpannableString string_forget_password = new SpannableString(getString(R.string.string_forget_password));
        string_forget_password.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(LoginActivity.this, PasswordActivity.class);
                startActivity(intent);
            }
        }, 0, string_forget_password.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textview_forgot.setText(string_forget_password);
        textview_forgot.setMovementMethod(LinkMovementMethod.getInstance());//激活
    }

    private void thirdLogin(String name) {
        showProgress(true);
        task_loginByThridParty = new ZAsyncTask_loginByThirdParty(new ZAsyncTask_loginByThirdParty.CallBack() {
            @Override
            public void whenGetResult(String token, String userId, String nickName, String icon, String userGender) {
                user.setIcon(icon);
                user.setGender(userGender);
                user.setToken(token);
                user.setUserId(userId);
                user.setNickName(nickName);
            }

            @Override
            public void onCancelled(String msg) {
                T.t(LoginActivity.this, msg);
                showProgress(false);
            }
        });
        task_loginByThridParty.execute(name);
    }

    private void attemptLogin() {
        edittext_password.setDrawingCacheBackgroundColor(Color.GREEN);
        setEditTextError(edittext_username_login, null);
        setEditTextError(edittext_password, null);
        String username = edittext_username_login.getText().toString();
        String password = edittext_password.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            setEditTextError(edittext_password, getString(R.string.error_invalid_password));
            focusView = edittext_password;
            cancel = true;
        }
        if (TextUtils.isEmpty(username)) {
            setEditTextError(edittext_username_login, getString(R.string.error_field_required));
            focusView = edittext_username_login;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            setEditTextError(edittext_username_login, getString(R.string.error_invalid_email));
            focusView = edittext_username_login;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            user.setUsername(username);
            user.setPassword(password);
            user.setTimestamp(System.currentTimeMillis());
            showProgress(true);
            task = new ZAsyncTask_loginByHTSPServer(new ZAsyncTask_loginByHTSPServer.CallBack() {
                @Override
                public void whenGetResult(final int func, final boolean isSuccess, final String msg) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            handleResult_login(func, isSuccess, msg);
                        }
                    });
                }

                @Override
                public void onCancelled() {
                    showProgress(false);
                }
            });
            task.execute(user, ZAsyncTask_loginByHTSPServer.FUNC_USER_LOGIN);
        }
    }

    private void setIndicator(Context context, TabLayout tabs, float percent_width) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (tabStrip == null) {
            return;
        }
        tabStrip.setAccessible(true);
        LinearLayout ll_tab = null;
        try {
            ll_tab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = windowmanager.getDefaultDisplay();
        Point p = new Point();
        d.getSize(p);
        int w_screen = p.x; // 屏幕宽（像素）
        int h_screen = p.y;
        int num_child = ll_tab.getChildCount();
        int width_child_px = (int) (w_screen / (float) num_child);
        int left = (int) ((width_child_px * (1 - percent_width)) / 2);
        for (int i = 0; i < num_child; i++) {
            View child = ll_tab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = left;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }


    /**
     * 当权限申请完成后
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        }
    }

    private void setEditTextError(EditText edittext, String string_error) {
        Drawable drawable = getResources().getDrawable(android.R.drawable.ic_delete);
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) LoginActivity.this).getWindowManager().getDefaultDisplay().getMetrics(metric);
        drawable.setBounds(0, 0, (int) (22 * metric.density), (int) (22 * metric.density));
        edittext.setError(string_error, drawable);
    }

    public void onBackClick(View view) {
        view.setBackgroundColor(Color.argb(33, 0, 0, 0));
        finish();
    }


    @Override
    protected void onDestroy() {
        if (task != null) {
            task.cancel(true);
        }
        if (task_loginByThridParty != null) {
            task_loginByThridParty.cancel(true);
        }
        if (ztimer != null) {
            ztimer.close();
        }
        super.onDestroy();
    }
}

