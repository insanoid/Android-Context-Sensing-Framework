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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author karthikeyaudupa
 * Model to store information about wifi access points.
 */
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

	/**
	 * Checks if the wifi accesspoint is of similar nature (repeaters)
	 * @param wifiNode node to compare with.
	 * @return
	 */
	public boolean isEqualTo(WifiAccessPointModel wifiNode){
		// && wifiNode.getAddress().equalsIgnoreCase(getAddress())
		if(wifiNode.getNetworkName().equalsIgnoreCase(getNetworkName())) {
			return true;
		}
		return false;
	}

	//Data conversion methods.
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
			
		}
		return jObject;
	}
}
