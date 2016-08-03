package com.tiandawu.ebook.slider;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.tiandawu.ebook.BookViewPager;
import com.tiandawu.ebook.BookViewPagerAdapter;

/**
 * Created by tiandawu on 2016/8/1.
 */

/**
 * ViewPager效果的的翻页
 */
public class ViewPagerSlider extends BaseSlider {

    /**
     * 手指按下的位置
     */
    private int startX;
    /**
     * 屏幕的宽度
     */
    private int screenWidth;
    /**
     * 移动的限制距离
     */
    private int limitDistance = 0;
    /**
     * 速率值
     */
    private int mVelocityValue = 0;
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
     * 移动的是否是最后一页或者第一页
     */
    private boolean mMoveLastPage, mMoveFirstPage;

    private Context mContext;
    private Scroller mScroller;
    private BookViewPager mBookViewPager;
    private BookViewPagerAdapter mAdapter;
    private VelocityTracker mVelocityTracker;

    /**
     * 滑动的view
     */
    private View mLeftScrollerView = null;
    private View mRightScrollerView = null;

    /**
     * 初始化
     */
    @Override
    public void init(BookViewPager mBookViewPager) {
        this.mBookViewPager = mBookViewPager;
        mContext = mBookViewPager.getContext();
        if (mAdapter == null) {
            mAdapter = mBookViewPager.getAdapter();
        }
        mScroller = new Scroller(mContext);
        screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        limitDistance = screenWidth / 2;
    }


