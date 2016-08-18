package com.tiandawu.ebook;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private BookViewPager mBookViewPager;
    private String[] datas = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideStatusBar(this);
        setContentView(R.layout.activity_main);
        mBookViewPager = (BookViewPager) findViewById(R.id.bookViewPager);
        for (int i = 0; i < 5; i++) {
            datas[i] = "第" + i + "页";
//            Log.e("tt", "data ===== " + datas[i]);
        }

        mBookViewPager.setListViewAdapter(new MyListAdapter());
//        mBookViewPager.setAdapter(new MyAdapter());
//        mBookViewPager.setSlider(new VerticalPageSlider());
//        mBookViewPager.setOnTapListener(new BookViewPager.OnTapListener() {
//            @Override
//            public void onSingleTap(MotionEvent event) {
//                int screenWidth = getResources().getDisplayMetrics().widthPixels;
//                int x = (int) event.getX();
//                if (x > screenWidth / 2) {
//                    mBookViewPager.slideNext();
////                    Log.e("tt", "++++++++");
//                } else if (x <= screenWidth / 2) {
//                    mBookViewPager.slidePrevious();
////                    Log.e("tt", "---------");
//                }
//            }
//        });
    }

    private class MyAdapter extends BookViewPagerAdapter<String> {

        private int index = 0;
        private int count = 3;

        @Override
        public int getPages() {
            return count;
        }

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
//            Log.e("tt", "getNextContent = " + "第" + index + "页！");
            return "第" + index + "页！";
        }

        @Override
        public String getNextContent() {
//            Log.e("tt", "getNextContent = " + "第" + (index + 1) + "页！");
            return "第" + (index + 1) + "页！";
        }

        @Override
        public String getPreviousContent() {
//            Log.e("tt", "getPreviousContent = " + "第" + (index - 1) + "页！");
            return "第" + (index - 1) + "页！";
        }

        @Override
        public boolean hasNextContent() {
//            Log.e("tt", "hasNextContent = " + index);
            return index < count;
        }

        @Override
        public boolean hasPreviousContent() {
//            Log.e("tt", "hasPreviousContent = " + index);
            return index > 0;
        }

        @Override
        protected void computeNext() {
            ++index;
            Log.e("tt", "computeNext = " + index);
        }

        @Override
        protected void computePrevious() {
            --index;
            Log.e("tt", "computePrevious = " + index);
        }
    }

    /**
     * 隐藏状态栏
     *
     * @param activity
     */
    private void hideStatusBar(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    public class MyListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return datas.length;
        }

        @Override
        public Object getItem(int i) {
            return datas[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

//            for (int n = 0; n < datas.length; n++) {
//                Log.e("tt", "n = " + n);
//                Log.e("tt", "data = " + datas[n]);
//            }
            Log.e("tt", "n = " + i);
            Log.e("tt", "data = " + datas[i]);
            if (view == null) {
                view = View.inflate(MainActivity.this, R.layout.item_list, null);
            }
            TextView showText = (TextView) view.findViewById(R.id.show_text);
            showText.setText(datas[i]);
            return view;
        }
    }

}
