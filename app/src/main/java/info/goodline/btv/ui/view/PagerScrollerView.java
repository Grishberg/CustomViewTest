package info.goodline.btv.ui.view;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by g on 29.07.15.
 */
public class PagerScrollerView extends ViewPager{
    private static final String TAG = PagerScrollerView.class.getSimpleName();
    private static final boolean DEFAULT_BOUNDARY_CASHING = false;

    OnPageChangeListener mOuterPageChangeListener;
    private LoopPagerAdapterWrapper mAdapter;
    private boolean mBoundaryCaching = DEFAULT_BOUNDARY_CASHING;


    /**
     * helper function which may be used when implementing FragmentPagerAdapter
     *
     * @param position
     * @param count
     * @return (position-1)%count
     */
    public static int toRealPosition( int position, int count ){
        position = position-1;
        if( position < 0 ){
            position += count;
        }else{
            position = position%count;
        }
        return position;
    }

    /**
     * If set to true, the boundary views (i.e. first and last) will never be destroyed
     * This may help to prevent "blinking" of some views
     *
     * @param flag
     */
    public void setBoundaryCaching(boolean flag) {
        mBoundaryCaching = flag;
        if (mAdapter != null) {
            mAdapter.setBoundaryCaching(flag);
        }
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        mAdapter = new LoopPagerAdapterWrapper(adapter);
        mAdapter.setBoundaryCaching(mBoundaryCaching);
        super.setAdapter(mAdapter);
        setCurrentItem(0, false);
    }

    @Override
    public PagerAdapter getAdapter() {
        return mAdapter != null ? mAdapter.getRealAdapter() : mAdapter;
    }

    @Override
    public int getCurrentItem() {
        return mAdapter != null ? mAdapter.toRealPosition(super.getCurrentItem()) : 0;
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        int realItem = mAdapter.toInnerPosition(item);
        super.setCurrentItem(realItem, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        if (getCurrentItem() != item) {
            setCurrentItem(item, true);
            //TODO: restart timer
        }
    }

    public void setCurrentItemAutomatic(int item) {
        if (getCurrentItem() != item) {
            setCurrentItem(item, true);
        }
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mOuterPageChangeListener = listener;
    };

    public PagerScrollerView(Context context) {
        super(context);
        init();
    }

    public PagerScrollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        super.setOnPageChangeListener(onPageChangeListener);
    }

    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        private float mPreviousOffset = -1;
        private float mPreviousPosition = -1;

        @Override
        public void onPageSelected(int position) {

            int realPosition = mAdapter.toRealPosition(position);
            if (mPreviousPosition != realPosition) {
                mPreviousPosition = realPosition;
                if (mOuterPageChangeListener != null) {
                    mOuterPageChangeListener.onPageSelected(realPosition);
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            int realPosition = position;
            if (mAdapter != null) {
                realPosition = mAdapter.toRealPosition(position);

                if (positionOffset == 0
                        && mPreviousOffset == 0
                        && (position == 0 || position == mAdapter.getCount() - 1)) {
                    setCurrentItem(realPosition, false);
                }
            }

            mPreviousOffset = positionOffset;
            if (mOuterPageChangeListener != null) {
                if (realPosition != mAdapter.getRealCount() - 1) {
                    mOuterPageChangeListener.onPageScrolled(realPosition,
                            positionOffset, positionOffsetPixels);
                } else {
                    if (positionOffset > .5) {
                        mOuterPageChangeListener.onPageScrolled(0, 0, 0);
                    } else {
                        mOuterPageChangeListener.onPageScrolled(realPosition,
                                0, 0);
                    }
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mAdapter != null) {
                int position = PagerScrollerView.super.getCurrentItem();
                int realPosition = mAdapter.toRealPosition(position);
                if (state == ViewPager.SCROLL_STATE_IDLE
                        && (position == 0 || position == mAdapter.getCount() - 1)) {
                    setCurrentItem(realPosition, false);
                }
            }
            if (mOuterPageChangeListener != null) {
                mOuterPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    };

    public static class LoopPagerAdapterWrapper extends PagerAdapter {

        private PagerAdapter mAdapter;

        private SparseArray<ToDestroy> mToDestroy = new SparseArray<ToDestroy>();

        private boolean mBoundaryCaching;

        void setBoundaryCaching(boolean flag) {
            mBoundaryCaching = flag;
        }

        LoopPagerAdapterWrapper(PagerAdapter adapter) {
            this.mAdapter = adapter;
        }

        @Override
        public void notifyDataSetChanged() {
            mToDestroy = new SparseArray<ToDestroy>();
            super.notifyDataSetChanged();
        }

        int toRealPosition(int position) {
            int realCount = getRealCount();
            if (realCount == 0)
                return 0;
            int realPosition = (position-1) % realCount;
            if (realPosition < 0)
                realPosition += realCount;

            return realPosition;
        }

        public int toInnerPosition(int realPosition) {
            int position = (realPosition + 1);
            return position;
        }

        private int getRealFirstPosition() {
            return 1;
        }

        private int getRealLastPosition() {
            return getRealFirstPosition() + getRealCount() - 1;
        }

        @Override
        public int getCount() {
            return mAdapter.getCount() + 2;
        }

        public int getRealCount() {
            return mAdapter.getCount();
        }

        public PagerAdapter getRealAdapter() {
            return mAdapter;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int realPosition = (mAdapter instanceof FragmentPagerAdapter || mAdapter instanceof FragmentStatePagerAdapter)
                    ? position
                    : toRealPosition(position);

            if (mBoundaryCaching) {
                ToDestroy toDestroy = mToDestroy.get(position);
                if (toDestroy != null) {
                    mToDestroy.remove(position);
                    return toDestroy.object;
                }
            }
            return mAdapter.instantiateItem(container, realPosition);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            int realFirst = getRealFirstPosition();
            int realLast = getRealLastPosition();
            int realPosition = (mAdapter instanceof FragmentPagerAdapter || mAdapter instanceof FragmentStatePagerAdapter)
                    ? position
                    : toRealPosition(position);

            if (mBoundaryCaching && (position == realFirst || position == realLast)) {
                mToDestroy.put(position, new ToDestroy(container, realPosition,
                        object));
            } else {
                mAdapter.destroyItem(container, realPosition, object);
            }
        }

    /*
     * Delegate rest of methods directly to the inner adapter.
     */

        @Override
        public void finishUpdate(ViewGroup container) {
            mAdapter.finishUpdate(container);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return mAdapter.isViewFromObject(view, object);
        }

        @Override
        public void restoreState(Parcelable bundle, ClassLoader classLoader) {
            mAdapter.restoreState(bundle, classLoader);
        }

        @Override
        public Parcelable saveState() {
            return mAdapter.saveState();
        }

        @Override
        public void startUpdate(ViewGroup container) {
            mAdapter.startUpdate(container);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            mAdapter.setPrimaryItem(container, position, object);
        }

    /*
     * End delegation
     */

    }
    /**
     * Container class for caching the boundary views
     */
    static class ToDestroy {
        ViewGroup container;
        int position;
        Object object;

        public ToDestroy(ViewGroup container, int position, Object object) {
            this.container = container;
            this.position = position;
            this.object = object;
        }
    }
}
