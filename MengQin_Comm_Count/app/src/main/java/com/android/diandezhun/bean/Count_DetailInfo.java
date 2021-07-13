package com.android.diandezhun.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * 数数量详细信息
 */
public class Count_DetailInfo extends LitePalSupport implements Serializable {

    @Column(unique = true, defaultValue = "unknown")
    public int id;
    //图像相关信息
    public double scale;    //图片缩放比例
    public int radius;      //显示直径
    public int mostRadius;  //精度
    public List<ImageRec_CircleInfo> circles; //圆点信息
    public ImageRec_FrameInfo frame;
    public String filePath; //文件保存路径
    public double radius_scale = 1;  //圆半径缩放率
    public int radius_min = 0;  //所有圆点 最小值
    public int radius_max = 0;  //所有圆点 最大值

    public String countType;
    public String countType_Gson;

    //其他信息
    public double length;  //单根长度
    public int count;   //数量
    public int inAndOutType; //出入库   0：出库  1：入库
    public String company;  //公司名
    public String remark;   //备注
    public long editTime = System.currentTimeMillis();   //最后修改时间


    //其他临时信息
//    public boolean isSelect; //历史记录列表选中
    public int isSelect; //历史记录列表选中   0 未选中   1选中

}
