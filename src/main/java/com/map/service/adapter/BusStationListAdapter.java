package com.map.service.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.map.service.BusRouteDetailActivity;
import com.map.service.R;
import com.map.service.util.AMapUtil;

import java.util.ArrayList;
import java.util.List;

public class BusStationListAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<BusStationItem> items;

	public BusStationListAdapter(Context context, ArrayList<BusStationItem> items) {
		mContext = context;
		this.items = items;
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
			convertView = View.inflate(mContext, R.layout.item_bus_station, null);
			holder.title = (TextView) convertView.findViewById(R.id.bus_station_title);
			holder.des = (TextView) convertView.findViewById(R.id.bus_station_des);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final BusStationItem item = items.get(position);
		holder.title.setText(item.getBusStationName());
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
