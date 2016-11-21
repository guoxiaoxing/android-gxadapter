package com.guoxiaoixng.gxadapter;

import android.view.ViewGroup;

import com.guoxiaoixng.gxadapter.holder.FCViewHolder;
import com.guoxiaoixng.gxadapter.item.SectionItem;

import java.util.List;

/**
 * For more information, you can contact me by guoxiaoxing@souche.com
 *
 * @author guoxiaoxing
 * @since 16/9/19 下午2:58
 */
public abstract class FCSectionAdapter<T extends SectionItem> extends FCAdapter {

    protected int mSectionHeadResId;
    protected static final int SECTION_HEADER_VIEW = 0x00000444;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param sectionHeadResId The section head layout id for each item
     * @param layoutResId      The layout resource id of each item.
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public FCSectionAdapter(int layoutResId, int sectionHeadResId, List<T> data) {
        super(layoutResId, data);
        this.mSectionHeadResId = sectionHeadResId;
    }

    @Override
    protected int getDefItemViewType(int position) {
        return ((SectionItem) mData.get(position)).isHeader ? SECTION_HEADER_VIEW : 0;
    }

    @Override
    protected FCViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SECTION_HEADER_VIEW)
            return new FCViewHolder(getItemView(mSectionHeadResId, parent));

        return super.onCreateDefViewHolder(parent, viewType);
    }

    /**
     * @param holder A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    @Override
    protected void bindData(FCViewHolder holder, Object item) {
        switch (holder.getItemViewType()) {
            case SECTION_HEADER_VIEW:
                setFullSpan(holder);
                bindHeaderData(holder, (T) item);
                break;
            default:
                bindData(holder, (T) item);
                break;
        }
    }

    protected abstract void bindHeaderData(FCViewHolder helper, T item);

    protected abstract void bindData(FCViewHolder helper, T item);

}
