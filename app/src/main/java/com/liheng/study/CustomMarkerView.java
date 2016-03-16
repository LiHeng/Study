package com.liheng.study;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.util.List;

/**
 * Created by dell on 2016/3/6.
 */
public class CustomMarkerView extends MarkerView {
    private TextView tvContent;
    private List<String> XVals;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent=(TextView)findViewById(R.id.tvContent);
    }

    public CustomMarkerView(Context context, int layoutResource,List<String> XVals) {
        super(context, layoutResource);
        this.XVals=XVals;
        tvContent=(TextView)findViewById(R.id.tvContent);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText(XVals.get(e.getXIndex())+" "+e.getVal());
    }

    @Override
    public int getXOffset(float xpos) {
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset(float ypos) {
        return -getHeight();
    }
}
