package com.tiandawu.ebook.slider;

import android.content.Context;
import android.graphics.Rect;
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
    private int downY;
    private int distance;
    private int moveAllDistance;
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
     * 用于标记是否向上滑动或者向下滑动过
     */
    private boolean isTopMove = false;
    private boolean isBottomMove = false;
    private boolean isToBottom = false;
    private boolean isDown = false;
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
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.e("tt", "Y = " + event.getY());

//                if ((int) event.getY() >= screenHeight || (int) event.getY() <= 0) {
//                    return false;
//                }

                if (!mScroller.isFinished()) {
                    return false;
                }
                if (startY == 0) {
                    startY = (int) event.getY();
                }
                int deltaY = startY - (int) event.getY();
//                Log.e("tt", "deltaY =  " + deltaY);

                /**
                 * 向上滑动
                 */
                if (deltaY > 0) {
//                    mMode = MOVE_TO_TOP;
//                    isTopMove = true;
//                    moveDownAllDistance = 0;

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
                        if ((mVisiablRect.top % screenHeight) == 0) {
                            moveAllDistance = 0;
                            Log.e("tt", "--------------1111111-----------");
                            return false;
                        }
//                        Log.e("tt", "mScrollerCurView = " + mScrollerCurView);
//                        Log.e("tt", "top = " + mVisiablRect.top);
//                        Log.e("tt", "deltaY = " + deltaY);
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
                            moveAllDistance += deltaY;
//                            Log.e("tt", "moveAllDistance = " + moveAllDistance);
                        }
                        startY = (int) event.getY();
                        Log.e("tt", "top ====== " + mVisiablRect.top);

                        return true;
                    }


//                    moveUpAllDistance += deltaY;
                    moveAllDistance += deltaY;
                    Log.e("tt", "moveAllDistance = " + moveAllDistance);
                    if (moveAllDistance >= screenHeight) {
//                        Log.e("tt", "moveUpAllDistance = " + moveUpAllDistance);
//                        Log.e("tt", "moveDownAllDistance = " + moveDownAllDistance);

//                        if (distance != 0) {
//                            moveUpAllDistance += distance;
//                        }

                        moveAllDistance %= screenHeight;
                        moveToNext();
                        if (!mAdapter.hasNextContent()) {
                            Log.e("tt", "deltaY = " + deltaY);
                            Log.e("tt", "moveAllDistance = " + moveAllDistance);
                            deltaY = deltaY - moveAllDistance;
                            Log.e("tt", "deltaY -moveAllDistance =  " + deltaY);
                            mScrollerCurView = mAdapter.getCurrentView();
//                            if (mAdapter.hasNextContent()) {
//                                mScrollerNextView = mAdapter.getNextView();
//                            } else {
//                                mScrollerNextView = null;
//                            }
//
//                            if (mAdapter.hasPreviousContent()) {
//                                mScrollerPrevView = mAdapter.getPreviousView();
//                            } else {
//                                mScrollerPrevView = null;
//                            }
//
//                            mScrollerCurView.getLocalVisibleRect(mVisiablRect);
//                            Log.e("tt", "mScrollerCurView ===== " + mScrollerCurView);

//                            if (mVisiablRect.top > 0) {
//                                return false;
//                            }
                            Log.e("tt", "mVisiablRect_top = " + mVisiablRect.top);
                            Log.e("tt", "mVisiablRect_bottom = " + mVisiablRect.bottom);
                            mScrollerCurView.scrollBy(0, deltaY);
                            if (mScrollerNextView != null) {
                                mScrollerNextView.scrollBy(0, deltaY);
                            }

                            if (mScrollerPrevView != null) {
                                mScrollerPrevView.scrollBy(0, deltaY);
                            }
                            moveAllDistance = 0;
                            isToBottom = true;
                            mScrollerCurView = mAdapter.getCurrentView();

                            Log.e("tt", "mVisiablRect_top = " + mVisiablRect.top);
                            Log.e("tt", "mVisiablRect_bottom = " + mVisiablRect.bottom);
                            Log.e("tt", "-------------------------");
                            return true;
                        }
                    }
