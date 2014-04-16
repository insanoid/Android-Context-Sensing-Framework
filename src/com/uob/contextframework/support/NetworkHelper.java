package com.uob.contextframework.support;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.uob.contextframework.ContextMonitor;
import com.uob.contextframework.baseclasses.NetworkConnectionStatus;

public class NetworkHelper {

	private static NetworkHelper instance = new NetworkHelper();
	static Context context;
	ConnectivityManager connectivityManager;
	boolean connected = false;

	public static NetworkHelper getInstance(Context ctx) {
		context = ctx;
		return instance;
	}

	public void updateConnectionInformation(Context con) {
		try {
			connectivityManager = (ConnectivityManager) con
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			int networkType = networkInfo.getType();

			connected = networkInfo != null && networkInfo.isAvailable() &&
					networkInfo.isConnected();

			NetworkConnectionStatus networkConn = new NetworkConnectionStatus();
			networkConn.isConnected = connected;
			networkConn.currentNetworkType = networkType;
			ContextMonitor.getInstance(con).setNetworkConnectionStatus(networkConn);

		} catch (Exception e) {
			NetworkConnectionStatus networkConn = new NetworkConnectionStatus();
			networkConn.isConnected = false;
			networkConn.currentNetworkType = -1;
			ContextMonitor.getInstance(con).setNetworkConnectionStatus(networkConn);
		}

	}

	public boolean isWiFiOn(Context con) {

		connectivityManager = (ConnectivityManager) con
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		return mWifi.isConnected();
	}
}
