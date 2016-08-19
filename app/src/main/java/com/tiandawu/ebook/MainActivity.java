package com.tiandawu.ebook;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.tiandawu.ebook.slider.CoverPageSlider;
import com.tiandawu.ebook.slider.ViewPagerSlider;

public class MainActivity extends AppCompatActivity {

    private BookViewPager mBookViewPager;
    private Button changeMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        hideStatusBar(this);
        setContentView(R.layout.activity_main);
        changeMode = (Button) findViewById(R.id.change_mode);
        mBookViewPager = (BookViewPager) findViewById(R.id.bookViewPager);
        /**
         * 默认翻译模式为水平滑动
         */
        mBookViewPager.setAdapter(new MyAdapter());
        mBookViewPager.setSlider(new ViewPagerSlider());
        setClick();


        changeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("选择翻页模式").setItems(new String[]{"水平滑动", "覆盖翻页", "竖直翻页"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0://水平滑动
                                        mBookViewPager.setAdapter(new MyAdapter());
                                        mBookViewPager.setSlider(new ViewPagerSlider());
                                        setClick();
                                        break;
                                    case 1://覆盖翻页
                                        mBookViewPager.setAdapter(new MyAdapter());
                                        mBookViewPager.setSlider(new CoverPageSlider());
                                        setClick();
                                        break;
                                    case 2://竖直翻页
                                        mBookViewPager.setListViewAdapter(new MyListAdapter());
                                        break;
                                }
                            }
                        });

                builder.create().show();
            }

        });


    }

    private void setClick() {
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
            return "第" + (index + 1) + "页！";
        }

        @Override
        public String getPreviousContent() {
            return "第" + (index - 1) + "页！";
        }

        @Override
        public boolean hasNextContent() {
            return index < 5;
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
        private String[] datas = new String[5];

        public MyListAdapter() {
            for (int i = 0; i < 5; i++) {
                datas[i] = "第" + i + "页";
            }
        }

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
            if (view == null) {
                view = View.inflate(MainActivity.this, R.layout.item_list, null);
            }
            TextView showText = (TextView) view.findViewById(R.id.show_text);
            showText.setText(datas[i]);
            return view;
        }
    }

}