//                    Log.e("tt", "moveUpAllDistance =========== " + moveUpAllDistance);
                    if (mAdapter.hasNextContent()) {


//
//                        mScrollerCurView = mAdapter.getCurrentView();
//                        if (mAdapter.hasNextContent()) {
//                            mScrollerNextView = mAdapter.getNextView();
//                        } else {
//                            mScrollerNextView = null;
//                        }
//
//
//                        if (mAdapter.hasPreviousContent()) {
//                            mScrollerPrevView = mAdapter.getPreviousView();
//                        } else {
//                            mScrollerPrevView = null;
//                        }
//                        if (mVisiablRect.top > screenHeight) {
//                            moveToNext();
//                        }

//                        if (!mAdapter.hasNextContent()) {
//                            mScrollerCurView = mAdapter.getCurrentView();
//                            mScrollerCurView.getLocalVisibleRect(mVisiablRect);
//                            deltaY = deltaY - mVisiablRect.top;
//                        }

                        mScrollerCurView.scrollBy(0, deltaY);
                        if (mScrollerNextView != null) {
                            mScrollerNextView.scrollBy(0, deltaY);
                        }

                        if (mScrollerPrevView != null) {
                            mScrollerPrevView.scrollBy(0, deltaY);
                        }
//                        mScrollerCurView.getLocalVisibleRect(mVisiablRect);


                        Log.e("tt", "mVisiablRect_top = " + mVisiablRect.top);
                        Log.e("tt", "mVisiablRect_bottom = " + mVisiablRect.bottom);
                        Log.e("tt", "++++++++++++++++++++++   " + moveAllDistance);
                    }

                }

                /**
                 * 向下滑动
                 */
                if (deltaY < 0) {
                    mMode = MOVE_TO_BOTTOM;
                    isBottomMove = true;
                    moveUpAllDistance = 0;
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


//                    Log.e("tt", "mScrollerCurView = " + mScrollerCurView);
//                    Log.e("tt", "mScrollerNextView = " + mScrollerNextView);
//                    Log.e("tt", "mScrollerPrevView = " + mScrollerPrevView);


//                    if (!mAdapter.hasPreviousContent() && mMode == MOVE_TO_BOTTOM && (mVisiablRect.top + deltaY) <= 0) {
//                        moveAllDistance = 0;
//                        Log.e("tt", "moveAllDistance ---------- " + moveAllDistance);
//                        return false;
//                    }


                    if (!mAdapter.hasPreviousContent()) {
//                        Log.e("tt", "+++++++++++++++");
                        mScrollerCurView.getLocalVisibleRect(mVisiablRect);
                        if ((mVisiablRect.top % screenHeight) == 0) {
                            moveAllDistance = 0;
//                            Log.e("tt", "++++++++++++++++++++++");
                            return false;
                        }

                        if (mVisiablRect.top > 0) {
                            if ((mVisiablRect.top + deltaY) <= 0) {
                                Log.e("tt", "deltaY = " + (-mVisiablRect.top));
                                deltaY = -mVisiablRect.top;
                            }
                            mScrollerCurView.scrollBy(0, deltaY);
                            if (mScrollerPrevView != null) {
                                mScrollerPrevView.scrollBy(0, deltaY);
                            }

                            if (mScrollerNextView != null) {
                                mScrollerNextView.scrollBy(0, deltaY);
                            }

                            moveAllDistance += deltaY;
//                            Log.e("tt", "moveAllDistance ========= " + moveAllDistance);

                        }
                        startY = (int) event.getY();
//                        Log.e("tt", "top = " + mVisiablRect.top);

//                        Log.e("tt", "moveAllDistance = " + moveAllDistance);
                        return true;
                    }

//                    Log.e("tt", "?????????????");
                    moveDownAllDistance += deltaY;
                    moveAllDistance += deltaY;

//                    Log.e("tt", "moveAllDistance ========= " + moveAllDistance);
                    if (moveAllDistance <= -screenHeight) {
//                        Log.e("tt", "moveUpAllDistance ===== " + moveUpAllDistance);
//                        Log.e("tt", "moveDownAllDistance ===== " + moveDownAllDistance);
//                        if (distance != 0) {
//                            moveDownAllDistance += distance;
//                        }
                        moveAllDistance %= screenHeight;
                        moveToPrevious();
                        if (!mAdapter.hasPreviousContent()) {
                            deltaY = deltaY - moveAllDistance;
//
//                            mScrollerCurView = mAdapter.getCurrentView();
//                            if (mAdapter.hasNextContent()) {
//                                mScrollerNextView = mAdapter.getNextView();
//                            } else {
//                                mScrollerNextView = null;
//                            }
//
//                            if (mAdapter.hasPreviousContent()) {
//                                mScrollerPrevView = mAdapter.getPreviousView();
//                            } else {
//                                mScrollerPrevView = null;
//                            }
//
//                            mScrollerCurView.getLocalVisibleRect(mVisiablRect);
//                            Log.e("tt", "mScrollerCurView ===== " + mScrollerCurView);
//
//                            if (mVisiablRect.top < 0) {
//                                deltaY = -mVisiablRect.top;
//                            }

                            mScrollerCurView.scrollBy(0, deltaY);
                            if (mScrollerPrevView != null) {
                                mScrollerPrevView.scrollBy(0, deltaY);
                            }

                            if (mScrollerNextView != null) {
                                mScrollerNextView.scrollBy(0, deltaY);
                            }
//                            Log.e("tt", "++++++++++++++++++++");
                            moveAllDistance = 0;
                            return true;
                        }
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
                }
                startY = (int) event.getY();

                break;
            case MotionEvent.ACTION_UP:
//                Log.e("tt", "moveUpAllDistance = " + moveUpAllDistance);
//                Log.e("tt", "moveDownAllDistance = " + moveDownAllDistance);
                distance = 0;
                if (isTopMove && isBottomMove) {
                    distance = downY - (int) event.getY();
                }

                isTopMove = false;
                isBottomMove = false;
//                Log.e("tt", "moveAllDistance = " + moveAllDistance);
//                Log.e("tt", "distance = " + ((int) event.getY() - downY));
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
            newNextView.scrollTo(0, -screenHeight + moveAllDistance);
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
            newPreviousView.scrollTo(0, screenHeight + moveAllDistance);
        }
        return true;
    }
}
