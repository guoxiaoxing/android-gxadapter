# FCAdapter User Guide

FCAdapter是一个轻巧的RecyclerView.Adapter封装库, 提供如下功能:

1. 下拉刷新、上拉加载
2. 头布局、尾布局与空布局
3. 多类型布局
4. 分隔条
5. 侧滑删除、拖拽排序、侧滑菜单
6. 分组展开与折叠
7. 快速滑动、索引滑动
8. Sticky Header

注: 所有用法均有demo演示, 建议先安装demo, 边操作, 边看用法。

##版本更迭:

1.0.0-SNAPSHOT -- 初始版本

1.0.1-SNAPSHOT -- 优化下拉刷新方式

1.0.2-SNAPSHOT -- 优化上拉加载方式

1.0.3-SNAPSHOT -- 添加侧滑菜单

##引用方式: 

```
compile 'com.souche.android.sdk:fcadapter:1.0.3-SNAPSHOT' 
```

已经引用到baselib, 可以直接使用。

# 一 基本用法

FCAdapter设计的用处之一就是最大限度的减少重复的代码, 使用步骤如下: 

1. 继承FCAdapter基类, 泛型为List的数据类型。

2. 重写bindData()方法, 完成数据的绑定即可。FCViewHolder提供常用操作View的方法。

注: position通过viewHolder.getAdapterPosition()获得

```java
public class Adapter extends FCAdapter<Status> {
    public FCAdapter() {
        super(R.layout.tweet, DataServer.getSampleData());
    }

    @Override
    protected void bindData(FCViewHolder viewHolder, Status item) {
        viewHolder.setText(R.id.tweetName, item.getUserName())
                .setText(R.id.tweetText, item.getText())
                .setText(R.id.tweetDate, item.getCreatedAt())
                .setVisible(R.id.tweetRT, item.isRetweet())
                .linkify(R.id.tweetText);
                 Glide.with(mContext).load(item.getUserAvatar()).crossFade().into((ImageView) helper.getView(R.id.iv));
    }
}
```

如果有自定义控件或者viewHolder没有提供的方法(如: SimpleDraweeView)，就可以通过getView方法来实现获取控件，然后进行的操作。可以用以下方式:

```java
SimpleDraweeView carPicture = fcViewHolder.getView(R.id.stock_detail_car_picture);
String uri = itemsBean.getPicUrl();
  if (TextUtils.isEmpty(uri)) {
      carPicture.setImageURI(String.valueOf(uri));
  }
```


# 三 添加Item事件

Item的点击事件

通过mRecyclerView.addOnItemTouchListener,传入FCItemClickListener。

```java
mRecyclerView.addOnItemTouchListener(new FCItemClickListener( ){
            @Override
            public void SimpleOnItemClick(FCAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();

            }
        });  
```

Item的长按事件

通过mRecyclerView.addOnItemTouchListener,传入FCItemLongClickListener。

```java
mRecyclerView.addOnItemTouchListener(new FCItemLongClickListener( ) {
            @Override
            public void SimpleOnItemLongClick(FCAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();

            }
        });
```

Item子控件的点击事件

在adapter的bindData方法里面通过viewHolder.addOnClickListener绑定一下的控件id

```java
 @Override
    protected void bindData(BaseViewHolder viewHolder, Status item) {
        viewHolder.setText(R.id.tweetName, item.getUserName())
                .setText(R.id.tweetText, item.getText())
                .setText(R.id.tweetDate, item.getCreatedAt())
                .setVisible(R.id.tweetRT, item.isRetweet())
                .addOnClickListener(R.id.tweetAvatar)
                .addOnClickListener(R.id.tweetName)
                .linkify(R.id.tweetText);

    }
    
```

在Activity的mRecyclerView.addOnItemTouchListener,传入FCItemChildClickListener。

```java
   mRecyclerView.addOnItemTouchListener(new FCItemChildClickListener( ) {
            @Override
            public void SimpleOnItemChildClick(FCAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();

            }
        });
        
```

Item子控件的长按事件

在adapter的bindData方法里面通过viewHolder.addOnLongClickListener绑定一下的控件id

