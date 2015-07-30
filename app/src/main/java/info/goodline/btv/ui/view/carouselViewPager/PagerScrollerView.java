package info.goodline.btv.ui.view.carouselViewPager;

import android.content.Context;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by g on 29.07.15.
 */
public class PagerScrollerView extends FrameLayout implements
        LoopViewPager.IOnManualPageChangeListener{
    private static final String TAG = PagerScrollerView.class.getSimpleName();
    private static final int DURATION = 2000;
    private LoopViewPager viewPager;
    private Handler mHandler;
    private int mNextItemIndex = 1;
    private PagerAdapter mAdapter;


    public PagerScrollerView(Context context){
        super(context);
        init();
    }

    public PagerScrollerView(Context context, AttributeSet attrs){
        super((context));
        init();
    }

    private void init(){
        viewPager = new LoopViewPager(getContext());
        viewPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

    }

    public void setAdapter(PagerAdapter adapter){
        viewPager.setAdapter(adapter);
        mAdapter = adapter;
        //start countdown timer
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, DURATION);

    }

    @Override
    public void onManualPageChanged() {
        //TODO: restart timer
        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable, DURATION);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            /** Do something **/
            viewPager.setCurrentItemAutomatic(mNextItemIndex++);
            if(mNextItemIndex > mAdapter.getCount()){
                mNextItemIndex = 1;
            }
            mHandler.postDelayed(mRunnable, DURATION);
        }
    };
}
