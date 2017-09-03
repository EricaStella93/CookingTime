package com.example.erica.cookingtime.Notifications;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.erica.cookingtime.Utils.ConstantsDictionary;

public class NotificationReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent service1 = new Intent(context, NotificationService.class);
        service1.putExtra(ConstantsDictionary.INGREDIENT_NAME,
                intent.getStringExtra(ConstantsDictionary.INGREDIENT_NAME));
        context.startService(service1);

    }
}
