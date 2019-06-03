package com.map.service.bean;

/**
 * Created by lgx on 2019/6/3.
 */

public class BusInfo {
    public enum BusStatus {
        NOTARRIVE,// 未到达
        ARRIVED   // 到达
    };
    private int mBusIndex;
    private BusStatus mStatus;//公交状态
    private int mSpeed;//当前速度
    private int mArrivedTime;//到站时间

    public int getmBusIndex() {
        return mBusIndex;
    }

    public void setmBusIndex(int mBusIndex) {
        this.mBusIndex = mBusIndex;
    }

    public BusStatus getmStatus() {
        return mStatus;
    }

    public void setmStatus(BusStatus mStatus) {
        this.mStatus = mStatus;
    }

    public int getmSpeed() {
        return mSpeed;
    }

    public void setmSpeed(int mSpeed) {
        this.mSpeed = mSpeed;
    }

    public int getmArrivedTime() {
        return mArrivedTime;
    }

    public void setmArrivedTime(int mArrivedTime) {
        this.mArrivedTime = mArrivedTime;
    }

    public String toString(){
        return mBusIndex +" "+mStatus+" "+mSpeed+" "+mArrivedTime;
    }
}
