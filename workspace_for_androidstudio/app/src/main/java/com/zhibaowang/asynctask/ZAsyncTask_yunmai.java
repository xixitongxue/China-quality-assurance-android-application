package com.zhibaowang.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.zhibaowang.model.Bill;
import com.zhibaowang.tools.B;
import com.zhibaowang.tools.S;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * Created by zhaoyuntao on 2017/11/30.
 */

public class ZAsyncTask_yunmai extends AsyncTask<Bill, Void, Bill> {

    // 获取版本号
    private static String OSVersion = android.os.Build.VERSION.RELEASE;
    private static String mxh = android.os.Build.MODEL;// 获取手机型号
    // 访问服务器地址
    public static String ENGINE_URL = "http://www.yunmaiocr.com/SrvXMLAPI";
    public static final String USERNAME2 = "84653ec2-5ab9-4952-8144-d6429a6297f5";// 开发者平台API帐号
    public static final String USERNAME = "36568850-f9bd-4e67-bd6f-f3b73a05592b";// 开发者平台API帐号
    public static final String PASSWORD2 = "oWWklddDkkDHoLxNIxkyGASxLAWCgf";// 开发者平台API密码
    public static final String PASSWORD = "DLjiFOtanLkbqZCZINpWKqNnwshdPA";// 开发者平台API密码
    public static final String connFail = "确认网络连接是否正常！";

    private LinkedHashMap<String, String> map_key;

    {
        map_key = new LinkedHashMap<>();
        map_key.put("Code1", "发票代码");//String,增值税发票,
        map_key.put("Code2", "发票号码");//String,ok,错误描述
        map_key.put("Date", "开票日期");//int,0,错误码
        map_key.put("CheckCode", "校验码");//int,0,旋转角度
        map_key.put("BuyerName", "购买方名称");//String,备注
        map_key.put("BuyerTaxN0", "购买方纳税人识别号");//String,发票代码
        map_key.put("BuyerAddress", "购买方地址、电话");//String,发票号码
        map_key.put("BuyerBank", "购买方开户行及账号");//String,税额明细
        map_key.put("Sum", "合计(金额)");//String,税率
        map_key.put("SumTax", "合计(税额)");//String,税额合计
        map_key.put("TotalCapital1", "价税合计(大写)");//String,销售方纳税人识别号
        map_key.put("TotalCapital2", "价税合计(小写)");//String,货物或服务名称
        map_key.put("PurchaserName", "销售方名称");//String,开票日期
        map_key.put("PurchaserTaxN0", "销售方纳税人识别号");//String,购买方名称
        map_key.put("PurchaserAddress", "销售方地址、电话");//String,金额明细
        map_key.put("PurchaserBank", "销售方开户行及账号");//String,销售方名称
        map_key.put("Receiver", "收款人");//String,代开(非代开条目为空)
        map_key.put("Checker", "复核人");//String,校验码,
        map_key.put("Drawer", "开票人");//String,纳税人识别号
    }

    public LinkedHashMap<String, String> getMap_key_bill() {
        return map_key;
    }

    public static final String action = "vatInvoice.scan";
    private CallBack callBack;

