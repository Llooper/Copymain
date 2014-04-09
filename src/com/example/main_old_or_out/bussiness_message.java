package com.example.main_old_or_out;
//package com.example.main;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.Socket;
//import java.net.UnknownHostException;
//import java.util.ArrayList;
//import java.util.Calendar;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.example.main.MsgListView.OnPullDownListener;
//import com.example.main_Adapter.bussinessAdapter;
//import com.example.main_Entity.bussinessEntity;
//import com.example.main_Info.bussinessInfo;
//import com.example.main_util.FuntionUtil;
//
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.app.ListActivity;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.os.Parcelable;
//import android.os.StrictMode;
//import android.util.Log;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemLongClickListener;
//import android.widget.ListAdapter;
//import android.widget.Toast;
//import android.widget.AdapterView.OnItemClickListener;
//
//
//public class bussiness_message extends ListActivity{
//
//	private static boolean log = false;
//	private String TAG = "bus";
//	
//	//set background_colour here and info background_colour
//	private String background_colour = "#99CCFF";
//	
//	//connect address
//	InetAddress serverAddr;
//	
//	//test data
////	static int k = 0;
//	
//	//MsgListView and Adapter
//	private MsgListView mListView ;
//	private bussinessAdapter mAdapter;
//
//	
//	private String currentStart = "2013-01-01 00:00:00";
//	private String currentEnd = "3000-12-31 23:59:59";
//	private int currentLine = 5;
//	private int updateLines = 0;
//	private long exitTime = 0;
//	
////	private List<bussinessEntity> globalID.BusDataArrays = new ArrayList<bussinessEntity>();
////	private List<bussinessEntity> globalID.BusDataArrays2 = new ArrayList<bussinessEntity>();
//
//	ProgressDialog pd = null;
//
//	Message msg = new Message();
//	final Handler mhandler = new Handler(){
//		@Override
//		public void  handleMessage (Message msg){
//			switch (msg.what){
//			case 0:
////				if(log)Log.v("mhandler", "case 0");
//				toast("查询错误");
//				break;
//			case 1:
////				if(log)Log.v("mhandler", "case 1");
//				if(updateLines!=0){
//					toast("该时段最近"+ updateLines +"条新闻信息");
//					break;
//				}
//			case 2:
////				if(log)Log.v("mhandler", "case 2");
//				if(updateLines!=0){
//					toast("刷新"+ updateLines +"条新闻信息");
//					break;
//				}
//			case 3:
////				if(log)Log.v("mhandler", "case 3");
//				if(updateLines!=0){
//					toast("加载"+ updateLines +"条历史信息");
//					break;
//				}
//			case 4:
////				if(log)Log.v("mhandler", "case 4");
//				toast("没有找到新闻信息");
//				break;
//				}
//		}
//	};
//	
//		@Override 
//		public void onCreate(Bundle savedInstanceState) { 
//		super.onCreate(savedInstanceState); 
//		GlobalID globalID = ((GlobalID)getApplication());
////		if(null != savedInstanceState){
////			String decode = savedInstanceState.getString("BusDataArrays"); 
////			if(log)Log.e(TAG, "onCreate get the BusDataArrays = "+decode);
////			globalID.setBusDataArrays(decode);
////			} 
//		//set no title
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.msglistview);
//		if(log)Log.v(TAG,"onCreate");
//
//		//set Adapter for ListView
//		initView();
//		
//		if(globalID.isBus_change()){
//			if(log)Log.v(TAG,"isBus_change");
//			globalID.setBus_change(false);
//                exitTime = System.currentTimeMillis();
//			try{
//				if(globalID.getID() == null||globalID.getStartDate() == null||globalID.getEndDate() == null){
//					globalID.start();
//				}
//					currentStart = globalID.getStartDate();
//					currentEnd = globalID.getEndDate();
//					currentLine = globalID.getLine();
//							
//					globalID.BusDataArrays.removeAll(globalID.BusDataArrays);
//					GetDataTask bus = new GetDataTask();
//					bus.startTime = currentStart;
//					bus.endTime = currentEnd;
//					bus.line = currentLine;
//					bus.Case = 1;
//					bus.Insert_Head = false;
//					bus.setId = "!= -1";
//					bus.DESC = "DESC";
//
//					pd = ProgressDialog.show(bussiness_message.this, "连接", "正在获取数据…");
//					bus.execute();
//				
//			}
//			catch(Exception e){
//				if(log)Log.v(TAG,"Exception" + e);
//			}
//		}
//		}
//
//		private void initView() {
//			// TODO Auto-generated method stub
//			final GlobalID globalID = ((GlobalID)getApplication());
//			mListView = (MsgListView)findViewById(android.R.id.list);
//			mListView.setOnItemClickListener(new OnItemClickListener(){
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//		                long arg3) {
//					globalID.un_stop = true;
//					
//					//set data to inner activity
//					String Title = globalID.BusDataArrays.get(arg2-1).getBussiness_Title();
//					String PostTime = globalID.BusDataArrays.get(arg2-1).getBussiness_PostTime();
//					String Poster = globalID.BusDataArrays.get(arg2-1).getBussiness_Poster();
//					String Content = globalID.BusDataArrays.get(arg2-1).getBussiness_detail();
//					
//					//set picture
//					
////					if(log)Log.v(TAG, String.valueOf(globalID.BusDataArrays.size()));
//					Bundle bundle = new Bundle();
//					bundle.putString("Title", Title);
//					bundle.putString("PostTime", PostTime);
//					bundle.putString("Poster", Poster);
//					bundle.putString("Content", Content);
//					bundle.putString("colour", background_colour);
//					
//					Intent intent = new Intent(bussiness_message.this,bussinessInfo.class);
//					
//					//post data
//					intent.putExtras(bundle);
//		            startActivity(intent);
//			}
//			});
//			
//			mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//				@Override
//				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//						final int arg2, long arg3) {
//					// TODO Auto-generated method stub
//					
//					new AlertDialog.Builder(bussiness_message.this).  
//					setTitle("删除信息").setMessage("确定删除？").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							dialog.dismiss();
//							globalID.BusDeleteArrays.add(globalID.BusDataArrays.get(arg2-1));
//							globalID.BusDataArrays.remove(arg2-1);
//							mAdapter.notifyDataSetChanged();
//							
//							toast("删除一条新闻信息");
//							}
//						}).show();
//					return true;
//				}
//			});
//			
////			mListView.setBackgroundColor(android.graphics.Color.parseColor(background_colour));
//			mListView.setBackgroundResource(R.drawable.text_background);
//			
//			mAdapter = new bussinessAdapter(this,globalID.BusDataArrays);
//			mListView.setAdapter(mAdapter);
//			
//
//			//设置可以自动获取更多 滑到最后一个自动获取  改成false将禁用自动获取更多
//			mListView.enableAutoFetchMore(false, 1);
////			//隐藏 并禁用尾部
////			mListView.setHideFooter();
////			//显示并启用自动获取更多
////			mListView.setShowFooter();
//			
//			mListView.setOnPullDownListener(new OnPullDownListener(){
//				/**更多事件接口  这里要注意的是获取更多完 要关闭 更多的进度条 notifyDidMore()**/
//
//				@Override
//				public void onRefresh() {
//					// TODO Auto-generated method stub
//					if((System.currentTimeMillis()-exitTime) < 2000){
//						((MsgListView) getListView()).onRefreshComplete();
//		            }
//					else{
//						refresh();
//					}
//				}
//
//				@Override
//				public void onMore() {
//					// TODO Auto-generated method stub
//					if((System.currentTimeMillis()-exitTime) < 2000){
//						((MsgListView) getListView()).onMoreComplete();
//		            }
//					else{
//					GetDataTask bus = new GetDataTask();
//					if(!globalID.BusDataArrays.isEmpty()){
//						bus.endTime = globalID.BusDataArrays.get(globalID.BusDataArrays.size()-1).getBussiness_PostTime();
//						bus.setId = "< "+globalID.BusDataArrays.get(globalID .BusDataArrays.size()-1).getId();
//						if(log)Log.v("onmore", "startTime = "+ globalID.BusDataArrays.get(globalID.BusDataArrays.size()-1).getBussiness_PostTime());
//					}
//					else {
//						bus.endTime = currentStart;
//						bus.setId = "!= -1";
//					}
//					bus.startTime = "0-0-0";
//					bus.execute();
//					bus.Case = 3;
//					bus.line = currentLine;
//					bus.Insert_Head = false;
//					bus.DESC = "DESC";
//	                exitTime = System.currentTimeMillis();
//					}
//				}
//			});
//		}
//		
//		public void onOtherResume(){
//			super.onResume();
//			if(log)Log.v(TAG,"bus_onOtherResume");
//			GlobalID globalID = ((GlobalID)getApplication());
//			if(globalID.isBus_push_UnGet()){
////				if(log)Log.v(TAG,"bus_push");
//				refresh();
//				globalID.setBus_push_UnGet(false);
//			}
//		}
//		
//		public void refresh(){
//			GlobalID globalID = ((GlobalID)getApplication());
//			GetDataTask bus = new GetDataTask();
//			if(!globalID.BusDataArrays.isEmpty()){
//				bus.startTime = globalID.BusDataArrays.get(0).getBussiness_PostTime();
//				bus.setId = "> "+globalID.BusDataArrays.get(0).getId();
////				if(log)Log.v("refresh", "startTime = "+ globalID.BusDataArrays2.get(0).getBussiness_PostTime());
//			}
//			else {
//				bus.startTime = currentStart;
//				bus.setId = "!= -1";
//			}
//			Calendar eDate = Calendar.getInstance();
//			bus.endTime = eDate.get(Calendar.YEAR)+"-"+(eDate.get(Calendar.MONTH)+1)+"-"+eDate.get(Calendar.DATE);
//			bus.Case = 2;
//			bus.line = currentLine;
//			bus.Insert_Head = true;
//			bus.DESC = "ASC";
//            exitTime = System.currentTimeMillis();
//			bus.execute();
//		}
//		
//		@Override
//		protected void onResume(){
//			super.onResume();
//			if(log)Log.v(TAG,"onResume");
////			mListView.headView.setPadding(0, -1 * mListView.headContentHeight, 0, 0);
////			//refresh headView...not a good idea
//////			if((System.currentTimeMillis()-exitTime) < 2000){
//////				return;
//////            }
//////			else{
////			GlobalID globalID = ((GlobalID)getApplication());
////			if(globalID.getCurrent_code() != 0){
//////				if(log)Log.v(TAG,"getCurrent_code != 0 ?");
////				return;
////			}
////			if(globalID.isBus_push_UnGet()){
////				if(log)Log.v(TAG,"bus_push_unget");
////				pd = ProgressDiaif(log)Log.show(bussiness_message.this, "连接", "正在获取数据…");
////				refresh();
////				globalID.setBus_push_UnGet(false);
////				return;
////			}
////			if(globalID.isBus_change()){
////				if(log)Log.v(TAG,"isBus_change");
////				globalID.setBus_change(false);
////	                exitTime = System.currentTimeMillis();
////				try{
////					if(globalID.getID() == null||globalID.getStartDate() == null||globalID.getEndDate() == null){
////						globalID.start();
////					}
////						currentStart = globalID.getStartDate();
////						currentEnd = globalID.getEndDate();
////						currentLine = globalID.getLine();
////								
////						globalID.BusDataArrays.removeAll(globalID.BusDataArrays);
////						GetDataTask bus = new GetDataTask();
////						bus.startTime = currentStart;
////						bus.endTime = currentEnd;
////						bus.line = currentLine;
////						bus.Case = 1;
////						bus.Insert_Head = false;
////						bus.setId = "!= -1";
////						bus.DESC = "DESC";
////
////						pd = ProgressDiaif(log)Log.show(bussiness_message.this, "连接", "正在获取数据…");
////						bus.execute();
////					
////				}
////				catch(Exception e){
////					if(log)Log.v(TAG,"Exception" + e);
////				}
////			}
//			
////			}
//			return;
//			
//		}
//		
//		/*********************test data**************************/
////		private void initData(int k) {
////			// TODO Auto-generated method stub
////
////			bussinessEntity entity = new bussinessEntity();
////			entity.setBussiness_detail(String.valueOf(k));
////			Bitmap good  = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
////			entity.setBussiness_picture(good);
////			
////			//set the last to the top
////				if(globalID.BusDataArrays.isEmpty()){
////					globalID.BusDataArrays.add(0,entity);
////					globalID.BusDataArrays.add(entity);
////					return;
////					}
////				else{
////					globalID.BusDataArrays.add(globalID.BusDataArrays.get(globalID.BusDataArrays.size()-1));
////					for(int i = globalID.BusDataArrays.size()-3;i>-1;i--){
////						globalID.BusDataArrays.set(i+1, globalID.BusDataArrays.get(i));
////						}
////
////					globalID.BusDataArrays.set(0,entity);
////					mAdapter.notifyDataSetChanged();
//					//complete the refresh picture
////					((MsgListView) getListView()).onRefreshComplete();
////				}
////		}
//		/*********************test data**************************/
//		
//		private class GetDataTask extends AsyncTask<Void , Void , Void>{
//
//			public String startTime;
//			public String endTime;
//			public int line;
//			public int Case;
//			public boolean Insert_Head;
//			public String setId;
//			public String DESC;
//			
//			@SuppressLint("NewApi")
//			@Override
//			protected Void doInBackground(Void... params) {
//				// TODO Auto-generated method stub
//				if(msg != null) msg = new Message();
//				try {
//					GlobalID globalID = ((GlobalID)getApplication());
//					String url = globalID.getUrlServer();
//					serverAddr = InetAddress.getByName(url);
//					int SERVERPORT = globalID.getSERVERPORT();
////					if(startTime != globalID.getStartDate()||endTime != globalID.getEndDate()){
////						startTime = globalID.getStartDate();
////						endTime = globalID.getEndDate();
////					}
//					Socket socket = new Socket();
//					try {
//						FuntionUtil.doSth();
//						
////						socket = new Socket(serverAddr,SERVERPORT);
//						InetSocketAddress socketAdd = new InetSocketAddress(serverAddr, SERVERPORT);
//						socket.connect(socketAdd, globalID.getTIMEOUT());
//						
//				        try {
//				            PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
//
//				            out.println(7);
////				            out.println(startTime);
////				            out.println(endTime);
//				            out.println(startTime);
//				            out.println(endTime);
//				            out.println(line);
//				            out.println(setId);
//				            out.println(DESC);
//				        } catch(Exception e) {
//				                if(log)Log.v(TAG, "Exception  "+e);
//				        }
//				        finally {
//				            socket.setSoTimeout(globalID.getTIMEOUT());
//				        	 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GB2312"));
//				        	 String line = reader.readLine();
//				        	 
//				        	 if(parseJson(line,Insert_Head)){
////			        			 if(!globalID.BusDataArrays2.isEmpty())globalID.BusDataArrays2.removeAll(globalID.BusDataArrays2);
////				        		 if(log)Log.v("err", "S: Error");
////				        		 for(int j = 0; j < globalID.BusDataArrays.size() ; j++){
////				        			 bussinessEntity bentity = new bussinessEntity();
////				        			 bentity = globalID.BusDataArrays.get(globalID.BusDataArrays.size()-j-1);
////				        			 globalID.BusDataArrays2.add(bentity);
////				        		 }
//				        		 if(msg != null) msg = new Message();
//				        		 msg.what = Case;
//				                 mhandler.sendMessage(msg);
////			        			 if(log)Log.v("err", globalID.BusDataArrays2.get(0).getBussiness_PostTime());
//				        	 }
//				        	 else{
//				        		 if(msg != null) msg = new Message();
//				                 msg.what = 4;
//				                 mhandler.sendMessage(msg);
//				        	 }
//				        }
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						if(msg != null) msg = new Message();
//		                msg.what = 0;
//		                mhandler.sendMessage(msg);
//						e1.printStackTrace();
//						if(log)Log.v(TAG, "IOException "+e1);
//					}  
//				} catch (UnknownHostException e) {
//					// TODO Auto-generated catch block
//					if(msg != null) msg = new Message();
//	                msg.what = 0;
//	                mhandler.sendMessage(msg);
//					e.printStackTrace();
//					if(log)Log.v(TAG, "UnknownHostException "+e);
//				}
//				return null;
//			}
//			@Override
//			protected void onPostExecute(Void result){
//				//notify refresh data
//				final GlobalID globalID = ((GlobalID)getApplication());
//				mAdapter.notifyDataSetChanged();
//				globalID.mpAdapter.notifyDataSetChanged();
//				if(log)Log.v(TAG,"get date finish");
//				//complete the refresh picture
//				((MsgListView) getListView()).onRefreshComplete();
//				((MsgListView) getListView()).onMoreComplete();
//				super.onPostExecute(result);
//			}
//			
//		}
//		
//	    /***********Json for bussiness list******************/
//	    private boolean parseJson(String resultObj,boolean Insert_Head){
//	    	boolean insert = false;
//	    	updateLines = 0;
//			final GlobalID globalID = ((GlobalID)getApplication());
//			try{
//				JSONArray jsonArray = new JSONArray(resultObj);
//				if(jsonArray.length()==0){
//					return false;
//					}
//				else{
////					if(Insert_Head){
////						for(int i = 0 ; i < jsonArray.length() ; i++){
////							bussinessEntity entity = new bussinessEntity();
////							JSONObject jsonObject = jsonArray.getJSONObject(i);
////							entity.setId(jsonObject.optString("Id"));
////							entity.setBussiness_Title(jsonObject.optString("Title"));
////							entity.setBussiness_detail(jsonObject.optString("Content"));
////							entity.setBussiness_PostTime(jsonObject.optString("PostTime"));
////							entity.setBussiness_Poster(jsonObject.optString("Poster"));
////							if(log)Log.v(TAG,"json "+ entity.getBussiness_PostTime());
////							if(log)Log.v(TAG,"json "+ entity.getBussiness_detail());
//////							if(!insert){
//////								for(int j = 0 ;  ; j++){
//////									if(log)Log.v(TAG,"json "+j);
//////									if(j > globalID.BusDataArrays.size()-1){
//////										insert = true;
//////										break;
//////									}
//////									else{
//////										if(entity.getId().equals(globalID.BusDataArrays.get(j).getId())){
//////											break;
//////										}
//////									}
//////								}
//////							}
//////							
//////							if(insert){
////								globalID.BusDataArrays.add(0,entity);
////								updateLines++;
//////								insert = false;
//////							}
////						}
////					}
////					else{
//						for(int i = jsonArray.length()-1 ; i > -1  ; i--){
//							insert = false;
//							bussinessEntity entity = new bussinessEntity();
//							JSONObject jsonObject = jsonArray.getJSONObject(i);
//							entity.setId(jsonObject.optString("Id"));
//							entity.setBussiness_Title(jsonObject.optString("Title"));
//							entity.setBussiness_detail(jsonObject.optString("Content"));
//							entity.setBussiness_PostTime(jsonObject.optString("PostTime"));
//							entity.setBussiness_Poster(jsonObject.optString("Poster"));
////							if(log)Log.v(TAG,"json "+ entity.getBussiness_PostTime());
////							if(log)Log.v(TAG,"json "+ entity.getBussiness_detail());
//							
////							if(!insert){
////								for(int j = globalID.BusDataArrays.size()-1 ;  ; j--){
////									if(log)Log.v(TAG,"json "+j);
////									if(j < 0 ){
////										insert = true;
////										break;
////									}
////									else{
////										if(entity.getBussiness_detail().equals(globalID.BusDataArrays.get(j).getBussiness_detail())
////												&& entity.getBussiness_PostTime().equals(globalID.BusDataArrays.get(j).getBussiness_PostTime())
////												&& entity.getBussiness_Poster().equals(globalID.BusDataArrays.get(j).getBussiness_Poster())
////												&& entity.getBussiness_Title().equals(globalID.BusDataArrays.get(j).getBussiness_Title())){
////											break;
////										}
////									}
////								}
////							}
////							
////							if(insert){
//							for(int j = globalID.BusDataArrays.size()-1 ; j>-1 ; j--){
//								if(entity.getId().equals(globalID.BusDataArrays.get(j).getId())){
//									insert = true;
//									break;
//								}
//							}
//							for(int j = globalID.BusDeleteArrays.size()-1 ; j>-1 ; j--){
//								if(entity.getId().equals(globalID.BusDeleteArrays.get(j).getId())){
//									insert = true;
//									break;
//								}
//							}
//							if(insert)continue;
//							if(Insert_Head)globalID.BusDataArrays.add(0,entity);
//							else globalID.BusDataArrays.add(entity);
//							updateLines++;
////								insert = false;
////							}
////						}
//					}
//				}
//			}catch(JSONException e){
//				e.printStackTrace();
//				return false;
//			}
//			//set current time for next refresh
////			startTime = globalID.BusDataArrays.get(globalID.BusDataArrays.size()-1).getBussiness_PostTime();
//			return true;
//		}
//	    /***********Json for bussiness list******************/
//	    
//	    void toast(String msg){
//			GlobalID globalID = ((GlobalID)getApplication());
//			if(pd!=null)pd.dismiss();
//			if(globalID.toast != null)globalID.toast.cancel();
//			globalID.toast = Toast.makeText(bussiness_message.this, msg, Toast.LENGTH_LONG);
//			globalID.toast.show();
//	    }
//
//	    @Override 
//	    public void onSaveInstanceState(Bundle savedInstanceState) {  
//	    	// Save away the original text, so we still have it if the activity   
//	    	// needs to be killed while paused.
////			GlobalID globalID = ((GlobalID)getApplication());
////	    	savedInstanceState.putString("BusDataArrays", globalID.Bus2String());
//	    	
//	    	super.onSaveInstanceState(savedInstanceState);  
//	    	if(log)Log.e(TAG, "onSaveInstanceState");  
//	    	}  
//	    
//	    @Override  
//	    public void onRestoreInstanceState(Bundle savedInstanceState) {  
//	    	super.onRestoreInstanceState(savedInstanceState);
////			GlobalID globalID = ((GlobalID)getApplication());
////	    	String StrTest = savedInstanceState.getString("BusDataArrays"); 
//	    	if(log)Log.e(TAG, "onRestoreInstanceState");  
//	    	}  
//	    
//	    @Override
//		 public void onBackPressed() {
//			 //实现Home键效果 
//			 //super.onBackPressed();这句话一定要注掉,不然又去调用默认的back处理方式了 
//			 Intent intent= new Intent(Intent.ACTION_MAIN); 
//			 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
//			 intent.addCategory(Intent.CATEGORY_HOME); 
//			 startActivity(intent);  
//			 if(log)Log.v(TAG,"onBackPressed()");
//		 }
//	    @Override
//	    public void onPause(){
//	    	super.onPause();
//	    	if(log)Log.v(TAG,"onPause");
//	    }
//	    
//	    @Override
//	    public void onStop(){
//	    	super.onStop();
//	    	if(log)Log.v(TAG,"onStop");
//	    }	    
//}
