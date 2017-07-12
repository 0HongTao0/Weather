package com.hongtao.weather.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * author：Administrator on 2017/7/12/012 10:21
 * email：935245421@qq.com
 */
public class ParseUtil {
    /**
     *  解析 json 格式的数据
     * @param jsonData
     * @return jsonArray
     */
    public static JSONArray parseJSONForJSONArray(String jsonData) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
    public static JSONObject parseJSONForJSONObject(String jsonData) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}