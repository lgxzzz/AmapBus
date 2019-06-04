package com.map.service.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.busline.BusStationItem;
import com.map.service.R;
import com.map.service.amap.api.SimulationDataApi;
import com.map.service.bean.BusInfo;

import java.util.List;

public class RealBusAdapter extends BaseAdapter {
	private Context mContext;
	private List<BusStationItem> items;
	private BusInfo mBusinfo;
	public RealBusAdapter(Context context, List<BusStationItem> items) {
		mContext = context;
		this.items = items;

	}

	public void setmArrivedIndex(BusInfo mBusinfo){
		this.mBusinfo = mBusinfo;
		notifyDataSetChanged();
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
			holder.mBusPic = (ImageView) convertView.findViewById(R.id.real_bus_pic);
			holder.mBusPoint = (ImageView) convertView.findViewById(R.id.real_bus_point);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final BusStationItem item = items.get(position);
		holder.station.setText(item.getBusStationName());
		if (mBusinfo!=null&&mBusinfo.getmBusIndex()==position)
		{
            holder.station.setTextColor(Color.RED);
			holder.mBusPoint.setVisibility(View.VISIBLE);
            if (mBusinfo.getmStatus().equals(BusInfo.BusStatus.ARRIVED)){
				holder.mBusPic.setVisibility(View.GONE);
			}else{
				holder.mBusPic.setVisibility(View.VISIBLE);
			}

		}else{
            holder.station.setTextColor(Color.BLACK);
            holder.mBusPic.setVisibility(View.GONE);
            holder.mBusPoint.setVisibility(View.GONE);
        }
		return convertView;
	}
	
	private class ViewHolder {
		TextView station;
		ImageView mBusPic;
		ImageView mBusPoint;
	}

}
