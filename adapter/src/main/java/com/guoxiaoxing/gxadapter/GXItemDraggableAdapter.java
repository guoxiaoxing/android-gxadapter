package com.guoxiaoxing.gxadapter;

import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.guoxiaoxing.gxadapter.callback.ItemTouchHelperCallBack;
import com.guoxiaoxing.gxadapter.holder.GXViewHolder;
import com.guoxiaoxing.gxadapter.listener.GXItemDragListener;
import com.guoxiaoxing.gxadapter.listener.GXItemSwipeListener;
import com.guoxiaoxing.gxadapter.swipe.GXSwipeLayout;
import com.guoxiaoxing.gxadapter.swipe.impl.SwipeItemMangerImpl;
import com.guoxiaoxing.gxadapter.swipe.interfaces.SwipeAdapterInterface;
import com.guoxiaoxing.gxadapter.swipe.interfaces.SwipeItemMangerInterface;
import com.guoxiaoxing.gxadapter.swipe.util.Attributes;

import java.util.Collections;
import java.util.List;

/**
 * For more information, you can contact me by guoxiaoxing@souche.com
 *
 * @author guoxiaoxing
 * @since 16/9/19 下午2:58
 */
public abstract class GXItemDraggableAdapter<T> extends GXAdapter<T> implements ItemTouchHelperCallBack.AdapterCallBack,
        SwipeItemMangerInterface, SwipeAdapterInterface {

    public SwipeItemMangerImpl mItemManger = new SwipeItemMangerImpl(this);

    public ItemTouchHelper mItemTouchHelper;
    public ItemTouchHelper.Callback mCallback;
    private GXItemDragListener mGXItemDragListener;
    private GXItemSwipeListener mGXItemSwipeListener;
    private ItemTouchHelperCallBack mItemTouchHelperCallBack;

    public GXItemDraggableAdapter(View contentView, List<T> data) {
        super(contentView, data);
    }

    public GXItemDraggableAdapter(List<T> data) {
        super(data);
    }

    public GXItemDraggableAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    public void setGXItemSwipeListener(GXItemSwipeListener GXItemSwipeListener) {
        mGXItemSwipeListener = GXItemSwipeListener;
    }

    public void setGXItemDragListener(GXItemDragListener GXItemDragListener) {
        mGXItemDragListener = GXItemDragListener;
    }

    @Override
    protected void bindData(GXViewHolder holder, T item) {
        mItemManger.bind(holder.itemView, holder.getAdapterPosition());
    }

    /**
     * Returns whether ItemTouchHelper should start a drag and drop operation if an item is
     * long pressed.<p>
     * Default value is false.
     *
     * @return true if ItemTouchHelper should start dragging an item when it is long pressed,
     * false otherwise. Default value is false.
     * @since 5.0.0-b1
     */
    public boolean isLongPressDragEnabled() {
        return mItemTouchHelperCallBack != null && mItemTouchHelperCallBack.isLongPressDragEnabled();
    }

    /**
     * Enable the Drag on LongPress on the entire ViewHolder.
     * <p><b>NOTE:</b> This will skip LongClick on the view in order to handle the LongPress,
     * however the LongClick listener will be called if necessary in the new
     * Default value is false.
     *
     * @param longPressDragEnabled true to activate, false otherwise
     * @return this Adapter, so the call can be chained
     * @since 5.0.0-b1
     */
    public final void setLongPressDragEnabled(boolean longPressDragEnabled) {
        setupItemTouchHelper();
        mItemTouchHelperCallBack.setLongPressDragEnabled(longPressDragEnabled);
    }

    private void setupItemTouchHelper() {
        if (mItemTouchHelper == null) {
            if (mRecyclerView == null) {
                throw new IllegalStateException("RecyclerView cannot be null. Enabling LongPressDrag or Swipe must be done after the Adapter is added to the RecyclerView.");
            }
            mItemTouchHelperCallBack = new ItemTouchHelperCallBack(this);
            mItemTouchHelper = new ItemTouchHelper(mItemTouchHelperCallBack);
            mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        }
    }

    /**
     * Returns whether ItemTouchHelper should start a swipe operation if a pointer is swiped
     * over the View.
     * <p>Default value is false.</p>
     *
     * @return true if ItemTouchHelper should start swiping an item when user swipes a pointer
     * over the View, false otherwise. Default value is false.
     * @since 5.0.0-b1
     */
    public final boolean isSwipeEnabled() {
        return mItemTouchHelperCallBack != null && mItemTouchHelperCallBack.isItemViewSwipeEnabled();
    }

    /**
     * Enable the Full Swipe of the items.
     * <p>Default value is false.</p>
     *
     * @param swipeEnabled true to activate, false otherwise
     * @return this Adapter, so the call can be chained
     * @since 5.0.0-b1
     */
    public final void setItemViewSwipeEnabled(boolean swipeEnabled) {
        setupItemTouchHelper();
        mItemTouchHelperCallBack.setItemViewSwipeEnabled(swipeEnabled);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        swapItems(fromPosition, toPosition);
        if (mGXItemDragListener != null) {
            mGXItemDragListener.onItemMove(fromPosition, toPosition);
        }
        return false;
    }

    /**
     * Swaps the elements of list at indices fromPosition and toPosition and notify the change.
     * <p>Selection of swiped elements is automatically updated.</p>
     *
     * @param fromPosition previous position of the item.
     * @param toPosition   new position of the item.
     * @since 5.0.0-b7
     */
    private void swapItems(int fromPosition, int toPosition) {
        if (fromPosition < 0 || fromPosition >= getItemCount() ||
                toPosition < 0 || toPosition >= getItemCount()) {
            return;
        }
        //Perform item swap
        Collections.swap(getData(), fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        remove(position);
        if(mGXItemSwipeListener != null){
            mGXItemSwipeListener.onItemDismiss(position);
        }
    }

    @Override
    public void notifyDatasetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void openItem(int position) {
        mItemManger.openItem(position);
    }

    @Override
    public void closeItem(int position) {
        mItemManger.closeItem(position);
    }

    @Override
    public void closeAllExcept(GXSwipeLayout layout) {
        mItemManger.closeAllExcept(layout);
    }

    @Override
    public void closeAllItems() {
        mItemManger.closeAllItems();
    }

    @Override
    public List<Integer> getOpenItems() {
        return mItemManger.getOpenItems();
    }

    @Override
    public List<GXSwipeLayout> getOpenLayouts() {
        return mItemManger.getOpenLayouts();
    }

    @Override
    public void removeShownLayouts(GXSwipeLayout layout) {
        mItemManger.removeShownLayouts(layout);
    }

    @Override
    public boolean isOpen(int position) {
        return mItemManger.isOpen(position);
    }

    @Override
    public Attributes.Mode getMode() {
        return mItemManger.getMode();
    }

    @Override
    public void setMode(Attributes.Mode mode) {
        mItemManger.setMode(mode);
    }
}
