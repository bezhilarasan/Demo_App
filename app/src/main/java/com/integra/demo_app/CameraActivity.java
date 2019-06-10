package com.integra.demo_app;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CameraActivity extends AppCompatActivity {
    Handler handler;
    Runnable runnable;
    int count = 0;
    TimePicker timePicker;
    private int PERMISSION_CALLBACK_CONSTANT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        reverseLinkedlist();
        insertionsort();
        diagonalmatrix();
        checkAndGivePermission();
        // handeler();
        // handeler();
//getting the timepicker object
        timePicker = (TimePicker) findViewById(R.id.timePicker);

        //attaching clicklistener on button
        findViewById(R.id.buttonAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //We need a calendar object to get the specified time in millis
                //as the alarm manager method takes time in millis to setup the alarm
                Calendar calendar = Calendar.getInstance();
                if (android.os.Build.VERSION.SDK_INT >= 23) {
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                            timePicker.getHour(), timePicker.getMinute(), 0);
                } else {
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                            timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
                }


                setAlarm(calendar.getTimeInMillis());
            }
        });
    }

    private void insertionsort() {
        int arr[] = {4, 3, 2, 6, 7, 1};
        println(arr);
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
        println(arr);
        // System.out.println("insertion sorting list ==>"+println(arr));
    }

    private void println(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.println("--->" + arr[i]);
        }

    }


    private void diagonalmatrix() {
        int mat[][] = {
                {3, 3, 3},
                {5, 5, 5},
                {-7, 7, 7}};
        int sum = 0, i, j, row = 3, col = 3, secsum = 0;
        for (i = 0; i < row; i++) {
            for (j = 0; j < col; j++) {
                //finding primary diagonals
                if (i == j) {
                    sum += mat[i][j];
                }
                //finding secondary diagonals
                if (i == row - j - 1) {
                    secsum += mat[i][j];
                }
            }
        }
        System.out.println("primary diagonal value sum is " + sum);
        System.out.println("secondary diagonal value sum is " + secsum);
    }

    private void reverseLinkedlist() {
        LinkedLists linkedLists = new LinkedLists();
        linkedLists.main();
        LinkedList linkedList = new LinkedList();
        linkedList.main();
    }


    private void setAlarm(long time) {
        //getting the alarm manager
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(this, Myalarm.class);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);

        //setting the repeating alarm that will be fired every day
        am.setRepeating(AlarmManager.RTC, time, AlarmManager.INTERVAL_DAY, pi);
        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();

    }

    private void handeler() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable, 4000);
                System.out.println("execution --->");
            }
        };
        handler.postDelayed(runnable, 4000);


        final DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.LONG);
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor =
                (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(5);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("background executiontime -- >" + dateFormat.format(new Date()));
                try {
                    Thread.sleep(10 * 1000);
                    System.out.println("end Time" + dateFormat.format(new Date()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        System.out.println(" submission time" + dateFormat.format(new Date()));
        // ScheduledFuture<?> scheduledFuture = scheduledThreadPoolExecutor.schedule(runnable, 5, TimeUnit.SECONDS);
        //  ScheduledFuture<?> scheduledFuture1 = scheduledThreadPoolExecutor.scheduleWithFixedDelay(runnable, 5, 5, TimeUnit.SECONDS);
        ScheduledFuture<?> scheduledFuture2 = scheduledThreadPoolExecutor.scheduleAtFixedRate(runnable, 5, 5, TimeUnit.SECONDS);

//        handler = new Handler();
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                count++;
//                System.out.println("running in handler ");
//                // Schedule the task to do again after an interval
//                handler.postDelayed(runnable, 4000);
//                //Toast.makeText(CameraActivity.this, "repeat " + count, Toast.LENGTH_SHORT).show();
//            }
//        };
//        handler.postDelayed(runnable, 4000);

    }

    private void checkAndGivePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_CALLBACK_CONSTANT);
        } else {
            //initialize();
        }
    }
}
