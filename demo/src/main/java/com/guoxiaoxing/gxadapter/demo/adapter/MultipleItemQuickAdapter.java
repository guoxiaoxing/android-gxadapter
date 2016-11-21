package com.guoxiaoxing.gxadapter.demo.adapter;

import android.content.Context;

import com.guoxiaoixng.gxadapter.FCMultiItemAdapter;
import com.guoxiaoxing.gxadapter.demo.R;
import com.guoxiaoxing.gxadapter.demo.entity.MultipleItem;
import com.guoxiaoixng.gxadapter.holder.FCViewHolder;

import java.util.List;

public class MultipleItemQuickAdapter extends FCMultiItemAdapter<MultipleItem> {

    public MultipleItemQuickAdapter(Context context, List data) {
        super(data);
        addItemType(MultipleItem.TEXT, R.layout.item_text_view);
        addItemType(MultipleItem.IMG, R.layout.item_image_view);
    }

    @Override
    protected void bindData(FCViewHolder holder, MultipleItem item) {
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
