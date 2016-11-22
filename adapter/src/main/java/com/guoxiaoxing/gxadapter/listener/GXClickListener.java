package com.guoxiaoxing.gxadapter.listener;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.guoxiaoxing.gxadapter.GXAdapter;
import com.guoxiaoxing.gxadapter.holder.FCViewHolder;

import java.util.Iterator;
import java.util.Set;

import static com.guoxiaoxing.gxadapter.GXAdapter.VIEW_TYPE_EMPTY;
import static com.guoxiaoxing.gxadapter.GXAdapter.VIEW_TYPE_FOOTER;
import static com.guoxiaoxing.gxadapter.GXAdapter.VIEW_TYPE_HEADER;
import static com.guoxiaoxing.gxadapter.GXAdapter.VIEW_TYPE_LOADING;

/**
 * This can be useful for applications that wish to implement various forms of click and longclick and childView click
 * manipulation of item views within the RecyclerView. GXClickListener may intercept
 * a touch interaction already in progress even if the GXClickListener is already handling that
 * gesture stream itself for the purposes of scrolling.
 * <p>
 * For more information, you can contact me by guoxiaoxing@souche.com
 *
 * @author guoxiaoxing
 * @since 16/9/18 上午11:19
 */
public abstract class GXClickListener implements RecyclerView.OnItemTouchListener {
    public static String TAG = "GXClickListener";
    protected GXAdapter baseQuickAdapter;
    private GestureDetectorCompat mGestureDetector;
    private RecyclerView recyclerView;
    private Set<Integer> childClickViewIds;
    private Set<Integer> longClickViewIds;
    private boolean mIsPrepressed = false;
    private boolean mIsShowPress = false;
    private View mPressedView = null;

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        if (recyclerView == null) {
            this.recyclerView = rv;
            this.baseQuickAdapter = (GXAdapter) recyclerView.getAdapter();
            mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(), new ItemTouchHelperGestureListener(recyclerView));
        }
        if (!mGestureDetector.onTouchEvent(e) && e.getActionMasked() == MotionEvent.ACTION_UP && mIsShowPress) {
            if (mPressedView != null) {
                mPressedView.setPressed(false);
                mPressedView = null;
            }
            mIsShowPress = false;
            mIsPrepressed = false;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.e(TAG, "onTouchEvent: ");
        mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     *
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     */
    public abstract void onItemClick(GXAdapter adapter, View view, int position);

    /**
     * callback method to be invoked when an item in this view has been
     * click and held
     *
     * @param view     The view whihin the AbsListView that was clicked
     * @param position The position of the view int the adapter
     */
    public abstract void onItemLongClick(GXAdapter adapter, View view, int position);

    public abstract void onItemChildClick(GXAdapter adapter, View view, int position);

    public abstract void onItemChildLongClick(GXAdapter adapter, View view, int position);

    public boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        if (view.getVisibility() != View.VISIBLE) {
            return false;
        }
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getRawX() < x
                || ev.getRawX() > (x + view.getWidth())
                || ev.getRawY() < y
                || ev.getRawY() > (y + view.getHeight())) {
            return false;
        }
        return true;
    }

    private boolean isHeaderOrFooterPosition(int position) {
        /**
         *  have a headview and VIEW_TYPE_EMPTY VIEW_TYPE_FOOTER VIEW_TYPE_LOADING
         */
        int type = baseQuickAdapter.getItemViewType(position);
        return (type == VIEW_TYPE_EMPTY || type == VIEW_TYPE_HEADER || type == VIEW_TYPE_FOOTER || type == VIEW_TYPE_LOADING);
    }

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {

        private RecyclerView recyclerView;

        public ItemTouchHelperGestureListener(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (mIsPrepressed && mPressedView != null) {
                mPressedView.setPressed(true);
                final View pressedView = mPressedView;
                FCViewHolder vh = (FCViewHolder) recyclerView.getChildViewHolder(pressedView);

                if (isHeaderOrFooterPosition(vh.getLayoutPosition())) {
                    return false;
                }
                childClickViewIds = vh.getChildClickViewIds();

                if (childClickViewIds != null && childClickViewIds.size() > 0) {
                    for (Iterator it = childClickViewIds.iterator(); it.hasNext(); ) {
                        View childView = pressedView.findViewById((Integer) it.next());
                        if (inRangeOfView(childView, e) && childView.isEnabled()) {
                            onItemChildClick(baseQuickAdapter, childView, vh.getLayoutPosition() - baseQuickAdapter.getHeaderLayoutCount());
                            resetPressedView(pressedView);
                            return true;
                        }
                    }


                    onItemClick(baseQuickAdapter, pressedView, vh.getLayoutPosition() - baseQuickAdapter.getHeaderLayoutCount());
                } else {
                    onItemClick(baseQuickAdapter, pressedView, vh.getLayoutPosition() - baseQuickAdapter.getHeaderLayoutCount());
                }
                resetPressedView(pressedView);

            }
            return true;
        }

        private void resetPressedView(final View pressedView) {
            if (pressedView != null) {
                pressedView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pressedView != null) {
                            pressedView.setPressed(false);
                        }

                    }
                }, 100);
            }

            mIsPrepressed = false;
            mPressedView = null;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            boolean isChildLongClick = false;
            if (mIsPrepressed && mPressedView != null) {
                FCViewHolder vh = (FCViewHolder) recyclerView.getChildViewHolder(mPressedView);
                if (!isHeaderOrFooterPosition(vh.getLayoutPosition())) {
                    longClickViewIds = vh.getItemChildLongClickViewIds();
                    if (longClickViewIds != null && longClickViewIds.size() > 0) {
                        for (Iterator it = longClickViewIds.iterator(); it.hasNext(); ) {
                            View childView = mPressedView.findViewById((Integer) it.next());
                            if (inRangeOfView(childView, e) && childView.isEnabled()) {
                                onItemChildLongClick(baseQuickAdapter, childView, vh.getLayoutPosition() - baseQuickAdapter.getHeaderLayoutCount());
                                mPressedView.setPressed(true);
                                mIsShowPress = true;
                                isChildLongClick = true;
                                break;
                            }
                        }
                    }
                    if (!isChildLongClick) {
                        onItemLongClick(baseQuickAdapter, mPressedView, vh.getLayoutPosition() - baseQuickAdapter.getHeaderLayoutCount());
                        mPressedView.setPressed(true);
                        mIsShowPress = true;
                    }

                }

            }
        }

        @Override
        public void onShowPress(MotionEvent e) {
            if (mIsPrepressed && mPressedView != null) {
                mPressedView.setPressed(true);
                mIsShowPress = true;
            }
            super.onShowPress(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            mIsPrepressed = true;
            mPressedView = recyclerView.findChildViewUnder(e.getX(), e.getY());
            super.onDown(e);
            return false;
        }


    }
}

