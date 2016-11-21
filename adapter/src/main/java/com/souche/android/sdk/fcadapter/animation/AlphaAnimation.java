package com.souche.android.sdk.fcadapter.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

public class AlphaAnimation implements BaseAnimation {

    private static final float DEFAULT_ALPHA_FROM = 0f;
    private final float mFrom;

    public AlphaAnimation() {
        this(DEFAULT_ALPHA_FROM);
    }

    public AlphaAnimation(float from) {
        mFrom = from;
    }

    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{ObjectAnimator.ofFloat(view, "alpha", mFrom, 1f)};
    }
}
