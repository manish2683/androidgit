package com.deletespam.ma066go.deletespammessage;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SMSListener extends BroadcastReceiver {

    private FileController fileController;

    public SMSListener() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                String messageFrom = smsMessage.getOriginatingAddress();
                if (messageFrom != null) {
                    fileController = new FileController(context);
                    boolean whiteList = false;
                    String[] whiteListFilter = GetWhiteListFilter(context);
                    if (whiteListFilter.length > 0) {
                        for (int i = 0; i < whiteListFilter.length; i++) {
                            if (messageFrom.toUpperCase().contains(whiteListFilter[i].toUpperCase())) {
                                whiteList = true;
                                break;
                            }
                        }
                    }

                    if(!whiteList) {
                        String[] spamFilter = GetSpamFilter(context);
                        if (spamFilter.length > 0) {
                            for (int i = 0; i < spamFilter.length; i++) {
                                if (messageFrom.toUpperCase().contains(spamFilter[i].toUpperCase())) {
                                    LogDiscardedMessage(context,smsMessage);
                                    this.abortBroadcast();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private String[] GetSpamFilter(Context context) {
        File spamFilterFile = fileController.GetFileObject(context.getString(R.string.spam_filter_folder),context.getString(R.string.spam_filter_file));
        String spamFilter = fileController.ReadContent(spamFilterFile);
        if(spamFilter.trim() == "")
            return new String[]{};
        else
            return fileController.ReadContent(spamFilterFile).split(",");
    }

    private String[] GetWhiteListFilter(Context context) {
        File whiteListFilterFile = fileController.GetFileObject(context.getString(R.string.spam_filter_folder), context.getString(R.string.whitelist_file));
        String whiteListFiler = fileController.ReadContent(whiteListFilterFile);
        if(whiteListFiler.trim() == "")
            return new String[]{};
        else
            return fileController.ReadContent(whiteListFilterFile).split(",");
    }

    private void LogDiscardedMessage(Context context,SmsMessage smsMessage) {
        File discardedMsgFile = fileController.GetFileObject(context.getString(R.string.spam_filter_folder),context.getString(R.string.discardedMessage_file));
        String spamMsgcontent = "\\nFrom : " + smsMessage.getOriginatingAddress() + "\\n";
        Date date = new Date(smsMessage.getTimestampMillis());
        String formattedDate = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(date);
        spamMsgcontent = spamMsgcontent + "Received Time : " + formattedDate + "\\n";
        spamMsgcontent = spamMsgcontent + "Content : " + smsMessage.getMessageBody() + "\\n";
        fileController.WriteCotent(discardedMsgFile, spamMsgcontent, true);
    }
}
