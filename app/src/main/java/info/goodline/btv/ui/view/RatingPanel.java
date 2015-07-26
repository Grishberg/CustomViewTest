package info.goodline.btv.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import info.goodline.btv.android_btvc.R;

/**
 * Created by g on 21.07.15.
 */
public class RatingPanel extends FrameLayout implements View.OnTouchListener, View.OnClickListener {
    private static final String TAG = RatingPanel.class.getSimpleName();
    private int[] mIds;
    private boolean[] mMask;
    private ImageView[] ivStars;
    private Rect[] mRects;
    private TextView tvOldRating;
    private ImageView ivIcon;
    private IOnRatingChangedListener mListener;
    private int mRating;
    private Rect mStarsRect;

    Rect outRect = new Rect();
    int[] location = new int[2];
    TextView tvRatingPanelText;

    // android.R.drawable.btn_star_big_on
    public RatingPanel(Context context) {
        super(context);
    }

    public RatingPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_rating_panel, this);
    }

    public void setOnRatingChangeListener(IOnRatingChangedListener listener) {
        mListener = listener;
    }

    public void init(int rating) {
        tvOldRating = (TextView) findViewById(R.id.tvRatingPanelOldValue);
        ivIcon = (ImageView) findViewById(R.id.ivRatingPanelIcon);
        if(rating > 0){
            tvOldRating.setVisibility(VISIBLE);
            tvOldRating.setText(String.valueOf(rating));
            ivIcon.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            tvOldRating.setVisibility(GONE);
            ivIcon.setImageResource(android.R.drawable.btn_star_big_off);
        }
        tvRatingPanelText = (TextView) findViewById(R.id.tvRatingPanelText);
        setOnClickListener(this);
        setOnTouchListener(this);
        mStarsRect = new Rect(-1,-1,-1,-1);
        TypedArray ids = getResources().obtainTypedArray(R.array.starIds);
        // set click-listeners for each star
        mIds = new int[ids.length()];
        mMask = new boolean[ids.length()];
        ivStars = new ImageView[ids.length()];
        mRects = new Rect[ids.length()];
        for (int i = 0; i < ids.length(); i++) {
            mIds[i] = ids.getResourceId(i, 0);
            ivStars[i] = (ImageView) findViewById(ids.getResourceId(i, 0));
        }
        ids.recycle();
        ivStars[0].addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mStarsRect.left < 0) {
                    v.getDrawingRect(outRect);
                    v.getLocationOnScreen(location);
                    outRect.offset(location[0], location[1]);
                    mStarsRect.left = outRect.left;
                }
            }
        });
        ivStars[ivStars.length-1].addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mStarsRect.right < 0){
                    v.getDrawingRect(outRect);
                    v.getLocationOnScreen(location);
                    outRect.offset(location[0], location[1]);
                    mStarsRect.right = outRect.right;
                    mStarsRect.top = outRect.top;
                    mStarsRect.bottom = outRect.bottom;

                    for (int i = 0; i < mRects.length; i++) {
                        mRects[i] = new Rect();
                        ivStars[i].getDrawingRect(mRects[i]);
                        ivStars[i].getLocationOnScreen(location);
                        mRects[i].offset(location[0], location[1]);
                    }
                }
            }
        });
        ivIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
    }
    public void show(){
        drawStars(-1);
        setVisibility(View.VISIBLE);
        setAlpha(0);
        animate().alpha(1.0f).setListener(null).start();
    }

    public void hide(){
        animate().alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        setVisibility(View.GONE);
                    }
                }).start();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        if(mStarsRect.contains(x,y)){
            mRating = getStarIndex(x,y);
            drawStars(mRating);
            tvRatingPanelText.setText(getRateText(mRating));
            if(event.getAction() == MotionEvent.ACTION_UP){
                onSaveResult(mRating);
                return true;
            }
        } else {
            mRating = -1;
            drawStars(mRating);
        }
        return false;
    }

    private int getStarIndex(int x, int y){
        int index = -1;
        for (int i = 0; i < ivStars.length; i++) {
            if (x < mRects[i].left) {
                break;
            } else {
                index = i;
            }
        }
        return index;
    }

    private void onSaveResult(int rating) {
        //TODO: save to DB
        if (mListener != null) {
            mListener.onRatingChanged(rating);
        }
        hide();
    }

    private void drawStars(int index) {
        for (int i = 0; i < mIds.length; i++) {
            if (i <= index) {
                // draw star
                if (!mMask[i]) {
                    mMask[i] = true;
                    ivStars[i].setImageResource(android.R.drawable.btn_star_big_on);
                }
            } else {
                // clear star
                if (mMask[i]) {
                    mMask[i] = false;
                    ivStars[i].setImageResource(android.R.drawable.btn_star_big_off);
                }
            }
        }
    }

    private String getRateText(int index) {
        switch (index) {
            case 0:
                return getContext().getString(R.string.rating_text_1);
            case 1:
                return getContext().getString(R.string.rating_text_2);
            case 2:
                return getContext().getString(R.string.rating_text_3);
            case 3:
                return getContext().getString(R.string.rating_text_4);
            case 4:
                return getContext().getString(R.string.rating_text_5);
            case 5:
                return getContext().getString(R.string.rating_text_6);
            case 6:
                return getContext().getString(R.string.rating_text_7);
            case 7:
                return getContext().getString(R.string.rating_text_8);
            case 8:
                return getContext().getString(R.string.rating_text_9);
            case 9:
                return getContext().getString(R.string.rating_text_10);
        }
        return "";
    }

    public interface IOnRatingChangedListener {
        void onRatingChanged(int rating);
    }
}
