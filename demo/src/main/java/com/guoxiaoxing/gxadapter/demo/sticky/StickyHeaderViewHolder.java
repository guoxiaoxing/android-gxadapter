package com.guoxiaoxing.gxadapter.demo.sticky;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.guoxiaoxing.gxadapter.demo.R;

class StickyHeaderViewHolder extends RecyclerView.ViewHolder {

    private TextView mTextView;

    StickyHeaderViewHolder(View view) {
        super(view);

        mTextView = (TextView) view.findViewById(R.id.text);
    }

    public void bindItem(String text) {
        mTextView.setText(text);
    }

    @Override
    public String toString() {
        return mTextView.getText().toString();
    }
}
