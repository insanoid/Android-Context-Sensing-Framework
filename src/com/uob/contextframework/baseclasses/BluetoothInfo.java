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

public class BluetoothInfo extends BroadcastReceiver{

	private String device_name;
	private String device_address;
	private Boolean is_paired;


	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if(BluetoothDevice.ACTION_FOUND.equals(action)) {
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			List<BluetoothInfo>devices = ContextMonitor.getInstance(context).getNearbyBluetoothAccessPoints();
			BluetoothInfo _d = new BluetoothInfo();
			_d.device_address = device.getAddress();
			_d.device_name = device.getName();
			if(!devices.contains(_d)){
				devices.add(_d);
			}
			ContextMonitor.getInstance(context).setNearbyBluetoothAccessPoints(devices);
		}	
		else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
			BluetoothInfo.sendBroadcast(context);
		}
	}

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
	 * @return the device_name
	 */
	public String getDevice_name() {
		return device_name;
	}


	/**
	 * @param device_name the device_name to set
	 */
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}


	/**
	 * @return the device_address
	 */
	public String getDevice_address() {
		return device_address;
	}


	/**
	 * @param device_address the device_address to set
	 */
	public void setDevice_address(String device_address) {
		this.device_address = device_address;
	}


	/**
	 * @return the is_paired
	 */
	public Boolean getIs_paired() {
		return is_paired;
	}


	/**
	 * @param is_paired the is_paired to set
	 */
	public void setIs_paired(Boolean is_paired) {
		this.is_paired = is_paired;
	}

	@Override
	public String toString() {

		return toJSON().toString();
	}

	public JSONObject toJSON() {

		JSONObject jObject = new JSONObject();
		try {
	
			jObject.put(Constants.DEVICE_NAME, String.valueOf(device_name));
			jObject.put(Constants.DEVICE_ADDRESS, String.valueOf(device_address));
			jObject.put(Constants.DEVICE_PAIRED, String.valueOf(is_paired));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jObject;
	}
}
