package com.guoxiaoxing.gxadapter.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

public class SlideRightAnimation implements BaseAnimation {

    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "translationX", view.getRootView().getWidth(), 0)
        };
    }
}
