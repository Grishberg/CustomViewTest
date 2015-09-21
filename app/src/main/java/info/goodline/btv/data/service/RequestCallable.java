package info.goodline.btv.data.service;

import android.os.*;

import java.util.concurrent.Callable;

import info.goodline.btv.framework.RestRequest;
import info.goodline.btv.framework.RestResponse;

/**
 * Created by g on 20.09.15.
 */
public class RequestCallable implements Callable<RestResponse> {
    private static final int MESSAGE_POST_RESULT = 1;
    private RestRequest mRequest;
    private IRunObserver mRunObserver;
    private int mPriority;
    private Handler mHandler;

    public RequestCallable(IRunObserver runObserver, RestRequest request, int priority) {
        mRequest = request;
        mRunObserver = runObserver;
        mPriority = priority;
        if(priority == -1){
            mPriority = android.os.Process.THREAD_PRIORITY_DEFAULT;
        }
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mRequest.onDone(msg.obj);
            }
        };
    }

    /**
     * do main work
     * @return
     * @throws Exception
     */
    @Override
    public RestResponse call() throws Exception {
        if (mRequest == null) return null;
        android.os.Process.setThreadPriority(mPriority);
        mRequest.onRequest();
        mRunObserver.onTaskDone(mRequest.getTag(),mRequest.getId());
        postResult(null);
        return null;
    }

    /**
     * send result in main thread
     * @param result
     */
    private void postResult(Object result) {
        Message message = mHandler.obtainMessage(MESSAGE_POST_RESULT, result);
        message.sendToTarget();
    }
}
