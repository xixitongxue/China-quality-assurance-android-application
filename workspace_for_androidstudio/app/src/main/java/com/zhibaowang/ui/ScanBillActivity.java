package com.zhibaowang.ui;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhibaowang.app.R;
import com.zhibaowang.asynctask.ZAsyncTask_TakePhoto2;
import com.zhibaowang.asynctask.ZAsyncTask_hehe;
import com.zhibaowang.asynctask.ZAsyncTask_saveInvoice;
import com.zhibaowang.asynctask.ZAsyncTask_yunmai;
import com.zhibaowang.component.ZButton;
import com.zhibaowang.component.ZCameraView;
import com.zhibaowang.model.Bill;
import com.zhibaowang.tools.B;
import com.zhibaowang.tools.ZDialogUtils;
import com.zhibaowang.tools.S;
import com.zhibaowang.tools.T;
import com.zhibaowang.tools.ZCameraTool;

/**
 * Created by zhaoyuntao on 2017/11/23.
 */

public class ScanBillActivity extends ZBaseActivity {

    private static final int CAMERA_OK = 1001;
    private static final int READ_OK = 1002;
    private FrameLayout frameLayout_center;
    private FrameLayout frameLayout_bottom;
    private ZCameraTool zCameraTool = new ZCameraTool();
    private View view_center;
    private View view_bottom;
    private static final int IMAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private static String[] PERMISSIONS_CAMERA = {Manifest.permission.CAMERA};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_bill);
        initView();
        initView_whenChooseCameraOrPhoto();
    }

    private void initReadFilePermission() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, READ_OK);
        } else {
            getPhotoFromStorage();
        }
    }

    private void initTools() {

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_CAMERA, CAMERA_OK);
        } else {
            initView_whenTakePhoto();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            S.s("intent data 不等于null");
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            Bitmap bm = BitmapFactory.decodeFile(imagePath);
            Drawable drawable = B.bitmapToDrawable(bm);
            S.s("选择的图片bm不等于null?:" + (bm != null));
            S.s("选择的图片drawble不等于null?:" + (drawable != null));
            c.close();
            final String string_rechoosephoto = getString(R.string.string_rechoosephoto);
            initView_whenPreviewPhoto(bm, drawable, string_rechoosephoto, new CallBack_onBackPressed() {
                @Override
                public void back() {
                    T.t(ScanBillActivity.this, string_rechoosephoto);
                    initView_whenChooseCameraOrPhoto();
                }
            });
        }
    }

    private void getPhotoFromStorage() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CAMERA_OK:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initView_whenTakePhoto();
                } else {
                    T.t(ScanBillActivity.this, "请手动打开相机权限");
                }
                break;
            case READ_OK:
                getPhotoFromStorage();
                break;
            default:
                break;
        }

    }

    private void initView_whenChooseCameraOrPhoto() {
        callBack_onBackPressed = null;
        view_center = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_scan_bill_center_choosephoto, null);
        view_bottom = null;
        ZButton zButton_takephoto = view_center.findViewById(R.id.button_center_takephoto);
        ZButton zButton_selectAPhoto = view_center.findViewById(R.id.button_center_choosefile);
        zButton_takephoto.setOnClickListener(new ZButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                S.s("开始拍照");
                //初始化拍照界面
                initTools();
            }
        });
        zButton_selectAPhoto.setOnClickListener(new ZButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                S.s("选择照片");
                //从系统相册选取一张照片
                initReadFilePermission();
            }
        });
        frameLayout_center.removeAllViews();
        if (view_center != null) {
            frameLayout_center.addView(view_center);
        }
        frameLayout_bottom.removeAllViews();
        if (view_bottom != null) {
            frameLayout_bottom.addView(view_bottom);
        }
    }

    private void initView_whenTakePhoto() {
        callBack_onBackPressed = new CallBack_onBackPressed() {
            @Override
            public void back() {
                initView_whenChooseCameraOrPhoto();
            }
        };
        view_bottom = LayoutInflater.from(ScanBillActivity.this).inflate(R.layout.layout_scan_bill_buttons_bottom_take_photo, null);
        ZButton zButton_takephoto = view_bottom.findViewById(R.id.button_bottom_takephoto_scan_bill);
        zButton_takephoto.setChoosen(true);
        final Camera camera = zCameraTool.initCamera(ScanBillActivity.this, CAMERA_OK);
        if (camera == null) {
            T.t(ScanBillActivity.this, getString(R.string.string_hardware_dose_not_support));
            view_center = null;
        } else {
            view_center = new ZCameraView(this, camera);
            zButton_takephoto.setOnClickListener(new ZButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //拍照
                    new ZAsyncTask_TakePhoto2(new ZAsyncTask_TakePhoto2.CallBack() {
                        @Override
                        public void whenGetPhoto(final Bitmap bitmap) {

                            final Drawable drawable = B.bitmapToDrawable(bitmap);
                            final String string_retakephoto = getString(R.string.string_retakephoto);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initView_whenPreviewPhoto(bitmap, drawable, string_retakephoto, new CallBack_onBackPressed() {
                                        @Override
                                        public void back() {
                                            T.t(ScanBillActivity.this, string_retakephoto);
                                            initView_whenTakePhoto();
                                        }
                                    });
                                }
                            });

                        }
                    }).execute(camera);
                }
            });
        }

        frameLayout_center.removeAllViews();
        if (view_center != null) {
            frameLayout_center.addView(view_center);
        }
        frameLayout_bottom.removeAllViews();
        if (view_bottom != null) {
            frameLayout_bottom.addView(view_bottom);
        }
    }

    private void initView_whenPreviewPhoto(final Bitmap bitmap, final Drawable bitmap_photo, String string_button_reTakePhoto, CallBack_onBackPressed callBack_onBackPressed) {
        this.callBack_onBackPressed = callBack_onBackPressed;
        view_center = new ImageView(ScanBillActivity.this);
        if (bitmap_photo != null) {
            Drawable photo = bitmap_photo;
            if (photo != null) {
                view_center.setBackground(photo);
            }
        } else {
            T.t(ScanBillActivity.this, "拍摄失败!");
            return;
        }
        view_center.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view_bottom = LayoutInflater.from(ScanBillActivity.this).inflate(R.layout.layout_scan_bill_buttons_bottom_preview_photo, null);
        ZButton zButton_photoisok = view_bottom.findViewById(R.id.button_upload_bottom_takemessage);
        zButton_photoisok.setOnClickListener(new ZButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                //照片满意,开始提取信息
                T.t(ScanBillActivity.this, getString(R.string.string_starttakemessagefromaphoto));
                recognizeInvoice(bitmap);
            }
        });
        ZButton zButton_retakephoto = view_bottom.findViewById(R.id.button_upload_bottom_retake_photo);
        zButton_retakephoto.setText_center(string_button_reTakePhoto);
        zButton_retakephoto.setOnClickListener(new ZButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                //重新拍照或者重新选取照片
                if (ScanBillActivity.this.callBack_onBackPressed != null) {
                    ScanBillActivity.this.callBack_onBackPressed.back();
                }
            }
        });
        if (view_center != null) {
            frameLayout_center.removeAllViews();
            frameLayout_center.addView(view_center);
        }
        if (view_bottom != null) {
            frameLayout_bottom.removeAllViews();
            frameLayout_bottom.addView(view_bottom);
        }
    }

    /**
     * 识别发票
     *
     * @param bitmap
     */
    private void recognizeInvoice(Bitmap bitmap) {
        showProgress(true);
        //合合科技的
        ZAsyncTask_hehe zTask = new ZAsyncTask_hehe(new ZAsyncTask_hehe.CallBack() {
            @Override
            public void onPostExecute(Bill bill) {
                initView_whenTakeMessageFromBill(bill);//将识别结果显示到ui
                showProgress(false);
            }
        });
//        ZAsyncTask_yunmai zTask = new ZAsyncTask_yunmai(new ZAsyncTask_yunmai.CallBack() {
//            @Override
//            public void onPostExecute(Bill bill) {
//                initView_whenTakeMessageFromBill(bill);//将识别结果显示到ui
//                showProgress(false);
//            }
//        });
        Bill bill = new Bill(zTask.getMap_key_bill());
        bill.setFunc(Bill.FUNC_USER_BILL_RECOGNIZE);
        bill.setBitmap(bitmap);
        zTask.execute(bill);
    }

    /**
     * 查验真伪
     */
    private void distinguishInvoice(Bill bill) {
        bill.setOk_distinguish(false);
        showProgress(true);
        ZAsyncTask_hehe asyncTask_hehe = new ZAsyncTask_hehe(new ZAsyncTask_hehe.CallBack() {
            @Override
            public void onPostExecute(Bill bill) {
                initView_whenDistinguishBill(bill);//将查验结果显示到ui
                showProgress(false);
            }
        });
        bill.setFunc(Bill.FUNC_USER_BILL_DISTINGUISH);
        asyncTask_hehe.execute(bill);
    }

    private void initView_whenTakeMessageFromBill(final Bill bill) {
        if (bill.isOk_recognize()) {
            callBack_onBackPressed = new CallBack_onBackPressed() {
                @Override
                public void back() {
                    ZDialogUtils.show(ScanBillActivity.this, "确定要退出当前操作吗? 现有信息将会丢弃", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            T.t(ScanBillActivity.this, "重新拍照");
                            dialog.dismiss();
                            initView_whenTakePhoto();
                        }
                    }, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            };
            T.t(ScanBillActivity.this, "发票识别成功");
            view_center = new ScrollView(ScanBillActivity.this);
            view_center.setVerticalScrollBarEnabled(true);
            LinearLayout view_container = new LinearLayout(ScanBillActivity.this);
            view_container.setOrientation(LinearLayout.VERTICAL);
            view_container.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT, ScrollView.LayoutParams.WRAP_CONTENT));
            ((ScrollView) view_center).addView(view_container);
            for (String key : bill.map_values.keySet()) {
                String key_chinese = bill.map_keys_chinese.get(key);//获取中文字段
                String value = bill.map_values.get(key);//获取对应的值
                LinearLayout item_list = new LinearLayout(ScanBillActivity.this);
                item_list.setOrientation(LinearLayout.HORIZONTAL);
                TextView textView = new TextView(ScanBillActivity.this);
                textView.setText(key_chinese);
                EditText editText = new EditText(ScanBillActivity.this);
                editText.setText(value);
                item_list.addView(textView);
                item_list.addView(editText);
                view_container.addView(item_list);
            }
            view_bottom = LayoutInflater.from(ScanBillActivity.this).inflate(R.layout.layout_scan_bill_buttons_bottom_show_message_bill, null);
            ZButton zButton_retakephoto2 = view_bottom.findViewById(R.id.button_bottom_retakemessage);
            zButton_retakephoto2.setOnClickListener(new ZButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //对发票识别结果不满意,希望重新拍摄
                    ZDialogUtils.show(ScanBillActivity.this, "确定要重新识别吗", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            T.t(ScanBillActivity.this, "重新拍照");
                            initView_whenTakePhoto();//初始化拍照页面的布局
                            dialog.cancel();
                        }

                    }, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                }
            });
            ZButton zButton_savebill = view_bottom.findViewById(R.id.button_bottom_save_bill);
            zButton_savebill.setOnClickListener(new ZButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //保存发票信息
                    T.t(ScanBillActivity.this, "保存发票信息");
                    new ZAsyncTask_saveInvoice(new ZAsyncTask_saveInvoice.CallBack() {
                        @Override
                        public void whenGetResult(boolean isSuccess, String msg) {
                            if (isSuccess) {
                                T.t(ScanBillActivity.this, "上传成功" + msg);
                            } else {
                                T.t(ScanBillActivity.this, "上传失败:" + msg);
                            }
                        }

                        @Override
                        public void onCancelled() {
                            showProgress(false);
                        }
                    }).execute(bill.getJson());
                }
            });
            ZButton zButton_distinguish = view_bottom.findViewById(R.id.button_bottom_check_bill);
            zButton_distinguish.setOnClickListener(new ZButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //查验真伪
                    distinguishInvoice(bill);
                    T.t(ScanBillActivity.this, "查验发票真伪");
                }
            });
            if (view_center != null) {
                frameLayout_center.removeAllViews();
                frameLayout_center.addView(view_center);
            }
            if (view_bottom != null) {
                frameLayout_bottom.removeAllViews();
                frameLayout_bottom.addView(view_bottom);
            }
            bill.recycle();
        } else {
            T.t(ScanBillActivity.this, "发票识别失败:" + bill.getMessage_recognize());
        }
    }

    private void initView_whenDistinguishBill(final Bill bill) {
        callBack_onBackPressed = new CallBack_onBackPressed() {
            @Override
            public void back() {
                initView_whenTakeMessageFromBill(bill);
            }
        };
        if (bill.isOk_distinguish()) {
            T.t(ScanBillActivity.this, bill.isReal ? "发票是真的!" : (bill.isOk_distinguish() ? "发票是假的" : "查询失败"));
            view_center = new ScrollView(ScanBillActivity.this);
            view_center.setVerticalScrollBarEnabled(true);
            LinearLayout view_container = new LinearLayout(ScanBillActivity.this);
            view_container.setOrientation(LinearLayout.VERTICAL);
            view_container.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT, ScrollView.LayoutParams.WRAP_CONTENT));
            ((ScrollView) view_center).addView(view_container);
            for (String key : bill.map_values.keySet()) {
                String key_chinese = bill.map_keys_chinese.get(key);//获取中文字段
                String value = bill.map_values.get(key);//获取对应的值
                LinearLayout item_list = new LinearLayout(ScanBillActivity.this);
                item_list.setOrientation(LinearLayout.HORIZONTAL);
                TextView textView = new TextView(ScanBillActivity.this);
                textView.setText(key_chinese);
                EditText editText = new EditText(ScanBillActivity.this);
                editText.setText(value);
                item_list.addView(textView);
                item_list.addView(editText);
                view_container.addView(item_list);
            }
            view_bottom = LayoutInflater.from(ScanBillActivity.this).inflate(R.layout.layout_scan_bill_buttons_bottom_distinguish_bill, null);
            ZButton zButton_distinguish_back = view_bottom.findViewById(R.id.button_bottom_distinguish_bill_ok);
            zButton_distinguish_back.setOnClickListener(new ZButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击返回
                    initView_whenTakeMessageFromBill(bill);
                }
            });
            if (view_center != null) {
                frameLayout_center.removeAllViews();
                frameLayout_center.addView(view_center);
            }
            if (view_bottom != null) {
                frameLayout_bottom.removeAllViews();
                frameLayout_bottom.addView(view_bottom);
            }
        } else {
            T.t(ScanBillActivity.this, "发票查询失败:" + bill.getMessage_recognize());
        }
    }

    private void initView() {

        frameLayout_center = findViewById(R.id.framelayout_scan_bill_center);
        frameLayout_bottom = findViewById(R.id.framelayout_scan_bill_bottom);
        progressBar = findViewById(R.id.framelayout_progress_scan_bill);

    }

    public void onBackClick(View view) {
        showProgress(false);
        finish();
    }


    @Override
    public void onBackPressed() {
        if (callBack_onBackPressed != null) {
            callBack_onBackPressed.back();
        } else {
            super.onBackPressed();
        }
    }

    CallBack_onBackPressed callBack_onBackPressed;

    private interface CallBack_onBackPressed {
        void back();
    }

    @Override
    public void finish() {
        super.finish();
    }
}
