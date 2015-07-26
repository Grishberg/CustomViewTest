package info.goodline.btv.ui.activity;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import info.goodline.btv.android_btvc.R;
import info.goodline.btv.ui.adapters.TestPageAdapter;
import info.goodline.btv.ui.view.CustomViewPager;

public class MainActivity extends FragmentActivity {
    boolean isVisible = false;
    String[] arr = {"test1", "test string 2", "test string 3"};
    int index = 0;
    CustomViewPager customViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customViewPager = (CustomViewPager) findViewById(R.id.viewpager);
        TestPageAdapter mAdapter = new TestPageAdapter(getSupportFragmentManager());
        customViewPager.setAdapter(mAdapter);
        customViewPager.setCurrentItem(0);
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
