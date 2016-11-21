package com.souche.android.fcadapter.demo.adapter;

import com.souche.android.sdk.fcadapter.FCAdapter;
import com.souche.android.fcadapter.demo.R;
import com.souche.android.fcadapter.demo.data.DataServer;
import com.souche.android.fcadapter.demo.entity.Status;
import com.souche.android.sdk.fcadapter.holder.FCViewHolder;

public class DemoFCAdapter extends FCAdapter<Status> {
    public DemoFCAdapter() {
        super(R.layout.tweet, DataServer.getSampleData(100));
    }

    public DemoFCAdapter(int dataSize) {
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
