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

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.telephony.CellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;

import com.uob.contextframework.ContextMonitor;
import com.uob.contextframework.support.Constants;

/**
 * @author karthikeyaudupa
 * Information about singals and conneciton status.
 */
public class SignalInfo  extends PhoneStateListener {

	SignalInfoModel signalInfoModel;
	Context mContext;
	NetworkConnectionStatus connStatus;
	
	public SignalInfo(Context _ctx){
		mContext = _ctx;
		signalInfoModel = ContextMonitor.getInstance(_ctx).getSignalInfo();
		connStatus = ContextMonitor.getInstance(_ctx).getNetworkConnectionStatus();
	}

	


	@SuppressLint("NewApi")
	public void  onCellInfoChanged (List<CellInfo> cellInfo){
		super.onCellInfoChanged(cellInfo);
		ContextMonitor.getInstance(mContext).signalInfo.setNearByCells(cellInfo);
		
		
		Intent intent = new Intent(Constants.CONTEXT_CHANGE_NOTIFY);
		intent.putExtra(Constants.INTENT_TYPE, Constants.SIGNAL_NOTIFY);
		intent.putExtra(Constants.SIGNAL_NOTIFY,toString());
		mContext.sendBroadcast(intent);
		
	}

	@Override
	public void onSignalStrengthsChanged(SignalStrength signalStrength) {
		super.onSignalStrengthsChanged(signalStrength);
		int gsmSingalStrength  = signalStrength.getGsmSignalStrength();
		ContextMonitor.getInstance(mContext).signalInfo.setGsmSignalStrength(gsmSingalStrength);
		
		Intent intent = new Intent(Constants.CONTEXT_CHANGE_NOTIFY);
		intent.putExtra(Constants.INTENT_TYPE, Constants.SIGNAL_NOTIFY);
		intent.putExtra(Constants.SIGNAL_NOTIFY,toString());
		mContext.sendBroadcast(intent);

	}


	//TODO: Never gets called.
	@Override
	public void onDataConnectionStateChanged(int state) {
		super.onDataConnectionStateChanged(state);
		ContextMonitor.getInstance(mContext).signalInfo.setDataConnectionState(state);
		
		
		Intent intent = new Intent(Constants.CONTEXT_CHANGE_NOTIFY);
		intent.putExtra(Constants.INTENT_TYPE, Constants.SIGNAL_NOTIFY);
		intent.putExtra(Constants.SIGNAL_NOTIFY,toString());
		mContext.sendBroadcast(intent);
		
	}

	//Data conversion methods.
	@Override
	public String toString() {

		return toJSON().toString();
	}

	public JSONObject toJSON() {

		JSONObject jObject = new JSONObject();
		try {
			if(signalInfoModel==null){

				return jObject;
			}
			jObject.put(Constants.CONN_INFO, connStatus.toJSON());
			jObject.put(Constants.DATA_CONNECTION_STATE, String.valueOf(signalInfoModel.getDataConnectionState()));
			jObject.put(Constants.GSM_SIGNAL, String.valueOf((signalInfoModel.getGsmSignalStrength()>0)?signalInfoModel.getGsmSignalStrength():0));
			jObject.put(Constants.NEAR_BY_CELLS, String.valueOf(signalInfoModel.getNearByCells()));
		} catch (JSONException e) {
			
			e.printStackTrace();
		}

		return jObject;
	}


}
