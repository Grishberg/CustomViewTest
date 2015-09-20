package info.goodline.btv.framework;

import retrofit.RestAdapter;

/**
 * Created by g on 19.09.15.
 */
public abstract class RestRequest<T> {
    private static int id;

    public RestRequest() {
        id++;
        if(id > 0xFFFF){
            id = 0;
        }
    }

    /**
     * init retrofit service from RestAdapter
     * @param adapter
     */
    public abstract void initService(RestAdapter adapter);

    /**
     * implement rest query from retrofit service
     * @return
     */
    public abstract T onRequest();

    /**
     * implement async logic after response
     * @param result
     */
    public void onSuccess(T result) {
    }

    /**
     * implement sync logic in main thread
     * @param result
     */
    public void onDone(T result) {
    }

    public void onFail(String message, int code) {

    }

    public int getId() {
        return id;
    }
}
