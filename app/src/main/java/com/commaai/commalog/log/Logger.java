package com.commaai.commalog.log;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.commaai.commalog.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;

/**
 * Created by fanqi on 2019-04-26.
 * Description:
 */
public class Logger {

    private final static String TAG= Logger.class.getCanonicalName();
    private Process exec=null;
    private BufferedReader reader = null;

    private MainActivity.LoggHandler mHandler;

    public Logger(MainActivity.LoggHandler handler) {
        mHandler=handler;
    }

    public void collectLog() {
        ThreadPoolFactory.getNormalThreadPoolProxy().execute(mRunnable);
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                exec = Runtime.getRuntime().exec("logcat");
                if (exec == null) {
                    return;
                }
                reader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    Message message = mHandler.obtainMessage();
                    message.what = Constants.LoggerMsg.WHAT_NEXT_LOG;
                    message.obj = line;
                    mHandler.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}
