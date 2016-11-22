package com.guoxiaoxing.gxadapter.listener;


/**
 * A convenience class to extend when you only want to GXItemClickListener for a subset
 * of all the GXClickListener. This implements all methods in the
 * {@link GXClickListener}
 * <p>
 * For more information, you can contact me by guoxiaoxing@souche.com
 *
 * @author guoxiaoxing
 * @since 16/9/18 上午11:04
 */
public interface GXItemSwipeListener {
    void onItemDismiss(int position);
}
