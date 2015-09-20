package info.goodline.btv.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import info.goodline.btv.android_btvc.R;
import info.goodline.btv.ui.ICanScrollInterface;

/**
 * Created by g on 22.07.15.
 */
public class SwipePanel extends FrameLayout implements View.OnTouchListener
        , ICanScrollInterface {
    private static final String TAG = SwipePanel.class.getSimpleName();
    private static final float SCROLL = 0.15f;
    private int mRightPanelWidth;
    private int mLeftPanelWidth;
    private boolean mIsExpanded;
    private View mMainView;
    private View mLeftView;
    private boolean mIsHidded;
    private int mStartX, mStartY;
    private boolean mIsDragging, mIsDisallowed;
    private int mCurrentOffset;

    public SwipePanel(Context context) {
        this(context, null);
    }

    public SwipePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_swipe_panel, this);
    }

    public void init(View view1, View view2) {
        mIsHidded = true;
        mMainView = view1;
        mLeftView = view2;
        addView(mLeftView);
        addView(mMainView);
        setOnTouchListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l , t, r, b);
        if (mLeftView != null) {
            mLeftView.layout(l, t, mLeftPanelWidth, b);
        }
        if (mMainView != null) {
            mMainView.layout(l+mLeftPanelWidth, t, r+mLeftPanelWidth, b);
        }
        changeLeftMargin(this, -mLeftPanelWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mRightPanelWidth == 0) {
            mRightPanelWidth = getMeasuredWidth();
        }
        if (mLeftPanelWidth == 0) {

            if (mLeftView != null) {
                mLeftView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                mLeftPanelWidth = mLeftView.getMeasuredWidth();
            }
        }
        setMeasuredDimension(mRightPanelWidth + mLeftPanelWidth, getMeasuredHeight());
    }

    private void changeLeftMargin(View view, int offset) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(offset, p.topMargin, p.rightMargin, p.bottomMargin);
            view.requestLayout();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int x = (int) event.getRawX();
        final int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsDragging = true;
                mStartX = x;
                mStartY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsDragging) {
                    onActionMove(x, y);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsDragging) {
                    onActionUp(x, y);
                }
                break;
        }
        return true;
    }

    private void onActionMove(int x, int y) {
        final int deltaX = x - mStartX;
        final int deltaY = y - mStartY;
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            getParent().requestDisallowInterceptTouchEvent(true);
            mIsDisallowed = true;
        }
        if(Math.abs(deltaX) <= Math.abs(mLeftPanelWidth)) {
            setTranslationX(mCurrentOffset + deltaX);
        }
    }

    private void onActionUp(int x, int y) {
        final int deltaX = x - mStartX;
        final int deltaY = y - mStartY;

        if (mIsDisallowed) {
            getParent().requestDisallowInterceptTouchEvent(false);
            mIsDisallowed = false;
        }
        if (deltaX < mLeftPanelWidth / 2) {
            animateToLeft();
        } else {
            animateToRight();
        }
    }

    private void animateToLeft() {
        animate().translationX(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mCurrentOffset = 0;
                mIsExpanded = false;
            }
        }).start();
    }

    private void animateToRight() {
        animate().translationX(mLeftPanelWidth).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mCurrentOffset = mLeftPanelWidth;
                mIsExpanded = true;
            }
        }).start();
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

    public interface IOnSwypeListener {
        void onSwype();
    }
}