    public ZAsyncTask_yunmai(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Bill doInBackground(Bill... bills) {

        //云脉
        Bill bill = bills[0];
        String xml = getSendXML(action, "jpg");//获取要发送的请求XML
        byte[] bytes = B.bitmapToBytes(bill.getBitmap());//获取图片大小
        if (!(bytes.length > (1000 * 1024 * 5))) {//判断图片大小是否超容
            String json = send(xml, bytes);//请求返回一个json识别结果
            S.s("云脉识别结果:" + json);
            parseJson(bill, json);//解析json并且赋值给发票对象bill
        } else {
            bill.setMessage_recognize("图片过大");//提示图片过大,无法进行识别
        }
        S.s("abcd_bitmap", bytes.length + " 折合" + bytes.length / 1024f / 1024f + "M");
        return bill;
    }

    @Override
    protected void onPostExecute(Bill bill) {
        if (callBack != null) {
            callBack.onPostExecute(bill);
        }
    }

    public interface CallBack {
        void onPostExecute(Bill bill);
    }

    private void parseJson(Bill bill, String json) {
        JSONObject jsonObject;
        JSONObject jsonObject_bill;
        try {
            jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            if (status != null) {
                if (status.equals("OK")) {
                    bill.setMessage_recognize("发票识别成功:" + status);
                    bill.setOk_recognize(true);
                    jsonObject_bill = jsonObject.getJSONObject("data");
                    if (jsonObject_bill != null) {
                        for (String key : map_key.keySet()) {
                            try {
                                String value = jsonObject_bill.getString(key);
                                bill.putKeyValue(key, value);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    bill.setMessage_recognize("发票识别失败:" + status);
                }
            } else {
                bill.setMessage_recognize("发票识别失败:拿不到status字段!");
            }
            bill.isReal = false;
        } catch (JSONException e) {
            e.printStackTrace();
            S.e(e);
            bill.setMessage_recognize("发票识别失败:" + e.getMessage());
        }
        S.s("发票识别结果:" + bill.getMessage_recognize());
    }

    /**
     * 获得发送请求的xml
     *
     * @param action
     * @param ext    图片格式
     * @return
     */
    public static String getSendXML(String action, String ext) {
        ArrayList<String[]> arr = new ArrayList<String[]>();
        String key = UUID.randomUUID().toString();
        String time = String.valueOf(System.currentTimeMillis());
        String verify = MD5(action + USERNAME + key + time + PASSWORD);
        arr.add(new String[]{"action", action});
        arr.add(new String[]{"client", USERNAME});
        arr.add(new String[]{"system", OSVersion + mxh});
        arr.add(new String[]{"password", MD5(PASSWORD)});
        arr.add(new String[]{"key", key});
        arr.add(new String[]{"time", time});
        arr.add(new String[]{"verify", verify});
        arr.add(new String[]{"ext", ext});
        arr.add(new String[]{"json", "1"});
        return getXML(arr, false);
    }


    //	<apps>
//  <app>
//	<name>Google Maps</name>
//      <version>1.0</version>
//  </app>
//  <app>
//      <name>Chrome</name>
//      <version>2.1</version>
//  </app>
//</apps>
    public void parseXMLWithPull(String xmlData) throws Exception {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(xmlData));
        int eventType = parser.getEventType();
        String name = "";
        String version = "";
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String nodeName = parser.getName();
            switch (eventType) {
                // 开始解析某个结点
                case XmlPullParser.START_TAG: {
                    if ("name".equals(nodeName)) {
                        name = parser.nextText();
                    } else if ("version".equals(nodeName)) {
                        version = parser.nextText();
                    }
                    break;
                }
                // 完成解析某个结点
                case XmlPullParser.END_TAG: {
                    if ("app".equals(nodeName)) {
                        S.s("name :" + name);
                        S.s("version :" + version);
                    }
                    break;
                }
                default:
                    break;
            }
            eventType = parser.next();
        }
    }

    public static String getXML(ArrayList<String[]> arr, boolean IsUpper) {
        if (arr == null || arr.size() == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        String tag = "";
        for (int idx = 0; idx < arr.size(); idx++) {
            tag = arr.get(idx)[0];
            if (IsUpper) {
                tag = tag.toUpperCase();
            }
            sb.append("<");
            sb.append(tag);
            sb.append(">");
            sb.append(arr.get(idx)[1]);
            // sb.append(XMLFunctions.code(arr.get(idx)[1]));
            sb.append("</");
            sb.append(tag);
            sb.append(">");
        }
        return sb.toString();
    }

    @SuppressWarnings("unused")
    public static String send(String xml, byte[] file) {
        byte[] dest = new byte[xml.getBytes().length + file.length + "<file></file>".getBytes()
                .length];
        int pos = 0;
        System.arraycopy(xml.getBytes(), 0, dest, pos, xml.getBytes().length);
        pos += xml.getBytes().length;
        System.arraycopy("<file>".getBytes(), 0, dest, pos, "<file>".getBytes().length);
        pos += "<file>".getBytes().length;
        System.arraycopy(file, 0, dest, pos, file.length);
        pos += file.length;
        System.arraycopy("</file>".getBytes(), 0, dest, pos, "</file>".getBytes().length);
        String requestString = new String(dest);
        try {
            return httpClient(ENGINE_URL, dest);
        } catch (IOException e) {
            return "-1";
        }
    }

    @SuppressWarnings("finally")
    public static String httpClient(String url, byte[] content) throws IOException {
        HttpClient httpClient = new HttpClient();
        HttpClientParams httparams = new HttpClientParams();
        httpClient.setParams(httparams);
        httpClient.setConnectionTimeout(30 * 1000);
        httpClient.setTimeout(30 * 1000);

        PostMethod method = new PostMethod(url);
        RequestEntity requestEntity = new ByteArrayRequestEntity(content);
        method.setRequestEntity(requestEntity);
        String responseBody = null;
        try {
            method.getParams().setContentCharset("utf-8");
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new
                    DefaultHttpMethodRetryHandler());
            int statusCode = httpClient.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                System.out.println("\r\nMethod failed: " + method.getStatusLine() + ",url:\r\n" +
                        url + "\r\n");
            }
            StringBuffer resultBuffer = new StringBuffer();
            BufferedReader in = new BufferedReader(new InputStreamReader(method
                    .getResponseBodyAsStream(), method.getResponseCharSet()));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                resultBuffer.append(inputLine);
                resultBuffer.append("\r\n");
            }
            in.close();
            responseBody = resultBuffer.toString().trim();
        } catch (ConnectTimeoutException ex) {
            responseBody = connFail;
        } catch (SocketTimeoutException e) {
            responseBody = connFail;
        } catch (UnknownHostException e) {
            responseBody = connFail;
        } catch (Exception e) {
            Log.d("tag", "-->" + e);
            e.printStackTrace();
            responseBody = "-2";
        } finally {
            if (method != null) {
                method.releaseConnection();
                method = null;
            }
            return responseBody;
        }

    }

    public static byte[] Inputstream2byte(InputStream is) {
        if (is == null) {
            return null;
        }
        return WriteFromStream(is);
    }

    public final static String MD5(String pwd) {
        // 用于加密的字符
        char md5String[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
                'E', 'F'};
        try {
            byte[] btInput = pwd.getBytes();

            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) { // i = 0
                byte byte0 = md[i]; // 95
                str[k++] = md5String[byte0 >>> 4 & 0xf]; // 5
                str[k++] = md5String[byte0 & 0xf]; // F
            }
            return new String(str);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将inoutsreame写入到ByteArrayOutputStream
     *
     * @param is 输入流
     * @return
     * @throws IOException
     */
    public static byte[] WriteFromStream(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        try {
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            is.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }


}
