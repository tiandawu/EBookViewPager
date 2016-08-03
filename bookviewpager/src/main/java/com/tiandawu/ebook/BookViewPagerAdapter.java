package com.tiandawu.ebook;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

/**
 * Created by tiandawu on 2016/7/31.
 */
public abstract class BookViewPagerAdapter<T> {

    private View[] mViews;
    private int currentViewIndex;
    private BookViewPager mBookViewPager;

    public BookViewPagerAdapter() {
        mViews = new View[3];
        currentViewIndex = 0;
    }

    /**
     * 获取指定索引的View
     *
     * @param index 索引
     * @return 指定索引的View
     */
    private View getView(int index) {
        return mViews[(index + 3) % 3];
    }

    /**
     * 插入View到指定索引处
     *
     * @param index 索引
     * @param view  需要插入的View
     */
    private void setView(int index, View view) {
        mViews[(index + 3) % 3] = view;
    }


    /**
     * 获取当前页View
     *
     * @return 当前页View
     */
    public View getCurrentView() {
        View curView = mViews[currentViewIndex];
        if (curView == null) {
            curView = getView(null, getCurrentContent());
            mViews[currentViewIndex] = curView;
        }
        return curView;
    }


    /**
     * 获取下一页View
     *
     * @return 下一页View
     */
    public View getNextView() {
        View nextView = getView(currentViewIndex + 1);
        if (nextView == null && hasNextContent()) {
            nextView = getView(null, getNextContent());
            setView(currentViewIndex + 1, nextView);
        }
        return nextView;
    }

    /**
     * 获取上一页View
     *
     * @return 上一页View
     */
    public View getPreviousView() {
        View previousView = getView(currentViewIndex - 1);
        if (previousView == null && hasPreviousContent()) {
            previousView = getView(null, getPreviousContent());
            setView(currentViewIndex - 1, previousView);
        }
        return previousView;
    }


    /**
     * 获取更新后的当前页
     *
     * @return 更新后的当前页
     */
    public View getUpdatedCurrentView() {
        View curView = mViews[currentViewIndex];
        if (curView == null) {
            curView = getView(null, getCurrentContent());
            mViews[currentViewIndex] = curView;
        } else {
            View updateView = getView(curView, getCurrentContent());
            if (curView != updateView) {
                curView = updateView;
                mViews[currentViewIndex] = updateView;
            }
        }
        return curView;
    }

    /**
     * 获取更新后的下一页
     *
     * @return 更新后的下一页
     */
    public View getUpdatedNextView() {
        View nextView = getView(currentViewIndex + 1);
        boolean hasNext = hasNextContent();
        if (nextView == null && hasNext) {
            nextView = getView(null, getNextContent());
            setView(currentViewIndex + 1, nextView);
        } else if (hasNext) {
            View updateView = getView(nextView, getNextContent());
            if (nextView != updateView) {
                nextView = updateView;
                setView(currentViewIndex + 1, nextView);
            }
        }
        return nextView;
    }

    /**
     * 获取更新后的前一页
     *
     * @return 更新后的前一页
     */
    public View getUpdatedPreviousView() {
        View previousView = getView(currentViewIndex - 1);
        boolean hasPrevious = hasPreviousContent();
        if (previousView == null && hasPrevious) {
            previousView = getView(null, getPreviousContent());
            setView(currentViewIndex - 1, previousView);
        } else if (hasPrevious) {
            View updateView = getView(previousView, getPreviousContent());
            if (previousView != updateView) {
                previousView = updateView;
                setView(currentViewIndex - 1, previousView);
            }
        }
        return previousView;
    }


    /**
     * 移动到下一页
     */
    public void moveToNext() {
        computeNext();
        currentViewIndex = (currentViewIndex + 1) % 3;
    }

    /**
     * 移动到上一页
     */
    public void moveToPrevious() {
        computePrevious();
        currentViewIndex = (currentViewIndex + 2) % 3;
    }


    /**
     * 设置BookViewPager
     *
     * @param mBookViewPager
     */
    public void setBookViewPager(BookViewPager mBookViewPager) {
        this.mBookViewPager = mBookViewPager;
    }

    /**
     * 设置前一页
     *
     * @param view
     */
    public void setPreviousView(View view) {
        setView(currentViewIndex - 1, view);
    }

    /**
     * 设置下一页
     *
     * @param view
     */
    public void setNextView(View view) {
        setView(currentViewIndex + 1, view);
    }

    /**
     * 设置当前页
     *
     * @param view
     */
    public void setCurrentView(View view) {
        setView(currentViewIndex, view);
    }


    public Bundle saveState() {
        return null;
    }

    public void restoreState(Parcelable parcelable, ClassLoader loader) {
        currentViewIndex = 0;
        if (mViews != null) {
            mViews[0] = null;
            mViews[1] = null;
            mViews[2] = null;
        }
    }

    public void notifyDataSetChanged() {
        if (mBookViewPager != null) {
            mBookViewPager.resetFromAdapter();
            mBookViewPager.postInvalidate();
        }
    }


    public abstract View getView(View convertView, T t);

    /**
     * 获取当前页内容
     *
     * @return t 内容
     */
    public abstract T getCurrentContent();

    /**
     * 获取下一页内容
     *
     * @return t 内容
     */
    public abstract T getNextContent();

    /**
     * 获取前一页内容
     *
     * @return t 内容
     */
    public abstract T getPreviousContent();

    /**
     * 是否还有下一页内容
     *
     * @return true:有   false:没有
     */
    public abstract boolean hasNextContent();

    /**
     * 是否还有前一页内容
     *
     * @return true:有   false:没有
     */
    public abstract boolean hasPreviousContent();

    protected abstract void computeNext();

    protected abstract void computePrevious();
}
