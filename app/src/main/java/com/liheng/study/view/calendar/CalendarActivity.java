package com.liheng.study.view.calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.liheng.study.R;

import java.util.ArrayList;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        List<Integer> list=new ArrayList<>();
        list.add(1);
        list.add(8);
        list.add(4);
        list.add(20);
        MonthDateView view=(MonthDateView)findViewById(R.id.monthDateView);
        view.setDaysHasThingList(list);
        view.setOnDateSelectedListener(new MonthDateView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int day) {
                Toast.makeText(getApplicationContext(),day+"",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
