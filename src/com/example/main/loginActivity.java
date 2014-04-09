package com.example.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.main.R.id;
import com.example.main_util.FuntionUtil;
import com.example.main_util.SpUtil;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.Vibrator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

public class loginActivity extends Activity {
	private String TAG = "login";
	private static boolean log = false;
    private Vibrator vibrator=null;
	
	//define data for button and editText
	private static Button btn_login_Login
	,btn_login_Register
	,btn_setAll
	;
	private static EditText et_login_ID,et_login_Password
//	,et_login_CheckCode
	;
//	private static TextView tv_login_Title
//	,tv_login_CheckCode
//	;
//	private static ScrollView sv_login_sv;
//	private static ImageView iv_login_CheckCode;
//	private static LinearLayout btn_login_Register;
	InetAddress serverAddr;
    
	ProgressDialog pd = null;
    byte[] code_picture = new byte[4];

    Context context = loginActivity.this;
    
	Message msg = new Message();
	@SuppressLint("HandlerLeak")
	final Handler mhandler = new Handler(){
		@Override
		public void  handleMessage (Message msg){
			final GlobalID globalID = ((GlobalID)getApplication());
            btn_login_Login.setClickable(true);
			switch (msg.what){
			case -2:
				if(log)Log.v("mhandler", "case -2");
				globalID.showDialog("错误","无法连接数据库",context);
//		 		tv_login_Title.requestFocus();
//				if(pd!=null)pd.dismiss();
//				 new AlertDialog.Builder(context).  
//					setTitle("错误").setMessage("无法连接数据库").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							dialog.dismiss();
////							et_login_ID.requestFocus();
//							}  
//						}).show();
		 		loginActivity.this.onResume();
				break;
			case -1:
				if(log)Log.v("mhandler", "case -1");
				globalID.showDialog("错误","账号或密码错误",context);
//		 		tv_login_Title.requestFocus();
//				if(pd!=null)pd.dismiss();
//				 new AlertDialog.Builder(context).  
//					setTitle("错误").setMessage("账号或密码错误").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							dialog.dismiss();
////							et_login_ID.requestFocus();
//							context.onResume();
//							}  
//						}).show();
				break;
			case 0:
				if(log)Log.v("mhandler", "case 0");
				globalID.showDialog("错误","验证码错误",context);
//		 		tv_login_Title.requestFocus();
//				if(pd!=null)pd.dismiss();
//				new AlertDialog.Builder(context).  
//					setTitle("错误").setMessage("验证码错误").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							dialog.dismiss();
////							et_login_CheckCode.requestFocus();
//							context.onResume();
//							}  
//						}).show();
				break;
			case 1:
				if(log)Log.v("mhandler", "case 1");
//				if(pd!=null)pd.dismiss();
				globalID.cancelPD();
				break;
			case 2:
				if(log)Log.v("mhandler", "case 2");
				globalID.cancelPD();
//				if(pd!=null)pd.dismiss();
				Intent intent = new Intent(context,NewMainActivity.class);
//	            globalID.clear();
				globalID.setCurrent_code(0);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
				finish();
				break;
			case 3:
				if(log)Log.v("mhandler", "case 3");
				globalID.showDialog("错误","网络连接出错",context);
//		 		tv_login_Title.requestFocus();
//				if(pd!=null)pd.dismiss();
//                new AlertDialog.Builder(context).  
//				setTitle("错误").setMessage("网络连接出错").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						}  
//					}).show();
                break;
			case 4:
				if(log)Log.v("mhandler", "case 4");
//				if(pd!=null)pd.dismiss();
				globalID.cancelPD();
//        		iv_login_CheckCode.setImageBitmap(getBitmapFromByte(code_picture));
        		break;
			case 5:
				if(log)Log.v("mhandler", "case 5");
//				if(pd!=null)pd.dismiss();
				globalID.cancelPD();
//        		iv_login_CheckCode.setImageBitmap(getBitmapFromByte(code_picture));
//				et_login_CheckCode.requestFocus();
        		break;
			}
//			et_login_CheckCode.setText("");
		}
	};
	
//	TimeOut timer = null;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
		stopService(new Intent(context, DemoService.class));
		final GlobalID globalID = ((GlobalID)getApplication());
		globalID.cancel_notification();
		globalID.start(context);
		try {
			if(log)Log.v(TAG +" first","urlServer = " + SpUtil.read(context, "urlServer"));
			if(log)Log.v(TAG +" first","ID = " + SpUtil.read(context, "ID"));
			if(log)Log.v(TAG +" first","PW = " + SpUtil.read(context, "PW"));
			if(log)Log.v(TAG +" first","startDate = " + SpUtil.read(context, "startDate"));
			if(log)Log.v(TAG +" first","endDate = " + SpUtil.read(context, "endDate"));
			if(log)Log.v(TAG +" first","line = " + SpUtil.read(context, "line"));
			if(log)Log.v(TAG +" first","DBurl = " + SpUtil.read(context, "DBurl"));
			if(log)Log.v(TAG +" first","SERVERPORT = " + SpUtil.read(context, "SERVERPORT"));
			if(log)Log.v(TAG +" first","TIMEOUT = " + SpUtil.read(context, "TIMEOUT"));

			if(log)Log.v(TAG +" first","urlServer = " + globalID.getUrlServer());
			if(log)Log.v(TAG +" first","ID = " + globalID.getID());
			if(log)Log.v(TAG +" first","PW = " + globalID.getPW());
			if(log)Log.v(TAG +" first","startDate = " + globalID.getStartDate());
			if(log)Log.v(TAG +" first","endDate = " + globalID.getEndDate());
			if(log)Log.v(TAG +" first","line = " + globalID.getLine());
			if(log)Log.v(TAG +" first","DBurl = " + globalID.getDBurl());
			if(log)Log.v(TAG +" first","SERVERPORT = " + globalID.getSERVERPORT());
			if(log)Log.v(TAG +" first","TIMEOUT = " + globalID.getTIMEOUT());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		switch(globalID.getCurrent_code()){
//		case -2:break;
//		case -1:break;
//		case 0:
//		case 1:
//		case 2:{
//			Intent intent = new Intent(context,MainActivity.class);
//			startActivity(intent);
//			finish();
//			return;
//		}
//		case 3:{
//			
//		}
//		case 4:{
//			
//		}
//		}
//		
        
        //no_title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_login);
        
        vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
        
        //link data to the view
        btn_login_Login = (Button)findViewById(R.id.btn_login_Login);
		btn_login_Register = (Button)findViewById(R.id.btn_login_Register);
		btn_setAll = (Button)findViewById(R.id.btn_setAll);
//		btn_login_Register = (LinearLayout)findViewById(R.id.btn_login_Register);
		
		et_login_ID = (EditText)findViewById(R.id.et_login_Id);
		et_login_Password = (EditText)findViewById(R.id.et_login_Password);
//		et_login_CheckCode = (EditText)findViewById(R.id.et_login_CheckCode);
		
//		tv_login_Title = (TextView)findViewById(R.id.tv_login_Title);
//		tv_login_CheckCode = (TextView)findViewById(R.id.tv_login_CheckCode);
//		tv_login_CheckCode.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		
//		iv_login_CheckCode = (ImageView)findViewById(R.id.iv_login_CheckCode);
		
//		sv_login_sv = (ScrollView)findViewById(R.id.sv_login_sv);
		
//		cb_login_ID = (CheckBox)findViewById(R.id.cb_login_ID);
//		cb_login_pw = (CheckBox)findViewById(R.id.cb_login_pw);
		
//		globalID.start(context);
//		try {
//			if(log)Log.v(TAG +" Create","urlServer = " + SpUtil.read(context, "urlServer"));
//			if(log)Log.v(TAG +" Create","ID = " + SpUtil.read(context, "ID"));
//			if(log)Log.v(TAG +" Create","PW = " + SpUtil.read(context, "PW"));
//			if(log)Log.v(TAG +" Create","startDate = " + SpUtil.read(context, "startDate"));
//			if(log)Log.v(TAG +" Create","endDate = " + SpUtil.read(context, "endDate"));
//			if(log)Log.v(TAG +" Create","line = " + SpUtil.read(context, "line"));
//			if(log)Log.v(TAG +" Create","DBurl = " + SpUtil.read(context, "DBurl"));
//			if(log)Log.v(TAG +" Create","SERVERPORT = " + SpUtil.read(context, "SERVERPORT"));
//			if(log)Log.v(TAG +" Create","TIMEOUT = " + SpUtil.read(context, "TIMEOUT"));
//
//			if(log)Log.v(TAG +" Create","urlServer = " + globalID.getUrlServer());
//			if(log)Log.v(TAG +" Create","ID = " + globalID.getID());
//			if(log)Log.v(TAG +" Create","PW = " + globalID.getPW());
//			if(log)Log.v(TAG +" Create","startDate = " + globalID.getStartDate());
//			if(log)Log.v(TAG +" Create","endDate = " + globalID.getEndDate());
//			if(log)Log.v(TAG +" Create","line = " + globalID.getLine());
//			if(log)Log.v(TAG +" Create","DBurl = " + globalID.getDBurl());
//			if(log)Log.v(TAG +" Create","SERVERPORT = " + globalID.getSERVERPORT());
//			if(log)Log.v(TAG +" Create","TIMEOUT = " + globalID.getTIMEOUT());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//ID.setChecked(globalID.isId_check());
//		cb_login_pw.setChecked(globalID.isPw_check());
//		if(globalID.isId_check()){
//			if(log)Log.v(TAG,"id "+globalID.getID()+" id_check "+globalID.isId_check());
//			et_login_ID.setText(globalID.getID());
//		}
		if(globalID.getID().length()!=0){
			et_login_ID.setText(globalID.getID().toString());
		}
		if(globalID.getPW().length()!=0){
			et_login_Password.setText(globalID.getPW());
		}
//		if(globalID.isId_check()&&globalID.isPw_check()){
//			et_login_CheckCode.requestFocus();
//		}
		//no ScrollBar
//		sv_login_sv.setVerticalScrollBarEnabled(false);
				
		
//		btn_login_Login.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View arg0, MotionEvent arg1) {
//				// TODO Auto-generated method stub
//				btn_login_Login.setBackgroundResource(R.drawable.login_button_down);
//				return false;
//			}
//		});
		
		btn_setAll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(context,SetPropertiesActivity.class);
                startActivity(intent);
			}
		});
		
		btn_login_Register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				btn_login_Register.setBackgroundColor(android.graphics.Color.parseColor("#E0DFDF"));
				btn_login_Register.setClickable(false);
