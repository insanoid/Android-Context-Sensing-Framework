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

package com.uob.contextframework.support;

/**
 * @author karthikeyaudupa
 * Stores constant used by the framework
 * Also respnsbile for storing timing of polling.
 */
public class Constants {

		
		public static long MAXIMUM_ACCEPTABLE_TIME = 40*1000L;
		public static long MAXIMUM_ACCEPTABLE_DISTANCE = 0;
		
		
		public static long SHORT_POLLING_INTERVAL =  15*60*1000L; 
		public static final long MINUTE_POLLING_INTERVAL = 3*60*1000L;
		public static final long LONG_POLLING_INTERVAL = 30*60*1000L;
		public static final long GRANULAR_POLLING_INTERVAL = 10*1000L;
		
		
		public static String TAG = "CONTEXT_FRAMEWORK";
		
		
		//JSON Element tags
		
		public static String LAT = "LAT";
		public static String LNG = "LNG";
		public static String ALT = "ALT";
		
		public static String IS_CHARGING = "IS_CHARGING";
		public static String CHARGING_SRC = "CHRG_SRC";
		public static String BATTERY_PCTG = "BATTERY_PERCENT";

		public static String CONN_INFO = "CONN_INFO";
		public static String DATA_CONNECTION_STATE = "CONNECTION_STATE";
		public static String GSM_SIGNAL = "GSM_SIGNAL_STRENGTH";
		public static String NEAR_BY_CELLS = "CELl_TOWERS_NEARBY";
		public static String CELL_LOCATION = "CELL_LOCATION";

		public static String ACCESSPOINT_COUNT = "ACCESSPOINT_COUNT";
		public static String ACCESSPOINT = "ACCESSPOINT";

		public static String CONNTECTION_TYPE = "CONN_TYPE";
		public static String NETWORK_STATUS = "NET_CONNECTED";
		public static String IS_WIFI_ON = "IS_WIFI_ON"; 
		public static String IS_WIFI_AVAILABLE = "IS_WIFI_AVAIL";
		
		public static String DEVICE_NAME = "DEVICE_NAME";
		public static String DEVICE_ADDRESS = "DEVICE_ADDRESS";
		public static String DEVICE_PAIRED = "IS_PAIRED";
		public static String IS_BLUETOOH_ON = "BLUETOOTH_ON";
		public static String BLUETOOH_DEVICES = "BLUETOOTH_DEVICE";
		

		public static String RING_VOL = "RING_VOLUME";
		public static String NORM_VOL = "NORMAL_VOLUME";
		public static String SCREEN_BRIGHTNESS = "SCREEN_BRIGHTNESS";
		public static String RINGER_MODE = "RINGER_MODE";
		
		// Intent Tags
		
		public static String CONTEXT_CHANGE_NOTIFY = "CONTEXT_CHANGE_NOTIFY";
		public static String INTENT_TYPE = "INTENT_TYPE";
		
		public static String LOC_NOTIFY = "LOC_UPDATE_NOTIFY";
		public static String BATTERY_NOTIFY = "BATTERY_UPDATE_NOTIFY";
		public static String SIGNAL_NOTIFY = "SIGNAL_UPDATE_NOTIFY";
		public static String WIFI_NOTIFY = "WIFI_UPDATE_NOTIFY";
		public static String EVENT_NOTIFY = "EVENT_UPDATE_NOTIFY";
		public static String BLUETOOTH_NOTIFY = "BLUETOOTH_UPDATE_NOTIFY";
		public static String PHONE_PROFILE_NOTIFY = "PHONE_PROFILE_NOTIFY";
}