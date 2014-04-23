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

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.uob.contextframework.ContextMonitor;
import com.uob.contextframework.support.Constants;

/**
 * @author karthikeyaudupa
 * Stores and handles bluetooth intents.
 */
public class BluetoothInfo extends BroadcastReceiver {

	private String deviceName;
	private String deviceAddress;
	private Boolean isPaired;


	/* Handles the broadcast for device scans.
	 * (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if(BluetoothDevice.ACTION_FOUND.equals(action)) {
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			List<BluetoothInfo>devices = ContextMonitor.getInstance(context).getNearbyBluetoothAccessPoints();
			BluetoothInfo _d = new BluetoothInfo();
			_d.deviceAddress = device.getAddress();
			_d.deviceName = device.getName();
			if(!devices.contains(_d)){
				devices.add(_d);
			}
			ContextMonitor.getInstance(context).setNearbyBluetoothAccessPoints(devices);
		}	
		else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
			BluetoothInfo.sendBroadcast(context);
		}
	}

	/**
	 * Sends broadcast with bluetooth information.
	 * @param mContext
	 */
	public static void sendBroadcast(Context mContext) {

		Boolean isOn = isBluetoothOn();
		JSONObject jObj = new JSONObject();
		try {
			jObj.put(Constants.IS_BLUETOOH_ON, isBluetoothOn());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(isOn == true){
			JSONArray jArray = new JSONArray();
			List<BluetoothInfo>devices = ContextMonitor.getInstance(mContext).getNearbyBluetoothAccessPoints();
			for(BluetoothInfo d: devices){
				try{
					jArray.put(d.toJSON());
				}catch(Exception e){

				}
			}

			try {
				jObj.put(Constants.BLUETOOH_DEVICES, jArray);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		Intent proxIntent = new Intent(Constants.CONTEXT_CHANGE_NOTIFY);
		proxIntent.putExtra(Constants.INTENT_TYPE, Constants.BLUETOOTH_NOTIFY);
		proxIntent.putExtra(Constants.BLUETOOTH_NOTIFY,jObj.toString());
		mContext.sendBroadcast(proxIntent);
	}

	/**
	 * Checks the status of the bluetooth.
	 * @return
	 */
	public static boolean isBluetoothOn() {

		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			return false;
		} else {
			if (mBluetoothAdapter.isEnabled()) {
				return true;
			}else{
				return false;
			}
		}
	}

	/**
	 * @return the deviceName
	 */
	public String getdeviceName() {
		return deviceName;
	}

	/**
	 * @param deviceName the deviceName to set
	 */
	public void setDeviceName(String _deviceName) {
		this.deviceName = _deviceName;
	}


	/**
	 * @return the deviceAddress
	 */
	public String getDeviceAddress() {
		return deviceAddress;
	}


	/**
	 * @param deviceAddress the deviceAddress to set
	 */
	public void setDeviceAddress(String _deviceAddress) {
		this.deviceAddress = _deviceAddress;
	}


	/**
	 * @return the _isPaired
	 */
	public Boolean getIsPaired() {
		return isPaired;
	}


	/**
	 * @param isPaired the _isPaired to set
	 */
	public void setIsPaired(Boolean _isPaired) {
		this.isPaired = _isPaired;
	}

	//Data conversion methods.
	@Override
	public String toString() {

		return toJSON().toString();
	}

	public JSONObject toJSON() {

		JSONObject jObject = new JSONObject();
		try {

			jObject.put(Constants.DEVICE_NAME, String.valueOf(deviceName));
			jObject.put(Constants.DEVICE_ADDRESS, String.valueOf(deviceAddress));
			jObject.put(Constants.DEVICE_PAIRED, String.valueOf(isPaired));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jObject;
	}
}
