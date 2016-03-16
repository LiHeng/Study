package com.liheng.study;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.gun0912.tedpicker.ImagePickerActivity;
import com.liheng.study.bmob.BmobActivity;
import com.liheng.study.drawable.CircleImageDrawable;
import com.liheng.study.drawable.RoundRectImageDrawable;
import com.liheng.study.recyclerview.RecyclerListActivity;
import com.liheng.study.view.PushSlideSwitch;
import com.liheng.study.view.ScreenLockActivity;
import com.liheng.study.view.calendar.CalendarActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int INTENT_REQUEST_GET_IMAGES = 13;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=RESULT_OK){
            return;
        }
        if (requestCode==INTENT_REQUEST_GET_IMAGES){
            ArrayList<Uri> image_uris = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
            ((ImageView)findViewById(R.id.pic1)).setImageURI(image_uris.get(0));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.example);
        ((ImageView)findViewById(R.id.pic1)).setImageDrawable(new RoundRectImageDrawable(bitmap));
        ((ImageView)findViewById(R.id.pic2)).setImageDrawable(new CircleImageDrawable(bitmap));
        ((ImageView)findViewById(R.id.pic3)).setImageResource(R.drawable.rect);

        (findViewById(R.id.pic1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ScreenLockActivity.class));
            }
        });

        (findViewById(R.id.pic2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CalendarActivity.class));
            }
        });

        (findViewById(R.id.pic3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RecyclerListActivity.class));
            }
        });

        ((PushSlideSwitch)findViewById(R.id.pushSlideSwitch)).setOnSwitchChangedListener(new PushSlideSwitch.OnSwitchChangedListener() {
            @Override
            public void onSwitchChange(PushSlideSwitch switchView, boolean isChecked) {
                startActivityForResult(new Intent(MainActivity.this, BmobActivity.class),INTENT_REQUEST_GET_IMAGES);
            }
        });
    }
}
