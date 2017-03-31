package com.prempoint.objectman.prempointqualifier;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.*;
import static com.prempoint.objectman.prempointqualifier.R.color.premscanbuttoncolor;
import static com.prempoint.objectman.prempointqualifier.R.drawable.premroundedbuttonskin;
import static com.prempoint.objectman.prempointqualifier.R.drawable.premscaninprogbtnskin;
import static com.prempoint.objectman.prempointqualifier.R.string.PremPTBTScannerDataKey;
import static com.prempoint.objectman.prempointqualifier.R.string.PremPTBTScannerMessageKey;
import static com.prempoint.objectman.prempointqualifier.R.string.scanButtonInitialStateText;
import static com.prempoint.objectman.prempointqualifier.R.string.scanButtonSelectedText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        scannerOutputList = (ListView) findViewById(R.id.scannerOutputList);
				scanResultArrayAdapter= new BLEScanResultAdapter(this.getTestDataModelCollection(),getApplicationContext());


        scannerOutputList.setAdapter(scanResultArrayAdapter);
    }


    private List<PremPTBLEScanResult> getTestDataModelCollection(){

				List<PremPTBLEScanResult> retVal = new ArrayList<PremPTBLEScanResult>();

			byte[] scanRecord = new byte[] {
							0x02, 0x01, 0x1a, // advertising flags
							0x05, 0x02, 0x0b, 0x11, 0x0a, 0x11, // 16 bit service uuids
							0x04, 0x09, 0x50, 0x65, 0x64, // name
							0x02, 0x0A, (byte) 0xec, // tx power level
							0x05, 0x16, 0x0b, 0x11, 0x50, 0x64, // service data
							0x05, (byte) 0xff, (byte) 0xe0, 0x00, 0x02, 0x15, // manufacturer specific data
							0x03, 0x50, 0x01, 0x02, // an unknown data type won't cause trouble
			};

			PremPTBLEScanResult scanRes = new PremPTBLEScanResult("LG G Vista",24.7f,scanRecord);

			retVal.add(scanRes);





				return retVal;
		}



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void scanButton_clickHandler(View anEventSource) {


        if(anEventSource == null ){

					Toast toast = makeText(getApplicationContext(),getString(R.string.null_pointer_fatal_error),
									LENGTH_LONG);

					toast.setGravity(Gravity.CENTER, 0, 0);

					toast.show();
					return;
        }

        if(!(anEventSource instanceof  Button)){


					Toast toast = makeText(getApplicationContext(),getString(R.string.illegal_argument_fatal_error),
									LENGTH_LONG);

					toast.setGravity(Gravity.CENTER, 0, 0);

					toast.show();

        }


        //: Now the real work begins
        //: Start a BLE scan

        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //: Comment this out, to test, that the sercice works as expected
        if(myBluetoothAdapter == null){

					Toast toast = makeText(getApplicationContext(),getString(R.string.no_bt_adapter_error_message),
                    LENGTH_LONG);

					toast.setGravity(Gravity.CENTER, 0, 0);
					//: toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
					toast.show();
            return;

        } //End of conditional which check if a BT Adapter is available.

        if(!myBluetoothAdapter.isEnabled()){

					Toast toast = makeText(getApplicationContext(),getString(R.string.bt_not_turnedon_error_message),
									LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					return;
				} //: End of BT enabled conditional

				if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {

					Toast toast = makeText(getApplicationContext(),getString(R.string.ble_unsupported_error_message),
									LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					return;
				} //: End of BLE Feature Conditional
				scanResultArrayAdapter.clear();
				Button buttonView  = (Button) anEventSource ;
				buttonView.setEnabled(false);
				buttonView.setText(scanButtonSelectedText);
				buttonView.setTextColor(getApplication().getResources().getColor(R.color.premScanButtonSelectedColor));
            buttonView.setBackground(getResources().getDrawable(premscaninprogbtnskin,null));
            //: Create the intent to statt the BlueToothService
            startService(new Intent(MainActivity.this,PremPTBTScannerService.class));
				Log.i(TAG,"Started BLE Scanner Service");




    }

    private void resetControlsToInitialState() {

        //: The only control to reset is the scan button
        Button scanButton = (Button) findViewById(R.id.scanButton);


        if (scanButton == null) {
            Log.i(TAG, "scanButton not found !!");
            return;
        }

        scanButton.setEnabled(true);
        scanButton.setText(scanButtonInitialStateText);
        scanButton.setTextColor(getApplication().getResources().getColor(premscanbuttoncolor));
        scanButton.setBackground(getResources().getDrawable(premroundedbuttonskin,null));

    }


    @Override
    protected void onPause() {
        // Unregister since the activity is paused.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                scannerEventReceiver);
        super.onPause();
    }

    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver scannerEventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Get extra data included in the Intent
            String strMsg =  intent.getStringExtra(getString(PremPTBTScannerMessageKey));

            Log.i(TAG, "Got message: " + strMsg);
					//: Checks for end of service
            if(strMsg.equalsIgnoreCase(getResources().getString(R.string.PremPTBTScannerServiceEnd))){
                Log.i(TAG,"resetting controls to initial state");
								if(scanResultArrayAdapter.isEmpty()){
										//: The BLE scan must have turned up nothing.
										//: Let notify the user of such
									Toast toast = makeText(getApplicationContext(),getString(R.string.ble_scan_no_items_found),
													LENGTH_LONG);
									toast.setGravity(Gravity.CENTER, 0, 0);
									toast.show();
								}
                resetControlsToInitialState();
            		return;
            }
            //: If we reach here, there must be data present
						Bundle dataBundle  = intent.getBundleExtra(getString(PremPTBTScannerDataKey));
						if(dataBundle == null){
							Log.d(TAG,"dataBundle is null");
							return;
						}


						PremPTBLEScanResult scanRes = (PremPTBLEScanResult) dataBundle.getSerializable(getString(PremPTBTScannerDataKey));
						//: Add the scan result to the list adapter
						if(scanRes == null){
							//: Something went wrong
							Log.d(TAG,"scanRes is null");
							return;
						}
						//: Finally after all that
						//: check is the relevant values are present
						//: add the BLE scan result to the list view
						//: The array adapter concept, is basically the
						//: same as the Flex/Actionscript dataProvider concept
						if(scanRes.isValid())
							scanResultArrayAdapter.add(scanRes);

        }
    };

    @Override
    protected void onResume() {
        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-event-name".
        LocalBroadcastManager.getInstance(this).registerReceiver(
                scannerEventReceiver,
                new IntentFilter(getString(R.string.PremPTBTScannerDataAvailableEvent)));
        super.onResume();
    }

    private BluetoothAdapter myBluetoothAdapter;
    private ListView scannerOutputList;
    private ArrayAdapter<PremPTBLEScanResult> scanResultArrayAdapter;
    private static final String TAG = "MainActivity";

}
