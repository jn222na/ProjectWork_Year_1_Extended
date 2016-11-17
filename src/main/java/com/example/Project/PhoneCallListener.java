package com.example.Project;

import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.content.Context;

public class PhoneCallListener extends PhoneStateListener  {
	 
	private boolean isPhoneCalling = false;
	String LOG_TAG = "LOGGING 123";
	Context context;
	public PhoneCallListener(Context context) {
	this.context = context;
}
	@Override
	public void onCallStateChanged(int state, String incomingNumber) {

		if (TelephonyManager.CALL_STATE_RINGING == state) {
			// phone ringing
			Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
		}

		if (TelephonyManager.CALL_STATE_OFFHOOK == state) {

			isPhoneCalling = true;
		}

		if (TelephonyManager.CALL_STATE_IDLE == state) {
			// run when class initial and phone call ended, 
			// need detect flag from CALL_STATE_OFFHOOK

			if (isPhoneCalling) {

				// restart app
				Intent i = context.getPackageManager()
					.getLaunchIntentForPackage(
							context.getPackageName());
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i);

				isPhoneCalling = false;
			}

		}
	}
}
