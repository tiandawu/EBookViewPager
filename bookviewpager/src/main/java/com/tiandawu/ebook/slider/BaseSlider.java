package com.tiandawu.ebook.slider;

/**
 * Created by tiandawu on 2016/8/1.
 */
public abstract class BaseSlider  implements Slider {

    /**
     * 手指移动的方向
     */
    public static final int MOVE_TO_LEFT = 0;  // Move to next
    public static final int MOVE_TO_RIGHT = 1; // Move to previous
    public static final int MOVE_NO_RESULT = 2;

    /**
     * 触摸的模式
     */
    static final int MODE_NONE = 0;
    static final int MODE_MOVE = 1;

}
