package com.map.service.amap.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusLineQuery;
import com.amap.api.services.busline.BusLineResult;
import com.amap.api.services.busline.BusLineSearch;
import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.busline.BusStationQuery;
import com.amap.api.services.busline.BusStationResult;
import com.amap.api.services.busline.BusStationSearch;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.map.service.overlay.BusLineOverlay;

import java.util.ArrayList;
import java.util.List;

public class BusSearchMgr implements BusStationSearch.OnBusStationSearchListener ,BusLineSearch.OnBusLineSearchListener {
    private Context mContext;
    private String cityCode = "";// 城市区号
    private BusStationQuery busStationQuery;// 公交站点查询
    private BusStationSearch busStationSearch;// 公交线路列表查询

    private BusLineResult busLineResult;// 公交线路搜索返回的结果
    private List<BusLineItem> lineItems = null;// 公交线路搜索返回的busline
    private BusLineQuery busLineQuery;// 公交线路查询的查询类

    private BusLineSearch busLineSearch;// 公交线路列表查询

    private BusLineSearchListener mLineListener;
    private BusStationSearchListener mStationListener;

    public BusSearchMgr(Context context){
        this.mContext = context;
    }

    public void setStationListener(BusStationSearchListener mStationListener){
        this.mStationListener = mStationListener;
    }

    public void setLineListener(BusLineSearchListener mLineListener){
        this.mLineListener = mLineListener;
    }

    /**
     * 公交线路搜索
     */
    public void searchLine(String search,String cityCode) {
        if ("".equals(search)) {
            mLineListener.onFail("搜索失败");
        }
        busLineQuery = new BusLineQuery(search, BusLineQuery.SearchType.BY_LINE_ID,
                cityCode);// 第一个参数表示公交线路名，第二个参数表示公交线路查询，第三个参数表示所在城市名或者城市区号
        busLineQuery.setPageSize(10);// 设置每页返回多少条数据
        busLineQuery.setPageNumber(0);// 设置查询第几页，第一页从0开始算起
        busLineSearch = new BusLineSearch(mContext, busLineQuery);// 设置条件
        busLineSearch.setOnBusLineSearchListener(this);// 设置查询结果的监听
        busLineSearch.searchBusLineAsyn();// 异步查询公交线路名称
    }



    /**
     * 公交站点搜索
     * */
    public void searchStation(String search,String cityCode){
          busStationQuery = new BusStationQuery(search,cityCode);
          busStationQuery.setPageSize(10);
          busStationQuery.setPageNumber(0);
          busStationSearch = new BusStationSearch(mContext,busStationQuery);
		  busStationSearch.setOnBusStationSearchListener(this);
		  busStationSearch.searchBusStationAsyn();
    }

    @Override
    public void onBusLineSearched(BusLineResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null
                    && result.getQuery().equals(busLineQuery)) {
                if (result.getPageCount() > 0
                        && result.getBusLines() != null
                        && result.getBusLines().size() > 0) {
                    busLineResult = result;
                    lineItems = result.getBusLines();
                    if(lineItems != null) {
                        mLineListener.onSuccess(lineItems);
                    }
                }
            } else {
                mLineListener.onFail("搜索失败");
            }
        } else {
            mLineListener.onFail("搜索失败");
        }
    }


    @Override
    public void onBusStationSearched(BusStationResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPageCount() > 0
                    && result.getBusStations() != null
                    && result.getBusStations().size() > 0) {
                ArrayList<BusStationItem> item = (ArrayList<BusStationItem>) result
                        .getBusStations();
                StringBuffer buf = new StringBuffer();
                for (int i = 0; i < item.size(); i++) {
                    BusStationItem stationItem = item.get(i);


                    buf.append(" station: ").append(i).append(" name: ")
                            .append(stationItem.getBusStationName());
                    Log.d("LG", "stationName:"
                            + stationItem.getBusStationName() + "stationpos:"
                            + stationItem.getLatLonPoint().toString());
                }
                String text = buf.toString();
                mStationListener.onSuccess(item);
            } else {
                mStationListener.onFail("没有搜索到公交站");
            }
        } else  {
            mStationListener.onFail("没有搜索到公交站");
        }
    }

    public interface BusLineSearchListener{
        public void onSuccess(List<BusLineItem> lineItems);
        public void onFail(String error);
    }

    public interface BusStationSearchListener{
        public void onSuccess( ArrayList<BusStationItem> items);
        public void onFail(String error);
    }

}
