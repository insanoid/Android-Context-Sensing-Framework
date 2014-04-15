package com.uob.contextframework.baseclasses;

import java.util.List;

import android.telephony.CellInfo;

public class SignalInfoModel {

	public SignalInfoModel() {
		super();
		gsmSignalStrength = 0;
		dataConnectionState = 0;
	}
	int dataConnectionState;
	List<CellInfo> nearByCells;
	int gsmSignalStrength;
	/**
	 * @return the dataConnectionState
	 */
	public int getDataConnectionState() {
		return dataConnectionState;
	}
	/**
	 * @param dataConnectionState the dataConnectionState to set
	 */
	public void setDataConnectionState(int dataConnectionState) {
		this.dataConnectionState = dataConnectionState;
	}
	/**
	 * @return the nearByCells
	 */
	public List<CellInfo> getNearByCells() {
		return nearByCells;
	}
	/**
	 * @param nearByCells the nearByCells to set
	 */
	public void setNearByCells(List<CellInfo> nearByCells) {
		this.nearByCells = nearByCells;
	}
	/**
	 * @return the gsmSignalStrength
	 */
	public int getGsmSignalStrength() {
		return gsmSignalStrength;
	}
	/**
	 * @param gsmSignalStrength the gsmSignalStrength to set
	 */
	public void setGsmSignalStrength(int gsmSignalStrength) {
		this.gsmSignalStrength = gsmSignalStrength;
	}
	
}
