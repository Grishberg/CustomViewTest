package info.goodline.btv.framework.ui.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by g on 20.09.15.
 */
public class BaseActivity extends Activity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private boolean mIsBound;

    @Override
    protected void onResume() {
        super.onResume();
        bindToService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindFromService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void bindToService(){
        if(!mIsBound){

        }
    }

    private void unbindFromService(){
        if(mIsBound){

        }
    }
    // connecting to Services
    private ServiceConnection mRestServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            mIsBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIsBound = false;
            Log.d(TAG, "onServiceDisconnected");
        }
    };
}
