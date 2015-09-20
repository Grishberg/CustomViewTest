package info.goodline.btv.data.service;

import info.goodline.btv.framework.RestRequest;

/**
 * Created by g on 19.09.15.
 */
public interface IApiService {
    void sendRequest(RestRequest restRequest);
    void cancelResponse(int id);
}
