package com.guoxiaoxing.gxadapter.demo.adapter;

import android.animation.Animator;

import com.guoxiaoixng.gxadapter.FCAdapter;
import com.guoxiaoixng.gxadapter.holder.FCViewHolder;
import com.guoxiaoxing.gxadapter.demo.R;
import com.guoxiaoxing.gxadapter.demo.entity.HomeItem;

import java.util.List;

public class HomeAdapter extends FCAdapter<HomeItem> {
    public HomeAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void bindData(FCViewHolder holder, HomeItem item) {
        holder.setText(R.id.info_text, item.getTitle());
    }

    @Override
    protected void startAnim(Animator anim, int index) {
        super.startAnim(anim, index);
        if (index < 5)
            anim.setStartDelay(index * 150);
    }
}