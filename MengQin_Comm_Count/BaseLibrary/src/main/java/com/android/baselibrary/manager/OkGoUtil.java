package com.android.baselibrary.manager;

import android.content.Context;
import android.text.TextUtils;

import com.android.baselibrary.event.Event_Token_Failure;
import com.android.baselibrary.interface_.OkHttpCallBack;
import com.android.baselibrary.responce.BaseResponce;
import com.android.baselibrary.util.Util;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.HttpMethod;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.BodyRequest;
import com.lzy.okgo.request.base.Request;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;

/**
 * 网络请求具体执行代码，这里可以替换其他框架
 */
public class OkGoUtil {

    /**
     * okHttp请求， 外部调用
     *
     * @param context     上下文，用于取消请求
     * @param methed      请求类型 (HttpMethod.POST/HttpMethod.GET)
     * @param url         请求地址
     * @param requestBody post方式 放入请求体的参数
     * @param callBack    回调对象
     */
    public static void okHttpRequest(Context context, HttpMethod methed, String url, Map<String, Object> urlParam, Map<String, Object> headers, Map<String, Object> requestBody, OkHttpCallBack<?> callBack, boolean isJson) {
        //url参数
        if (urlParam != null && urlParam.size() > 0) {
            if (!url.contains("?")) {
                url += "?";
            }
            String paramStr = "";
            for (String key : urlParam.keySet()) {
                if (urlParam.get(key) != null && !TextUtils.isEmpty(urlParam.get(key).toString())) {
                    paramStr += key + "=" + URLEncoder.encode(urlParam.get(key).toString()) + "&";
                }
            }
            if (paramStr.endsWith("&")) {
                paramStr = paramStr.substring(0, paramStr.length() - 1);
            }
            url += paramStr;
            urlParam = null;
        }
        executeByOkHttp(methed, context, url, requestBody, headers, callBack, isJson);
    }

    public static void uploadByOkhttp(final Context context, final String url, Map<String, Object> headers, Map<String, Object> requestBody, File file, final OkHttpCallBack<?> callBack, boolean isJson) {
        PostRequest postRequest = OkGo.post(url);
        postRequest.isMultipart(true);
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        int time = 60 * 1000;
//        builder.readTimeout(time, TimeUnit.MILLISECONDS);      //全局的读取超时时间
//        builder.writeTimeout(time, TimeUnit.MILLISECONDS);     //全局的写入超时时间
//        builder.connectTimeout(time, TimeUnit.MILLISECONDS);   //全局的连接超时时间
//        HttpsUtils.SSLParams sslParams4 = SslSocketFactoryManager.getSslSocketFactory(context);
//        builder.sslSocketFactory(sslParams4.sSLSocketFactory, sslParams4.trustManager);
//        request.client(builder.build());

        //添加headers
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                String value = headers.get(key) + "";
                postRequest.headers(key, value);
            }
        }
        if (requestBody != null) {
            for (String key : requestBody.keySet()) {
                String value = requestBody.get(key) + "";
                if (!TextUtils.isEmpty(value)) {
                    postRequest.params(key, value);
                }
            }
        }
        postRequest.params("image", file);
        postRequest.execute(new MyAbsCallback1(context, callBack));
    }

    private static void executeByOkHttp(HttpMethod methed, Context context, final String url, Map<String, Object> requestBody, Map<String, Object> headers, final OkHttpCallBack<?> callBack, boolean isJson) {
        try {
            Request request = null;
            if (HttpMethod.GET == methed) {
                request = OkGo.get(url).tag(context);
            } else {
                if (HttpMethod.POST == methed) {
                    request = OkGo.post(url).tag(context);
                } else if (HttpMethod.PUT == methed) {
                    request = OkGo.put(url).tag(context);
                } else if (HttpMethod.DELETE == methed) {
                    request = OkGo.delete(url).tag(context);
                }

                if (isJson) {
                    String json = new Gson().toJson(requestBody);
                    ((BodyRequest) request).upJson(json);
                } else {
                    FormBody.Builder builder = new FormBody.Builder();
                    if (requestBody != null) {
                        for (String key : requestBody.keySet()) {
                            String value = (String) requestBody.get(key);
                            if (!TextUtils.isEmpty(value)) {
                                builder.add(key, value);
                            }
                        }
                    }
                    ((BodyRequest) request).upRequestBody(builder.build());
                }
            }

            //添加headers
            if (headers != null && headers.size() > 0) {
                for (String key : headers.keySet()) {
                    String value = headers.get(key) + "";
                    request.getHeaders().put(key, value);
                }
            }
            request.execute(new MyAbsCallback1(context, callBack));
            headers = null;
            requestBody = null;
        } catch (Exception e) {
            e.printStackTrace();
            if (callBack != null) {
                callBack.onFailure(null);
            }
        }
    }

    static class MyAbsCallback1 extends AbsCallback<Object> {

        Context context;
        Type responseClass;
        OkHttpCallBack callBack;
        int times = 0;

        public MyAbsCallback1(Context context, OkHttpCallBack<?> callBack) {
            this.context = context;
            this.responseClass = Util.getReflectType(callBack);
            this.callBack = callBack;
        }

        @Override
        public void onSuccess(Response<Object> response) {
            if (callBack == null) return;
            Object result = response.body();
            try {
                //UI线程
                if (result == null)  //数据解析失败
                {
                    BaseResponce baseResponce = new BaseResponce();
                    baseResponce.statusCode = -1;
                    baseResponce.message = "";
                    callBack.onFailure(baseResponce);
                } else {           //数据解析成功
                    if (result instanceof BaseResponce) {
                        BaseResponce baseResponce = (BaseResponce) result;
                        if (baseResponce.statusCode == BaseResponce.Status_Success) {
                            callBack.onSuccess(result);
                        } else {
                            callBack.onFailure(result);
                            //登录失效，发送事件
                            if (baseResponce.statusCode == BaseResponce.Status_Token_failure) {
                                EventBus.getDefault().post(new Event_Token_Failure());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                callBack.onFailure(result);
            }
        }

        @Override
        public Object convertResponse(okhttp3.Response response) throws Throwable {
            //子线程
            Object object = null;
            String s = response.body().string();

            if (responseClass != null) {
                try {
                    object = new Gson().fromJson(s, responseClass);
                } catch (Exception e) {
                    object = new Gson().fromJson(s, BaseResponce.class);
                    e.printStackTrace();
                }
            } else {
                object = new BaseResponce();
                ((BaseResponce) object).statusCode = BaseResponce.Status_Success;
            }
            return object;
        }

        @Override
        public void onError(Response<Object> response) {
            super.onError(response);
            try {
                BaseResponce baseResponce = new BaseResponce();
                baseResponce.statusCode = -1;
                baseResponce.message = response.getException().getMessage();
                if (callBack != null) {
                    callBack.onFailure(baseResponce);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
