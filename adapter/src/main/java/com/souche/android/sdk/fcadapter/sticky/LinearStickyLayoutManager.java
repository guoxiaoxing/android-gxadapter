package com.souche.android.sdk.fcadapter.sticky;

import android.view.View;

public class LinearStickyLayoutManager extends SectionLayoutManager {

    public static int ID = StickyLayoutManager.SECTION_MANAGER_LINEAR;

    public LinearStickyLayoutManager(StickyLayoutManager stickyLayoutManager) {
        super(stickyLayoutManager);
    }

    @Override
    public int computeHeaderOffset(int firstVisiblePosition, SectionData sd, LayoutState state) {
        /*
         * Work from an assumed overlap and add heights from the start until the overlap is zero or
         * less, or the current position (or max items) is reached.
         */

        int areaAbove = 0;
        for (int position = sd.firstPosition + 1;
                areaAbove < sd.headerHeight && position < firstVisiblePosition;
                position++) {
            // Look to see if the header overlaps with the displayed area of the mSection.
            LayoutState.View child = state.getView(position);
            measureChild(child, sd);

            areaAbove += mStickyLayoutManager.getDecoratedMeasuredHeight(child.view);
            state.cacheView(position, child.view);
        }

        if (areaAbove == sd.headerHeight) {
            return 0;
        } else if (areaAbove > sd.headerHeight) {
            return 1;
        } else {
            return -areaAbove;
        }
    }

    @Override
    public int fillToEnd(int leadingEdge, int markerLine, int anchorPosition, SectionData sd,
            LayoutState state) {
        final int itemCount = state.getRecyclerState().getItemCount();

        for (int i = anchorPosition; i < itemCount; i++) {
            if (markerLine >= leadingEdge) {
                break;
            }

            LayoutState.View next = state.getView(i);
            StickyLayoutManager.LayoutParams params = next.getLayoutParams();
            if (params.getTestedFirstPosition() != sd.firstPosition) {
                state.cacheView(i, next.view);
                break;
            }

            measureChild(next, sd);
            markerLine = layoutChild(next, markerLine, StickyLayoutManager.Direction.END, sd, state);
            addView(next, i, StickyLayoutManager.Direction.END, state);
        }

        return markerLine;
    }

    @Override
    public int fillToStart(int leadingEdge, int markerLine, int anchorPosition, SectionData sd,
            LayoutState state) {
        // Check to see if we have to adjust for minimum section height. We don't if there is an
        // attached non-header view in this section.
        boolean applyMinHeight = false;
        for (int i = 0; i < state.getRecyclerState().getItemCount(); i++) {
            View check = mStickyLayoutManager.getChildAt(0);
            if (check == null) {
                applyMinHeight = false;
                break;
            }

            StickyLayoutManager.LayoutParams checkParams =
                    (StickyLayoutManager.LayoutParams) check.getLayoutParams();
            if (checkParams.getTestedFirstPosition() != sd.firstPosition) {
                applyMinHeight = true;
                break;
            }

            if (!checkParams.isHeader) {
                applyMinHeight = false;
                break;
            }
        }

        // Work out offset to marker line by measuring items from the end. If section height is less
        // than min height, then adjust marker line and then lay out items.
        int measuredPositionsMarker = -1;
        int sectionHeight = 0;
        int minHeightOffset = 0;
        if (applyMinHeight) {
            for (int i = anchorPosition; i >= 0; i--) {
                LayoutState.View measure = state.getView(i);
                state.cacheView(i, measure.view);
                StickyLayoutManager.LayoutParams params = measure.getLayoutParams();
                if (params.getTestedFirstPosition() != sd.firstPosition) {
                    break;
                }

                if (params.isHeader) {
                    continue;
                }

                measureChild(measure, sd);
                sectionHeight += mStickyLayoutManager.getDecoratedMeasuredHeight(measure.view);
                measuredPositionsMarker = i;
                if (sectionHeight >= sd.minimumHeight) {
                    break;
                }
            }

            if (sectionHeight < sd.minimumHeight) {
                minHeightOffset = sectionHeight - sd.minimumHeight;
                markerLine += minHeightOffset;
            }
        }

        for (int i = anchorPosition; i >= 0; i--) {
            if (markerLine - minHeightOffset <= leadingEdge) {
                break;
            }

            LayoutState.View next = state.getView(i);
            StickyLayoutManager.LayoutParams params = next.getLayoutParams();
            if (params.isHeader) {
                state.cacheView(i, next.view);
                break;
            }
            if (params.getTestedFirstPosition() != sd.firstPosition) {
                state.cacheView(i, next.view);
                break;
            }

            if (!applyMinHeight || i < measuredPositionsMarker) {
                measureChild(next, sd);
            } else {
                state.decacheView(i);
            }
            markerLine = layoutChild(next, markerLine, StickyLayoutManager.Direction.START, sd, state);
            addView(next, i, StickyLayoutManager.Direction.START, state);
        }

        return markerLine;
    }

    @Override
    public int finishFillToEnd(int leadingEdge, View anchor, SectionData sd, LayoutState state) {
        final int anchorPosition = mStickyLayoutManager.getPosition(anchor);
        final int markerLine = mStickyLayoutManager.getDecoratedBottom(anchor);

        return fillToEnd(leadingEdge, markerLine, anchorPosition + 1, sd, state);
    }

    @Override
    public int finishFillToStart(int leadingEdge, View anchor, SectionData sd, LayoutState state) {
        final int anchorPosition = mStickyLayoutManager.getPosition(anchor);
        final int markerLine = mStickyLayoutManager.getDecoratedTop(anchor);

        return fillToStart(leadingEdge, markerLine, anchorPosition - 1, sd, state);
    }

    private int layoutChild(LayoutState.View child, int markerLine,
                            StickyLayoutManager.Direction direction, SectionData sd, LayoutState state) {
        final int height = mStickyLayoutManager.getDecoratedMeasuredHeight(child.view);
        final int width = mStickyLayoutManager.getDecoratedMeasuredWidth(child.view);

        int left = state.isLTR ? sd.contentStart : sd.contentEnd;
        int right = left + width;
        int top;
        int bottom;

        if (direction == StickyLayoutManager.Direction.END) {
            top = markerLine;
            bottom = top + height;
        } else {
            bottom = markerLine;
            top = bottom - height;
        }
        mStickyLayoutManager.layoutDecorated(child.view, left, top, right, bottom);

        if (direction == StickyLayoutManager.Direction.END) {
            markerLine = mStickyLayoutManager.getDecoratedBottom(child.view);
        } else {
            markerLine = mStickyLayoutManager.getDecoratedTop(child.view);
        }

        return markerLine;
    }

    private void measureChild(LayoutState.View child, SectionData sd) {
        mStickyLayoutManager.measureChildWithMargins(child.view, sd.getTotalMarginWidth(), 0);
    }
}
