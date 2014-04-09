package com.example.main_old_or_out;
//package com.example.main;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.io.UnsupportedEncodingException;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.Socket;
//import java.net.SocketException;
//import java.net.UnknownHostException;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.example.main_Adapter.SendPreAdapter;
//import com.example.main_Adapter.TalkAdapter;
//import com.example.main_Entity.SendPreEntity;
//import com.example.main_util.LogHelper;
//import com.example.main_util.SpUtil;
//import com.example.main_util.StringUtil;
//
//import android.app.ListActivity;
//import android.app.TabActivity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.AdapterView.OnItemClickListener;
//
//public class ImgTalkPre extends ListActivity{
//
//	private static boolean log = false;
//	private String TAG = "ImgTalkPre";
//	
//	private EditText et_pre_send;
//	private ListView mListView;
//	private Button btn_pre_send_delete;
//	private SendPreAdapter mAdapter;
//	private List<SendPreEntity> mDataArrays = new ArrayList<SendPreEntity>();
//	
//	Message msg = new Message();
//	final Handler mhandler = new Handler(){
//		@Override
//		public void  handleMessage (Message msg){
//			LogHelper.trace(TAG, "handleMessage");
//			 search_ark(et_pre_send.getText().toString());
//		}
//	};
//	
//	 @Override
//	 public void onCreate(Bundle savedInstanceState) {
//	        super.onCreate(savedInstanceState);
//
//			final GlobalID globalID = ((GlobalID)getApplication());
//			
//			if(null != savedInstanceState){
//				LogHelper.trace(TAG, "null != savedInstanceState");
////				String decode = savedInstanceState.getString("All"); 
////				Log.e(TAG, "onCreate get the savedInstanceState + All = "+decode);
////				globalID.setAll(decode);
//				}
//			
//			//set no title
//			requestWindowFeature(Window.FEATURE_NO_TITLE);
//			
//			setContentView(R.layout.send_pre);
//			if(log)Log.v(TAG,"onCreate");
//			mListView = (ListView)findViewById(android.R.id.list);
//			btn_pre_send_delete = (Button)findViewById(R.id.btn_pre_send_delete);
//			et_pre_send = (EditText)findViewById(R.id.et_pre_send);
//			
//			et_pre_send.addTextChangedListener(new TextWatcher() {
//				@Override
//				public void onTextChanged(CharSequence s, int start, int before, int count) {
//					
//				}
//				@Override
//				public void beforeTextChanged(CharSequence s, int start, int count,
//						int after) {
//					
//				}
//				@Override
//				public void afterTextChanged(Editable s) {
//					if(s.length() > 0){
//						btn_pre_send_delete.setVisibility(View.VISIBLE);
//					}else{
//						btn_pre_send_delete.setVisibility(View.GONE);
//					}
//					search_ark(et_pre_send.getText().toString());
//				}
//			});
//			
//			btn_pre_send_delete.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					et_pre_send.setText("");
//				}
//			});
//			
////			btn_pre_send_Cancel.setOnClickListener(new OnClickListener() {
////				
////				@Override
////				public void onClick(View arg0) {
////					// TODO Auto-generated method stub
////					MsgTalkPre.this.finish();
////				}
////			});
//
//			mAdapter = new SendPreAdapter(this,mDataArrays);
//	        mListView.setAdapter(mAdapter);
//	        
//	        mListView.setOnItemClickListener(new OnItemClickListener(){
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//		                long arg3) {
//					globalID.un_stop = true;
//					
//					//set data to inner activity					
//					Bundle bundle = new Bundle();
//					bundle.putInt("ark_num", arg2);
//					if(log)Log.v(TAG,"arg2:"+arg2);
////					Intent intent = new Intent(SendPre.this,SendActivity.class);
//					Intent intent = new Intent(ImgTalkPre.this,ImgTalk.class);
//					
//					//post data
//					intent.putExtras(bundle);
//		            startActivity(intent);
//			}
//			});
//	        
////	        search_ark(et_pre_send.getText().toString());
////			if(log)Log.v(TAG,"onCreate finish");
//	        
//	        Thread check_thread = new Thread(){
//	        	public void run(){
//	        		if(globalID.isAll_change()){
//	        			search_ark(et_pre_send.getText().toString());
//	        		} 
//	        		else
//						try {
//							sleep( 10 * StringUtil.M_RATE);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//	        	}
//	        };
//	        check_thread.start();
//	    }
//	 
//	 @Override
//	 public void onResume(){
//		 super.onResume();
//		 if(log)Log.v(TAG,"onResume");
////		 GlobalID globalID = ((GlobalID)getApplication());
////		 globalID.un_stop = false;
////		 globalID.cancel_notification();
//////		 if(globalID.is_sended){
//////			 globalID.is_sended = false;
//////			 this.finish();
//////			 return;
//////		 }
////		 
////		 if(globalID.getAll().size() == 0){
////			 Thread set_Thread = new Thread(){
////				 public void run(){
////					 if(setArk_all()){
////						 if(log)Log.v("login", "setAll success!");
////						 msg.what = 0;
////						 mhandler.sendMessage(msg);
////						 }
////					 }
////			 };
////			 set_Thread.start();
////		 }
////		 search_ark(et_pre_send.getText().toString());
////		 if(log)Log.v(TAG,"onResume finish");
//	 }
//	 
////	 private boolean setArk_all() {
////		// TODO Auto-generated method stub
////		 GlobalID globalID = ((GlobalID)getApplication());
////		 Socket socket = new Socket();
////		 String url = globalID.getUrlServer();
////		 InetAddress serverAddr = null;
////		try {
////			serverAddr = InetAddress.getByName(url);
////		} catch (UnknownHostException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////			if(log)Log.v(TAG,"onResume UnknownHostException "+e);
////		}
////		 int SERVERPORT = globalID.getSERVERPORT();
////		 InetSocketAddress socketAdd = new InetSocketAddress(serverAddr, SERVERPORT);
////		 try {
////			socket.connect(socketAdd, globalID.getTIMEOUT());
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////			if(log)Log.v(TAG,"onResume IOException "+e);
////		}
////
////		PrintWriter out = null;
////		try {
////			out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
////		} catch (IOException e2) {
////			// TODO Auto-generated catch block
////			e2.printStackTrace();
////			if(log)Log.v(TAG,"onResume IOException "+e2);
////		}
////		 out.flush();
////		 out.println(4);
////		 out.println(globalID.getID());
////		 
////		 try {
////			socket.setSoTimeout(globalID.getTIMEOUT());
////		} catch (SocketException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////			if(log)Log.v(TAG,"onResume SocketException "+e);
////		}
////		 BufferedReader reader2 = null;
////		try {
////			reader2 = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GB2312"));
////		} catch (UnsupportedEncodingException e1) {
////			// TODO Auto-generated catch block
////			e1.printStackTrace();
////			if(log)Log.v(TAG,"onResume UnsupportedEncodingException "+e1);
////		} catch (IOException e1) {
////			// TODO Auto-generated catch block
////			e1.printStackTrace();
////			if(log)Log.v(TAG,"onResume IOException "+e1);
////		}
////		 String line = null;
////		try {
////			line = reader2.readLine();
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////			if(log)Log.v(TAG,"onResume IOException "+e);
////		}
//////		 parseJson2(line);
////		
////		 return globalID.setAll(line);
////	}
//
//	 @Override
//	 public void onBackPressed() {
//		 //实现Home键效果 
//		 //super.onBackPressed();这句话一定要注掉,不然又去调用默认的back处理方式了 
//		 Intent intent= new Intent(Intent.ACTION_MAIN); 
//		 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
//		 intent.addCategory(Intent.CATEGORY_HOME); 
//		 startActivity(intent);  
//		 if(log)Log.v(TAG,"onBackPressed()");
//	 }
//	 
//	@Override
//	 public void finish(){
//		 super.finish();
//		 if(log)Log.v(TAG,"finish");
//		 GlobalID globalID = ((GlobalID)getApplication());
//		 globalID.un_stop = true;
//	 }
//	 
//	 @Override
//	 public void onStop(){
//	    super.onStop();
////		GlobalID globalID = ((GlobalID)getApplication());
////	    if(globalID.un_stop)return;
////	    else{
////	    	globalID.setCurrent_code(4);
////	    	globalID.create_notification("后台接收数据", "后台运行", "岸客户端", false, false, false,SendPre.class.getName());
////			if(globalID.toast != null)globalID.toast.cancel();
////			globalID.clear();
////		   	MainActivity.this.finish();
////	    }
//	    if(log)Log.v(TAG,"stop");
//	}
//	 @Override
//	    public void onPause(){
//	    	super.onPause();
//	    	if(log)Log.v(TAG,"onPause");
////	    	GlobalID globalID = ((GlobalID)getApplication());
////		    if(globalID.un_stop)return;
////		    else{
//////		    	globalID.setCurrent_code(4);
////		    	globalID.create_notification("后台接收数据", "后台运行", "岸客户端", false, false, false,MsgTalkPre.class.getName());
////				if(globalID.toast != null)globalID.toast.cancel();
////				globalID.clear();
////		    }
//	    }
//	 
//
//		private void search_ark(String search) {
//			// TODO Auto-generated method stub
//			mDataArrays.clear();
//			mAdapter.notifyDataSetChanged();
//	    	GlobalID globalID = ((GlobalID)getApplication());
////	    	globalID.mpAdapter.notifyDataSetChanged();
//			for(int i = 0;i<globalID.getAll().size();i++){
////				ark_id[i] = globalID.getAll().get(i).getArk_id()+": "+globalID.getAll().get(i).getArk_no();
//				SendPreEntity entity = new SendPreEntity();
//				entity.setSendPre_detail(globalID.getAll().get(i).getArk_no());
//				Bitmap hv  = BitmapFactory.decodeResource(getResources(),R.drawable.send_pre_head);
//				entity.setSendPre_picture(hv);
//				if(globalID.getAll().get(i).getArk_no().contains(search)){
//					mDataArrays.add(entity);
//					mAdapter.notifyDataSetChanged();
////					if(log)Log.v(TAG+" search_ark","get id");
//					}
//			}
////			globalID.mpAdapter.notifyDataSetChanged();
//			if(log)Log.v(TAG+" search_ark","globalID.mpAdapter.notifyDataSetChanged()");
//	        if(globalID.getAll().size() == 0){
//				if(globalID.toast != null)globalID.toast.cancel();
//				globalID.toast = Toast.makeText(ImgTalkPre.this, "没有船号信息", Toast.LENGTH_LONG);
//				globalID.toast.show();
//			}
//		}
//		
//		@Override 
//	    public void onSaveInstanceState(Bundle savedInstanceState) {  
//	    	// Save away the original text, so we still have it if the activity   
//	    	// needs to be killed while paused.
////			GlobalID globalID = ((GlobalID)getApplication());
////	    	savedInstanceState.putBoolean("Bus_change", globalID.isBus_change());
////	    	savedInstanceState.putBoolean("Msg_change", globalID.isMsg_change());
////	    	savedInstanceState.putBoolean("Img_change", globalID.isImg_change());
////	    	
////	    	savedInstanceState.putString("All", globalID.Ark_line2String());
////	    	savedInstanceState.putInt("Now_ark", globalID.getNow_ark());
////	    	super.onSaveInstanceState(savedInstanceState);  
//	    	LogHelper.trace(TAG, "onSaveInstanceState");  
////	    	if(log)Log.v(TAG, "Now_ark = "+ globalID.getNow_ark());
//	    	}  
//	    
//	    @Override  
//	    public void onRestoreInstanceState(Bundle savedInstanceState) {  
//	    	super.onRestoreInstanceState(savedInstanceState);
////			GlobalID globalID = ((GlobalID)getApplication());
////	    	globalID.setAll(savedInstanceState.getString("All"));
////	    	globalID.setNow_ark(savedInstanceState.getInt("Now_ark"));
////	    	if(log)Log.v(TAG, "Now_ark = "+ globalID.getNow_ark());
////			search_ark("");
//	    	LogHelper.trace(TAG, "onRestoreInstanceState");  
//	    	}  
//}
