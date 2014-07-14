package com.uob.contextframework.baseclasses;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.uob.contextframework.ContextMonitor;
import com.uob.contextframework.support.Constants;

public class ScreenStatusInfo extends BroadcastReceiver {

	boolean isScreenOn = false;

	public ScreenStatusInfo(Context mContext) {

		if( ContextMonitor.getInstance(mContext).getScreenStatusInfo()!=null){
			isScreenOn = ContextMonitor.getInstance(mContext).getScreenStatusInfo().isScreenOn();
		}else{
			PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
			isScreenOn = pm.isScreenOn();
		}
	}


	@Override
	public void onReceive(Context context, Intent intent) {
		String strAction = intent.getAction();

		if (strAction.equals(Intent.ACTION_SCREEN_OFF)) {
			isScreenOn = false;
		}else if(strAction.equals(Intent.ACTION_SCREEN_ON)){
			isScreenOn = true;
		}
		sendNotification(context);
	}


	public void sendNotification(Context context) {
		Intent signalIntent = new Intent(Constants.CONTEXT_CHANGE_NOTIFY);
		signalIntent.putExtra(Constants.INTENT_TYPE, Constants.SCREEN_NOTIFY);
		signalIntent.putExtra(Constants.SCREEN_NOTIFY,toString());
		context.sendBroadcast(signalIntent);
	}

	//Data conversion methods.
	@Override
	public String toString() {

		return toJSON().toString();
	}

	public JSONObject toJSON() {

		JSONObject jObject = new JSONObject();
		try {
			jObject.put(Constants.SCREEN_STATE, isScreenOn);
		} catch (JSONException e) {
		}
		return jObject;
	}
	/**
	 * @return the isScreenOn
	 */
	public boolean isScreenOn() {
		return isScreenOn;
	}
	/**
	 * @param isScreenOn the isScreenOn to set
	 */
	public void setScreenOn(boolean isScreenOn) {
		this.isScreenOn = isScreenOn;
	}
}