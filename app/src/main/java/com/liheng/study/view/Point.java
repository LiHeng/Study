package com.liheng.study.view;

/**
 * Created by dell on 2016/3/2.
 */
public class Point {
    public static final int STATE_NOMAL=0;
    public static final int STATE_PRESS=1;
    public static final int STATE_ERROR=2;

    float x;
    float y;
    int state=STATE_NOMAL;

    public Point(float x,float y){
        this.x=x;
        this.y=y;
    }

    public float distance(Point a){
        float distance=(float)Math.sqrt((x-a.x)*(x-a.x)+(y-a.y)*(y-a.y));
        return distance;
    }

}
