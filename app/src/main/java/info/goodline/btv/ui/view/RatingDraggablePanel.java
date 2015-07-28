package info.goodline.btv.ui.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import info.goodline.btv.android_btvc.R;
import info.goodline.btv.ui.ICanScrollInterface;

/**
 * Created by g on 23.07.15.
 */
public class RatingDraggablePanel extends LinearLayout implements View.OnTouchListener ,
        ICanScrollInterface {
    private static final String TAG = SwipePanel.class.getSimpleName();
    private static final float SCROLL = 0.15f;

    private int mRightPanelWidth;
    private int mLeftPanelWidth;
    private int mDraggWidth;
    private int mDraggContainerWidth;
    private int mDraggLeftOffset;
    LinearLayout llLeftPanel;
    LinearLayout llRightPanel;
    private View mMainView;
    private View mDraggView;
    private int mLastX;
    private float m;
    private boolean mIsHidded;

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

    public void init(View mainPanel, View dragContainer){
        mMainView = mainPanel;
        mDraggView = dragContainer;
        llRightPanel.addView(mMainView);
        mDraggView.setOnTouchListener(this);
    }

    private void init() {
        llLeftPanel = (LinearLayout) findViewById(R.id.llLeft);
        llRightPanel = (LinearLayout) findViewById(R.id.llMain);
        llLeftPanel.setVisibility(GONE);
        mIsHidded = true;
//        llRightPanel.setOnTouchListener(this);
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
        if(mRightPanelWidth == 0 ){
            mRightPanelWidth = getMeasuredWidth();
        }

        if(mDraggWidth == 0) {
            mLeftPanelWidth = llLeftPanel.getMeasuredWidth();
            mDraggView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            mDraggContainerWidth = mDraggView.getMeasuredWidth();
            mDraggWidth = mRightPanelWidth - mDraggContainerWidth;
            changeLeftPanelWidth(mDraggWidth);
            //TODO: get left offset
            Rect outRect = new Rect();
            int[] location = new int[2];

            mDraggView.getDrawingRect(outRect);
            mDraggView.getLocationOnScreen(location);
            outRect.offset(location[0], location[1]);
            mDraggLeftOffset = outRect.left;
        }
        setMeasuredDimension(mRightPanelWidth + mDraggWidth, getMeasuredHeight());
    }

    private void changeLeftPanelWidth(int width){
        ViewGroup.LayoutParams params = llLeftPanel.getLayoutParams();
        params.width = width;
        llLeftPanel.setLayoutParams(params);
    }

    private void changeWidth(int width) {
        LinearLayout.LayoutParams llParams =
                (LinearLayout.LayoutParams) llLeftPanel.getLayoutParams();
        mLeftPanelWidth = llParams.width;

        llParams = (LinearLayout.LayoutParams) llRightPanel.getLayoutParams();
        llParams.width = width;
        llRightPanel.setLayoutParams(llParams);

        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) getLayoutParams();
        params.leftMargin = -mLeftPanelWidth;
        setLayoutParams(params);
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
        if(mIsHidded){
            mIsHidded = false;
            llLeftPanel.setVisibility(VISIBLE);
            changeLeftMargin( -mDraggWidth);
        }
        final int x = (int) event.getRawX();
        final int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onActionDown(x);
                return true;
            case MotionEvent.ACTION_MOVE:
                onActionMove(x);
                return true;
            case MotionEvent.ACTION_UP:
                onActionUp(x);
                return true;
        }
        return false;
    }

    private void onActionMove(int x) {
        Log.d(TAG, "on move x=" + x);
        int deltaX = x - mLastX;
        mLastX = x;
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) getLayoutParams();
        params.leftMargin += deltaX;
        if (params.leftMargin > 0) {
            params.leftMargin = 0;
        } else if (params.leftMargin < -mLeftPanelWidth) {
            params.leftMargin = -mLeftPanelWidth;
        }
        setLayoutParams(params);
    }

    private void onActionDown(int x) {
        Log.d(TAG, "on down x=" + x);
        mLastX = x;
    }

    private void onActionUp(int x) {
        Log.d(TAG, "on up x=" + x);
    }
}