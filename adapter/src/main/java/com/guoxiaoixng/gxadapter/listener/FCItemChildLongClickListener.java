package com.guoxiaoixng.gxadapter.listener;

import android.view.View;

import com.guoxiaoixng.gxadapter.GXAdapter;

/**
 * A convenience class to extend when you only want to FCItemChildLongClickListener for a subset
 * of all the FCClickListener. This implements all methods in the
 * {@link FCClickListener}
 * <p>
 * For more information, you can contact me by guoxiaoxing@souche.com
 *
 * @author guoxiaoxing
 * @since 16/9/18 上午11:04
 */
public abstract class FCItemChildLongClickListener extends FCClickListener {

    @Override
    public void onItemClick(GXAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemLongClick(GXAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(GXAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(GXAdapter adapter, View view, int position) {
        SimpleOnItemChildLongClick(adapter, view, position);
    }

    public abstract void SimpleOnItemChildLongClick(GXAdapter adapter, View view, int position);
}
