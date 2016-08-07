package com.tiandawu.ebook.slider;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.tiandawu.ebook.BookViewPager;
import com.tiandawu.ebook.BookViewPagerAdapter;

/**
 * Created by tiandawu on 2016/8/7.
 */
public class VerticalPageSlider extends BaseSlider {

    private int startY;
    private int distance;
    private int screenHeight;

    private Context mContext;
    private Scroller mScroller;
    private BookViewPagerAdapter mAdapter;
    private BookViewPager mBookViewPager;

    private View mScrollerCurView;//当前滑动的View
    private View mScrollerPrevView;//跟随当前伴随滑动的prevView
    private View mScrollerNextView;//跟随当前伴随滑动的nextView


    @Override
    public void init(BookViewPager mBookViewPager) {
        this.mBookViewPager = mBookViewPager;
        this.mContext = mBookViewPager.getContext();
        this.mAdapter = mBookViewPager.getAdapter();
        this.mScroller = new Scroller(mContext);
        screenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public void resetFromAdapter(BookViewPagerAdapter mAdapter) {
        View mCurView = mAdapter.getUpdatedCurrentView();
        mBookViewPager.addView(mCurView);
        mCurView.scrollTo(0, 0);

        if (mAdapter.hasPreviousContent()) {
            View mPrevView = mAdapter.getUpdatedPreviousView();
            mBookViewPager.addView(mPrevView);
            mPrevView.scrollTo(0, screenHeight);
        }

        if (mAdapter.hasNextContent()) {
            View mNextView = mAdapter.getUpdatedNextView();
            mBookViewPager.addView(mNextView);
            mNextView.scrollTo(0, -screenHeight);
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

                int deltaY = startY - (int) event.getY();
                distance += deltaY;

                if (deltaY > 0) {
                    Log.e("tt", "distance1 = " + distance);
                    Log.e("tt", "hasNext = " + !mAdapter.hasNextContent());
                    if (!mAdapter.hasNextContent() && distance >= 0) {
                        distance = 0;
                        Log.e("tt", "++++++++++++++++++=");
                        return false;
                    }

                    mScrollerCurView = mAdapter.getCurrentView();
                    if (mAdapter.hasNextContent()) {
                        mScrollerNextView = mAdapter.getNextView();
                    } else {
                        mScrollerNextView = null;
                    }

                    if (mAdapter.hasPreviousContent()) {
                        mScrollerPrevView = mAdapter.getPreviousView();
                    } else {
                        mScrollerPrevView = null;
                    }


                    mScrollerCurView.scrollBy(0, deltaY);
                    if (mScrollerNextView != null) {
                        mScrollerNextView.scrollBy(0, deltaY);
                    }

                    if (mScrollerPrevView != null) {
                        mScrollerPrevView.scrollBy(0, deltaY);
                    }

                }

                if (deltaY < 0) {
                    Log.e("tt", "distance2 = " + distance);
                    Log.e("tt", "hasPrev = " + !mAdapter.hasPreviousContent());
                    if (!mAdapter.hasPreviousContent() && distance <= 0) {
                        distance = 0;
                        Log.e("tt", "----------------------");
                        return false;
                    }

                    mScrollerCurView = mAdapter.getCurrentView();
                    if (mAdapter.hasPreviousContent()) {
                        mScrollerPrevView = mAdapter.getPreviousView();
                    } else {
                        mScrollerPrevView = null;
                    }

                    if (mAdapter.hasNextContent()) {
                        mScrollerNextView = mAdapter.getNextView();
                    } else {
                        mScrollerNextView = null;
                    }


                    mScrollerCurView.scrollBy(0, deltaY);
                    if (mScrollerPrevView != null) {
                        mScrollerPrevView.scrollBy(0, deltaY);
                    }

                    if (mScrollerNextView != null) {
                        mScrollerNextView.scrollBy(0, deltaY);
                    }
                }

                startY = (int) event.getY();

                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}
