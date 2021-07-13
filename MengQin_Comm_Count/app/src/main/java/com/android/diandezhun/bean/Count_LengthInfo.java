package com.android.diandezhun.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class Count_LengthInfo extends LitePalSupport implements Serializable {
    @Column(unique = true)
    public String length;

    public Count_LengthInfo(String length) {
        this.length = length;
    }
}
