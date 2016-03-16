package com.liheng.study;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class SensorActivity extends AppCompatActivity implements SensorEventListener{
    private static final float SENSOR_DELAY=0.5f;
    private SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        mSensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        TextView tx1=(TextView)findViewById(R.id.tv_info);
        List<Sensor> allSensors=mSensorManager.getSensorList(Sensor.TYPE_ALL);
        //显示有多少个传感器
        tx1.setText("经检测该手机有" + allSensors.size() + "个传感器，他们分别是：\n");

        for (Sensor s : allSensors) {
            String tempString = "\n" + "  设备名称：" + s.getName() + "\n" + "  设备版本：" + s.getVersion() + "\n" + "  供应商："
                    + s.getVendor() + "\n";
            switch (s.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 加速度传感器accelerometer" + tempString);
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 陀螺仪传感器gyroscope" + tempString);
                    break;
                case Sensor.TYPE_LIGHT:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 环境光线传感器light" + tempString);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 电磁场传感器magnetic field" + tempString);
                    break;
                case Sensor.TYPE_ORIENTATION:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 方向传感器orientation" + tempString);
                    break;
                case Sensor.TYPE_PRESSURE:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 压力传感器pressure" + tempString);
                    break;
                case Sensor.TYPE_PROXIMITY:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 距离传感器proximity" + tempString);
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 温度传感器temperature" + tempString);
                    break;
                default:
                    tx1.setText(tx1.getText().toString() + s.getType() + " 未知传感器" + tempString);
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE)==null){
            Log.d("SensorActivity","TEMPERATURE is null");
        }
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE),SensorManager.SENSOR_DELAY_UI);

    }

    @Override
    protected void onStop()
    {
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
          float values[]=event.values;
//        StringBuilder sb = new StringBuilder();
//        sb.append("X方向上的加速度");
//        sb.append(values[0]);
//        sb.append("Y方向上的加速度：");
//        sb.append(values[1]);
//        sb.append("Z方向上的加速度：");
//        sb.append(values[2]);
        Log.d("SensorActivity",String.valueOf(values[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
