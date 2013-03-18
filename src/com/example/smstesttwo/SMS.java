package com.example.smstesttwo;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SMS extends FragmentActivity {

	public static String str = "";
	
	private Intent StartSMSReceiver;
	
	public void sendMessage( String message , String phone_number ){
		PendingIntent sentPi = PendingIntent.getBroadcast(
				this, 0 , new Intent("SMS_SENT") , 0 );

		PendingIntent deliveredPi = PendingIntent.getBroadcast(
				this, 0 , new Intent("SMS_DEILVERED") , 0 );
		//SMS sent process result
		registerReceiver(
				new BroadcastReceiver(){
					@Override
					public void onReceive(Context context, Intent intent) {
						switch( getResultCode() ){
						case Activity.RESULT_OK:
							Toast.makeText(getBaseContext(), "SMS Sent", 
									Toast.LENGTH_SHORT ).show();
							//clean up number & message input after SMS were sent successfully.
							((TextView)findViewById( R.id.sms_message)).setText("", TextView.BufferType.EDITABLE );
							((TextView)findViewById( R.id.phone_number)).setText("", TextView.BufferType.EDITABLE );
							break;
						case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
							Toast.makeText(getBaseContext(), "SMS Sent", 
									Toast.LENGTH_SHORT ).show();
							break;
						case SmsManager.RESULT_ERROR_NO_SERVICE:
							Toast.makeText(getBaseContext(), "No Service", 
									Toast.LENGTH_SHORT ).show();
							break;
						case SmsManager.RESULT_ERROR_NULL_PDU:
							Toast.makeText(getBaseContext(), "Null PDU", 
									Toast.LENGTH_SHORT ).show();
							break;
						case SmsManager.RESULT_ERROR_RADIO_OFF:
							Toast.makeText(getBaseContext(), "Radio Off", 
									Toast.LENGTH_SHORT ).show();
							break;
						}
						
					}
				}, new IntentFilter("SMS_SENT"));

		registerReceiver(
				new BroadcastReceiver(){
					@Override
					public void onReceive(Context context, Intent intent) {
						switch( getResultCode() ){
						case Activity.RESULT_OK:
							Toast.makeText(getBaseContext(), "SMS deilvered", 
									Toast.LENGTH_SHORT ).show();
							break;
						case Activity.RESULT_CANCELED:
							Toast.makeText(getBaseContext(), "SMS not deilvered", 
									Toast.LENGTH_SHORT ).show();
							break;
						}
					}
				}
				, new IntentFilter("SMS_DEILVERED"));
		
		SmsManager.getDefault().sendTextMessage( 
				phone_number , null , message , sentPi , deliveredPi );	
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.sms_sender);
		SMSSenderFragment t = new SMSSenderFragment();
		t.setArguments( getIntent().getExtras() );
		getSupportFragmentManager().beginTransaction().add(
				R.id.fragment_container, t ).commit();
		//ensure SMS receiver started
		/*
		getPackageManager().setComponentEnabledSetting( new ComponentName( getBaseContext() , SMSReceiver.class ) ,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
		*/
	}
	
	@Override
	public void onStart(){
		super.onStart();
		//Send SMS Message
		Button b = (Button)(findViewById(R.id.sms_frame).findViewById(R.id.send_button));
		b.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						sendMessage(
						((TextView)findViewById( R.id.sms_message )).getText().toString(),
						((TextView)findViewById( R.id.phone_number )).getText().toString());
					}
				});
		
		StartSMSReceiver = new Intent( getBaseContext() , SMSReceiverService.class );
		startService(StartSMSReceiver);
	}
	
	public void startDisplayer( View v ){
		try{
			Intent i = new Intent( getApplicationContext() , SMSDisplayer.class );
			startActivity(i);
		} catch( android.content.ActivityNotFoundException e ){
			Toast.makeText(getBaseContext(), "Activity Not Found", 
					Toast.LENGTH_SHORT ).show();
		}
	}
	
	public void exitReceiver( View v ){
		this.finish();
		//disable SMS receiver after exit
		/*
		PackageManager pm = getPackageManager();
		pm.setComponentEnabledSetting( new ComponentName( getBaseContext() , SMSReceiver.class ) ,
		                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
		                            PackageManager.DONT_KILL_APP);
		*/
		stopService(StartSMSReceiver);
		Toast.makeText(getBaseContext(), "Exit...", 
				Toast.LENGTH_SHORT ).show();
	}
	/*
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	*/
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sm, menu);
		return true;
	}
	*/

}
