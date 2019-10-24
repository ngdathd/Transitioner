package com.animation.transitioner.java;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;

public class Transitioner {
    /**
     * The interpolator of the animation
     */
    private TimeInterpolator interpolator = new AccelerateDecelerateInterpolator();

    /**
     * The duration of the animation
     */
    private int duration = 400;

    /**
     * The current progress of the animation
     */
    private float currentProgress = 0.0f;
    private Callback callback;

    private ArrayList<StateOfViews> mappedViews = new ArrayList();
    private ArrayList<View> startingChildViews;
    private ArrayList<View> endingChildViews;

    public Transitioner(View startingView, View endingView, Callback callback) {
        startingChildViews = getAllChildrenView(startingView);
        endingChildViews = getAllChildrenView(endingView);
        this.callback = callback;
        for (View old : startingChildViews) {
            for (View it : endingChildViews) {
                if (old.getTag() == it.getTag()) {
                    mappedViews.add(new StateOfViews(old, it, new Dimensions((int) old.getX(), (int) old.getY(), old.getWidth(), old.getHeight())));
                }
            }
        }
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        if (duration >= 0) {
            this.duration = duration;
        }
    }

    public TimeInterpolator getInterpolator() {
        return interpolator;
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        this.interpolator = interpolator;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    /**
     * Manually set animation progress based on a float number between 0 and 1
     *
     * @param progress The float to which the animation progress should be set
     */
    public void setProgress(float progress) {
        currentProgress = progress;
        callback.onPercentChanged(progress);
        for (StateOfViews it : mappedViews) {
            it.startV.setX(it.origDimens.x + (it.endV.getX() - it.origDimens.x) * progress);
            it.startV.setY(it.origDimens.y + (it.endV.getY() - it.origDimens.y) * progress);
            it.startV.getLayoutParams().width = (int) (it.origDimens.width + ((it.endV.getWidth() - it.origDimens.width) * progress));
            it.startV.getLayoutParams().height = (int) (it.origDimens.height + ((it.endV.getHeight() - it.origDimens.height) * progress));
            it.startV.requestLayout();
        }
    }

    /**
     * Manually set animation progress based on a integer number between 0 and 100
     *
     * @param percent The integer to which the animation progress should be set
     */
    public void setProgress(int percent) {
        setProgress((float) percent / 100);
    }

    /**
     * Animate to a given percent. Optional interpolator
     *
     * @param percent      The float to which the animation progress should be set
     * @param interpolator The interpolator for the animation. Optional
     * @param duration     The duration of the animation. Optional
     */
    public void animateTo(float percent, long duration, TimeInterpolator interpolator) {
        if (currentProgress == percent || percent < 0f || percent > 1f) return;
        ValueAnimator animator = ValueAnimator.ofFloat(currentProgress, percent);
        animator.getDuration();
        animator.setDuration(duration != -1 ? duration : this.duration);
        animator.setInterpolator(interpolator != null ? interpolator : this.interpolator);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setProgress((float) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    private ArrayList<View> getAllChildrenView(View v) {
        ArrayList<View> visited = new ArrayList<>();
        ArrayList<View> unvisited = new ArrayList<>();
        unvisited.add(v);
        while (!unvisited.isEmpty()) {
            View child = unvisited.remove(0);
            visited.add(child);
            if (!(child instanceof ViewGroup)) continue;
            ViewGroup c = ((ViewGroup) child);
            for (int i = 0; i < c.getChildCount(); i++) {
                unvisited.add(c.getChildAt(i));
            }
        }
        return visited;
    }

    public interface Callback {
        void onPercentChanged(float percent);
    }

    private class Dimensions {
        private int x;
        private int y;
        private int width;
        private int height;

        Dimensions(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    private class StateOfViews {
        private View startV;
        private View endV;
        private Dimensions origDimens;

        StateOfViews(View startV, View endV, Dimensions origDimens) {
            this.startV = startV;
            this.endV = endV;
            this.origDimens = origDimens;
        }
    }
}