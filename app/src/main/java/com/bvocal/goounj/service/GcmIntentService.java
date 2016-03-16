package com.bvocal.goounj.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.bvocal.goounj.MainHomeActivity;
import com.bvocal.goounj.R;
import com.bvocal.goounj.interfaces.Appinterface;
import com.bvocal.goounj.receiver.GCMBroadcastreceiver;

import org.json.JSONObject;

import java.util.Random;

/**
 * Created by nandagopal on 28/10/15.
 */
public class GcmIntentService extends IntentService implements Appinterface {

    public static final int NOTIFICATION_ID = 5;
    public static String TAG = "tag";
    NotificationCompat.Builder builder;
    private NotificationManager mNotificationManager;
    private JSONObject object;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public GcmIntentService() {
        super("");
    }

    /**
     * This method is invoked on the worker thread with a request to process.
     * Only one Intent is processed at a time, but the processing happens on a
     * worker thread that runs independently from other application logic.
     * So, if this code takes a long time, it will hold up other requests to
     * the same IntentService, but it will not hold up anything else.
     * When all requests have been handled, the IntentService stops itself,
     * so you should not call {@link #stopSelf}.
     *
     * @param intent The value passed to {@link
     *               Context#startService(Intent)}.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
//        String data = intent.getStringExtra("title");
//        String alert = intent.getStringExtra("title");
        String msgBody = extras.getString("gcm.notification.body");
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);
        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {

            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {
                try {
//                    object = new JSONObject(data);
//                    if (object == null)
//                        return;
                    sendNotification("" + msgBody);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        GCMBroadcastreceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String source) {

        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent launchintent = new Intent(this, MainHomeActivity.class);


        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                uniqueInt, launchintent, PendingIntent.FLAG_UPDATE_CURRENT);
        Resources res = this.getResources();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this);
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_app_icon)
                .setLargeIcon(
                        BitmapFactory.decodeResource(res, R.drawable.logo))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(source))
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getString(R.string.app_name))
                .setContentText(source);
        Notification n = builder.build();
        n.flags = Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(new Random().nextInt(), n);

    }

}
