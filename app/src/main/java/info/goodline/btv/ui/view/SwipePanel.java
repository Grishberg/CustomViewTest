package info.goodline.btv.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import info.goodline.btv.android_btvc.R;
import info.goodline.btv.ui.ICanScrollInterface;

/**
 * Created by g on 22.07.15.
 */
public class SwipePanel extends LinearLayout implements View.OnTouchListener
        , GestureDetector.OnGestureListener, ICanScrollInterface {
    private static final String TAG = SwipePanel.class.getSimpleName();
    private static final float SCROLL = 0.15f;
    private int mRightPanelWidth;
    private int mLeftPanelWidth;
    LinearLayout llLeftPanel;
    LinearLayout llRightPanel;
    private int mLastX;
    private float mScale;
    private boolean mIsExpanded;
    private View mMainView;
    private View mLeftView;
    private boolean mIsHidded;
    private GestureDetector mDetector;

    public SwipePanel(Context context) {
        super(context);
        inflate(getContext(), R.layout.view_swipe_panel, this);
    }

    public SwipePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_swipe_panel, this);
    }

    public void init(View view1, View view2) {
        init(view1, view2, view1);
    }

    public void init(View view1, View view2, View swipeView) {
        mScale = getContext().getResources().getDisplayMetrics().density;
        mDetector = new GestureDetector(getContext(), this);
        llLeftPanel = (LinearLayout) findViewById(R.id.llLeft);
        llRightPanel = (LinearLayout) findViewById(R.id.llMain);
        mIsHidded = true;
        mMainView = view1;
        mLeftView = view2;
        llRightPanel.addView(mMainView);
        llLeftPanel.addView(mLeftView);
        llLeftPanel.setVisibility(GONE);
        swipeView.setOnTouchListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(mRightPanelWidth == 0) {
            mRightPanelWidth = getMeasuredWidth();

        }
        if(mLeftPanelWidth == 0) {
            mLeftView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            mLeftPanelWidth = mLeftView.getMeasuredWidth();
        }
        setMeasuredDimension(mRightPanelWidth + mLeftPanelWidth, getMeasuredHeight());
    }

    private void changeLeftMargin(int offset) {
        if (getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) getLayoutParams();
            p.setMargins(offset, p.topMargin, p.rightMargin, p.bottomMargin);
            requestLayout();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mIsHidded) {
            mIsHidded = false;
            llLeftPanel.setVisibility(VISIBLE);
            changeLeftMargin(-mLeftPanelWidth);
        }
        return this.mDetector.onTouchEvent(event);
    }

    @Override
    public boolean isCanScroll(int x) {
        int width = getWidth();
        int slideWidth = Math.round(width * SCROLL);
        if (x < slideWidth || x > width - slideWidth) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        Log.d(TAG, "onFling: " + event1.toString() + event2.toString());
        if (velocityX > 0 && !mIsExpanded) {
            //move right
            animate().translationX(mLeftPanelWidth).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mIsExpanded = true;
                }
            }).start();
        } else if(mIsExpanded){
            // move left
            animate().translationX(0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mIsExpanded = false;
                }
            }).start();
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return true;
    }
}
