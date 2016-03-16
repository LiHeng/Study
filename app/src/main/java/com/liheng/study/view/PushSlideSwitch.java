package com.liheng.study.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.liheng.study.R;

/**
 * Created by dell on 2016/3/5.
 */
public class PushSlideSwitch extends View{
    /** 最大透明度，就是不透明 */
    private final int MAX_ALPHA = 255;

    private Bitmap mSwitchBgUnselected;
    private Bitmap mSwitchBgSelected ;
    private Bitmap mSwitchBallUnseleted;
    private Bitmap mSwitchBallSeleted;

    private boolean mSwitchOn = true;
    private float mCurrentX = 0;
    /** Switch 最大移动距离   */
    private int mMoveLength;
    /** 第一次按下的有效区域 */
    private float mLastX = 0;
    /** 绘制的目标区域大小  */
    private Rect mDest = null;

    /** Switch 移动的偏移量  */
    private int mMoveDeltX = 0;

    private Paint mPaint;
    private OnSwitchChangedListener onSwitchChangedListener;
    private boolean mFlag = false;
    /** enabled 属性 为 true */
    private boolean mEnabled = true;

    private int mAlpha = MAX_ALPHA;
    private boolean mIsScrolled =false;

    private BitmapShader mBitmapShader;
    private Matrix mShaderMatrix = new Matrix();
    private float scaleX,scaleY=1.5f;


    public PushSlideSwitch(Context context) {
        this(context,null);
    }

    public PushSlideSwitch(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public PushSlideSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mSwitchBgSelected = BitmapFactory.decodeResource(getResources(), R.mipmap.push_button_selected_bg);
        mSwitchBgUnselected = BitmapFactory.decodeResource(getResources(), R.mipmap.push_button_unselected_bg);

        mSwitchBallSeleted = BitmapFactory.decodeResource(getResources(),R.mipmap.push_button_ball_selected);
        mSwitchBallUnseleted = BitmapFactory.decodeResource(getResources(),R.mipmap.push_button_ball_unselected);

        mMoveLength=mSwitchBgSelected.getWidth()-mSwitchBallSeleted.getWidth();
        mDest=new Rect(0,0,mSwitchBgSelected.getWidth(), mSwitchBgSelected.getHeight());
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setAlpha(255);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        mBitmapShader=new BitmapShader(mSwitchBgUnselected, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(mBitmapShader);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.saveLayerAlpha(new RectF(mDest), mAlpha, Canvas.MATRIX_SAVE_FLAG
                | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
                | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
                | Canvas.CLIP_TO_LAYER_SAVE_FLAG);

        if (!mSwitchOn){
            if (mMoveDeltX>0){
                if (mMoveDeltX<mMoveLength/2){
                    canvas.drawBitmap(mSwitchBgUnselected, 0, 0, null);
                    canvas.drawBitmap(mSwitchBallUnseleted, mMoveDeltX, 0, null);
                }else {
                    canvas.drawBitmap(mSwitchBgSelected,0,0,null);
                    canvas.drawBitmap(mSwitchBallSeleted, mMoveDeltX, 0, null);
                }
            }else {
                canvas.drawBitmap(mSwitchBgUnselected, 0, 0, null);
                canvas.drawBitmap(mSwitchBallUnseleted, 0, 0, null);
            }
        }else {
            if (mMoveDeltX<0){
                if(Math.abs(mMoveDeltX) < mMoveLength/2){
                    canvas.drawBitmap(mSwitchBgSelected,0,0,null);
                    canvas.drawBitmap(mSwitchBallSeleted,mMoveLength+mMoveDeltX,0,null);
                }else {
                    canvas.drawBitmap(mSwitchBgUnselected,0,0,null);
                    canvas.drawBitmap(mSwitchBallUnseleted,mMoveLength+mMoveDeltX,0,null);
                }
            }else {
                canvas.drawBitmap(mSwitchBgSelected,0,0,null);
                canvas.drawBitmap(mSwitchBallSeleted,mMoveLength,0,null);
            }
        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mEnabled){
            return true;
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastX=event.getX();
                break;
            case MotionEvent.ACTION_UP:
                if (!mIsScrolled){
                    mMoveDeltX = mSwitchOn ? mMoveLength : -mMoveLength;
                    mSwitchOn = !mSwitchOn;
                    if (onSwitchChangedListener != null) {
                        onSwitchChangedListener.onSwitchChange(this, mSwitchOn);
                    }
                    invalidate();
                    mMoveDeltX = 0;
                    break;
                }
                mIsScrolled=false;
                if (Math.abs(mMoveDeltX) > 0 && Math.abs(mMoveDeltX) < mMoveLength / 2) {
                    mMoveDeltX = 0;
                    invalidate();
                } else if (Math.abs(mMoveDeltX) > mMoveLength / 2
                        && Math.abs(mMoveDeltX) <= mMoveLength) {
                    mMoveDeltX = mMoveDeltX > 0 ? mMoveLength : -mMoveLength;
                    mSwitchOn = !mSwitchOn;
                    if (onSwitchChangedListener != null) {
                        onSwitchChangedListener.onSwitchChange(this, mSwitchOn);
                    }
                    invalidate();
                    mMoveDeltX = 0;
                } else if (mMoveDeltX == 0 && mFlag) {
                    // 这时候得到的是不需要进行处理的，因为已经move过了
                    mMoveDeltX = 0;
                    mFlag = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentX=event.getX();
                mMoveDeltX=(int)(mCurrentX-mLastX);
                if (mMoveDeltX>3){
                    mIsScrolled=true;
                }
                if ((mSwitchOn && mMoveDeltX > 0) || (!mSwitchOn && mMoveDeltX < 0)) {
                    mFlag = true;
                    mMoveDeltX = 0;
                }
                if (Math.abs(mMoveDeltX) > mMoveLength) {
                    mMoveDeltX = mMoveDeltX > 0 ? mMoveLength : -mMoveLength;
                }
                invalidate();
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mSwitchBgSelected.getWidth(), mSwitchBallSeleted.getHeight());
    }

    public void setOnSwitchChangedListener(OnSwitchChangedListener onSwitchChangedListener) {
        this.onSwitchChangedListener = onSwitchChangedListener;
    }

    @Override
    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
        mAlpha = enabled ? MAX_ALPHA : MAX_ALPHA/2;
        Log.d("enabled", enabled ? "true" : "false");
        super.setEnabled(enabled);
        invalidate();
    }

    public void setChecked(boolean checked) {
        mSwitchOn = checked;
        invalidate();
    }

    public void toggle() {
        setChecked(!mSwitchOn);
    }

    public interface OnSwitchChangedListener{
        public void onSwitchChange(PushSlideSwitch switchView, boolean isChecked);
    }
}
