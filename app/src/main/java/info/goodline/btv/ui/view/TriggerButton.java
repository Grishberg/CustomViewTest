package info.goodline.btv.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import info.goodline.btv.android_btvc.R;

/**
 * Created by g on 15.07.15.
 */
public class TriggerButton extends FrameLayout implements View.OnClickListener {

    ImageView ivContainer;
    private TransitionDrawable mTransition;
    private OnClickListener mClickListener;
    private boolean mIsPressed;
    private Drawable mDrawable1;
    private Drawable mDrawable2;
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
        try {
            mDrawable1 = a.getDrawable(R.styleable.TriggerButton_imageRes1);
            mDrawable2 = a.getDrawable(R.styleable.TriggerButton_imageRes2);
        } finally {
            a.recycle();
        }

        inflate(context, R.layout.view_trigger_button, this);
        ivContainer = (ImageView) findViewById(R.id.ivTriggerButtonContainer);

        ivContainer.setOnClickListener(this);
    }

    private void initTransition(boolean mIsPressed) {
        if (mIsPressed) {
            mDrawables = new Drawable[]{mDrawable2, mDrawable1};
        } else {
            mDrawables = new Drawable[]{mDrawable1, mDrawable2};
        }
        mTransition = new TransitionDrawable(mDrawables);
        mTransition.setCrossFadeEnabled(true);
        ivContainer.setImageDrawable(mTransition);
    }

    public void init(boolean isPressed) {
        if (mIsPressed == isPressed && mTransition != null) return;
        mIsPressed = isPressed;
        initTransition(isPressed);
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
        mTransition.reverseTransition(mShortAnimationDuration);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
        mClickListener = l;
    }

    public boolean isPressed() {
        return mIsPressed;
    }
}