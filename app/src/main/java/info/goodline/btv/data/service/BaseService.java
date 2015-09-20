package info.goodline.btv.data.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.Handler;
import android.os.Message;
import android.os.Process;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by g on 19.09.15.
 */
public class BaseService extends Service {
    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    protected static final int CORE_POOL_SIZE = NUMBER_OF_CORES * 2;
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final int KEEP_ALIVE = 1;
    private static final int MESSAGE_POST_RESULT = 0x1;
    private static final int MESSAGE_POST_PROGRESS = 0x2;
    //    private static volatile Executor sDefaultExecutor = SERIAL_EXECUTOR;
    protected ExecutorService mExecutor;

    private volatile AsyncTask.Status mStatus = AsyncTask.Status.PENDING;

    private int mBindersCount;
    public BaseService() {
        mBindersCount = 0;

        mExecutor = Executors.newFixedThreadPool(CORE_POOL_SIZE);
    }

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
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        mBindersCount++;
        super.onRebind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mBindersCount++;
        return null;
    }

    protected void sendMessage(String action, int message){
        Intent sendableIntent = new Intent(getClass().getName());
        sendableIntent.setAction(action);
        LocalBroadcastManager.getInstance(this).
                sendBroadcast(sendableIntent);
    }
}
