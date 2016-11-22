package com.guoxiaoxing.gxadapter.demo.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

import com.guoxiaoxing.gxadapter.GXItemDraggableAdapter;
import com.guoxiaoxing.gxadapter.holder.FCViewHolder;
import com.guoxiaoxing.gxadapter.demo.R;

import java.util.List;

public class ItemDragAndSwipeAdapter extends GXItemDraggableAdapter<String> {

    public ItemDragAndSwipeAdapter(List<String> data) {
        super(R.layout.item_drag_and_swipe, data);
    }

    @Override
    protected void bindData(final FCViewHolder holder, String item) {
        holder.setOnClickListener(R.id.item_front_view, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(), "Item Click " + holder.getLayoutPosition(), Toast.LENGTH_SHORT).show();
            }
        });

        //底部菜单监听事件
        holder.setOnClickListener(R.id.view_list_repo_action_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setMessage("确定删除吗？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                remove(holder.getLayoutPosition());
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        holder.setText(R.id.tv, item);
        //绑定ItemView
        mItemManger.bind(holder.itemView, holder.getAdapterPosition());
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        //返回FCSwipeLayout在xml文件里的id
        return R.id.swipe_layout;
    }
}
