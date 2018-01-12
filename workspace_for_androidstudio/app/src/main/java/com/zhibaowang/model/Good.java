package com.zhibaowang.model;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by zhaoyuntao on 2017/12/4.
 */

public class Good {

    //==================
    public LinkedHashMap<String, String> map_values;//货物字段
    public HashMap<String, String> map_keys_chinese;//货物key对应的中文

    public Good(HashMap<String, String> map_keys_chinese) {
        this.map_keys_chinese = map_keys_chinese;
        map_values=new LinkedHashMap<>();
    }

    public void putKeyValue(String key_good, String value) {
        this.map_values.put(key_good, value);
    }
}
