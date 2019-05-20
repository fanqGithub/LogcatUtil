package com.commaai.commalog;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.commaai.commalog.log.Constants;
import com.commaai.commalog.log.Logger;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();

    private TextView logTv;

    private StringBuilder sb = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity: ");
        initView();
        initLog();
    }

    private void initView() {

        logTv = findViewById(R.id.logContent);
        sb = new StringBuilder();

    }

    private void initLog() {
        LoggHandler handler = new LoggHandler(this);
        Logger logger = new Logger(handler);
        logger.collectLog();

    }

    public static class LoggHandler extends Handler {

        private final WeakReference<MainActivity> mActivity;

        private LoggHandler(MainActivity mActivty) {
            this.mActivity = new WeakReference<MainActivity>(mActivty);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case Constants.LoggerMsg.WHAT_NEXT_LOG:
                        String line = (String) msg.obj;
                        updateLogView(line);
                        break;
                    default:
                        break;
                }
            }
        }

        private void updateLogView(String line) {
            mActivity.get().logTv.setText("");
            mActivity.get().sb.append(line+"\n");
            mActivity.get().logTv.setText(mActivity.get().sb.toString());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
