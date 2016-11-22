package com.guoxiaoxing.gxadapter.demo.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.guoxiaoxing.gxadapter.GXAdapter;
import com.guoxiaoxing.gxadapter.holder.GXViewHolder;
import com.guoxiaoxing.gxadapter.demo.R;
import com.guoxiaoxing.gxadapter.demo.data.DataServer;
import com.guoxiaoxing.gxadapter.demo.entity.Status;
import com.guoxiaoxing.gxadapter.demo.transform.GlideCircleTransform;


public class ClickAdapter extends GXAdapter<Status> {
    public ClickAdapter() {
        super(R.layout.item, DataServer.getSampleData(100));
    }

    public ClickAdapter(int dataSize) {
        super(R.layout.item, DataServer.getSampleData(dataSize));
    }

    @Override
    protected void bindData(GXViewHolder holder, Status item) {
//        holder.getContentView().setBackgroundResource(R.drawable.card_click);
        holder.setText(R.id.tweetName, item.getUserName())
                .setText(R.id.tweetText, item.getText())
                .setText(R.id.tweetDate, item.getCreatedAt())
                .setVisible(R.id.tweetRT, item.isRetweet())
                .addOnClickListener(R.id.tweetAvatar)
                .addOnClickListener(R.id.tweetName)
                .addOnLongClickListener(R.id.tweetText)
        ;
        Glide.with(mContext).load(item.getUserAvatar()).crossFade().placeholder(R.drawable.def_head).transform(new GlideCircleTransform(mContext)).into((ImageView) holder.getView(R.id.tweetAvatar));
    }
}
