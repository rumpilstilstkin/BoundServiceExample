package com.geekbrains.boundserviceexample;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.geekbrains.boundserviceexample.executors.DefaultExecutorSupplier;

import java.util.concurrent.Future;


public class MainActivity extends AppCompatActivity {

    boolean bind = false;
    ServiceConnection sConn;
    TimerService service;
    TextView speedSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speedSize = (TextView) findViewById(R.id.textViewSpeedSize);

        sConn = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder binder) {
                Toast.makeText(getBaseContext(), getString(R.string.service_connected), Toast.LENGTH_SHORT).show();
                service = ((TimerService.ExampleBinder) binder).getService();
                speedSize.setText("" + service.getInterval());
                bind = true;
            }

            public void onServiceDisconnected(ComponentName name) {
                Toast.makeText(getApplicationContext(), getString(R.string.service_disconnected), Toast.LENGTH_SHORT).show();
                speedSize.setText(getString(R.string.service_not_bind));
                bind = false;
            }
        };
    }

    public void onBindService(View view) {
        Intent intent = new Intent(this, TimerService.class);
        bindService(intent, sConn, BIND_AUTO_CREATE);
    }

    public void onUnbindService(View view) {
        if(bind) {
            unbindService(sConn);
            speedSize.setText(getString(R.string.service_unbind));
        }
        bind = false;
    }

    public void onSpeedUp(View view) {
        if (!bind) return;
        service.upInterval(1000);
        speedSize.setText("" + service.getInterval());
    }

    public void onSpeedDown(View view) {
        if (!bind) return;
        service.downInterval(1000);
        speedSize.setText("" + service.getInterval());
    }

    private void workWithExecutors(){
        Future future = DefaultExecutorSupplier.getInstance().forBackgroundTasks()
                .submit(new Runnable() {
                    @Override
                    public void run() {
                        // do some background work here.
                    }
                });

        /*
         * cancelling the task
         */
        future.cancel(true);
    }

    /*
     * Using it for Background Tasks
     */
    public void doSomeBackgroundWork(){
        DefaultExecutorSupplier.getInstance().forBackgroundTasks()
                .execute(new Runnable() {
                    @Override
                    public void run() {
                        // do some background work here.
                    }
                });
    }

    /*
     * Using it for Light-Weight Background Tasks
     */
    public void doSomeLightWeightBackgroundWork(){
        DefaultExecutorSupplier.getInstance().forLightWeightBackgroundTasks()
                .execute(new Runnable() {
                    @Override
                    public void run() {
                        //go net
                        //parse data
                        DefaultExecutorSupplier.getInstance().forMainThreadTasks()
                                .execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        // do some Main Thread work here.
                                    }
                                });
                        //some do with data
                        DefaultExecutorSupplier.getInstance().forMainThreadTasks()
                                .execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        // do some Main Thread work here.
                                    }
                                });

                    }
                });
    }

    /*
     * Using it for MainThread Tasks
     */
    public void doSomeMainThreadWork(){
        DefaultExecutorSupplier.getInstance().forMainThreadTasks()
                .execute(new Runnable() {
                    @Override
                    public void run() {
                        // do some Main Thread work here.
                    }
                });
    }
}
