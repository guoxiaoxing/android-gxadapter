package com.guoxiaoxing.gxadapter.item;

import java.util.List;

public interface IExpandable<T> {
    boolean isExpanded();
    void setExpanded(boolean expanded);
    List<T> getSubItems();
}
