package info.goodline.btv.ui.activity;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import info.goodline.btv.android_btvc.R;
import info.goodline.btv.ui.adapters.ScrollerPagerAdapter;
import info.goodline.btv.ui.adapters.TestPageAdapter;
import info.goodline.btv.ui.view.CustomViewPager;
import info.goodline.btv.ui.view.PagerScrollerView;

public class MainActivity extends FragmentActivity {
    boolean isVisible = false;
    Handler mHandler;
    PagerScrollerView pager;
    ScrollerPagerAdapter adapter;
    int mNextItemIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager = (PagerScrollerView)findViewById(R.id.viewpager);
        adapter = new ScrollerPagerAdapter(getApplicationContext());
        pager.setAdapter(adapter);
        //start countdown timer
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 1000);

    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            /** Do something **/
            pager.setCurrentItemAutomatic(mNextItemIndex++);
            if(mNextItemIndex > adapter.getCount()){
                mNextItemIndex = 1;
            }
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
