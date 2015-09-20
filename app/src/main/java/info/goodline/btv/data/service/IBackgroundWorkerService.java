package info.goodline.btv.data.service;

/**
 * Created by g on 20.09.15.
 */
public interface IBackgroundWorkerService {
    void startBackgroundWork(String tag);
    void stopBackgroundWork(String tag);
}
