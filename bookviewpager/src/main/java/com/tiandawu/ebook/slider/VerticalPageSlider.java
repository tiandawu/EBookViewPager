package com.tiandawu.ebook.slider;

import android.content.Context;
import android.graphics.Rect;
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
    private int moveUpAllDistance;
    private int moveDownAllDistance;
    private int screenHeight;
    private int mMode;

    private Context mContext;
    private Scroller mScroller;
    private BookViewPagerAdapter mAdapter;
    private BookViewPager mBookViewPager;

    private View mScrollerCurView;//当前滑动的View
    private View mScrollerPrevView;//跟随当前伴随滑动的prevView
    private View mScrollerNextView;//跟随当前伴随滑动的nextView

    /**
     * 用于标记是否像上滑动或者像下滑动过
     */
    private boolean isTopMove = false;
    private boolean isBottomMove = false;
    private Rect mVisiablRect = new Rect();


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


                /**
                 * 向上滑动
                 */
                if (deltaY > 0) {
//                    Log.e("tt", "ttttttttttttttttttt ");
//                    isTopMove = true;
//                    if (isBottomMove) {
//                        isBottomMove = false;
//                        return false;
//                    }
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


                    if (!mAdapter.hasNextContent()) {
                        mScrollerCurView.getLocalVisibleRect(mVisiablRect);
//                        Log.e("tt", "top = " + mVisiablRect.top);
//                        Log.e("tt", "botoom = " + mVisiablRect.bottom);
                        if (mVisiablRect.top < 0) {
                            if ((mVisiablRect.top + deltaY) >= 0) {
                                deltaY = -mVisiablRect.top;
                            }
                            mScrollerCurView.scrollBy(0, deltaY);
                            if (mScrollerNextView != null) {
                                mScrollerNextView.scrollBy(0, deltaY);
                            }

                            if (mScrollerPrevView != null) {
                                mScrollerPrevView.scrollBy(0, deltaY);
                            }
                        }
                        startY = (int) event.getY();
//                        moveUpAllDistance = 0;
//                        moveDownAllDistance = 0;
                        return false;
                    }
                    moveUpAllDistance += deltaY;
//                    Log.e("tt", "moveUpAllDistance = " + moveUpAllDistance);

                    if (moveUpAllDistance >= screenHeight) {
                        moveUpAllDistance %= screenHeight;
                        moveToNext();
                    }
//                    Log.e("tt", "moveUpAllDistance =========== " + moveUpAllDistance);
                    if (mAdapter.hasNextContent()) {
                        mScrollerCurView.scrollBy(0, deltaY);
                        if (mScrollerNextView != null) {
                            mScrollerNextView.scrollBy(0, deltaY);
                        }

                        if (mScrollerPrevView != null) {
                            mScrollerPrevView.scrollBy(0, deltaY);
                        }
                    }

                    if (!mAdapter.hasNextContent()) {
                        deltaY = deltaY - moveUpAllDistance;
                        mScrollerCurView.scrollBy(0, deltaY);
                        if (mScrollerNextView != null) {
                            mScrollerNextView.scrollBy(0, deltaY);
                        }

                        if (mScrollerPrevView != null) {
                            mScrollerPrevView.scrollBy(0, deltaY);
                        }
                    }
                }

                /**
                 * 向下滑动
                 */
                if (deltaY < 0) {
//                    Log.e("tt", "bbbbbbbbbbbbbbbbbbbb ");
//                    isBottomMove = true;
//                    if (isTopMove) {
//                        isTopMove = false;
//                        return false;
//                    }
//                    Log.e("tt", "bbbbbbbbbbbbbbbbbbbb ");
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


                    if (!mAdapter.hasPreviousContent()) {
                        mScrollerCurView.getLocalVisibleRect(mVisiablRect);
                        if (mVisiablRect.top > 0) {
                            if ((mVisiablRect.top + deltaY) <= 0) {
                                deltaY = -mVisiablRect.top;
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
//                        moveUpAllDistance = 0;
//                        moveDownAllDistance = 0;
                        return false;
                    }
                    moveDownAllDistance += deltaY;
                    if (moveDownAllDistance < -screenHeight) {
                        moveDownAllDistance %= screenHeight;
                        moveToPrevious();
                    }


                    if (mAdapter.hasPreviousContent()) {
                        mScrollerCurView.scrollBy(0, deltaY);
                        if (mScrollerPrevView != null) {
                            mScrollerPrevView.scrollBy(0, deltaY);
                        }

                        if (mScrollerNextView != null) {
                            mScrollerNextView.scrollBy(0, deltaY);
                        }
                    }

                    if (!mAdapter.hasPreviousContent()) {
                        deltaY = deltaY - moveDownAllDistance;
                        mScrollerCurView.scrollBy(0, deltaY);
                        if (mScrollerPrevView != null) {
                            mScrollerPrevView.scrollBy(0, deltaY);
                        }

                        if (mScrollerNextView != null) {
                            mScrollerNextView.scrollBy(0, deltaY);
                        }
                    }

                }

                startY = (int) event.getY();

                break;
            case MotionEvent.ACTION_UP:
//                Log.e("tt", "moveUpAllDistance = " + moveUpAllDistance);
//                Log.e("tt", "moveDownAllDistance = " + moveDownAllDistance);
                break;
        }
        return true;
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
            newNextView.scrollTo(0, -screenHeight + moveUpAllDistance);
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
            newPreviousView.scrollTo(0, screenHeight + moveDownAllDistance);
        }
        return true;
    }
}
