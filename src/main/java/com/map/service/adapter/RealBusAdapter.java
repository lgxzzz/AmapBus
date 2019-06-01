package com.map.service.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.busline.BusStationItem;
import com.map.service.R;

import java.util.ArrayList;
import java.util.List;

public class RealBusAdapter extends BaseAdapter {
	private Context mContext;
	private List<BusStationItem> items;

	public RealBusAdapter(Context context, List<BusStationItem> items) {
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
			convertView = View.inflate(mContext, R.layout.item_real_bus, null);
//			holder.title = (TextView) convertView.findViewById(R.id.bus_station_title);
//			holder.des = (TextView) convertView.findViewById(R.id.bus_station_des);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final BusStationItem item = items.get(position);
//		holder.title.setText(item.getBusStationName());
//		holder.des.setText(item.toString());
		return convertView;
	}
	
	private class ViewHolder {
		TextView title;
		TextView des;
	}

}
