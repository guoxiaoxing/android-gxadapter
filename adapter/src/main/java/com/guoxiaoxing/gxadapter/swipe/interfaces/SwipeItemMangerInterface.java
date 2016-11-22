package com.guoxiaoxing.gxadapter.swipe.interfaces;

import com.guoxiaoxing.gxadapter.swipe.GXSwipeLayout;
import com.guoxiaoxing.gxadapter.swipe.util.Attributes;

import java.util.List;

public interface SwipeItemMangerInterface {

    void openItem(int position);

    void closeItem(int position);

    void closeAllExcept(GXSwipeLayout layout);
    
    void closeAllItems();

    List<Integer> getOpenItems();

    List<GXSwipeLayout> getOpenLayouts();

    void removeShownLayouts(GXSwipeLayout layout);

    boolean isOpen(int position);

    Attributes.Mode getMode();

    void setMode(Attributes.Mode mode);
}
