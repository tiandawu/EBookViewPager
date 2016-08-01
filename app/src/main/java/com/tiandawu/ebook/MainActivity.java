package com.tiandawu.ebook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tiandawu.bookviewpager.BookViewPager;
import com.tiandawu.bookviewpager.BookViewPagerAdapter;
import com.tiandawu.bookviewpager.slider.ViewPagerSlider;

public class MainActivity extends AppCompatActivity {

    private BookViewPager mBookViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBookViewPager = (BookViewPager) findViewById(R.id.bookViewPager);
        mBookViewPager.setSlider(new ViewPagerSlider());
        mBookViewPager.setAdapter(new MyAdapter());
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
            Log.e("tt", "----------");
            return convertView;
        }

        @Override
        public String getCurrentContent() {
            return "第" + index + "页！";
        }

        @Override
        public String getNextContent() {
            return "第" + (index + 1) + "页！";
        }

        @Override
        public String getPreviousContent() {
            return "第" + (index - 1) + "页！";
        }

        @Override
        public boolean hasNextContent() {
            return index < 10;
        }

        @Override
        public boolean hasPreviousContent() {
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
