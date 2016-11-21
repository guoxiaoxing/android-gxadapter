package com.souche.android.fcadapter.demo.adapter;

import android.animation.Animator;
import android.graphics.Color;
import android.support.v7.widget.CardView;

import com.souche.android.sdk.fcadapter.FCAdapter;
import com.souche.android.fcadapter.demo.R;
import com.souche.android.fcadapter.demo.entity.HomeItem;
import com.souche.android.sdk.fcadapter.holder.FCViewHolder;

import java.util.List;

public class HomeAdapter extends FCAdapter<HomeItem> {
    public HomeAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void startAnim(Animator anim, int index) {
        super.startAnim(anim, index);
        if (index < 5)
            anim.setStartDelay(index * 150);
    }

    @Override
    protected void bindData(FCViewHolder holder, HomeItem item) {
        holder.setText(R.id.info_text, item.getTitle());
        CardView cardView = holder.getView(R.id.front_view);
        cardView.setCardBackgroundColor(Color.parseColor(item.getColorStr()));
    }
}
