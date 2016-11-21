package com.guoxiaoxing.gxadapter.demo.adapter;

import com.guoxiaoixng.gxadapter.FCSectionAdapter;
import com.guoxiaoxing.gxadapter.demo.R;
import com.guoxiaoxing.gxadapter.demo.entity.MySection;
import com.guoxiaoxing.gxadapter.demo.entity.Video;
import com.guoxiaoixng.gxadapter.holder.FCViewHolder;

import java.util.List;


public class SectionAdapter extends FCSectionAdapter<MySection> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param sectionHeadResId The section head layout id for each item
     * @param layoutResId The layout resource id of each item.
     * @param data        A new list is created out of this one to avoid mutable list
     */
    public SectionAdapter( int layoutResId, int sectionHeadResId, List data) {
        super( layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void bindHeaderData(FCViewHolder helper, final MySection item) {
        helper.setText(R.id.header, item.header);
        helper.setVisible(R.id.more,item.isMore());
        helper.addOnClickListener(R.id.more);
    }


    @Override
    protected void bindData(FCViewHolder helper, MySection item) {
        Video video = (Video) item.t;
        //helper.setImageUrl(R.id.iv, video.getImg());
        helper.setText(R.id.tv, video.getName());
    }
}
