package com.uob.contextframework.baseclasses;

import org.json.JSONException;
import org.json.JSONObject;

public class WifiAccessPointModel {

	String address;
	String networkName;
	String capabilities;
	int signalLevel;
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the networkName
	 */
	public String getNetworkName() {
		return networkName;
	}
	/**
	 * @param networkName the networkName to set
	 */
	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}
	/**
	 * @return the capabilities
	 */
	public String getCapabilities() {
		return capabilities;
	}
	/**
	 * @param capabilities the capabilities to set
	 */
	public void setCapabilities(String capabilities) {
		this.capabilities = capabilities;
	}
	/**
	 * @return the signalLevel
	 */
	public int getSignalLevel() {
		return signalLevel;
	}
	/**
	 * @param signalLevel the signalLevel to set
	 */
	public void setSignalLevel(int signalLevel) {
		this.signalLevel = signalLevel;
	}
	

@Override
public String toString() {

	return toJSON().toString();
}

public JSONObject toJSON() {

	JSONObject jObject = new JSONObject();
	try {

		jObject.put("WName", getNetworkName());
		jObject.put("WCap", getCapabilities());
		jObject.put("WSignalLvl", getSignalLevel());
		jObject.put("WAddress", getAddress());

	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return jObject;
}
}
