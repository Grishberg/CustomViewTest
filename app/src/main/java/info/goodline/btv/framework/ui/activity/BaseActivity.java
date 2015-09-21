package info.goodline.btv.framework.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by g on 20.09.15.
 */
public class BaseActivity extends Activity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private static final String LOCAL_INTENT_FILTER = "localIntentFilter";
    private boolean mIsBound;
    private boolean mIsBroadcasRegistered;
    private IntentFilter mLocalBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocalBroadcast = new IntentFilter(LOCAL_INTENT_FILTER);
        registerBroadcast();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindToService();
        registerBroadcast();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindFromService();
        unregisterBroadcast();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();
    }

    private void registerBroadcast() {
        if (!mIsBroadcasRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(
                    mMessageReceiver, mLocalBroadcast);
            mIsBroadcasRegistered = true;
        }
    }

    private void unregisterBroadcast() {
        if (mIsBroadcasRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(
                    mMessageReceiver);
            mIsBroadcasRegistered = false;
        }
    }

    private void bindToService() {
        if (!mIsBound) {

        }
    }

    private void unbindFromService() {
        if (mIsBound) {

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
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override

        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            Double currentSpeed = intent.getDoubleExtra("currentSpeed", 20);

            Double currentLatitude = intent.getDoubleExtra("latitude", 0);

            Double currentLongitude = intent.getDoubleExtra("longitude", 0);

            //  ... react to local broadcast message

        }
    };
}
