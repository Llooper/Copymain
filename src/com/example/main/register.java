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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class register extends Activity {
	
	//define data for button and editText
	private static Button btn_register_Register;
	private static EditText et_register_ID,et_register_New_Password,et_register_Check_Password;
//	private static TextView tv_register_Title;
	private static ScrollView sv_register_sv;
	private static TextView talk_title;
	private static ImageButton title_bar_back,title_bar_gps;

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
			btn_register_Register.setClickable(true);
			switch (msg.what){
			case 0:
				Log.v("mhandler", "case 0");
				if(pd!=null)pd.dismiss();
				new AlertDialog.Builder(register.this).  
					setTitle("错误").setMessage("该账号已注册").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							et_register_ID.requestFocus();
							}  
						}).show();
				et_register_ID.setFocusable(true);
				et_register_ID.setFocusableInTouchMode(true);
				break;
			case 1:
				Log.v("mhandler", "case 1");
				if(pd!=null)pd.dismiss();
				new AlertDialog.Builder(register.this).  
				setTitle("成功").setMessage("成功注册！请联系管理员，为此账号关联渔船信息").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						GlobalID globalID = ((GlobalID)getApplication());
						globalID.setPW("");
//						Intent intent = new Intent(register.this,register2.class);
//						startActivity(intent);
						register.this.finish();
						}  
					}).show();
                break;
			case 2:
				Log.v("mhandler", "case 2");
				if(pd!=null)pd.dismiss();
                new AlertDialog.Builder(register.this).  
				setTitle("错误").setMessage("网络连接出错").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						}  
					}).show();
                break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
		
		//no_title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		
		//link data to the view
		btn_register_Register = (Button)findViewById(R.id.btn_register_Register);
		
		et_register_ID = (EditText)findViewById(R.id.et_register_ID);
		et_register_New_Password = (EditText)findViewById(R.id.et_register_New_Password);
		et_register_Check_Password = (EditText)findViewById(R.id.et_register_Check_Password);
		
//		tv_register_Title = (TextView)findViewById(R.id.tv_register_Title);
		
		sv_register_sv = (ScrollView)findViewById(R.id.sv_register_sv);
		
		setTitle();
		
		//no ScrollBar
		sv_register_sv.setVerticalScrollBarEnabled(false);
		
		//no focus
//		iv_lLogo.setFocusable(true);
//		tv_register_Title.setFocusableInTouchMode(true);
		
		//set on click listener
		btn_register_Register.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				btn_register_Register.setClickable(false);
