package com.tiandawu.bookviewpager.slider;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.tiandawu.bookviewpager.BookViewPager;
import com.tiandawu.bookviewpager.BookViewPagerAdapter;

/**
 * Created by tiandawu on 2016/8/2.
 */

/**
 * 页面覆盖滑动效果
 */
public class CoverPageSlider extends BaseSlider {


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

    /**
     * 触摸模式
     */
    private int mMode = MODE_NONE;

    /**
     * 速率值
     */
    private int mVelocityValue = 0;

    /**
     * 商定这个滑动是否有效的距离
     */
    private int limitDistance = 0;

    private Context context;
    private Scroller mScroller;
    private BookViewPager mBookViewPager;
    private BookViewPagerAdapter mAdapter;
    private View mScrollerView;
    private VelocityTracker velocityTracker;


    @Override
    public void init(BookViewPager mBookViewPager) {
        this.mBookViewPager = mBookViewPager;
        this.context = mBookViewPager.getContext();
        this.mAdapter = mBookViewPager.getAdapter();
        mScroller = new Scroller(context);
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        limitDistance = screenWidth / 3;
    }

    @Override
    public void resetFromAdapter(BookViewPagerAdapter mAdapter) {
        mBookViewPager.addView(mAdapter.getCurrentView());

        if (mAdapter.hasNextContent()) {
            View nextView = mAdapter.getNextView();
            mBookViewPager.addView(nextView, 0);
            nextView.scrollTo(0, 0);
        }

        if (mAdapter.hasPreviousContent()) {
            View prevView = mAdapter.getPreviousView();
            mBookViewPager.addView(prevView);
            prevView.scrollTo(screenWidth, 0);
        }
        mBookViewPager.pageSelected(mAdapter.getCurrentView());
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mScrollerView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        } else if (mScroller.isFinished() && mTouchResult != MOVE_NO_RESULT) {
            if (mTouchResult == MOVE_TO_LEFT) {
                moveToNext();
            } else {
                moveToPrevious();
            }
            mTouchResult = MOVE_NO_RESULT;
            invalidate();
        }
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
                startX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished()) {
                    return false;
                }
                if (startX == 0) {
                    startX = (int) event.getX();
                }

                final int distance = startX - (int) event.getX();
