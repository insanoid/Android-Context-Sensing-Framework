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

import android.telephony.CellInfo;
import android.telephony.CellLocation;

/**
 * @author karthikeyaudupa
 * Model to store tower signal information.
 */
public class SignalInfoModel {

	public SignalInfoModel() {
		super();
		gsmSignalStrength = 0;
		dataConnectionState = 0;
	}
	int dataConnectionState;
	List<CellInfo> nearByCells;
	int gsmSignalStrength;
	CellLocation cellLocation;
	
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
	/**
	 * @return the cellLocation
	 */
	public CellLocation getCellLocation() {
		return cellLocation;
	}
	/**
	 * @param cellLocation the cellLocation to set
	 */
	public void setCellLocation(CellLocation cellLocation) {
		this.cellLocation = cellLocation;
	}
	
}
