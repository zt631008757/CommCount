package com.android.diandezhun.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class CommCount_Type extends LitePalSupport implements Serializable {

    public int id; //": 1,
    public String title; //": "钢管",
    public String thumb; //": "https://diandezhun.oss-cn-shanghai.aliyuncs.com/images/gangguan.jpg",
    public String type; //": "steel_pipe"

    @Column(defaultValue = "false")
//    public boolean isHide;  //是否隐藏  默认全展示 false
    public int isHide;  //是否隐藏  默认全展示 false   0 不隐藏
    public int sort;
}
