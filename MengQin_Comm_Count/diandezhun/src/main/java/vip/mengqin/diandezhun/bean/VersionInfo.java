package vip.mengqin.diandezhun.bean;

import java.io.Serializable;

public class VersionInfo implements Serializable {
    public String mark;//": "更新info",    更新说明
    public String downloadUrl;//": "http://www.baidu.com",    下载地址
    public String version;//":      "1.0.1"     最新版本
    public int isConstraint;      //是否强制更新    1 :强制   0 非强制
}
