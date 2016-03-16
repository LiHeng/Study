package com.liheng.study.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.liheng.study.R;

/**
 * Created by liheng on 2016/3/3.
 */

//首先调用setImageXXX函数,然后调用构造函数
public class CircleImageView extends ImageView{
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;
    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private static final int DEFAULT_FILL_COLOR = Color.TRANSPARENT;
    private static final boolean DEFAULT_BORDER_OVERLAY = false;
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 2;
    protected Bitmap mBitmap;

    protected int mBorderColor = DEFAULT_BORDER_COLOR;
    protected int mBorderWidth = DEFAULT_BORDER_WIDTH;
    protected int mFillColor = DEFAULT_FILL_COLOR;
    protected boolean mBorderOverlay=DEFAULT_BORDER_OVERLAY;
    protected BitmapShader mBitmapShader;
    protected int mBitmapWidth;
    protected int mBitmapHeight;

    protected float mDrawableRadius;
    protected float mBorderRadius;

    protected boolean mReady;
    protected boolean mSetupPending;

    protected final Paint mBitmapPaint = new Paint();
    protected final Paint mBorderPaint = new Paint();
    protected Paint mFillPaint=new Paint();

    protected final RectF mDrawableRect = new RectF();
    protected final RectF mBorderRect = new RectF();
    private final Matrix mShaderMatrix = new Matrix();

    private ColorFilter mColorFilter;

    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != SCALE_TYPE) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (adjustViewBounds) {
            throw new IllegalArgumentException("adjustViewBounds not supported.");
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(@ColorInt int borderColor) {
        if (borderColor == mBorderColor) {
            return;
        }
        mBorderColor = borderColor;
        mBorderPaint.setColor(mBorderColor);
        invalidate();
    }

    public void setFillColorResource(@ColorRes int fillColorRes) {
        setFillColor(getContext().getResources().getColor(fillColorRes));
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        if (borderWidth == mBorderWidth) {
            return;
        }

        mBorderWidth = borderWidth;
        setup();
    }

    public void setBorderColorResource(@ColorRes int borderColorRes) {
        setBorderColor(getContext().getResources().getColor(borderColorRes));
    }

    public int getFillColor() {
        return mFillColor;
    }

    public void setFillColor(@ColorInt int fillColor) {
        if (fillColor == mFillColor) {
            return;
        }

        mFillColor = fillColor;
        mFillPaint.setColor(fillColor);
        invalidate();
    }

    public boolean isBorderOverlay() {
        return mBorderOverlay;
    }

    public void setBorderOverlay(boolean borderOverlay) {
        if (borderOverlay == mBorderOverlay) {
            return;
        }

        mBorderOverlay = borderOverlay;
        setup();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (cf == mColorFilter) {
            return;
        }

        mColorFilter = cf;
        mBitmapPaint.setColorFilter(mColorFilter);
        invalidate();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        mBitmap=getBitmapFromDrawable(getDrawable());
        setup();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        mBitmap=getBitmapFromDrawable(getDrawable());
        setup();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap=getBitmapFromDrawable(drawable);
        setup();
    }

    public CircleImageView(Context context) {
        super(context);
        init();
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array=context.obtainStyledAttributes(attrs,R.styleable.CircleImageView,defStyleAttr,0);
        mBorderWidth=array.getDimensionPixelSize(R.styleable.CircleImageView_border_width, DEFAULT_BORDER_WIDTH);
        mBorderColor = array.getColor(R.styleable.CircleImageView_border_color, DEFAULT_BORDER_COLOR);
        mFillColor = array.getColor(R.styleable.CircleImageView_fill_color,DEFAULT_FILL_COLOR);
        mBorderOverlay = array.getBoolean(R.styleable.CircleImageView_border_overlay,DEFAULT_BORDER_OVERLAY);
        array.recycle();
        init();
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable()==null){
            return;
        }
        if (mFillColor != Color.TRANSPARENT) {
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mDrawableRadius, mFillPaint);
        }
        canvas.drawCircle(getWidth()/2,getHeight()/2,mDrawableRadius,mBitmapPaint);
        if (mBorderWidth!=0){
            canvas.drawCircle(getWidth()/2,getHeight()/2,mBorderRadius,mBorderPaint);
        }

    }

    private void init(){
        super.setScaleType(SCALE_TYPE);
        mReady=true;
        if (mSetupPending){
            setup();
            mSetupPending=false;
        }

    }

    private Bitmap getBitmapFromDrawable(Drawable drawable){
        if (drawable==null){
            return null;
        }
        if (drawable instanceof BitmapDrawable){
            return ((BitmapDrawable)drawable).getBitmap();
        }
        try {
            Bitmap bitmap;
            if (drawable instanceof ColorDrawable){
                bitmap=Bitmap.createBitmap(COLORDRAWABLE_DIMENSION,COLORDRAWABLE_DIMENSION,BITMAP_CONFIG);
            }else {
                bitmap=Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),BITMAP_CONFIG);
            }
            Canvas canvas=new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    protected void setup(){
        if (!mReady){
            mSetupPending=true;
            return;
        }
        if (getWidth() == 0 && getHeight() == 0) {
            return;
        }
        if (mBitmap==null){
            invalidate();
            return;
        }
        // 构建渲染器，用mBitmap来填充绘制区域 ，参数值代表如果图片太小的话 就直接拉伸
        mBitmapShader=new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        // 设置图片画笔反锯齿
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);

        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderPaint.setColor(mBorderColor);

        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setAntiAlias(true);
        mFillPaint.setColor(mFillColor);

        mBitmapHeight=mBitmap.getHeight();
        mBitmapWidth=mBitmap.getWidth();

        // 设置含边界显示区域，取的是CircleImageView的布局实际大小
        mBorderRect.set(0,0,getWidth(),getHeight());
        mBorderRadius=Math.min((mBorderRect.height() - mBorderWidth) / 2.0f,(mBorderRect.width() - mBorderWidth) / 2.0f);

        mDrawableRect.set(mBorderRect);
        if (!mBorderOverlay){
            mDrawableRect.inset(mBorderWidth,mBorderWidth);
        }
        mDrawableRadius=Math.min(mDrawableRect.width()/2.0f,mDrawableRect.height()/2.0f);
        updateShaderMatrix();
        invalidate();
    }

    protected void updateShaderMatrix(){
        float scale;
        float dx=0;
        float dy=0;

        mShaderMatrix.set(null);
        if (mBitmapWidth*mDrawableRect.height()>mBitmapHeight*mDrawableRect.width()){
            scale=mDrawableRect.height()/(float)mBitmapHeight;
            dx=(mDrawableRect.width()-mBitmapWidth*scale)*0.5f;
        }else {
            scale=mDrawableRect.width()/(float)mBitmapWidth;
            dy=(mDrawableRect.height()-mBitmapHeight*scale)*0.5f;
        }
        // shader的变换矩阵，我们这里主要用于放大或者缩小。
        mShaderMatrix.setScale(scale, scale);
        // 平移
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left, (int) (dy + 0.5f) + mDrawableRect.top);
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }
}
