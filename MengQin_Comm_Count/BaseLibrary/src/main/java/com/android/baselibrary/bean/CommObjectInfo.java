package com.android.baselibrary.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/9/20.
 */

public class CommObjectInfo implements Serializable {
    public String key;
    public Object value;

    public boolean isSelect;

    public CommObjectInfo() {

    }

    public CommObjectInfo(String key, Object value) {
        this.key = key;
        this.value = value;
    }
}
