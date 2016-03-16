package com.bvocal.goounj.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.bvocal.goounj.interfaces.Appinterface;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cool on 09-Sep-15.
 */
public class SmsReceiver extends BroadcastReceiver implements Appinterface {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    public void onReceive(Context context, Intent intent) {
        preferences = context.getSharedPreferences(SHARED_PREFERENCES_POLLING, context.MODE_PRIVATE);
        editor = preferences.edit();
        final Bundle bundle = intent.getExtras();
        try {
            String pattern = "\\d+";
            Pattern p;
            Matcher m;
            String otp_content = "";
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    try {
                        if (senderNum.equals("YD-GOOUNJ") || senderNum.equals("BW-GOOUNJ") || senderNum.contains("GOOUNJ")) {
//                        if (senderNum.equals("+919524002445") || senderNum.equals("Amma")) {
//                        if (senderNum.equals("Org Ezhil") || senderNum.equals("8608736373") || senderNum.equals("+918608736373")) {
//                        if (senderNum.equals("DZ-WAYSMS") || senderNum.equals("DM-WAYSMS") || senderNum.equals("IM-WAYSMS")) {
                            editor.putString(OTP_MESSAGE_CONTENT, "" + message).commit();
                            Log.e("OTP From Receiver", "" + preferences.getString(OTP_MESSAGE_CONTENT, ""));
                            break;
                        }
                    } catch (Exception e) {
                        Log.e(LOG_Exception, "" + e);
                    }

                }
                Intent smsIntent = new Intent();
                smsIntent.setAction(INCOMING_RECEIVER_SMS);
                p = Pattern.compile(pattern);
                otp_content = preferences.getString(OTP_MESSAGE_CONTENT, "");
                m = p.matcher(otp_content);
                if (m.find()) {
                    Log.e("Found value:0- ", "" + m.group(0));
//                    Toast.makeText(context, "" + m.group(0), Toast.LENGTH_SHORT).show();
                    smsIntent.putExtra(GET_INCOMING_SMS_CONTENT, "" + m.group(0));
                    LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);
                } else {
                    Log.e("NO MATCH", "No");
                }
//                context.sendBroadcast(smsIntent);
            }

        } catch (Exception e) {
            Log.e("Exception", "" + e);
        }
    }
}
