package com.prempoint.objectman.prempointqualifier;

import java.io.Serializable;



/**
 * Created by objectman on 3/31/17.
 */

public class PremPTBLEScanResult implements Serializable {

	public PremPTBLEScanResult(String aDeviceName,Double aSigStrengthVal,byte[] aScanRecordID){
			this.setDeviceName(aDeviceName);
			this.setSignalStength(aSigStrengthVal);
			this.setScanRecordID(aScanRecordID);
	}
	public Double getSignalStength() {
		return signalStength_;
	}

	public void setSignalStength(Double signalStength) {
		this.signalStength_ = signalStength;
	}

	public byte[] getScanRecordID() {
		return scanRecordID_;
	}

	public void setScanRecordID(byte[] id) {
		this.scanRecordID_ = id;
	}

	public String getDeviceName() {
		return deviceName_;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName_ = deviceName;
	}

	public String getScanRecordIDAsHexString(){

		StringBuilder sb = new StringBuilder();
		for (byte b : this.getScanRecordID()) {
			sb.append(String.format("%02X ", b));
		}

		return sb.toString();
	}

	public boolean isValid(){
		/*
		: Not including signal strenth
		: because, signal strength could be 0
		*/
		return (this.getDeviceName() != null && this.getScanRecordID() != null);
	}
	private String deviceName_;
	private Double signalStength_;
	private	byte[] scanRecordID_;

}
