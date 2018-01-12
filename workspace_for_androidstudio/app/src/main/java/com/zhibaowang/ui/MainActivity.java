package com.zhibaowang.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.zhibaowang.app.HongboheshiApplication;
import com.zhibaowang.app.R;
import com.zhibaowang.component.ZButton;
import com.zhibaowang.component.ZPagerAdapter;
import com.zhibaowang.component.ZViewPager;
import com.zhibaowang.component.ZWindowView;
import com.zhibaowang.model.User;
import com.zhibaowang.tools.B;
import com.zhibaowang.tools.T;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ZBaseActivity {

    private final String[] arr_personal = {"录入订单", "", "查看订单", "", "实 例", ""};
    private ZWindowView.CallBack callBack_main = new ZWindowView.CallBack() {
        @Override
        public boolean whenClick(int position) {
            Intent intent ;
            switch (position) {
                case 0:
                    intent = new Intent(MainActivity.this, ScanBillActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    intent = new Intent(MainActivity.this, SystemSettingsActivity.class);
                    startActivity(intent);
                    break;
            }
            return false;
        }
    };
    private final String[] arr_enterprise = {"新建订单", "销售统计", "内部消息", "产品管理", "店面管理", "品牌管理"};
    private final int[] arr_bitmap_personal_choose = {R.drawable.scanning, R.drawable.upload, R.drawable.administer, R.drawable.sharing, R.drawable.instance, R.drawable.setting};
    private final int[] arr_bitmap_personal_unchoose = {R.drawable.scanning, R.drawable.upload, R.drawable.administer, R.drawable.sharing, R.drawable.instance, R.drawable.setting};
    private final int[] arr_bitmap_enterprise_choose = {R.drawable.neworder, R.drawable.sell, R.drawable.news, R.drawable.products, R.drawable.store, R.drawable.brands};
    private final int[] arr_bitmap_enterprise_unchoose = {R.drawable.neworder, R.drawable.sell, R.drawable.news, R.drawable.products, R.drawable.store, R.drawable.brands};
    private ZViewPager viewPager_buttons_main;
    private ZViewPager viewPager_back_main;
    private ZButton[] zButtons;
    private final int CODE_LOGIN = 110;
    private final int CODE_SETTING = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false); //禁止滑动删除
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            // 透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        Intent intent = getIntent();
        if (intent != null) {
            String content = intent.getStringExtra("MSG");
        }
        initView();
        initButtons();
        initCallBacks();
    }

    /**
     * 点击事件: 右上角的用户头像
     *
     * @param view
     */
    public void goToUserCenterPage(View view) {
        Intent intent = new Intent(MainActivity.this, UserCenterActivity.class);
        startActivity(intent);
    }

    /**
     * 右上角的登录按钮
     *
     * @param view
     */
    public void login(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(intent, CODE_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CODE_LOGIN:
                //登录后判断登录状态
                judgeLoginState();
                break;
            case CODE_SETTING:
                //
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initCallBacks() {
        final ZButton zButton_personal = MainActivity.this.zButtons[0];
        final ZButton zButton_enterprise = MainActivity.this.zButtons[1];
        zButton_personal.setOnClickListener(new ZButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                zButton_enterprise.setChoosen(false);
                viewPager_buttons_main.setCurrentItem(0);
                viewPager_back_main.setCurrentItem(0);
            }
        });

        zButton_enterprise.setOnClickListener(new ZButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                zButton_personal.setChoosen(false);
                viewPager_buttons_main.setCurrentItem(1);
                viewPager_back_main.setCurrentItem(1);
            }
        });
        zButton_personal.setChoosen(true);
        viewPager_buttons_main.setCurrentItem(0);
    }

    private void initView() {
        viewPager_buttons_main = findViewById(R.id.viewpager_main);
        viewPager_back_main = findViewById(R.id.zviewpager_back_main);
        ZButton zButton_personal = findViewById(R.id.button_personal);
        ZButton zButton_enterprise = findViewById(R.id.button_enterprise);
        ZButton[] zButtons = {zButton_personal, zButton_enterprise};
        MainActivity.this.zButtons = zButtons;
        judgeLoginState();
    }

    /**
     * 根据用户登录状态进行一些操作,比如隐藏登录的按钮等
     */
    private void judgeLoginState() {
        User user = HongboheshiApplication.getUser();
        Button button = findViewById(R.id.button_gotosigninpage);
        ZButton button_gotouserpage = findViewById(R.id.button_gotouserpage);
        if (user != null && user.isLogin()) {
            //如果用户已登录,则显示为用户中心按钮
            button.setVisibility(View.INVISIBLE);
            button_gotouserpage.setVisibility(View.VISIBLE);
            button_gotouserpage.setDrawable_center(B.getBitmapById_Percent(MainActivity.this, R.drawable.usericon, 1));
        } else {
            //如果用户未登录,则显示登录
            button.setText(getString(R.string.string_login));
            button.setVisibility(View.VISIBLE);
            button_gotouserpage.setVisibility(View.INVISIBLE);
        }
    }

    private void initButtons() {

        final List<ImageView> viewList_viewpager_back = new ArrayList<>();
        ImageView iv_firstPage = new ImageView(this);
        ImageView iv_secondPage = new ImageView(this);
        iv_firstPage.setBackgroundResource(R.drawable.background);
        iv_secondPage.setBackgroundResource(R.drawable.background_enterprise);
        viewList_viewpager_back.add(iv_firstPage);
        viewList_viewpager_back.add(iv_secondPage);

        ZPagerAdapter pagerAdapter_enterprise = new ZPagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == viewList_viewpager_back.get((int) Integer.parseInt(object.toString()));
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewList_viewpager_back.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList_viewpager_back.get(position));
                return position;
            }
        };
        viewPager_back_main.setAdapter(pagerAdapter_enterprise);
        viewPager_back_main.setScrollByTouch(false);
        viewPager_back_main.setScrollWay();

        ZWindowView zWindowView = new ZWindowView(MainActivity.this);
        for (int i = 0; i < arr_personal.length; i++) {
            ZWindowView.Child child = zWindowView.new Child(arr_personal[i], arr_bitmap_personal_choose[i], arr_bitmap_personal_unchoose[i]);
            zWindowView.initDate(child);
        }
        zWindowView.setCallBack(callBack_main);


        ZWindowView zWindowView2 = new ZWindowView(MainActivity.this);
        for (int i = 0; i < arr_personal.length; i++) {
            ZWindowView.Child child = zWindowView2.new Child(arr_enterprise[i], arr_bitmap_enterprise_choose[i], arr_bitmap_enterprise_unchoose[i]);
            zWindowView2.initDate(child);
        }
        zWindowView2.setCallBack(new ZWindowView.CallBack() {
            @Override
            public boolean whenClick(int position) {
                T.t(MainActivity.this, arr_enterprise[position]);
                return false;
            }
        });


        final List<ZWindowView> viewList = new ArrayList<>();
        viewList.add(zWindowView);
        viewList.add(zWindowView2);

        ZPagerAdapter pagerAdapter = new ZPagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == viewList.get((int) Integer.parseInt(object.toString()));
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return position;
            }
        };
        viewPager_buttons_main.setAdapter(pagerAdapter);
        viewPager_buttons_main.setOnPageChangeListener(new ZViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                viewPager_back_main.setCurrentItem(position);
                for (int i = 0; i < zButtons.length; i++) {
                    if (position == i) {
                        zButtons[position].setChoosen(true);
                    } else {
                        zButtons[i].setChoosen(false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                for (int i = 0; i < viewList.size(); i++) {
                    ZWindowView zview = viewList.get(i);
                    if (zview != null) {
                        zview.resetAllItem();
                    }
                }
            }
        });
//        viewPager_buttons_main.setPageTransformer(true, neworder ZViewPager.PageTransformer() {
//            private final float MIN_SCALE = 0.7f;
//
//            public void transformPage(View view, float position) {
//                int pageWidth = view.getWidth();
//
//                if (position < -1) { // [-Infinity,-1)
//                    // This page is way off-screen to the left.
//                    view.setAlpha(0);
//
//                } else if (position <= 0) { // [-1,0]
//                    // Use the default slide transition when moving to the left page
//                    view.setAlpha(1);
//                    view.setTranslationX(0);
//                    view.setScaleX(1);
//                    view.setScaleY(1);
//
//                } else if (position <= 1) { // (0,1]
//                    // Fade the page out.
//                    view.setAlpha(1 - position);
//
//                    // Counteract the default slide transition
//                    view.setTranslationX(pageWidth * -position);
//
//                    // Scale the page down (between MIN_SCALE and 1)
//                    float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
//                    view.setScaleX(scaleFactor);
//                    view.setScaleY(scaleFactor);
//
//                } else { // (1,+Infinity]
//                    // This page is way off-screen to the right.
//                    view.setAlpha(0);
//                }
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        B.close();
        super.onDestroy();
    }
}
