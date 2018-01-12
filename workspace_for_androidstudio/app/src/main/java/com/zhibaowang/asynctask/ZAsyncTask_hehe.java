package com.zhibaowang.asynctask;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.zhibaowang.model.Bill;
import com.zhibaowang.model.Good;
import com.zhibaowang.tools.B;
import com.zhibaowang.tools.S;
import com.zhibaowang.tools.ZHttpTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;

/**
 * Created by zhaoyuntao on 2017/11/30.
 */

public class ZAsyncTask_hehe extends AsyncTask<Bill, Void, Bill> {
    // 增值税发票网络识别 API 返回 HTTP(s) body 错误码说明
    // error_code 值 error_msg 内容
    // 0 ok 正常返回
    // 40001 invalid parameter 参数不对
    // 40002 missing parameter 缺少参数
    // 40003 invalid user or password 帐号或密码不对
    // 40004 missing request body 没有 HTTP body
    // 40005 invalid image format HTTP body 不是图像或者不支持该格式
    // 40006 invalid image size 图像太大或太小
    // 40007 fail to recognize 识别失败
    // 40008 invalid content type 通过HTTP form上传图片时，ContentType 无效
    // 40009 corrupted request body 请求 body 损坏
    // 40010 fail to extract image 提取图像裸数据失败
    // 50001 backend down 后台服务器宕机
    // 50004 timeout 识别超时
    // 90099 unknown 未知错误
    private LinkedHashMap<String, String> map_errorCode_message_recognize;

    {
        map_errorCode_message_recognize = new LinkedHashMap<>();
        map_errorCode_message_recognize.put("0", "正常返回结果");
        map_errorCode_message_recognize.put("40001", "参数错误");
        map_errorCode_message_recognize.put("40002", "缺少参数");
        map_errorCode_message_recognize.put("40003", "帐号或密码不对");
        map_errorCode_message_recognize.put("40004", "没有 HTTP body");
        map_errorCode_message_recognize.put("40005", "HTTP body 不是图像或者不支持该格式");
        map_errorCode_message_recognize.put("40006", "图像太大或太小");
        map_errorCode_message_recognize.put("40007", "识别失败");
        map_errorCode_message_recognize.put("40008", "通过HTTP form上传图片时，ContentType 无效");
        map_errorCode_message_recognize.put("40009", "请求 body 损坏");
        map_errorCode_message_recognize.put("40010", "提取图像裸数据失败");
        map_errorCode_message_recognize.put("50001", "后台服务器宕机");
        map_errorCode_message_recognize.put("50004", "识别超时");
        map_errorCode_message_recognize.put("90099", "未知错误");
    }

    public LinkedHashMap<String, String> getMap_errorCode_message_recognize() {
        return map_errorCode_message_recognize;
    }


    private LinkedHashMap<String, String> map_errorCode_message_distinguish;

    {
        map_errorCode_message_distinguish = new LinkedHashMap<>();
        map_errorCode_message_distinguish.put("0", "处理成功");
        map_errorCode_message_distinguish.put("-1", "校验超时");
        map_errorCode_message_distinguish.put("-2", "查询次数超过限制");
        map_errorCode_message_distinguish.put("-3", "发票代号或发票号码不合法");
        map_errorCode_message_distinguish.put("-4", "发票金额或者校验信息不一致");
    }

    public LinkedHashMap<String, String> getMap_errorCode_message_distinguish() {
        return map_errorCode_message_distinguish;
    }

    private LinkedHashMap<String, String> map_key_bill;

