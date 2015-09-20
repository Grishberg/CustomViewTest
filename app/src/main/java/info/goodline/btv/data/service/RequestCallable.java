package info.goodline.btv.data.service;

import android.os.*;

import java.util.concurrent.Callable;

import info.goodline.btv.framework.RestRequest;
import info.goodline.btv.framework.RestResponse;

/**
 * Created by g on 20.09.15.
 */
public class RequestCallable implements Callable<RestResponse> {
    private RestRequest mRequest;
    private IRunObserver mRunObserver;
    private int mPriority;

    public RequestCallable(IRunObserver runObserver, RestRequest request, int priority) {
        mRequest = request;
        mRunObserver = runObserver;
        mPriority = priority;
        if(priority == -1){
            mPriority = android.os.Process.THREAD_PRIORITY_DEFAULT;
        }
    }

    @Override
    public RestResponse call() throws Exception {
        if (mRequest == null) return null;
        android.os.Process.setThreadPriority(mPriority);
        mRequest.onRequest();
        mRunObserver.onTaskDone(mRequest.getId());
        return null;
    }
}
