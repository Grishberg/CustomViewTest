package info.goodline.btv.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import info.goodline.btv.android_btvc.R;

/**
 * Created by g on 21.07.15.
 */
public class SlidingPanel extends LinearLayout {
    private int mWidth;
    public SlidingPanel(Context context) {
        super(context);
        init();
    }

    public SlidingPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_sliding_panel, this);
        init();
    }
    private void init(){
        post(new Runnable() {
            @Override
            public void run() {
                mWidth = getLayoutParams().width;
                setTranslationX(-mWidth);
            }
        });
    }

    public void show(){
        animate().translationX(0);
    }

    public void hide(){
        animate().translationX(-mWidth);
    }
}
