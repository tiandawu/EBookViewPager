package com.tiandawu.bookviewpager;

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


    public void setBookViewPager(BookViewPager mBookViewPager) {
        this.mBookViewPager = mBookViewPager;
    }


    public abstract View getView(View convertView, T t);

    /**
     * 获取当前页内容
     *
     * @return t 内容
     */
    public abstract T getCurrentContent();

    public abstract T getNextContent();

    public abstract T getPreviousContent();

    public abstract boolean hasNextContent();

    public abstract boolean hasPreviousContent();

    protected abstract void computeNextContent();

    protected abstract void computePreviousContent();
}
