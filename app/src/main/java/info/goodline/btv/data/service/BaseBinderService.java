package info.goodline.btv.data.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;


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
        mBindersCount --;
        if(mBindersCount == 0){
            // start shutdown
            mIsShutdowning = true;
            mShutdownHandler.postDelayed(mShutdownRunnable, SHUTDOWN_TIMER);
        }
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        if(mIsShutdowning){
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
            if(mBindersCount == 0) {
                stopSelf();
            }
        }
    };

    /**
     * send messsage to activities
     * @param action
     * @param message
     */
    protected void sendMessage(String action, int message){
        Intent sendableIntent = new Intent(getClass().getName());
        sendableIntent.setAction(action);
        LocalBroadcastManager.getInstance(this).
                sendBroadcast(sendableIntent);
    }
}
