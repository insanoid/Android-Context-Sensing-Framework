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
import com.uob.contextframework.baseclasses.PhoneProfile;
import com.uob.contextframework.baseclasses.SignalInfo;
import com.uob.contextframework.baseclasses.WiFiInfo;
import com.uob.contextframework.support.Constants;
import com.uob.contextframework.support.ContextManagerServices;

/**
 * @author karthikeyaudupa
 * Context manager to initate and handle monitoring tasks.
 */
public class ContextManager {

	private Context mContext;
	private Timer locationTimer, batteryTimer, signalTimer, wifiTimer, eventsTimer, bluetoothTimer, phoneProfileTimer;


	/**
	 * Constructor for the class.
	 * @param mContext context for the object.
	 */
	public ContextManager(Context mContext) {
		super();
		this.mContext = mContext;
	}

	/**
	 * Monitoring service of a certain context is initiated with this call.
	 * @param mService  Defines the context required to be monitored
	 * @param minimumUpdateTime Minimum interval after which a broadcast is sent.
	 * @throws Exception 
	 */
	public void monitorContext(ContextManagerServices mService, long minimumUpdateTime) throws Exception{
		monitorContext(mService, minimumUpdateTime,0, null);
	}


	/**
	 * Monitoring service of a certain context is initiated with this call.
	 * @param mService Defines the context required to be monitored
	 * @param minimumUpdateTime Minimum interval after which a broadcast is sent.
	 * @param pollingTime Minimum interval after which the sensor is polled for data.
	 * @param flags Additional flags for the context monitoring.
	 * @throws Exception 
	 */
	public void monitorContext(ContextManagerServices mService, long minimumUpdateTime, long pollingTime, ArrayList<Integer> flags ) throws Exception{

		if(checkPermissionForContext(mService)==true){
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
				bluetoothTimer.schedule(bluetoothUpdateTask, 0, minimumUpdateTime);
			}
			
			if(mService == ContextManagerServices.CTX_FRAMEWORK_PHONE_SETTINGS){
				ContextMonitor.getInstance(mContext).initiatePhoneProfileServices(pollingTime);
				phoneProfileTimer = new Timer("PHONE_PROFILE_TIMER");
				phoneProfileTimer.schedule(phoneProfileUpdateTask, 0, minimumUpdateTime);
			}
			
		}else{
			throw new Exception("Requires permission to monitor this context.");
		}


	}


	/**
	 * Stops a already running monitoring service, if the service is not running does nothing.
	 * @param mService Context that needs to be stopped to be monitored.
	 */
	public void stopMonitoringContext(ContextManagerServices mService){

		if(mService == ContextManagerServices.CTX_FRAMEWORK_LOCATION){
			if(locationTimer!=null){
				locationTimer.cancel();
				locationTimer = null;
			}
			ContextMonitor.getInstance(mContext).stopLocationServices();
		}

		if(mService == ContextManagerServices.CTX_FRAMEWORK_BATTERY){
			if(batteryTimer!=null){
				batteryTimer.cancel();
				batteryTimer = null;
			}
			
			ContextMonitor.getInstance(mContext).stopBatteryServices();
		}

		if(mService == ContextManagerServices.CTX_FRAMEWORK_SIGNALS){
			if(signalTimer!=null){
				signalTimer.cancel();
				batteryTimer = null;
			}
			ContextMonitor.getInstance(mContext).stopSignalServices();
		}

		if(mService == ContextManagerServices.CTX_FRAMEWORK_WIFI){
			if(wifiTimer!=null){
				wifiTimer.cancel();
				wifiTimer = null;
			}
			ContextMonitor.getInstance(mContext).stopWiFiServices();
		}

		if(mService == ContextManagerServices.CTX_FRAMEWORK_EVENTS){
			if(eventsTimer!=null){
				eventsTimer.cancel();
				eventsTimer = null;
			}
			ContextMonitor.getInstance(mContext).stopCalendarServices();

		}

		if(mService == ContextManagerServices.CTX_FRAMEWORK_BLUETOOTH){
			if(bluetoothTimer!=null){
				bluetoothTimer.cancel();
				bluetoothTimer = null;
			}
			ContextMonitor.getInstance(mContext).stopBluetoothServices();
		}
		
		if(mService == ContextManagerServices.CTX_FRAMEWORK_PHONE_SETTINGS){
			if(phoneProfileTimer!=null){
				phoneProfileTimer.cancel();
				phoneProfileTimer = null;
			}
			ContextMonitor.getInstance(mContext).stopPhoneProfileServices();
		}
		
	}


	/**
	 * Location information broadcasting task.
	 * Type : LOC_NOTIFY
	 */
	private TimerTask locationUpdateTask = new TimerTask() {

		@Override
		public void run() {
			LocationInfo locationInfo = new LocationInfo(mContext);
			Location loc = locationInfo.getLocation();
			if(loc!=null){
			Intent intent = new Intent(Constants.CONTEXT_CHANGE_NOTIFY);
			intent.putExtra(Constants.INTENT_TYPE, Constants.LOC_NOTIFY);
			intent.putExtra(Constants.LOC_NOTIFY,loc);
			mContext.sendBroadcast(intent);
			}
		}
	};

	/**
	 * Battery information broadcasting task.
	 * Type : BATTERY_NOTIFY
	 */
	private TimerTask batteryUpdateTask = new TimerTask() {

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

	
	/**
	 * Network signal information broadcasting task.
	 * Type : SIGNAL_NOTIFY
	 */
	private TimerTask signalUpdateTask = new TimerTask() {

		@Override
		public void run() {
			Handler h = new Handler(mContext.getMainLooper());
			h.post(new Runnable() {
				@Override
				public void run() {
					SignalInfo signalInfo = new SignalInfo(mContext);
					if(signalInfo!=null){
						Intent intent = new Intent(Constants.CONTEXT_CHANGE_NOTIFY);
						intent.putExtra(Constants.INTENT_TYPE, Constants.SIGNAL_NOTIFY);
						intent.putExtra(Constants.SIGNAL_NOTIFY,signalInfo.toString());
						mContext.sendBroadcast(intent);
					}
				}
			});	
		}
	};

	
	/**
	 * Network signal information broadcasting task.
	 * Type : WIFI_NOTIFY
	 */
	private TimerTask wifiUpdateTask = new TimerTask() {

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

	
	/**
	 * user event information broadcasting task.
	 * Type : EVENT_NOTIFY
	 */
	private TimerTask eventsUpdateTask = new TimerTask() {
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

	
	/**
	 * Bluetooth network information broadcasting task.
	 * Type : BLUETOOTH_NOTIFY
	 */
	private TimerTask bluetoothUpdateTask = new TimerTask() {

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
	
	/**
	 * Phone setting information broadcasting task.
	 * Type : PHONE_PROFILE_NOTIFY
	 */
	private TimerTask phoneProfileUpdateTask = new TimerTask() {

		@Override
		public void run() {

			Handler h = new Handler(mContext.getMainLooper());

			h.post(new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent(Constants.CONTEXT_CHANGE_NOTIFY);
					intent.putExtra(Constants.INTENT_TYPE, Constants.PHONE_PROFILE_NOTIFY);
					intent.putExtra(Constants.PHONE_PROFILE_NOTIFY,	new PhoneProfile(mContext).toString());
					mContext.sendBroadcast(intent);

				}
			});	
		}
	};
	

	/**
	 * Provides information about the device in the form of a @{DeviceInfo} object.
	 * @return DeviceInfo <b>DeviceInfo</b> object containing information about the device.
	 */
	public static DeviceInfo getPhoneInformation(){
		return new DeviceInfo();
	}

	/**
	 * Function checks if the required permissions are available for a certain context monitoring task.
	 * @param mService Type of context to check permissions for.
	 * @return boolean specifies if permission is allowed or not.
	 */
	private static boolean checkPermissionForContext(ContextManagerServices mService) {
		return true;
	}
}
