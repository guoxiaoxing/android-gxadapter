package com.guoxiaoixng.gxadapter;

import android.support.annotation.LayoutRes;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.guoxiaoixng.gxadapter.holder.FCViewHolder;
import com.guoxiaoixng.gxadapter.item.MultiItem;

import java.util.List;

/**
 * For more information, you can contact me by guoxiaoxing@souche.com
 *
 * @author guoxiaoxing
 * @since 16/9/19 下午2:58
 */
public abstract class GXMultiItemAdapter<T extends MultiItem> extends GXAdapter<T> {

    private static final int DEFAULT_VIEW_TYPE = -0xff;
    /**
     * layouts indexed with their types
     */
    private SparseArray<Integer> layouts;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public GXMultiItemAdapter(List<T> data) {
        super(data);
    }

    protected void setDefaultViewTypeLayout(@LayoutRes int layoutResId) {
        addItemType(DEFAULT_VIEW_TYPE, layoutResId);
    }

    protected void addItemType(int type, @LayoutRes int layoutResId) {
        if (layouts == null) {
            layouts = new SparseArray<>();
        }
        layouts.put(type, layoutResId);
    }

    @Override
    protected FCViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, getLayoutId(viewType));
    }

    @Override
    protected int getDefItemViewType(int position) {
        Object item = mData.get(position);
        if (item instanceof MultiItem) {
            return ((MultiItem) item).getItemType();
        }
        return DEFAULT_VIEW_TYPE;
    }

    private int getLayoutId(int viewType) {
        return layouts.get(viewType);
    }
}