//				btn_register_Register.setBackgroundResource(R.drawable.register_button_up);

				final String id = et_register_ID.getText().toString();
				final String password1 =  et_register_New_Password.getText().toString();
				String password2 =  et_register_Check_Password.getText().toString();
				
				if(id.equals("")){
					
					new AlertDialog.Builder(register.this).  
					setTitle("错误").setMessage("请输入账号").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							et_register_ID.requestFocus();
							btn_register_Register.setClickable(true);
							}  
						}).show(); 
					}
				
				else if(password1.length()<6){
					
					new AlertDialog.Builder(register.this).  
					setTitle("提示").setMessage("密码长度不能低于6位").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							et_register_New_Password.requestFocus();
							btn_register_Register.setClickable(true);
							}  
						}).show();
					}
				else if(password1.equals(password2)== false){
					
					new AlertDialog.Builder(register.this).  
					setTitle("提示").setMessage("请输入相同密码").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							et_register_Check_Password.requestFocus();
							btn_register_Register.setClickable(true);
							}  
						}).show();
				}
				else
				{
					pd = ProgressDialog.show(register.this, "连接", "正在连接服务器…");
					
					Thread a_thread = new Thread(){
						@Override
						public void run(){
//							Looper.prepare();
							if(msg != null) msg = new Message();
			                msg.what = 1;
				// TODO Auto-generated method stub
				InetAddress serverAddr;
				GlobalID globalID = ((GlobalID)getApplication());
				String url = globalID.getUrlServer();
				int SERVERPORT = globalID.getSERVERPORT();
				Socket socket = new Socket();
				int i = 0;
				
				try {
					
					serverAddr = InetAddress.getByName(url);
					FuntionUtil.doSth();
					
//					socket = new Socket(serverAddr,SERVERPORT);
					InetSocketAddress socketAdd = new InetSocketAddress(serverAddr, SERVERPORT);
					socket.connect(socketAdd, globalID.getTIMEOUT());
					socket.setSoTimeout(globalID.getTIMEOUT());
//					Log.v("LAST3", "RIGHT: "+String.valueOf(1)); 
					
					 PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
//			            Log.v("LAST2", "RIGHT: ");
			            out.println(5);
			            out.println(id);
			            out.println(password1);
			            out.println("");
			            
			            BufferedReader reader = null;
				            socket.setSoTimeout(globalID.getTIMEOUT());
							reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GB2312"));
			        	 String line = null;
							line = reader.readLine();
//							Log.v("tag",line);
			        	 i = Integer.parseInt(line);
			        	 
							
//			        	 Log.v("tag2",String.valueOf(i));
			        	 if(i == 0){
			        		 msg.what = 0;
				                mhandler.sendMessage(msg);
			        		 }
			        	 else{
			        		 GlobalID globalID1 = ((GlobalID)getApplication());
			        		 globalID1.setID(id);
			        		 mhandler.sendMessage(msg);
			        	 }
				} catch (Exception e) {
					// TODO Auto-generated catch block
					if(msg != null) msg = new Message();
					msg.what = 2;
	                mhandler.sendMessage(msg);
					e.printStackTrace();
					Log.v("Exception", "err  "+e);
//					new AlertDialog.Builder(register.this).  
//					setTitle("错误").setMessage("网络连接出错").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//						public void onClick(DialogInterface dialog, int which) {
//							dialog.dismiss();
//							}  
//						}).show();
				}
				finally {
//	                Looper.loop();
		             try {
		            	 if(socket != null)socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
					};
					a_thread.start();
				}
					
				}
			});
		
//		btn_register_Register.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View arg0, MotionEvent arg1) {
//				// TODO Auto-generated method stub
////				btn_register_Register.setBackgroundColor(android.graphics.Color.parseColor("#FFFFFF"));
//				btn_register_Register.setBackgroundResource(R.drawable.register_button_down);
//				return false;
//			}
//		});
		
		et_register_New_Password.addTextChangedListener(new TextWatcher() {
            private int selectionStart ;
    		GlobalID globalID = ((GlobalID)getApplication());
            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                    int arg3) {
            }
            
            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                    int arg3) {
            }
            
            @Override
            public void afterTextChanged(Editable s) {
            	selectionStart = et_register_New_Password.getSelectionStart();
//                Log.i("gongbiao1",""+selectionStart);
        			if(selectionStart == 20){
        				if(globalID.toast != null)globalID.toast.cancel();
            			globalID.toast = Toast.makeText(register.this, "世界的尽头", 2000);
            			globalID.toast.show();
        			}
        			else{
        				if(globalID.toast != null)globalID.toast.cancel();
            			globalID.toast = Toast.makeText(register.this, String.valueOf(selectionStart)+"位", 2000);
            			globalID.toast.show();
        			}
                }

        });
		
		et_register_Check_Password.addTextChangedListener(new TextWatcher() {
            private int selectionStart ;
    		GlobalID globalID = ((GlobalID)getApplication());
            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                    int arg3) {
            }
            
            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                    int arg3) {
            }
            
            @Override
            public void afterTextChanged(Editable s) {
            	selectionStart = et_register_Check_Password.getSelectionStart();
//                Log.i("gongbiao1",""+selectionStart);
        			if(selectionStart > 20){
        				if(globalID.toast != null)globalID.toast.cancel();
            			globalID.toast = Toast.makeText(register.this, "超越了世界的尽头", 2000);
            			globalID.toast.show();
        			}
        			else{
        				if(globalID.toast != null)globalID.toast.cancel();
            			globalID.toast = Toast.makeText(register.this, String.valueOf(selectionStart)+"位", 2000);
            			globalID.toast.show();
        			}
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
		talk_title = (TextView)findViewById(R.id.talk_title);
    	talk_title.setText(R.string.register_title);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
	

    @Override
    public void finish(){
    	super.finish();
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }
    
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
				register.this.finish();
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
