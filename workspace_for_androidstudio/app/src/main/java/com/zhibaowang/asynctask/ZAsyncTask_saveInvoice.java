package com.zhibaowang.asynctask;

import android.os.AsyncTask;

import com.zhibaowang.app.HongboheshiApplication;
import com.zhibaowang.model.User;
import com.zhibaowang.tools.S;
import com.zhibaowang.tools.ZHttpTools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhaoyuntao on 2017/12/7.
 */

public class ZAsyncTask_saveInvoice extends AsyncTask<Object, Void, Boolean> {

    private final String url_saveInvoice = "http://192.168.1.12/invoice/save";
    public static final int SUCCESS = 1;
    public static final int FAILED = -1;
    private CallBack callBack;

    public ZAsyncTask_saveInvoice(CallBack callBack) {
        this.callBack = callBack;
    }

    //异步
    @Override
    protected Boolean doInBackground(Object... users) {
        boolean isSuccess = false;
        String msg = null;
        String json_invoice = (String) users[0];
        if (json_invoice == null) {
            msg = "保存失败,发票信息为空";
            callBack.whenGetResult(isSuccess, msg);
            return isSuccess;
        }
        try {
            JSONObject jsonObject = new JSONObject(json_invoice);
            User user = HongboheshiApplication.getUser();
            String requestCode = user.getRequestCode();
            jsonObject.put("userid", requestCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ZHttpTools.Response response = ZHttpTools.request(url_saveInvoice, json_invoice);
        if (response != null) {
            if (response.bytes != null) {
                String json = new String(response.bytes);
                S.s("服务器返回:" + json);
                if (response.code == 200) {
                    try {
                        JSONObject jsonObject_result = new JSONObject(json);
                        try {
                            msg = jsonObject_result.getString("message");
                        } catch (JSONException e) {
                            S.e(e);
                        }
                        int result = FAILED;
                        try {
                            result = jsonObject_result.getInt("result");
                        } catch (JSONException e) {
                            S.e(e);
                        }
                        isSuccess = (result == SUCCESS);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        S.e(e);
                    }
                } else {
                    msg = "错误代码" + response.code;
                }
            } else {
                msg = "消息体为空";
            }
        } else {
            msg = "无法访问服务器";
        }
        callBack.whenGetResult(isSuccess, msg);
        return isSuccess;
    }

    //ui
    @Override
    protected void onPostExecute(final Boolean success) {

    }


    //异步
    @Override
    protected void onCancelled() {
        callBack.onCancelled();
    }

    public interface CallBack {
        void whenGetResult(boolean isSuccess, String msg);

        void onCancelled();
    }
}