```
 @Override
    protected void bindData(BaseViewHolder helper, Status item) {
        helper.setText(R.id.tweetName, item.getUserName())
                .setText(R.id.tweetText, item.getText())
                .setText(R.id.tweetDate, item.getCreatedAt())
                .setVisible(R.id.tweetRT, item.isRetweet())
                .addOnLongClickListener(R.id.tweetText)
                .linkify(R.id.tweetText);

    }
```

在Activity的mRecyclerView.addOnItemTouchListener,传入FCItemChildLongClickListener。

```java
 mRecyclerView.addOnItemTouchListener(new FCItemChildLongClickListener( ) {
            @Override
            public void SimpleOnItemChildLongClick(FCAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }
        });
```

如果添加了多种不同的Item事件

重写以下任意方法

```java
mRecyclerView.addOnItemTouchListener(new FCClickListener() {
            @Override
            public void onItemClick(FCAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(FCAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemChildClick(FCAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemChildLongClick(FCAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }
        });     
```

这里采用了缺省适配器模式

# 三 下拉刷新、上拉加载

FCAdapter提供了下拉刷新成功后的刷新功能, 如下所示:

```java
mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFCAdapter.onRefreshSuccess(DataServer.getSampleData(0));
                mSwipeRefreshLayout.setRefreshing(false);
                isErr = false;
            }
        }, delayMillis);
    }
});


```

FCAdapter提供了上拉加载失败重试, 加载成功后新数据添加, 如下所示:

注: FCAdapter默认上拉加载的pageSize为10, 所以当接口请求回的数据size<10, 则判定所有数据已经加载完, 自动显示"别拽了, 到底啦~"

```java
mFCAdapter.setOnLoadMoreListener(new FCAdapter.OnLoadMoreListener() {
    @Override
    public void onLoadMore() {
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (isErr) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mFCAdapter.getData().size() <= LIST_MAX_SIZE) {
                                mFCAdapter.onLoadMoreSucess(DataServer.getSampleData(NETWORK_REQUEST_PAGE_SIZE));
                            } else {
                                mFCAdapter.onLoadMoreSucess(DataServer.getSampleData(6));
                            }
                        }
                    }, delayMillis);
                } else {
                    isErr = true;
                    mFCAdapter.onLoadMoreFailed();
                }
            }
        });
    }
});
```

# 四 头布局、尾布局与空布局

添加头部、尾部

```java
mFCAdapter.addHeaderView(getView());
mFCAdapter.addFooterView(getView());
```

删除指定头部、尾部

```java
mFCAdapter.removeHeaderView(getView);
mFCAdapter.removeFooterView(getView);
```

删除所有头部、尾部

```java
mFCAdapter.removeAllHeaderView();
mFCAdapter.removeAllFooterView();
```

添加空布局

```java
mEmptyLayout = new EmptyLayout(EmptyViewActivity.this);
RecyclerView.LayoutParams params = new GridStickyLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT);
mEmptyLayout.setLayoutParams(params);
mAdapter.setEmptyView(mEmptyLayout);
```

# 五 多类型布局

实体类必须实现MultiItem，在设置数据的时候，需要给每一个数据设置itemType

```java
public class MultipleItem implements MultiItem {
    public static final int TEXT = 1;
    public static final int IMG = 2;
    public static final int BIG_IMG_SPAN_SIZE = 3;
    public static final int TEXT_SPAN_SIZE = 3;
    public static final int IMG_SPAN_SIZE = 1;
    private int itemType;
    private int spanSize;

    public MultipleItem(int itemType, int spanSize, String content) {
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.content = content;
    }

    public MultipleItem(int itemType, int spanSize) {
        this.itemType = itemType;
        this.spanSize = spanSize;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
```


在构造里面addItemType绑定type和layout的关系

```java
public class MultipleItemAdapter extends FCMultiItemAdapter<MultipleItem> {

    public MultipleItemAdapter(Context context, List data) {
        super(data);
        addItemType(MultipleItem.TEXT, R.layout.item_text_view);
        addItemType(MultipleItem.IMG, R.layout.item_image_view);
    }

    @Override
    protected void bindData(FCViewHolder holder, MultipleItem item) {
        switch (holder.getItemViewType()) {
            case MultipleItem.TEXT:
                holder.setText(R.id.tv, item.getContent());
                break;
            case MultipleItem.IMG:
                // set img data
                break;
        }
    }

}
```

# 六 分隔条

HorizontalItemDecoration  水平分隔条

