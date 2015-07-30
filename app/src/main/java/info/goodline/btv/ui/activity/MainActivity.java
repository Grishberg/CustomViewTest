package info.goodline.btv.ui.activity;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import info.goodline.btv.android_btvc.R;
import info.goodline.btv.ui.adapters.ScrollerPagerAdapter;
import info.goodline.btv.ui.view.carouselViewPager.PagerScrollerView;

public class MainActivity extends FragmentActivity {
    boolean isVisible = false;
    PagerScrollerView pager;
    ScrollerPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.tvText);

        pager = (PagerScrollerView)findViewById(R.id.loopviewpager);
        adapter = new ScrollerPagerAdapter(getApplicationContext());
        pager.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pager.release();
    }
}
