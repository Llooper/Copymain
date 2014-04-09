package com.example.main;

import java.util.ArrayList;
import java.util.List;

import com.example.main_Entity.OverlayEntity;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class DemoService extends Service{
	private String TAG = "DemoService";
	private static boolean log = false;
	public static final String ACTION = "com.lql.service.ServiceDemo";
	private String ID = "";
	private String urlServer = "59.39.61.50";
//	private String urlServer = "192.168.1.10";
	private String DBurl = "www.wingsofark.com:8101";
//	private String DBurl = urlServer;
	private int SERVERPORT = 8102;
	Thread test_thread = new Thread(){
		public void run(){
			int work = 0;
			boolean isAppRunning = false;
			String MY_PKG_NAME = DemoService.class.getPackage().getName();
			while(true){
				try {
//					if(log)Log.v(TAG,"running..."+String.valueOf(work++));
//					if(log)Log.v(TAG,"current_code: "+String.valueOf(current_code));
					//判断应用是否在运行
					ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
					List<RunningTaskInfo> list = am.getRunningTasks(100);
					for(RunningTaskInfo info : list){
						if(info.topActivity.getPackageName().equals(MY_PKG_NAME) 
								|| info.baseActivity.getPackageName().equals(MY_PKG_NAME)){
							isAppRunning = true;
							if(log)Log.v(TAG,MY_PKG_NAME +" found");
							
//							String data1 = sharedPreferences.getString("found", "");
//							if(log)Log.v("sharedPreferences.getString","found "+ data1);
							break;
						}
					}
					if(!isAppRunning){
						if(log)Log.v(TAG,"request stop");
					    GlobalID globalID = ((GlobalID)getApplication());
						globalID.cancel_notification();
						DemoService.this.stopSelf();
						return;
					}
					Thread.sleep(120*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if(log)Log.v(TAG,"test_thread interrupted");
					break;
				}
			}
		}
	};
	public List<OverlayEntity> GpsDataArrays = new ArrayList<OverlayEntity>();
	public List<OverlayEntity> GpsDeleteArrays = new ArrayList<OverlayEntity>();

	private int current_code = -1;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
//		if(log)Log.v(TAG,"onBind");
		return null;
	}
	
	@Override
	public void onCreate() {
//		if(log)Log.v(TAG, "ServiceDemo onCreate");
		super.onCreate();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		GlobalID globalID = ((GlobalID)getApplication());
		this.ID = globalID.getID();
		this.urlServer = globalID.getUrlServer();
		this.DBurl = globalID.getDBurl();
		this.SERVERPORT = globalID.getSERVERPORT();
//		this.MsgDataArrays = globalID.MsgDataArrays;
//		this.MsgDeleteArrays = globalID.MsgDeleteArrays;
//		this.ImgDataArrays = globalID.ImgDataArrays;
//		this.ImgDeleteArrays = globalID.ImgDeleteArrays;
		this.current_code = globalID.getCurrent_code();
		if(!test_thread.isAlive())test_thread.start();
		if(log)Log.v(TAG, "ServiceDemo onStart");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//		if(log)Log.v(TAG, "ServiceDemo onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {     
		 super.onDestroy();
		 if(log)Log.v(TAG, "onDestroy stopedService");
			if(log)Log.v(TAG,"current_code: "+String.valueOf(current_code));
			GlobalID globalID = ((GlobalID)getApplication());
			globalID.setID(ID);
			globalID.setUrlServer(urlServer);
			globalID.setDBurl(DBurl);
			globalID.setSERVERPORT(SERVERPORT);
//			globalID.MsgDataArrays = this.MsgDataArrays;
//			globalID.BusDeleteArrays = this.BusDeleteArrays;
//			globalID.ImgDataArrays = this.ImgDataArrays;
//			globalID.ImgDeleteArrays = this.ImgDeleteArrays;
			globalID.setCurrent_code(current_code);
			if(log)Log.v(TAG,"global current_code: "+String.valueOf(globalID.getCurrent_code()));
			test_thread.interrupt();
		 }

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getUrlServer() {
		return urlServer;
	}

	public void setUrlServer(String urlServer) {
		this.urlServer = urlServer;
	}

	public String getDBurl() {
		return DBurl;
	}

	public void setDBurl(String dBurl) {
		DBurl = dBurl;
	}

	public int getSERVERPORT() {
		return SERVERPORT;
	}

	public void setSERVERPORT(int sERVERPORT) {
		SERVERPORT = sERVERPORT;
	}

	public List<OverlayEntity> getGpsDataArrays() {
		return GpsDataArrays;
	}

	public void setGpsDataArrays(List<OverlayEntity> gpsDataArrays) {
		GpsDataArrays = gpsDataArrays;
	}

	public List<OverlayEntity> getGpsDeleteArrays() {
		return GpsDeleteArrays;
	}

	public void setGpsDeleteArrays(List<OverlayEntity> gpsDeleteArrays) {
		GpsDeleteArrays = gpsDeleteArrays;
	}

	public int getCurrent_code() {
		return current_code;
	}

	public void setCurrent_code(int current_code) {
		this.current_code = current_code;
	}

}