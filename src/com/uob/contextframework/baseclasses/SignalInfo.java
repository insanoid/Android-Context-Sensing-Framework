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

public class SignalInfo  extends PhoneStateListener {

	SignalInfoModel signalInfoModel;
	Context mContext;
	NetworkConnectionStatus connStatus;
	
	public SignalInfo(Context _ctx){
		mContext = _ctx;
		signalInfoModel = ContextMonitor.getInstance(_ctx).getSignalInfo();
		connStatus = ContextMonitor.getInstance(_ctx).getNetworkConnectionStatus();
	}

	@Override
	public String toString() {

		return toJSON().toString();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jObject;
	}


}
