package com.geekbrains.boundserviceexample;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {

    Timer timer;
    TimerTask tTask;
    private long interval = 1000;
    ExampleBinder binder = new ExampleBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Dto", "onCreate");
        timer = new Timer();
        schedule();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Dto", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    void schedule() {
        if (tTask != null) tTask.cancel();
        if (interval > 0) {
            tTask = new TimerTask() {
                public void run() {
                    Log.d("Dto", "timerTask " + interval);
                }
            };
            timer.schedule(tTask, 1000, interval);
        }
    }

    public void upInterval(long intervalChange) {
        interval = interval + intervalChange;
        schedule();
    }

    public void downInterval(long intervalChange) {
        interval = interval - intervalChange;
        if (interval < 0) interval = 0;
        schedule();
    }

    public IBinder onBind(Intent intent) {
        Log.d("Dto", "onBind");
        Toast.makeText(this, getString(R.string.service_bind), Toast.LENGTH_SHORT).show();
        return binder;
    }

    public boolean onUnbind(Intent intent) {
        Log.d("Dto", "onUnbind");
        Toast.makeText(this, getString(R.string.service_unbind), Toast.LENGTH_SHORT).show();
        tTask.cancel();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d("Dto", "onDestroy");
        Toast.makeText(this, getString(R.string.service_destroy), Toast.LENGTH_SHORT).show();
    }

    public long getInterval() {
        return interval;
    }

    class ExampleBinder extends Binder {
        TimerService getService() {
            return TimerService.this;
        }
    }
}