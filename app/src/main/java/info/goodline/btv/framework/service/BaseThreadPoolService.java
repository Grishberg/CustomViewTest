package info.goodline.btv.framework.service;

import android.util.SparseArray;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import info.goodline.btv.data.service.IRunObserver;

/**
 * Created by g on 20.09.15.
 */
public abstract class BaseThreadPoolService extends BaseBinderService implements IRunObserver {
    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    protected static final int CORE_POOL_SIZE = NUMBER_OF_CORES * 2;
    protected volatile Map<String, SparseArray<Future>> mTaskQueue;
    protected ExecutorService mExecutor;

    public BaseThreadPoolService() {
        mExecutor = Executors.newFixedThreadPool(CORE_POOL_SIZE);
        mTaskQueue = new HashMap<>(CORE_POOL_SIZE);
    }

    /**
     * removes task from queue when it's done
     *
     * @param id
     */
    @Override
    public void onTaskDone(String tag, int id) {
        SparseArray<Future> queue = mTaskQueue.get(tag);
        if (queue != null) {
            queue.remove(id);
        }
    }

    /**
     * stop task with tag and id
     *
     * @param tag
     * @param id
     */
    protected synchronized void cancelQueueResponse(String tag, int id) {
        SparseArray<Future> queue = mTaskQueue.get(tag);
        if (queue != null) {
            Future task = queue.get(id);
            if (task != null && !task.isCancelled()) {
                task.cancel(true);
                queue.remove(id);
            }
        }
    }

    /**
     * stop all tasks and remove from queue
     *
     * @param tag
     */
    protected synchronized void cancelQueueResponse(String tag) {
        SparseArray<Future> queue = mTaskQueue.get(tag);
        if (queue != null) {
            for (int i = queue.size() - 1; i >= 0; i++) {
                int key = queue.keyAt(i);
                // get the object by the key.
                Future task = queue.get(key);
                if (!task.isCancelled()) {
                    task.cancel(true);
                }
                queue.removeAt(i);
            }
        }
    }

    protected synchronized void cancelAll() {
        for (String tag : mTaskQueue.keySet()) {
            SparseArray<Future> queue = mTaskQueue.get(tag);
            if (queue != null) {
                for (int i = queue.size() - 1; i >= 0; i++) {
                    int key = queue.keyAt(i);
                    // get the object by the key.
                    Future task = queue.get(key);
                    if (!task.isCancelled()) {
                        task.cancel(true);
                    }
                    queue.removeAt(i);
                }
            }
            mTaskQueue.remove(tag);
        }
    }
}
