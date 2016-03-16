package com.liheng.study;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.liheng.study.util.DateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TemperChartActivity extends AppCompatActivity implements SensorEventListener {
    private LineChart mChart;
    RelativeLayout container;
    private LineData data;
    private List<LightEntity> lightEntities=new ArrayList<>();
    private ArrayList<String> xVals;
    private LineDataSet dataSet;
    private ArrayList<Entry> yVals;
    private Random random;


    private Handler mHandler=new Handler();
    private SensorManager mSensorManager;
    private long mCurrentTime;

    private boolean mContinueDetect=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temper_chart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mChart=(LineChart)findViewById(R.id.spread_pie_chart);
        setupChart();
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                //Toast.makeText(TemperChartActivity.this,xVals.get(e.getXIndex())+" "+e.getVal(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected() {

            }
        });

        container=(RelativeLayout)findViewById(R.id.container);


        //generateData();
       // mChart.setData(data);
        //mChart.setDescription("公司年度利润");
        //mChart.animateY(3000);

//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                generateData();
//                mChart.setData(data);
//                mChart.invalidate();
//                Log.d("Temp","change data");
//            }
//        },5000);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void setupChart(){
        mChart.setAutoScaleMinMaxEnabled(true);
        mChart.setDescription("时间");
        Legend legend=mChart.getLegend();
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
        legend.setTextSize(20);
        XAxis xAxis=mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineColor(Color.RED);
        xAxis.setDrawGridLines(false);
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisLineColor(Color.GREEN);
        mChart.animateXY(2000,2000);
    }

    private void generateData(){
        xVals=new ArrayList<>();
        yVals=new ArrayList<>();
        random=new Random();
        for(int i=0;i<12;i++){
            float profix=random.nextFloat();
            yVals.add(new Entry(profix,i));
            xVals.add((i+1)+"月");
        }
        dataSet=new LineDataSet(yVals,"公司年度利润");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        data=new LineData(xVals,dataSet);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }else if (id==R.id.action_detect){
            mContinueDetect=!mContinueDetect;
            if (!mContinueDetect){
                item.setTitle("开始检测");
            }else {
                item.setTitle("停止检测");
            }
            return true;
        }else if(id==R.id.line){
            mChart.setVisibility(View.GONE);
            mChart=new LineChart(this);
            LayoutParams layoutParams=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            container.addView(mChart, layoutParams);
            container.invalidate();
            setupData();
            mChart.setData(data);
            mChart.notifyDataSetChanged();
            mChart.invalidate();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onStop()
    {
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values=event.values;
        float light=values[0];
        if ((System.currentTimeMillis()-mCurrentTime)>5000&&mContinueDetect){
            lightEntities.add(new LightEntity(light, DateUtil.getCurrentTime()));
            mCurrentTime=System.currentTimeMillis();
            setupData();
            mChart.setData(data);
            mChart.notifyDataSetChanged();
            if (lightEntities.size()>5){
                mChart.moveViewToX((float)(yVals.get(lightEntities.size()-5).getXIndex()));
            }
            mChart.invalidate();
        }
    }

    private void setupData(){
        xVals=new ArrayList<>();
        yVals=new ArrayList<>();
        for (int i=0;i<lightEntities.size();i++){
            xVals.add(lightEntities.get(i).getTimestamp());
            yVals.add(new Entry(lightEntities.get(i).getLight(),i));
        }
        dataSet = new LineDataSet(yVals,"光照强度变化图");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        data=new LineData(xVals,dataSet);
        CustomMarkerView markerView=new CustomMarkerView(this,R.layout.view_marker,xVals);
        mChart.setMarkerView(markerView);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
