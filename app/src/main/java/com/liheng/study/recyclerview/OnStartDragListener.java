package com.liheng.study.recyclerview;

import android.support.v7.widget.RecyclerView;

/**
 * Created by dell on 2016/3/8.
 */
public interface OnStartDragListener {
    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
