package info.goodline.btv.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import info.goodline.btv.ui.ICanScrollInterface;

/**
 * Created by g on 25.07.15.
 */
public class CustomViewPager extends ViewPager {
    private static final String TAG = CustomViewPager.class.getSimpleName();
    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        Log.d(TAG, "onScroll checkV="+checkV+"  dx="+dx+" x="+x+" y="+y);
        if(v instanceof ICanScrollInterface){
            return ((ICanScrollInterface)v).isCanScroll(x);
        }
        return super.canScroll(v, checkV, dx, x, y);
    }
}
