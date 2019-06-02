package com.map.service.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.Random;

public class SQLiteDbHelper extends SQLiteOpenHelper {

    //数据库名称
    public static final String DB_NAME = "database.db";
    //数据库版本号
    public static int DB_VERSION = 2;
    //用户表
    public static final String TAB_USER = "user";
    //用户反馈表
    public static final String TAB_USER_REPORT = "user_report";
    //公交路线收藏表
    public static final String TAB_FAV_BUS_STATION = "fav_bus_station";

    public SQLiteDbHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableUser(db);
        createTableUserReport(db);
        createTableFavBusStation(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TAB_USER);
        db.execSQL("DROP TABLE IF EXISTS "+TAB_FAV_BUS_STATION);
        db.execSQL("DROP TABLE IF EXISTS "+TAB_USER_REPORT);
        onCreate(db);
    }


    //创建用户表
    public void createTableUser(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TAB_USER +
                "(id integer primary key autoincrement, " +
                "user_name varchar(60), " +
                "user_email varchar(60), " +
                "user_pwd varchar(60))");
    }

    //用户反馈表
    public void createTableUserReport(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TAB_USER_REPORT +
                "(id integer primary key autoincrement, " +
                "user_name varchar(60), " +
                "report_time varchar(60), " +
                "report_content varchar(60))");
    }

    //收藏公交信息表
    public void createTableFavBusStation(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TAB_FAV_BUS_STATION +
                "(id integer primary key autoincrement, " +
                "bus_name varchar(60), " +
                "bus_line_id varchar(60), " +
                "city_code varchar(60))");
    }
}
