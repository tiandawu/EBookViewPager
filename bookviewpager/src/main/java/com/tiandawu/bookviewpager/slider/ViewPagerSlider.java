package com.tiandawu.bookviewpager.slider;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.tiandawu.bookviewpager.BookViewPager;
import com.tiandawu.bookviewpager.BookViewPagerAdapter;

/**
 * Created by tiandawu on 2016/8/1.
 */

/**
 * ViewPager效果的的翻页
 */
public class ViewPagerSlider extends BaseSlider {

    private int startX;
    private int screenWidth;
    /**
     * 最后触摸的结果方向
     */
    private int mTouchResult = MOVE_NO_RESULT;
    /**
     * 一开始的方向
     */
    private int mDirection = MOVE_NO_RESULT;


    private boolean mMoveLastPage, mMoveFirstPage;

    private Context mContext;
    private Scroller mScroller;
    private BookViewPager mBookViewPager;
    private BookViewPagerAdapter mAdapter;


    private BookViewPagerAdapter getAdapter() {
        return mBookViewPager.getAdapter();
    }

    @Override
    public void init(BookViewPager mBookViewPager) {
        this.mBookViewPager = mBookViewPager;
        mContext = mBookViewPager.getContext();
        if (mAdapter == null) {
            mAdapter = getAdapter();
        }
        mScroller = new Scroller(mContext);
        screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;

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
                startX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished()) {
                    return false;
                }
                if (startX == 0) {
                    startX = (int) event.getX();
                }

                int distance = (int) (startX - event.getX());
                if (mDirection == MOVE_NO_RESULT) {
                    if (distance > 0) {
                        mDirection = MOVE_TO_LEFT;
                        mMoveLastPage = !mAdapter.hasNextContent();
                        mMoveFirstPage = false;
                        mBookViewPager.pageScrollStateChanged(MOVE_TO_LEFT);
                    } else if (distance < 0) {
                        mDirection = MOVE_TO_RIGHT;
                        mMoveFirstPage = !mAdapter.hasPreviousContent();
                        mMoveLastPage = false;
                        mBookViewPager.pageScrollStateChanged(MOVE_TO_RIGHT);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }

    @Override
    public void resetFromAdapter(BookViewPagerAdapter mAdapter) {
        View curView = mAdapter.getUpdatedCurrentView();
        mBookViewPager.addView(curView);
        curView.scrollTo(0, 0);

        if (mAdapter.hasPreviousContent()) {
            View previousView = mAdapter.getUpdatedPreviousView();
            mBookViewPager.addView(previousView);
            previousView.scrollTo(screenWidth, 0);
        }

        if (mAdapter.hasNextContent()) {
            View nextView = mAdapter.getUpdatedNextView();
            mBookViewPager.addView(nextView);
            nextView.scrollTo(-screenWidth, 0);
        }

        mBookViewPager.pageSelected(mAdapter.getCurrentView());
    }
}
