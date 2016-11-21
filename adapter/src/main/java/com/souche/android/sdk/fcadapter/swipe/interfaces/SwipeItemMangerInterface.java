package com.souche.android.sdk.fcadapter.swipe.interfaces;

import com.souche.android.sdk.fcadapter.swipe.FCSwipeLayout;
import com.souche.android.sdk.fcadapter.swipe.util.Attributes;

import java.util.List;

public interface SwipeItemMangerInterface {

    void openItem(int position);

    void closeItem(int position);

    void closeAllExcept(FCSwipeLayout layout);
    
    void closeAllItems();

    List<Integer> getOpenItems();

    List<FCSwipeLayout> getOpenLayouts();

    void removeShownLayouts(FCSwipeLayout layout);

    boolean isOpen(int position);

    Attributes.Mode getMode();

    void setMode(Attributes.Mode mode);
}
