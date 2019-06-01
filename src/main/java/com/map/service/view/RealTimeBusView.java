package com.map.service.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.color;
import android.R.raw;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.media.JetPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusStationItem;
import com.map.service.R;
import com.map.service.amap.api.BusSearchMgr;

public class RealTimeBusView extends View {


	static public final int DRAW_ROAD_VERTICAL = 1;
	static public final int DRAW_ROAD_HORIZONTAL = 2;
	
	
	ArrayList<HashMap<String, String>> roadList = new ArrayList<HashMap<String,String>>();
	ArrayList<HashMap<String, ArrayList<Float>>> coorsList = new ArrayList<HashMap<String,ArrayList<Float>>>();
	
	List<Map<String, String>> r_road_list = new ArrayList<Map<String,String>>();
	List<Map<String, String>> l_road_list = new ArrayList<Map<String,String>>();
	Map<String, String> road_general_info = new HashMap<String, String>();

	List<BusStationItem> mBusStationItems = new ArrayList<>();

	FontMetricsInt fontMetrics;
	Paint paint = new Paint();
	Rect bgRect = new Rect();
	Rect speedRect = new Rect();
	Rect crossRect = new Rect();
	Rect colorRect = new Rect();
	Path path = new Path();
	
	int goLineColor = Color.GREEN;
	int backLineColor = Color.GREEN;
	int curDrawDerection = DRAW_ROAD_VERTICAL;//默认水平
	
	String mTime="";
	String TIME_FORMAT = "%02d:%02d"; 
	
	boolean showTime = false;
	boolean IsShowRoadName = false;
	
	int verDistance = 15;
	int horDistance = 5;
	
	int radius = 15;
	
	int[] road_state_colors = {Color.rgb(255, 201, 14), Color.rgb(173, 14, 22), Color.RED, Color.GREEN, Color.BLACK};
	String [] road_state_strs;
	String can_not_search_road_info = "没有公交路线信息";
	
	int margin_left = 10;
	int margin_right = 10;
	
	int testSize = 30;

	BusSearchMgr mBusSearchMgr;

	public RealTimeBusView(Context context){
		super(context,null);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		mTime = String.format(TIME_FORMAT,calendar.get(Calendar.HOUR),
				calendar.get(Calendar.MINUTE));
		initResourceStr(context);
	}
	
