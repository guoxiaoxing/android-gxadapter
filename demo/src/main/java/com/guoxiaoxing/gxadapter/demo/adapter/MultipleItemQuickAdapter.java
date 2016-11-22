package com.guoxiaoxing.gxadapter.demo.adapter;

import android.content.Context;

import com.guoxiaoxing.gxadapter.GXMultiItemAdapter;
import com.guoxiaoxing.gxadapter.holder.GXViewHolder;
import com.guoxiaoxing.gxadapter.demo.R;
import com.guoxiaoxing.gxadapter.demo.entity.MultipleItem;

import java.util.List;

public class MultipleItemQuickAdapter extends GXMultiItemAdapter<MultipleItem> {

    public MultipleItemQuickAdapter(Context context, List data) {
        super(data);
        addItemType(MultipleItem.TEXT, R.layout.item_text_view);
        addItemType(MultipleItem.IMG, R.layout.item_image_view);
    }

    @Override
    protected void bindData(GXViewHolder holder, MultipleItem item) {
        switch (holder.getItemViewType()) {
            case MultipleItem.TEXT:
                holder.setText(R.id.tv, item.getContent());
                break;
            case MultipleItem.IMG:
                // set img data
                break;
        }
    }

}
