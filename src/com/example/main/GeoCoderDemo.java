package com.example.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.example.main_Entity.Ark_lineEntity;
import com.example.main_Entity.OverlayEntity;
import com.example.main_util.FuntionUtil;
import com.example.main_util.StringUtil;
 
@SuppressLint("DefaultLocale")
public class GeoCoderDemo extends Activity{
	private static final boolean log = true;
	private String TAG = "GEO";
	private String KEY = "fDvORTRl1DQYstUWOMu8FA2Z";

	private Calendar calendar = Calendar.getInstance();
	
	private Button sTime = null;
	private Button eTime = null;
	private Button selectAll = null;
	
	//set menu ID
	private static final int ITEM1 = Menu.FIRST ;
	private static final int ITEM2 = Menu.FIRST+1 ;
	private static final int ITEM3 = Menu.FIRST+2 ;
	private static final int ITEM4 = Menu.FIRST+3 ;
	private static final int ITEM5 = Menu.FIRST+4 ;

	
	private MyOverlay mOverlay = null;
	private PopupOverlay   pop  = null;
	private ArrayList<OverlayEntity> mItems = new ArrayList<OverlayEntity>();
	
	BMapManager mBMapManager = null;
	
	BMapManager mBMapMan = null;
	
	MapView mMapView = null;
	
	MKSearch mSearch = null;
	
	MapController mMapController = null;
	
	Button btn_geocode_select = null;
	Button btn_geocode_time = null;
	Button btn_geocode_search = null;
	Button btn_geocode_ark_id = null;
	
	private static TextView tv_gps_Title;
	private static ImageButton title_bar_back,title_bar_gps;
	
	private String currentStart = "2013-01-01 00:00:00";
	private String currentEnd = "3000-12-31 23:59:59";
	private String currentArk_id = "";
	private int currentLine = 5;
	private int updateLines = 0;
	private double length = 0.0;
	private static ArrayList<Ark_lineEntity> all = new ArrayList<Ark_lineEntity>();
	private int currentArk = -1;
	
	ProgressDialog pd = null;
	private String line = new String();
//	final String[] ArkNames = {"��̨118881","��̨12828 ","��ï81888 "};
	
	private static final int SWIPE_MIN_DISTANCE = 120 ;
	private static final int SWIPE_MAX_OFF_PATH = 250 ;
	//pulling speed
	private static final int SWIPE_THRESHOLD_VELOCITY = 200 ;
	//
	private GestureDetector gestureDetector ;
	View.OnTouchListener gestureListener ;

	private Context context = GeoCoderDemo.this;
	
