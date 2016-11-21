package com.souche.android.fcadapter.demo.sticky;

import com.souche.android.sdk.fcadapter.FCMultiItemAdapter;
import com.souche.android.fcadapter.demo.R;
import com.souche.android.fcadapter.demo.entity.LineItem;
import com.souche.android.sdk.fcadapter.holder.FCViewHolder;
import com.souche.android.sdk.fcadapter.sticky.StickyLayoutManager;

import java.util.List;

public class StickyHeaderAdapter extends FCMultiItemAdapter<LineItem> {

    public static final int VIEW_TYPE_CONTENT = 0x00;
    public static final int VIEW_TYPE_HEADER = 0x01;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public StickyHeaderAdapter(List<LineItem> data) {
        super(data);
        addItemType(VIEW_TYPE_CONTENT, R.layout.item_text_line);
        addItemType(VIEW_TYPE_HEADER, R.layout.item_sticky_header);
    }


    @Override
    protected void bindData(FCViewHolder holder, LineItem item) {

        StickyLayoutManager.LayoutParams layoutParams = (StickyLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
        layoutParams.setFirstPosition(0);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_CONTENT:
                holder.setText(R.id.text, item.getText());
                break;
            case VIEW_TYPE_HEADER:
                holder.setText(R.id.text, item.getText());
                break;
            default:
                break;
        }
    }
}
