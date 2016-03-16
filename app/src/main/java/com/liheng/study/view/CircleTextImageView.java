package com.liheng.study.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.liheng.study.R;

/**
 * Created by liheng on 2016/3/3.
 */
public class CircleTextImageView extends CircleImageView{
    private static final int DEFAULT_TEXT_COLOR = Color.BLACK;
    private static final int DEFAULT_TEXT_SIZE = 22;
    private static final int DEFAULT_TEXT_PADDING = 4;

    private final Paint mTextPaint = new Paint();

    private String mTextString;
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mTextSize = DEFAULT_TEXT_SIZE;
    private int mTextPadding=DEFAULT_TEXT_PADDING;

    public String getTextString() {
        return mTextString;
    }

    public void setTextString(String mTextString) {
        this.mTextString = mTextString;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
    }

    public int getTextPadding() {
        return mTextPadding;
    }

    public void setTextPadding(int mTextPadding) {
        this.mTextPadding = mTextPadding;
    }

    public CircleTextImageView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMeasureSpecMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthMeasureSpecSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightMeasureSpecMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightMeasureSpecSize=MeasureSpec.getSize(heightMeasureSpec);
        if (!TextUtils.isEmpty(mTextString)){
            int textMeasuredSize=(int)(mTextPaint.measureText(mTextString));
            textMeasuredSize+=2*mTextPadding;
            if(widthMeasureSpecMode==MeasureSpec.AT_MOST&&heightMeasureSpecMode==MeasureSpec.AT_MOST)
            {
                if(textMeasuredSize>getMeasuredWidth()||textMeasuredSize>getMeasuredHeight())
                {
                    setMeasuredDimension(textMeasuredSize,textMeasuredSize);
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null&&TextUtils.isEmpty(mTextString)) {
            return;
        }

        if (mFillColor != Color.TRANSPARENT) {
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mDrawableRadius, mFillPaint);
            Log.d("CircleTextImageView","fill");
        }
        if(mBitmap!=null) {
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mDrawableRadius, mBitmapPaint);
        }
        if (mBorderWidth != 0) {
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mBorderRadius, mBorderPaint);
            Log.d("CircleTextImageView","DrawBorder");
        }

        if (!TextUtils.isEmpty(mTextString)) {
            Paint.FontMetricsInt fm = mTextPaint.getFontMetricsInt();
            canvas.drawText(mTextString,
                    getWidth() / 2 - mTextPaint.measureText(mTextString) / 2,
                    getHeight() / 2 - fm.descent + (fm.bottom - fm.top) / 2, mTextPaint);
            Log.d("CircleTextImageView","DrawText");
        }
    }

    @Override
    protected void setup() {
        if (!mReady) {
            mSetupPending = true;
            return;
        }

        if (getWidth() == 0 && getHeight() == 0) {
            return;
        }

        if (mBitmap == null&&TextUtils.isEmpty(mTextString)) {
            invalidate();
            return;
        }
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);

        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setAntiAlias(true);
        mFillPaint.setColor(mFillColor);

        mBorderRect.set(0, 0, getWidth(), getHeight());
        mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2.0f, (mBorderRect.width() - mBorderWidth) / 2.0f);

        mDrawableRect.set(mBorderRect);
        if (!mBorderOverlay) {
            mDrawableRect.inset(mBorderWidth, mBorderWidth);
        }
        mDrawableRadius = Math.min(mDrawableRect.height() / 2.0f, mDrawableRect.width() / 2.0f);

        if(mBitmap!=null)
        {
            mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mBitmapHeight = mBitmap.getHeight();
            mBitmapWidth = mBitmap.getWidth();
            mBitmapPaint.setAntiAlias(true);
            mBitmapPaint.setShader(mBitmapShader);
            updateShaderMatrix();
        }
        invalidate();
    }

    public CircleTextImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.CircleTextImageView,defStyleAttr,0);
        mTextString=array.getString(R.styleable.CircleTextImageView_citv_text_text);
        mTextColor=array.getColor(R.styleable.CircleTextImageView_citv_text_color, DEFAULT_TEXT_COLOR);
        mTextSize=array.getDimensionPixelSize(R.styleable.CircleTextImageView_citv_text_size, DEFAULT_TEXT_SIZE);
        mTextPadding=array.getDimensionPixelSize(R.styleable.CircleTextImageView_citv_text_padding, DEFAULT_TEXT_PADDING);
        array.recycle();
    }

    public CircleTextImageView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

}
