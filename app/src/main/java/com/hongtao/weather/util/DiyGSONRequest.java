package com.hongtao.weather.util;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 自定义发送网络请求返回的数据类型 List<Weather>
 * author：hongtao on 2017/7/18/018 11:31
 * email：935245421@qq.com
 * mobile：18306620711
 */
public class DiyGSONRequest<T> extends Request<T> {
    private final Response.Listener<List<T>> mListener;
    private Class<T> mClass;

    private DiyGSONRequest(int method, String url, Class<T> clazz, Response.Listener<List<T>> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mClass = clazz;
        mListener = listener;
    }

    public DiyGSONRequest(String url, Class<T> clazz, Response.Listener<List<T>> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, clazz, listener, errorListener);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        String jsonData;

        try {
            jsonData = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            List<T> list = new ArrayList<>();
            JsonArray array = new JsonParser().parse(jsonData).getAsJsonArray();
            for (JsonElement element : array) {
                list.add(new Gson().fromJson(element, mClass));
            }
            return (Response<T>) Response.success(list, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T t) {
        mListener.onResponse((List<T>) t);
    }
}
