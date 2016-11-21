package com.souche.android.sdk.fcadapter.item;

import java.util.List;

public interface IExpandable<T> {
    boolean isExpanded();
    void setExpanded(boolean expanded);
    List<T> getSubItems();
}
