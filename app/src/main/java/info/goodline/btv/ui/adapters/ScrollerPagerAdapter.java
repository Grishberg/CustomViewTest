package info.goodline.btv.ui.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import info.goodline.btv.android_btvc.R;

/**
 * Created by g on 29.07.15.
 */
public class ScrollerPagerAdapter extends PagerAdapter {
    private static final String TAG = ScrollerPagerAdapter.class.getSimpleName();
    ArrayList<ViewGroup> views;
    LayoutInflater inflater;

    public ScrollerPagerAdapter(Context context) {
        super();
        inflater = LayoutInflater.from(context);
        //instantiate your views list
        views = new ArrayList<ViewGroup>(5);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewGroup currentView;
        Log.e(TAG, "instantiateItem for " + position);
        if (views.size() > position && views.get(position) != null) {
            Log.e(TAG, "instantiateItem views.get(position) " + views.get(position));
            currentView = views.get(position);
        } else {
            Log.e(TAG, "instantiateItem need to create the View");
            int rootLayout = R.layout.view_custom_main;
            currentView = (ViewGroup) inflater.inflate(rootLayout, container, false);

            ((TextView) currentView.findViewById(R.id.tvText)).setText("My Views " + position);
        }
        container.addView(currentView);
        return currentView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);

    }
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }
}
