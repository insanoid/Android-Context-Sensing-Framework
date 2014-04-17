package com.uob.contextframework;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;

import com.uob.contextframework.baseclasses.BatteryInfo;
import com.uob.contextframework.baseclasses.BluetoothInfo;
import com.uob.contextframework.baseclasses.DeviceInfo;
import com.uob.contextframework.baseclasses.Event;
import com.uob.contextframework.baseclasses.LocationInfo;
import com.uob.contextframework.baseclasses.SignalInfo;
import com.uob.contextframework.baseclasses.WiFiInfo;
import com.uob.contextframework.support.Constants;
import com.uob.contextframework.support.ContextManagerServices;



public class ContextManager {

	private Context mContext;
	private Timer locationTimer, batteryTimer, signalTimer, wifiTimer, eventsTimer, bluetoothTimer;

	public ContextManager(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public void monitorContext(ContextManagerServices mService, long minimumUpdateTime){
		monitorContext(mService, minimumUpdateTime,0, null);
	}


	public void monitorContext(ContextManagerServices mService, long minimumUpdateTime, long pollingTime, ArrayList<Integer> flags ){

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

		if(mService == ContextManagerServices.CTX_FRAMEWORK_WIFI){

			ContextMonitor.getInstance(mContext).initiateWiFiServices(pollingTime);
			wifiTimer = new Timer("WIFI_TIMER");
			wifiTimer.schedule(wifiUpdateTask, 0, minimumUpdateTime);

		}
		
		if(mService == ContextManagerServices.CTX_FRAMEWORK_EVENTS){

			ContextMonitor.getInstance(mContext).initiateCalendarServices(pollingTime);
			eventsTimer = new Timer("EVENT_TIMER");
			eventsTimer.schedule(eventsUpdateTask, 0, minimumUpdateTime);

		}
		
		if(mService == ContextManagerServices.CTX_FRAMEWORK_BLUETOOTH){

			ContextMonitor.getInstance(mContext).initiateBluetoothServices(pollingTime);
			bluetoothTimer = new Timer("BLUETOOTH_TIMER");
			eventsTimer.schedule(bluetoothUpdateTask, 0, minimumUpdateTime);

		}
		
		

	}


	public void stopMonitoringContext(ContextManagerServices mService){

		if(mService == ContextManagerServices.CTX_FRAMEWORK_LOCATION){
			locationTimer.cancel();
			ContextMonitor.getInstance(mContext).stopLocationServices();
		}

		if(mService == ContextManagerServices.CTX_FRAMEWORK_BATTERY){
			batteryTimer.cancel();
			ContextMonitor.getInstance(mContext).stopBatteryServices();
		}

		if(mService == ContextManagerServices.CTX_FRAMEWORK_SIGNALS){
			signalTimer.cancel();
			ContextMonitor.getInstance(mContext).stopSignalServices();
		}

		if(mService == ContextManagerServices.CTX_FRAMEWORK_WIFI){
			wifiTimer.cancel();
			ContextMonitor.getInstance(mContext).stopWiFiServices();
		}
		
		if(mService == ContextManagerServices.CTX_FRAMEWORK_EVENTS){
			eventsTimer.cancel();
			ContextMonitor.getInstance(mContext).stopCalendarServices();

		}
		
		if(mService == ContextManagerServices.CTX_FRAMEWORK_BLUETOOTH){
			bluetoothTimer.cancel();
			ContextMonitor.getInstance(mContext).stopBluetoothServices();
		}
	}

	TimerTask locationUpdateTask = new TimerTask() {

		@Override
		public void run() {
			LocationInfo locationInfo = new LocationInfo(mContext);
			Location loc = locationInfo.getLocation();
			Intent intent = new Intent(Constants.CONTEXT_CHANGE_NOTIFY);
			intent.putExtra(Constants.INTENT_TYPE, Constants.LOC_NOTIFY);
			intent.putExtra(Constants.LOC_NOTIFY,loc);
			mContext.sendBroadcast(intent);
		}
	};

	TimerTask batteryUpdateTask = new TimerTask() {

		@Override
		public void run() {
			Handler h = new Handler(mContext.getMainLooper());

			h.post(new Runnable() {
				@Override
				public void run() {
					BatteryInfo batteryInfo = new BatteryInfo(mContext);
					Intent intent = new Intent(Constants.CONTEXT_CHANGE_NOTIFY);
					intent.putExtra(Constants.INTENT_TYPE, Constants.BATTERY_NOTIFY);
					intent.putExtra(Constants.BATTERY_NOTIFY,batteryInfo.toString());
					mContext.sendBroadcast(intent);
				}
			});	
		}
	};

	TimerTask signalUpdateTask = new TimerTask() {

		@Override
		public void run() {

			Handler h = new Handler(mContext.getMainLooper());

			h.post(new Runnable() {
				@Override
				public void run() {
					SignalInfo signalInfo = new SignalInfo(mContext);
					Intent intent = new Intent(Constants.CONTEXT_CHANGE_NOTIFY);
					intent.putExtra(Constants.INTENT_TYPE, Constants.SIGNAL_NOTIFY);
					intent.putExtra(Constants.SIGNAL_NOTIFY,signalInfo.toString());
					mContext.sendBroadcast(intent);
				}
			});	
		}
	};

	TimerTask wifiUpdateTask = new TimerTask() {

		@Override
		public void run() {

			Handler h = new Handler(mContext.getMainLooper());

			h.post(new Runnable() {
				@Override
				public void run() {
					WiFiInfo wifiInfo = new WiFiInfo(mContext);
					Intent intent = new Intent(Constants.CONTEXT_CHANGE_NOTIFY);
					intent.putExtra(Constants.INTENT_TYPE, Constants.WIFI_NOTIFY);
					intent.putExtra(Constants.WIFI_NOTIFY, wifiInfo.toString());
					mContext.sendBroadcast(intent);
				}
			});	
		}
	};
	
	
	TimerTask eventsUpdateTask = new TimerTask() {

		@Override
		public void run() {

			Handler h = new Handler(mContext.getMainLooper());

			h.post(new Runnable() {
				@Override
				public void run() {
					JSONArray jArray = Event.currentEventList(mContext);
					Intent intent = new Intent(Constants.CONTEXT_CHANGE_NOTIFY);
					intent.putExtra(Constants.INTENT_TYPE, Constants.EVENT_NOTIFY);
					intent.putExtra(Constants.EVENT_NOTIFY, jArray.toString());
					mContext.sendBroadcast(intent);
					
				}
			});	
		}
	};

	TimerTask bluetoothUpdateTask = new TimerTask() {

		@Override
		public void run() {

			Handler h = new Handler(mContext.getMainLooper());

			h.post(new Runnable() {
				@Override
				public void run() {
					BluetoothInfo.sendBroadcast(mContext);
					
				}
			});	
		}
	};
	
	public static DeviceInfo getPhoneInformation(){
		return new DeviceInfo();
	}
}
