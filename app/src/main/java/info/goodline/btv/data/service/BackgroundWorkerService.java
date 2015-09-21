package info.goodline.btv.data.service;

import android.os.Binder;
import android.os.IBinder;

import info.goodline.btv.framework.service.BaseBinderService;

public class BackgroundWorkerService extends BaseBinderService
        implements IBackgroundWorkerService {
    private WorkerBinder mBinder = new WorkerBinder();
    public BackgroundWorkerService() {
    }

    /**
     * start async work
     * @param tag
     */
    @Override
    public void startBackgroundWork(String tag) {

    }

    /**
     * stop async work
     * @param tag
     */
    @Override
    public void stopBackgroundWork(String tag) {

    }

    @Override
    protected IBinder getBinder() {
        return mBinder;
    }

    // service container for Activity
    public class WorkerBinder extends Binder {
        public IBackgroundWorkerService getService() {
            return BackgroundWorkerService.this;
        }
    }
}