    {
        map_key_bill = new LinkedHashMap<>();
        map_key_bill.put("type", "增值税发票");//String,增值税发票,
        map_key_bill.put("rotate_angle", "旋转角度");//int,0,旋转角度
        map_key_bill.put("vat_invoice_note", "备注");//String,备注
        map_key_bill.put("vat_invoice_daima", "发票代码");//String,发票代码
        map_key_bill.put("vat_invoice_haoma", "发票号码");//String,发票号码
        map_key_bill.put("vat_invoice_tax_list", "税额明细");//String,税额明细
        map_key_bill.put("vat_invoice_tax_rate", "税率");//String,税率
        map_key_bill.put("vat_invoice_tax_total", "税额合计");//String,税额合计
        map_key_bill.put("vat_invoice_seller_id", "纳税人识别号");//String,销售方纳税人识别号
        map_key_bill.put("vat_invoice_goods_list", "货物或服务名称");//String,货物或服务名称
        map_key_bill.put("vat_invoice_issue_date", "开票日期");//String,开票日期
        map_key_bill.put("vat_invoice_payer_name", "购买方名称");//String,购买方名称
        map_key_bill.put("vat_invoice_price_list", "金额明细");//String,金额明细
        map_key_bill.put("vat_invoice_seller_name", "销售方名称");//String,销售方名称
        map_key_bill.put("vat_invoice_dai_kai_flag", "代开");//String,代开(非代开条目为空)
        map_key_bill.put("vat_invoice_correct_code", "校验码");//String,校验码,
        map_key_bill.put("vat_invoice_rate_payer_id", "纳税人识别号");//String,纳税人识别号
        map_key_bill.put("vat_invoice_tax_rate_list", "税率明细");//String,税率明细
        map_key_bill.put("vat_invoice_zhuan_yong_flag", "发票类型");//String,发票类型:专票/普票
        map_key_bill.put("vat_invoice_total", "合计");//String,合计
        map_key_bill.put("vehicle_invoice_total_price", "合计大写");//String,合计大写
        map_key_bill.put("vat_invoice_total_cover_tax", "价税合计大写");//String,价税合计大写
        map_key_bill.put("vat_invoice_total_cover_tax_digits", "税价合计小写");//String,税价合计小写
        //--------------------------------------验真字段------------------------------------------
        map_key_bill.put("date", "发票日期");//String ,发票开票日期_验真返回
        map_key_bill.put("invoice_code", "发票代码");//String,发票代码_验真返回
        map_key_bill.put("invoice_no", "发票号码");//String,发票号码_验真返回
        map_key_bill.put("machine_no", "机器识别码");//String,机器识别码_验真返回
        map_key_bill.put("verify_code", "校验码");//String,校验码_验真返回

        map_key_bill.put("sum", "合计金额");//String,合计金额_验真返回
        map_key_bill.put("tax", "合计税额");//String,合计税额_验真返回
        map_key_bill.put("address_payer", "购买方地址");//String,购买方地址_验真返回
        map_key_bill.put("name_payer", "购买方名称");//String,购买方名称_验真返回
        map_key_bill.put("nsrsbh_payer", "购买方纳税人识别号");//String,购买方纳税人识别号_验真返回
        map_key_bill.put("username_payer", "购买方开户行");//String,购买方开户行_验真返回
        map_key_bill.put("address_seller", "销售方地址");//String,销售方地址_验真返回
        map_key_bill.put("name_seller", "销售方名称");//String,销售方名称_验真返回
        map_key_bill.put("nsrsbh_seller", "销售方纳税人识别号");//String,销售方纳税人识别号_验真返回
        map_key_bill.put("username_seller", "销售方开户行");//String,销售方开户行_验真返回
    }

    public LinkedHashMap<String, String> getMap_key_bill() {
        return map_key_bill;
    }

    private LinkedHashMap<String, String> map_key_good;

    {
        map_key_good = new LinkedHashMap<>();
        map_key_good.put("name", "产品名称");//String,产品名称
        map_key_good.put("quantity", "数量");//int,数量
        map_key_good.put("sum", "金额");//String,金额
        map_key_good.put("tax", "税额");//String,税额
        map_key_good.put("tax_rate", "税率");//String,税率
        map_key_good.put("type", "商品类型");//String,商品类型
        map_key_good.put("unit", "单位");//String,单位
        map_key_good.put("unit_price", "单价");//String,单价
    }

    public LinkedHashMap<String, String> getMap_key_good() {
        return map_key_good;
    }

    private final String username_hehe = "hongbotech";
    private final String password_hehe = "4xOHrug8s8";
    //合合发票识别api
    private final String url_bill_recognize = "https://imgs-sandbox.intsig.net/icr/recognize_vat_invoice";
    //合合发票验真所需json格式
    private final String url_bill_distinguish = "https://imgs-sandbox.intsig.net/icr/verify_vat";

    private CallBack callBack;

