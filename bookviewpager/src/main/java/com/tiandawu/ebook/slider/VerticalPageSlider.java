package com.tiandawu.ebook.slider;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;

import com.tiandawu.ebook.BookViewPager;
import com.tiandawu.ebook.BookViewPagerAdapter;

/**
 * Created by tiandawu on 2016/8/3.
 */

/**
 * 垂直滑动效果
 */
public class VerticalPageSlider extends BaseSlider {


    private int startY;
    private int screenHeight;

    private Context context;
    private Scroller mScroller;
    private BookViewPagerAdapter mAdapter;
    private BookViewPager mBookViewPager;
    private VelocityTracker mVelocityTracker;
    private View mScrollPreView, mScrollCurView;

    @Override
    public void init(BookViewPager mBookViewPager) {
        this.mBookViewPager = mBookViewPager;
        this.context = mBookViewPager.getContext();
        this.mAdapter = mBookViewPager.getAdapter();
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        mScroller = new Scroller(context);

    }


    @Override
    public void resetFromAdapter(BookViewPagerAdapter mAdapter) {
        View curView = mAdapter.getUpdatedCurrentView();
        mBookViewPager.addView(curView);
        curView.scrollTo(0, 0);

        if (mAdapter.hasPreviousContent()) {
            View prevView = mAdapter.getUpdatedPreviousView();
            mBookViewPager.addView(prevView);
            prevView.scrollTo(screenHeight, 0);
        }

        if (mAdapter.hasNextContent()) {
            View nextView = mAdapter.getUpdatedNextView();
            mBookViewPager.addView(nextView);
            nextView.scrollTo(-screenHeight, 0);
        }

        mBookViewPager.pageSelected(mAdapter.getCurrentView());
    }

    @Override
    public void computeScroll() {

    }

    @Override
    public void slideNext() {

    }

    @Override
    public void slidePrevious() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        obtainVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    break;
                }
                startY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished()) {
                    return false;
                }

                if (startY == 0) {
                    startY = (int) event.getY();
                }

                final int distance = startY - (int) event.getY();

                if ((!mAdapter.hasPreviousContent() && distance < 0)
                        || (!mAdapter.hasNextContent() && distance > 0)) {
                    return false;
                }

                if (distance > 0) {
                    mScrollPreView = mAdapter.getCurrentView();
                    if (mAdapter.hasNextContent()) {
                        mScrollCurView = mAdapter.getNextView();
                    } else {
                        mScrollCurView = null;
                    }

                    mScrollPreView.scrollTo(0, distance);
                    if (mScrollCurView != null) {
                        mScrollCurView.scrollTo(0, -screenHeight + distance);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    /**
     * 获取速率检测器
     *
     * @param event
     */
    private void obtainVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 释放速率检测器资源
     */
    private void releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }


}
