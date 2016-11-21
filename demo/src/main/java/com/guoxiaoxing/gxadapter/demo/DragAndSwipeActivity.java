package com.guoxiaoxing.gxadapter.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guoxiaoxing.gxadapter.demo.R;
import com.guoxiaoxing.gxadapter.demo.adapter.ItemDragAndSwipeAdapter;
import com.guoxiaoixng.gxadapter.listener.FCItemDragListener;
import com.guoxiaoixng.gxadapter.listener.FCItemSwipeListener;
import com.guoxiaoixng.gxadapter.swipe.adapter.RecyclerSwipeAdapter;

import java.util.ArrayList;
import java.util.List;

public class DragAndSwipeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<String> mData;
    private ItemDragAndSwipeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_touch_use);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mData = generateData(50);

        FCItemDragListener fcItemDragListener = new FCItemDragListener() {

            @Override
            public void onItemMove(int fromPosition, int toPosition) {
            }
        };

        FCItemSwipeListener fcItemSwipeListener = new FCItemSwipeListener() {

            @Override
            public void onItemDismiss(int position) {

            }
        };

        mAdapter = new ItemDragAndSwipeAdapter(mData);
        mRecyclerView.setAdapter(mAdapter);

        //以下代码需要在mRecyclerView.setAdapter(mAdapter)之后

        //开启拖拽排序, fcItemDragListener如果不用可不写
        mAdapter.setLongPressDragEnabled(true);
        //mAdapter.setFCItemDragListener(fcItemDragListener);
        //开启侧滑删除, fcItemSwipeListener如果不用可不写， 侧滑删除和侧滑菜单是互斥的，
        //开启侧滑删除后会直接删除整个ItemView，不再显示侧滑菜单。
        //mAdapter.setItemViewSwipeEnabled(true);
        //mAdapter.setFCItemSwipeListener(fcItemSwipeListener);
    }

    private List<String> generateData(int size) {
        ArrayList<String> data = new ArrayList(size);
        for (int i = 1; i < size; i++) {
            data.add("item " + i);
        }
        return data;
    }

    /**
     * 为排除侧滑菜单(删除出现覆盖)问题建立的DragAdapter
     * modified by liuxuehao
     */
    public static class DragAdapter extends RecyclerSwipeAdapter<DragAdapter.ViewHolder> {
        private List<String> mData;
        private Activity mContext;

        public DragAdapter(Activity context, List<String> mData) {
            mContext = context;
            this.mData = mData;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mContext.getLayoutInflater().inflate(R.layout.item_drag_list, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tvItem.setText("item " + position);

            mItemManger.bind(holder.itemView, position);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.swipe_layout;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvItem;

            public ViewHolder(View itemView) {
                super(itemView);
                tvItem = (TextView) itemView.findViewById(R.id.tv_item);
            }
        }
    }
}
