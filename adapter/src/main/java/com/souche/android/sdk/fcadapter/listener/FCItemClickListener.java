package com.souche.android.sdk.fcadapter.listener;

import android.view.View;

import com.souche.android.sdk.fcadapter.FCAdapter;

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
public abstract class FCItemClickListener extends FCClickListener {

    @Override
    public void onItemClick(FCAdapter adapter, View view, int position) {
        SimpleOnItemClick(adapter, view, position);
    }

    @Override
    public void onItemLongClick(FCAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(FCAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(FCAdapter adapter, View view, int position) {

    }

    public abstract void SimpleOnItemClick(FCAdapter adapter, View view, int position);
}
