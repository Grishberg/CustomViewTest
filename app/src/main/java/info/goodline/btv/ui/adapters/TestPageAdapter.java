package info.goodline.btv.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import info.goodline.btv.ui.fragment.BlankFragment;
import info.goodline.btv.ui.fragment.MainFragment;

/**
 * Created by g on 25.07.15.
 */
public class TestPageAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 2;
    private static final int PAGE_MAIN = 0;
    private static final int PAGE_SECOND = 1;

    public TestPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case PAGE_MAIN:
                return MainFragment.newInstance("","");
            case PAGE_SECOND:
                return BlankFragment.newInstance("","");
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
