package com.prempoint.objectman.prempointqualifier.services;

import android.app.Service;
import android.bluetooth.*;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.prempoint.objectman.prempointqualifier.model.PremPTBLEScanResult;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.makeText;
import static com.prempoint.objectman.prempointqualifier.R.string.PremPTBTNormalDataSend;
import static com.prempoint.objectman.prempointqualifier.R.string.PremPTBTScannerDataAvailableEvent;
import static com.prempoint.objectman.prempointqualifier.R.string.PremPTBTScannerDataKey;
import static com.prempoint.objectman.prempointqualifier.R.string.PremPTBTScannerMessageKey;
import static com.prempoint.objectman.prempointqualifier.R.string.PremPTBTScannerServiceEnd;
;

/**
 * Created by objectman on 3/30/17.
 */

public class PremPTBTScannerService extends Service {

	public PremPTBTScannerService() {
		super();
	}

	@Override
	public void onCreate() {
		super.onCreate();

		Log.d(TAG, "onCreate");

		mHandler = new Handler();
		final BluetoothManager bluetoothManager =
						(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
	}

	//: Sends this message locally to the main activity
	private void sendBluetoothDataUsing(String aStringMessage,PremPTBLEScanResult aScanResult) {

		Log.d(TAG, "sendBluetoothDataUsing(" + aStringMessage +  "," + aScanResult + ");");
		Intent intent = new Intent(getString(PremPTBTScannerDataAvailableEvent));
		//: If the scan result is null
		//: that means that this message
		//: is being sent to end the scan session
		if(aScanResult != null){
			Bundle resultBundle =  new Bundle();
			resultBundle.putSerializable(getString(PremPTBTScannerDataKey),aScanResult);
			intent.putExtra(getString(PremPTBTScannerDataKey),resultBundle);
		}
		intent.putExtra(getString(PremPTBTScannerMessageKey),aStringMessage);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}



	public int onStartCommand(Intent intent, int flags, int startId) {

		if(Build.VERSION.SDK_INT >= 21) {
			mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
			settings = new ScanSettings.Builder()
							.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
							.build();
			filters = new ArrayList<ScanFilter>();
			scanLeDevice(true);
		}
		return START_STICKY;
	}

	private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			Log.i("onConnectionStateChange", "Status: " + status);
			switch (newState) {
				case BluetoothProfile.STATE_CONNECTED:
					Log.i("gattCallback", "STATE_CONNECTED");
					gatt.discoverServices();
					break;
				case BluetoothProfile.STATE_DISCONNECTED:
					Log.e("gattCallback", "STATE_DISCONNECTED");
					break;
				default:
					Log.e("gattCallback", "STATE_OTHER");
			}

		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			List<BluetoothGattService> services = gatt.getServices();
			Log.i("onServicesDiscovered", services.toString());
			gatt.readCharacteristic(services.get(1).getCharacteristics().get
							(0));
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
																		 BluetoothGattCharacteristic
																						 characteristic, int status) {
			Log.i("onCharacteristicRead", characteristic.toString());
			gatt.disconnect();
		}
	};



	public void connectToDevice(BluetoothDevice device) {
		if (mGatt == null) {
			mGatt = device.connectGatt(this, false, gattCallback);
			scanLeDevice(false);// will stop after first device detection
		}
	}

	private BluetoothAdapter.LeScanCallback mLeScanCallback =
					new BluetoothAdapter.LeScanCallback() {
						@Override
						public void onLeScan(final BluetoothDevice device, int rssi,
																 byte[] scanRecord) {
							new Runnable() {
								@Override
								public void run() {
									Log.i("onLeScan", device.toString());
									connectToDevice(device);
								}
							};
						}
	};

	private void scanLeDevice(final boolean enable) {

		if (enable){
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (Build.VERSION.SDK_INT < 21) {
						mBluetoothAdapter.stopLeScan(mLeScanCallback);
					}
					else {
						Log.d(TAG,"Stopping scan !!");
						sendBluetoothDataUsing(getString(PremPTBTScannerServiceEnd),null);
						mLEScanner.stopScan(mScanCallback);

					}
				}
			}, SCAN_PERIOD);
			if (Build.VERSION.SDK_INT < 21) {
				mBluetoothAdapter.startLeScan(mLeScanCallback);
			}
			else {

				Log.d(TAG,"Starting mLE Scan !!");

				mLEScanner.startScan(filters, settings, mScanCallback);
			}
		}//: End of if(enabled)

		else {
			if (Build.VERSION.SDK_INT < 21) {
				mBluetoothAdapter.stopLeScan(mLeScanCallback);
			}
			else {
				mLEScanner.stopScan(mScanCallback);
			}
		}
	}

	private ScanCallback mScanCallback = new ScanCallback() {

		@Override
		public void onScanResult(int callbackType, ScanResult aScanResult) {

			BluetoothDevice btDevice = aScanResult.getDevice();
			PremPTBLEScanResult premPtScanRes = new PremPTBLEScanResult(btDevice.getName(),(float)aScanResult.getRssi(),aScanResult.getScanRecord().getBytes());


			Log.i("callbackType", String.valueOf(callbackType));
			Log.i("result", aScanResult.toString());
			Log.d(TAG,"signal level is " + aScanResult.getRssi());
			Log.d(TAG,"device name is " + btDevice.getName());
			Log.d(TAG,"device address is " + btDevice.getAddress());
			Log.d(TAG,"scan record is: " + aScanResult.getScanRecord().getBytes());

			sendBluetoothDataUsing(getString(PremPTBTNormalDataSend),premPtScanRes);
			connectToDevice(btDevice);
		}
		@Override
		public void onBatchScanResults(List<ScanResult> results) {
			for (ScanResult sr : results) {
				Log.i("ScanResult - Results", sr.toString());
			}
		}

		@Override
		public void onScanFailed(int errorCode) {
			Log.e("Scan Failed", "Error Code: " + errorCode);
		}
	};


	@Override
	public void onDestroy() {

		if(mGatt != null){
			mGatt.close();
			mGatt = null;
		}

		super.onDestroy();
	}

	private static final String TAG = "PremPTScannerService";

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}


	private BluetoothAdapter mBluetoothAdapter;
	private int REQUEST_ENABLE_BT = 1;
	private Handler mHandler;
	private static final long SCAN_PERIOD = (15 * 1000);
	private BluetoothLeScanner mLEScanner;
	private ScanSettings settings;
	private List<ScanFilter> filters;
	private BluetoothGatt mGatt;


}