package com.example.smstesttwo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class SMSDisplayer extends Activity {
	protected void onCreate( Bundle savedInstanceState ){
		super.onCreate( savedInstanceState );
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView( R.layout.received_sms_display );
		
		TextView t = (TextView)findViewById(R.id.sms_text_received);
		t.setText( SMS.str , TextView.BufferType.NORMAL );
		SMS.str = "";//clear up SMS after displayed
	}
    public void finishDialog(View v) {
      this.finish();
    }
}