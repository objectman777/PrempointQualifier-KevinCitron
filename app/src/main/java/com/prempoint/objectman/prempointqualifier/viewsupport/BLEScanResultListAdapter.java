package com.prempoint.objectman.prempointqualifier.viewsupport;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.prempoint.objectman.prempointqualifier.R;
import com.prempoint.objectman.prempointqualifier.model.PremPTBLEScanResult;

import java.util.List;

/**
 * Created by objectman on 3/31/17.
 * This concept, is, exactly the same
 * as the ActionScript list item renderer.
 * So. This is a piece of cake
 */

public class BLEScanResultListAdapter extends ArrayAdapter {


	@Override
	public long getItemId(int position) {
		return super.getItemId(position);
	}

	public BLEScanResultListAdapter(List<PremPTBLEScanResult> data, Context context) {

		super(context, R.layout.scanresult_row_item, data);

		this.dataSet = data;

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
			//: Get the view container from the cache
			viewHolder = (ViewHolder) convertView.getTag();
			result=convertView;
		}
		//: Custom font stuff, just for fun
		//: make this an instance var.
		//: not need to keep creating a typeface


		viewHolder.txtDeviceName.setTypeface(this.getDefaultTypeface());
		viewHolder.txtDeviceName.setTextSize(TypedValue.COMPLEX_UNIT_SP,LIST_ITEM_DEVICENAME_FONT_SIZE);

		viewHolder.txtRSSIValue.setTypeface(this.getDefaultTypeface());
		viewHolder.txtRSSIValue.setTextSize(TypedValue.COMPLEX_UNIT_SP,LIST_ITEM_SIGNAL_STRENGTH_FONT_SIZE);

		viewHolder.txtScanResultID.setTypeface(this.getDefaultTypeface());
		viewHolder.txtScanResultID.setTextSize(TypedValue.COMPLEX_UNIT_SP,LIST_ITEM_SCAN_RECORD_ID_FONT_SIZE);


		//: Lets use a StrigBuilder to prevent exceess memory use
		//: Lots of catenation happening, creates multiple
		//: StringBuilders/StringBuffer instances behind the scenes
		StringBuilder listItemStr = new StringBuilder();
		//: A lot of the string handler stuff
		//: could be refactored into their own respective meothods
		//: ie getDeviceHeadingStrUsing(context,dataModel)....etc
		//: But. For the sake of breveity. I'm not going to
		listItemStr.append(getContext().getString(R.string.LIST_ITEM_DEVICE_NAME_HEADING));
		listItemStr.append(" "); //: Add a space, to make it more legible to view
		listItemStr.append(dataModel.getDeviceName());

		viewHolder.txtDeviceName.setText(listItemStr.toString());

		listItemStr.setLength(0);//: Reuse the listItemStr;

		listItemStr.append(getContext().getString(R.string.LIST_ITEM_DEVICE_SIGNAL_STRENGTH_HEADING));
		listItemStr.append(" "); //: Add a space, to make it more legible to view
		listItemStr.append(dataModel.getSignalStength().toString());
		viewHolder.txtRSSIValue.setText(listItemStr.toString());

		listItemStr.setLength(0); //: Reuse the listItemStr;

		listItemStr.append(getContext().getString(R.string.LIST_ITEM_DEVICE_RECORDID_HEADING));
		listItemStr.append(" "); //: Add a space, to make it more legible to view
		listItemStr.append(PremPTBLEScanResult.HEX_RECORD_ISOLATOR_OPENING);
		listItemStr.append(" "); //: Add a space, to make it more legible to view
		listItemStr.append(dataModel.getScanRecordIDAsHexString());
		listItemStr.append(PremPTBLEScanResult.HEX_RECORD_ISOLATOR_CLOSING);

		viewHolder.txtScanResultID.setText(listItemStr.toString());

		//: Cleanup after usage
		listItemStr.setLength(0);
		listItemStr = null;



		return convertView;
	}

	private Typeface getDefaultTypeface(){

		if(defaultCustomTypeface != null)
				return defaultCustomTypeface;

			//: There should be no failure here. This font is embedded.
			defaultCustomTypeface = Typeface.createFromAsset(getContext().getAssets(),getContext().getString(R.string.DEFAULT_LIST_ITEM_FONT1));

			return defaultCustomTypeface;
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
	private Typeface defaultCustomTypeface;
	private static final String TAG = "BLEScanResultListAdapter";
	private static final int LIST_ITEM_DEVICENAME_FONT_SIZE = 20;
	private static final int LIST_ITEM_SIGNAL_STRENGTH_FONT_SIZE = 20;
	private static final int LIST_ITEM_SCAN_RECORD_ID_FONT_SIZE = 17;



}