	Message msg = new Message();
	@SuppressLint("HandlerLeak")
	final Handler mhandler = new Handler(){
		@Override
		public void  handleMessage (Message msg){
			GlobalID globalID = ((GlobalID)getApplication());
			switch (msg.what){
			case 0:
				if(log)Log.v("GeoCoderDemo mhandler", "case 0");
				if(pd!=null)pd.dismiss();
    			String strInfo = String.format("���ӳ���");
				if(globalID.toast != null)globalID.toast.cancel();
				globalID.toast = Toast.makeText(context, strInfo, Toast.LENGTH_LONG);
				globalID.toast.show();
				break;
			case 1:
				if(log)Log.v("GeoCoderDemo mhandler", "case 1");
				if(pd!=null)pd.dismiss();
        		int j = mItems.size();
        		length = 0.0;
//				mOverlay = new MyOverlay(getResources().getDrawable(R.drawable.nav_turn_via_1),mMapView);
				mOverlay = new MyOverlay(null, mMapView);
				OverlayEntity item = new OverlayEntity(mItems.get(0).getPoint(),"start",mItems.get(0).getDatetime());
//	            item.setMarker(getResources().getDrawable(R.drawable.icon_markf));
	        	mOverlay.addItem(item);

        		if(j==1){
        			//ֻ��һ����
        			mMapView.getOverlays().clear();
		        	mMapController.setZoom(11);
        		}
        		
        		else{
        			mMapView.getOverlays().clear();
		        	mMapController.setZoom(11);
	        		GeoPoint[] p = new GeoPoint[j];
	        		
	        		//�������ȡ����Ϣ
	        		for(int i = 0; i<j;i++){
	        			//��ȡ��
	        			p[i] = mItems.get(i).getPoint();
	        			
	        			//�������
	        			if(i<j-1)
	        				length += DistanceUtil.getDistance(mItems.get(i).getPoint(),mItems.get(i+1).getPoint());
	        			if(log)Log.v(TAG,i+" length: "+length);
	        		}

	        		//start point
	        		GeoPoint start = p[j-1];
        			//end point
        			GeoPoint stop  = p[0];

        			MKRoute route = new MKRoute();
        			
        			//����·ͼ
        				route.customizeRoute(start,stop,p);
        				//������վ����Ϣ��MKRoute��ӵ�RouteOverlay��
        				RouteOverlay routeOverlay = new RouteOverlay(GeoCoderDemo.this, mMapView);		
        				routeOverlay.setData(route);
        				//���ͼ��ӹ���õ�RouteOverlay
        				mMapView.getOverlays().add(routeOverlay);
    	        		
    				for(int i = 1;i<j-1;i++){
    					OverlayEntity item1 = new OverlayEntity(mItems.get(i).getPoint(),"������1",mItems.get(i).getDatetime());
    					item1.setMarker(getResources().getDrawable(R.drawable.nav_turn_via_1));
			        	mOverlay.addItem(item1);
    				}
    				item = new OverlayEntity(mItems.get(j-1).getPoint(), "last", mItems.get(j-1).getDatetime());
//		            item.setMarker(getResources().getDrawable(R.drawable.icon_gcoding));
		        	mOverlay.addItem(item);
        		}
	        	mMapView.getOverlays().add(mOverlay);

        		mMapView.getController().animateTo(mItems.get(0).getPoint());

    			String strInfo1 = String.format("����: " + all.get(currentArk).getArk_no()
												+"\n���"+ updateLines +"����¼\n"
												+"ȫ��: "+StringUtil.fnum.format(length)+ "m");
				if(globalID.toast != null)globalID.toast.cancel();
				globalID.toast = Toast.makeText(context, strInfo1, Toast.LENGTH_LONG);
				globalID.toast.show();

    			mMapView.refresh();
				break;
			case 2:
				if(log)Log.v("GeoCoderDemo mhandler","case 2");
				if(pd!=null)pd.dismiss();
        		Log.e("GeoCoderDemo mhandler err","connect error");

    			String strInfo2 = String.format("û����������Ϣ");
				if(globalID.toast != null)globalID.toast.cancel();
				globalID.toast = Toast.makeText(context, strInfo2, Toast.LENGTH_LONG);
				globalID.toast.show();
				break;
			}
		}
	};

        @SuppressLint({ "DefaultLocale", "CutPasteId" })
		@Override
        public void onCreate(Bundle savedInstanceState){
			if(log)Log.v(TAG,"here");
        	super.onCreate(savedInstanceState);
			if(log)Log.v(TAG,"here");
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

			if(log)Log.v(TAG,"here");
			if(null != savedInstanceState){
				GlobalID globalID = ((GlobalID)getApplication());
				globalID.start(context);
				String decode = savedInstanceState.getString("All"); 
				Log.e(TAG, "onCreate get the savedInstanceState + All = "+decode);
				globalID.setAll(decode);
				globalID.setNow_ark(savedInstanceState.getInt("Now_ark"));
				}
			
//          DemoApplication app = (DemoApplication)this.getApplication();

            try{

            	mBMapMan = new BMapManager(getApplication());
            	mBMapMan.init(KEY, null); 
    			if(log)Log.v(TAG,"here1");

        		//set no title
        		requestWindowFeature(Window.FEATURE_NO_TITLE);
            	//ע�⣺��������setContentViewǰ��ʼ��BMapManager���󣬷���ᱨ��
            	setContentView(R.layout.activity_geocoderdemo);


    			if(log)Log.v(TAG,"here2");
        		tv_gps_Title = (TextView)findViewById(R.id.talk_title);
        		title_bar_back = (ImageButton)findViewById(R.id.title_bar_back);
        		title_bar_gps = (ImageButton)findViewById(R.id.title_bar_gps);
        		title_bar_gps.setVisibility(View.INVISIBLE);
        		title_bar_back.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});
        		