//				btn_login_Register.setTextColor(android.graphics.Color.parseColor("#000000"));
				Intent intent = new Intent(context,register.class);
                startActivity(intent);
				btn_login_Register.setClickable(true);
			}
		});
		
		//set on click listener
		btn_login_Login.setOnClickListener(new OnClickListener() {
		
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				login();
			}
		});
		
//		tv_login_CheckCode.setOnClickListener(new OnClickListener() {
//			
//			@SuppressLint("NewApi")
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				pd = ProgressDialog.show(context, "连接", "正在获取验证码…");
//				Thread a_thread = new Thread(){
//					public void run(){
////						Looper.prepare();
//						if(msg != null) msg = new Message();
//		                msg.what = 5;
//		        		if(!getCodePicture(2)){
//			                msg.what = 3;
//		        		}
//		                mhandler.sendMessage(msg);
//					}
//				};
//				a_thread.start();
//			}
//		});
		
//		iv_login_CheckCode.setOnClickListener(new OnClickListener() {
//			
//			@SuppressLint("NewApi")
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				pd = ProgressDialog.show(context, "连接", "正在获取验证码…");
//				Thread a_thread = new Thread(){
//					public void run(){
////						Looper.prepare();
//						if(msg != null) msg = new Message();
//		                msg.what = 5;
//		        		if(!getCodePicture(2)){
//			                msg.what = 3;
//		        		}
//		                mhandler.sendMessage(msg);
//					}
//				};
//				a_thread.start();
//			}
//		});

		if(globalID.getCurrent_code() != -2 
				&& globalID.getID().length() != 0
				&& globalID.getPW().length() != 0){
			login();
		}
	}
	
	public void login(){
		final GlobalID globalID = ((GlobalID)getApplication());
		// TODO Auto-generated method stub
//		btn_login_Login.setBackgroundResource(R.drawable.login_button_up);
		btn_login_Login.setClickable(false);
		final String id = et_login_ID.getText().toString();
		final String password =  et_login_Password.getText().toString();
//		final String check_code = et_login_CheckCode.getText().toString();
		
		if(id.equals("")){
			new AlertDialog.Builder(context).  
			setTitle("错误").setMessage("请输入账号").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
				@Override
				public void onClick(DialogInterface dialog, int which) {
					btn_login_Login.setClickable(true);
					dialog.dismiss();
//					et_login_ID.requestFocus();
					}
				}).show(); 
			}
		
		else if(password.equals("")){
			
			new AlertDialog.Builder(context).  
			setTitle("提示").setMessage("请输入密码").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
				@Override
				public void onClick(DialogInterface dialog, int which) {
					btn_login_Login.setClickable(true);
					dialog.dismiss();
					et_login_Password.requestFocus();
					}  
				}).show();
			}
