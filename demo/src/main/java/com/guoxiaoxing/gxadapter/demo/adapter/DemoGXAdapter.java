package com.guoxiaoxing.gxadapter.demo.adapter;

import com.guoxiaoixng.gxadapter.GXAdapter;
import com.guoxiaoixng.gxadapter.holder.FCViewHolder;
import com.guoxiaoxing.gxadapter.demo.R;
import com.guoxiaoxing.gxadapter.demo.data.DataServer;
import com.guoxiaoxing.gxadapter.demo.entity.Status;

public class DemoGXAdapter extends GXAdapter<Status> {
    public DemoGXAdapter() {
        super(R.layout.tweet, DataServer.getSampleData(100));
    }

    public DemoGXAdapter(int dataSize) {
        super(R.layout.tweet, DataServer.getSampleData(dataSize));
    }

    @Override
    protected void bindData(FCViewHolder holder, Status item) {
        holder.setText(R.id.tweetName, item.getUserName())
                .setText(R.id.tweetText, item.getText())
                .setText(R.id.tweetDate, item.getCreatedAt())
                .setVisible(R.id.tweetRT, item.isRetweet())
                .setImageResource(R.id.tweetAvatar, R.drawable.def_head)
                .addOnClickListener(R.id.tweetAvatar)
                .addOnClickListener(R.id.tweetName)
                .linkify(R.id.tweetText);
    }

    @Override
    public String onCreateBubbleText(int pos) {
        return String.valueOf(pos);
    }
}
