package info.goodline.btv.data.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.SparseArray;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import info.goodline.btv.framework.RestRequest;

public class ApiService extends BaseService implements IApiService, IRunObserver {
    protected volatile SparseArray<Future> mTaskQueue;

    public ApiService() {
        mTaskQueue = new SparseArray<>(CORE_POOL_SIZE);
    }

    @Override
    public void sendRequest(RestRequest restRequest) {
        int requestId = restRequest.getId();
        Future future = mExecutor.submit(new RequestCallable(this, restRequest));
        mTaskQueue.append(requestId, future);
    }

    /**
     * stop task
     *
     * @param id
     */
    @Override
    public void cancelResponse(int id) {
        Future task = mTaskQueue.get(id);
        if (task != null) {
            if (!task.isCancelled()) {
                task.cancel(true);
            }
        }
    }

    /**
     * removes task from queue when it's done
     *
     * @param id
     */
    @Override
    public void onTaskDone(int id) {
        mTaskQueue.remove(id);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mExecutor.shutdown();
    }
}
