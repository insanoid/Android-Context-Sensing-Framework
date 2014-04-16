package com.uob.contextframework;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.uob.contextframework.baseclasses.BatteryChargeType;
import com.uob.contextframework.baseclasses.BatteryInfo;
import com.uob.contextframework.baseclasses.NetworkConnectionStatus;
import com.uob.contextframework.baseclasses.SignalInfo;
import com.uob.contextframework.baseclasses.SignalInfoModel;
import com.uob.contextframework.baseclasses.WiFiInfo;
import com.uob.contextframework.baseclasses.WifiAccessPointModel;
import com.uob.contextframework.support.Constants;

public class ContextMonitor {
	
	//Class Properties.
	private static ContextMonitor mInstance = null;
	private Context mContext;
	
	//Timers.
	private Timer locationMonitoringTimer;
	private Timer dataConnectionStateMonitorTimer;
		
	//Location Context
	private Location bestAvailableLocation;
	private String locationNetworkProvider;
	
	// Battery Context
	BatteryInfo batteryInfo;
	private Boolean deviceCharging = false; 
	private BatteryChargeType currentChargingSource = BatteryChargeType.BATTERY_PLUGGED_AC;
	private float batteryLevel = 0;

	// Signal Info.
	public SignalInfoModel signalInfo;
	public SignalInfo singalInfoReceiver;
	
	//WiFi Info.
	private ArrayList<WifiAccessPointModel> accessPoints;
	private WiFiInfo wifiBroadcastListner;
	
	//Network Info.
	private NetworkConnectionStatus networkConnectionStatus;
	
	/**
	 * Destroy connections if needed.
	 */
	public void destroy(){
		
		if(batteryInfo!=null)
			mContext.unregisterReceiver(batteryInfo);
		
		if(wifiBroadcastListner!=null)
			mContext.unregisterReceiver(wifiBroadcastListner);
		
	}

	public static ContextMonitor getInstance(Context _ctx){
		if(mInstance == null) {
			mInstance = new ContextMonitor(_ctx);
		}
		return mInstance;
	}

	private ContextMonitor(Context context){

		mContext = context;
		Handler h = new Handler(mContext.getMainLooper());

		h.post(new Runnable() {
			@Override
			public void run() {

			}
		});	



	}
	
	public void initiateLocationServices(long pollingTime, ArrayList<Integer> flags) {
		
		locationNetworkProvider = LocationManager.GPS_PROVIDER;
		if(flags!=null){
			if(flags.get(0)==1){
				locationNetworkProvider = LocationManager.NETWORK_PROVIDER;
			}
		}
		bestAvailableLocation = new Location(LocationManager.NETWORK_PROVIDER);
		locationMonitoringTimer = new Timer("LONG_TERM_POLLER");
		locationMonitoringTimer.schedule(longTermTasks, 0, pollingTime>0?pollingTime:Constants.SHORT_POLLING_INTERVAL);
	}
	
	public void stopLocationServices(){
		locationMonitoringTimer.cancel();
	}

	public void initiateBatteryServices() {
		batteryInfo = new BatteryInfo(mContext);
		IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		mContext.registerReceiver(batteryInfo, batteryLevelFilter);
	}
	
	public void stopBatteryServices(){
		mContext.unregisterReceiver(batteryInfo);
		batteryInfo = null;
	}
	
	@SuppressLint("NewApi")
	public void initiateSignalServices(long pollingTime) {
		signalInfo = new SignalInfoModel();
		dataConnectionStateMonitorTimer = new Timer("MINUTE_TERM_TIMER");
		dataConnectionStateMonitorTimer.schedule(minuteDataTask, 0, pollingTime>0?pollingTime:Constants.MINUTE_POLLING_INTERVAL);
		TelephonyManager Tel = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
        Tel.listen(singalInfoReceiver, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        signalInfo.setNearByCells(Tel.getAllCellInfo());
        signalInfo.setDataConnectionState(Tel.getDataState());
	}
	
	public void stopSignalServices(){
		dataConnectionStateMonitorTimer.cancel();
	}

	public void initiateWiFiServices(long pollingTime) {
		networkConnectionStatus = new NetworkConnectionStatus();
		wifiBroadcastListner = new WiFiInfo(mContext);
		mContext.registerReceiver(wifiBroadcastListner, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	}
	
	public void stopWiFiServices(){
		mContext.unregisterReceiver(wifiBroadcastListner);
		wifiBroadcastListner = null;
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
					
					Log.e(Constants.TAG,"Location Updated Broadcasted");
					setBestAvailableLocation(arg0);

					Intent intent = new Intent(Constants.CONTEXT_CHANGE_NOTIFY);  
					intent.putExtra(Constants.INTENT_TYPE, Constants.LOC_NOTIFY);
					intent.putExtra(Constants.LOC_NOTIFY,arg0);
					mContext.sendBroadcast(intent);

					locationManager.removeUpdates(this);

				}
			};

			//TODO: Make it work for any type.
			//Criteria criteria = new Criteria();     
			boolean isProviderEnabled = locationManager.isProviderEnabled(locationNetworkProvider);
			if(isProviderEnabled)
			{
				//final String mProvider = locationManager.getBestProvider(criteria, false);
				Handler h = new Handler(mContext.getMainLooper());

				h.post(new Runnable() {
					@Override
					public void run() {
						locationManager.requestSingleUpdate(locationNetworkProvider, locationListner, null);
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

	/**
	 * @return the accessPoints
	 */
	public ArrayList<WifiAccessPointModel> getAccessPoints() {
		return accessPoints;
	}

	/**
	 * @param accessPoints the accessPoints to set
	 */
	public void setAccessPoints(ArrayList<WifiAccessPointModel> accessPoints) {
		this.accessPoints = accessPoints;
	}

	/**
	 * @return the networkConnectionStatus
	 */
	public NetworkConnectionStatus getNetworkConnectionStatus() {
		return networkConnectionStatus;
	}

	/**
	 * @param networkConnectionStatus the networkConnectionStatus to set
	 */
	public void setNetworkConnectionStatus(
			NetworkConnectionStatus networkConnectionStatus) {
		this.networkConnectionStatus = networkConnectionStatus;
	}
}
