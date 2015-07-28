package info.goodline.btv.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import info.goodline.btv.android_btvc.R;
import info.goodline.btv.ui.ICanScrollInterface;

/**
 * Created by g on 23.07.15.
 */
public class RatingDraggablePanel extends LinearLayout implements View.OnTouchListener,
        ICanScrollInterface {
    private static final String TAG = SwipePanel.class.getSimpleName();
    private static final float SCROLL = 0.15f;
    private int mRightPanelWidth;
    private int mLeftPanelWidth;
    private int mDraggWidth;
    private int mDraggContainerWidth;
    private int mDraggContainerLeftOffset;
    LinearLayout llLeftPanel;
    LinearLayout llRightPanel;
    private View mMainView;
    private View mDraggView;
    private int mLastX;
    private int mLastY;
    private float m;
    private int mLeftMargin;
    private boolean mIsHidded;
    private boolean mIsMoving;

    public RatingDraggablePanel(Context context) {
        super(context);
        inflate(getContext(), R.layout.view_rating_draggable_panel, this);
        init();
    }

    public RatingDraggablePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_rating_draggable_panel, this);
        init();
    }

    private void init() {
        llLeftPanel = (LinearLayout) findViewById(R.id.llLeft);
        llRightPanel = (LinearLayout) findViewById(R.id.llMain);
        llLeftPanel.setVisibility(GONE);
        mIsHidded = true;
    }

    public void init(View mainPanel, View dragContainer) {
        mMainView = mainPanel;
        mDraggView = dragContainer;
        llRightPanel.addView(mMainView);
        mDraggView.setOnTouchListener(this);
        mDraggView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mDraggContainerLeftOffset == 0) {
                    Rect outRect = new Rect();
                    int[] location = new int[2];

                    mDraggView.getDrawingRect(outRect);
                    mDraggView.getLocationOnScreen(location);
                    outRect.offset(location[0], location[1]);
                    mDraggContainerLeftOffset = outRect.left;
                    mDraggContainerWidth = outRect.right - outRect.left;
                    mDraggWidth = mRightPanelWidth - mDraggContainerLeftOffset - mDraggContainerWidth;
                    changeLeftPanelWidth(mDraggWidth);
                    setMeasuredDimension(mRightPanelWidth + mDraggWidth, getMeasuredHeight());
                }
            }
        });
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mRightPanelWidth == 0) {
            mRightPanelWidth = getMeasuredWidth();
            changeRightPanelWidth(mRightPanelWidth);
        }

        setMeasuredDimension(mRightPanelWidth * 2, getMeasuredHeight());
    }

    private void changeRightPanelWidth(int width) {
        ViewGroup.LayoutParams params = mMainView.getLayoutParams();
        params.width = width;
        mMainView.setLayoutParams(params);
    }

    private void changeLeftPanelWidth(int width) {
        ViewGroup.LayoutParams params = llLeftPanel.getLayoutParams();
        params.width = width;
        llLeftPanel.setLayoutParams(params);
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
            changeLeftMargin(-mDraggWidth);
        }
        final int x = (int) event.getRawX();
        final int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onActionDown(x);
                return true;
            case MotionEvent.ACTION_MOVE:
                onActionMove(x, y);
                return true;
            case MotionEvent.ACTION_UP:
                onActionUp(x);
                return true;
        }
        return false;
    }

    private void onActionMove(int x, int y) {
        Log.d(TAG, "on move x=" + x);
        int deltaX = x - mLastX;
        int deltaY = y - mLastY;
        if (mIsMoving == false && Math.abs(deltaX) > Math.abs(deltaY)) {
            mDraggView.getParent().requestDisallowInterceptTouchEvent(true);
            mIsMoving = true;
        }
        mLastX = x;
        if (getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) getLayoutParams();
            mLeftMargin = p.leftMargin + deltaX;
            if (mLeftMargin > 0) {
                mLeftMargin = 0;
            } else if (mLeftMargin < -mDraggWidth) {
                mLeftMargin = -mDraggWidth;
            }
            Log.d(TAG, "leftMargin = " + mLeftMargin);
            p.setMargins(mLeftMargin, p.topMargin, p.rightMargin, p.bottomMargin);
            requestLayout();
        }
    }

    private void onActionDown(int x) {
        Log.d(TAG, "on down x=" + x);
        mLastX = x;
    }

    private void onActionUp(int x) {
        Log.d(TAG, "on up x=" + x);
        if (mIsMoving) {
            mDraggView.getParent().requestDisallowInterceptTouchEvent(false);
        }
        // move left
        final int newLeftMargin = -mDraggWidth;
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                changeLeftMargin((int) (newLeftMargin * interpolatedTime));
            }
        };
        a.setDuration(500); // in ms
        startAnimation(a);
        /*
        animate().translationX(-mDraggWidth).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        }).start();*/
    }
}