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

import com.uob.contextframework.ContextMonitor;
import com.uob.contextframework.support.Constants;
import com.uob.contextframework.support.NetworkHelper;

/**
 * @author karthikeyaudupa
 * Model for storing WiFi information and also responsbile for handling scan result broadcast.
 */
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

	public void onReceive(Context c, Intent intent) {

		WifiManager mainWifi  = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
		List<ScanResult> wifiList = mainWifi.getScanResults();

		ArrayList<WifiAccessPointModel> accessPointsList = new ArrayList<WifiAccessPointModel>();
		for (int i = 0; i < wifiList.size(); i++) {

			ScanResult scanResult = wifiList.get(i);
			WifiAccessPointModel accessPoint = new WifiAccessPointModel();
			accessPoint.setAddress(scanResult.BSSID);
			accessPoint.setCapabilities(scanResult.capabilities);
			accessPoint.setNetworkName(scanResult.SSID);
			accessPoint.setSignalLevel(scanResult.level);
			if(existsInCurrentArray(accessPointsList, accessPoint)==false){
				accessPointsList.add(accessPoint);
			}
		}
		accessPoints = accessPointsList; 
		ContextMonitor.getInstance(c).setAccessPoints(accessPoints);


		Intent signalIntent = new Intent(Constants.CONTEXT_CHANGE_NOTIFY);
		signalIntent.putExtra(Constants.INTENT_TYPE, Constants.WIFI_NOTIFY);
		signalIntent.putExtra(Constants.WIFI_NOTIFY,toString());
		mContext.sendBroadcast(signalIntent);

	}

	/**
	 * Checks if the access point already exists in the array.
	 * @param points
	 * @param currentPoint
	 * @return
	 */
	private boolean existsInCurrentArray(ArrayList<WifiAccessPointModel> points, WifiAccessPointModel currentPoint){

		for(WifiAccessPointModel w: points){
			if(w.isEqualTo(currentPoint)){
				return true;
			}
		}
		return false;
	}
	

	//Data coversion methods.
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

	
}