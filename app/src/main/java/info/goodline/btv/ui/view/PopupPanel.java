package info.goodline.btv.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActionBar;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import info.goodline.btv.android_btvc.R;

/**
 * Created by g on 17.07.15.
 */
public class PopupPanel extends FrameLayout {
    private static final int DURATION = 2000;
    private boolean mIsVisible;
    TextView tvPopupPanelTitle;
    Handler mHandler;

    private int mHeight = 0;
    public PopupPanel(Context context) {
        super(context);
    }

    public PopupPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_popup_panel, this);
        init();
    }
    private void init(){
        mHandler = new Handler();
        tvPopupPanelTitle = (TextView) findViewById(R.id.tvPopupPanelTitle);
        addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(mHeight==0) {
                    mHeight = bottom - top;
                    setVisibility(GONE);
                }
            }
        });
    }
    public void show(String text){
        tvPopupPanelTitle.setText(text);
        if(mIsVisible == false) {
            this.setVisibility(VISIBLE);
            this.setAlpha(0f);
            this.animate().translationY(mHeight).alpha(0.7f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mIsVisible = true;
                            //start countdown timer
                            mHandler.postDelayed(mRunnable, DURATION);
                        }
                    }).start();
        } else {
            // restart timer
            mHandler.removeCallbacks(mRunnable);
            mHandler.postDelayed(mRunnable, DURATION);
        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            /** Do something **/
            hide();
        }
    };

    private void hide(){
        this.animate().alpha(0f)
                .translationY(-mHeight)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        mIsVisible = false;
                    }
                }).start();
    }
}
