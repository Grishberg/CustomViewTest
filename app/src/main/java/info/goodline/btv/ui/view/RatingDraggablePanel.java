package info.goodline.btv.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import info.goodline.btv.android_btvc.R;

/**
 * Created by g on 23.07.15.
 */
public class RatingDraggablePanel extends LinearLayout implements View.OnTouchListener {
    private static final String TAG = SwipePanel.class.getSimpleName();
    private int mRightPanelWidth;
    private int mLeftPanelWidth;
    LinearLayout llLeftPanel;
    LinearLayout llRightPanel;
    private int mLastX;
    private float m;

    public RatingDraggablePanel(Context context) {
        super(context);
    }

    public RatingDraggablePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_rating_draggable_panel, this);
        init();
    }

    private void init() {
        llLeftPanel = (LinearLayout) findViewById(R.id.llLeft);
        llRightPanel = (LinearLayout) findViewById(R.id.llMain);
        llRightPanel.setOnTouchListener(this);
        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mRightPanelWidth == 0) {
                    mRightPanelWidth = right - left;
                    changeWidth(mRightPanelWidth);
                }
            }
        });
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
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