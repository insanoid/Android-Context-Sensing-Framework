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

public class BatteryInfo extends BroadcastReceiver {

	Context mContext;

	Boolean isCharging;
	BatteryChargeType currentChargingSource;
	float batteryPercentage;


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

	/*
	 * To poll battery levels.
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

	@SuppressLint("InlinedApi")
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

		ContextMonitor.getInstance(mContext).setCurrentChargingSource(currentChargingSource);
		ContextMonitor.getInstance(mContext).setDeviceCharging(deviceCharging);
		ContextMonitor.getInstance(mContext).setBatteryLevel(battPct);

		Intent proxIntent = new Intent(Constants.LOC_NOTIFY);
		proxIntent.putExtra(Constants.INTENT_TYPE, Constants.BATTERY_NOTIFY);
		proxIntent.putExtra(Constants.BATTERY_NOTIFY,toString());
		mContext.sendBroadcast(proxIntent);

	}

}
