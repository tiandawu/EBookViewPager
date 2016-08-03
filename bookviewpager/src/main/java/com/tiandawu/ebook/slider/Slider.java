package com.tiandawu.ebook.slider;

import android.view.MotionEvent;

import com.tiandawu.ebook.BookViewPager;
import com.tiandawu.ebook.BookViewPagerAdapter;

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
     * 初始化放置View
     *
     * @param mAdapter
     */
    void resetFromAdapter(BookViewPagerAdapter mAdapter);

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

}
