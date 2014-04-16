package com.uob.contextframework.baseclasses;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.uob.contextframework.ContextMonitor;
import com.uob.contextframework.support.Constants;
import com.uob.contextframework.support.NetworkHelper;

public class WiFiInfo extends BroadcastReceiver {
	
	Context mContext;
	ArrayList<WifiAccessPointModel> accessPoints;
	boolean isWifiAvailable;
	boolean isWifiEnabled;
	
	public WiFiInfo(Context context){
		mContext = context;
		accessPoints = ContextMonitor.getInstance(context).getAccessPoints();
		isWifiAvailable = NetworkHelper.getInstance(context).isWiFiOn(context);
		isWifiEnabled = NetworkHelper.getInstance(context).isWiFiTurnedOn(context);
	}
	
	public void updateState(){
		isWifiAvailable = NetworkHelper.getInstance(mContext).isWiFiOn(mContext);
		isWifiEnabled = NetworkHelper.getInstance(mContext).isWiFiTurnedOn(mContext);
		accessPoints = null;
	}
	
	@Override
	public String toString() {
		
		return toJSON().toString();
	}
	
	public JSONObject toJSON() {

		JSONObject jObject = new JSONObject();
		try {
		
			jObject.put(Constants.IS_WIFI_AVAILABLE, isWifiAvailable);
			jObject.put(Constants.IS_WIFI_ON, isWifiEnabled);
			if(isWifiEnabled){
				jObject.put(Constants.ACCESSPOINT, accessPointJSON());
				jObject.put(Constants.ACCESSPOINT_COUNT, (accessPoints==null)?String.valueOf(0):String.valueOf(accessPoints.size()));
			}
		} catch (JSONException e) {

		}
		
		return jObject;
	}
	
	private JSONArray accessPointJSON() {
		
		JSONArray jArray = new JSONArray();
		if(accessPoints!=null){
		for(WifiAccessPointModel w: accessPoints){
			jArray.put(w.toJSON());
		}}
		return jArray;
	}
	
	public void onReceive(Context c, Intent intent) {

    	WifiManager mainWifi  = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> wifiList = mainWifi.getScanResults();
       
        ArrayList<WifiAccessPointModel> accessPointsList = new ArrayList<WifiAccessPointModel>();
        Log.e("----->","--"+accessPointsList);
        for (int i = 0; i < wifiList.size(); i++) {
            
        	ScanResult scanResult = wifiList.get(i);
           WifiAccessPointModel accessPoint = new WifiAccessPointModel();
           accessPoint.setAddress(scanResult.BSSID);
           accessPoint.setCapabilities(scanResult.capabilities);
           accessPoint.setNetworkName(scanResult.SSID);
           accessPoint.setSignalLevel(scanResult.level);
           accessPointsList.add(accessPoint);
        }
        accessPoints = accessPointsList; 
        ContextMonitor.getInstance(c).setAccessPoints(accessPoints);
        
		
		Intent signalIntent = new Intent(Constants.CONTEXT_CHANGE_NOTIFY);
		signalIntent.putExtra(Constants.INTENT_TYPE, Constants.SIGNAL_NOTIFY);
		signalIntent.putExtra(Constants.SIGNAL_NOTIFY,toString());
		mContext.sendBroadcast(signalIntent);
		
     }
	
}