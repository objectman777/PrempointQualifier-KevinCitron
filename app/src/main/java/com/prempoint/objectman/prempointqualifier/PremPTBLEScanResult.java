package com.prempoint.objectman.prempointqualifier;

import java.io.Serializable;



/**
 * Created by objectman on 3/31/17.
 */


public class PremPTBLEScanResult implements Serializable {

	public PremPTBLEScanResult(String aDeviceName,Float aSigStrengthVal,byte[] aScanRecordID){
			this.setDeviceName(aDeviceName);
			this.setSignalStength(aSigStrengthVal);
			this.setScanRecordID(aScanRecordID);
	}
	public Float getSignalStength() {
		return signalStength_;
	}

	public void setSignalStength(Float signalStength) {
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


		if(scanRecordID_ == null || scanRecordID_.length == 0){
			return BLE_SCAN_RECORD_UNAVAILABLE_STRING;
		}
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

	public String toString(){

			StringBuilder retVal = new StringBuilder();

			retVal.append(TOSTRING_RECORD_START);
			retVal.append(this.getDeviceName());
			retVal.append(TOSTRING_SEPARATOR);
			retVal.append(this.getSignalStength());
			retVal.append(TOSTRING_SEPARATOR);
			retVal.append(HEX_RECORD_ISOLATOR_OPENING);
			retVal.append(this.getScanRecordIDAsHexString());
			retVal.append(HEX_RECORD_ISOLATOR_CLOSING);
			retVal.append(TOSTRING_RECORD_END);

			return retVal.toString();

	}


	private String deviceName_;
	private Float signalStength_;
	private	byte[] scanRecordID_;
	private final static String TOSTRING_SEPARATOR=",";
	private final static String HEX_RECORD_ISOLATOR_OPENING="(";
	private final static String HEX_RECORD_ISOLATOR_CLOSING=")";
	private final static String BLE_SCAN_RECORD_UNAVAILABLE_STRING="BLE Scan record id, not avalable";
	private final static String TOSTRING_RECORD_START = "[";
	private final static String TOSTRING_RECORD_END = "]";

}
