package com.zhibaowang.tools;

import com.zhibaowang.model.User;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;

/**
 * Created by zhaoyuntao on 2017/11/17.
 */

public class ZHttpTools {
    public static String sCookie;
    public static class Response {
        public int code;
        //        public String result;
        public byte[] bytes;
    }

    public static class Request {
        public static final String content_type_json = "application/json";
        public static final String content_type_jpg = "image/jpeg";
        public static final String content_type_bit = "application/x-jpg";
        private String content_type;
        private byte[] bytes;
        public static final String way_get = "GET";
        public static final String way_post = "POST";
        private String way;
        private String url;

        public Request(byte[] bytes, String content_type, String way, String url) {
            this.bytes = bytes;
            this.content_type = content_type;
            this.way = way;
            this.url = url;
        }

        public String getContent_type() {
            return content_type;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public String getWay() {
            return way;
        }

        public String getUrl() {
            return url;
        }
    }

    /**
     * 从网络获取json数据,(String byte[})
     *
     * @return
     */
    public static Response request(String url, String postString) {
        S.s("abcdef", url);
        S.s("abcdef", "请求命令参数:" + postString);
        byte[] bytes;
        try {
            bytes = postString.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            S.e(e);
            return null;
        }
        return request(new Request(bytes, Request.content_type_json, Request.way_post, url));
    }

    /**
     * 提交http请求并且
     * 从网络获取数据,(String byte[})
     *
     * @return
     */
    public static Response request(Request request) {
        Response response = new Response();
        String string_url = request.getUrl();
        String way = request.getWay();
        String content_type = request.getContent_type();
        byte[] bytes = request.getBytes();
        try {
            URL url = new URL(string_url);
            //打开连接
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            // 设置是否从httpUrlConnection读入，默认情况下是true;
//            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setConnectTimeout(10000);
            httpUrlConnection.setReadTimeout(10000);
            if (content_type != null) {
                httpUrlConnection.setRequestProperty("Content-Type", content_type);
            }
            if (sCookie != null && sCookie.length() > 0) {
                httpUrlConnection.setRequestProperty("Cookie", sCookie);
            }
            S.s("request way:[" + way + "]");
            if (Request.way_post.equals(way)) {
                if (bytes != null) {
                    // Post 请求不能使用缓存
//                    httpUrlConnection.setUseCaches(false);
                    // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true, 默认情况下是false;
                    httpUrlConnection.setDoOutput(true);
                    httpUrlConnection.setDoInput(true);
//                    httpUrlConnection.setRequestProperty("Accept-Charset", "utf-8");
                    // 设定请求的方法为"POST"，默认是GET
                    httpUrlConnection.setRequestMethod("POST");
                    OutputStream os = httpUrlConnection.getOutputStream();
                    os.write(bytes);
                    os.flush();
                    os.close();
                }
            } else {
                httpUrlConnection.setRequestMethod("GET");
            }

            // 设定传送的内容类型是可序列化的java对象
            // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
            // httpUrlConnection.setRequestProperty("Content-type",
            // "application/x-java-serialized-object");
            // 连接，从上述url.openConnection()至此的配置必须要在connect之前完成，
            // httpUrlConnection.connect();
            response.code = httpUrlConnection.getResponseCode();
            S.s("abcdef", "response code:" + response.code);
            sCookie = httpUrlConnection.getHeaderField("Set-Cookie");
            //得到输入流
            InputStream is = httpUrlConnection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while (-1 != (len = is.read(buffer))) {
                baos.write(buffer, 0, len);
                baos.flush();
            }
            response.bytes = baos.toByteArray();
            httpUrlConnection.disconnect();
            return response;
        } catch (SocketException e) {
            e.printStackTrace();
            S.e("abcdef", e);
        } catch (IOException e) {
            e.printStackTrace();
            S.e("abcdef", e);
        }
        return null;
    }


}