//		else if(check_code.equals("")){
//			
//			new AlertDialog.Builder(context).  
//			setTitle("提示").setMessage("请输入验证码").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//					et_login_CheckCode.requestFocus();
//					}  
//				}).show();
//			}
		else{
			globalID.setID(id);
//			pd = ProgressDialog.show(context, "连接", "正在连接服务器…");
			globalID.PD(context,"连接", "正在连接服务器…");
			Thread a_thread = new Thread(){
				@Override
				public void run(){
//					Looper.prepare();
					if(msg != null) msg = new Message();
	                msg.what = 1;

					try {
						String url = globalID.getUrlServer();
						serverAddr = InetAddress.getByName(url);
						int SERVERPORT = globalID.getSERVERPORT();
						
						Socket socket= new Socket();
						try {

							FuntionUtil.doSth();
							
							InetSocketAddress socketAdd = new InetSocketAddress(serverAddr, SERVERPORT);
							socket.connect(socketAdd, globalID.getTIMEOUT());
//							socket = new Socket(serverAddr, SERVERPORT);
//							if(log)Log.v("LAST3", "RIGHT: "+String.valueOf(1)); 

					        try {
					            PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
					            if(log)Log.v("LAST2", "RIGHT: ");
					            out.println(3);
					            out.println(id);
					            out.println(password);
//		                        byte[] case_3 = new byte[4];
//		                        case_3[0] = (byte)(3);
//		                        case_3[1] = (byte)(3 >> 8);
//		                        case_3[2] = (byte)(3 >> 16);
//		                        case_3[3] = (byte)(3 >> 24);
////		                        if(log)Log.v("case_3",String.valueOf(case_3[0]));
////		                        if(log)Log.v("case_3",String.valueOf(case_3[1]));
////		                        if(log)Log.v("case_3",String.valueOf(case_3[2]));
////		                        if(log)Log.v("case_3",String.valueOf(case_3[3]));
//		                        byte[] id_length = new byte[4];
//		                        id_length[0] = (byte)(id.length());
//		                        id_length[1] = (byte)(id.length() >> 8);
//		                        id_length[2] = (byte)(id.length() >> 16);
//		                        id_length[3] = (byte)(id.length() >> 24);
//		                        if(log)Log.v("id.length()",":"+String.valueOf(id.length()));
//		                        byte[] pw_length = new byte[4];
//		                        pw_length[0] = (byte)(password.length());
//		                        pw_length[1] = (byte)(password.length() >> 8);
//		                        pw_length[2] = (byte)(password.length() >> 16);
//		                        pw_length[3] = (byte)(password.length() >> 24);
//		                        if(log)Log.v("id.length()",":"+String.valueOf(password.length()));
//		                       	byte[] login_send = new byte[12+id.length()+password.length()];
//		                       	System.arraycopy(case_3, 0, login_send, 0, 4);
//		                       	System.arraycopy(id_length, 0, login_send, 4 , 4);
//		                       	System.arraycopy(id.getBytes(), 0, login_send, 8 , id.length());
//		                       	System.arraycopy(pw_length, 0, login_send, 8+id.length() , 4);
//		                       	System.arraycopy(password.getBytes(), 0, login_send, 12+id.length() , password.length());
//		                        if(log)Log.v("case_3",":"+String.valueOf(login_send[8]));
//		                        if(log)Log.v("case_3",":"+String.valueOf(login_send[9]));
//		                        if(log)Log.v("case_3",":"+String.valueOf(login_send[10]));
//		                        if(log)Log.v("case_3",":"+String.valueOf(login_send[8]));
//					            OutputStream os = socket.getOutputStream();
//		                       	os.write(login_send);
//					            out.println(check_code);
					        
					             socket.setSoTimeout(globalID.getTIMEOUT()*2);
					        	 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GB2312"));
					        	 String line = reader.readLine();
					        	 if(log)Log.v("id_checked",line);
					        	 
									
					        	 String id_checked = parseJson(line);
					        	 if(id_checked.equals("-1")){
					        		 msg.what = -1;
						                mhandler.sendMessage(msg);
					        		 }
					        	 else{
										
					        		 if(id_checked.equals("-2")){
					        			 msg.what = 0;
							                mhandler.sendMessage(msg);
					        		 }else{
											
						        		 if(id_checked.equals("-3")){
						        			 msg.what = -2;
								                mhandler.sendMessage(msg);
						        		 }
					        		 else{
//					        			 globalID.setId_check(cb_login_ID.isChecked());
//					        			 if(log)Log.v("cb_login_ID.isChecked()","cb_login_ID.isChecked():"+cb_login_ID.isChecked());
//					                 	globalID.setPw_check(cb_login_pw.isChecked());
//					        			 if(cb_login_pw.isChecked()){
//						        			 globalID.setPw_check(true);
//					        				 globalID.setPW(password);
//					        			 }
					        			 
//					        			 FuntionUtil.doSth();

					        			 
//					        			 socket = new Socket(serverAddr, SERVERPORT);
					        			 socket = new Socket();
										 socket.connect(socketAdd, globalID.getTIMEOUT());

					        			 out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
					        			 out.flush();
					        			 out.println(4);
					        			 out.println(id);
					        			 
					        			 socket.setSoTimeout(globalID.getTIMEOUT()*2);
					        			 BufferedReader reader2 = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GB2312"));
					        			 line = reader2.readLine();
//					        			 parseJson2(line);
					        			 
					        			 if(globalID.setAll(line)){
					        				 if(log)Log.v(TAG, "setAll success!");
					        			 }
//					        			 globalID.setNow_ark();
				        				 globalID.setID(id);
					        			 
//					        			 if(globalID.getNow_ark().equals("-1")){
//					        				 new AlertDialog.Builder(context).  
//												setTitle("错误").setMessage("对不起，您权限被限制").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//													public void onClick(DialogInterface dialog, int which) {
//									        			 Intent intent = new Intent(context,MainActivity.class);
//									        			 startActivity(intent);
//									        			 context.finish();
//														dialog.dismiss();
//														}  
//													}).show();
//					        			 }
					        			 
//					        			 else{
				        				 if(globalID.isShake()){
//				        					 if(log)Log.v(TAG,"shake");
				        					 vibrator.vibrate(new long[]{1000,50,50,100,50}, -1);
				        				 }
				        				 globalID.setPW(password);
				        				 msg.what = 2;
				        				 mhandler.sendMessage(msg);
//					        			 }
							        	 }
					        	 }}
					             socket.close();
//		        				 Looper.loop();
//					        }
					        } catch(SocketTimeoutException e) {
				                Log.e("err0", "S: Error" + e);
//				                Message msg1 = new Message();
//				                msg1.what = 1;
				                if(msg != null) msg = new Message();
				                msg.what = 3;
				                mhandler.sendMessage(msg);
//				                Looper.loop();
				        }
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							if(msg != null) msg = new Message();
			                msg.what = 3;
			                mhandler.sendMessage(msg);
			                Log.e("err1", "S: Error" + e1);
//							Looper.loop();
						}  
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						if(msg != null) msg = new Message();
		                msg.what = 3;
		                mhandler.sendMessage(msg);
		                Log.e("err2", "S: Error" + e);
//						Looper.loop();
					}catch(Exception e){
						if(msg != null) msg = new Message();
		                msg.what = 3;
		                mhandler.sendMessage(msg);
		                Log.e("err3", "Exception:  " + e);
					}
//					Looper.loop();
				}
			};
			a_thread.start();
			
			
		
		}
	}
	
	@SuppressLint({ "DefaultLocale", "NewApi" })
	@Override
    protected void onResume(){
		super.onResume();
		//ask server for checkcode
//		pd = ProgressDialog.show(context, "连接", "正在获取验证码…");
//		Thread a_thread = new Thread(){
//			public void run(){
////				Looper.prepare();
//				if(msg != null) msg = new Message();
//                msg.what = 4;
//        		if(!getCodePicture(1)){
//	                msg.what = 3;
//        		}
//                mhandler.sendMessage(msg);
//			}
//		};
//		a_thread.start();
		
		GlobalID globalID = ((GlobalID)getApplication());
    	if(globalID.getID().length() != 0){
 			et_login_ID.setText(globalID.getID().toString());
 		}
 		if(globalID.getPW().length() != 0){
 			et_login_Password.setText(globalID.getPW());
 		}
    	//login_title catch the focus
// 		iv_lLogo.setFocusable(true);
// 		tv_login_Title.setFocusableInTouchMode(true);
// 		tv_login_Title.requestFocus();
 		
		return;
    }
    
    /***********Json for login******************/
    private String parseJson(String resultObj){
		String abc = null;
		try{
			JSONArray jsonArray = new JSONArray(resultObj);
			for(int i = 0 ; i < jsonArray.length() ; i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				abc = jsonObject.optString("UserId");
			}
			
		}catch(JSONException e){
			e.printStackTrace();
		}
		return abc;
	}
    /***********Json for login******************/
    
