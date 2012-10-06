/*
 * Copyright (C) 2012 Thomas Thyberg
 *
 * Everyone is permitted to copy and distribute verbatim or modified
 * copies of this license document, and changing it is allowed as long
 * as the name is changed.
 *
 *           DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
 *   TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
 *
 *  0. You just DO WHAT THE FUCK YOU WANT TO.
 */
package se.birabirro.android.smspingpong;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        for (final SmsMessage msg : getMessagesFromIntent(intent)) {
            handleMessage(msg);
        }
    }

    private void handleMessage(final SmsMessage msg) {
        if (isPingCommand(getCommandFromMessage(msg))) {
            sendPongReply(msg.getOriginatingAddress());
            abortBroadcast();
        }
    }

    private static String getCommandFromMessage(final SmsMessage msg) {
        return msg.getMessageBody().trim().toLowerCase();
    }

    private static boolean isPingCommand(final String command) {
        return "ping".equals(command);
    }

    private static void sendPongReply(final String address) {
        SmsManager.getDefault().sendTextMessage(
            address, null, "pong", null, null
        );
    }

    private static List<SmsMessage> getMessagesFromIntent(final Intent intent) {
        final List<SmsMessage> list = new ArrayList<SmsMessage>();
        for (final Object pdu : getPdusFromIntent(intent)) {
            list.add(SmsMessage.createFromPdu((byte[]) pdu));
        }
        return list;
    }

    private static Object[] getPdusFromIntent(final Intent intent) {
        return (Object[]) intent.getExtras().get("pdus");
    }
}
