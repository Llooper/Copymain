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
//import java.util.List;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
////import com.example.main.MsgListView.OnRefreshListener;
//import com.example.main.MsgListView.OnPullDownListener;
//import com.example.main_Adapter.TestAdapter;
//import com.example.main_Entity.TestEntity;
//import com.example.main_Info.testInfo;
//
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.app.ListActivity;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.os.StrictMode;
//import android.util.Log;
//import android.view.View;
//import android.view.Window;
//import android.widget.AdapterView;
//import android.widget.Toast;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AdapterView.OnItemLongClickListener;
//
//
//public class Text_information extends ListActivity{
//	private String TAG = "msg";
//	
//	private String background_colour = "#99CCFF";
//	
//	InetAddress serverAddr;
//	
//	//test data
////	static int k = 0;
//	
//	private MsgListView mListView ;
//	private TestAdapter mAdapter;
//
//	private String currentStart = "2013-01-01 00:00:00";
//	private String currentEnd = "3000-12-31 23:59:59";
////	private String currentArk = "";
//	private long exitTime = 0;
//	private int currentLine = 5;
//	private int updateLines = 0;
//	
////	private List<TestEntity> globalID.MsgDataArrays = new ArrayList<TestEntity>();
////	private List<TestEntity> globalID.MsgDeleteArrays = new ArrayList<TestEntity>();
//
//	ProgressDialog pd = null;
//
//	Message msg = new Message();
//	final Handler mhandler = new Handler(){
//		@Override
//		public void  handleMessage (Message msg){
//			switch (msg.what){
//			case 0:
////				Log.v("mhandler", "case 0");
//				toast("查询错误");
//				break;
//			case 1:
////				Log.v("mhandler", "case 1");
//				if(updateLines!=0){
//					toast("该时段最近"+ updateLines +"条文字信息");
//					break;
//				}
//			case 2:
////				Log.v("mhandler", "case 2");
//				if(updateLines!=0){
//					toast("刷新"+ updateLines +"条文字信息");
//					break;
//				}
//			case 3:
////				Log.v("mhandler", "case 3");
//				if(updateLines!=0){
//					toast("加载"+ updateLines +"条历史信息");
//					break;
//				}
//			case 4:
////				Log.v("mhandler", "case 4");
//				toast("没有找到文字信息");
//				break;
//				}
//		}
//	};
//		@Override 
//		public void onCreate(Bundle savedInstanceState) { 
//		super.onCreate(savedInstanceState); 
//		if(null != savedInstanceState){
//			GlobalID globalID = ((GlobalID)getApplication());
//			String decode = savedInstanceState.getString("MsgDataArrays"); 
//			Log.e(TAG, "onCreate get the savedInstanceState + MsgDataArrays = "+decode);
//			globalID.setMsgDataArrays(decode);
//			}
//		//set no title
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.msglistview);
//
//		initView();
//		
//		final GlobalID globalID = ((GlobalID)getApplication());
//
//		Log.v(TAG,"onCreate");
//		mListView = (MsgListView)findViewById(android.R.id.list);
//		mListView.setOnItemClickListener(new OnItemClickListener(){
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//	                long arg3) {
//				
////				globalID.MsgDeleteArrays.get(arg2-1).getLl_test_list().setBackgroundColor(android.graphics.Color.parseColor("#00CCFF"));
//
//				globalID.un_stop = true;
//				//set data to inner activity
//				String PostTime = globalID.MsgDataArrays.get(arg2-1).getTest_PostTime();
//				String Content = globalID.MsgDataArrays.get(arg2-1).getTest_detail();
//				String test_ark_id = globalID.MsgDataArrays.get(arg2-1).getTest_ark_id();
//				String send_user_id = globalID.MsgDataArrays.get(arg2-1).getSend_user_id();
//				//set picture
//				
//				
////				Log.v(TAG, String.valueOf(globalID.MsgDataArrays.size()));
//				Bundle bundle = new Bundle();
//				bundle.putString("PostTime", PostTime);
//				bundle.putString("Content", Content);
//				bundle.putString("test_ark_id", test_ark_id);
//				bundle.putString("send_user_id", send_user_id);
//				bundle.putString("colour", background_colour);
//				
//				Intent intent = new Intent(Text_information.this,testInfo.class);
//				
//				//post data
//				intent.putExtras(bundle);
//	            startActivity(intent);
//		}
//		});
//		
//		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//					final int arg2, long arg3) {
//				// TODO Auto-generated method stub
//				
//				new AlertDialog.Builder(Text_information.this).  
//				setTitle("删除信息").setMessage("确定删除？").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						globalID.MsgDeleteArrays.add(globalID.MsgDataArrays.get(arg2-1));
//						globalID.MsgDataArrays.remove(arg2-1);
//						mAdapter.notifyDataSetChanged();
//						GlobalID globalID = ((GlobalID)getApplication());
//						if(globalID.toast != null)globalID.toast.cancel();
//						globalID.toast = Toast.makeText(Text_information.this, "删除一条文字信息", Toast.LENGTH_LONG);
//						globalID.toast.show();
//						}
//					}).show();
//				
//				return true;
//			}
//		});
////		
////		mListView.setOnTouchListener(new OnTouchListener() {
////			
////			@Override
////			public boolean onTouch(View arg0, MotionEvent arg1) {
////				// TODO Auto-generated method stub
//////				globalID.MsgDataArrays.get(arg1).getLl_test_list().setBackgroundColor(color);
////				return false;
////			}
////		});
////		
//		
//		}
//
//		private void initView() {
//			// TODO Auto-generated method stub
//			mListView = (MsgListView)findViewById(android.R.id.list);
//			
////			GlobalID globalID = ((GlobalID)getApplication());
////			currentArk = globalID.getNow_ark();
//			
//			mListView.setBackgroundResource(R.drawable.text_background);
//
//			final GlobalID globalID = ((GlobalID)getApplication());
//			mAdapter = new TestAdapter(this,globalID.MsgDataArrays);
//			mListView.setAdapter(mAdapter);
//			
//			//设置可以自动获取更多 滑到最后一个自动获取  改成false将禁用自动获取更多
//			mListView.enableAutoFetchMore(false, 1);
////			mListView.setonRefreshListener(new OnRefreshListener(){
////				
////				@SuppressLint("NewApi")
////				public void onRefresh(){
////					GlobalID globalID = ((GlobalID)getApplication());
////					if(globalID.getNow_ark().equals("-1")){
////		    			String strInfo = String.format("没有船号信息");
////						if(globalID.toast != null)globalID.toast.cancel();
////						globalID.toast = Toast.makeText(Text_information.this, strInfo, Toast.LENGTH_LONG);
////						globalID.toast.show();
////						((MsgListView) getListView()).onRefreshComplete();
////					}
////					else{
////					if((System.currentTimeMillis()-exitTime) < 2000){
////						((MsgListView) getListView()).onRefreshComplete();
////		            }
////					else{
////						GetDataTask tex = new GetDataTask();
////						if(!globalID.MsgDeleteArrays.isEmpty()){
////							tex.startTime = globalID.MsgDeleteArrays.get(0).getTest_PostTime();
//////							Log.v("refresh", "startTime = "+ globalID.MsgDeleteArrays.get(0).getImage_datetime_send());
////						}
////						else tex.startTime = currentStart;
////						Calendar eDate = Calendar.getInstance();
////						tex.endTime = eDate.get(Calendar.YEAR)+"-"+(eDate.get(Calendar.MONTH)+1)+"-"+eDate.get(Calendar.DATE)
////								+ " " + eDate.get(Calendar.HOUR)+":"+eDate.get(Calendar.MINUTE)+":"+eDate.get(Calendar.SECOND);
////						tex.now_ark_id = currentArk;
////						tex.line = currentLine;
////						tex.Case = 2;
////						tex.execute();
////		                exitTime = System.currentTimeMillis();
////					}
////					}
////				}
////			});
//			
//			mListView.setOnPullDownListener(new OnPullDownListener(){
//				/**更多事件接口  这里要注意的是获取更多完 要关闭 更多的进度条 notifyDidMore()**/
//
//				@Override
//				public void onRefresh() {
//					// TODO Auto-generated method stub
////					GlobalID globalID = ((GlobalID)getApplication());
////					if(globalID.getNow_ark().equals("-1")){
////		    			String strInfo = String.format("没有船号信息");
////						if(globalID.toast != null)globalID.toast.cancel();
////						globalID.toast = Toast.makeText(Text_information.this, strInfo, Toast.LENGTH_LONG);
////						globalID.toast.show();
////						((MsgListView) getListView()).onRefreshComplete();
////					}
////					else{
//					if((System.currentTimeMillis()-exitTime) < 2000){
//						((MsgListView) getListView()).onRefreshComplete();
//		            }
//					else{
//						refresh();
//					}
////					}
//				}
//
//				@Override
//				public void onMore() {
////					GlobalID globalID = ((GlobalID)getApplication());
////					if(globalID.getNow_ark().equals("-1")){
////		    			String strInfo = String.format("没有船号信息");
////						if(globalID.toast != null)globalID.toast.cancel();
////						globalID.toast = Toast.makeText(Text_information.this, strInfo, Toast.LENGTH_LONG);
////						globalID.toast.show();
////						((MsgListView) getListView()).onMoreComplete();
////					}
////					else{
//					if((System.currentTimeMillis()-exitTime) < 2000){
//						((MsgListView) getListView()).onMoreComplete();
//		            }
//					else{
//						GetDataTask tex = new GetDataTask();
//						if(!globalID.MsgDataArrays.isEmpty()){
//							tex.endTime = globalID.MsgDataArrays.get(globalID.MsgDataArrays.size()-1).getTest_PostTime();
//							tex.setMsg_id = "< " + globalID.MsgDataArrays.get(globalID.MsgDataArrays.size()-1).getMsg_id();
////							Log.v("refresh", "startTime = "+ globalID.MsgDeleteArrays.get(0).getImage_datetime_send());
//						}
//						else {
//							tex.endTime = currentStart;
//							tex.setMsg_id = "!= -1";
//						}
////						Calendar eDate = Calendar.getInstance();
//						tex.startTime = "0-0-0";
////						tex.now_ark_id = currentArk;
//						tex.line = currentLine;
//						tex.Case = 3;
//						tex.execute();
//						tex.Insert_Head = false;
//						tex.DESC = "DESC";
//						exitTime = System.currentTimeMillis();
//					}
//				}
////				}
//			});
//		}
//		
//		public void onOtherResume(){
//			super.onResume();
//			Log.v("text","onOtherResume");
//			GlobalID globalID = ((GlobalID)getApplication());
//			if(globalID.isMsg_push_UnGet()){
//				Log.v(TAG,"isMsg_push_UnGet");
//				refresh();
//				globalID.setMsg_push_UnGet(false);
//			}
//		}
//		
//		public void refresh(){
//			GlobalID globalID = ((GlobalID)getApplication());
//
//			GetDataTask tex = new GetDataTask();
//			if(!globalID.MsgDataArrays.isEmpty()){
//				tex.startTime = globalID.MsgDataArrays.get(0).getTest_PostTime();
//				tex.setMsg_id = "> " + globalID.MsgDataArrays.get(0).getMsg_id();
////				Log.v("refresh", "startTime = "+ globalID.MsgDeleteArrays.get(0).getImage_datetime_send());
//			}
//			else {
//				tex.startTime = currentStart;
//				tex.setMsg_id = "!= -1";
//			}
//			Calendar eDate = Calendar.getInstance();
//			tex.endTime = eDate.get(Calendar.YEAR)+"-"+(eDate.get(Calendar.MONTH)+1)+"-"+eDate.get(Calendar.DATE)
//					+ " " + eDate.get(Calendar.HOUR)+":"+eDate.get(Calendar.MINUTE)+":"+eDate.get(Calendar.SECOND);
////			tex.now_ark_id = currentArk;
//			tex.line = currentLine;
//			tex.Case = 2;
//			tex.execute();
//			tex.Insert_Head = true;
//			tex.DESC = "ASC";
//            exitTime = System.currentTimeMillis();
//		}
//		
//		@Override
//		protected void onResume(){
//			super.onResume();
//			Log.v("text","onResume");
//			//refresh headView...not a good idea  and no use in ViewPager
//			mListView.headView.setPadding(0, -1 * mListView.headContentHeight, 0, 0);
//			GlobalID globalID = ((GlobalID)getApplication());
//			if(globalID.isMsg_push_UnGet()){
//				Log.v(TAG,"isMsg_push_UnGet");
//				pd = ProgressDialog.show(Text_information.this, "连接", "正在获取数据…");
//				refresh();
//				globalID.setMsg_push_UnGet(false);
//				return;
//			}
//			if(globalID.isMsg_change()){
//				globalID.setMsg_change(false);
//	                exitTime = System.currentTimeMillis();
////				if(globalID.getNow_ark().equals("-1")){
////	    			String strInfo = String.format("没有船号信息");
////					if(globalID.toast != null)globalID.toast.cancel();
////					globalID.toast = Toast.makeText(Text_information.this, strInfo, Toast.LENGTH_LONG);
////					globalID.toast.show();
////				}
////				else{
//				try{
//					if(currentStart != globalID.getStartDate()||currentEnd != globalID.getEndDate()
////							||currentArk != globalID.getNow_ark()
//							||currentLine != globalID.getLine()){
//						currentStart = globalID.getStartDate();
//						currentEnd = globalID.getEndDate();
////						currentArk = globalID.getNow_ark();
//						currentLine = globalID.getLine();
//						
//						globalID.MsgDataArrays.removeAll(globalID.MsgDataArrays);
////						globalID.MsgDeleteArrays.removeAll(globalID.MsgDeleteArrays);
//						Calendar eDate = Calendar.getInstance();
//						GetDataTask tex = new GetDataTask();
//						tex.startTime = currentStart;
//						tex.endTime = currentEnd+ " " + eDate.get(Calendar.HOUR)+":"+eDate.get(Calendar.MINUTE)+":"+eDate.get(Calendar.SECOND);
////						tex.now_ark_id = currentArk;
//						tex.line = currentLine;
//						tex.Case = 1;
//						tex.Insert_Head = false;
//						tex.setMsg_id = "!= -1";
//						tex.DESC = "DESC";
//						
//						pd = ProgressDialog.show(Text_information.this, "连接", "正在获取数据…");
//						tex.execute();
//					}
//				}catch(Exception e){
//					Log.v("tex","Exception" + e);
//				}
////				}
//			
//			}
//			return;
//		}
//		
//		private class GetDataTask extends AsyncTask<Void , Void , Void>{
//
//			public String startTime;
//			public String endTime;
////			public String now_ark_id;
//			public String user_id;
//			public int line;
//			public int Case;
//			public boolean Insert_Head;
//			public String setMsg_id;
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
//					user_id = globalID.getID();
////					if(currentStart != globalID.getStartDate()||currentEnd != globalID.getEndDate()){
////						currentStart = globalID.getStartDate();
////						currentEnd = globalID.getEndDate();
////	        			globalID.MsgDataArrays.removeAll(globalID.MsgDataArrays);
////						globalID.MsgDeleteArrays.removeAll(globalID.MsgDeleteArrays);
////					}
//					
//					Socket socket = new Socket();
//					try {
//						Log.v("LAST3", "RIGHT: "+String.valueOf(2)); 
//						StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//						.detectDiskReads().detectDiskWrites().detectNetwork()
//						.penaltyLog().build());
//						
//						StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//						.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
//						.build());
//						
////						socket = new Socket(serverAddr,SERVERPORT);
//						InetSocketAddress socketAdd = new InetSocketAddress(serverAddr, SERVERPORT);
//						socket.connect(socketAdd, globalID.getTIMEOUT());
//						Log.v("LAST3", "RIGHT: "+String.valueOf(1)); 
//				        try {
//				            PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
//				            Log.v("LAST2", "RIGHT: ");
//				            out.println(8);
////				            out.println(currentStart);
////				            out.println(currentEnd);
//				            out.println(startTime);
//				            out.println(endTime);
//				            out.println(user_id);
//				            out.println(line);
//				            out.println(setMsg_id);
//				            out.println(DESC);
//				        } catch(Exception e) {
//				                Log.e("err", "S: Error", e);
//				        }
//				        finally {
//				            socket.setSoTimeout(globalID.getTIMEOUT());
//				        	 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GB2312"));
//				        	 String line = reader.readLine();
//				        	 if(parseJson(line,Insert_Head)){
////				        		 if(!globalID.MsgDeleteArrays.isEmpty())globalID.MsgDeleteArrays.removeAll(globalID.MsgDeleteArrays);
////				        		 Log.v("err", "S: Error");
////				        		 for(int j = 0; j < globalID.MsgDataArrays.size() ; j++){
////				        			 TestEntity bentity = new TestEntity();
////				        			 bentity = globalID.MsgDataArrays.get(globalID.MsgDataArrays.size()-j-1);
////				        			 globalID.MsgDeleteArrays.add(bentity);
////				        		 }
//				        		 if(msg != null) msg = new Message();
//				                 msg.what = Case;
//				                 mhandler.sendMessage(msg);
//				        	 }
//				        	 else{
//				        		 if(msg != null) msg = new Message();
//				                 msg.what = 4;
//				                 mhandler.sendMessage(msg);
//				        	 }
//				        }
//					} catch (IOException e1) {
//						if(msg != null) msg = new Message();
//			            msg.what = 0;
//			            mhandler.sendMessage(msg);
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}  
//				} catch (UnknownHostException e) {
//					if(msg != null) msg = new Message();
//		            msg.what = 0;
//		            mhandler.sendMessage(msg);
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return null;
//			}
//			@Override
//			protected void onPostExecute(Void result){
//				final GlobalID globalID = ((GlobalID)getApplication());
//				globalID.mpAdapter.notifyDataSetChanged();
//				mAdapter.notifyDataSetChanged();
//				Log.v(TAG,"get date finish");
//				((MsgListView) getListView()).onRefreshComplete();
//				((MsgListView) getListView()).onMoreComplete();
//				super.onPostExecute(result);
//			}
//			
//		}
//		
//	    /***********Json******************/
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
////							TestEntity entity = new TestEntity();
////							JSONObject jsonObject = jsonArray.getJSONObject(i);
////							entity.setMsg_id(jsonObject.optString("msg_id"));
////							entity.setTest_detail(jsonObject.optString(TAG));
////							entity.setTest_PostTime(jsonObject.optString("datetime_send"));
////							entity.setTest_ark_id(jsonObject.optString("ark_id"));
////							entity.setSend_user_id(jsonObject.optString("user_id"));
////							
//////							if(!insert){
//////								for(int j = 0 ;  ; j++){
//////									Log.v("bus","json1 " + j);
//////									if(j > globalID.MsgDataArrays.size()-1){
//////										insert = true;
//////										break;
//////									}
//////									else{
//////										if(entity.getTest_detail().equals(globalID.MsgDataArrays.get(j).getTest_detail())
//////												&& entity.getTest_PostTime().equals(globalID.MsgDataArrays.get(j).getTest_PostTime())
//////												&& entity.getSend_user_id().equals(globalID.MsgDataArrays.get(j).getSend_user_id())
//////												&& entity.getTest_ark_id().equals(globalID.MsgDataArrays.get(j).getTest_ark_id())){
//////											break;
//////										}
//////									}
//////								}
//////							}
//////							
//////							if(insert){
////								globalID.MsgDataArrays.add(0,entity);
////								updateLines++;
//////								insert = false;
//////							}
////						}
////					}
////					else{
//						for(int i = jsonArray.length()-1 ; i > -1  ; i--){
//							insert = false;
//							TestEntity entity = new TestEntity();
//							JSONObject jsonObject = jsonArray.getJSONObject(i);
//							entity.setMsg_id(jsonObject.optString("msg_id"));
//							entity.setTest_detail(jsonObject.optString("msg"));
//							entity.setTest_PostTime(jsonObject.optString("datetime_send"));
//							entity.setTest_ark_id(jsonObject.optString("ark_id"));
//							entity.setSend_user_id(jsonObject.optString("user_id"));
//							
////							if(!insert){
////								for(int j = 0 ;  ; j++){
////									Log.v("bus","json1 " + j);
////									if(j > globalID.MsgDataArrays.size()-1){
////										insert = true;
////										break;
////									}
////									else{
////										if(entity.getTest_detail().equals(globalID.MsgDataArrays.get(j).getTest_detail())
////												&& entity.getTest_PostTime().equals(globalID.MsgDataArrays.get(j).getTest_PostTime())
////												&& entity.getSend_user_id().equals(globalID.MsgDataArrays.get(j).getSend_user_id())
////												&& entity.getTest_ark_id().equals(globalID.MsgDataArrays.get(j).getTest_ark_id())){
////											break;
////										}
////									}
////								}
////							}
////							
////							if(insert){
//							for(int j = globalID.MsgDataArrays.size()-1 ; j>-1 ; j--){
//								if(entity.getMsg_id().equals(globalID.MsgDataArrays.get(j).getMsg_id())){
//									insert = true;
//									break;
//								}
//							}
//							for(int j = globalID.MsgDeleteArrays.size()-1 ; j>-1 ; j--){
//								if(entity.getMsg_id().equals(globalID.MsgDeleteArrays.get(j).getMsg_id())){
//									insert = true;
//									break;
//								}
//							}
//							if(insert)continue;
//							if(Insert_Head)globalID.MsgDataArrays.add(0,entity);
//							else globalID.MsgDataArrays.add(entity);
//								updateLines++;
////								insert = false;
////							}
////						}
//					}
//				}
//				
//			}catch(JSONException e){
//				e.printStackTrace();
//			}
////			currentStart = globalID.MsgDataArrays.get(globalID.MsgDataArrays.size()-1).getTest_PostTime();
////   		    Log.v("err",currentEnd);
//			return true;
//		}
//	    /***********Json******************/
//	    
//	    void toast(String msg){
//			GlobalID globalID = ((GlobalID)getApplication());
//			if(pd!=null)pd.dismiss();
//			if(globalID.toast != null)globalID.toast.cancel();
//			globalID.toast = Toast.makeText(Text_information.this, msg, Toast.LENGTH_LONG);
//			globalID.toast.show();
//	    }
//	    
//	    @Override 
//	    public void onSaveInstanceState(Bundle savedInstanceState) {  
//	    	// Save away the original text, so we still have it if the activity   
//	    	// needs to be killed while paused.
//			GlobalID globalID = ((GlobalID)getApplication());
//	    	savedInstanceState.putString("MsgDataArrays", globalID.Msg2String());
//	    	super.onSaveInstanceState(savedInstanceState);  
//	    	Log.e(TAG, "onSaveInstanceState");  
//	    	}  
//	    
//	    @Override  
//	    public void onRestoreInstanceState(Bundle savedInstanceState) {  
//	    	super.onRestoreInstanceState(savedInstanceState);
//	    	String StrTest = savedInstanceState.getString("MsgDataArrays"); 
//	    	Log.e(TAG, "onRestoreInstanceState + MsgDataArrays = "+ StrTest);  
//	    	}  
//}
