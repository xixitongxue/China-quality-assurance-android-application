package com.zhibaowang.asynctask;

import android.os.AsyncTask;

import com.zhibaowang.model.User;
import com.zhibaowang.tools.S;
import com.zhibaowang.tools.ZHttpTools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhaoyuntao on 2017/12/7.
 */

public class ZAsyncTask_getUserInfo extends AsyncTask<Object, Void, Boolean> {

    private final String url_getUserInfo = "http://192.168.1.12/user/getUserInfo";
    public static final int SUCCESS=1;
    public static final int FAILED=-1;
    private CallBack callBack;

    public ZAsyncTask_getUserInfo(CallBack callBack) {
        this.callBack = callBack;
    }
    //异步
    @Override
    protected Boolean doInBackground(Object... users) {
        User user=(User) users[0];
        boolean isSuccess=false;
        String msg=null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", user.getUserId());
            jsonObject.put("timestamp", System.currentTimeMillis());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json_req = jsonObject.toString();
        ZHttpTools.Response response = ZHttpTools.request(url_getUserInfo, json_req);
        if (response != null && response.bytes != null) {
            String json = new String(response.bytes);
            S.s("getuserinfo服务器返回:" + json);
            if (response.code == 200) {
                try {
                    JSONObject jsonObject_result = new JSONObject(json);
                    try {
                        JSONObject jsonObject1_data=jsonObject_result.getJSONObject("data");
                        String username=jsonObject1_data.getString("username");
                        String name=jsonObject1_data.getString("name");
                    }catch (JSONException e){
                        S.e(e);
                    }
                    try {
                        msg=jsonObject_result.getString("msg");
                    } catch (JSONException e) {
                        S.e(e);
                    }
                    int result=FAILED;
                    try {
                        result=jsonObject_result.getInt("result");
                    } catch (JSONException e) {
                        S.e(e);
                    }
                    long timestamp=0;
                    try {
                        timestamp=jsonObject_result.getLong("timestamp");
                    } catch (JSONException e) {
                        S.e(e);
                    }
                    isSuccess=(result==SUCCESS);

                }catch (JSONException e){
                    e.printStackTrace();
                    S.e(e);
                }
            } else {
            }
        }
        callBack.whenGetResult(isSuccess,msg);
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
        void whenGetResult( boolean isSuccess, String msg);

        void onCancelled();
    }
}