VerticalItemDecoration	  竖直分隔条

```java
RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
recyclerView.addItemDecoration(
        new HorizontalDividerItemDecoration.Builder(this)
                .color(Color.RED)
                .sizeResId(R.dimen.divider)
                .marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
                .build());
```


# 七 侧滑删除、拖拽排序、侧滑菜单

注意：侧滑删除和侧滑菜单是互斥的，开启侧滑删除后会直接删除整个ItemView，不再显示侧滑菜单。

拖拽和侧滑的回调方法

```java
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


```

Adapter继承FCItemDraggableAdapter

```java
public class ItemDragAndSwipeAdapter extends FCItemDraggableAdapter<String> {

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
```

Adapter的xml布局

```xml
<<?xml version="1.0" encoding="utf-8"?>
 
 <!--如果开启侧滑菜单，则用FCSwipeLayout将顶部内容View和底部菜单View层叠起来-->
 
 <com.souche.android.sdk.fcadapter.swipe.FCSwipeLayout
     xmlns:android="http://schemas.android.com/apk/res/android"
     android:id="@+id/swipe_layout"
     android:layout_width="match_parent"
     android:layout_height="wrap_content"
     android:layout_marginLeft="16dp"
     android:layout_marginRight="16dp"
     android:layout_marginTop="4dp">
 
     <!--底部菜单View-->
 
     <LinearLayout
         android:id="@+id/item_action_container"
         android:layout_width="wrap_content"
         android:layout_height="wrap_content"
         android:layout_gravity="right"
         android:orientation="horizontal"
         android:visibility="visible">
 
         <TextView
             android:id="@+id/view_list_repo_action_delete"
             android:layout_width="80dp"
             android:layout_height="wrap_content"
             android:background="@android:color/holo_red_light"
             android:gravity="center"
             android:padding="13dp"
             android:text="Delete"
             android:textColor="@android:color/white"/>
 
         <TextView
             android:id="@+id/view_list_repo_action_update"
             android:layout_width="80dp"
             android:layout_height="wrap_content"
             android:background="@android:color/holo_green_light"
             android:gravity="center"
             android:padding="13dp"
             android:text="Update"
             android:textColor="@android:color/white"/>
     </LinearLayout>
 
     <!--顶部内容View-->
 
     <RelativeLayout
         android:id="@+id/item_front_view"
         android:layout_width="match_parent"
         android:layout_height="wrap_content"
         android:background="@color/item_bg"
         android:clickable="true"
         android:padding="5dp">
 
         <TextView
             android:id="@+id/tv"
             android:layout_width="match_parent"
             android:layout_height="wrap_content"
             android:layout_centerVertical="true"
             android:layout_marginLeft="16dp"
             android:text="item 1"
             android:textColor="@android:color/black"
             />
 
         <ImageView
             android:id="@+id/iv"
             android:layout_width="50dp"
             android:layout_height="wrap_content"
             android:layout_alignParentRight="true"
             android:scaleType="center"
             android:src="@drawable/ic_drag_handle"/>
     </RelativeLayout>
 
 </com.souche.android.sdk.fcadapter.swipe.FCSwipeLayout>
```

Activity里开启侧滑菜单和拖拽排序

```java
mAdapter = new ItemDragAndSwipeAdapter(mData);
mRecyclerView.setAdapter(mAdapter);

//以下代码需要在mRecyclerView.setAdapter(mAdapter)之后

//开启拖拽排序, fcItemDragListener如果不用可不写
mAdapter.setLongPressDragEnabled(true);
mAdapter.setFCItemDragListener(fcItemDragListener);
//开启侧滑删除, fcItemSwipeListener如果不用可不写， 侧滑删除和侧滑菜单是互斥的，
//开启侧滑删除后会直接删除整个ItemView，不再显示侧滑菜单。
//mAdapter.setItemViewSwipeEnabled(true);
//mAdapter.setFCItemSwipeListener(fcItemSwipeListener);
```


默认不支持多个不同的 ViewType 之间进行拖拽

# 八 分组展开与折叠

```java
// if you don't want to extent a class, you can also use the interface IExpandable.
// AbstractExpandableItem is just a helper class.
public class Level0Item extends AbstractExpandableItem<Level1Item> {...}
public class Level1Item extends AbstractExpandableItem<Person> {...}
public class Person {...}
```

