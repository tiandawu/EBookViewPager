package com.tiandawu.ebook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.tiandawu.bookviewpager.BookViewPager;
import com.tiandawu.bookviewpager.BookViewPagerAdapter;
import com.tiandawu.bookviewpager.slider.CoverPageSlider;

public class MainActivity extends AppCompatActivity {

    private BookViewPager mBookViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBookViewPager = (BookViewPager) findViewById(R.id.bookViewPager);
        mBookViewPager.setAdapter(new MyAdapter());
        mBookViewPager.setSlider(new CoverPageSlider());
        mBookViewPager.setOnTapListener(new BookViewPager.OnTapListener() {
            @Override
            public void onSingleTap(MotionEvent event) {
                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                int x = (int) event.getX();
                if (x > screenWidth / 2) {
                    mBookViewPager.slideNext();
                } else if (x <= screenWidth / 2) {
                    mBookViewPager.slidePrevious();
                }
            }
        });
    }

    private class MyAdapter extends BookViewPagerAdapter<String> {

        private int index = 0;

        @Override
        public View getView(View convertView, String s) {
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.page_content, null);
            }

            TextView textView = (TextView) convertView.findViewById(R.id.text);
            textView.setText(s);
            return convertView;
        }

        @Override
        public String getCurrentContent() {
            return "第" + index + "页！";
        }

        @Override
        public String getNextContent() {
            Log.e("tt", "indexNext = " + index);
            return "第" + (index + 1) + "页！";
        }

        @Override
        public String getPreviousContent() {
            Log.e("tt", "indexPrev = " + index);
            return "第" + (index - 1) + "页！";
        }

        @Override
        public boolean hasNextContent() {
            return index < 10;
        }

        @Override
        public boolean hasPreviousContent() {
            Log.e("tt", "index = " + index);
            return index > 0;
        }

        @Override
        protected void computeNext() {
            ++index;
        }

        @Override
        protected void computePrevious() {
            --index;
        }
    }

}
