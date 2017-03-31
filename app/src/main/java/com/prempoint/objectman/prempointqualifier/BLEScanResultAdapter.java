package com.prempoint.objectman.prempointqualifier;

import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by objectman on 3/31/17.
 */

public class BLEScanResultAdapter extends ArrayAdapter {


	@Override
	public long getItemId(int position) {
		return super.getItemId(position);
	}

	public BLEScanResultAdapter(List<PremPTBLEScanResult> data, Context context) {

		super(context, R.layout.scanresult_row_item, data);

		this.dataSet = data;
		this.mContext=context;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		PremPTBLEScanResult dataModel = (PremPTBLEScanResult) getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		ViewHolder viewHolder; // view lookup cache stored in tag

		final View result;

		if (convertView == null) {

			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.scanresult_row_item, parent, false);
			viewHolder.txtDeviceName = (TextView) convertView.findViewById(R.id.deviceName);
			viewHolder.txtRSSIValue = (TextView) convertView.findViewById(R.id.signalStrength);
			viewHolder.txtScanResultID = (TextView) convertView.findViewById(R.id.scanRecordID);


			result=convertView;

			convertView.setTag(viewHolder);
		}

		else {

			viewHolder = (ViewHolder) convertView.getTag();
			result=convertView;
		}
		//: Custom font stuff, just for fun
		Typeface typeface= Typeface.createFromAsset(getContext().getAssets(), "fonts/WORLDOFW.TTF");
		viewHolder.txtDeviceName.setTypeface(typeface);
		viewHolder.txtDeviceName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

		viewHolder.txtRSSIValue.setTypeface(typeface);
		viewHolder.txtRSSIValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

		viewHolder.txtScanResultID.setTypeface(typeface);
		viewHolder.txtScanResultID.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

		viewHolder.txtDeviceName.setText("DEVICE NAME: " + dataModel.getDeviceName());
		viewHolder.txtRSSIValue.setText("SIGNAL STRENGTH: " + dataModel.getSignalStength().toString());
		viewHolder.txtScanResultID.setText("SCAN RECORD ID: ( " + dataModel.getScanRecordIDAsHexString() + " )");

		return convertView;
	}


	@Nullable
	@Override
	public Object getItem(int position) {
		return dataSet.get(position);
	}

	private static class ViewHolder {

		TextView txtDeviceName;
		TextView txtRSSIValue;
		TextView txtScanResultID;

	}

	private List<PremPTBLEScanResult> dataSet;
	private Context mContext;
	private static final String TAG = "BLEScanResultAdapter";


}
