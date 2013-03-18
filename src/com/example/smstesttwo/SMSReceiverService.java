package com.example.smstesttwo;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.Toast;

public class SMSReceiverService extends Service {
	  private Looper mServiceLooper;
	  private ServiceHandler mServiceHandler;
	  private IBinder mBinder;// interface for clients that bind

	  // Handler that receives messages from the thread
	  private final class ServiceHandler extends Handler {
		  
	      public ServiceHandler(Looper looper) {
	          super(looper);
	          
	      }
	      @Override
	      public void handleMessage(Message msg) {
	          // Normally we would do some work here, like download a file.
		      registerReceiver( new SMSReceiver(),
		      		  new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));		  		        	 
	          // Stop the service using the startId, so that we don't stop
	          // the service in the middle of handling another job
	          //stopSelf(msg.arg1);
	      	}
	  }

	  @Override
	  public IBinder onBind(Intent intent) {
		  return mBinder;
	  }

	  @Override
	  public void onCreate() {
	    // Start up the thread running the service.  Note that we create a
	    // separate thread because the service normally runs in the process's
	    // main thread, which we don't want to block.  We also make it
	    // background priority so CPU-intensive work will not disrupt our UI.
	    HandlerThread thread = new HandlerThread("ServiceStartArguments",
	            Process.THREAD_PRIORITY_BACKGROUND);
	    thread.start();
 	    //Handler h = new Handler( this.getMainLooper() );
 	    //h.post( new DisplayToast("SMS receiver Started."));

	    // Get the HandlerThread's Looper and use it for our Handler 
	    mServiceLooper = thread.getLooper();
	    mServiceHandler = new ServiceHandler(mServiceLooper);
	  }
	  /*
	  @Override
	  public void onStart(final Intent intent, final int startId) {
		 }
	  */
	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
	      Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

	      // For each start request, send a message to start a job and deliver the
	      // start ID so we know which request we're stopping when we finish the job
	      Message msg = mServiceHandler.obtainMessage();
	      msg.arg1 = startId;
	      mServiceHandler.sendMessage(msg);

	      // If we get killed, after returning from here, restart
	      return START_STICKY_COMPATIBILITY;
	  }
	  
	  @Override
	  public void onDestroy() {
	    Toast.makeText(this, "sms service done", Toast.LENGTH_SHORT).show(); 
	  }
	  /*
	  private class DisplayToast implements Runnable{
		  String mText;
		  public DisplayToast(String text){
		    mText = text;
		  }
		  @Override
		  public void run(){
		     Toast.makeText(getApplicationContext(), mText, Toast.LENGTH_SHORT).show();
		  }
		}
	*/ 
}
