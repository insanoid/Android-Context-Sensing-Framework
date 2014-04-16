package com.uob.contextframework.baseclasses;

import org.json.JSONException;
import org.json.JSONObject;

import com.uob.contextframework.support.Constants;


public class NetworkConnectionStatus {

	public int currentNetworkType;
	public Boolean isConnected;

@Override
public String toString() {

	return toJSON().toString();
}

public JSONObject toJSON() {

	JSONObject jObject = new JSONObject();
	try {

		jObject.put(Constants.CONNTECTION_TYPE, String.valueOf(currentNetworkType));
		jObject.put(Constants.NETWORK_STATUS, String.valueOf(isConnected));

	} catch (JSONException e) {

	}
	return jObject;
}
}
