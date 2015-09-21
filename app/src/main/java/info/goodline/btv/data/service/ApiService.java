package info.goodline.btv.data.service;

import android.os.Binder;
import android.os.IBinder;
import android.util.SparseArray;

import java.util.concurrent.Future;

import info.goodline.btv.framework.RestRequest;
import info.goodline.btv.framework.service.BaseThreadPoolService;

public class ApiService extends BaseThreadPoolService implements IApiService {
    private ApiBinder mBinder = new ApiBinder();

    public ApiService() {

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


    @Override
    public void onDestroy() {
        super.onDestroy();
        mExecutor.shutdown();
    }

    @Override
    protected IBinder getBinder() {
        return mBinder;
    }

    // service container for Activity
    public class ApiBinder extends Binder {
        public IApiService getService() {
            return ApiService.this;
        }
    }
}
