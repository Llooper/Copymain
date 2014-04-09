package com.example.main_old_or_out;
//package com.example.main;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.MalformedURLException;
//import java.net.Socket;
//import java.net.URL;
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
//import com.example.main_Adapter.imageAdapter;
//import com.example.main_Entity.imageEntity;
//import com.example.main_Info.imageInfo;
//
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.app.ListActivity;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
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
//public class image_information extends ListActivity{
//	private String TAG = "img";
//
//	//set background_colour here
//	private String background_colour = "#99CCFF";
//	
//	InetAddress serverAddr;
//	
//	//test data
////	static int k = 0;
//	
//	private MsgListView mListView ;
//	private imageAdapter mAdapter;
//
//	private String currentStart = "2013-01-01 00:00:00";
//	private String currentEnd = "3000-12-31 23:59:59";
////	private String currentArk = "";
//	private long exitTime = 0;
//	private int currentLine = 5;
//	private int updateLines = 0;
//	
////	private List<imageEntity> globalID.ImgDataArrays = new ArrayList<imageEntity>();
////	private List<imageEntity> globalID.ImgDataArrays2 = new ArrayList<imageEntity>();
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
//					toast("该时段最近"+ updateLines +"条图片信息");
//					break;
//				}
//			case 2:
////				Log.v("mhandler", "case 2");
//				if(updateLines!=0){
//					toast("刷新"+ updateLines +"条图片信息");
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
//				toast("没有找到图片信息");
//				break;
//			case 5:
//				mAdapter.notifyDataSetChanged();
//				GlobalID globalID = ((GlobalID)getApplication());
//				globalID.mpAdapter.notifyDataSetChanged();
//				break;
//			}
//		}
//	};
//		@Override 
//		public void onCreate(Bundle savedInstanceState) { 
//		super.onCreate(savedInstanceState); 
//		 
//		if(null != savedInstanceState){
//			final GlobalID globalID = ((GlobalID)getApplication());
//			final String decode = savedInstanceState.getString("ImgDataArrays"); 
//			Log.e(TAG, "onCreate get the savedInstanceState + ImgDataArrays = "+decode);
//			Thread getImg = new Thread(){
//				public void run(){
//					globalID.setImgDataArrays(decode);
//					if(msg!=null)msg = new Message();
//					msg.what = 5;
//					mhandler.sendMessage(msg);
//				}
//			};
//			getImg.start();
//			} 
//		//set no title
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.msglistview);
//
//		//set Adapter for ListView
//		initView();
//		
//		final GlobalID globalID = ((GlobalID)getApplication());
//		Log.v(TAG,"onCreate");
//		mListView = (MsgListView)findViewById(android.R.id.list);
//		mListView.setOnItemClickListener(new OnItemClickListener(){
//			@Override
//			@SuppressLint("ShowToast")
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//	                long arg3) {
//
//				globalID.un_stop = true;
//				//set data to inner activity
//				String ark_id = globalID.ImgDataArrays.get(arg2-1).getImage_ark_id();
//				String Image_datetime_send = globalID.ImgDataArrays.get(arg2-1).getImage_datetime_send();
//				String send_user_id = globalID.ImgDataArrays.get(arg2-1).getSend_user_id();
//				
//				Bundle bundle = new Bundle();
//				//set picture
//				Bitmap Image_picture = globalID.ImgDataArrays.get(arg2-1).getImage_picture();
//				if(Image_picture==null){
//					bundle.putByteArray("Image_picture", null);
//					}
//				else{
//					ByteArrayOutputStream image_picture = new ByteArrayOutputStream();
//					Image_picture.compress(Bitmap.CompressFormat.PNG, 100, image_picture);
//					bundle.putByteArray("Image_picture", image_picture.toByteArray());
//					
//				}
//				
////				Log.v(TAG, String.valueOf(globalID.ImgDataArrays.size()));
//				bundle.putString("ark_id", ark_id);
//				bundle.putString("Image_datetime_send", Image_datetime_send);
//				bundle.putString("send_user_id", send_user_id);
//				bundle.putString("colour", background_colour);
//				
//				Intent intent = new Intent(image_information.this,imageInfo.class);
//				
//				//post data
//				intent.putExtras(bundle);
//	            startActivity(intent);
////				Toast.makeText(image_information.this, "arg2"+String.valueOf(arg2), Toast.LENGTH_LONG);
////				Log.v("arg2", "arg2"+String.valueOf(arg2));
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
//				new AlertDialog.Builder(image_information.this).  
//				setTitle("删除信息").setMessage("确定删除？").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						globalID.ImgDeleteArrays.add(globalID.ImgDataArrays.get(arg2-1));
//						globalID.ImgDataArrays.remove(arg2-1);
//						mAdapter.notifyDataSetChanged();
//						GlobalID globalID = ((GlobalID)getApplication());
//						if(globalID.toast != null)globalID.toast.cancel();
//						globalID.toast = Toast.makeText(image_information.this, "删除一条图片信息", Toast.LENGTH_LONG);
//						globalID.toast.show();
//						}
//					}).show();
//				
//				return true;
//			}
//		});
//		
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
////			mListView.setBackgroundColor(android.graphics.Color.parseColor(background_colour));
//			mListView.setBackgroundResource(R.drawable.text_background);
//
//			final GlobalID globalID = ((GlobalID)getApplication());
//			
//			mAdapter = new imageAdapter(this,globalID.ImgDataArrays);
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
////						globalID.toast = Toast.makeText(image_information.this, strInfo, Toast.LENGTH_LONG);
////						globalID.toast.show();
////						((MsgListView) getListView()).onRefreshComplete();
////					}
////					else{
////					if((System.currentTimeMillis()-exitTime) < 2000){
////						((MsgListView) getListView()).onRefreshComplete();
////		            }
////					else{
////						GetDataTask img = new GetDataTask();
////						if(!globalID.ImgDataArrays2.isEmpty()){
////							img.startTime = globalID.ImgDataArrays2.get(0).getImage_datetime_send();
//////							Log.v("refresh", "startTime = "+ globalID.ImgDataArrays2.get(0).getImage_datetime_send());
////						}
////						else img.startTime = currentStart;
////						Calendar eDate = Calendar.getInstance();
////						img.endTime = eDate.get(Calendar.YEAR)+"-"+(eDate.get(Calendar.MONTH)+1)+"-"+eDate.get(Calendar.DATE)
////								+" "+eDate.get(Calendar.HOUR)+":"+eDate.get(Calendar.MINUTE)+":"+eDate.get(Calendar.SECOND);
////						img.now_ark_id = currentArk;
////						img.execute();
////						img.line = currentLine;
////						img.Case = 2;
////		                exitTime = System.currentTimeMillis();
////					}
////					}
////				}
////			});
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
////						globalID.toast = Toast.makeText(image_information.this, strInfo, Toast.LENGTH_LONG);
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
////						String strInfo = String.format("没有船号信息");
////						if(globalID.toast != null)globalID.toast.cancel();
////						globalID.toast = Toast.makeText(image_information.this, strInfo, Toast.LENGTH_LONG);
////						globalID.toast.show();
////						((MsgListView) getListView()).onMoreComplete();
////					}
////				else{
//					if((System.currentTimeMillis()-exitTime) < 2000){
//						((MsgListView) getListView()).onMoreComplete();
//		            }
//					else{
//						GetDataTask img = new GetDataTask();
//						if(!globalID.ImgDataArrays.isEmpty()){
//							img.endTime = globalID.ImgDataArrays.get(globalID.ImgDataArrays.size()-1).getImage_datetime_send();
//							img.setImg_id = "< " + globalID.ImgDataArrays.get(globalID.ImgDataArrays.size()-1).getImg_id();
////							Log.v("refresh", "startTime = "+ globalID.ImgDataArrays2.get(0).getImage_datetime_send());
//						}
//						else {
//							img.endTime = currentStart;
//							img.setImg_id = "!= -1";
//						}
//						img.startTime = "0-0-0";
////						img.now_ark_id = currentArk;
//						img.execute();
//						img.line = currentLine;
//						img.Insert_Head = false;
//						img.Case = 3;
//						img.DESC = "DESC";
//						exitTime = System.currentTimeMillis();
//					}
//				}
////				}
//			});
//		}
//		
//		public void onOtherResume(){
//			super.onResume();
//			Log.v(TAG,"onOtherResume");
//			GlobalID globalID = ((GlobalID)getApplication());
//			if(globalID.isImg_push_UnGet()){
//				Log.v(TAG,"isImg_push_UnGet");
//				refresh();
//				globalID.setImg_push_UnGet(false);
//			}
//		}
//		
//		public void refresh(){
//			GlobalID globalID = ((GlobalID)getApplication());
//			GetDataTask img = new GetDataTask();
//			if(!globalID.ImgDataArrays.isEmpty()){
//				img.startTime = globalID.ImgDataArrays.get(0).getImage_datetime_send();
//				img.setImg_id = "> " + globalID.ImgDataArrays.get(0).getImg_id();
////				Log.v("refresh", "startTime = "+ globalID.ImgDataArrays2.get(0).getImage_datetime_send());
//			}
//			else {
//				img.startTime = currentStart;
//				img.setImg_id = "!= -1";
//			}
//			Calendar eDate = Calendar.getInstance();
//			img.endTime = eDate.get(Calendar.YEAR)+"-"+(eDate.get(Calendar.MONTH)+1)+"-"+eDate.get(Calendar.DATE)
//					+" "+eDate.get(Calendar.HOUR)+":"+eDate.get(Calendar.MINUTE)+":"+eDate.get(Calendar.SECOND);
////			img.now_ark_id = currentArk;
//			img.execute();
//			img.line = currentLine;
//			img.Insert_Head = true;
//			img.Case = 2;
//			img.DESC = "ASC";
//            exitTime = System.currentTimeMillis();
//		}
//		
//		@Override
//		protected void onResume(){
//			super.onResume();
//			Log.v(TAG,"onResume");
//			mListView.headView.setPadding(0, -1 * mListView.headContentHeight, 0, 0);
//			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
//			GlobalID globalID = ((GlobalID)getApplication());
//			if(globalID.isImg_push_UnGet()){
//				Log.v(TAG,"isImg_push_UnGet");
//				pd = ProgressDialog.show(image_information.this, "连接", "正在获取数据…");
//				refresh();
//				globalID.setImg_push_UnGet(false);
//				return;
//			}
//			
//			if(globalID.isImg_change()){
//				globalID.setImg_change(false);
//				if((System.currentTimeMillis()-exitTime) < 2000){
//					return;
//	            }
//				else{
//	                exitTime = System.currentTimeMillis();
//				//refresh headView...not a good idea
////				if(globalID.getNow_ark().equals("-1")){
////	    			String strInfo = String.format("没有船号信息");
////					if(globalID.toast != null)globalID.toast.cancel();
////					globalID.toast = Toast.makeText(image_information.this, strInfo, Toast.LENGTH_LONG);
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
//						globalID.ImgDataArrays.removeAll(globalID.ImgDataArrays);
////						globalID.ImgDataArrays2.removeAll(globalID.ImgDataArrays2);
//						Calendar eDate = Calendar.getInstance();
//						GetDataTask img = new GetDataTask();
//						img.startTime = currentStart;
//						img.endTime = currentEnd + " " + eDate.get(Calendar.HOUR)+":"+eDate.get(Calendar.MINUTE)+":"+eDate.get(Calendar.SECOND);
////						img.now_ark_id = currentArk;
//						img.line = currentLine;
//						img.Case = 1;
//						img.Insert_Head = false;
//						img.setImg_id = "!= -1";
//						img.DESC = "DESC";
//						
//						pd = ProgressDialog.show(image_information.this, "连接", "正在获取数据…");
//						img.execute();
//					}
//				}catch(Exception e){
//					Log.v(TAG,"Exception" + e);
//				}
//				}
//			}
//			
////			}
//			return;
//			
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
//			public String setImg_id;
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
//					
////					if(startTime != globalID.getStartDate()||endTime != globalID.getEndDate()){
////						startTime = globalID.getStartDate();
////						endTime = globalID.getEndDate();
////	        			globalID.ImgDataArrays.removeAll(globalID.ImgDataArrays);
////						globalID.ImgDataArrays2.removeAll(globalID.ImgDataArrays2);
////					}
//					
//					Socket socket = new Socket();
//					try {
//						/********************very important for android 4.0(and above) to connect internet************************/
//						StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//						.detectDiskReads().detectDiskWrites().detectNetwork()
//						.penaltyLog().build());
//						
//						StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//						.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
//						.build());
//						/********************very important for android 4.0(and above) to connect internet************************/
//						
////						socket = new Socket(serverAddr,SERVERPORT);
//						InetSocketAddress socketAdd = new InetSocketAddress(serverAddr, SERVERPORT);
//						socket.connect(socketAdd, globalID.getTIMEOUT());
//				        try {
//				            PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
//				            Log.v("LAST2", "RIGHT: ");
//				            out.println(9);
////				            out.println(startTime);
////				            out.println(endTime);
//				            out.println(startTime);
//				            out.println(endTime);
//				            out.println(user_id);
//				            out.println(line);
//				            out.println(setImg_id);
//				            out.println(DESC);
//
//				            socket.setSoTimeout(globalID.getTIMEOUT());
//				        	 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GB2312"));
//				        	 String line = reader.readLine();
//				        	 if(parseJson(line,Insert_Head)){
////				        		 if(!globalID.ImgDataArrays2.isEmpty())globalID.ImgDataArrays2.removeAll(globalID.ImgDataArrays2);
////				        		 Log.v("err", "S: Error");
////				        		 for(int j = 0; j < globalID.ImgDataArrays.size() ; j++){
////				        			 imageEntity bentity = new imageEntity();
////				        			 bentity = globalID.ImgDataArrays.get(globalID.ImgDataArrays.size()-j-1);
////				        			 globalID.ImgDataArrays2.add(bentity);
////				        		 }
//				        		 if(msg != null) msg = new Message();
//				                 msg.what = Case;
//				                 mhandler.sendMessage(msg);
////			        			 Log.v("err", globalID.ImgDataArrays2.get(0).getImage_datetime_send());
//				        	 }
//				        	 else{
//				        		 if(msg != null) msg = new Message();
//				                 msg.what = 4;
//				                 mhandler.sendMessage(msg);
//				        	 }
//				        } catch(Exception e) {
//				        	if(msg != null) msg = new Message();
//			                msg.what = 0;
//			                mhandler.sendMessage(msg);
//				                Log.e("err", "S: Error", e);
//				        }
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						if(msg != null) msg = new Message();
//		                msg.what = 0;
//		                mhandler.sendMessage(msg);
//						e1.printStackTrace();
//					}  
//				} catch (UnknownHostException e) {
//					// TODO Auto-generated catch block
//					if(msg != null) msg = new Message();
//	                msg.what = 0;
//	                mhandler.sendMessage(msg);
//					e.printStackTrace();
//				}
//				return null;
//			}
//			@Override
//			protected void onPostExecute(Void result){
//				final GlobalID globalID = ((GlobalID)getApplication());
//				globalID.mpAdapter.notifyDataSetChanged();
//				mAdapter.notifyDataSetChanged();
//				((MsgListView) getListView()).onRefreshComplete();
//				((MsgListView) getListView()).onMoreComplete();
//				super.onPostExecute(result);
//			}
//			
//		}
//		
//	    /***********Json for image list******************/
//	    private boolean parseJson(String resultObj,boolean Insert_Head){
//	    	boolean insert = false;
//	    	updateLines = 0;
//			try{
//				GlobalID globalID = ((GlobalID)getApplication());
//				JSONArray jsonArray = new JSONArray(resultObj);
//				if(jsonArray.length()==0){
//					return false;
//					}
//				else{
////					if(Insert_Head){
////						for(int i = 0 ; i < jsonArray.length() ; i++){
////							imageEntity entity = new imageEntity();
////							JSONObject jsonObject = jsonArray.getJSONObject(i);
////							entity.setImg_id(jsonObject.optString("img_id"));
////							entity.setImage_datetime_send(jsonObject.optString("datetime_send"));
////							
////							//set where to download picture
////							String pic = jsonObject.optString("img_name");
////							Bitmap bit = downloadPic("http://"+ globalID.getDBurl() +"/user/images" + pic);
////							Log.v("pic","http://"+ globalID.getDBurl() +"/user/images" + pic);
////							entity.setImage_picture(bit);
////
////							entity.setImage_ark_id(jsonObject.optString("ark_id"));
////							entity.setSend_user_id(jsonObject.optString("user_id"));
////							
//////							if(!insert){
//////								for(int j = 0 ;  ; j++){
//////									Log.v("bus","json1 " + j);
//////									if(j > globalID.ImgDataArrays.size()-1){
//////										insert = true;
//////										break;
//////									}
//////									else{
//////										if(entity.getImage_datetime_send().equals(globalID.ImgDataArrays.get(j).getImage_datetime_send())
//////												&& entity.getSend_user_id().equals(globalID.ImgDataArrays.get(j).getSend_user_id())
//////												&& entity.getImage_ark_id().equals(globalID.ImgDataArrays.get(j).getImage_ark_id())){
//////											break;
//////										}
//////									}
//////								}
//////							}
//////							
//////							if(insert){
////								globalID.ImgDataArrays.add(0,entity);
////								updateLines++;
//////								insert = false;
//////							}
////						}
////					}
////					else{
//						for(int i = jsonArray.length()-1 ; i > -1  ; i--){
//							insert = false;
//							imageEntity entity = new imageEntity();
//							JSONObject jsonObject = jsonArray.getJSONObject(i);
//							entity.setImg_id(jsonObject.optString("img_id"));
//							entity.setImage_datetime_send(jsonObject.optString("datetime_send"));
//							
//							//set where to download picture
//							String pic = jsonObject.optString("img_name");
//							entity.setPic(pic);
//							Bitmap bit = downloadPic("http://"+ globalID.getDBurl() +"/user/images" + pic);
//							Log.v("pic","http://"+ globalID.getDBurl() +"/user/images" + pic);
//							if(bit!=null){
//								entity.setImage_picture(bit);
//		  					}
//							else{
//	 	  		 				Bitmap good  = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
//								entity.setImage_picture(good);
//	 	  						}
//
//							entity.setImage_ark_id(jsonObject.optString("ark_id"));
//							entity.setSend_user_id(jsonObject.optString("user_id"));
//							
////							if(!insert){
////								for(int j = 0 ;  ; j++){
////									Log.v("bus","json1 " + j);
////									if(j > globalID.ImgDataArrays.size()-1){
////										insert = true;
////										break;
////									}
////									else{
////										if(entity.getImage_datetime_send().equals(globalID.ImgDataArrays.get(j).getImage_datetime_send())
////												&& entity.getSend_user_id().equals(globalID.ImgDataArrays.get(j).getSend_user_id())
////												&& entity.getImage_ark_id().equals(globalID.ImgDataArrays.get(j).getImage_ark_id())){
////											break;
////										}
////									}
////								}
////							}
////							
////							if(insert){
//							for(int j = globalID.ImgDataArrays.size()-1 ; j>-1 ; j--){
//								if(entity.getImg_id().equals(globalID.ImgDataArrays.get(j).getImg_id())){
//									insert = true;
//									break;
//								}
//							}
//							for(int j = globalID.ImgDeleteArrays.size()-1 ; j>-1 ; j--){
//								if(entity.getImg_id().equals(globalID.ImgDeleteArrays.get(j).getImg_id())){
//									insert = true;
//									break;
//								}
//							}
//							if(insert)continue;
//							if(Insert_Head)globalID.ImgDataArrays.add(0,entity);
//							else globalID.ImgDataArrays.add(entity);
//							updateLines++;
////								insert = false;
////							}
////						}
//					}
//				}
//				
//				
//			}catch(JSONException e){
//				e.printStackTrace();
//			}
////			startTime = globalID.ImgDataArrays.get(globalID.ImgDataArrays.size()-1).getImage_datetime_send();
////   		    Log.v("err",endTime);
//			return true;
//		}
//	    /***********Json for image list******************/
//	    
//	    /***********down picture from urlPic******************/
//	    private Bitmap downloadPic(String urlPic){
//			try {
//				URL url = new URL(urlPic);
//				InputStream is = url.openStream();
//				Bitmap bit = BitmapFactory.decodeStream(is);
//				return bit ;
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return null;
//		}
//	    /***********down picture from URL******************/
//	    
//	    void toast(String msg){
//			GlobalID globalID = ((GlobalID)getApplication());
//			if(pd!=null)pd.dismiss();
//			if(globalID.toast != null)globalID.toast.cancel();
//			globalID.toast = Toast.makeText(image_information.this, msg, Toast.LENGTH_LONG);
//			globalID.toast.show();
//	    }
//	    
//	    @Override 
//	    public void onSaveInstanceState(Bundle savedInstanceState) {  
//	    	// Save away the original text, so we still have it if the activity   
//	    	// needs to be killed while paused.
//			GlobalID globalID = ((GlobalID)getApplication());
//	    	savedInstanceState.putString("ImgDataArrays", globalID.Img2String());
//	    	super.onSaveInstanceState(savedInstanceState);  
//	    	Log.e(TAG, "onSaveInstanceState");  
//	    	}  
//	    
//	    @Override  
//	    public void onRestoreInstanceState(Bundle savedInstanceState) {  
//	    	super.onRestoreInstanceState(savedInstanceState);
//	    	String StrTest = savedInstanceState.getString("ImgDataArrays"); 
//	    	Log.e(TAG, "onRestoreInstanceState + ImgDataArrays = "+ StrTest);  
//	    	}  
//
//	    @Override
//	    public void onStop(){
//	    	super.onStop();
//			overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
//	    }
//}