//    /***********Json to get ark_id******************/
//    private void parseJson2(String resultObj){
//    	GlobalID globalID = ((GlobalID)getApplication());
//    	globalID.getAll().clear();
//    	String abc = "-1";
//		try{
//			JSONArray jsonArray = new JSONArray(resultObj);
//			for(int i = 0 ; i < jsonArray.length() ; i++){
//				JSONObject jsonObject = jsonArray.getJSONObject(i);
//				abc = jsonObject.optString("ark_id");
//				globalID.getAll().add(abc);
//			}
//		}catch(JSONException e){
//			e.printStackTrace();
//			if(log)Log.v("timeout?", "2");
//		}
//	}
//    /***********Json to get ark_id******************/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	menu.add(0,0,0,"退出");
        return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.login, menu);
		
		switch(item.getItemId()){
		//菜单项1被选择
		case 0 :
			finish();
			overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
			break;
		}
		return true;
	}
    
    @Override
    public void finish(){
    	super.finish();
    	try {
    		SpUtil.write(context, et_login_ID.getText().toString(), "ID");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			SpUtil.write(context, et_login_Password.getText().toString(), "PW");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if(log)Log.v(TAG,"finish()");
    }
    
    /***********************press KEY_BACK twice to exit*******************/
    private long exitTime = 0;
    @SuppressLint("ShowToast")
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
            if((System.currentTimeMillis()-exitTime) > 2000){  
                Toast.makeText(getApplicationContext(), "再按一次退出程序", 2000).show();                         
                exitTime = System.currentTimeMillis();
            } else {
            	GlobalID globalID = ((GlobalID)getApplication());
            	if(!et_login_ID.getText().toString().equals(""))globalID.setID(et_login_ID.getText().toString());
//            	globalID.setId_check(cb_login_ID.isChecked());
//            	globalID.clear();
                finish();
        		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    /***********************press KEY_BACK twice to exit*******************/
    
    /**********************change byte[] to bitmap************************/
    public static Bitmap getBitmapFromByte(byte[] temp){   
        if(temp != null){   
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);   
            return bitmap;   
        }else{   
            return null;   
        }   
    } 
    /**********************change byte[] to bitmap************************/
    
    /**********************change bitmap to byte[]************************/
    public static byte[] BitmapToBytes(Bitmap bitmap){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
		return baos.toByteArray();
    } 
    /**********************change bitmap to byte[]************************/

	
    
//    @SuppressLint("NewApi")
//	public boolean getCodePicture(int i){
//    	try {
//			GlobalID globalID = ((GlobalID)getApplication());
//			String url = globalID.getUrlServer();
//
////            timer = new TimeOut(globalID.getTIMEOUT(), context);
////			timer.start();
//			
//			serverAddr = InetAddress.getByName(url);
//			int SERVERPORT = globalID.getSERVERPORT();
//
//			FuntionUtil.doSth();
//
//			Socket socket = new Socket();
//			SocketAddress socketAddr = new InetSocketAddress(serverAddr,SERVERPORT);
//			socket.connect(socketAddr,globalID.getTIMEOUT());
////			socket = new Socket(serverAddr,SERVERPORT);
////			if(log)Log.v("LAST3", "RIGHT: "+String.valueOf(100)); 
//
//            PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
//            if(log)Log.v("LAST2", "RIGHT: ");
//            out.println(i);
//            
//
//			socket.setSoTimeout(globalID.getTIMEOUT());
//            InputStream is = socket.getInputStream();
////            timer.interrupt();
//            	is.read(code_picture, 0, 4);
//            	if(log)Log.v("length", String.valueOf(code_picture.length));
//            	
//            	int addr = code_picture[0] & 0xFF;
//                addr |= ((code_picture[1] << 8) & 0xFF00);
//                addr |= ((code_picture[2] << 16) & 0xFF0000);
//                addr |= ((code_picture[3] << 24) & 0xFF000000);
//            	if(log)Log.v("length", String.valueOf(addr));
//                
//            	code_picture = new byte[addr];
//            	//ok
//            	for(int i1=0;i1<addr;i1++){
//            		code_picture[i1]=(byte)is.read();
//            	   }
//            	//sometimes appear bug 1/20-1/60
////            	is.read(code_picture,0,addr);
//    		return true;
//    	}catch(Exception e){
//			e.printStackTrace();
////			timer.interrupt();
////			new AlertDialog.Builder(context).  
////			setTitle("错误").setMessage("网络连接出错").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
////				public void onClick(DialogInterface dialog, int which) {
////					dialog.dismiss();
////					}  
////				}).show();
//			return false;
//		}
//    }
    /*********************get check_code*********************************/
}
