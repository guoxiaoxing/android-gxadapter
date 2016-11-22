package com.guoxiaoxing.gxadapter;

import android.view.ViewGroup;

import com.guoxiaoxing.gxadapter.holder.FCViewHolder;
import com.guoxiaoxing.gxadapter.item.SectionItem;

import java.util.List;

/**
 * For more information, you can contact me by guoxiaoxing@souche.com
 *
 * @author guoxiaoxing
 * @since 16/9/19 下午2:58
 */
public abstract class GXSectionAdapter<T extends SectionItem> extends GXAdapter {

    protected static final int SECTION_HEADER_VIEW = 0x00000444;
    protected int mSectionHeadResId;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param sectionHeadResId The section head layout id for each item
     * @param layoutResId      The layout resource id of each item.
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public GXSectionAdapter(int layoutResId, int sectionHeadResId, List<T> data) {
        super(layoutResId, data);
        this.mSectionHeadResId = sectionHeadResId;
    }

    @Override
    protected FCViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SECTION_HEADER_VIEW)
            return new FCViewHolder(getItemView(mSectionHeadResId, parent));

        return super.onCreateDefViewHolder(parent, viewType);
    }

    @Override
    protected int getDefItemViewType(int position) {
        return ((SectionItem) mData.get(position)).isHeader ? SECTION_HEADER_VIEW : 0;
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
