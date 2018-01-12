package com.zhibaowang.asynctask;

import android.os.AsyncTask;

import com.zhibaowang.tools.S;
import com.zhibaowang.tools.ZHttpTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by zhaoyuntao on 2017/12/7.
 */

public class ZAsyncTask_loginByThirdParty extends AsyncTask<String, Void, Void> {

    private final String url = "http://192.168.1.12/user/register/sendget";
    private CallBack callBack;

    public ZAsyncTask_loginByThirdParty(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected Void doInBackground(String[] objects) {
        String loginname = objects[0];
        thirdlogin(loginname);
        return null;
    }

    @Override
    protected void onPostExecute(Void o) {

    }

    @Override
    protected void onCancelled() {

    }

    private void thirdlogin(final String name) {
        Platform platform = ShareSDK.getPlatform(name);
        if(!platform.isClientValid()) {
            S.s("未安装微信");
        }
        S.s("获取登录平台信息["+name+"]:"+platform);
        //回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
        platform.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                S.s("======================= onError =======================");
                S.e("login Error:" + arg2);
                S.s(arg0 + "登录授权出错");
                callBack.onCancelled("登录授权出错");
                arg2.printStackTrace();
            }

            @Override
            public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
                S.s("======================= onComplete =======================");
                S.s(platform + " 登录授权完毕");
                //输出所有授权信息
                S.s(platform.getDb().exportData());
                Iterator ite = res.entrySet().iterator();
                S.s("------------------------------------------------------------");
                while (ite.hasNext()) {
                    Map.Entry entry = (Map.Entry) ite.next();
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    S.s("Key-Value:" + key + "： " + value);
                }
                S.s("------------------------------------------------------------");
                String msg = null;
                //用户资源都保存到res
                //通过打印res数据看看有哪些数据是你想要的
                if (action == Platform.ACTION_USER_INFOR) {
                    PlatformDb platDB = platform.getDb();//获取数平台数据DB
                    //通过DB获取各种数据
                    String token = platDB.getToken();
                    String userGender = platDB.getUserGender();
                    String userIcon = platDB.getUserIcon();
                    String userId = platDB.getUserId();
                    String nickName = platDB.getUserName();
                    S.s("UserGender:" + userGender);
                    S.s("Icon:" + userIcon);
                    S.s("Token:" + token);
                    S.s("UserId:" + userId);
                    S.s("UserName:" + nickName);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("token", token);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonObject.put("usergender", userGender);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonObject.put("icon", userIcon);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonObject.put("number_id", userId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonObject.put("nickname", nickName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        String platformInfo=null;
                        if (QQ.NAME.equals(name)) {
                            platformInfo="QQ";
                        } else if (Wechat.NAME.equals(name)) {
                            platformInfo="Wechat";
                        } else if (SinaWeibo.NAME.equals(name)) {
                            platformInfo="SinaWeibo";
                        }
                        jsonObject.put("platform", platformInfo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        S.e(e);
                    }
                    String json_send = jsonObject.toString();
                    S.s("正在向服务器发送第三方登录信息:" + json_send);
                    ZHttpTools.Request request = new ZHttpTools.Request(json_send.getBytes(), ZHttpTools.Request.content_type_json, ZHttpTools.Request.way_post, url);
                    ZHttpTools.Response response = ZHttpTools.request(request);
                    if (response != null) {
                        if (response.code == 200) {

//                            {
//                                "result": 1,
//                                    "msg": "token匹配",
//                                    "data": null,
//                                    "timestamp": null
//                            }

                            if (response.bytes != null) {
                                String result = new String(response.bytes);
                                S.s("第三方登录返回结果:" + result);
                                try {
                                    JSONObject jsonObject1 = new JSONObject(result);
                                    int code_result = jsonObject1.getInt("result");
                                    try {
                                        msg = jsonObject1.getString("msg");
                                    } catch (JSONException e) {
                                        S.e(e);
                                    }
                                    try {
                                        String data = jsonObject1.getString("data");
                                    } catch (JSONException e) {
                                        S.e(e);
                                    }
                                    try {
                                        long timeStamp = jsonObject1.getLong("timestamp");
                                    } catch (JSONException e) {
                                        S.e(e);
                                    }
                                    if (code_result == 1) {
                                        callBack.whenGetResult(token, userId, nickName, userIcon, userGender);
                                        msg = "登录成功";
                                    } else {
                                        msg = "登录失败" + msg;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    S.e(e);
                                    msg = "登录失败,服务器返回信息异常";
                                }
                            } else {
                                msg = "登录失败,服务器返回空";
                            }
                        } else {
                            msg = "登录失败,第三方服务异常";
                            S.e("提交第三方用户信息时发生错误,服务器无法做出正确回应");
                        }
                    } else {
                        msg = "登录失败,无法访问服务器";
                    }
                }
                callBack.onCancelled(msg);
                S.s("------------------------------------------------------------");
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                S.s("======================= onCancel =======================");
                S.s(arg0 + "登录授权被取消");
                callBack.onCancelled("登录授权被取消");
            }
        });
        S.s("开始授权");
        if (platform.isAuthValid()){
            S.s("授权已存在,准备移除");
            platform.removeAccount(true);
        }
        //authorize与showUser单独调用一个即可
//        platform.authorize();//单独授权,OnComplete返回的hashmap是空的
        platform.showUser(null);//授权并获取用户信息
        //移除授权
        //weibo.removeAccount(true);
    }

    @Override
    protected void onCancelled(Void o) {
        callBack.onCancelled("已取消操作");
    }

    public interface CallBack {
        void whenGetResult(String token, String userId, String nickName, String icon, String userGender);

        void onCancelled(String msg);
    }
}
