package com.map.service.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusStationItem;
import com.map.service.R;
import com.map.service.adapter.BusLineListAdapter;
import com.map.service.bean.User;
import com.map.service.manager.LoginManager;

import java.util.List;


public class BusStationDetailDialog extends Dialog {

    private boolean iscancelable;//控制点击dialog外部是否dismiss
    private boolean isBackCancelable;//控制返回键是否dismiss
    private View view;
    private Context context;

    private TextView mBusStationNameTv;
    private String mBusStationName;
    private ListView mBusLineListView;
    private BusLineListAdapter mAdapter;

    private List<BusLineItem> busLineItems;

    public BusStationDetailDialog(Context context, int layoutid, boolean isCancelable, boolean isBackCancelable) {
        super(context, R.style.MyDialog);

        this.context = context;
        this.view = LayoutInflater.from(context).inflate(layoutid, null);
        this.iscancelable = isCancelable;
        this.isBackCancelable = isBackCancelable;

        initView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(view);//这行一定要写在前面
        setCancelable(iscancelable);//点击外部不可dismiss
        setCanceledOnTouchOutside(isBackCancelable);


    }

    public void initView() {
        mBusStationNameTv = (TextView) this.view.findViewById(R.id.bus_station_name);
        mBusLineListView = (ListView) this.view.findViewById(R.id.bus_line_listview);



    }


    public void setBusStation(BusStationItem stationItem){
        this.mBusStationName = stationItem.getBusStationName();
        this.busLineItems = stationItem.getBusLineItems();
        mAdapter = new BusLineListAdapter(getContext(),this.busLineItems);
        mBusLineListView.setAdapter(mAdapter);
        mBusStationNameTv.setText(mBusStationName);
    }
}