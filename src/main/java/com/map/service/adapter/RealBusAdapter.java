package com.map.service.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.busline.BusStationItem;
import com.map.service.R;
import com.map.service.amap.api.SimulationDataApi;
import com.map.service.bean.BusInfo;

import java.util.List;

public class RealBusAdapter extends BaseAdapter {
	private Context mContext;
	private List<BusStationItem> items;
	private SimulationDataApi mSimulationApi;
	public RealBusAdapter(Context context, List<BusStationItem> items) {
		mContext = context;
		this.items = items;
		mSimulationApi = new SimulationDataApi();
		mSimulationApi.setStationItems(items);
		mSimulationApi.setListener(new SimulationDataApi.SimulationDataListener() {
			@Override
			public void onSimulation(BusInfo busInfo) {
				Log.e("zzz",busInfo.toString());
			}
		});
		mSimulationApi.startSimulation();
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
			holder.station = (TextView) convertView.findViewById(R.id.real_bus_station);
//			holder.des = (TextView) convertView.findViewById(R.id.bus_station_des);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final BusStationItem item = items.get(position);
		holder.station.setText(item.getBusStationName());
//		holder.des.setText(item.toString());
		return convertView;
	}
	
	private class ViewHolder {
		TextView station;
		TextView des;
	}

}