	public RealTimeBusView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initResourceStr(context);
	}
	
	public RealTimeBusView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initResourceStr(context);
	}
	
	private void initResourceStr(Context context){
		road_state_strs = context.getResources().getStringArray(R.array.road_state_strs);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub

		super.onDraw(canvas);
		int viewWidth = getMeasuredWidth();
		int viewHeight = getMeasuredHeight();
		
		//道路背景
		bgRect.left =0;
		bgRect.top = 0;
		bgRect.right = viewWidth/2;
		bgRect.bottom = 50;

		paint.setStrokeWidth(20);
		paint.setAntiAlias(true);
		setBackgroundColor(Color.WHITE);
		onDrawByEngineAUTONAVI(canvas, viewWidth, viewHeight);
	}
	
	private void onDrawByEngineAUTONAVI(Canvas canvas, int viewWidth, int viewHeight){
		if (coorsList.size()==0) {
			bgRect.left =0;
			bgRect.top = 0;
			bgRect.right = viewWidth;
			bgRect.bottom = viewHeight;
			paint.setTextSize(40);
			fontMetrics =  paint.getFontMetricsInt();
			paint.setTextAlign(Align.CENTER);
			int baseline1 = bgRect.top + (bgRect.bottom -bgRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
			canvas.drawText(can_not_search_road_info, bgRect.centerX(), baseline1, paint);
			return;
		}
		if (roadList.size()==0) {
			return;
		}
		HashMap<String, String>  ro = roadList.get(0);
		String road   = ro.get("road");
		int heading = Integer.parseInt(ro.get("heading"));
		
		//道路名称，状态
		paint.setTextSize(30);
		paint.setTextAlign(Align.LEFT);
		fontMetrics =  paint.getFontMetricsInt();
		int baseline = bgRect.top + (bgRect.bottom - bgRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
		
		if (IsShowRoadName) {
//			paint.setColor(Color.GRAY);
//			canvas.drawRect(bgRect, paint);
			paint.setColor(Color.BLACK);
			canvas.drawText(road, 50, baseline, paint);
		}
		
		bgRect.left = viewWidth/2;
		if (showTime) {
			canvas.drawText(mTime, 50, baseline*2, paint);
		}

		
		setCurDrawDirection(heading);
		
		switch (curDrawDerection) {
		
		case DRAW_ROAD_VERTICAL:
				
			for (int i = 0; i < coorsList.size(); i++) {
				HashMap<String, String>  roadinfo = roadList.get(i);
				String status = roadinfo.get("status");
				String direction = roadinfo.get("direction");
				String lcodes = roadinfo.get("lcodes");
				String speed = roadinfo.get("speed");
				Log.e("MyView", "status: "+status+ "road:"+road+" "
						+"direction:"+direction+" heading:"+heading+" lcodes"+lcodes
						+" speed"+speed
				);
				if (coorsList.size()==1) 
				{
					return;
				}
				int offset = (viewWidth-(verDistance+radius)*2)/(coorsList.size()-1);
				//两排圆圈
				paint.setColor(Color.BLACK);
				canvas.drawCircle(offset*i+verDistance+radius, viewHeight/2-50, radius, paint);
				canvas.drawCircle(offset*i+verDistance+radius, viewHeight/2, radius, paint);
				paint.setColor(Color.WHITE);
				canvas.drawCircle(offset*i+verDistance+radius, viewHeight/2-50, 10, paint);
				canvas.drawCircle(offset*i+verDistance+radius, viewHeight/2, 10, paint);
				
				//连线与箭头，速度
				if (i!=coorsList.size()-1) {
					
					reseatLineColor();
					
					if (judgeTheDirection(lcodes)) {
						goLineColor = getLinesColorByStatues(status);
					}else{
						backLineColor = getLinesColorByStatues(status);
					}
					//连线
					paint.setColor(backLineColor);
					canvas.drawLine(offset*i+verDistance+radius*2, viewHeight/2-50, offset*(i+1)+verDistance, viewHeight/2-50, paint);
					//箭头
					path.reset();
					path.moveTo(offset*(i+1)-offset/2+radius*2 , viewHeight/2-60);
					path.lineTo(offset*(i+1)-offset/2+radius*2-10, viewHeight/2-50);
					path.lineTo(offset*(i+1)-offset/2+radius*2, viewHeight/2-40);
					paint.setColor(Color.WHITE);
					canvas.drawPath(path, paint);
					
					//连线
					paint.setColor(goLineColor);
					canvas.drawLine(offset*i+verDistance+radius*2, viewHeight/2, offset*(i+1)+verDistance, viewHeight/2, paint);
					//箭头
					path.reset();
					path.moveTo(offset*(i+1) -offset/2+radius*2 , viewHeight/2-10);
					path.lineTo(offset*(i+1)+10-offset/2+radius*2, viewHeight/2);
					path.lineTo(offset*(i+1)-offset/2+radius*2, viewHeight/2+10);
					paint.setColor(Color.WHITE);
					canvas.drawPath(path, paint);
					
					//速度
					if (isShowSpeedByStatues(status)) {//2.3时显示速度
						paint.setColor(Color.RED);
						if (judgeTheDirection(lcodes)) {//判断显示在上边还是下边
							paint.setTextSize(30);
							fontMetrics =  paint.getFontMetricsInt();
							speedRect.left = offset*i+verDistance + radius*2;
							speedRect.top = viewHeight/2;
							speedRect.right = offset*(i+1)+verDistance - radius;
							speedRect.bottom = viewHeight/2+50;
							int baseline1 = speedRect.top + (speedRect.bottom -speedRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
							canvas.drawText(speed, speedRect.centerX(), baseline1, paint);
							paint.setTextSize(18);
							canvas.drawText("Km/h", speedRect.centerX(), baseline1+20, paint);
						}else{
							paint.setTextSize(30);
							fontMetrics =  paint.getFontMetricsInt();
							speedRect.left = offset*i+verDistance + radius*2;
							speedRect.top = viewHeight/2-130;
							speedRect.right = offset*(i+1)+verDistance - radius;
							speedRect.bottom = viewHeight/2-80;
							int baseline1 = speedRect.top + (speedRect.bottom -speedRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
							canvas.drawText(speed, speedRect.centerX(), baseline1, paint);
							paint.setTextSize(18);
							canvas.drawText("Km/h", speedRect.centerX(), baseline1+20, paint);
						}
					}
					
				}
				//路口名称
				paint.setTextSize(testSize);
				paint.setColor(Color.BLACK);
				String[] names = direction.split("到");
				String cross = names[0].substring(1);
				float textContentLength = viewHeight/2- 10;
				float textLength = paint.measureText(cross);
				while (textLength>textContentLength) {
					testSize= testSize - 1;
					paint.setTextSize(testSize);
					textLength = paint.measureText(cross);
				}
				for (int j = 0; j < cross.length(); j++) {
					canvas.drawText(String.valueOf(cross.toCharArray()[j]),offset*i+verDistance,testSize*(j+1)+viewHeight/2+15, paint);
				}
				
			}
			break;
		
		case DRAW_ROAD_HORIZONTAL:
			Log.e("coorsSize", "size:"+coorsList.size());
			Log.e("roadList", "size:"+roadList.size());
			for (int i = 0; i < coorsList.size(); i++) {
				HashMap<String, String>  roadinfo = roadList.get(i);
				String status = roadinfo.get("status");
				String direction = roadinfo.get("direction");
				String lcodes = roadinfo.get("lcodes");
				String speed = roadinfo.get("speed");
				Log.e("MyView", "status: "+status+ "road:"+road+" "
						+"direction:"+direction+" heading:"+heading+" lcodes"+lcodes
						+" speed"+speed
				);
				if (coorsList.size()==1) 
				{
					return;
				}
				int offset = (viewHeight-(horDistance+radius)*2)/(coorsList.size()-1);
				//两排圆圈
				paint.setColor(Color.BLACK);
				canvas.drawCircle(viewWidth/2-25, offset*i+horDistance+radius, 15, paint);
				canvas.drawCircle(viewWidth/2+25, offset*i+horDistance+radius, 15, paint);
				paint.setColor(Color.WHITE);
				canvas.drawCircle(viewWidth/2-25, offset*i+horDistance+radius, 10, paint);
				canvas.drawCircle(viewWidth/2+25, offset*i+horDistance+radius, 10, paint);
				
				//连线与箭头，速度
				if (i!=coorsList.size()-1) {
					
					reseatLineColor();
					
					if (judgeTheDirection(lcodes)) {
						goLineColor = getLinesColorByStatues(status);
					}else{
						backLineColor = getLinesColorByStatues(status);
					}
					//连线
					paint.setColor(backLineColor);
					canvas.drawLine(viewWidth/2-25, offset*i+horDistance+radius*2, viewWidth/2-25, offset*(i+1)+horDistance+radius, paint);
					//箭头
					path.reset();
					path.moveTo(viewWidth/2-35, offset*(i+1)-offset/2+radius*2);
					path.lineTo(viewWidth/2-25,offset*(i+1)-offset/2+radius*2+20);
					path.lineTo(viewWidth/2-15,offset*(i+1)-offset/2+radius*2);
					paint.setColor(Color.WHITE);
					canvas.drawPath(path, paint);
					
					//连线
					paint.setColor(goLineColor);
					canvas.drawLine(viewWidth/2+25, offset*i+horDistance+radius*2, viewWidth/2+25, offset*(i+1)+horDistance+radius, paint);
					//箭头
					path.reset();
					path.moveTo(viewWidth/2+15, offset*(i+1)-offset/2+20);
					path.lineTo(viewWidth/2+25,	offset*(i+1)-offset/2);
					path.lineTo(viewWidth/2+35, offset*(i+1)-offset/2+20);
					paint.setColor(Color.WHITE);
					canvas.drawPath(path, paint);
					
					//速度
					if (isShowSpeedByStatues(status)) {//2.3时显示速度
						paint.setColor(Color.RED);
						if (judgeTheDirection(lcodes)) {//判断显示在上边还是下边
							paint.setTextSize(30);
							fontMetrics =  paint.getFontMetricsInt();
							speedRect.left = viewWidth/2+100;
							speedRect.top = offset*i;
							speedRect.right = viewWidth/2+150;
							speedRect.bottom =offset*(i+1);
							int baseline1 = speedRect.top + (speedRect.bottom -speedRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
							canvas.drawText(speed, speedRect.centerX(), baseline1, paint);
							paint.setTextSize(18);
							canvas.drawText("Km/h", speedRect.centerX(), baseline1+20, paint);
						}else{
							paint.setTextSize(30);
							fontMetrics =  paint.getFontMetricsInt();
							speedRect.left = viewWidth/2-150;
							speedRect.top = offset*i;
							speedRect.right = viewWidth/2-100;
							speedRect.bottom =offset*(i+1);
							int baseline1 = speedRect.top + (speedRect.bottom -speedRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
							canvas.drawText(speed, speedRect.centerX(), baseline1, paint);
							paint.setTextSize(18);
							canvas.drawText("Km/h", speedRect.centerX(), baseline1+20, paint);
						}
					}
					
				}
				//路口名称
				paint.setTextSize(30);
				paint.setColor(Color.BLACK);
				String[] names = direction.split("到");
				String cross = names[0].substring(1);
				crossRect.left = viewWidth/2+100;
				crossRect.top = offset*i+horDistance;
				crossRect.right = viewWidth/2+150;
				crossRect.bottom = offset*i+horDistance+radius;
				fontMetrics =  paint.getFontMetricsInt();
				int crossline = crossRect.top + (crossRect.bottom -crossRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
				canvas.drawText(cross, crossRect.centerX(), crossline, paint);
			}
			
			break;
			
		default:
			break;
		}
	}

	

	
	public boolean isShowTime() {
		return showTime;
	}


	public void setShowTime(boolean showTime) {
		this.showTime = showTime;
	}

	
	
	public boolean isIsShowRoadName() {
		return IsShowRoadName;
	}

	public void setIsShowRoadName(boolean isShowRoadName) {
		IsShowRoadName = isShowRoadName;
	}

	public void setCurDrawDirection(int heading){
		/*
		 * 由第一个数据的方向角确定绘制方向   0~45 135~225 315~360水平方向
		 * 45~135 225~360 竖直方向
		 * */
		if (heading<45||heading>135&&heading<225||heading>315) {
			curDrawDerection =	DRAW_ROAD_VERTICAL;
		}else{
			curDrawDerection =  DRAW_ROAD_HORIZONTAL;
		}
		//如果路口数量太多，则绘制模式改为水平方向
		if (coorsList.size()>4) {
			curDrawDerection =  DRAW_ROAD_VERTICAL;
		}
	}
	
	
	public void reseatLineColor(){
		 goLineColor = Color.GREEN;
		 backLineColor = Color.GREEN;
	}
	
	public int getLinesColorByStatues(String status){
		//状态连线,0未知，1畅通，2缓行，3拥堵
		if (status.contains("1")||status.contains("0")) {
			return Color.GREEN;
		}else if(status.contains("2")){
			return Color.parseColor("#FFD700");
		}else{
			return Color.RED;
		}
	}
	
	public boolean isShowSpeedByStatues(String status){
		if(status.contains("2")||status.contains("3")){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean judgeTheDirection(String lcodes){
		if (lcodes.contains("-")) {
			return false;
		}else{
			return true;
		}
	}
	
	public void setRoadInfo(ArrayList<HashMap<String, String>> roadList){
		this.roadList = roadList;
	}
	
	public void setCoors(ArrayList<HashMap<String, ArrayList<Float>>> coorsList) {
		this.coorsList = coorsList;
		invalidate();
	}

	public void setBusLineItem(BusLineItem item,String cityCode){
		mBusSearchMgr = new BusSearchMgr(getContext());
		mBusSearchMgr.setLineListener(new BusSearchMgr.BusLineSearchListener() {
			@Override
			public void onSuccess(List<BusLineItem> lineItems) {
				BusLineItem lineItem = lineItems.get(0);
				mBusStationItems = lineItem.getBusStations();
				invalidate();
			}

			@Override
			public void onFail(String error) {

			}
		});
		mBusSearchMgr.searchLine(item.getBusLineId(),cityCode);
	}
}