//                Log.e("tt", "distance = " + distance);
                if (mDirection == MOVE_NO_RESULT) {
                    if (mAdapter.hasNextContent() && distance > 0) {
                        mDirection = MOVE_TO_LEFT;
//                        Log.e("tt", "111111111");
                    } else if (mAdapter.hasPreviousContent() && distance < 0) {
                        mDirection = MOVE_TO_RIGHT;
//                        Log.e("tt", "22222222");
                    }
                }

                if (mMode == MODE_NONE
                        && ((mDirection == MOVE_TO_LEFT && mAdapter.hasNextContent())
                        || (mDirection == MOVE_TO_RIGHT && mAdapter.hasPreviousContent()))) {
                    mMode = MODE_MOVE;
                }

                if (mMode == MODE_MOVE) {
//                    Log.e("tt", "mDirection = " + mDirection);
                    if ((mDirection == MOVE_TO_LEFT && distance <= 0)
                            || (mDirection == MOVE_TO_RIGHT && distance >= 0)) {
                        mMode = MODE_NONE;
//                        Log.e("tt", "????????");

                    }
                }

                if (mDirection != MOVE_NO_RESULT) {
                    if (mDirection == MOVE_TO_LEFT) {
                        mScrollerView = getCurrentView();
                    } else {
                        mScrollerView = getTopView();
                    }

                    if (mMode == MODE_MOVE) {
                        velocityTracker.computeCurrentVelocity(1000, ViewConfiguration.getMaximumFlingVelocity());
                        if (mDirection == MOVE_TO_LEFT) {
                            mScrollerView.scrollTo(distance, 0);
                        } else {
                            mScrollerView.scrollTo(distance + screenWidth, 0);
                        }
                    } else {
                        final int scrollX = mScrollerView.getScrollX();
                        if (mDirection == MOVE_TO_LEFT && scrollX != 0 && mAdapter.hasNextContent()) {
                            mScrollerView.scrollTo(0, 0);
                            Log.e("tt", "-------");
                        } else if (mDirection == MOVE_TO_RIGHT && mAdapter.hasPreviousContent()
                                && screenWidth != Math.abs(scrollX)) {
                            mScrollerView.scrollTo(screenWidth, 0);
                        }
                    }
                }

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (mScrollerView == null) {
                    return false;
                }

                final int scrollX = mScrollerView.getScrollX();

                mVelocityValue = (int) velocityTracker.getXVelocity();
                Log.e("tt", "mVelocityValue = " + mVelocityValue);
                int time = 500;
                if (mMode == MODE_MOVE && mDirection == MOVE_TO_LEFT) {
                    if (scrollX > limitDistance || mVelocityValue < -time) {
                        //说明满足翻屏的条件
                        // 手指向左移动，可以翻屏幕
                        mTouchResult = MOVE_TO_LEFT;
                        if (mVelocityValue < -time) {
                            time = 200;
                        }
                        mScroller.startScroll(scrollX, 0, screenWidth - scrollX, 0, time);
                    } else {
                        //不满足翻屏的条件，将View恢复到原位置
                        mTouchResult = MOVE_NO_RESULT;
                        mScroller.startScroll(scrollX, 0, -scrollX, 0, time);
                    }
                } else if (mMode == MODE_MOVE && mDirection == MOVE_TO_RIGHT) {
                    if ((screenWidth - scrollX) > limitDistance || mVelocityValue > time) {
                        // 手指向右移动，可以翻屏幕
                        mTouchResult = MOVE_TO_RIGHT;
                        if (mVelocityValue > time) {
                            time = 250;
                        }
                        mScroller.startScroll(scrollX, 0, -scrollX, 0, time);
                    } else {
                        mTouchResult = MOVE_NO_RESULT;
                        mScroller.startScroll(scrollX, 0, screenWidth - scrollX, 0, time);
                    }
                }
                resetVariables();
                invalidate();
                break;
        }
        return true;
    }


    /**
     * 移动到后一页
     *
     * @return
     */
    private boolean moveToNext() {

        if (!mAdapter.hasNextContent()) {
            return false;
        }

        View prevView = mAdapter.getPreviousView();
        if (prevView != null) {
            mBookViewPager.removeView(prevView);
        }
        View newNextView = prevView;
        mAdapter.moveToNext();
        if (mAdapter.hasNextContent()) {
            if (newNextView != null) {
                View updateNextView = mAdapter.getView(newNextView, mAdapter.getNextContent());
                if (updateNextView != newNextView) {
                    mAdapter.setNextView(updateNextView);
                    newNextView = updateNextView;
                }
            } else {
                newNextView = mAdapter.getNextView();
            }
            mBookViewPager.addView(newNextView, 0);
            newNextView.scrollTo(0, 0);
        }
        return true;
    }

    /**
     * 移动到前一页
     *
     * @return
     */
    private boolean moveToPrevious() {

        if (!mAdapter.hasPreviousContent()) {
            return false;
        }

        View nextView = mAdapter.getNextView();
        if (nextView != null) {
            mBookViewPager.removeView(nextView);
        }
        View newPrevView = nextView;
        if (mAdapter.hasPreviousContent()) {
            Log.e("tt", "==========11111===========");
            if (newPrevView != null) {
                Log.e("tt", "==========2222===========");
                View updatePrevView = mAdapter.getView(newPrevView, mAdapter.getPreviousContent());
                if (updatePrevView != newPrevView) {
                    mAdapter.setPreviousView(updatePrevView);
                    newPrevView = updatePrevView;
                }
            } else {
                Log.e("tt", "==========33333===========");
                newPrevView = mAdapter.getPreviousView();
            }
            mBookViewPager.addView(newPrevView);
            newPrevView.scrollTo(screenWidth, 0);
        }
        return true;
    }


    /**
     * 刷新
     */
    private void invalidate() {
        mBookViewPager.postInvalidate();
    }

    /**
     * 获取上面的View
     *
     * @return
     */
    private View getTopView() {
        return mAdapter.getPreviousView();
    }

    /**
     * 获取当前显示的View
     *
     * @return
     */
    private View getCurrentView() {
        return mAdapter.getCurrentView();
    }

    /**
     * 获取下面的View
     *
     * @return
     */
    private View getBottomView() {
        return mAdapter.getNextView();
    }

    /**
     * 获取测速器
     *
     * @param event
     */
    private void obtainVelocityTracker(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
    }

    /**
     * 释放测速器资源
     */
    private void releaseVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    /**
     * 重置变量
     */
    private void resetVariables() {
        mDirection = MOVE_NO_RESULT;
        mMode = MODE_NONE;
        startX = 0;
        releaseVelocityTracker();
    }
}
