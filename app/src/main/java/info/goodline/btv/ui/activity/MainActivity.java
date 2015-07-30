package info.goodline.btv.ui.activity;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
        pager = (PagerScrollerView)findViewById(R.id.loopviewpager);
        adapter = new ScrollerPagerAdapter(getApplicationContext());
        pager.setAdapter(adapter);
    }



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
