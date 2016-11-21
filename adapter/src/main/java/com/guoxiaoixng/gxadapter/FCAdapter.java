package com.guoxiaoixng.gxadapter;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import com.souche.android.sdk.fcadapter.R;
import com.guoxiaoixng.gxadapter.animation.AlphaAnimation;
import com.guoxiaoixng.gxadapter.animation.BaseAnimation;
import com.guoxiaoixng.gxadapter.animation.ScaleAnimation;
import com.guoxiaoixng.gxadapter.animation.SlideBottomAnimation;
import com.guoxiaoixng.gxadapter.animation.SlideLeftAnimation;
import com.guoxiaoixng.gxadapter.animation.SlideRightAnimation;
import com.guoxiaoixng.gxadapter.fastscroller.FastScroller;
import com.guoxiaoixng.gxadapter.holder.FCViewHolder;
import com.guoxiaoixng.gxadapter.item.IExpandable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * This class is backed by an ArrayList of arbitrary objects of <b>T</b>, where <b>T</b> is
 * your Model object containing the data.
 * This class provides a set of standard methods to handle changes on the data set such as
 * filtering, adding, removing, moving and animating an item.
 * This Adapter supports a set of standard methods for Headers/Sections expand and collapse
 * an Expandable item, to Drag&Drop and Swipe any item.
 * <p>
 * For more information, you can contact me by guoxiaoxing@souche.com
 *
 * @author guoxiaoxing
 * @since 16/9/18 上午10:43
 */
public abstract class FCAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements FastScroller.onCreateBubbleTextListener {

    protected static final String TAG = FCAdapter.class.getSimpleName();

    public static final int VIEW_TYPE_DEFAULT = 0x0000000;
    public static final int VIEW_TYPE_HEADER = 0x00000111;
    public static final int VIEW_TYPE_LOADING = 0x00000222;
    public static final int VIEW_TYPE_FOOTER = 0x00000333;
    public static final int VIEW_TYPE_EMPTY = 0x00000555;

    public static final int ANIMATION_TYPE_ALPHA = 0x00000001;
    public static final int ANIMATION_TYPE_SCALE = 0x00000002;
    public static final int ANIMATION_TYPE_SLIDE_BOTTOM = 0x00000003;
    public static final int ANIMATION_TYPE_SLIDE_LEFT = 0x00000004;
    public static final int ANIMATION_TYPE_SLIDE_RIGHT = 0x00000005;

    @IntDef({ANIMATION_TYPE_ALPHA, ANIMATION_TYPE_SCALE, ANIMATION_TYPE_SLIDE_BOTTOM, ANIMATION_TYPE_SLIDE_LEFT, ANIMATION_TYPE_SLIDE_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    @interface AnimationType {
    }

    public static final int NETWORK_REQUEST_PAGE_SIZE = 10;

    private boolean mLoadMoreEnable = false;
    private boolean mLoadingMoreEnable = false;
    private boolean mFirstOnlyEnable = true;
    private boolean mOpenAnimationEnable = false;
    private boolean mEmptyEnable;
    private boolean mHeaderAndEmptyEnable;
    private boolean mFootAndEmptyEnable;

    private Interpolator mInterpolator = new LinearInterpolator();
    private BaseAnimation mCustomAnimation;
    private BaseAnimation mSelectAnimation = new SlideBottomAnimation();

    private LinearLayout mHeaderLayout;
    private LinearLayout mFooterLayout;
    private LinearLayout mCopyHeaderLayout = null;
    private LinearLayout mCopyFooterLayout = null;

    private View mContentView;
    private View mEmptyView;
    private View mCopyEmptyLayout;
    private View mLoadMoreFailedView;
    private View mLoadNoMoreView;
    private View mLoadingView;

    private OnLoadMoreListener mOnLoadMoreListener;

    protected Context mContext;
    protected int mLayoutResId;
    protected RecyclerView mRecyclerView;
    protected LayoutInflater mLayoutInflater;
    protected List<T> mData;

    private int mDuration = 300;
    private int mLastPosition = -1;
    private int pageSize = -1;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId The layout resource id of each item.
     * @param data        A new list is created out of this one to avoid mutable list
     */
    public FCAdapter(int layoutResId, List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : data;
        if (layoutResId != 0) {
            this.mLayoutResId = layoutResId;
        }


    }

    public FCAdapter(List<T> data) {
        this(0, data);
    }

    public FCAdapter(View contentView, List<T> data) {
        this(0, data);
        mContentView = contentView;
    }

    /*--------------*/
    /* MAIN METHODS */
    /*--------------*/

    @Override
    public FCViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FCViewHolder FCViewHolder = null;
        this.mContext = parent.getContext();
        this.mLayoutInflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case VIEW_TYPE_LOADING:
                FCViewHolder = getLoadingView(parent);
                break;
            case VIEW_TYPE_HEADER:
                FCViewHolder = new FCViewHolder(mHeaderLayout);
                break;
            case VIEW_TYPE_EMPTY:
                FCViewHolder = new FCViewHolder(mEmptyView == mCopyEmptyLayout ? mCopyEmptyLayout : mEmptyView);
                break;
            case VIEW_TYPE_FOOTER:
                FCViewHolder = new FCViewHolder(mFooterLayout);
                break;
            default:
                FCViewHolder = onCreateDefViewHolder(parent, viewType);
        }
        return FCViewHolder;
    }