    public ZAsyncTask_hehe(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Bill doInBackground(Bill... bills) {

        //合合
        Bill bill = bills[0];
        String url = null;
        switch (bill.getFunc()) {
            case Bill.FUNC_USER_BILL_RECOGNIZE:
                bill.setOk_recognize(false);
                url = url_bill_recognize + "?user=" + username_hehe + "&password=" + password_hehe;
                Bitmap bitmap = bill.getBitmap();
                byte[] bytes = B.bitmapToBytes(bitmap);
                // 增值税发票网络识别图片要求
                // 在服务器端增值税发票识别速度非常快，但如果网络速度慢，上传图像可能需要几秒钟到数分钟。
                // 为了提供良好的用户体验，客户应压缩增值税发票图像的大小，然 后再上传。
                // 推荐 jpg 文件设置为：增值税发票的最短边不低于 1200，图像质量 75 以上，位深 度 24。保证图片质量可以获得更好的识别体验。
                // 24位深,三通道都为8位,即一个像素点占24bit,也就是3字节
                int w_bitmap = bitmap.getWidth();
                int h_bitmap = bitmap.getHeight();
                if (w_bitmap < 1200) {
                    float percnet_w = 1300f / w_bitmap;
                    bitmap = B.getBitmapByPercent(bitmap, percnet_w);
                }
                if (h_bitmap < 1200) {
                    float percnet_h = 1300f / h_bitmap;
                    bitmap = B.getBitmapByPercent(bitmap, percnet_h);
                }
                S.s("原图片宽度:"+w_bitmap+" 高度:"+h_bitmap);
                S.s("转换后图片宽度:"+bitmap.getWidth()+" 高度:"+bitmap.getHeight());
                if (bytes.length < (5 * 1024 * 1024)) {//限制图片大小为5Mb
                    ZHttpTools.Request request = new ZHttpTools.Request(B.bitmapToBytes(bitmap), ZHttpTools.Request.content_type_bit, ZHttpTools.Request.way_post, url);
                    ZHttpTools.Response response = ZHttpTools.request(request);
                    if (response!=null) {

                        S.s("abcd", "response code :" + response.code);
                        // Http(s) Response Code 值 说明
                        // 200 识别成功。对应返回的 http(s) body 里面 error_code 为 0
                        // 403 识别失败。对应返回的 http(s) body 里面 error_code 可包含三 种情况：40003。
                        // 404 找不到请求识别的服务器，请检查请求识别的服务器 URL 是 否正确。
                        // 406 识别失败。对应返回的 http(s) body 里面 error_code 可包含三 种情况： 40004，40005 和 40007。
                        // 500 服务器内部错误，无法正常接收和处理请求。对应返回的 http(s) body 里面 error_code 为 90099 表
                        switch (response.code) {
                            case 200:
                                S.s("识别成功:200");
                                bill.setOk_recognize(true);
                                break;
                            case 403:
                                S.s("识别失败:403");
                                bill.setOk_recognize(false);
                                bill.setMessage_recognize("识别失败");
                                break;
                            case 404:
                                S.s("识别失败:404,找不到请求识别的服务器");
                                bill.setOk_recognize(false);
                                bill.setMessage_recognize("找不到服务器");
                                break;
                            case 406:
                                S.s("识别失败:406,参数可能存在问题");
                                bill.setOk_recognize(false);
                                bill.setMessage_recognize("参数存在问题");
                                break;
                            case 500:
                                S.s("识别失败:500,服务器内部错误，无法正常接收和处理请求");
                                bill.setOk_recognize(false);
                                bill.setMessage_recognize("服务器内部错误");
                                break;
                        }
                        if (response.bytes != null) {
                            try {
                                String json = new String(response.bytes, "UTF-8");
                                S.s("合合识别结果:" + json);
                                if (json != null && !"".equals(json)) {
                                    parseJson_recognize(bill, json);
                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                S.e(e);
                            }
                        }
                    }
                } else {
                    bill.setMessage_recognize("图片过大");//提示图片过大,无法进行识别
                }
                S.s("abcd_bitmap", "图片大小[" + bytes.length + "] 换算:" + bytes.length / 1024f / 1024f + "M");
                break;
            case Bill.FUNC_USER_BILL_DISTINGUISH:
                bill.setOk_distinguish(false);
                url = url_bill_distinguish;
                //     合合发票验真api所需的json格式
//    {         "user":"xxx",       //客户参数，由合合信息授权提供。
//              "password":"xxx",   //API_KEY 参数，由合合信息授权提供。
//              "invoice_code":"1100154320",//发票代码，不能为空；
//              "invoice_no":"21131985",    //发票号码，不能为空;
//              "verify_code":"48472882623345981717",   //机器验证码，如果增值税发票上没有这个信息，可以填空;
//              "date":"20160731",          //开票日期，不能为空；
//              "sum":"150.80"              //总金额（非含税），不能为空；
//    }
                JSONObject jsonObject = new JSONObject();
                String invoice_code = bill.getValue("vat_invoice_daima");
                String invoice_no = bill.getValue("vat_invoice_haoma");
                String verify_code = bill.getValue("vat_invoice_correct_code");
                String date = bill.getValue("vat_invoice_issue_date");
                String sum = bill.getValue("vat_invoice_total");
                try {
                    jsonObject.put("user", username_hehe);
                    jsonObject.put("password", password_hehe);
                    jsonObject.put("invoice_code", invoice_code);
                    jsonObject.put("invoice_no", invoice_no);
                    jsonObject.put("verify_code", verify_code == null ? "" : verify_code);
                    jsonObject.put("date", date.replaceAll("[^\\d]", ""));
                    jsonObject.put("sum", sum.replaceAll("[¥]", ""));
                    String json = jsonObject.toString();
                    S.s("abcd", "准备查询真伪:" + json);
                    try {
                        ZHttpTools.Request request = new ZHttpTools.Request(json.getBytes("UTF-8"), ZHttpTools.Request.content_type_json, ZHttpTools.Request.way_post, url);
                        ZHttpTools.Response response = ZHttpTools.request(request);
                        switch (response.code) {
                            case 200:
                                bill.setOk_distinguish(true);
                                if (response.bytes != null) {
                                    String result = new String(response.bytes, "UTF-8");
                                    S.s("合合验真结果:" + result);
                                    if (json != null && !"".equals(json)) {
                                        parseJson_distinguish(bill, result);
                                    }
                                }
                                break;
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        } return bill;
    }

    @Override
    protected void onPostExecute(Bill bill) {
        if (callBack != null) {
            callBack.onPostExecute(bill);
        }
    }

    private void parseJson_recognize(Bill bill, String json) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            int error_code = jsonObject.getInt("error_code");
            try {
                String error_msg = jsonObject.getString("error_msg");
                bill.setMessage_recognize(error_msg);
            } catch (JSONException e) {
                e.printStackTrace();
                S.e(e);
            }
            if (error_code == 0) {
                for (String key : map_key_bill.keySet()) {
                    try {
                        String value = jsonObject.getString(key);
                        bill.putKeyValue(key, value);
                    } catch (JSONException e) {
                    }
                }
                bill.setJson(json);
            }
            S.s("发票识别返回码: error_code:[" + error_code + "]" + getMap_errorCode_message_recognize().get(String.valueOf(error_code)));
        } catch (JSONException e) {
            e.printStackTrace();
            S.e(e);
            bill.setMessage_recognize("发票识别失败:JSONException [ ZAsyncTask_hehe ]");
        }
        bill.isReal = false;//默认发票为假的,验真以后才是真的
        S.s("发票识别结果:" + bill.getMessage_recognize());
    }

    public void parseJson_distinguish(Bill bill, String json) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            S.e(e);
            return;
        }
        try {
            int error_no = jsonObject.getInt("error_no");
            try {
                String error_msg = jsonObject.getString("error_msg");
                bill.setMessage_distinguish(error_msg);
            } catch (JSONException e) {
                e.printStackTrace();
                S.e(e);
            }
            if (error_no == 0) {//0成功,-1校验超时,-2查询次数超过限制-3发票代号或者发票号码不合法-4发票金额或者校验信息不一致
                try {
                    bill.isReal = jsonObject.getBoolean("tax_valid");
                } catch (JSONException e) {
                    e.printStackTrace();
                    S.e(e);
                }
                for (String key : map_key_bill.keySet()) {
                    try {
                        String value = jsonObject.getString(key);
                        bill.putKeyValue(key, value);
                    } catch (JSONException e) {
                    }
                }
            }
            S.s("发票验真返回码: error_no:[" + error_no + "]" + getMap_errorCode_message_distinguish().get(String.valueOf(error_no)));
        } catch (JSONException e) {
            e.printStackTrace();
            S.e(e);
            S.e("发票验真返回的json解析失败,拿不到error_code字段!");
        }
        try {
            //从data结构里面取数据
            JSONObject jsonObject_data = jsonObject.getJSONObject("data");
            for (String key : map_key_bill.keySet()) {
                try {
                    String value = jsonObject.getString(key);
                    bill.putKeyValue(key, value);
                } catch (JSONException e) {
                }
            }
            JSONObject jsonObject_goods = jsonObject_data.getJSONObject("goods");
            JSONArray jsonArray_detail = jsonObject_goods.getJSONArray("detail");
            for (int i = 0; i < jsonArray_detail.length(); i++) {
                JSONObject jsonObject_good = jsonArray_detail.getJSONObject(i);
                Good good = new Good(getMap_key_good());
                bill.getList_goods().add(good);
                for (String key_good : getMap_key_good().keySet()) {
                    String value = jsonObject_good.getString(key_good);
                    good.putKeyValue(key_good, value);
                }
            }
            final String[] keys_payer_and_seller = {"address", "name", "nsrsbh", "username"};
            JSONObject jsonObject_payer = jsonObject_data.getJSONObject("payer");
            for (int i = 0; i < keys_payer_and_seller.length; i++) {
                try {
                    bill.putKeyValue(keys_payer_and_seller[i] + "_payer", jsonObject_payer.getString(keys_payer_and_seller[i]));
                } catch (JSONException e) {
                    e.printStackTrace();
                    S.e(e);
                }
            }
            JSONObject jsonObject_seller = jsonObject_data.getJSONObject("seller");
            for (int i = 0; i < keys_payer_and_seller.length; i++) {
                try {
                    bill.putKeyValue(keys_payer_and_seller[i] + "_seller", jsonObject_seller.getString(keys_payer_and_seller[i]));
                } catch (JSONException e) {
                    e.printStackTrace();
                    S.e(e);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            S.e(e);
        }
    }

    public interface CallBack {
        void onPostExecute(Bill bill);
    }
}
