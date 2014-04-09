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
import java.util.Calendar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.main.SettingActivity.MyGestureDetector;
import com.example.main_util.FuntionUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

public class register2 extends Activity {
	
	//define data for button and editText
	private static Button btn_register2_register;
	private static EditText et_register2_Real_name,et_register2_code,et_register2_address,et_register2_e_mail
							,et_register2_telephone,et_register_Check2_mobile;
//	private static TextView tv_register2_Title;
	private static tryscroll sv_register2_sv;
	private  static RadioButton rb_register2_female,rb_register2_male;
	private static TextView talk_title;
	private static ImageButton title_bar_back,title_bar_gps;

	
	private String Real_name = "";
	private String code = "";
	private String address = "";
	private String e_mail = "";
	private String telephone = "";
	private String mobile = "";
	private String sex = "";

	private String line;
	InetAddress serverAddr;
	ProgressDialog pd = null;

	private static final int SWIPE_MIN_DISTANCE = 120 ;
	private static final int SWIPE_MAX_OFF_PATH = 250 ;
	//pulling speed
	private static final int SWIPE_THRESHOLD_VELOCITY = 200 ;
	//
	private GestureDetector gestureDetector ;
	View.OnTouchListener gestureListener ;
	
	Message msg = new Message();
	@SuppressLint("HandlerLeak")
	final Handler mhandler = new Handler(){
		@Override
		public void  handleMessage (Message msg){
			switch (msg.what){
			case 0:
				Log.v("mhandler", "case 0");
				if(pd!=null)pd.dismiss();
				new AlertDialog.Builder(register2.this).  
					setTitle("成功").setMessage("成功修改个人信息").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
						@Override
						public void onClick(DialogInterface dialog, int which) {
							register2.this.finish();
							dialog.dismiss();
							}  
						}).show();
				break;
			case 1:
				Log.v("mhandler", "case 1");
				if(pd!=null)pd.dismiss();
				new AlertDialog.Builder(register2.this).
				setTitle("错误").setMessage("移动电话重复注册").setPositiveButton("确定", new DialogInterface.OnClickListener() {
       			 @Override
				public void onClick(DialogInterface dialog, int which) {
       				 dialog.dismiss();
       				 }
       			 }).show();
				break;
			case 2:
				Log.v("mhandler", "case 2");
				if(pd!=null)pd.dismiss();
				new AlertDialog.Builder(register2.this).  
				setTitle("错误").setMessage("网络连接出错").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						}  
					}).show();
				break;
			case 3:
				Log.v("mhandler", "case 3");
	            parseJson(line);
				if(pd!=null)pd.dismiss();
				break;
			}
		}
	};
	
	
//	private TimeOut timer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
		
		//no_title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register2);
		
		setTitle();
		
		//link data to the view
		btn_register2_register = (Button)findViewById(R.id.btn_register2_register);
		
		et_register2_Real_name = (EditText)findViewById(R.id.et_register2_Real_name);
		et_register2_code = (EditText)findViewById(R.id.et_register2_code);
		et_register2_address = (EditText)findViewById(R.id.et_register2_address);
		et_register2_e_mail = (EditText)findViewById(R.id.et_register2_e_mail);
		et_register2_telephone = (EditText)findViewById(R.id.et_register2_telephone);
		et_register_Check2_mobile = (EditText)findViewById(R.id.et_register_Check2_mobile);
		
//		tv_register2_Title = (TextView)findViewById(R.id.tv_register2_Title);
		
		sv_register2_sv = (tryscroll)findViewById(R.id.sv_register2_sv);

		rb_register2_female = (RadioButton)findViewById(R.id.rb_register2_female);
		rb_register2_male = (RadioButton)findViewById(R.id.rb_register2_male);
		
		//no ScrollBar
		sv_register2_sv.setVerticalScrollBarEnabled(false);
		
		//no focus
//		iv_lLogo.setFocusable(true);
//		tv_register2_Title.setFocusableInTouchMode(true);
		
		
		/*****************************get personal info*****************************/
		pd = ProgressDialog.show(register2.this, "连接", "正在连接服务器…");
		final GlobalID globalID = ((GlobalID)getApplication());
		Thread b_thread = new Thread(){
			@Override
			public void run(){
//				Looper.prepare();
				if(msg != null) msg = new Message();
                msg.what = 3;
                String url = globalID.getUrlServer();
				try {
					serverAddr = InetAddress.getByName(url);
					int SERVERPORT = globalID.getSERVERPORT();
					Socket socket = new Socket();
					FuntionUtil.doSth();
					
					try {
//						socket = new Socket(serverAddr,SERVERPORT);
						InetSocketAddress socketAdd = new InetSocketAddress(serverAddr, SERVERPORT);
						socket.connect(socketAdd, globalID.getTIMEOUT());
						PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
						out.println(13);
			            out.println(globalID.getID());
			            
			            socket.setSoTimeout(globalID.getTIMEOUT());
			            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GB2312"));
			            line = reader.readLine();
		             	mhandler.sendMessage(msg);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						if(msg != null) msg = new Message();
		                msg.what = 2;
		             	mhandler.sendMessage(msg);
		             	Log.v("register2","IOException: "+e);
					}
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if(msg != null) msg = new Message();
	                msg.what = 2;
	             	mhandler.sendMessage(msg);
	             	Log.v("register2","UnknownHostException: "+e);
				}
			}
		};
		b_thread.start();
		
		/*****************************get personal info*****************************/
		
		//set on click listener
		btn_register2_register.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				btn_register2_register.setClickable(false);
