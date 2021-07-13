package com.android.baselibrary.manager;

import android.content.Context;
import android.text.TextUtils;

import com.android.baselibrary.interface_.OkHttpCallBack;
import com.android.baselibrary.tool.Log;
import com.lzy.okgo.model.HttpMethod;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.request.PostRequest;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class EasyHttpUtil {

    public static void okHttpRequest(Context context, HttpMethod methed, String url, Map<String, Object> urlParam, Map<String, Object> headers, Map<String, Object> requestBody, OkHttpCallBack<?> callBack, boolean isJson) {
        Map<String, String> param = new HashMap<>();
        for (String key : requestBody.keySet()) {
            String value = requestBody.get(key) + "";
            if (!TextUtils.isEmpty(value)) {
                param.put(key, value);
            }
        }

        PostRequest request = EasyHttp.post(url).params(param);
        //添加headers
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                String value = headers.get(key)+"";
                request.headers(key, value);
            }
        }
        request.execute(new SimpleCallBack<String>() {
            @Override
            public void onError(ApiException e) {

            }

            @Override
            public void onSuccess(String s) {
                Log.i("onSuccess:" + s);
            }
        });
    }

}
