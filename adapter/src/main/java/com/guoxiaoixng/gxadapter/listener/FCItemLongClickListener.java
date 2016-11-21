package com.guoxiaoixng.gxadapter.listener;

import android.view.View;

import com.guoxiaoixng.gxadapter.FCAdapter;

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
public abstract class FCItemLongClickListener extends FCClickListener {

    @Override
    public void onItemClick(FCAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemLongClick(FCAdapter adapter, View view, int position) {
        SimpleOnItemLongClick(adapter, view, position);
    }

    @Override
    public void onItemChildClick(FCAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(FCAdapter adapter, View view, int position) {
    }

    public abstract void SimpleOnItemLongClick(FCAdapter adapter, View view, int position);
}
