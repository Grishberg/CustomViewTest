package info.goodline.btv.framework;

/**
 * Created by g on 20.09.15.
 */
public class RestResponse {
    private Object response;
    private int errorCode;
    private String errorMessage = null;

    public RestResponse(Object response) {
        this.response = response;
    }

    public Object getResponse() {
        return response;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