//				if(Real_name.equals("")||code.equals("")||address.equals("")||e_mail.equals("")||telephone.equals("")||mobile.equals("")||sex.equals("")){
//					new AlertDialog.Builder(register2.this).  
//					setTitle("错误").setMessage("请输入完整信息").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//						public void onClick(DialogInterface dialog, int which) {
//							dialog.dismiss();
//							}  
//						}).show();
//				}
				
//				else{
					
//				}
				pd = ProgressDialog.show(register2.this, "连接", "正在连接服务器…");
				btn_register2_register.setBackgroundColor(android.graphics.Color.parseColor("#FE9B21"));
				
				Log.v("btn_register2_register", "run");
				
				Thread a_thread = new Thread(){
					@Override
					public void run(){
						send_msg();
						btn_register2_register.setClickable(true);
						}
				};
				a_thread.start();
			}
		});
		
		btn_register2_register.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				btn_register2_register.setBackgroundColor(android.graphics.Color.parseColor("#FFFFFF"));
				return false;
			}
		});
		
		gestureDetector = new GestureDetector(new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(gestureDetector.onTouchEvent(event)){
					return true ;
				}
				return false;
			}
		}; 
		
	}
	
	private void setTitle() {
		// TODO Auto-generated method stub
				// TODO Auto-generated method stub
			talk_title = (TextView)findViewById(R.id.talk_title);
	    	talk_title.setText(R.string.register2_title);
	    	title_bar_back = (ImageButton)findViewById(R.id.title_bar_back);
	    	title_bar_back.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}
			});
	    	title_bar_gps = (ImageButton)findViewById(R.id.title_bar_gps);
	    	title_bar_gps.setVisibility(View.INVISIBLE);
	}

	private void parseJson(String resultObj) {
		// TODO Auto-generated method stub
		try {
			JSONArray jsonArray = new JSONArray(resultObj);
			for(int i = 0 ; i < jsonArray.length() ; i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				et_register2_Real_name.setText(jsonObject.optString("Real_Name"));
				et_register2_code.setText(jsonObject.optString("code"));
				String sex = jsonObject.optString("Sex");
				if(sex.equals("男"))rb_register2_male.setChecked(true);
				if(sex.equals("女"))rb_register2_female.setChecked(true);
				et_register2_address.setText(jsonObject.optString("Address"));
				et_register2_e_mail.setText(jsonObject.optString("Email"));
				et_register2_telephone.setText(jsonObject.optString("Telephone"));
				et_register_Check2_mobile.setText(jsonObject.optString("Mobile"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void send_msg() {
//		Looper.prepare();
		if(msg != null) msg = new Message();
        msg.what = 1;
		
		Real_name = et_register2_Real_name.getText().toString();
		code = et_register2_code.getText().toString();
		address = et_register2_address.getText().toString();
		e_mail = et_register2_e_mail.getText().toString();
		telephone = et_register2_telephone.getText().toString();
		mobile = et_register_Check2_mobile.getText().toString();
		sex = "男";
		Calendar c = Calendar.getInstance();
		c.get(Calendar.DATE);
		System.out.println(c.getTime().getDate()); 
		
		if((rb_register2_female).isChecked()){
			sex = "女";
		}
		else {
			sex = "男";
		}
		// TODO Auto-generated method stub

		Socket socket = new Socket();
		try {
			GlobalID globalID = ((GlobalID)getApplication());
			String url = globalID.getUrlServer();
			InetAddress serverAddr = InetAddress.getByName(url);
			int SERVERPORT = globalID.getSERVERPORT();
			String id = globalID.getID();
			
			String sql = "update tbl_Users set Real_Name ='" + Real_name + "', code = '"+ code
					+ "', Sex = '"+sex+"',Address = '"+address+"', Email = '"+e_mail+"',Telephone = '"+ telephone 
					+"', Mobile = '"+ mobile +"' where UserId = '"+id+"'";
			Log.v("sql", "RIGHT: "+sql);

			FuntionUtil.doSth();
			
//			socket = new Socket(serverAddr,SERVERPORT);
			InetSocketAddress socketAdd = new InetSocketAddress(serverAddr, SERVERPORT);
			socket.connect(socketAdd, globalID.getTIMEOUT());
			PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
			
            out.println(6);
            out.println(sql);
            
            socket.setSoTimeout(globalID.getTIMEOUT());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GB2312"));
            line = reader.readLine();
            Log.v("id_checked",line);
            int i = Integer.parseInt(line);
            if(i == 1){
//            	timer.interrupt();
            	msg.what = 0;
             	mhandler.sendMessage(msg);
        		 }
        	 else{
//             	timer.interrupt();
             	mhandler.sendMessage(msg);
        	 }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if(msg != null) msg = new Message();
			msg.what = 2;
            mhandler.sendMessage(msg);
			e.printStackTrace();
			Log.v("Exception", "err  "+e);
//			new AlertDialog.Builder(register2.this).  
//			setTitle("错误").setMessage("网络连接出错").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//					}  
//				}).show();
		}
		finally{
//			Looper.loop();
            try {
           	 if(socket != null)socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

    @Override
    public void finish(){
    	super.finish();
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }
	
//	/***********************press KEY_BACK*******************/
//    @SuppressLint("ShowToast")
//	@Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//    	pd = ProgressDialog.show(register2.this, "连接", "正在连接服务器…");
//		btn_register2_register.setBackgroundColor(android.graphics.Color.parseColor("#FE9B21"));
//    	
//    	Thread a_thread = new Thread(){
//			public void run(){
//		send_msg();}
//		};
//		a_thread.start();
//		
//        return super.onKeyDown(keyCode, event);
//    }
//    /***********************press KEY_BACK*******************/
	
//	public class TimeOut extends Thread{
//		public Handler mHandler;
//		
//		/***每个多少毫秒检测一次**/ 
//		protected int m_rate = 100; 
//		/***超时时间长度毫秒计算**/ 
//		private int m_length; 
//		/** 已经运行的时间*/
//		private int m_elapsed;
//		
//		private Context ctx;
//		/***构造函数*
//		 ** @param length*Length of time before timeout occurs 
//		 * @return */
//		public TimeOut(int length,Context ct) {
//			// Assign to member variable
//			System.out.println("start timeOut");
//			Log.v("start ","timeOut");
//			ctx = ct;
//			
//			m_length = length;
//			// Set time elapsed
//			m_elapsed = 0;
//			}
//		/***重新计时**/
//		public synchronized void reset() { 
//			m_elapsed = 0; 
//			System.out.println("reset timer"); 
//			} 
//		/***故意设置为超时,可以在服务器有返回,但是错误返回的时候直接调用这个,当成超时处理**/
//		public synchronized void setTimeOut(){
//			m_elapsed = m_length+1; 
//			} 
//		/***/ 
//		public void run() { 
//			//循环
//			Looper.prepare(); 
//			mHandler = new Handler() { 
//			@SuppressLint("HandlerLeak")
//			public void handleMessage(Message msg) { 
//			// process incoming messages here 
//				
//			} 
//			}; 
//			System.out.println("timer running");
//			GlobalID globalID = new GlobalID();
//			for (;;) {
////				System.out.println("start timeOut2");
////				Log.v("start ","timeOut2");
//				// Put the timer to sleep
//				try {
//					Thread.sleep(m_rate);
//					} catch (InterruptedException ioe) {
//						continue; 
//						} 
//				synchronized (this) {
//					// Increment time remaining 
//					m_elapsed += m_rate; 
//					// Check to see if the time has been exceeded
//					if (m_elapsed > m_length && !globalID.isConnActive()) { //isConnActive 为全局变量
//						// Trigger a timeout
//						timeout();
//						Log.v("isConnActive",String.valueOf(globalID.isConnActive()));
//						break;
//						}
//					} 
//				if(m_elapsed > m_length)break;
//				}
//
//			Looper.loop(); 
//			}
//		/*** 超时时候的处理**/
//		public void timeout() {
//			Log.v("timeOut","TimeOut");
//			 new AlertDialog.Builder(register2.this).  
//				setTitle("错误").setMessage("错误").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						}  
//					}).show();
//			 register2.this.finish();
//	    }
//	}
    

	/**********************MotionEvent*************************/
	class MyGestureDetector extends SimpleOnGestureListener{
		@Override
		public boolean onFling(MotionEvent e1 , MotionEvent e2 , float velocityX , 
				float velocityY){
			if(Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH){
				return false;
			}

			//向左手势
			if(e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY ){
				register2.this.finish();
				return true;
				}
			return false;
			}
	}
	
	public boolean dispatchTouchEvent(MotionEvent event){
		if(gestureDetector.onTouchEvent(event)){
			event.setAction(MotionEvent.ACTION_CANCEL);
		}
		return super.dispatchTouchEvent(event);
	}
	/**********************MotionEvent*************************/
}
