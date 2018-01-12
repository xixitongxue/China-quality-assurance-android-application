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

public class ZAsyncTask_loginByHTSPServer extends AsyncTask<Object, Void, Boolean> {

    private final String url_request_authcode = "http://192.168.1.12/user/register/querysmst";
    private final String url_send_authcode = "http://192.168.1.12/user/register/register";
    private final String url_login = "http://192.168.1.12/user/register/userbylogin";
    private String url = "http://192.168.1.12";
    public static final int FUNC_USER_REQUEST_AUTHCODE = 100;
    public static final int FUNC_USER_CHECK_AUTHCODE = 101;
    public static final int FUNC_USER_LOGIN = 102;
    public static final int FUNC_SERVER_RETURN_REQUEST_AUTHCODE = 200;
    public static final int FUNC_SERVER_RETURN_CHECK_AUTHCODE = 201;
    public static final int FUNC_SERVER_RETURN_LOGIN = 202;
    public static final int SUCCESS=1;
    public static final int FAILED=-1;
    private CallBack callBack;

    public ZAsyncTask_loginByHTSPServer(CallBack callBack) {
        this.callBack = callBack;
    }
    //异步
    @Override
    protected Boolean doInBackground(Object... users) {
        User user=(User) users[0];
        int func = (Integer) users[1];
        boolean isSuccess=false;
        String msg=null;
        switch (func) {
            case FUNC_USER_REQUEST_AUTHCODE:
                url = url_request_authcode;
                break;
            case FUNC_USER_CHECK_AUTHCODE:
                url = url_send_authcode;
                break;
            case FUNC_USER_LOGIN:
                url = url_login;
                break;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", user.getUsername());
            jsonObject.put("password", user.getPassword());
            jsonObject.put("timestamp", user.getTimestamp());
            jsonObject.put("authcode", user.getAuthcode());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json_req = jsonObject.toString();
        ZHttpTools.Response response = ZHttpTools.request(url, json_req);
        if (response != null && response.bytes != null) {
            String json = new String(response.bytes);
            S.s("服务器返回:" + json);
            if (response.code == 200) {
                try {
                    JSONObject jsonObject_result = new JSONObject(json);
                    try {
                        msg=jsonObject_result.getString("message");
                    } catch (JSONException e) {
                        S.e(e);
                    }
                    int result=FAILED;
                    try {
                        result=jsonObject_result.getInt("result");
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
        callBack.whenGetResult(func ,isSuccess,msg);
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
        void whenGetResult(int func, boolean isSuccess, String msg);

        void onCancelled();
    }
}
