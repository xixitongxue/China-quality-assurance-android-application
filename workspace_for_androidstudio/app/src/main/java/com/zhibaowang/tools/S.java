package com.zhibaowang.tools;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 打印工具类
 */
public class S {
    /**
     * ip字符串的正则表达式
     */
    private final static String ip_regular = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))" + "\\" + ".){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";
    /**
     * port字符串的正则表达式
     */
    private final static String port_regular = "^([1-9][0-9]{0," + "3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]{1}|6553[0-5])$";

    private static final String tag = "abcd";
    private static final String tag2 = "abcde";
    private static final String tag3 = "abcdef";
    private static boolean debugE = true;
    private static boolean debugI = true;
    private static boolean debugV = true;
    private static boolean debugD = true;
    public static boolean debug_input=true;


    public static void s(Object text, boolean flag) {
        if (flag) {
            s(text);
        }
    }

    public static void s(String tag, Object o) {
        if (S.debugI) {
            tag = S.tag + tag + "[" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(System.currentTimeMillis())) + "]";
            if (o != null) {
                Log.i(tag, o.toString());
            } else {
                Log.i(tag, "null");
            }
        }
    }

    public static void s(Object text) {
        if (S.debugI) {
            if (text != null) {
                Log.i(tag, text.toString());
            } else {
                Log.i(tag, "null");
            }
        }
    }

    public static void ss(Object text) {
        if (S.debugI) {
            if (text != null) {
                Log.i(tag2, text.toString());
            } else {
                Log.i(tag2, "null");
            }
        }
    }

    public static void sss(Object text) {
        if (S.debugI) {
            if (text != null) {
                Log.i(tag3, text.toString());
            } else {
                Log.i(tag3, "null");
            }
        }
    }

    public static void v(Object text) {
        if (S.debugV) {
            if (text != null) {
                Log.i(tag, text.toString());
            } else {
                Log.i(tag, "null");
            }
        }
    }

    public static void d(String text) {
        if (S.debugD) {
            if (text != null) {
                Log.d(tag, text.toString());
            } else {
                Log.d(tag, "null");
            }
        }
    }

    public static void e(Object text) {
        if (S.debugE) {
            if (text != null) {
                Log.e(tag, text.toString());
            } else {
                Log.e(tag, "null");
            }
        }
    }

    public static void e(Exception e) {

        if (S.debugE && e != null) {
            e.printStackTrace();
            Log.e(tag, e.toString());
        } else {
            Log.e(tag, "null");
        }
    }

    /**
     * 判断一个字符串是否是合法的ip地址
     *
     * @param ip
     * @return
     */
    public static boolean isIp(String ip) {
        if (Pattern.compile(ip_regular).matcher(ip + "").matches()) {
            return true;
        } else {
            // e("错误的ip格式");
            return false;
        }
    }

    /**
     * 判断一个数字是否是合法的端口号
     *
     * @param port 输入的端口号
     * @return 是否是合法的端口号
     */
    public static boolean isPort(int port) {
        if (Pattern.compile(port_regular).matcher(port + "").matches()) {
            return true;
        } else {
            // e("错误的port");
            return false;
        }
    }

    public static String format_sec(long num) {
        return format_msec(num * 1000);
    }

    /**
     * 将一个long毫秒数转化为一个持续时间的字符串,例如1天12月6天4小时23分
     *
     * @param num 毫秒数
     * @return 返回字符串结果
     */
    public static String format_msec(long num) {
        String date = "";
        Calendar c2 = Calendar.getInstance();

        c2.setTimeInMillis(num);
        int year = c2.get(Calendar.YEAR);
        if ((year - 1970) != 0) {
            date += ((year - 1970) + "年");
        }
        int month = c2.get(Calendar.MONTH);
        if (month != 0) {
            date += (month + "月");
        }
        int day = c2.get(Calendar.DATE) - 1;
        if (day != 0) {
            date += (day + "天");
        }
        int hour = c2.get(Calendar.HOUR_OF_DAY);
        if (year == 1970 && month == 0 && day == 0) {
            hour -= 8;
        }
        if (hour != 0) {
            date += (hour + "小时");
        }
        int minute = c2.get(Calendar.MINUTE);
        if (minute != 0) {
            date += (minute + "分");
        }
        int second = c2.get(Calendar.SECOND);
        if (second != 0) {
            date += (second + "秒");
        }
        return date;
    }

    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static int[] concat(int[] first, int[] second) {
        return concat(first, second, second.length);
    }

    public static int[] concat(int[] first, int[] second, int length_second) {
        int[] result = Arrays.copyOf(first, first.length + length_second);
        System.arraycopy(second, 0, result, first.length, length_second);
        return result;
    }

    public static byte[] concat(byte[] first, byte[] second) {
        return concat(first, second, second.length);
    }

    public static byte[] concat(byte[] first, byte[] second, int length_second) {
        byte[] result = Arrays.copyOf(first, first.length + length_second);
        System.arraycopy(second, 0, result, first.length, length_second);
        return result;
    }

    public static <T> void cpArr(T[] src, int srcPos, T[] dest, int destPos, int length) {
        try {
            System.arraycopy(src, srcPos, dest, destPos, length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] copyOf(byte[] arr, int i) {
        try {
            return Arrays.copyOf(arr, i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] copyOfRange(byte[] arr, int start, int end) {
        try {
            return Arrays.copyOfRange(arr, start, end);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String addTab(String name, int num) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < name.length(); ) {
            if (i + num < name.length()) {
                sb.append(name.substring(i, i + num) + "\n");
                i += num;
            } else {
                sb.append(name.substring(i, name.length()));
                break;
            }
        }
        return sb.toString();
    }

    public static String a2s(byte[] arr_head) {
        return Arrays.toString(arr_head);
    }

    public static void cpArr(int[] src, int srcPos, int[] dest, int destPos, int length) {
        try {
            System.arraycopy(src, srcPos, dest, destPos, length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cpArr(byte[] src, int srcPos, byte[] dest, int destPos, int length) {
        try {
            System.arraycopy(src, srcPos, dest, destPos, length);
        } catch (Exception e) {
//            S.e("Arr copy err:ArryaIndexOutOfBoudsException");
//			e.printStackTrace();
        }
    }

    public static void e(String abcdef, Object e) {
        e(e);
    }
}
