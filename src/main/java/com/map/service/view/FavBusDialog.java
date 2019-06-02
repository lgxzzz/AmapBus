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
import android.widget.Toast;

import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusStationItem;
import com.map.service.R;
import com.map.service.adapter.BusLineListAdapter;
import com.map.service.adapter.RealBusAdapter;
import com.map.service.amap.api.BusSearchMgr;
import com.map.service.bean.FavBus;
import com.map.service.bean.User;
import com.map.service.manager.DBManager;

import java.util.List;


public class FavBusDialog extends Dialog {

    private boolean iscancelable;//控制点击dialog外部是否dismiss
    private boolean isBackCancelable;//控制返回键是否dismiss
    private View view;
    private Context context;

    private TextView mBusLineNameTv;
    private String mBusStationName;
    private HorizontalListView2 horizon_listview;
    private BusSearchMgr mBusSearchMgr;
    private RealBusAdapter mRealBusAdapter;
    private BusLineItem mSelectBusLineItem;

    public FavBusDialog(Context context, int layoutid, boolean isCancelable, boolean isBackCancelable) {
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
        mBusLineNameTv = (TextView) this.view.findViewById(R.id.bus_line_name);
        horizon_listview = (HorizontalListView2) this.view.findViewById(R.id.horizon_listview);
    }


    public void setFavBus(FavBus favBus){
        this.mBusStationName = favBus.getBus_name();
        mBusLineNameTv.setText(mBusStationName);
        mBusSearchMgr.searchLine(favBus.getBus_line_id(),favBus.getCity_code());
    }

}