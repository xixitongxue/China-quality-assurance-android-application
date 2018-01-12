package com.zhibaowang.app;

import android.app.Application;
import android.graphics.Color;


import com.mob.MobApplication;
import com.mob.MobSDK;
import com.zhibaowang.model.User;

import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by zhaoyuntao on 2017/11/6.
 */

public class HongboheshiApplication extends MobApplication {
    private static User user;
    public static int color= Color.parseColor("#2bb161");
    @Override
    public void onCreate() {
        super.onCreate();

        // 通过代码注册你的AppKey和AppSecret
        MobSDK.init(this, "22fe42d165fdd", "4c6130ad575cc3e5c157e91297f31f82");

        HashMap<String, Object> hashMap_sinaWeibo = new HashMap<String, Object>();
        hashMap_sinaWeibo.put("Id", "1");
        hashMap_sinaWeibo.put("SortId", "1");
        hashMap_sinaWeibo.put("AppKey", "568898243");
        hashMap_sinaWeibo.put("AppSecret", "38a4f8204cc784f81f9f0daaf31e02e3");
        hashMap_sinaWeibo.put("RedirectUrl", "http://www.sharesdk.cn");
        hashMap_sinaWeibo.put("ShareByAppClient", "false");
        hashMap_sinaWeibo.put("Enable", "true");
        ShareSDK.setPlatformDevInfo(SinaWeibo.NAME, hashMap_sinaWeibo);

        HashMap<String, Object> hashMap_qq = new HashMap<String, Object>();
        hashMap_qq.put("Id", "7");
        hashMap_qq.put("SortId", "7");
        hashMap_qq.put("AppId", "1106501057");
        hashMap_qq.put("AppKey", "tE5PeB4bjGYLWFZI");
        hashMap_qq.put("BypassApproval", "true");
        hashMap_qq.put("ShareByAppClient", "true");
        hashMap_qq.put("Enable", "true");
        ShareSDK.setPlatformDevInfo(QQ.NAME, hashMap_qq);


        HashMap<String, Object> hashMap_wechat = new HashMap<String, Object>();
        hashMap_wechat.put("Id", "4");
        hashMap_wechat.put("SortId", "4");
        hashMap_wechat.put("AppId", "wx6737790d2d7b925d");
        hashMap_wechat.put("AppSecret", "1f5333e417b6ce37759f13592f2fe28f");
        hashMap_wechat.put("BypassApproval", "false");
        hashMap_wechat.put("Enable", "true");
        ShareSDK.setPlatformDevInfo(Wechat.NAME, hashMap_wechat);

        HashMap<String, Object> hashMap_weichatMoments = new HashMap<String, Object>();
        hashMap_weichatMoments.put("Id", "5");
        hashMap_weichatMoments.put("SortId", "5");
        hashMap_weichatMoments.put("AppId", "wx6737790d2d7b925d");
        hashMap_weichatMoments.put("AppSecret", "1f5333e417b6ce37759f13592f2fe28f");
        hashMap_weichatMoments.put("BypassApproval", "false");
        hashMap_weichatMoments.put("Enable", "true");
        ShareSDK.setPlatformDevInfo(WechatMoments.NAME, hashMap_weichatMoments);

        HashMap<String, Object> hashMap_wechatFavorite = new HashMap<String, Object>();
        hashMap_wechatFavorite.put("Id", "6");
        hashMap_wechatFavorite.put("SortId", "6");
        hashMap_wechatFavorite.put("AppId", "wx6737790d2d7b925d");
        hashMap_wechatFavorite.put("AppSecret", "1f5333e417b6ce37759f13592f2fe28f");
        hashMap_wechatFavorite.put("Enable", "true");
        ShareSDK.setPlatformDevInfo(WechatFavorite.NAME, hashMap_wechatFavorite);

    }

    public static User getUser() {
        synchronized (Class.class) {
            if (user == null) {
                synchronized (Class.class) {
                    user = new User();
                }
            }
        }
        return user;
    }

}
