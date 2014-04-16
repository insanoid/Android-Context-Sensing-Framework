package com.uob.contextframework.support;

public class Constants {

		
		public static long MAXIMUM_ACCEPTABLE_TIME = 40*1000L;
		public static long MAXIMUM_ACCEPTABLE_DISTANCE = 0;
		
		
		public static long SHORT_POLLING_INTERVAL =  15*1000L; 
		public static final long MINUTE_POLLING_INTERVAL = 3*60*1000L;
		
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


		public static String ACCESSPOINT_COUNT = "ACCESSPOINT_COUNT";
		public static String ACCESSPOINT = "ACCESSPOINT";

		public static String CONNTECTION_TYPE = "CONN_TYPE";
		public static String NETWORK_STATUS = "NET_CONNECTED";
		public static String IS_WIFI_ON = "IS_WIFI_ON"; 
		
		
		// Intent Tags
		
		public static String CONTEXT_CHANGE_NOTIFY = "CONTEXT_CHANGE_NOTIFY";
		public static String INTENT_TYPE = "INTENT_TYPE";
		
		public static String LOC_NOTIFY = "LOC_UPDATE_NOTIFY";
		public static String BATTERY_NOTIFY = "BATTERY_UPDATE_NOTIFY";
		public static String SIGNAL_NOTIFY = "SIGNAL_UPDATE_NOTIFY";
		public static String WIFI_NOTIFY = "WIFI_UPDATE_NOTIFY";
}