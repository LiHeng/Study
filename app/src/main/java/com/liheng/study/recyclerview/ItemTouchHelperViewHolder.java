package com.liheng.study.recyclerview;

/**
 * Created by dell on 2016/3/8.
 */
public interface ItemTouchHelperViewHolder {
    /**
     * Called when the {@link android.support.v7.widget.helper.ItemTouchHelper} first registers an item as being moved or swiped.
     * Implementations should update the item view to indicate it's active state.
     */
    void onItemSelected();


    /**
     * Called when the {@link android.support.v7.widget.helper.ItemTouchHelper} has completed the move or swipe, and the active item
     * state should be cleared.
     */
    void onItemClear();
}
