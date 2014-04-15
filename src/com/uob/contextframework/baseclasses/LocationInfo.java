package com.uob.contextframework.baseclasses;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;

import com.uob.contextframework.ContextMonitor;
import com.uob.contextframework.support.Constants;
import com.uob.contextframework.support.LocationsHelper;

public class LocationInfo {
	
	private Context mContext;
	private Location bestAvailableLocation;
	
	public LocationInfo(Context context){
		
		mContext = context;
		Location bestStoredLocaton = ContextMonitor.getInstance(mContext).getBestAvailableLocation();
		Location bestNewLocation = LocationsHelper.getLatestLocation(mContext);
		Boolean isNewBetter = LocationsHelper.isBetterLocation(bestNewLocation, bestStoredLocaton);
		if(isNewBetter){
			bestAvailableLocation = bestNewLocation;
		}else{
			bestAvailableLocation = bestStoredLocaton;
		}
	}
	
	public Location getLocation(){
		return bestAvailableLocation;
	}
	

	@Override
	public String toString() {
		
		return toJSON().toString();
	}
	
	public JSONObject toJSON() {
		
		JSONObject jObject = new JSONObject();
		try {
			jObject.put(Constants.LAT, bestAvailableLocation.getLatitude());
			jObject.put(Constants.LNG, bestAvailableLocation.getLongitude());
			jObject.put(Constants.ALT, bestAvailableLocation.getAltitude());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return jObject;
	}
}
