package info.goodline.btv.data.service;

import android.util.SparseArray;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import info.goodline.btv.framework.RestRequest;

public class ApiService extends BaseBinderService implements IApiService, IRunObserver {
    protected volatile Map<String, SparseArray<Future>> mTaskQueue;
    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    protected static final int CORE_POOL_SIZE = NUMBER_OF_CORES * 2;
    protected ExecutorService mExecutor;

    public ApiService() {
        mTaskQueue = new HashMap<>(CORE_POOL_SIZE);
        mExecutor = Executors.newFixedThreadPool(CORE_POOL_SIZE);

    }

    @Override
    public void sendRequest(RestRequest restRequest, int priority) {
        int requestId = restRequest.getId();
        String requestTag = restRequest.getTag();
        Future future = mExecutor.submit(new RequestCallable(this, restRequest, priority));
        SparseArray<Future> queue = mTaskQueue.get(requestTag);
        if(queue == null){
            queue = new SparseArray<>(CORE_POOL_SIZE);
            mTaskQueue.put(requestTag, queue);
        }
        queue.put(requestId, future);
    }

    /**
     * stop task
     *
     * @param id
     */
    @Override
    public void cancelResponse(String tag, int id) {
        SparseArray<Future> queue = mTaskQueue.get(tag);
        if (queue != null) {
            Future task = queue.get(id);
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
    public void onTaskDone(String tag, int id) {
        mTaskQueue.remove(id);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mExecutor.shutdown();
    }
}
