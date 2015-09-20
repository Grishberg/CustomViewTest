package info.goodline.btv.ui.activity;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.zip.Inflater;

import info.goodline.btv.android_btvc.R;
import info.goodline.btv.ui.adapters.ScrollerPagerAdapter;
import info.goodline.btv.ui.view.SwipePanel;
import info.goodline.btv.ui.view.carouselViewPager.PagerScrollerView;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    boolean isVisible = false;
    PagerScrollerView pager;
    ScrollerPagerAdapter adapter;
    Button btnNext;
    SwipePanel panel1, panel2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnNext = (Button) findViewById(R.id.btnNextScreen);
        btnNext.setOnClickListener(this);

        Button button1 = new Button(this);
        button1.setText("button1");
        LayoutInflater inflater = getLayoutInflater();
        View viewMain = inflater.inflate(R.layout.view_custom_main, null,false);
        View viewLeft = inflater.inflate(R.layout.view_custom, null,false);
        panel1 = (SwipePanel) findViewById(R.id.swipe1);
        panel1.init(viewMain, viewLeft);

        button1 = new Button(getApplication().getApplicationContext());
        button1.setText("button1");
    }

    @Override
    public void onClick(View v) {
        SecondActivity.startActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(btnNext != null){
            btnNext.setOnClickListener(null);
        }
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
