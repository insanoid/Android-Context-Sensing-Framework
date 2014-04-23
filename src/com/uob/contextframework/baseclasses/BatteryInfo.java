/* **************************************************
Copyright (c) 2014, University of Birmingham
Karthikeya Udupa, kxu356@bham.ac.uk

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ************************************************** */

package com.uob.contextframework.baseclasses;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.uob.contextframework.ContextMonitor;
import com.uob.contextframework.support.Constants;
/**
 * @author karthikeyaudupa
 * Stores and handles battery intents.
 */
@SuppressLint("InlinedApi")
public class BatteryInfo extends BroadcastReceiver {

	private Context mContext;
	private Boolean isCharging;
	private BatteryChargeType currentChargingSource;
	private float batteryPercentage;


	/**
	 * Constructor of battery info.
	 * @param _ctx
	 */
	public BatteryInfo(Context _ctx){
		mContext = _ctx;
		batteryPercentage = ContextMonitor.getInstance(mContext).getBatteryLevel();
		currentChargingSource = ContextMonitor.getInstance(mContext).getCurrentChargingSource();
		isCharging = ContextMonitor.getInstance(mContext).getDeviceCharging();

	}

	/**
	 * Provides the battery level.
	 * @return battery value.
	 */
	public float getBatteryLevel() {

		Intent batteryIntent = mContext.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		if(level == -1 || scale == -1) {
			return 50.0f;
		}

		return ((float)level / (float)scale) * 100.0f; 
	}

	/*
	 * Battery information reciever.
	 * (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {

		int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		float battPct = level/(float)scale;  


		int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		Boolean deviceCharging = false;
		deviceCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
				status == BatteryManager.BATTERY_STATUS_FULL;

		int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
		boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
		boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
		boolean wirelessCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS;

		if(usbCharge)
			currentChargingSource = BatteryChargeType.BATTERY_PLUGGED_USB;
		if(acCharge)
			currentChargingSource = BatteryChargeType.BATTERY_PLUGGED_AC;
		if(wirelessCharge)
			currentChargingSource = BatteryChargeType.BATTERY_PLUGGED_WIRELESS;

		if(!deviceCharging)
			currentChargingSource = BatteryChargeType.BATTERY_PLUGGED_NO;


		boolean isNotificationNeeded = false;
		if(currentChargingSource != ContextMonitor.getInstance(mContext).getCurrentChargingSource()){
			isNotificationNeeded = true;
		}

		if(deviceCharging != ContextMonitor.getInstance(mContext).getDeviceCharging()){
			isNotificationNeeded = true;
		}

		if(battPct<15 && ContextMonitor.getInstance(mContext).getBatteryLevel()>=15){
			isNotificationNeeded = true;
		}else if(battPct<10 && ContextMonitor.getInstance(mContext).getBatteryLevel()>=10){
			isNotificationNeeded = true;
		}else if(battPct<30 && ContextMonitor.getInstance(mContext).getBatteryLevel()>=30){
			isNotificationNeeded = true;
		}else if(battPct<50 && ContextMonitor.getInstance(mContext).getBatteryLevel()>=50){
			isNotificationNeeded = true;
		}else if(battPct>=50 && ContextMonitor.getInstance(mContext).getBatteryLevel()<50){
			isNotificationNeeded = true;
		}else if(battPct>=80 && ContextMonitor.getInstance(mContext).getBatteryLevel()<80){
			isNotificationNeeded = true;
		}else if(battPct>=95 && ContextMonitor.getInstance(mContext).getBatteryLevel()<95){
			isNotificationNeeded = true;
		}


		ContextMonitor.getInstance(mContext).setCurrentChargingSource(currentChargingSource);
		ContextMonitor.getInstance(mContext).setDeviceCharging(deviceCharging);
		ContextMonitor.getInstance(mContext).setBatteryLevel(battPct);


		batteryPercentage = battPct;
		isCharging =	deviceCharging;

		if(isNotificationNeeded==true){
			pushBroadcast();
		}
	}

	/**
	 * Pushes latest battery information as broadcast.
	 */
	public void pushBroadcast(){
		Intent proxIntent = new Intent(Constants.CONTEXT_CHANGE_NOTIFY);
		proxIntent.putExtra(Constants.INTENT_TYPE, Constants.BATTERY_NOTIFY);
		proxIntent.putExtra(Constants.BATTERY_NOTIFY,toString());
		mContext.sendBroadcast(proxIntent);
	}


	//Data conversion methods.
	@Override
	public String toString() {

		return toJSON().toString();
	}

	public JSONObject toJSON() {

		JSONObject jObject = new JSONObject();
		try {
			jObject.put(Constants.IS_CHARGING, isCharging);
			jObject.put(Constants.CHARGING_SRC, String.valueOf(currentChargingSource));
			jObject.put(Constants.BATTERY_PCTG, String.valueOf(batteryPercentage));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jObject;
	}

}