        		//set title
        		GlobalID globalID = ((GlobalID)getApplication());
        		currentStart = globalID.getStartDate();
        		currentEnd = globalID.getEndDate();
        		all = globalID.getAll();
        		currentArk = globalID.getNow_ark();
        		if(currentArk == -1) currentArk_id = "-1";
        		else currentArk_id = all.get(currentArk).getArk_id();
        		currentLine = globalID.getLine();
            	try {
            		if(currentArk != -1)
            			creatLine(currentStart, currentEnd, currentArk_id,currentLine);
    			} catch (IOException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
    			if(log)Log.v(TAG,"here3");
        		
//        		if(globalID.getNow_ark() != -1){
//        			tv_gps_Title.setText("�û�:"+globalID.getID()+" ���� : " + globalID.getAll().get(globalID.getNow_ark()).getArk_no());
//        		}
//        		else{
        			tv_gps_Title.setText("����: "+ all.get(currentArk).getArk_no());
//        		}
        		
        		tv_gps_Title.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View arg0) {
    					// TODO Auto-generated method stub
    					selectArk_id();
    				}
    			});
        		

    			if(log)Log.v(TAG,"here4");
            	mMapView=(MapView)findViewById(R.id.bmapsView);
            	mMapView.setBuiltInZoomControls(true);
            	
            	//�����������õ����ſؼ�
            	mMapController = mMapView.getController();
            	
            	mSearch = new MKSearch();
            	mBMapManager = new BMapManager(this);

    			if(log)Log.v(TAG,"here5");
            	mSearch.init(mBMapManager, new MKSearchListener() {
                    @Override
                    public void onGetPoiDetailSearchResult(int type, int error) {
                    }
                    
        			@Override
    				@SuppressLint("DefaultLocale")
    				public void onGetAddrResult(MKAddrInfo res, int error) {
    	    			GlobalID globalID = ((GlobalID)getApplication());
        				if (error != 0) {
        					if(globalID.toast != null)globalID.toast.cancel();
        					String str = String.format("����ţ�%d", error);
        					globalID.toast = Toast.makeText(context, str, Toast.LENGTH_LONG);
        					globalID.toast.show();
        					return;
        				}
        				//��ͼ�ƶ����õ�
        				mMapView.getController().animateTo(res.geoPt);	
        				if (res.type == MKAddrInfo.MK_GEOCODE){
        					
        					//������룺ͨ����ַ���������
        					if(globalID.toast != null)globalID.toast.cancel();        					
        					String strInfo = String.format("γ��:"+StringUtil.fnum.format(res.geoPt.getLatitudeE6()/1e6)
        							+"���ȣ�"+StringUtil.fnum.format(res.geoPt.getLongitudeE6()/1e6));
        					String strInfo2 = res.strAddr;
        					globalID.toast = Toast.makeText(context, strInfo + "\n"+"��ַ��"+strInfo2, Toast.LENGTH_LONG);
        					globalID.toast.show();
        					tv_gps_Title.setText("γ:"+StringUtil.fnum.format(res.geoPt.getLatitudeE6()/1e6)
        							+"����"+StringUtil.fnum.format(res.geoPt.getLongitudeE6()/1e6));
        				}
        				if (res.type == MKAddrInfo.MK_REVERSEGEOCODE){
        					
        					//��������룺ͨ������������ϸ��ַ���ܱ�poi
        					String strInfo = res.strAddr;
        					if(globalID.toast != null)globalID.toast.cancel();
        					globalID.toast = Toast.makeText(context, strInfo, Toast.LENGTH_LONG);
        					globalID.toast.show();
        				}
        				
        				//����Item
        				OverlayItem item = new OverlayItem(res.geoPt, "", null);
        				
        				//�õ���Ҫ���ڵ�ͼ�ϵ���Դ
        				mOverlay = new MyOverlay(getResources().getDrawable(R.drawable.nav_turn_via_1),mMapView);
             	        mOverlay.addItem(item);
        				mMapView.getOverlays().clear();
             	        mMapView.getOverlays().add(mOverlay);
        				//Ϊmaker����λ�úͱ߽�
//        				marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
//        				
//        				//��item����marker
//        				item.setMarker(marker);
//        				
//        				//��ͼ�������item
//        				itemOverlay.addItem(item);
//        				
//        				//�����ͼ����ͼ��
//        				//���һ����עItemizedOverlayͼ��
//        				mMapView.getOverlays().add(itemOverlay);
        				//ִ��ˢ��ʹ��Ч
        				mMapView.refresh();
        			}

    				@Override
    				public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
    					// TODO Auto-generated method stub
    					
    				}

    				@Override
    				public void onGetDrivingRouteResult(MKDrivingRouteResult arg0,
    						int arg1) {
    					// TODO Auto-generated method stub
    					
    				}

    				@Override
    				public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
    					// TODO Auto-generated method stub
    					
    				}

    				@Override
    				public void onGetShareUrlResult(MKShareUrlResult arg0,
    						int arg1, int arg2) {
    					// TODO Auto-generated method stub
    					
    				}

    				@Override
    				public void onGetSuggestionResult(MKSuggestionResult arg0,
    						int arg1) {
    					// TODO Auto-generated method stub
    					
    				}

    				@Override
    				public void onGetTransitRouteResult(MKTransitRouteResult arg0,
    						int arg1) {
    					// TODO Auto-generated method stub
    					
    				}

    				@Override
    				public void onGetWalkingRouteResult(MKWalkingRouteResult arg0,
    						int arg1) {
    					// TODO Auto-generated method stub
    					
    				}
            	});
            	
