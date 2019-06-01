package com.map.service.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusStationItem;
import com.map.service.R;

import java.util.ArrayList;
import java.util.List;

public class BusLineListAdapter extends BaseAdapter {
	private Context mContext;
	private List<BusLineItem> items;

	public BusLineListAdapter(Context context,List<BusLineItem> busLineItems) {
		mContext = context;
		this.items = busLineItems;
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_bus_line, null);
			holder.title = (TextView) convertView.findViewById(R.id.bus_line_title);
			holder.des = (TextView) convertView.findViewById(R.id.bus_line_des);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final BusLineItem item = items.get(position);
		holder.title.setText(item.getBusLineName());
		holder.des.setText(item.toString());
		
//		convertView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(mContext.getApplicationContext(),
//						BusRouteDetailActivity.class);
//				intent.putExtra("bus_path", item);
//				intent.putExtra("bus_result", mBusRouteResult);
//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				mContext.startActivity(intent);
				
//			}
//		});
		
		return convertView;
	}
	
	private class ViewHolder {
		TextView title;
		TextView des;
	}

}
