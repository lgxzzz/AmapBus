package com.map.service.bean;


import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;

/**
 * Created by lgx on 2019/5/14.
 */

public class GeoSearchEntity {
    boolean isChoose = false;
    GeocodeAddress geoItem;

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public GeocodeAddress getGeoItem() {
        return geoItem;
    }

    public void setGeoItem(GeocodeAddress geoItem) {
        this.geoItem = geoItem;
    }
}
