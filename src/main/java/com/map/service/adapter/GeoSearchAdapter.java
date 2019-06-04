package com.map.service.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.map.service.R;
import com.map.service.bean.GeoSearchEntity;
import com.map.service.bean.PoiSearchEntity;

import java.util.ArrayList;
import java.util.List;

public class GeoSearchAdapter extends BaseAdapter {
    private Context mContext;
    private List<GeoSearchEntity> mData = new ArrayList<>();

    public GeoSearchAdapter(Context mContext, List<GeocodeAddress> geoaddress){
        this.mContext = mContext;
        for (int i =0;i<geoaddress.size();i++){
            GeocodeAddress address = geoaddress.get(i);
            GeoSearchEntity geoSearchEntity = new GeoSearchEntity();
            geoSearchEntity.setGeoItem(address);
            this.mData.add(geoSearchEntity);
        }
    }

    @Override
    public int getCount() {
        return this.mData.size();
    }

    @Override
    public Object getItem(int i) {
        return this.mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        GeoSearchEntity data = this.mData.get(i);
        ViewHoler holer = null;
        if (view == null){
            holer = new ViewHoler();
            view = LayoutInflater.from(mContext).inflate(R.layout.poi_list_item,null);
            holer.mTitle = (TextView) view.findViewById(R.id.poi_title);
            holer.mContent = (TextView) view.findViewById(R.id.poi_content);
            view.setTag(holer);
        }else{
            holer = (ViewHoler) view.getTag();
        }

        holer.mTitle.setText(data.getGeoItem().getFormatAddress());
        holer.mContent.setText(data.getGeoItem().getDistrict());
        return view;
    }

    class ViewHoler{
        TextView mTitle;
        TextView mContent;
    }


}
