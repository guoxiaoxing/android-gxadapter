package com.guoxiaoxing.gxadapter.demo.adapter;

import android.util.Log;
import android.view.View;

import com.guoxiaoxing.gxadapter.GXMultiItemAdapter;
import com.guoxiaoxing.gxadapter.holder.GXViewHolder;
import com.guoxiaoxing.gxadapter.item.MultiItem;
import com.guoxiaoxing.gxadapter.demo.R;
import com.guoxiaoxing.gxadapter.demo.entity.Level0Item;
import com.guoxiaoxing.gxadapter.demo.entity.Level1Item;
import com.guoxiaoxing.gxadapter.demo.entity.Person;

import java.util.List;

public class ExpandableItemAdapter extends GXMultiItemAdapter<MultiItem> {
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    public static final int TYPE_PERSON = 2;
    private static final String TAG = ExpandableItemAdapter.class.getSimpleName();

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ExpandableItemAdapter(List<MultiItem> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0);
        addItemType(TYPE_LEVEL_1, R.layout.item_expandable_lv1);
        addItemType(TYPE_PERSON, R.layout.item_text_view);
    }

    @Override
    protected void bindData(final GXViewHolder holder, final MultiItem item) {
        switch (holder.getItemViewType()) {
            case TYPE_LEVEL_0:
                final Level0Item lv0 = (Level0Item)item;
                holder.setText(R.id.title, lv0.title)
                        .setText(R.id.sub_title, lv0.subTitle)
                        .setText(R.id.expand_state, lv0.isExpanded() ? R.string.expanded : R.string.collapsed);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        Log.d(TAG, "Level 0 item pos: " + pos);
                        if (lv0.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });
                break;
            case TYPE_LEVEL_1:
                final Level1Item lv1 = (Level1Item)item;
                holder.setText(R.id.title, lv1.title)
                        .setText(R.id.sub_title, lv1.subTitle)
                        .setText(R.id.expand_state, lv1.isExpanded() ? R.string.expanded : R.string.collapsed);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        Log.d(TAG, "Level 1 item pos: " + pos);
                        if (lv1.isExpanded()) {
                            collapse(pos, false);
                        } else {
                            expand(pos, false);
                        }
                    }
                });
                break;
            case TYPE_PERSON:
                final Person person = (Person)item;
                holder.setText(R.id.tv, person.name);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "person: " + person.name + " age: " + person.age);
                    }
                });
                break;
        }
    }
}
