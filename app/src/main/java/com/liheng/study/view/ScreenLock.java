package com.liheng.study.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.liheng.study.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/3/2.
 */
public class ScreenLock extends View{

    private Point[][] points=new Point[3][3];
    private boolean inited=false;
    private Bitmap bitmapPointNormal;
    private Bitmap bitmapPointPress;
    private Bitmap bitmapPointError;
    private Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paintPress=new Paint(Paint.ANTI_ALIAS_FLAG);
    private float bitmapR;
    private ArrayList<Point> pointsList=new ArrayList<>();
    private ArrayList<Integer> passList=new ArrayList<>();
    private OnDrawFinishedListener mOnDrawFinishedListener;

    float mouseX,mouseY;
    boolean isDraw=false;

    public void setOnDrawFinishedListener(OnDrawFinishedListener mOnDrawFinishedListener) {
        this.mOnDrawFinishedListener = mOnDrawFinishedListener;
    }

    public ScreenLock(Context context) {
        super(context);
    }

    public ScreenLock(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScreenLock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!inited){
            init();
        }
        drawPoints(canvas);
        if (pointsList.size()>0){
            Point a=pointsList.get(0);
            for(int i=1;i<pointsList.size();i++){
                Point b=pointsList.get(i);
                drawLine(canvas,a,b);
                a=b;
            }
            if (isDraw){
                drawLine(canvas,a,new Point(mouseX,mouseY));
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mouseX=event.getX();
        mouseY=event.getY();
        int[] ij;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                resetPoints();
                ij=getPointSelecetd();
                if (ij!=null){
                    isDraw=true;
                    points[ij[0]][ij[1]].state=Point.STATE_PRESS;
                    pointsList.add(points[ij[0]][ij[1]]);
                }
                break;
            case MotionEvent.ACTION_UP:
                boolean valid=false;
                if (mOnDrawFinishedListener!=null&&isDraw){
                    valid=mOnDrawFinishedListener.onDrawFinished(passList);
                }
                if (!valid){
                    for (Point p:pointsList){
                        p.state=Point.STATE_ERROR;
                    }
                }
                isDraw=false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDraw){
                    ij=getPointSelecetd();
                    if (ij!=null){
                        if (!pointsList.contains(points[ij[0]][ij[1]])){
                            points[ij[0]][ij[1]].state=Point.STATE_PRESS;
                            pointsList.add(points[ij[0]][ij[1]]);
                            passList.add(ij[0]*3+ij[1]);
                        }
                    }
                }
                break;
        }
        this.postInvalidate();
        return true;
    }

    private int[] getPointSelecetd(){
        Point pMouse=new Point(mouseX,mouseY);
        for (int i=0;i<3;i++) {
            for (int j = 0; j < 3; j++) {
                if (points[i][j].distance(pMouse)<bitmapR){
                    int[] result=new int[2];
                    result[0]=i;
                    result[1]=j;
                    return result;
                }
            }
        }
        return null;
    }

    private void init(){
        paintPress.setColor(Color.YELLOW);
        paintPress.setStrokeWidth(7);
        bitmapPointNormal= BitmapFactory.decodeResource(getResources(), R.mipmap.normal);
        bitmapPointPress=BitmapFactory.decodeResource(getResources(),R.mipmap.press);
        bitmapPointError=BitmapFactory.decodeResource(getResources(),R.mipmap.error);
        bitmapR=bitmapPointNormal.getWidth()/2;
        int height=getHeight();
        int width=getWidth();
        int offset=Math.abs(height-width)/2;
        int offsetX,offsetY;
        int space;
        if (width>height){
            space=height/4;
            offsetX=offset;
            offsetY=0;
        }else {
            space=width/4;
            offsetX=0;
            offsetY=offset;
        }

        for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                points[i][j]=new Point(offsetX+(i+1)*space,offsetY+(j+1)*space);
            }
        }
        inited=true;
    }

    private void drawPoints(Canvas canvas){
        for (int i=0;i<3;i++) {
            for (int j = 0; j < 3; j++) {
                if (points[i][j].state==Point.STATE_NOMAL){
                    canvas.drawBitmap(bitmapPointNormal,points[i][j].x-bitmapR,points[i][j].y-bitmapR,paint);
                }else if (points[i][j].state==Point.STATE_PRESS){
                    canvas.drawBitmap(bitmapPointPress,points[i][j].x-bitmapR,points[i][j].y-bitmapR,paint);
                }else {
                    canvas.drawBitmap(bitmapPointError,points[i][j].x-bitmapR,points[i][j].y-bitmapR,paint);
                }
            }
        }
    }

    private void drawLine(Canvas canvas,Point a,Point b){
        if (a.state==Point.STATE_PRESS){
            canvas.drawLine(a.x,a.y,b.x,b.y,paintPress);
        }else if(a.state==Point.STATE_ERROR){
            paintPress.setColor(Color.RED);
            canvas.drawLine(a.x,a.y,b.x,b.y,paintPress);
        }
    }

    public void resetPoints(){
        pointsList.clear();
        passList.clear();
        for (int i=0;i<3;i++) {
            for (int j = 0; j < 3; j++) {
                points[i][j].state=Point.STATE_NOMAL;
            }
        }
        this.postInvalidate();
    }

    public interface OnDrawFinishedListener{
        boolean onDrawFinished(List<Integer> passList);
    }
}
