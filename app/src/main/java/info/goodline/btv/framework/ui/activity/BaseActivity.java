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

import info.goodline.btv.data.service.ApiService;
import info.goodline.btv.data.service.BackgroundWorkerService;
import info.goodline.btv.framework.Const;

/**
 * Created by g on 20.09.15.
 */
public class BaseActivity extends Activity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private boolean mIsBoundApiService;
    private boolean mIsBoundWorkerService;
    private Intent mApiServiceIntent;
    private Intent mWorkerServiceIntent;
    private boolean mIsBroadcasRegistered;
    private IntentFilter mLocalBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocalBroadcast = new IntentFilter(Const.REST_SERVICE_INTENT_FILTER);
        mApiServiceIntent = new Intent(this, ApiService.class);
        mWorkerServiceIntent = new Intent(this, BackgroundWorkerService.class);
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

    //------------ helper ----------------------------
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
        if (!mIsBoundApiService) {
            bindService(mApiServiceIntent, mApiServiceConnection, Context.BIND_AUTO_CREATE);
        }
        if (!mIsBoundWorkerService) {
            bindService(mWorkerServiceIntent, mWorkerServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private void unbindFromService() {
        if (mIsBoundApiService) {
            unbindService(mApiServiceConnection);
        }

        if (mIsBoundWorkerService) {
            unbindService(mApiServiceConnection);
        }
    }

    // connecting to Services
    private ServiceConnection mApiServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected ApiService");
            mIsBoundApiService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIsBoundApiService = false;
            Log.d(TAG, "onServiceDisconnected ApiService");
        }
    };
    private ServiceConnection mWorkerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected Worker");
            mIsBoundWorkerService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIsBoundWorkerService = false;
            Log.d(TAG, "onServiceDisconnected Worker");
        }
    };
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override

        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Const.REST_SERVICE_ACTION:
                    int responseCode = intent.getIntExtra(Const.REST_SERVICE_RESPONSE_CODE_EXTRA, 0);
                    int responseId = intent.getIntExtra(Const.REST_SERVICE_RESPONSE_ID_EXTRA, 0);
                    onResponse(responseCode, responseId);
                    break;
                case Const.WORKER_SERVICE_ACTION:
                    break;
            }
        }
    };

    /**
     * receive response from service
     *
     * @param responseCode
     * @param requestId
     */
    protected void onResponse(int responseCode, int requestId) {
    }
}
