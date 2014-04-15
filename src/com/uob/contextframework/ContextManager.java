package com.uob.contextframework;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.uob.contextframework.baseclasses.BatteryInfo;
import com.uob.contextframework.baseclasses.DeviceInfo;
import com.uob.contextframework.baseclasses.LocationInfo;
import com.uob.contextframework.baseclasses.SignalInfo;
import com.uob.contextframework.support.Constants;
import com.uob.contextframework.support.ContextManagerServices;



public class ContextManager {

	private Context mContext;
	private Timer locationTimer, batteryTimer, signalTimer;

	public ContextManager(Context mContext) {
		super();
		this.mContext = mContext;
	}
	
	public void monitorContext(ContextManagerServices mService, long minimumUpdateTime){
		monitorContext(mService, minimumUpdateTime,0, null);
	}
	

	public void monitorContext(ContextManagerServices mService, long minimumUpdateTime, long pollingTime, int[] flags ){

		if(mService == ContextManagerServices.CTX_FRAMEWORK_LOCATION){

			ContextMonitor.getInstance(mContext).initiateLocationServices(pollingTime, flags);
			locationTimer = new Timer("LOCATION_TIMER");
			locationTimer.schedule(locationUpdateTask, 0, minimumUpdateTime);

		}

		if(mService == ContextManagerServices.CTX_FRAMEWORK_BATTERY){

			ContextMonitor.getInstance(mContext).initiateBatteryServices();
			batteryTimer = new Timer("BATTERY_TIMER");
			batteryTimer.schedule(batteryUpdateTask, 0, minimumUpdateTime);

		}

		if(mService == ContextManagerServices.CTX_FRAMEWORK_SIGNALS){

			ContextMonitor.getInstance(mContext).initiateSignalServices(pollingTime);
			signalTimer = new Timer("SIGNAL_TIMER");
			signalTimer.schedule(signalUpdateTask, 0, minimumUpdateTime);

		}


	}

	TimerTask locationUpdateTask = new TimerTask() {

		@Override
		public void run() {
			LocationInfo locationInfo = new LocationInfo(mContext);
			Location loc = locationInfo.getLocation();
			Intent intent = new Intent(Constants.LOC_NOTIFY);
			intent.putExtra(Constants.INTENT_TYPE, Constants.LOC_NOTIFY);
			intent.putExtra(Constants.LOC_NOTIFY,loc);
			mContext.sendBroadcast(intent);
		}
	};

	TimerTask batteryUpdateTask = new TimerTask() {

		@Override
		public void run() {
			BatteryInfo batteryInfo = new BatteryInfo(mContext);
			Intent intent = new Intent(Constants.LOC_NOTIFY);
			intent.putExtra(Constants.INTENT_TYPE, Constants.BATTERY_NOTIFY);
			intent.putExtra(Constants.BATTERY_NOTIFY,batteryInfo.toString());
			mContext.sendBroadcast(intent);
		}
	};

	TimerTask signalUpdateTask = new TimerTask() {

		@Override
		public void run() {
			SignalInfo signalInfo = new SignalInfo(mContext);
			Intent intent = new Intent(Constants.SIGNAL_NOTIFY);
			intent.putExtra(Constants.INTENT_TYPE, Constants.BATTERY_NOTIFY);
			intent.putExtra(Constants.SIGNAL_NOTIFY,signalInfo.toString());
			mContext.sendBroadcast(intent);
		}
	};


	public static DeviceInfo getPhoneInformation(){
		return new DeviceInfo();
	}
}
