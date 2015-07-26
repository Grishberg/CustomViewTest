package info.goodline.btv.ui.view;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

//import butterknife.Bind;
//import butterknife.ButterKnife;
//import info.goodline.btv.R;


import android.widget.FrameLayout;

import info.goodline.btv.android_btvc.R;

/**
 * Created by g on 17.07.15.
 */
public class TriggerButton extends FrameLayout implements View.OnClickListener {

    ImageView ivContainer;
    private TransitionDrawable mTransition;
    private OnClickListener mClickListener;
    private boolean mIsPressed;
    private int mShortAnimationDuration = 500;
    private Drawable[] mDrawables;

    public TriggerButton(Context context) {
        super(context);
    }

    public TriggerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TriggerButton,
                0, 0);
        int transitionRes = 0;
        try {
            transitionRes = a.getResourceId(R.styleable.TriggerButton_transitionRes
                    , R.drawable.abc_list_focused_holo);
        } finally {
            a.recycle();
        }

        mTransition = (TransitionDrawable)getResources().getDrawable(transitionRes);

        inflate(context, R.layout.view_trigger_button, this);
        ivContainer = (ImageView) findViewById(R.id.ivTriggerButtonContainer);

        ivContainer.setImageDrawable(mTransition);
        ivContainer.setOnClickListener(this);
    }

    public void init(boolean isPressed) {
        mIsPressed = isPressed;

        if (isPressed) {
            mTransition.startTransition(0);
        } else {
            //mTransition.resetTransition();
        }
    }

    @Override
    public void onClick(View v) {
        changeState();
        if (mClickListener != null) {
            mClickListener.onClick(this);
        }
    }

    private void changeState() {
        mIsPressed = !mIsPressed;
        if (mIsPressed) {
            mTransition.startTransition(mShortAnimationDuration);
        } else {
            mTransition.reverseTransition(mShortAnimationDuration);
        }
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
        mClickListener = l;
    }
}