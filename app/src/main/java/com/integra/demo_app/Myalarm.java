package com.integra.demo_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Myalarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("my Myalarm trigered");
    }
}
