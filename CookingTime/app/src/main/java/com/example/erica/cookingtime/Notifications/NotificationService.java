package com.example.erica.cookingtime.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.erica.cookingtime.Activities.FridgeActivity;
import com.example.erica.cookingtime.Activities.HomeActivity;
import com.example.erica.cookingtime.R;
import com.example.erica.cookingtime.Utils.ConstantsDictionary;

public class NotificationService extends Service{

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @SuppressWarnings("static-access")
    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);

        // prepare intent which is triggered if the
        // notification is selected
        Intent intentOpen = new Intent(this, FridgeActivity.class);
        // use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intentOpen, 0);

        String name = intent.getStringExtra(ConstantsDictionary.INGREDIENT_NAME);

        Notification n  = new Notification.Builder(this)
                .setContentTitle(name+ " is going to expire!")
                .setSmallIcon(R.drawable.ic_cooking)
                .setColor(getResources().getColor(R.color.white))
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
    }
}
