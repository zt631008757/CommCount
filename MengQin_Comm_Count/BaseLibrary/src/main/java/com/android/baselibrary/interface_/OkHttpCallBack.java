package com.android.baselibrary.interface_;

import com.android.baselibrary.responce.BaseResponce;

/**
 * Created by zt on 2018/6/13.
 */

public interface OkHttpCallBack<T> {
    void onSuccess(T baseResponce);
    void onFailure(T baseResponce);
}
