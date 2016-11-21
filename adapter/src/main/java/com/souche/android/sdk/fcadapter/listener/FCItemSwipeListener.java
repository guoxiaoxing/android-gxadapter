package com.souche.android.sdk.fcadapter.listener;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;


/**
 * A convenience class to extend when you only want to FCItemClickListener for a subset
 * of all the FCClickListener. This implements all methods in the
 * {@link FCClickListener}
 * <p>
 * For more information, you can contact me by guoxiaoxing@souche.com
 *
 * @author guoxiaoxing
 * @since 16/9/18 上午11:04
 */
public interface FCItemSwipeListener {
    void onItemDismiss(int position);
}
