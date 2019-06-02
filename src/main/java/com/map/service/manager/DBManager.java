package com.map.service.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.amap.api.services.busline.BusLineItem;
import com.map.service.bean.FavBus;
import com.map.service.bean.User;
import com.map.service.constant.ErrorCode;
import com.map.service.data.SQLiteDbHelper;

import java.util.ArrayList;
import java.util.List;

public class DBManager {

    Context mContext;

    private static DBManager mInstance = null;

    SQLiteDbHelper mDbHelper;

    public DBManager(Context context){

        this.mContext = context;
        mDbHelper = new SQLiteDbHelper(context);
        mInstance = this;
    }

    public static DBManager getInstance(Context context) {
        if (mInstance == null){
            mInstance = new DBManager(context);
        }
        return mInstance;
    }



    //所有用户
    public List<User> queryUser(String[] columns, String selection, String[]  selectionArgs, String groupBy, String having, String orderBy){
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.query(SQLiteDbHelper.TAB_USER,columns,selection,selectionArgs,groupBy,having,orderBy);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("user_name"));
            String number = cursor.getString(cursor.getColumnIndex("user_number"));
            String pwd = cursor.getString(cursor.getColumnIndex("user_pwd"));
            String role = cursor.getString(cursor.getColumnIndex("user_role"));
            User user = new User();
            user.setName(name);
            user.setPwd(pwd);
            users.add(user);
        }
        db.close();
        return users;
    }



    public void execSQL(String sql){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    public void addUser(User user,DBManagerListener listener){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        //先判断是否已经注册过用户
        Cursor cursor = db.rawQuery("select * from user where user_name =?",new String[]{user.getName()});
        if (cursor!=null&&cursor.moveToFirst()){
            listener.onFail(ErrorCode.ERROR_ALREADY_REGISTERED);
            return;
        };
        //插入用户表中
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_name",user.getName());
        contentValues.put("user_pwd",user.getPwd());
        db.insert(SQLiteDbHelper.TAB_USER,null,contentValues);
        db.close();
        listener.onSuccess(user);
    }

    //登陆
    public void doLogin(User user,DBManagerListener listener){
        try{
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from user where user_name =? and user_pwd=?",new String[]{user.getName(),user.getPwd()});
            if (cursor.moveToFirst()){
                listener.onSuccess(user);
            }else{
                listener.onFail(ErrorCode.ERROR_SEARCH);
            }
            db.close();
            return;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        listener.onFail(ErrorCode.ERROR_SEARCH);
    }

    //收藏公交路线
    public void addFavBusStation(BusLineItem item,String city_code,DBManagerListener listener){
        int index = item.getBusLineName().lastIndexOf("(");
        String buslineName = item.getBusLineName().substring(0,index);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        //先判断是否已经收藏过该路线
        Cursor cursor = db.rawQuery("select * from fav_bus_station where bus_line_id =? and bus_name =?",new String[]{item.getBusLineId(),buslineName});
        if (cursor!=null&&cursor.moveToFirst()){
            listener.onFail(ErrorCode.ERROR_ALREADY_REGISTERED);
            return;
        };


        ContentValues values = new ContentValues();
        values.put("bus_name",buslineName);
        values.put("bus_line_id",item.getBusLineId());
        values.put("city_code",city_code);
        long x = db.insert(SQLiteDbHelper.TAB_FAV_BUS_STATION,null,values);
        db.close();
        listener.onFavsucces();
    }

    //取消收藏路线
    public void delFavBusStation(String bus_line_id){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long x = db.delete(SQLiteDbHelper.TAB_FAV_BUS_STATION,"bus_line_id =?",new String[]{bus_line_id});
    }

    //获取所有收藏路线
    public List<FavBus> getAllFavBus(String[] columns, String selection, String[]  selectionArgs, String groupBy, String having, String orderBy){
        List<FavBus> buses = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.query(SQLiteDbHelper.TAB_FAV_BUS_STATION,columns   ,selection,selectionArgs,groupBy,having,orderBy);
        while (cursor.moveToNext()){
            String bus_name = cursor.getString(cursor.getColumnIndex("bus_name"));
            String bus_line_id = cursor.getString(cursor.getColumnIndex("bus_line_id"));
            String city_code = cursor.getString(cursor.getColumnIndex("city_code"));
            FavBus bus = new FavBus();
            bus.setBus_line_id(bus_line_id);
            bus.setBus_name(bus_name);
            bus.setCity_code(city_code);
            buses.add(bus);
        }
        db.close();
        return buses;
    }

    public interface DBManagerListener{
        public void onSuccess(User user);
        public void onFail(int error);
        public void onFavsucces();
    }
}
