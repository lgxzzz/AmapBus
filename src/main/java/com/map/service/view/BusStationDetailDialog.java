package com.map.service.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusStationItem;
import com.map.service.R;
import com.map.service.adapter.BusLineListAdapter;
import com.map.service.adapter.RealBusAdapter;
import com.map.service.amap.api.BusSearchMgr;

import java.util.List;


public class BusStationDetailDialog extends Dialog {

    private boolean iscancelable;//控制点击dialog外部是否dismiss
    private boolean isBackCancelable;//控制返回键是否dismiss
    private View view;
    private Context context;

    private TextView mBusStationNameTv;
    private TextView mBusLineNameTv;
    private String mBusStationName;
    private ListView mBusLineListView;
    private BusLineListAdapter mAdapter;

    private List<BusLineItem> busLineItems;

    private LinearLayout mBusStationLayout;
    private LinearLayout mBusLineDetailLayout;
    private Button mBackBtn;
    private Button mFaveBtn;

    private RealTimeBusView mRealTimeBusView;
    private String cityCode;
    private HorizontalListView2 horizon_listview;
    private BusSearchMgr mBusSearchMgr;
    private RealBusAdapter mRealBusAdapter;

    public BusStationDetailDialog(Context context, int layoutid, boolean isCancelable, boolean isBackCancelable) {
        super(context, R.style.MyDialog);
        mBusSearchMgr = new BusSearchMgr(context);
        mBusSearchMgr.setLineListener(new BusSearchMgr.BusLineSearchListener() {
            @Override
            public void onSuccess(List<BusLineItem> lineItems) {
                List<BusStationItem> itmes = lineItems.get(0).getBusStations();
                mRealBusAdapter = new RealBusAdapter(getContext(),itmes);
                horizon_listview.setAdapter(mRealBusAdapter);
            }

            @Override
            public void onFail(String error) {

            }
        });
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
        mBusLineNameTv = (TextView) this.view.findViewById(R.id.bus_line_name);
        mBusLineListView = (ListView) this.view.findViewById(R.id.bus_line_listview);

        mBusStationLayout = (LinearLayout)this.view.findViewById(R.id.bus_station_layout);
        mBusLineDetailLayout = (LinearLayout)this.view.findViewById(R.id.bus_line_layout);

        mBackBtn = (Button)this.view.findViewById(R.id.bus_line_detail_back);
        mFaveBtn = (Button)this.view.findViewById(R.id.bus_line_detail_fav);

        mRealTimeBusView = (RealTimeBusView) this.view.findViewById(R.id.real_bus_view);

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBusLineDetailLayout.setVisibility(View.GONE);
                mBusStationLayout.setVisibility(View.VISIBLE);
            }
        });

        mFaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        horizon_listview = (HorizontalListView2) this.view.findViewById(R.id.horizon_listview);
    }


    public void setBusStation(BusStationItem stationItem, final String cityCode){
        this.mBusStationName = stationItem.getBusStationName();
        this.busLineItems = stationItem.getBusLineItems();
        mAdapter = new BusLineListAdapter(getContext(),this.busLineItems);
        mBusLineListView.setAdapter(mAdapter);
        mBusLineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BusLineItem busLineItem = (BusLineItem)adapterView.getAdapter().getItem(i);
                mBusLineDetailLayout.setVisibility(View.VISIBLE);
                mBusStationLayout.setVisibility(View.GONE);
//                mRealTimeBusView.setBusLineItem(busLineItem,cityCode);
                int index = busLineItem.getBusLineName().lastIndexOf("(");
                mBusLineNameTv.setText(busLineItem.getBusLineName().substring(0,index));
                mBusSearchMgr.searchLine(busLineItem.getBusLineId(),cityCode);
            }
        });

        mBusStationNameTv.setText(mBusStationName);
    }
}