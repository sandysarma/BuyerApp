package com.buyer.buyerApp.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.buyer.buyerApp.MainActivity;
import com.buyer.buyerApp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private final String TAG = "fcm";
    private String message = "";
    String usertype, title, msg;
    public static final int NOTIFICATION_ID = 1;
    public static final String PRIMARY_CHANNEL = "default";
    private NotificationManager notificationManager;
    Notification notification = null;
    NotificationCompat.Builder builder;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        String stringmess = remoteMessage.getNotification().getBody();

        title = remoteMessage.getNotification().getTitle();
        msg = remoteMessage.getNotification().getBody();
        // usertype = SharedPref.getInstance(getApplicationContext()).getStringPref(SharedPref.type);
        //showNotification(remoteMessage.getNotification().getBody());
        showNotification(title, msg);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showNotification(String title, String message) {
        sendNotification(title, message);
    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = null;
     /* if(title.equals("Placed Order")){
          Intent intent =new Intent(MyFirebaseMessagingService.this, OrderHistoryDetailsActivity.class);
          startActivity(intent);
      }*/
        if (title.contains("Placed Order")) {
            intent = new Intent(MyFirebaseMessagingService.this, MainActivity.class);
            intent.putExtra("login_key", "order_detail");
        } else if (title.contains("Order Cancel")) {
            intent = new Intent(MyFirebaseMessagingService.this, MainActivity.class);
            intent.putExtra("login_key", "order_cancel");
        }else if (title.contains("Placed Orderssss")) {
            intent = new Intent(MyFirebaseMessagingService.this, MainActivity.class);
            intent.putExtra("login_key", "login_value");
        }else if (title.contains("Service Request Placed")) {
            intent = new Intent(MyFirebaseMessagingService.this, MainActivity.class);
            intent.putExtra("login_key", "my_service");
        }else if (title.contains("Service Request Cancel")) {
            intent = new Intent(MyFirebaseMessagingService.this, MainActivity.class);
            intent.putExtra("login_key", "my_service_cancel");
        }
        else  {
            intent = new Intent(MyFirebaseMessagingService.this, MainActivity.class);
            intent.putExtra("login_key", "login_value");
        }

        SharedPreferences sharedPreferences_login = getApplicationContext().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences_login.edit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = "Default Channel";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        //.setContentTitle(getString(R.string.fcm_message))
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "SmartFarms",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }

    private NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    @Override
    public void onNewToken(String token) {
        Log.e("token", "RefreshedToken: " + token);
        //SharedPref.getInstance(getApplicationContext()).setStringPref(SharedPref.device_token, token);
        //sendRegistrationToServer(token);
        // DeviceTokenNav.setDeviceToken(this, token);
    }
}

