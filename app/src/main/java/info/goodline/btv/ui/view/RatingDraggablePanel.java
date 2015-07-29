package info.goodline.btv.ui.view;

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

import info.goodline.btv.android_btvc.R;
import info.goodline.btv.ui.ICanScrollInterface;

/**
 * Created by g on 23.07.15.
 */
public class RatingDraggablePanel extends LinearLayout implements View.OnTouchListener,
        ICanScrollInterface {
    private static final String TAG = SwipePanel.class.getSimpleName();
    private static final float SCROLL = 0.15f;
    private static final int DURATION = 500;
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
    private IOnRateChangeListener mListener;
    private int mRate;

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
        mMainView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT));
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
                    changeLeftPanelSize(mDraggWidth, getMeasuredHeight());
                    setMeasuredDimension(mRightPanelWidth + mDraggWidth, getMeasuredHeight());
                }
            }
        });
    }
    private void calculateStarMargin(int dragWith){
        int starWidth = (int)getResources().getDimension(R.dimen.rating_panel_star_size);
        int scale = (int)getResources().getDisplayMetrics().density;
        int starWidthInPix = starWidth * scale;
        int marg = dragWith - starWidthInPix * 10;
        Log.d(TAG, "star margin" +marg);
    }
    @Override
    public boolean isCanScroll(int x) {
        int width = mRightPanelWidth;
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

    private void changeLeftPanelSize(int width, int height) {
        ViewGroup.LayoutParams params = llLeftPanel.getLayoutParams();
        params.width = width;
        params.height = height;
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
                onActionDown(x, y);
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
        int deltaX = x - mLastX;
        int deltaY = y - mLastY;
//        Log.d(TAG, "on move x=" + x+ " y="+y +" dx="+deltaX+" dy="+deltaY+ " isMoving="+mIsMoving);
        if (mIsMoving == false && Math.abs(deltaX) > Math.abs(deltaY)) {
            Log.d(TAG, "on disable touch");
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
            p.setMargins(mLeftMargin, p.topMargin, p.rightMargin, p.bottomMargin);
            requestLayout();
        }
        mRate = getRating(mDraggWidth, mLeftMargin, 10);
        Log.d(TAG, "rating = "+mRate);
        if(mIsMoving){
            if(mListener != null){
                mListener.onRateChanging(mRate);
            }
        }
    }

    private void onActionDown(int x, int y) {
        Log.d(TAG, "on down x=" + x);
        mLastX = x;
        mLastY = y;
    }

    private void onActionUp(int x) {
        Log.d(TAG, "on up x=" + x);
        if (mIsMoving) {
            mDraggView.getParent().requestDisallowInterceptTouchEvent(false);
            mIsMoving = false;
            if(mListener != null) {
                mListener.onRateChanged(mRate);
            }
            Log.d(TAG, "on enable touch");
        }

        // move left
        final int newLeftMargin = -mDraggWidth - mLeftMargin;
        final int newLeftOffset = mLeftMargin;
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                changeLeftMargin((int) (newLeftMargin * interpolatedTime) + newLeftOffset);
            }
        };
        animation.setDuration(Math.abs(DURATION * newLeftMargin / mDraggWidth)); // in ms
        startAnimation(animation);
    }

    private int getRating(int totalWidth, int offset, int maxRating){
        return (int)(maxRating - Math.abs((float)maxRating * (float)offset/(float)totalWidth));
    }

    public void setOnRateChangeListener(IOnRateChangeListener listener){
        mListener = listener;
    }

    public interface IOnRateChangeListener{
        void onRateChanging(int rate);
        void onRateChanged(int rate);
    }
}