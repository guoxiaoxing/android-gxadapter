package com.guoxiaoxing.gxadapter.demo.adapter;

import android.animation.Animator;

import com.guoxiaoxing.gxadapter.GXAdapter;
import com.guoxiaoxing.gxadapter.holder.GXViewHolder;
import com.guoxiaoxing.gxadapter.demo.R;
import com.guoxiaoxing.gxadapter.demo.entity.HomeItem;

import java.util.List;

public class HomeAdapter extends GXAdapter<HomeItem> {
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
    protected void bindData(GXViewHolder holder, HomeItem item) {
        holder.setText(R.id.info_text, item.getTitle());
    }
}
