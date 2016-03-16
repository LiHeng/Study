package com.liheng.study.view.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * Created by liheng on 2016/3/3.
 */
public class WeekDayView extends View{
    //上横线颜色
    private int mTopLineColor = Color.parseColor("#CCE4F2");
    //下横线颜色
    private int mBottomLineColor = Color.parseColor("#CCE4F2");
    //周一到周五的颜色
    private int mWeekdayColor = Color.parseColor("#1FC2F3");
    //周六、周日的颜色
    private int mWeekendColor = Color.parseColor("#fa4451");

    private int mStrokeWidth=4;
    private int mWeekSize=14;
    private Paint mPaint;
    private DisplayMetrics mDisplayMetrics;
    private String[] weekString = new String[]{"日","一","二","三","四","五","六"};

    public int getWeekSize() {
        return mWeekSize;
    }

    public void setWeekSize(int mWeekSize) {
        this.mWeekSize = mWeekSize;
    }

    public int getTopLineColor() {
        return mTopLineColor;
    }

    public void setTopLineColor(int mTopLineColor) {
        this.mTopLineColor = mTopLineColor;
    }

    public int getBottomLineColor() {
        return mBottomLineColor;
    }

    public void setBottomLineColor(int mBottomLineColor) {
        this.mBottomLineColor = mBottomLineColor;
    }

    public int getWeekdayColor() {
        return mWeekdayColor;
    }

    public void setWeekdayColor(int mWeekdayColor) {
        this.mWeekdayColor = mWeekdayColor;
    }

    public int getWeekendColor() {
        return mWeekendColor;
    }

    public void setWeekendColor(int mWeekendColor) {
        this.mWeekendColor = mWeekendColor;
    }

    public String[] getWeekString() {
        return weekString;
    }

    public void setWeekString(String[] weekString) {
        this.weekString = weekString;
    }

    public WeekDayView(Context context) {
        super(context);
    }

    public WeekDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayMetrics=context.getResources().getDisplayMetrics();
        mPaint=new Paint();
    }

    public WeekDayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width=getWidth();
        int height=getHeight();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mTopLineColor);
        mPaint.setStrokeWidth(mStrokeWidth);
        canvas.drawLine(0, 0, width, 0, mPaint);

        mPaint.setColor(mBottomLineColor);
        canvas.drawLine(0, height, width, height, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(mWeekSize * mDisplayMetrics.scaledDensity);
        int columnWidth = width / 7;

        for (int i=0;i<weekString.length;i++){
            String text=weekString[i];
            int fontWidth=(int)mPaint.measureText(text);
            int startX=columnWidth*i+(columnWidth-fontWidth)/2;
            Log.d("WeekDayView",String.valueOf(mPaint.ascent()));
            Log.d("WeekDayView",String.valueOf(mPaint.descent()));
            int startY=(int)(height/2-(mPaint.ascent()+mPaint.descent())/2);//基准线的y坐标
            if(text.indexOf("日") > -1|| text.indexOf("六") > -1){
                mPaint.setColor(mWeekendColor);
            }else{
                mPaint.setColor(mWeekdayColor);
            }
            canvas.drawText(text,startX,startY,mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if(heightMode == MeasureSpec.AT_MOST){
            heightSize = mDisplayMetrics.densityDpi * 30;
        }
        if(widthMode == MeasureSpec.AT_MOST){
            widthSize = mDisplayMetrics.densityDpi * 300;
        }
        setMeasuredDimension(widthSize, heightSize);
    }
}
