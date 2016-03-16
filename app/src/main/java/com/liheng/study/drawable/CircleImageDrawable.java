package com.liheng.study.drawable;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by dell on 2016/3/2.
 */
public class CircleImageDrawable extends Drawable{
    private Paint mPaint;
    private Bitmap mBitmap;
    private int mWidth;
    private RectF mRectF;

    public CircleImageDrawable(Bitmap bitmap){
        this.mBitmap=bitmap;
        BitmapShader bitmapShader=new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);
        mRectF=new RectF(0,0,bitmap.getWidth(),bitmap.getHeight());
        mWidth=Math.min(bitmap.getWidth(), bitmap.getHeight());
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(mRectF.left+mWidth/2,mRectF.top+mWidth/2,mWidth/2,mPaint);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        mRectF = new RectF(left, top, right, bottom);
        mWidth = (int)Math.min(mRectF.width(), mRectF.height());
    }

    @Override
    public int getIntrinsicWidth() {
        return mWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mWidth;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
