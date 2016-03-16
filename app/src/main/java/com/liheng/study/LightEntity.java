package com.liheng.study;

/**
 * Created by dell on 2016/3/5.
 */
public class LightEntity {
    private float light;
    private String timestamp;

    public LightEntity(float light,String timestamp){
        this.light=light;
        this.timestamp=timestamp;
    }

    public float getLight() {
        return light;
    }

    public void setLight(float light) {
        this.light = light;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
