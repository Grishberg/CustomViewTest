package info.goodline.btv.data.service;

import java.util.concurrent.Callable;

import info.goodline.btv.framework.RestRequest;
import info.goodline.btv.framework.RestResponse;

/**
 * Created by g on 20.09.15.
 */
public class RequestCallable implements Callable<RestResponse> {
    private RestRequest mRequest;
    private IRunObserver mRunObserver;

    public RequestCallable(IRunObserver runObserver, RestRequest request) {
        mRequest = request;
        mRunObserver = runObserver;
    }

    @Override
    public RestResponse call() throws Exception {
        if (mRequest == null) return null;
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mRequest.onRequest();
        mRunObserver.onTaskDone(mRequest.getId());
        return null;
    }
}
