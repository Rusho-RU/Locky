package com.dragontwister.locky;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * Created by The Chocobos on 02-Dec-17.
 */

public class LockyService extends Service {

    BroadcastReceiver broadcastReceiver;

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onCreate(){
        KeyguardManager.KeyguardLock keyguardLock;
        KeyguardManager keyguardManager = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);

        keyguardLock = keyguardManager.newKeyguardLock("IN");
        keyguardLock.disableKeyguard();

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);

        broadcastReceiver = new LockyReceiver();
        registerReceiver(broadcastReceiver, intentFilter);
    }
}
