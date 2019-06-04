package com.map.service.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.map.service.amap.api.SimulationDataApi;
import com.map.service.bean.BusInfo;
import com.map.service.bean.FavBus;
import com.map.service.bean.User;
import com.map.service.manager.DBManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class FavBusDialog extends Dialog {

    private boolean iscancelable;//控制点击dialog外部是否dismiss
    private boolean isBackCancelable;//控制返回键是否dismiss
    private View view;
    private Context context;

    private TextView mBusLineNameTv;
    private TextView mBusSpeedTv;
    private TextView mBusArrivedTimeTv;
    private TextView mBusCurStationTv;
    private String mBusStationName;
    private HorizontalListView2 horizon_listview;
    private BusSearchMgr mBusSearchMgr;
    private RealBusAdapter mRealBusAdapter;
    private SimulationDataApi mSimulationApi;
    private List<BusStationItem> itmes;

    public FavBusDialog(Context context, int layoutid, boolean isCancelable, boolean isBackCancelable) {
        super(context, R.style.MyDialog);
        mBusSearchMgr = new BusSearchMgr(context);
        mBusSearchMgr.setLineListener(new BusSearchMgr.BusLineSearchListener() {
            @Override
            public void onSuccess(List<BusLineItem> lineItems) {
                itmes = lineItems.get(0).getBusStations();
                mRealBusAdapter = new RealBusAdapter(getContext(),itmes);
                horizon_listview.setAdapter(mRealBusAdapter);
                mSimulationApi.setStationItems(itmes);

                mSimulationApi.startSimulation();
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
        mBusSpeedTv = (TextView) this.view.findViewById(R.id.bus_speed);
        mBusArrivedTimeTv = (TextView) this.view.findViewById(R.id.bus_arrived_time);
        mBusCurStationTv = (TextView) this.view.findViewById(R.id.bus_cur_station);
        horizon_listview = (HorizontalListView2) this.view.findViewById(R.id.horizon_listview);
        mSimulationApi = new SimulationDataApi();
        mSimulationApi.setListener(new SimulationDataApi.SimulationDataListener() {
            @Override
            public void onSimulation(BusInfo busInfo) {
                try{
                    Log.e("zzz",busInfo.toString());
                    mBusSpeedTv.setText("速度："+busInfo.getmSpeed()+"km/h");
                    mBusArrivedTimeTv.setText("到达时间："+busInfo.getmArrivedTime()+"min");
                    String des = "前方站点："+itmes.get(busInfo.getmBusIndex()).getBusStationName();
                    mBusCurStationTv.setText(des);
                    mRealBusAdapter.setmArrivedIndex(busInfo);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }


    public void setFavBus(FavBus favBus){
        this.mBusStationName = favBus.getBus_name();
        mBusLineNameTv.setText(mBusStationName);
        mBusSearchMgr.searchLine(favBus.getBus_line_id(),favBus.getCity_code());
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mSimulationApi.finishSimulation();
    }
}