//            	// �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����
//            	GeoPoint point =new GeoPoint((int)(39.915* 1E6),(int)(116.404* 1E6));
//            	//�ø����ľ�γ�ȹ���һ��GeoPoint����λ��΢�� (�� * 1E6)
//            	
//            	//set map center
//            	mMapController.setCenter(point);
//            	//set map zoom size
//            	mMapController.setZoom(12);
            	

    			if(log)Log.v(TAG,"here6");
            	btn_geocode_select = (Button)findViewById(R.id.btn_geocode_select);
            	btn_geocode_time = (Button)findViewById(R.id.btn_geocode_time);
            	btn_geocode_search= (Button)findViewById(R.id.btn_geocode_search);
            	btn_geocode_ark_id= (Button)findViewById(R.id.btn_geocode_ark_id);
            	
            	OnClickListener clickListener = new OnClickListener(){
         			@Override
    				public void onClick(View v) {
         					try {
    							SearchButtonProcess(v);
    						} catch (UnknownHostException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						} catch (IOException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
         			}
                 };

                 btn_geocode_select.setOnClickListener(clickListener);
                 btn_geocode_time.setOnClickListener(clickListener);
                 btn_geocode_search.setOnClickListener(clickListener);
                 btn_geocode_ark_id.setOnClickListener(clickListener);
                 
                 btn_geocode_ark_id.setText(all.get(currentArk).getArk_no());
             	if(currentLine == 100){
             		btn_geocode_search.setText("���м�¼");
             	}
             	else btn_geocode_search.setText(currentLine + " ��");

    			if(log)Log.v(TAG,"here7");
            }catch(Exception e){
            	if(log)Log.e(TAG,"Exception: "+e);
            }
        }
        
        @SuppressLint("NewApi")
		void SearchButtonProcess(View v) throws UnknownHostException, IOException {
//        	GeoPoint ptCenter = null;
    		final GlobalID globalID = ((GlobalID)getApplication());
    		if (btn_geocode_select.equals(v)) {
    			if(currentStart == globalID.getStartDate() 
    					&& currentEnd == globalID.getEndDate() && currentArk == globalID.getNow_ark()
    					&& currentLine == globalID.getLine()){
    				if(!mItems.isEmpty()){
    					mMapView.getController().animateTo(mItems.get(0).getPoint());
            			String strInfo = String.format("����: "+all.get(currentArk).getArk_no()
            											+"\n���"+ updateLines +"����¼\n"
            											+"ȫ��: "+StringUtil.fnum.format(length)+ "m");
        				if(globalID.toast != null)globalID.toast.cancel();
        				globalID.toast = Toast.makeText(context, strInfo, Toast.LENGTH_LONG);
        				globalID.toast.show();
    					tv_gps_Title.setText("ȫ��: "+StringUtil.fnum.format(length)+ "m");
    				}
    				else{
            			currentStart = globalID.getStartDate();
            			currentEnd = globalID.getEndDate();
            			currentArk = globalID.getNow_ark();
                		if(currentArk == -1) currentArk_id = "-1";
                		else currentArk_id = all.get(currentArk).getArk_id();
            			currentLine = globalID.getLine();
        				creatLine(currentStart,currentEnd,currentArk_id,currentLine);
    				}
    			}
    			else{
        			currentStart = globalID.getStartDate();
        			currentEnd = globalID.getEndDate();
        			currentArk = globalID.getNow_ark();
            		if(currentArk == -1) currentArk_id = "-1";
            		else currentArk_id = all.get(currentArk).getArk_id();
        			currentLine = globalID.getLine();
    				creatLine(currentStart,currentEnd,currentArk_id,currentLine);
    				}
    			}
    		else if (btn_geocode_time.equals(v)) {
    			selectDate();
     		}
    		 else if (btn_geocode_search.equals(v)) {
//    			ptCenter = new GeoPoint((int)(39.915* 1E6),(int)(116.404* 1E6));
//    			mSearch.reverseGeocode(ptCenter);
    			 selectLine();
    		}
    		 else if (btn_geocode_ark_id.equals(v)) {
    			 selectArk_id();
     		}
    	}
        
        @SuppressLint("NewApi")
		private void creatLine(String startDate,  String endDate,  String ark_id, int Line) throws IOException {
			// TODO Auto-generated method stub
        	final String StartDate = startDate;
        	final String EndDate = endDate;
        	final String Ark_id = ark_id;
        	final int Li = Line;
        	if(log)Log.v("gps", "ark_id"+ ark_id);
        	if(log)Log.v("gps", "Ark_id"+ Ark_id);
        	GlobalID globalID = ((GlobalID)getApplication());
			if(Ark_id.equals("-1")){
    			String strInfo = String.format("û�д�����Ϣ");
				if(globalID.toast != null)globalID.toast.cancel();
				globalID.toast = Toast.makeText(context, strInfo, Toast.LENGTH_LONG);
				globalID.toast.show();
				return;
			}
			
			pd = ProgressDialog.show(context, "����", "���ڶ�ȡ���ݡ�");
			Thread a_thread = new Thread(){
				@Override
				public void run(){
				if(msg != null) msg = new Message();
                msg.what = 1;
					try {
		        	GlobalID globalID = ((GlobalID)getApplication());
					String url = globalID.getUrlServer();
					InetAddress serverAddr = InetAddress.getByName(url);
					int SERVERPORT = globalID.getSERVERPORT();
					
			
			Socket socket = new Socket();
			
			FuntionUtil.doSth();
				
//				socket = new Socket(serverAddr,SERVERPORT);
				InetSocketAddress socketAdd = new InetSocketAddress(serverAddr, SERVERPORT);
				socket.connect(socketAdd, globalID.getTIMEOUT());
			PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
//            if(log)Log.v("LAST2", "RIGHT: ");
            out.println(10);
			out.println(StartDate);
			out.println(EndDate);
			out.println(Ark_id);
//        	if(log)Log.v("gps", "Ark_id"+ Ark_id);
			out.println(Li);

            socket.setSoTimeout(globalID.getTIMEOUT());
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GB2312"));
        	line = reader.readLine();
			
			
        	if(parseJson(line)){
                mhandler.sendMessage(msg);
        	}
        	else{
        		if(msg != null) msg = new Message();
        		msg.what = 2;
        		mhandler.sendMessage(msg);
        	}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						if(msg != null) msg = new Message();
						msg.what = 0;
						mhandler.sendMessage(msg);
					}
					
						}
					};
					a_thread.start();
		}

		@Override
        protected void onDestroy(){
                mMapView.destroy();
                if(mBMapMan!=null){
                        mBMapMan.destroy();
                        mBMapMan=null;
                }
                super.onDestroy();
        }
        @Override
        protected void onPause(){
            super.onPause();
            mMapView.onPause();
            if(mBMapMan!=null){
                   mBMapMan.stop();
            }
    	    GlobalID globalID = ((GlobalID)getApplication());
    		if(globalID.un_stop)return;
    		else{
    			globalID.create_notification("��̨��������", "��̨����", "���ͻ���", false, false, false,GeoCoderDemo.class.getName());
        		if(globalID.toast != null)globalID.toast.cancel();    			
    		}
        }
        @Override
        protected void onResume(){
            super.onResume();
            try{
        	    GlobalID globalID = ((GlobalID)getApplication());
        		globalID.cancel_notification();
                mMapView.onResume();
                if(mBMapMan!=null){
                        mBMapMan.start();
                }
            }catch(Exception e){
            	if(log)Log.e(TAG,"Exception: "+e);
            }
        }
        
        
        
        @SuppressLint("DefaultLocale")
		@SuppressWarnings("rawtypes")
		public class MyOverlay extends ItemizedOverlay{

    		public MyOverlay(Drawable defaultMarker, MapView mapView) {
    			super(defaultMarker, mapView);
    		}
    		

    		//OnClickListener for overlays
    		@SuppressLint("DefaultLocale")
			@Override
    		public boolean onTap(int index){
    			OverlayEntity item = (OverlayEntity) getItem(index);
    			String strInfo = String.format("ʱ��: "+item.getDatetime()
    					+"\nγ��:"+StringUtil.fnum.format(getItem(index).getPoint().getLatitudeE6()/1e6)
						+"���ȣ�"+StringUtil.fnum.format(getItem(index).getPoint().getLongitudeE6()/1e6));

    			GlobalID globalID = ((GlobalID)getApplication());
				if(globalID.toast != null)globalID.toast.cancel();
				globalID.toast = Toast.makeText(context, strInfo, Toast.LENGTH_LONG);
				globalID.toast.show();

				tv_gps_Title.setText("γ:"+StringUtil.fnum.format(getItem(index).getPoint().getLatitudeE6()/1e6)
						+"����"+StringUtil.fnum.format(getItem(index).getPoint().getLongitudeE6()/1e6));
				mMapView.getController().animateTo(item.getPoint());
    			return true;
    		}
    		
    		@Override
    		public boolean onTap(GeoPoint pt , MapView mMapView){
    			if (pop != null){
                    pop.hidePop();
    			}
    			return false;
    		}
        	
        }

        /***********Json for gps******************/
        private boolean parseJson(String line) {
			// TODO Auto-generated method stub
        	updateLines = 0;
        	boolean insert = true;
        	if(line.length() == 0)return false;
        	try{
        		if(!mItems.isEmpty())mItems.clear();
				JSONArray jsonArray = new JSONArray(line);
        		if(log)Log.v("OH NO1", String.valueOf(mItems.size()));
				if(jsonArray.length()==0){
					return false;
					}
				for(int i = 0 ; i < jsonArray.length() ; i++){
					insert = true;
					OverlayEntity entity = new OverlayEntity(new GeoPoint(0, 0),null,null);
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					entity.setArk_id(jsonObject.optString("ark_id"));
					entity.setArk_captain(jsonObject.optString("ark_captain"));
					entity.setDatetime(jsonObject.optString("datetime"));
					entity.getPoint().setLatitudeE6((int)(Float.valueOf(jsonObject.optString("latitude"))/1));
					entity.getPoint().setLongitudeE6((int)(Float.valueOf(jsonObject.optString("longitude"))/1));
					
					for(int j = 0; j < mItems.size() ; j++){
						if(mItems.get(j).getDatetime().equals(entity.getDatetime())){
							insert = false;
							break;
						}
					}
					
					if(insert){
						mItems.add(entity);
						updateLines++;
					}
	        		if(log)Log.v("haha", entity.getDatetime());
				}
				
			}catch(JSONException e){
				e.printStackTrace();
        		if(log)Log.v("OH NO2", String.valueOf(mItems.size()));
			}
			return true;
		}
	    /***********Json for gps******************/
        
        
        @Override
    	public boolean onCreateOptionsMenu(Menu menu) {
    		// Inflate the menu; this adds items to the action bar if it is present.
//    		getMenuInflater().inflate(R.menu.main, menu);
    		
    		//add menu item
    		menu.add(0,ITEM1,0,"��ȡ������Ϣ");
    		menu.add(0,ITEM2,0,"������Ϣ");
    		menu.add(0,ITEM3,0,"��ʱ������");
    		menu.add(0,ITEM4,0,"����������");
    		menu.add(0,ITEM5,0,"����");
    		
    		return true;
    	}
        
        @Override
		public boolean onOptionsItemSelected(MenuItem item) {
    		// Inflate the menu; this adds items to the action bar if it is present.
    		//getMenuInflater().inflate(R.menu.login, menu);

			final GlobalID globalID = ((GlobalID)getApplication());
    		switch(item.getItemId()){
    		//�˵���1��ѡ��
    		case ITEM1 :
    			selectLine();
    			break;
    		case ITEM2 :
    			if(!mItems.isEmpty()){
            		mMapView.getController().animateTo(mItems.get(0).getPoint());
					if(globalID.toast != null)globalID.toast.cancel();
					globalID.toast = Toast.makeText(context, "���"+ updateLines +"����¼\n"
							+"ȫ��: "+StringUtil.fnum.format(length)+ "m", Toast.LENGTH_LONG);
					globalID.toast.show();
    			}
				else{
					if(msg != null) msg = new Message();
					msg.what = 2;
					mhandler.sendMessage(msg);
				}
    			break;
    			
    		//case 2 change a select date
    		case ITEM3 :
    			selectDate();
    			break;
    			
    		//case 3 change a select ark_id
    		case ITEM4 :
    			selectArk_id();
    			break;
    			
    			//case 4 for setting
    		case ITEM5 :
    			Intent intent4 = new Intent(context,SettingActivity.class);
                startActivity(intent4);
    			break;
    		}
    		return true;
    	}
        
        private void selectDate(){
        	final GlobalID globalID = ((GlobalID)getApplication());
        	AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater factory = LayoutInflater.from(context);
            final View textEntryView = factory.inflate(R.layout.inquire, null);
//                builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("ѡ���ѯ��Χ");
                builder.setView(textEntryView);
                
                selectAll = (Button) textEntryView.findViewById(R.id.btn_inquire_selectAll);
                
                sTime = (Button) textEntryView.findViewById(R.id.btn_inquire_startTime); 
                eTime = (Button) textEntryView.findViewById(R.id.btn_inquire_endTime);
                
                sTime.setText(globalID.getStartDate());
                eTime.setText(globalID.getEndDate());
                
                selectAll.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						sTime.setText("2013-01-01");
						Calendar eDate = Calendar.getInstance();
						eTime.setText(eDate.get(Calendar.YEAR)+"-"+(eDate.get(Calendar.MONTH)+1)+"-"+eDate.get(Calendar.DATE));
					}
				});
                
                sTime.setOnClickListener(new OnClickListener() {
					
					@SuppressWarnings("deprecation")
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						showDialog(0);
					}
				});
                
                eTime.setOnClickListener(new OnClickListener() {
					
					@SuppressWarnings("deprecation")
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						showDialog(1);
					}
				});
                
                
                
                builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
                    @Override
					public void onClick(DialogInterface dialog, int whichButton) {
//                    	TV_sign.setText(et_asSign.getText().toString());
//                    TV_sign.setText(et_asSign.getText().toString());
                    	globalID.setStartDate(sTime.getText().toString());
            			globalID.setEndDate(eTime.getText().toString());
            			if(currentStart != globalID.getStartDate() || currentEnd != globalID.getEndDate()){
            			currentStart = globalID.getStartDate();
            			currentEnd = globalID.getEndDate();
            			try {
							creatLine(currentStart,currentEnd,currentArk_id,currentLine);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if(log)Log.v("GeoCoderDemo", "IOException: "+e);
						}
                    }
            			else{
	        				if(!mItems.isEmpty()){
	        					mMapView.getController().animateTo(mItems.get(0).getPoint());
	        					if(globalID.toast != null)globalID.toast.cancel();
	        					globalID.toast = Toast.makeText(context, "����: "+ all.get(currentArk).getArk_no()
	        							+"\n���"+ updateLines +"����¼\n"
										+"ȫ��: "+StringUtil.fnum.format(length)+ "m", Toast.LENGTH_LONG);
	        					globalID.toast.show();
	        				}
	        				else{
	        					if(msg != null) msg = new Message();
	        					msg.what = 2;
	        					mhandler.sendMessage(msg);
	        				}
	        			}
            			dialog.dismiss();
                    }
                });

                builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
                    @Override
					public void onClick(DialogInterface dialog, int whichButton) {
                    	dialog.dismiss();
                    }
                });
                builder.create().show();
        }
        
        private void selectArk_id(){
        	final GlobalID globalID = ((GlobalID)getApplication());
        	if(globalID.getNow_ark()== -1){
				new AlertDialog.Builder(context).  
				setTitle("����").setMessage("�Բ�����Ȩ�ޱ�����").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {  
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						}  
					}).show();
			}
			
			else{
//				final ArrayList<String> Items = new ArrayList<String>();
//				Items.addAll(globalID.getAll());
//				if(log)Log.v("ark","ark:"+Items.size());
				AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
	            builder1.setTitle("����ѡ���");
	            final String[] ArkItems = new String[all.size()];
	            for(int i  = 0;i<all.size();i++){
	            	ArkItems[i] = all.get(i).getArk_id()+": "+ all.get(i).getArk_no();
	            }
	            builder1.setSingleChoiceItems(ArkItems,currentArk, new DialogInterface.OnClickListener() {
	                @Override
					public void onClick(DialogInterface dialog, int which) {
	                	globalID.setNow_ark(which);
//	                	tv_gps_Title.setText("�û�:"+globalID.getID()+"    ����:("+globalID.getNow_ark()+")"+ArkNames[Integer.parseInt(globalID.getNow_ark())-1]);
	                	
 	                	if(currentArk != which){
 	                		currentArk = which;
	        				currentArk_id = all.get(currentArk).getArk_id();
	 	                	btn_geocode_ark_id.setText(all.get(currentArk).getArk_no());
 	                	try {
							creatLine(currentStart,currentEnd,currentArk_id,currentLine);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            			}
	        			else{
	        				if(!mItems.isEmpty()){
	        					mMapView.getController().animateTo(mItems.get(0).getPoint());
	        					if(globalID.toast != null)globalID.toast.cancel();
	        					globalID.toast = Toast.makeText(context, "����: "+all.get(currentArk).getArk_no()
	        							+"\n���"+ updateLines +"����¼\n"
										+"ȫ��: "+StringUtil.fnum.format(length)+ "m", Toast.LENGTH_LONG);
	        					globalID.toast.show();
	        				}
	        				else{
	        					if(msg != null) msg = new Message();
	        					msg.what = 2;
	        					mhandler.sendMessage(msg);
	        				}
	        			}
 	                	tv_gps_Title.setText("����: "+ all.get(currentArk).getArk_no());
            			dialog.dismiss();
	                }
	            });
	            builder1.create().show();
			}
        }
        
        private void selectLine(){
        	final GlobalID globalID = ((GlobalID)getApplication());
        	AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setTitle("��ѯ���N����¼");
            final String[] Lines = {"5��","10��","15��","20��","���м�¼"};
            int k = globalID.getLine()/5-1;
            if(k==19)k = 4;
			builder1.setSingleChoiceItems(Lines,k, new DialogInterface.OnClickListener() {
                @Override
				public void onClick(DialogInterface dialog, int which) {
                	if(globalID.getLine() != (which+1)*5){
                		globalID.setLine((which+1)*5);
                    	if(which == 4)globalID.setLine(100);
                    	try {
                    		currentLine = globalID.getLine();
                         	if(currentLine == 100){
                         		btn_geocode_search.setText("���м�¼");
                         	}
                         	else btn_geocode_search.setText(currentLine + " ��");
    						creatLine(currentStart, currentEnd, currentArk_id,currentLine);
    					} catch (IOException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
                		
                	}
                	else{
                		if(!mItems.isEmpty()){
        					mMapView.getController().animateTo(mItems.get(0).getPoint());
        					if(globalID.toast != null)globalID.toast.cancel();
        					globalID.toast = Toast.makeText(context, "���"+ updateLines +"����¼\n"
									+"ȫ��: "+StringUtil.fnum.format(length)+ "m", Toast.LENGTH_LONG);
        					globalID.toast.show();
        				}
        				else{
        					if(msg != null) msg = new Message();
        					msg.what = 2;
        					mhandler.sendMessage(msg);
        				}
                	}
        			dialog.dismiss();
                }
            });
			builder1.setIcon(android.R.drawable.ic_dialog_info);
            builder1.create().show();
        }
        
        
        /******************creat dialog for time selecting*******************************/
        @Override
        protected Dialog onCreateDialog(int id) {
        	Dialog dialog = null;
        	switch (id) {
        	case 0:
        		dialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
					public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
                    	if(month<9){
                        	if(dayOfMonth<10){
                            	sTime.setText(year + "-0" + (month+1) + "-0" + dayOfMonth);
                        	}
                        	else sTime.setText(year + "-0" + (month+1) + "-" + dayOfMonth);
                    	}
                    	else sTime.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                    }
                }, 
                calendar.get(Calendar.YEAR), // �������
                calendar.get(Calendar.MONTH), // �����·�
                calendar.get(Calendar.DAY_OF_MONTH) // ��������
            );
        		dialog.setTitle("ѡ��ʼ����");
            break;
        case 1:
            dialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
					public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
                    	if(month<9){
                        	if(dayOfMonth<10){
                            	eTime.setText(year + "-0" + (month+1) + "-0" + dayOfMonth);
                        	}
                        	else eTime.setText(year + "-0" + (month+1) + "-" + dayOfMonth);
                    	}
                    	else eTime.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                    }
                }, 
                calendar.get(Calendar.YEAR), // �������
                calendar.get(Calendar.MONTH), // �����·�
                calendar.get(Calendar.DAY_OF_MONTH) // ��������
            );
            dialog.setTitle("ѡ���������");
            break;
        }
        return dialog;
    }
        /******************creat dialog for time selecting*******************************/
        
        @Override
	    public void finish(){
	    	super.finish();
			overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
			GlobalID globalID = ((GlobalID)getApplication());
			globalID.un_stop = true;
	    }
        

		@Override 
	    public void onSaveInstanceState(Bundle savedInstanceState) {  
	    	// Save away the original text, so we still have it if the activity   
	    	// needs to be killed while paused.
			GlobalID globalID = ((GlobalID)getApplication());
	    	savedInstanceState.putBoolean("Bus_change", globalID.isBus_change());
	    	savedInstanceState.putBoolean("Msg_change", globalID.isMsg_change());
	    	savedInstanceState.putBoolean("Img_change", globalID.isImg_change());
	    	
	    	savedInstanceState.putString("All", globalID.Ark_line2String());
	    	savedInstanceState.putInt("Now_ark", globalID.getNow_ark());
	    	super.onSaveInstanceState(savedInstanceState);  
	    	Log.e(TAG, "onSaveInstanceState");  
	    	if(log)Log.v(TAG, "Now_ark = "+ globalID.getNow_ark());
	    	}  
	    
	    @Override  
	    public void onRestoreInstanceState(Bundle savedInstanceState) {  
	    	super.onRestoreInstanceState(savedInstanceState);
			GlobalID globalID = ((GlobalID)getApplication());
	    	globalID.setAll(savedInstanceState.getString("All"));
	    	globalID.setNow_ark(savedInstanceState.getInt("Now_ark"));
    		globalID.un_stop = false;
	    	if(log)Log.v(TAG, "Now_ark = "+ globalID.getNow_ark());
	    	}
} 
