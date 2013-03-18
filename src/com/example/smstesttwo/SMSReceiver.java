package com.example.smstesttwo;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver
{
	private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) 
    {
		mContext=context;
        //---get the SMS message passed in--
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        if (bundle != null) {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                SMS.str += "SMS from " + msgs[i].getOriginatingAddress();
                SMS.str += " :";
                SMS.str += msgs[i].getMessageBody().toString();
                SMS.str += "\n";
            }
			//persistSMS(SMS.str, msgs[0].getOriginatingAddress() ,"inbox");
			//handleResponseFrom33700( msgs );
			abortBroadcast();
			
            //make a dialog window to display received SMS 
			Intent displayIntent = new Intent( context , SMSDisplayer.class );
            displayIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try{
            	context.startActivity( displayIntent );
            } catch( android.content.ActivityNotFoundException e ){           		
          	  	Toast.makeText(context, "Activity not found!", Toast.LENGTH_SHORT).show();
            }
            
        }
    }
	/**
	 * Save a SMS to the default Android SMS application.
	 * 
	 * @param message
	 * The text of the SMS.
	 * @param sender
	 * The phone number of the SMS.
	 * @param box
	 * The box where to store the message.
	 */
	private void persistSMS( String message, String sender, String box) {
		ContentValues values = new ContentValues();
		values.put("address", sender);
		values.put("body", message);
		mContext.getContentResolver().insert(Uri.parse("content://sms/"+box), values);
	}
}
