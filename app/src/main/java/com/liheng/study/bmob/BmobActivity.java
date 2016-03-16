package com.liheng.study.bmob;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.liheng.study.R;
import com.liheng.study.util.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class BmobActivity extends AppCompatActivity {
    Button add;
    Button delete;
    Button update;
    Button query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmob);
        add=(Button)findViewById(R.id.add);
        delete=(Button)findViewById(R.id.delete);
        update=(Button)findViewById(R.id.update);
        query=(Button)findViewById(R.id.query);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Light light=new Light();
                light.setLight(100.7);
                light.setTimestamp(new BmobDate(new Date()));
                light.save(BmobActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(BmobActivity.this,"save success",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
            }
        });

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobQuery<Light> query=new BmobQuery<Light>();
                List<BmobQuery<Light>> and = new ArrayList<BmobQuery<Light>>();
                query.setLimit(100);

                BmobQuery<Light> q1 = new BmobQuery<Light>();
                String start="2016-3-14 00:00:00";
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date  = null;
                try {
                    date = format.parse(start);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                q1.addWhereGreaterThanOrEqualTo("timestamp",new BmobDate(date));
                and.add(q1);

                BmobQuery<Light> q2 = new BmobQuery<Light>();
                String end = "2016-3-14 23:59:59";
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date1  = null;
                try {
                    date1 = sdf1.parse(end);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                q2.addWhereLessThanOrEqualTo("timestamp", new BmobDate(date1));
                and.add(q2);
                query.and(and);

                query.findObjects(BmobActivity.this, new FindListener<Light>() {
                    @Override
                    public void onSuccess(List<Light> list) {
                        if (list!=null){
                            for (Light light:list){
                                Log.d("tag",light.getTimestamp().getDate()+light.getLight());
                            }
                        }
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }
        });
    }
}
