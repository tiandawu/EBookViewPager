package com.tiandawu.bookviewpager.slider;

import android.view.MotionEvent;

import com.tiandawu.bookviewpager.BookViewPager;
import com.tiandawu.bookviewpager.BookViewPagerAdapter;

/**
 * Created by tiandawu on 2016/8/1.
 */
public interface Slider {
    /**
     * 初始化
     *
     * @param mBookViewPager
     */
    void init(BookViewPager mBookViewPager);

    /**
     * 滑动估算
     */
    void computeScroll();

    /**
     * 滑动到下一页
     */
    void slideNext();

    /**
     * 滑动到上一页
     */
    void slidePrevious();

    /**
     * 触摸事件
     *
     * @param event
     * @return
     */
    boolean onTouchEvent(MotionEvent event);

    /**
     * 重置Adapter
     *
     * @param mAdapter
     */
    void resetFromAdapter(BookViewPagerAdapter mAdapter);
}
