package com.map.service.amap.api;

import android.os.Handler;
import android.os.Message;

import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.map.service.bean.BusInfo;

import java.util.List;
import java.util.Random;

/**
 * 模拟构造车辆实时位置信息和到站提醒
 *
 * */
public class SimulationDataApi extends Handler {


    List<BusStationItem> stationItems;
    private SimulationDataListener listener;

    private int mBusIndex;//当前到站
    private int mArrivedCount =1;//刷新两次后到站
    private BusInfo mBusInfo;

    public SimulationDataApi(){

    }

    //开始模拟
    public void startSimulation(){
        setDefaultValue();
        refreshUI(1000);
    }

    //结束模拟
    public void finishSimulation(){
        removeCallbacksAndMessages(null);
    }

    //刷新数据
    public void refreshUI(long delay){
        removeMessages(HANDLER_MSG_REFRESH_UI);
        sendEmptyMessageDelayed(HANDLER_MSG_REFRESH_UI,delay);
    }

    //随机选取一个公交站
    public void setDefaultValue(){
        int length = stationItems.size();
        mBusIndex = new Random().nextInt(length);
        mBusInfo = new BusInfo();
        mBusInfo.setmBusIndex(mBusIndex);
        mBusInfo.setmStatus( BusInfo.BusStatus.NOTARRIVE);
        mBusInfo.setmSpeed(new Random().nextInt(20));
        mBusInfo.setmArrivedTime(new Random().nextInt(15));
    }

    //改变状态
    public void changeStatus(){
        if (mArrivedCount>=0){
            mArrivedCount--;
            if (mArrivedCount == 0)
            {
                mBusInfo.setmStatus( BusInfo.BusStatus.ARRIVED);
                mBusInfo.setmSpeed(new Random().nextInt(20));
                mBusInfo.setmArrivedTime(mBusInfo.getmArrivedTime()/2);
            }else{
                mBusInfo.setmStatus( BusInfo.BusStatus.NOTARRIVE);
                mBusInfo.setmSpeed(new Random().nextInt(20));
                mBusInfo.setmArrivedTime(mBusInfo.getmArrivedTime()/2);
            }
        }else{
            mArrivedCount = 2;
            goToNextStation();
        }
    }

    public void goToNextStation(){
        if(mBusIndex != stationItems.size()){
            mBusIndex++;
        }else{
            mBusIndex = 0;
        }
        mBusInfo.setmBusIndex(mBusIndex);
    }

    public void setListener(SimulationDataListener listener){
       this.listener = listener;
    }

    public void setStationItems(List<BusStationItem> stationItems){
        this.stationItems = stationItems;
    }


    public interface SimulationDataListener{
        public void onSimulation(BusInfo busInfo);
    }

    private static final int HANDLER_MSG_REFRESH_UI = 0x01;

    @Override
    public void handleMessage(Message msg) {
        switch(msg.what){
            case HANDLER_MSG_REFRESH_UI:
            listener.onSimulation(mBusInfo);
            changeStatus();
            refreshUI(100);
            break;
            default:
            break;
        }
    }
}
