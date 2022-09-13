package com.buyer.buyerApp.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.buyer.buyerApp.Activity.Login1Activity;
import com.buyer.buyerApp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class FcmListenerService extends FirebaseMessagingService
{
    private final String TAG = FcmListenerService.this.getClass().getName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        Log.i(TAG, "Message data:" + remoteMessage.getData());
        if (remoteMessage.getData().size() > 0)
        {
            Log.d(TAG, "From: " + remoteMessage.getFrom());
            Log.d(TAG, "Notification_Model Message Body: " + remoteMessage.getNotification().getBody());
            String click_action = remoteMessage.getNotification().getClickAction();
            //showNotification(remoteMessage.getNotification().getBody(),click_action);
        }
    }

    @Override
    public void onNewToken(String registrationId) {
        super.onNewToken(registrationId);
        Log.e("token", "RefreshedToken:" + registrationId);
       // SharedPref.getInstance(getApplicationContext()).setStringPref(SharedPref.device_token, registrationId);
       // DeviceTokenNav.setDeviceToken(this,registrationId);
    }

    private void showNotification(String msg, String click_action)
    {
        Intent intent = new Intent(this, Login1Activity.class);
       // intent.putExtra("msg", msg);
        SharedPreferences sharedPreferences_login = getApplicationContext().getSharedPreferences("SHARED_PREFERENCE_LOGIN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences_login.edit();
        editor.putString("notification_text", msg);
        editor.apply();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("SmartFarms");
        builder.setContentText(msg);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        //builder.setSmallIcon(R.drawable.logo_img);
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager)super.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0,notification);
    }
}
