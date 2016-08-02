package com.tiandawu.bookviewpager;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.tiandawu.bookviewpager.slider.Slider;

/**
 * Created by tiandawu on 2016/7/31.
 */
public class BookViewPager extends ViewGroup {

    /**
     * 记录按下的X,Y
     */
    private int mDownX, mDownY;
    /**
     * 记录按下的时间
     */
    private long mDownTime;

    private Slider mSlider;

    private BookViewPagerAdapter mAdapter;

    private Parcelable mRestoredAdapterState = null;
    private ClassLoader mRestoredClassLoader = null;

    private OnTapListener mOnTapListener;
    private OnPageChangeListener mPageChangeListener;

    public BookViewPager(Context context) {
        super(context);
    }

    public BookViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BookViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSlider(Slider mSlider) {
        this.mSlider = mSlider;
        mSlider.init(this);
        resetFromAdapter();
    }

    public void setAdapter(BookViewPagerAdapter mAdapter) {
        this.mAdapter = mAdapter;
        mAdapter.setBookViewPager(this);
        if (mRestoredAdapterState != null) {
            mAdapter.restoreState(mRestoredAdapterState, mRestoredClassLoader);
            mRestoredAdapterState = null;
            mRestoredClassLoader = null;
        }
        resetFromAdapter();
        postInvalidate();
    }

    public BookViewPagerAdapter getAdapter() {
        return mAdapter;
    }


    public void resetFromAdapter() {
        removeAllViews();
        if (mSlider != null && mAdapter != null) {
            mSlider.resetFromAdapter(mAdapter);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            child.layout(0, 0, width, height);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                mDownTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                computeClickMotion(event);
                break;
        }
        return mSlider.onTouchEvent(event) || super.onTouchEvent(event);
    }


    private void computeClickMotion(MotionEvent event) {
        if (mOnTapListener == null) {
            return;
        }
        int xDiff = Math.abs((int) event.getX() - mDownX);
        int yDiff = Math.abs((int) event.getY() - mDownY);
        long timeDiff = System.currentTimeMillis() - mDownTime;
//        Log.e("tt", "xDiff = " + xDiff);
//        Log.e("tt", "yDiff = " + yDiff);
//        Log.e("tt", "mDownMotionX = " + mDownX);
//        Log.e("tt", "mDownMotionY = " + mDownY);
//        Log.e("tt", "timeDiff = " + timeDiff);
        /**
         * 如果不满足下面条件就认为不是点击事件，属于滑动事件
         */
        if (xDiff < 5 && yDiff < 5 && timeDiff < 200) {
            mOnTapListener.onSingleTap(event);
        }
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mSlider != null) {
            mSlider.computeScroll();
        }
    }


    public void slideNext() {
        if (mSlider != null) {
            mSlider.slideNext();
        }
    }

    public void slidePrevious() {
        if (mSlider != null) {
            mSlider.slidePrevious();
        }
    }


    /**
     * 点击监听
     */
    public interface OnTapListener {
        /**
         * 单击
         */
        void onSingleTap(MotionEvent event);
    }

    public void setOnTapListener(OnTapListener onTapListener) {
        this.mOnTapListener = onTapListener;
    }


    /**
     * 页面变更监听
     */
    public interface OnPageChangeListener {

        /**
         * 页面状态改变
         *
         * @param touchResult
         */
        void onPageScrollStateChanged(int touchResult);

        /**
         * 页面选中
         *
         * @param obj
         */
        void onPageSelected(Object obj);
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.mPageChangeListener = onPageChangeListener;
    }


    public void pageScrollStateChanged(int moveDirection) {
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageScrollStateChanged(moveDirection);
        }
    }


    public void pageSelected(Object object) {
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageSelected(object);
        }
    }


    @Override
    protected Parcelable onSaveInstanceState() {

        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        if (mAdapter != null) {
            savedState.adapterState = mAdapter.saveState();
        }
        return savedState;
    }


    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        if (mAdapter != null) {
            mAdapter.restoreState(savedState.adapterState, savedState.loader);
            resetFromAdapter();
        } else {
            mRestoredAdapterState = savedState.adapterState;
            mRestoredClassLoader = savedState.loader;
        }
    }

    public static class SavedState extends BaseSavedState {
        Parcelable adapterState;
        ClassLoader loader;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeParcelable(adapterState, flags);
        }

        @Override
        public String toString() {
            return "BaseBookViewPager.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + "}";
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        });

        SavedState(Parcel in, ClassLoader loader) {
            super(in);
            if (loader == null) {
                loader = getClass().getClassLoader();
            }
            adapterState = in.readParcelable(loader);
            this.loader = loader;
        }
    }

}
