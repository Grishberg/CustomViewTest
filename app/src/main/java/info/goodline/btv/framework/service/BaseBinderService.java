package info.goodline.btv.framework.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import info.goodline.btv.framework.Const;

/**
 * Created by g on 19.09.15.
 */
public abstract class BaseBinderService extends Service {
    private static final int SHUTDOWN_TIMER = 5000;
    private boolean mIsShutdowning;
    private Handler mShutdownHandler;
    private int mBindersCount;

    public BaseBinderService() {
        mBindersCount = 0;
        mShutdownHandler = new Handler();
    }

    protected abstract IBinder getBinder();

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mBindersCount--;
        if (mBindersCount == 0) {
            // start shutdown
            mIsShutdowning = true;
            mShutdownHandler.postDelayed(mShutdownRunnable, SHUTDOWN_TIMER);
        }
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        if (mIsShutdowning) {
            // cancel shutdown
            mShutdownHandler.removeCallbacks(mShutdownRunnable);
            mIsShutdowning = false;
        }
        mBindersCount++;
        super.onRebind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mBindersCount++;
        return getBinder();
    }

    private Runnable mShutdownRunnable = new Runnable() {
        @Override
        public void run() {
            if (mBindersCount == 0) {
                stopSelf();
            }
        }
    };

    /**
     * send message to activities
     */
    protected void sendMessage(String action, int code, int id) {
        Intent intent = new Intent(Const.REST_SERVICE_INTENT_FILTER);
        intent.setAction(action);
        intent.putExtra(Const.REST_SERVICE_RESPONSE_CODE_EXTRA, code);
        intent.putExtra(Const.REST_SERVICE_RESPONSE_ID_EXTRA, id);
        LocalBroadcastManager.getInstance(this).
                sendBroadcast(intent);
    }
}
