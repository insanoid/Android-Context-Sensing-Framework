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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.uob.contextframework.ContextMonitor;
import com.uob.contextframework.baseclasses.NetworkConnectionStatus;

/**
 * @author karthikeyaudupa
 * Singleton helper to process network information.
 */
public class NetworkHelper {

	private static NetworkHelper instance = new NetworkHelper();
	@SuppressWarnings("unused")
	private static Context context;
	private ConnectivityManager connectivityManager;
	boolean connected = false;

	/**
	 * Singlton instance fetcher.
	 * @param ctx Context.
	 * @return
	 */
	public static NetworkHelper getInstance(Context ctx) {
		context = ctx;
		return instance;
	}

	/**
	 * Updates the network information and stores in the @{ContextMonitor}
	 * @param con Context.
	 */
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

	/**
	 * Checks if the WiFi is connected.
	 * @param con
	 * @return
	 */
	public boolean isWiFiOn(Context con) {

		connectivityManager = (ConnectivityManager) con
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		return mWifi.isConnected();
	}

	/**
	 * Checks if the WiFi is turned on.
	 * @param con
	 * @return
	 */
	public boolean isWiFiTurnedOn(Context con) {

		WifiManager mWifi = (WifiManager) con.getSystemService(Context.WIFI_SERVICE);

		return mWifi.isWifiEnabled();
	}

}
