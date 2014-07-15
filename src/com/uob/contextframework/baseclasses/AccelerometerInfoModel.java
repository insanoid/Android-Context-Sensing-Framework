package com.uob.contextframework.baseclasses;

import org.json.JSONArray;
import org.json.JSONException;

public class AccelerometerInfoModel {
	float x,y,z;

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * @return the z
	 */
	public float getZ() {
		return z;
	}

	/**
	 * @param z the z to set
	 */
	public void setZ(float z) {
		this.z = z;
	}
	
	public String toString() {
		
		return toJSON().toString();
	}
	
	public JSONArray toJSON() {
		JSONArray jArray = new JSONArray();
		
		try {
			jArray.put(x);
			jArray.put(y);
			jArray.put(z);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jArray;
		
	}
}
