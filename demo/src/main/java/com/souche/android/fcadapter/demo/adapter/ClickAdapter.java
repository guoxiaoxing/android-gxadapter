package com.souche.android.fcadapter.demo.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.souche.android.sdk.fcadapter.FCAdapter;
import com.souche.android.fcadapter.demo.R;
import com.souche.android.fcadapter.demo.data.DataServer;
import com.souche.android.fcadapter.demo.entity.Status;
import com.souche.android.fcadapter.demo.transform.GlideCircleTransform;
import com.souche.android.sdk.fcadapter.holder.FCViewHolder;


public class ClickAdapter extends FCAdapter<Status> {
    public ClickAdapter() {
        super( R.layout.item, DataServer.getSampleData(100));
    }

    public ClickAdapter(int dataSize) {
        super( R.layout.item, DataServer.getSampleData(dataSize));
    }

    @Override
    protected void bindData(FCViewHolder holder, Status item) {
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