adapter需要继承FCMultiItemAdapter

```java
public class ExpandableItemAdapter extends FCMultiItemAdapter<MultiItemEntity> { 
    public ExpandableItemAdapter(List<MultiItemEntity> data) {    
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0);   
        addItemType(TYPE_LEVEL_1, R.layout.item_expandable_lv1);    
        addItemType(TYPE_PERSON, R.layout.item_text_view);
    }
    @Override
    protected void bindData(final FCViewHolder holder, final MultiItemEntity item) {
        switch (holder.getItemViewType()) {
        case TYPE_LEVEL_0:
            ....
            //set view content
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   int pos = holder.getAdapterPosition();
                   if (lv0.isExpanded()) { 
                       collapse(pos);
                   } else {
                       expand(pos);
                   }
           }});
           break;
        case TYPE_LEVEL_1:
           // similar with level 0
           break;
        case TYPE_PERSON:
           //just set the content
           break;
    }
}
```

Activity使用代码

```java
public class ExpandableUseActivity extends Activity {
    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        ...
        ArrayList<MultiItemEntity> list = generateData();
        ExpandableItemAdapter adapter = new ExpandableItemAdapter(list);
        mRecyclerView.setAdapter(adapter);
    }

    private ArrayList<MultiItemEntity> generateData() {
        ArrayList<MultiItemEntity> res = new ArrayList<>();
        for (int i = 0; i < lv0Count; i++) {
            Level0Item lv0 = new Level0Item(...);
            for (int j = 0; j < lv1Count; j++) {
                Level1Item lv1 = new Level1Item(...);
                for (int k = 0; k < personCount; k++) {
                    lv1.addSubItem(new Person());
                }
                lv0.addSubItem(lv1);
            }
            res.add(lv0);
        }
        return res;
    }
}
```

# 九 快速滑动、索引滑动

布局文件里添加FastScroller

xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
              style="@style/bg">

    <android.support.v4.widget.SwipeRefreshLayout
        android:id="@+id/swipeLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        >

        <android.support.v7.widget.RecyclerView
            android:id="@+id/rv_list"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>
    </android.support.v4.widget.SwipeRefreshLayout>

    <!-- FastScroller Layout must be at the end of ViewHierarchy
     in order to be displayed at the top of every views -->
    <com.souche.fengche.lib.base.widget.adapter.fastscroller.FastScroller
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        android:id="@+id/baselib_fast_scroller"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_alignBottom="@+id/swipeLayout"
        android:layout_alignParentEnd="true"
        android:layout_alignParentRight="true"
        android:layout_alignTop="@+id/swipeLayout"
        android:layout_centerHorizontal="true"
        android:visibility="visible"
        tools:visibility="visible"/>
</RelativeLayout>

```

代码里设置FastScroller

```java
mFastScroller.setRecyclerView(mRecyclerView);
mFastScroller.addOnScrollStateChangeListener(this);
mFastScroller.setViewsToUse(
        R.layout.baselib_fast_scroller_bubble,
        R.id.fast_scroller_bubble,
        R.id.fast_scroller_handle, Color.parseColor("#FF571A"));
```

# 十 Sticky Header

1 设置StickyLayoutManager

```java
mRvStickyHeader.setLayoutManager(new StickyLayoutManager(StickyHeaderActivity.this));
```

2 编写Sticky Header的布局, 可以用app:slm_headerDisplay="inline|sticky"等来控制StickyHeader的属性

```xml
<?xml version="1.0" encoding="utf-8"?>
<TextView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/text"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@color/colorPrimary"
    android:gravity="start"
    android:layoutDirection="locale"
    android:padding="16dip"
    android:textDirection="locale"
    android:textSize="24sp"
    app:slm_headerDisplay="inline|sticky"
    app:slm_isHeader="true"
    app:slm_section_headerMarginStart="56dp"
    tools:text="Header Item"
    />
```

3 设置固定不动Item的postion

```java
@Override
public void onBindViewHolder(ViewHolder holder, int position) {
    /** << snip >> **/
    final LayoutParams params = holder.getLayoutParams();
    params.setSlm("my_special_layout"); 
    params.setFirstPosition(item.sectionFirstPosition);
    holder.setLayoutParams(params);
}
```