    /**
     * 放置View
     */
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

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mLeftScrollerView != null) {
                mLeftScrollerView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            }

            if (mRightScrollerView != null) {
                if (mMoveFirstPage) {
                    mRightScrollerView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
                } else {
                    mRightScrollerView.scrollTo(mScroller.getCurrX() - screenWidth, mScroller.getCurrY());
                }
                invalidate();
            }
        } else if (mScroller.isFinished()) {
            if (mTouchResult != MOVE_NO_RESULT) {
                if (mTouchResult == MOVE_TO_LEFT) {
                    moveToNext();
                } else {
                    moveToPrevious();
                }
                mTouchResult = MOVE_NO_RESULT;
                mBookViewPager.pageScrollStateChanged(MOVE_NO_RESULT);
                invalidate();
            }
        }

    }

    @Override
    public void slideNext() {
        if (!mAdapter.hasNextContent() || !mScroller.isFinished()) {
            return;
        }
        mLeftScrollerView = getCurrentView();
        mRightScrollerView = getNextView();
        mScroller.startScroll(0, 0, screenWidth, 0, 500);
        mTouchResult = MOVE_TO_LEFT;
        mBookViewPager.pageScrollStateChanged(MOVE_TO_LEFT);
        invalidate();
    }

    @Override
    public void slidePrevious() {
        if (!mAdapter.hasPreviousContent() || !mScroller.isFinished()) {
            return;
        }
        mLeftScrollerView = getPreviousView();
        mRightScrollerView = getCurrentView();
        mScroller.startScroll(screenWidth, 0, -screenWidth, 0, 500);
        mTouchResult = MOVE_TO_RIGHT;
        mBookViewPager.pageScrollStateChanged(MOVE_TO_RIGHT);
        invalidate();
    }


    /**
     * 移动到下一页
     *
     * @return
     */
    private boolean moveToNext() {
        if (!mAdapter.hasNextContent()) {
            return false;
        }

        View previousView = mAdapter.getPreviousView();
        if (previousView != null) {
            mBookViewPager.removeView(previousView);
        }
        View newNextView = previousView;
        mAdapter.moveToNext();
        mBookViewPager.pageSelected(mAdapter.getCurrentView());
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
            mBookViewPager.addView(newNextView);
            newNextView.scrollTo(-screenWidth, 0);
        }
        return true;
    }


    /**
     * 移动到上一页
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
        View newPreviousView = nextView;
        mAdapter.moveToPrevious();
        mBookViewPager.pageSelected(mAdapter.getCurrentView());

        if (mAdapter.hasPreviousContent()) {
            if (newPreviousView != null) {
                View updatePrevView = mAdapter.getView(newPreviousView, mAdapter.getPreviousContent());
                if (updatePrevView != newPreviousView) {
                    mAdapter.setPreviousView(updatePrevView);
                    newPreviousView = updatePrevView;
                }
            } else {
                newPreviousView = mAdapter.getPreviousView();
            }
            mBookViewPager.addView(newPreviousView);
            newPreviousView.scrollTo(screenWidth, 0);
        }
        return true;
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

                /**
                 * 如果滑动的View是第一页且是向右滑动，或则滑动的View是最后一页且是向左滑动，
                 * 则不让用户的滑动事件生效。
                 */
                if ((!mAdapter.hasPreviousContent() && distance < 0) || (!mAdapter.hasNextContent() && distance > 0)) {
                    return false;
                }

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

                if (mMode == MODE_NONE
                        && ((mDirection == MOVE_TO_LEFT)
                        || (mDirection == MOVE_TO_RIGHT))) {
                    mMode = MODE_MOVE;
                }

                if (mMode == MODE_MOVE) {
                    if ((mDirection == MOVE_TO_LEFT && distance <= 0)
                            || (mDirection == MOVE_TO_RIGHT && distance >= 0)) {
                        mMode = MODE_NONE;
                        /**
                         * 进入此段逻辑表面，用户操作是：
                         * 1：按下后，先左滑，接着右滑，并且右滑的距离大于左滑的距离，抬手。
                         * 2：或者按下后，先右滑，接着作滑，并且左滑的距离大于右滑的距离，抬手。
                         */
                    }
                }

                if (mDirection != MOVE_NO_RESULT) {
                    if (mDirection == MOVE_TO_LEFT) {
                        mLeftScrollerView = getCurrentView();
                        if (!mMoveLastPage) {
                            mRightScrollerView = getNextView();
                        } else {
                            mRightScrollerView = null;
                        }
                    } else {
                        mRightScrollerView = getCurrentView();
                        if (!mMoveFirstPage) {
                            mLeftScrollerView = getPreviousView();
                        } else {
                            mLeftScrollerView = null;
                        }
                    }

                    if (mMode == MODE_MOVE) {
                        mVelocityTracker.computeCurrentVelocity(1000, ViewConfiguration.getMaximumFlingVelocity());
                        if (mDirection == MOVE_TO_LEFT) {
                            mLeftScrollerView.scrollTo(distance, 0);
                            mRightScrollerView.scrollTo(-screenWidth + distance, 0);
                        } else {
                            mRightScrollerView.scrollTo(distance, 0);
                            mLeftScrollerView.scrollTo(screenWidth + distance, 0);
                        }
                    } else {
                        int scrollX = 0;
                        if (mLeftScrollerView != null) {
                            scrollX = mLeftScrollerView.getScrollX();
                        } else if (mRightScrollerView != null) {
                            scrollX = mRightScrollerView.getScrollX();
                        }

                        if (mDirection == MOVE_TO_LEFT && scrollX != 0 && mAdapter.hasNextContent()) {
                            mLeftScrollerView.scrollTo(0, 0);
                            if (mRightScrollerView != null) {
                                mRightScrollerView.scrollTo(screenWidth, 0);
                            }
                        } else if (mDirection == MOVE_TO_RIGHT && mAdapter.hasPreviousContent() && screenWidth != Math.abs(scrollX)) {
                            if (mLeftScrollerView != null) {
                                mLeftScrollerView.scrollTo(-screenWidth, 0);
                            }
                            mRightScrollerView.scrollTo(0, 0);
                        }
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if ((mLeftScrollerView == null && mDirection == MOVE_TO_LEFT)
                        || (mRightScrollerView == null && mDirection == MOVE_TO_RIGHT)) {
                    return false;
                }

                int time = 500;

                if (mMoveFirstPage && mRightScrollerView != null) {
                    final int rScrollX = mRightScrollerView.getScrollX();
                    mScroller.startScroll(rScrollX, 0, -rScrollX, 0, time * Math.abs(rScrollX) / screenWidth);
                    mTouchResult = MOVE_NO_RESULT;
                }

                if (mMoveLastPage && mLeftScrollerView != null) {
                    final int lScrollX = mLeftScrollerView.getScrollX();
                    mScroller.startScroll(lScrollX, 0, -lScrollX, 0, time * Math.abs(lScrollX) / screenWidth);
                    mTouchResult = MOVE_NO_RESULT;
                }

                if (!mMoveFirstPage && !mMoveLastPage && mLeftScrollerView != null) {
                    final int scrollX = mLeftScrollerView.getScrollX();
                    mVelocityValue = (int) mVelocityTracker.getXVelocity();
                    if (mMode == MODE_MOVE && mDirection == MOVE_TO_LEFT) {
                        if (scrollX > limitDistance || mVelocityValue < -time) {
                            // 手指向左移动，可以翻屏幕
                            mTouchResult = MOVE_TO_LEFT;
                            if (mVelocityValue < -time) {
                                int tmptime = 1000 * 1000 / Math.abs(mVelocityValue);
                                time = tmptime > 500 ? 500 : tmptime;
                            }
                            mScroller.startScroll(scrollX, 0, screenWidth - scrollX, 0, time);
                        } else {
                            mTouchResult = MOVE_NO_RESULT;
                            mScroller.startScroll(scrollX, 0, -scrollX, 0, time);
                        }

                    } else if (mMode == MODE_MOVE && mDirection == MOVE_TO_RIGHT) {
                        if ((screenWidth - scrollX) > limitDistance || mVelocityValue > time) {
                            // 手指向右移动，可以翻屏幕
                            mTouchResult = MOVE_TO_RIGHT;
                            if (mVelocityValue > time) {
                                int tmptime = 1000 * 1000 / Math.abs(mVelocityValue);
                                time = tmptime > 500 ? 500 : tmptime;
                            }
                            mScroller.startScroll(scrollX, 0, -scrollX, 0, time);
                        } else {
                            mTouchResult = MOVE_NO_RESULT;
                            mScroller.startScroll(scrollX, 0, screenWidth - scrollX, 0, time);
                        }

                    }
                }
                resetVariables();
                invalidate();
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
        //将事件加入到VelocityTracker类实例中
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


    /**
     * 重置变量
     */
    private void resetVariables() {
        mDirection = MOVE_NO_RESULT;
        mMode = MODE_NONE;
        startX = 0;
        releaseVelocityTracker();
    }

    /**
     * 刷新
     */
    private void invalidate() {
        mBookViewPager.postInvalidate();
    }


    /**
     * 获取上一页
     *
     * @return
     */
    private View getPreviousView() {
        return mAdapter.getPreviousView();
    }

    /**
     * 获取当前页
     *
     * @return
     */
    private View getCurrentView() {
        return mAdapter.getCurrentView();
    }

    /**
     * 获取下一页
     *
     * @return
     */
    private View getNextView() {
        return mAdapter.getNextView();
    }
}
