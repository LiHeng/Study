package com.liheng.study.bmob;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by dell on 2016/3/14.
 */
public class Light extends BmobObject{
    private Double light;
    private BmobDate timestamp;

    public Double getLight() {
        return light;
    }

    public void setLight(Double light) {
        this.light = light;
    }

    public BmobDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(BmobDate timestamp) {
        this.timestamp = timestamp;
    }
}