    /**
     * To bind different types of holder and solve different the bind events
     *
     * @param holder    holder
     * @param positions positions
     * @see #getDefItemViewType(int)
     */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int positions) {
        int viewType = holder.getItemViewType();

        switch (viewType) {
            case VIEW_TYPE_DEFAULT:
                bindData((FCViewHolder) holder, mData.get(holder.getLayoutPosition() - getHeaderLayoutCount()));
                break;
            case VIEW_TYPE_LOADING:
                addLoadMore(holder);
                break;
            case VIEW_TYPE_HEADER:
                break;
            case VIEW_TYPE_EMPTY:
                break;
            case VIEW_TYPE_FOOTER:
                break;
            default:
                bindData((FCViewHolder) holder, mData.get(holder.getLayoutPosition() - getHeaderLayoutCount()));
                break;
        }
    }

    protected FCViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, mLayoutResId);
    }

    protected FCViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {
        if (mContentView == null) {
            return new FCViewHolder(getItemView(layoutResId, parent));
        }
        return new FCViewHolder(mContentView);
    }

    /**
     * Get the number of item that will be created
     *
     * @return item count
     */
    @Override
    public int getItemCount() {

        int i = isLoadMore() ? 1 : 0;

        int count = mData.size() + i + getHeaderLayoutCount() + getFooterLayoutCount();

        if (mData.size() == 0 && mEmptyView != null) {
            /**
             *  setEmptyView(false) and add emptyView
             */
            if (count == 0 && (!mHeaderAndEmptyEnable || !mFootAndEmptyEnable)) {
                count += getEmptyViewCount();
                /**
                 * {@link #setEmptyView(true, true, View)}
                 */
            } else if (mHeaderAndEmptyEnable || mFootAndEmptyEnable) {
                count += getEmptyViewCount();
            }

            if ((mHeaderAndEmptyEnable && getHeaderLayoutCount() == 1 && count == 1) || count == 0) {
                mEmptyEnable = true;
                count += getEmptyViewCount();
            }

        }
        return count;
    }

    /**
     * Get the type of View that will be created by {@link #getItemView(int, ViewGroup)} for the specified item.
     *
     * @param position The position of the item within the adapter's data set whose view type we
     *                 want.
     * @return An integer representing the type of View. Two views should share the same type if one
     * can be converted to the other in {@link #getItemView(int, ViewGroup)}. Note: Integers must be in the
     * range 0 to {@link #getItemCount()} - 1.
     */
    @Override
    public int getItemViewType(int position) {

        //if set headView and position = 0
        if (mHeaderLayout != null && position == 0) {
            return VIEW_TYPE_HEADER;
        }

        //if user has no data and add emptyView and position < 2(HeaderView + EmptyView)
        if (mData.size() == 0 && mEmptyEnable && mEmptyView != null && position < 2) {
            /**
             * if set {@link #setEmptyView(boolean, boolean, View)}  position = 1
             */
            if ((mHeaderAndEmptyEnable || mFootAndEmptyEnable) && position == 1) {
                /**
                 * if user want to show HeaderView and FooterView and EmptyView but not add HeaderView
                 */
                if (mHeaderLayout == null && mFooterLayout != null) {
                    return VIEW_TYPE_FOOTER;
                    /**
                     * add headview
                     */
                } else if (mHeaderLayout != null) {
                    return VIEW_TYPE_EMPTY;
                }
            } else if (position == 0) {
                /**
                 * has no emptyView just add emptyview
                 */
                if (mHeaderLayout == null) {
                    return VIEW_TYPE_EMPTY;
                } else if (mFooterLayout != null)

                    return VIEW_TYPE_EMPTY;


            } else if (position == 2 && (mFootAndEmptyEnable || mHeaderAndEmptyEnable) && mHeaderLayout != null && mEmptyView != null) {
                return VIEW_TYPE_FOOTER;

            }
            //User forget to set {@link #setEmptyView(boolean, boolean, View)}  but add FooterView and HeaderView and EmptyView
            else if ((!mFootAndEmptyEnable || !mHeaderAndEmptyEnable) && position == 1 && mFooterLayout != null) {
                return VIEW_TYPE_FOOTER;
            }
        } else if (mData.size() == 0 && mEmptyView != null && getItemCount() == (mHeaderAndEmptyEnable ? 2 : 1) && mEmptyEnable) {
            return VIEW_TYPE_EMPTY;
        } else if (position == mData.size() + getHeaderLayoutCount()) {
            if (mLoadMoreEnable)
                return VIEW_TYPE_LOADING;
            else
                return VIEW_TYPE_FOOTER;
        } else if (position > mData.size() + getHeaderLayoutCount()) {
            return VIEW_TYPE_FOOTER;
        }
        return getDefItemViewType(position - getHeaderLayoutCount());
    }

    protected int getDefItemViewType(int position) {
        return super.getItemViewType(position);
    }

    /**
     * @param layoutResId ID for an XML layout resource to load
     * @param parent      Optional view to be the parent of the generated hierarchy or else simply an object that
     *                    provides a set of LayoutParams values for root of the returned
     *                    hierarchy
     * @return view will be return
     */
    protected View getItemView(int layoutResId, ViewGroup parent) {
        return mLayoutInflater.inflate(layoutResId, parent, false);
    }

    /**
     * Implement this method and use the holder to adapt the view to the given item.
     *
     * @param holder A fully initialized holder.
     * @param item   The item that needs to be displayed.
     */
    protected abstract void bindData(FCViewHolder holder, T item);

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*-------------------------------*/
    /* PULL TO REFRESH AND LOAD MORE */
    /*-------------------------------*/

    /**
     * setting up a new instance to data;
     *
     * @param data new data
     */
    public void onRefreshSuccess(List<T> data) {
        if (data == null) {
            mData.clear();
        } else {
            this.mData = data;
        }
        if (mOnLoadMoreListener != null) {
            mLoadMoreEnable = true;
            // mFooterLayout = null;
        }
        if (mLoadMoreFailedView != null) {
            removeFooterView(mLoadMoreFailedView);
        }
        mLastPosition = -1;
        notifyDataSetChanged();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
        openLoadMore(NETWORK_REQUEST_PAGE_SIZE);
    }

    /**
     * Load more success
     *
     * @param newData newData
     */
    public void onLoadMoreSucess(List<T> newData) {
        this.mData.addAll(newData);
        if (mLoadMoreEnable) {
            mLoadingMoreEnable = false;
        }
        if (newData.size() < NETWORK_REQUEST_PAGE_SIZE) {
            showLoadNoMoreView();
        }
        notifyItemRangeChanged(mData.size() - newData.size() + getHeaderLayoutCount(), newData.size());
    }

    /**
     * Load more failed
     */
    public void onLoadMoreFailed() {
        if (mLoadMoreEnable) {
            mLoadingMoreEnable = false;
        }
        showLoadMoreFailedView();
    }

    /**
     * when adapter's data size than pageSize and enable is true,the loading more function is enable,or disable
     *
     * @param pageSize pageSize
     */
    private void openLoadMore(int pageSize) {
        this.pageSize = pageSize;
        mLoadMoreEnable = true;

    }

    public void showLoadNoMoreView() {
        loadComplete();
        if (mLoadNoMoreView == null) {
            mLoadNoMoreView = mLayoutInflater.inflate(R.layout.baselib_load_no_more, null);
        }
        addFooterView(mLoadNoMoreView);
    }

    /**
     * Call this method when load more failed.
     */
    private void showLoadMoreFailedView() {
        loadComplete();
        if (mLoadMoreFailedView == null) {
            mLoadMoreFailedView = mLayoutInflater.inflate(R.layout.baselib_load_more_failed, null);
            mLoadMoreFailedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeFooterView(mLoadMoreFailedView);
                    openLoadMore(pageSize);
                }
            });
        }
        addFooterView(mLoadMoreFailedView);
    }

    private void addLoadMore(RecyclerView.ViewHolder holder) {
        if (isLoadMore() && !mLoadingMoreEnable) {
            mLoadingMoreEnable = true;
            mOnLoadMoreListener.onLoadMore();
        }
    }

    public void loadComplete() {
        mLoadMoreEnable = false;
        mLoadingMoreEnable = false;
        this.notifyItemChanged(getItemCount());
    }

    /**
     * @return Whether the Adapter is actively showing load
     * progress.
     */
    public boolean isLoading() {
        return mLoadingMoreEnable;
    }

    /**
     * Determine whether it is loaded more
     *
     * @return is load more
     */
    private boolean isLoadMore() {
        return mLoadMoreEnable && pageSize != -1 && mOnLoadMoreListener != null && mData.size() >= pageSize;
    }


    /*----------------*/
    /* DATA OPERATION */
    /*----------------*/

    /**
     * same as onLoadMoreSucess(List<T>) but for when data is manually added to the adapter
     */
    public void dataAdded() {
        if (mLoadMoreEnable) {
            mLoadingMoreEnable = false;
        }
        notifyDataSetChanged();
    }

    /**
     * return the value of pageSize
     *
     * @return
     */
    public int getPageSize() {
        return this.pageSize;
    }

    /**
     * remove the item associated with the specified position of adapter
     *
     * @param position
     */
    public void remove(int position) {
        mData.remove(position);
        notifyItemRemoved(position + getHeaderLayoutCount());
    }

    /**
     * insert  a item associated with the specified position of adapter
     *
     * @param position
     * @param item
     */
    public void add(int position, T item) {
        mData.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * additional data;
     *
     * @param newData
     */
    public void addData(List<T> newData) {
        this.mData.addAll(newData);
        if (mLoadMoreEnable) {
            mLoadingMoreEnable = false;
        }
        notifyItemRangeChanged(mData.size() - newData.size() + getHeaderLayoutCount(), newData.size());
    }


    /**
     * Get the data of list
     *
     * @return data
     */
    public List<T> getData() {
        return mData;
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    public T getItem(int position) {
        return mData.get(position);
    }

    /**
     * if addHeaderView will be return 1, if not will be return 0
     */
    public int getHeaderLayoutCount() {
        return mHeaderLayout == null ? 0 : 1;
    }

    /**
     * if addFooterView will be return 1, if not will be return 0
     */
    public int getFooterLayoutCount() {
        return mFooterLayout == null ? 0 : 1;
    }

    /**
     * if mEmptyView will be return 1 or not will be return 0
     *
     * @return
     */
    public int getEmptyViewCount() {
        return mEmptyView == null ? 0 : 1;
    }


    private FCViewHolder getLoadingView(ViewGroup parent) {
        if (mLoadingView == null) {
            return createBaseViewHolder(parent, R.layout.baselib_loading_more);
        }
        return new FCViewHolder(mLoadingView);
    }

    /**
     * Called when a view created by this adapter has been attached to a window.
     * simple to solve item will layout using all
     * {@link #setFullSpan(RecyclerView.ViewHolder)}
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int type = holder.getItemViewType();
        if (type == VIEW_TYPE_EMPTY || type == VIEW_TYPE_HEADER || type == VIEW_TYPE_FOOTER || type == VIEW_TYPE_LOADING) {
            setFullSpan(holder);
        } else {
            addAnimation(holder);
        }
    }

    /**
     * When set to true, the item will layout using all span area. That means, if orientation
     * is vertical, the view will have full width; if orientation is horizontal, the view will
     * have full height.
     * if the hold view use StaggeredGridLayoutManager they should using all span area
     *
     * @param holder True if this item should traverse all spans.
     */
    protected void setFullSpan(RecyclerView.ViewHolder holder) {
        if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            params.setFullSpan(true);
        }
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    if (mSpanSizeLookup == null)
                        return (type == VIEW_TYPE_EMPTY || type == VIEW_TYPE_HEADER || type == VIEW_TYPE_FOOTER || type == VIEW_TYPE_LOADING) ? gridManager.getSpanCount() : 1;
                    else
                        return (type == VIEW_TYPE_EMPTY || type == VIEW_TYPE_HEADER || type == VIEW_TYPE_FOOTER || type == VIEW_TYPE_LOADING) ? gridManager.getSpanCount() : mSpanSizeLookup.getSpanSize(gridManager, position - getHeaderLayoutCount());
                }
            });
        }
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (mOnLoadMoreListener != null && pageSize == -1) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    int visibleItemCount = layoutManager.getChildCount();
                    Log.e("visibleItemCount", visibleItemCount + "");
                    openLoadMore(visibleItemCount);
                }
            }
        });

    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRecyclerView = null;
    }

    private boolean flag = true;
    private SpanSizeLookup mSpanSizeLookup;

    public interface SpanSizeLookup {
        int getSpanSize(GridLayoutManager gridLayoutManager, int position);
    }

    /**
     * @param spanSizeLookup instance to be used to query number of spans occupied by each item
     */
    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        this.mSpanSizeLookup = spanSizeLookup;
    }

    /*-------------------*/
    /* HEADER AND FOOTER */
    /*-------------------*/

    /**
     * Return root layout of header
     */
    public LinearLayout getHeaderLayout() {
        return mHeaderLayout;
    }

    /**
     * Return root layout of footer
     */
    public LinearLayout getFooterLayout() {
        return mFooterLayout;
    }

    /**
     * Append header to the rear of the mHeaderLayout.
     *
     * @param header
     */
    public void addHeaderView(View header) {
        addHeaderView(header, -1);
    }

    /**
     * Add header view to mHeaderLayout and set header view position in mHeaderLayout.
     * When index = -1 or index >= child count in mHeaderLayout,
     * the effect of this method is the same as that of {@link #addHeaderView(View)}.
     *
     * @param header
     * @param index  the position in mHeaderLayout of this header.
     *               When index = -1 or index >= child count in mHeaderLayout,
     *               the effect of this method is the same as that of {@link #addHeaderView(View)}.
     */
    public void addHeaderView(View header, int index) {
        if (mHeaderLayout == null) {
            if (mCopyHeaderLayout == null) {
                mHeaderLayout = new LinearLayout(header.getContext());
                mHeaderLayout.setOrientation(LinearLayout.VERTICAL);
                mHeaderLayout.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
                mCopyHeaderLayout = mHeaderLayout;
            } else {
                mHeaderLayout = mCopyHeaderLayout;
            }
        }
        index = index >= mHeaderLayout.getChildCount() ? -1 : index;
        mHeaderLayout.addView(header, index);
        this.notifyDataSetChanged();
    }

    /**
     * Append footer to the rear of the mFooterLayout.
     *
     * @param footer footer
     */
    public void addFooterView(View footer) {
        addFooterView(footer, -1);
    }

    /**
     * Add footer view to mFooterLayout and set footer view position in mFooterLayout.
     * When index = -1 or index >= child count in mFooterLayout,
     * the effect of this method is the same as that of {@link #addFooterView(View)}.
     *
     * @param footer footer
     * @param index  the position in mFooterLayout of this footer.
     *               When index = -1 or index >= child count in mFooterLayout,
     *               the effect of this method is the same as that of {@link #addFooterView(View)}.
     */
    public void addFooterView(View footer, int index) {
        mLoadMoreEnable = false;
        if (mFooterLayout == null) {
            if (mCopyFooterLayout == null) {
                mFooterLayout = new LinearLayout(footer.getContext());
                mFooterLayout.setOrientation(LinearLayout.VERTICAL);
                mFooterLayout.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
                mCopyFooterLayout = mFooterLayout;
            } else {
                mFooterLayout = mCopyFooterLayout;
            }
        }
        index = index >= mFooterLayout.getChildCount() ? -1 : index;
        mFooterLayout.addView(footer, index);
        this.notifyItemChanged(getItemCount());
    }

    /**
     * remove header view from mHeaderLayout.
     * When the child count of mHeaderLayout is 0, mHeaderLayout will be set to null.
     *
     * @param header header
     */
    public void removeHeaderView(View header) {
        if (mHeaderLayout == null) return;

        mHeaderLayout.removeView(header);
        if (mHeaderLayout.getChildCount() == 0) {
            mHeaderLayout = null;
        }
        this.notifyDataSetChanged();
    }

    /**
     * remove footer view from mFooterLayout,
     * When the child count of mFooterLayout is 0, mFooterLayout will be set to null.
     *
     * @param footer footer
     */
    public void removeFooterView(View footer) {
        if (mFooterLayout == null) return;

        mFooterLayout.removeView(footer);
        if (mFooterLayout.getChildCount() == 0) {
            mFooterLayout = null;
        }
        this.notifyDataSetChanged();
    }

    /**
     * remove all header view from mHeaderLayout and set null to mHeaderLayout
     */
    public void removeAllHeaderView() {
        if (mHeaderLayout == null) return;

        mHeaderLayout.removeAllViews();
        mHeaderLayout = null;
    }

    /**
     * remove all footer view from mFooterLayout and set null to mFooterLayout
     */
    public void removeAllFooterView() {
        if (mFooterLayout == null) return;

        mFooterLayout.removeAllViews();
        mFooterLayout = null;
    }

    /*------------*/
    /* EMPTY VIEW */
    /*------------*/

    /**
     * Sets the view to show if the adapter is empty
     */
    public void setEmptyView(View emptyView) {
        setEmptyView(false, false, emptyView);
    }

    /**
     * @param isHeadAndEmpty false will not show headView if the data is empty true will show emptyView and headView
     * @param emptyView
     */
    public void setEmptyView(boolean isHeadAndEmpty, View emptyView) {
        setEmptyView(isHeadAndEmpty, false, emptyView);
    }

    /**
     * set emptyView show if adapter is empty and want to show headview and footview
     *
     * @param isHeadAndEmpty isHeadAndEmpty
     * @param isFootAndEmpty isFootAndEmpty
     * @param emptyView      emptyView
     */
    public void setEmptyView(boolean isHeadAndEmpty, boolean isFootAndEmpty, View emptyView) {
        mHeaderAndEmptyEnable = isHeadAndEmpty;
        mFootAndEmptyEnable = isFootAndEmpty;
        mEmptyView = emptyView;
        if (mCopyEmptyLayout == null) {
            mCopyEmptyLayout = emptyView;
        }
        mEmptyEnable = true;
    }

    /**
     * When the current adapter is empty, the BaseQuickAdapter can display a special view
     * called the empty view. The empty view is used to provide feedback to the user
     * that no data is available in this AdapterView.
     *
     * @return The view to show if the adapter is empty.
     */
    public View getEmptyView() {
        return mEmptyView;
    }

    /*----------------*/
    /* ITEM ANIMATION */
    /*----------------*/

    /**
     * add animation when you want to show time
     *
     * @param holder
     */
    private void addAnimation(RecyclerView.ViewHolder holder) {
        if (mOpenAnimationEnable) {
            if (!mFirstOnlyEnable || holder.getLayoutPosition() > mLastPosition) {
                BaseAnimation animation = null;
                if (mCustomAnimation != null) {
                    animation = mCustomAnimation;
                } else {
                    animation = mSelectAnimation;
                }
                for (Animator anim : animation.getAnimators(holder.itemView)) {
                    startAnim(anim, holder.getLayoutPosition());
                }
                mLastPosition = holder.getLayoutPosition();
            }
        }
    }


    /**
     * set anim to start when loading
     *
     * @param anim
     * @param index
     */
    protected void startAnim(Animator anim, int index) {
        anim.setDuration(mDuration).start();
        anim.setInterpolator(mInterpolator);
    }

    /**
     * Set the view animation type.
     *
     * @param animationType One of {@link #ANIMATION_TYPE_ALPHA}, {@link #ANIMATION_TYPE_SCALE}, {@link #ANIMATION_TYPE_SLIDE_BOTTOM}, {@link #ANIMATION_TYPE_SLIDE_LEFT}, {@link #ANIMATION_TYPE_SLIDE_RIGHT}.
     */
    public void openLoadAnimation(@AnimationType int animationType) {
        this.mOpenAnimationEnable = true;
        mCustomAnimation = null;
        switch (animationType) {
            case ANIMATION_TYPE_ALPHA:
                mSelectAnimation = new AlphaAnimation();
                break;
            case ANIMATION_TYPE_SCALE:
                mSelectAnimation = new ScaleAnimation();
                break;
            case ANIMATION_TYPE_SLIDE_BOTTOM:
                mSelectAnimation = new SlideBottomAnimation();
                break;
            case ANIMATION_TYPE_SLIDE_LEFT:
                mSelectAnimation = new SlideLeftAnimation();
                break;
            case ANIMATION_TYPE_SLIDE_RIGHT:
                mSelectAnimation = new SlideRightAnimation();
                break;
            default:
                break;
        }
    }

    /**
     * Set Custom ObjectAnimator
     *
     * @param animation ObjectAnimator
     */
    public void openLoadAnimation(BaseAnimation animation) {
        this.mOpenAnimationEnable = true;
        this.mCustomAnimation = animation;
    }

    /**
     * To open the animation when loading
     */
    public void openLoadAnimation() {
        this.mOpenAnimationEnable = true;
    }

    /**
     * {@link #addAnimation(RecyclerView.ViewHolder)}
     *
     * @param firstOnly true just show anim when first loading false show anim when load the data every time
     */
    public void isFirstOnly(boolean firstOnly) {
        this.mFirstOnlyEnable = firstOnly;
    }

    /*---------------------*/
    /* EXPAND AND COLLAPSE */
    /*---------------------*/

    private int recursiveExpand(int position, @NonNull List list) {
        int count = 0;
        int pos = position + list.size() - 1;
        for (int i = list.size() - 1; i >= 0; i--, pos--) {
            if (list.get(i) instanceof IExpandable) {
                IExpandable item = (IExpandable) list.get(i);
                if (item.isExpanded() && hasSubItems(item)) {
                    List subList = item.getSubItems();
                    mData.addAll(pos + 1, subList);
                    int subItemCount = recursiveExpand(pos + 1, subList);
                    count += subItemCount;
                }
            }
        }
        return count;
    }

    /**
     * Expand an expandable item
     *
     * @param position     position of the item
     * @param animate      expand items with animation
     * @param shouldNotify notify the RecyclerView to rebind items, <strong>false</strong> if you want to do it yourself.
     * @return the number of items that have been added.
     */
    public int expand(@IntRange(from = 0) int position, boolean animate, boolean shouldNotify) {
        position -= getHeaderLayoutCount();

        IExpandable expandable = getExpandableItem(position);
        if (expandable == null) {
            return 0;
        }
        if (!hasSubItems(expandable)) {
            expandable.setExpanded(false);
            return 0;
        }
        int subItemCount = 0;
        if (!expandable.isExpanded()) {
            List list = expandable.getSubItems();
            mData.addAll(position + 1, list);
            subItemCount += recursiveExpand(position + 1, list);

            expandable.setExpanded(true);
            subItemCount += list.size();
        }
        int parentPos = position + getHeaderLayoutCount();
        if (shouldNotify) {
            if (animate) {
                notifyItemChanged(parentPos);
                notifyItemRangeInserted(parentPos + 1, subItemCount);
            } else {
                notifyDataSetChanged();
            }
        }
        return subItemCount;
    }

    /**
     * Expand an expandable item
     *
     * @param position position of the item, which includes the header layout count.
     * @param animate  expand items with animation
     * @return the number of items that have been added.
     */
    public int expand(@IntRange(from = 0) int position, boolean animate) {
        return expand(position, animate, true);
    }

    /**
     * Expand an expandable item with animation.
     *
     * @param position position of the item, which includes the header layout count.
     * @return the number of items that have been added.
     */
    public int expand(@IntRange(from = 0) int position) {
        return expand(position, true, true);
    }

    public int expandAll(int position, boolean animate, boolean notify) {
        position -= getHeaderLayoutCount();

        T endItem = null;
        if (position + 1 < getItemCount()) {
            endItem = getItem(position + 1);
        }

        IExpandable expandable = getExpandableItem(position);
        if (!hasSubItems(expandable)) {
            return 0;
        }

        int count = expand(position + getHeaderLayoutCount(), false, false);
        for (int i = position + 1; i < getItemCount(); i++) {
            T item = getItem(i);

            if (item == endItem) {
                break;
            }
            if (isExpandable(item)) {
                count += expand(i + getHeaderLayoutCount(), false, false);
            }
        }

        if (notify) {
            if (animate) {
                notifyItemRangeInserted(position + getHeaderLayoutCount() + 1, count);
            } else {
                notifyDataSetChanged();
            }
        }
        return count;
    }

    /**
     * expand the item and all its subItems
     *
     * @param position position of the item, which includes the header layout count.
     * @param init     whether you are initializing the recyclerView or not.
     *                 if <strong>true</strong>, it won't notify recyclerView to redraw UI.
     * @return the number of items that have been added to the adapter.
     */
    public int expandAll(int position, boolean init) {
        return expandAll(position, true, !init);
    }

    private int recursiveCollapse(@IntRange(from = 0) int position) {
        T item = getItem(position);
        if (!isExpandable(item)) {
            return 0;
        }
        IExpandable expandable = (IExpandable) item;
        int subItemCount = 0;
        if (expandable.isExpanded()) {
            List<T> subItems = expandable.getSubItems();
            for (int i = subItems.size() - 1; i >= 0; i--) {
                T subItem = subItems.get(i);
                int pos = getItemPosition(subItem);
                if (pos < 0) {
                    continue;
                }
                if (subItem instanceof IExpandable) {
                    subItemCount += recursiveCollapse(pos);
                }
                mData.remove(pos);
                subItemCount++;
            }
        }
        return subItemCount;
    }

    /**
     * Collapse an expandable item that has been expanded..
     *
     * @param position the position of the item, which includes the header layout count.
     * @param animate  collapse with animation or not.
     * @param notify   notify the recyclerView refresh UI or not.
     * @return the number of subItems collapsed.
     */
    public int collapse(@IntRange(from = 0) int position, boolean animate, boolean notify) {
        position -= getHeaderLayoutCount();

        IExpandable expandable = getExpandableItem(position);
        if (expandable == null) {
            return 0;
        }
        int subItemCount = recursiveCollapse(position);
        expandable.setExpanded(false);
        int parentPos = position + getHeaderLayoutCount();
        if (notify) {
            if (animate) {
                notifyItemChanged(parentPos);
                notifyItemRangeRemoved(parentPos + 1, subItemCount);
            } else {
                notifyDataSetChanged();
            }
        }
        return subItemCount;
    }

    /**
     * Collapse an expandable item that has been expanded..
     *
     * @param position the position of the item, which includes the header layout count.
     * @return the number of subItems collapsed.
     */
    public int collapse(@IntRange(from = 0) int position) {
        return collapse(position, true, true);
    }

    /**
     * Collapse an expandable item that has been expanded..
     *
     * @param position the position of the item, which includes the header layout count.
     * @return the number of subItems collapsed.
     */
    public int collapse(@IntRange(from = 0) int position, boolean animate) {
        return collapse(position, animate, true);
    }

    private int getItemPosition(T item) {
        return item != null && mData != null && !mData.isEmpty() ? mData.indexOf(item) : -1;
    }

    private boolean hasSubItems(IExpandable item) {
        List list = item.getSubItems();
        return list != null && list.size() > 0;
    }

    private boolean isExpandable(T item) {
        return item != null && item instanceof IExpandable;
    }

    private IExpandable getExpandableItem(int position) {
        T item = getItem(position);
        if (isExpandable(item)) {
            return (IExpandable) item;
        } else {
            return null;
        }
    }

    @Override
    public String onCreateBubbleText(int pos) {
        return null;
    }
}
