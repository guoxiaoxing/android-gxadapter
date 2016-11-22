package com.guoxiaoxing.gxadapter.swipe.interfaces;

import com.guoxiaoxing.gxadapter.swipe.FCSwipeLayout;
import com.guoxiaoxing.gxadapter.swipe.util.Attributes;

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
