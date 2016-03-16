package com.liheng.study;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class CompassActivity extends AppCompatActivity{
    private final static String TAG="CompassActivity";
    private Compass mCompass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        mCompass=new Compass(this);
        mCompass.arrowView=(ImageView)findViewById(R.id.main_image_hands);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCompass.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "stop compass");
        mCompass.stop();
    }
}
