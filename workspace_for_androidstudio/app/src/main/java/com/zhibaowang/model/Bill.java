package com.zhibaowang.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by zhaoyuntao on 2017/11/30.
 */

public class Bill {

    //商品列表
    private List<Good> list_goods;
    private String json;
    //==================
    public LinkedHashMap<String, String> map_values;//发票字段
    public HashMap<String, String> map_keys_chinese;//发票key对应的中文
    private Bitmap bitmap;//发票图像
    private String message_recognize;//发票识别返回信息
    private String message_distinguish;//发票验真返回信息
    private boolean isOk_recognize;//发票是否识别成功
    private boolean isOk_distinguish;//发票是否验真成功
    public boolean isOk_recognize() {
        return isOk_recognize;
    }
    public void setOk_recognize(boolean ok_recognize) {
        isOk_recognize = ok_recognize;
    }

    public boolean isOk_distinguish() {
        return isOk_distinguish;
    }

    public List<Good> getList_goods() {
        return list_goods;
    }
    public void addGood(Good good) {
        list_goods.add(good);
    }

    public void setOk_distinguish(boolean ok_distinguish) {
        isOk_distinguish = ok_distinguish;
    }

    public String getMessage_distinguish() {
        return message_distinguish;
    }

    public void setMessage_distinguish(String message_distinguish) {
        this.message_distinguish = message_distinguish;
    }
    /**
     * 发票图像识别
     */
    public static final int FUNC_USER_BILL_RECOGNIZE = 100;
    /**
     * 发票验证真伪
     */
    public static final int FUNC_USER_BILL_DISTINGUISH = 101;
    private int func;
    public boolean isReal;

    public Bill(HashMap<String, String> map_keys_chinese) {
        this.map_keys_chinese = map_keys_chinese;
        this.map_values = new LinkedHashMap<>();
        this.list_goods = new ArrayList<>();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setFunc(int func) {
        this.func = func;
    }

    public void recycle() {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    public int getFunc() {
        return func;
    }

    public String getMessage_recognize() {
        return message_recognize;
    }

    public void setMessage_recognize(String message_recognize) {
        this.message_recognize = message_recognize;
    }

    public void putKeyValue(String key, String value) {
        map_values.put(key, value);
    }

    public String getValue(String key) {
        return map_values.get(key);
    }

    public String getJson() {
        return json;
    }
    public void setJson(String json){
        this.json=json;
    }
}
