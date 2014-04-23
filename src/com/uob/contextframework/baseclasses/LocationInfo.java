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

import android.content.Context;
import android.location.Location;

import com.uob.contextframework.ContextMonitor;
import com.uob.contextframework.support.Constants;
import com.uob.contextframework.support.LocationsHelper;

/**
 * @author karthikeyaudupa
 * Location information along with the logic to provide best possible location on intialization.
 */
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
	
	/**
	 * Provides the best location available.
	 * @return Location
	 */
	public Location getLocation(){
		return bestAvailableLocation;
	}
	

	
	//Data conversion methods.
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
