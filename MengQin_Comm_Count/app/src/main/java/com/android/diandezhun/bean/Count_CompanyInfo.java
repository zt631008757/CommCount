package com.android.diandezhun.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class Count_CompanyInfo extends LitePalSupport implements Serializable {
    @Column(unique = true)
    public String company;

    public Count_CompanyInfo(String company) {
        this.company = company;
    }
}
