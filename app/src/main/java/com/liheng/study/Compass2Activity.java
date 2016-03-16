package com.liheng.study;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Compass2Activity extends AppCompatActivity implements SensorEventListener{

    private static final String TAG = "CompassActivity";
    private static final Paint mBackgroundPaint = new Paint();
    private static final Paint mCompassTextPaint = new Paint(1);
    private Bitmap mBufferImage;
    private SensorEvent mLatestSensorEvent;
    private TextView mOrientationText;
    private ImageView mPointerImage;
    private Sensor mSensor;
    private SensorManager mSensorManager;
    float[] mGravity;
    float[] mGeomagnetic;
    float azimut;

    private void drawRotatedText(Canvas canvas, String s, float f, Rect rect, float f1, float f2)
    {
        float f3 = rect.width();
        float f4 = rect.height();
        canvas.save();
        canvas.rotate(f, f1 + f3 / 2.0F, f2 + f4 / 2.0F);
        canvas.drawText(s, f1, f2 + f4, mCompassTextPaint);
        canvas.restore();
    }

    private Bitmap prepareImage(int i, int j)
    {
        if (i <= 0 || j <= 0)
        {
            return null;
        }
        if (mBufferImage == null || mBufferImage.getWidth() != i || mBufferImage.getHeight() != j)
        {
            mBufferImage = Bitmap.createBitmap(i, j, android.graphics.Bitmap.Config.ARGB_4444);
        }
        return mBufferImage;
    }

    private void updateContents()
    {
        if (mOrientationText != null && mPointerImage != null)
        {
            SensorEvent sensorevent = mLatestSensorEvent;
            float f;
            int i;
            int j;
            Bitmap bitmap;
            if (sensorevent != null)
            {
                f = sensorevent.values[0];
                mOrientationText.setText((new StringBuilder("Bearing: ")).append(f).append(" degrees also -   " ).append(azimut).toString());
            } else
            {
                mOrientationText.setText(R.string.no_data);
                f = 0.0F;
            }

            i = mPointerImage.getWidth();
            j = mPointerImage.getHeight();
            bitmap = prepareImage(i, j);
            if (bitmap != null)
            {
                Canvas canvas = new Canvas(bitmap);
                float f1 = i;
                float f2 = j;
                float f3 = f1 / 2.0F;
                float f4 = f2 / 2.0F;
                canvas.drawRect(0.0F, 0.0F, f1, f2, mBackgroundPaint);
                drawPointer(canvas, f, i, j, f3, f4);
                mPointerImage.setImageBitmap(bitmap);
            }
        }
    }

    public void drawPointer(Canvas canvas, float f, float f1, float f2, float f3, float f4)
    {
        float f5 = 0.9F * Math.min(f1, f2);
        float f6 = f5 / 2.0F;
        mCompassTextPaint.setTextSize(0.15F * f5);
        Rect rect = new Rect();
        float f7 = f3 - f6;
        float f8 = f3 + f6;
        float f9 = f4 - f6;
        float f10 = f4 + f6;
        mCompassTextPaint.getTextBounds("N", 0, "N".length(), rect);
        canvas.save();
        canvas.rotate((360F - f) % 360F, f3, f4);
        canvas.drawText("N", f3 - (float)rect.width() / 2.0F, f9 + (float)rect.height(), mCompassTextPaint);
        drawRotatedText(canvas, "S", 180F, rect, f3 - (float)rect.width() / 2.0F, f10 - (float)rect.height());
        drawRotatedText(canvas, "W", 270F, rect, f7, f4 - (float)rect.height() / 2.0F);
        drawRotatedText(canvas, "E", 90F, rect, f8 - (float)rect.width(), f4 - (float)rect.height() / 2.0F);
        canvas.restore();
    }

    public final void onAccuracyChanged(Sensor sensor, int i)
    {
        updateContents();
    }

    public void onConfigurationChanged(Configuration configuration)
    {
        super.onConfigurationChanged(configuration);
        updateContents();
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_compass2);
        mOrientationText = (TextView)findViewById(R.id.OrientationText);
        mPointerImage = (ImageView)findViewById(R.id.PointerImage);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mBackgroundPaint.setColor(0xff000000);
        mCompassTextPaint.setColor(-1);
        mCompassTextPaint.setTextAlign(android.graphics.Paint.Align.LEFT);
        if (mSensorManager != null)
        {
            mSensor = mSensorManager.getDefaultSensor(3);
            mSensorManager.registerListener(this, mSensor, 1);
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 1);
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), 1);

        }
    }

    protected void onPause()
    {
        super.onPause();
        if (mSensorManager != null)
        {
            mSensorManager.unregisterListener(this);
        }
    }

    protected void onResume()
    {
        super.onResume();
        if (mSensorManager != null)
        {
            mSensorManager.registerListener(this, mSensor, 1);
        }
        updateContents();
    }

    public final void onSensorChanged(SensorEvent event)
    {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION )
            mLatestSensorEvent = event;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
                    mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                azimut = (float)((Math.toDegrees(orientation[0])+360)%360);
            }
        }
        updateContents();
    }
}
