package com.uob.contextframework;

import java.util.Timer;
import java.util.TimerTask;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.uob.contextframework.baseclasses.BatteryChargeType;
import com.uob.contextframework.baseclasses.BatteryInfo;
import com.uob.contextframework.baseclasses.SignalInfo;
import com.uob.contextframework.baseclasses.SignalInfoModel;
import com.uob.contextframework.support.Constants;

public class ContextMonitor {


	private static ContextMonitor mInstance = null;
	private Context mContext;
	
	private Timer longTermTimer;
	private Timer minuteTermTimer;
	
	//Location Context
	private Location bestAvailableLocation;

	// Battery Context
	BatteryInfo batteryInfo;
	private Boolean deviceCharging = false; 
	private BatteryChargeType currentChargingSource = BatteryChargeType.BATTERY_PLUGGED_AC;
	private float batteryLevel = 0;

	// Signal Info.
	public SignalInfoModel signalInfo;
	public SignalInfo singalInfoReceiver;
	
	
	/**
	 * Destroy connections if needed.
	 */
	public void destroy(){
		mContext.unregisterReceiver(batteryInfo);
	//	mContext.unregisterReceiver(singalInfoReceiver);
	}

	public static ContextMonitor getInstance(Context context){
		if(mInstance == null)
		{
			mInstance = new ContextMonitor(context);
		}
		return mInstance;
	}

	private ContextMonitor(Context context){

		mContext = context;
		initiateBatteryServices();
		//initiateLocationServices();
		
		Handler h = new Handler(mContext.getMainLooper());

		h.post(new Runnable() {
			@Override
			public void run() {

			}
		});	



	}
	
	public void initiateLocationServices(long pollingTime, int[] flags) {
		bestAvailableLocation = new Location(LocationManager.NETWORK_PROVIDER);
		longTermTimer = new Timer("LONG_TERM_POLLER");
		longTermTimer.schedule(longTermTasks, 0, pollingTime>0?pollingTime:Constants.SHORT_POLLING_INTERVAL);
	}

	public void initiateBatteryServices() {
		batteryInfo = new BatteryInfo(mContext);
		IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		mContext.registerReceiver(batteryInfo, batteryLevelFilter);
	}
	
	@SuppressLint("NewApi")
	public void initiateSignalServices(long pollingTime) {
		signalInfo = new SignalInfoModel();
		minuteTermTimer = new Timer("MINUTE_TERM_TIMER");
		minuteTermTimer.schedule(minuteDataTask, 0, pollingTime>0?pollingTime:Constants.MINUTE_POLLING_INTERVAL);
		TelephonyManager Tel = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
        Tel.listen(singalInfoReceiver, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        signalInfo.setNearByCells(Tel.getAllCellInfo());
        signalInfo.setDataConnectionState(Tel.getDataState());
	}
	
	
	
	private TimerTask minuteDataTask = new TimerTask() {
		@Override
		public void run() {
			int dataConnType = signalInfo.getDataConnectionState();
			updateDataState();
			if(dataConnType!=signalInfo.getDataConnectionState()){
				Intent intent = new Intent(Constants.SIGNAL_NOTIFY);
				intent.putExtra(Constants.INTENT_TYPE, Constants.BATTERY_NOTIFY);
				intent.putExtra(Constants.SIGNAL_NOTIFY,signalInfo.toString());
				mContext.sendBroadcast(intent);
			}
		}
	};
	
	void updateDataState(){
		TelephonyManager tel = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
		signalInfo.setDataConnectionState(tel.getDataState());
	}
	
	
	/**
	 * Monitors long term polling tasks (set by polling interval)
	 */
	private TimerTask longTermTasks = new TimerTask() {
		@Override
		public void run() {

			updateLocationInformation();
		}

		/*
		 * Updates location information periodically.
		 */
		public void updateLocationInformation(){

			boolean isGPSEnabled = false;
			boolean isNetworkEnabled = false;

			final LocationManager locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
			final LocationListener locationListner = new LocationListener() {

				@Override
				public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onProviderEnabled(String arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onProviderDisabled(String arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onLocationChanged(Location arg0) {
					Log.e("CONTEXT_FRAMEWORK: MONITOR","Location Updated Broadcasted");
					setBestAvailableLocation(arg0);

					Intent proxIntent = new Intent(Constants.LOC_NOTIFY);  
					proxIntent.putExtra(Constants.LOC_NOTIFY,arg0);
					mContext.sendBroadcast(proxIntent);

					locationManager.removeUpdates(this);

				}
			};

			//TODO: Make it work for any type.
			//Criteria criteria = new Criteria();     
			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			if(isGPSEnabled || isNetworkEnabled)
			{
				//final String mProvider = locationManager.getBestProvider(criteria, false);
				Handler h = new Handler(mContext.getMainLooper());

				h.post(new Runnable() {
					@Override
					public void run() {
						locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListner, null);
					}
				});	

			}else{

			}
		}

	};

	/**
	 * @return the bestAvailableLocation
	 */
	public Location getBestAvailableLocation() {
		return bestAvailableLocation;
	}


	/**
	 * @param bestAvailableLocation the bestAvailableLocation to set
	 */
	public void setBestAvailableLocation(Location bestAvailableLocation) {
		this.bestAvailableLocation = bestAvailableLocation;
	}

	/**
	 * @return the deviceCharging
	 */
	public Boolean getDeviceCharging() {
		return deviceCharging;
	}

	/**
	 * @param deviceCharging the deviceCharging to set
	 */
	public void setDeviceCharging(Boolean deviceCharging) {
		this.deviceCharging = deviceCharging;
	}

	/**
	 * @return the currentChargingSource
	 */
	public BatteryChargeType getCurrentChargingSource() {
		return currentChargingSource;
	}

	/**
	 * @param currentChargingSource the currentChargingSource to set
	 */
	public void setCurrentChargingSource(BatteryChargeType currentChargingSource) {
		this.currentChargingSource = currentChargingSource;
	}

	/**
	 * @return the batteryLevel
	 */
	public float getBatteryLevel() {
		return batteryLevel;
	}

	/**
	 * @param batteryLevel the batteryLevel to set
	 */
	public void setBatteryLevel(float batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	/**
	 * @return the signalInfo
	 */
	public SignalInfoModel getSignalInfo() {
		return signalInfo;
	}

	/**
	 * @param signalInfo the signalInfo to set
	 */
	public void setSignalInfo(SignalInfoModel signalInfo) {
		this.signalInfo = signalInfo;
	}
}
