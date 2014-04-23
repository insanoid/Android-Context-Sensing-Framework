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

import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;

/**
 * @author karthikeyaudupa
 * Provides device information as an compact object.
 */
public class DeviceInfo
{
	private String bootloader;
	private String brand;
	private String device;
	private String display;
	private String manufacture;
	private String model;
	private String product;
	private String osVersion;
	private String timezone;

	private static String BOOTLOADER = "bootloader";
	private static String BRAND = "brand";
	private static String DEVICE = "device";
	private static String DISPLAY = "display";
	private static String MANF = "manufacture";
	private static String MODEL = "model";
	private static String PRODUCT = "product";
	private static String OSVERSION = "osversion";
	private static String TIMEZONE = "timezone";
	
	/**
	 * Constructor.
	 */
	public DeviceInfo() {
		
		bootloader = Build.BOOTLOADER;
		brand = Build.BRAND;
		device = Build.DEVICE;
		display = Build.DISPLAY;
		manufacture = Build.MANUFACTURER;
		model = Build.MODEL;
		product = Build.PRODUCT;
		osVersion = Build.VERSION.RELEASE;
		timezone = TimeZone.getDefault().getID();
	
	}


	/**
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(String product) {
		this.product = product;
	}

	/**
	 * @return the bootloader
	 */
	public String getBootloader() {
		return bootloader;
	}

	/**
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * @return the device
	 */
	public String getDevice() {
		return device;
	}

	/**
	 * @return the display
	 */
	public String getDisplay() {
		return display;
	}

	/**
	 * @return the manufacture
	 */
	public String getManufacture() {
		return manufacture;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @return the osVersion
	 */
	public String getOsVersion() {
		return osVersion;
	}

	/**
	 * @return the timezone
	 */
	public String getTimezone() {
		return timezone;
	}

	//data conversion method.
	@Override
	public String toString() {
		
		return toJSON().toString();
	}
	
	public JSONObject toJSON() {

		JSONObject jObject = new JSONObject();
		try {
			jObject.put(BOOTLOADER, getBootloader());
			jObject.put(BRAND, getBrand());
			jObject.put(DEVICE, getDevice());
			jObject.put(DISPLAY, getDisplay());
			jObject.put(MANF, getManufacture());
			jObject.put(MODEL, getModel());
			jObject.put(PRODUCT, getProduct());
			jObject.put(OSVERSION, getOsVersion());
			jObject.put(TIMEZONE, getTimezone());	

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jObject;
	}
	

}