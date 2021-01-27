package com.example.sdapp;


import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class HttpGetContext {

    /**
     * @param url
     * @param jsonParam
     * @return
     */
    public String getData(String url, JSONObject jsonParam) {
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = null;//解决中文乱码问题
        try {
            entity = new StringEntity(jsonParam.toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (entity != null) {
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
        }
        httpPost.setEntity(entity);
        HttpClient httpClient = new DefaultHttpClient();
        // 获取HttpResponse实例
        HttpResponse httpResp = null;
        try {
            httpResp = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 判断是够请求成功
        if (httpResp != null) {
            if (httpResp.getStatusLine().getStatusCode() == 200) {
                // 获取返回的数据
                String result = null;
                try {
                    result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
                    result = result.replace("\r\n", "");
//                    Log.e("HttpPost方式请求成功，返回数据如下：", result);
                    return result;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("打印数据", "HttpPost方式请求失败" + httpResp.getStatusLine().getStatusCode());
            }
        }
        return "请求失败";
    }
}