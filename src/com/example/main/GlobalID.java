package com.example.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.example.main_Entity.Ark_lineEntity;
import com.example.main_Entity.BandWEntity;
import com.example.main_Entity.OverlayEntity;
import com.example.main_Entity.TalkEntity;
import com.example.main_util.FuntionUtil;
import com.example.main_util.SpUtil;
import com.example.main_util.StringUtil;

//all global data change and set in here
public class GlobalID extends Application{
	private static final boolean log = false;
	private String TAG = "global";

    private static GlobalID mInstance = null;
	private final Context context = GlobalID.this;
	private String ID = "";
	private String urlServer = "";
//	private String urlServer = "192.168.1.10";
	private String DBurl = "";
//	private String DBurl = urlServer;
	private int SERVERPORT = 0;
	private int TIMEOUT = 0;
	private int m_rate = StringUtil.M_RATE;

	Calendar sDate = Calendar.getInstance();
	Calendar eDate = Calendar.getInstance();
	private String startDate = "2013-01-01 00:00:00";
	private String endDate = StringUtil.SIMPLE_DATEFORMAT.format(eDate.getTime());

//	private ArrayList<String> all = new ArrayList<String>();
	private ArrayList<Ark_lineEntity> all = new ArrayList<Ark_lineEntity>();
//	private String now_ark = "";
	private int now_ark = 0;

	private int line = StringUtil.LINE;
	private int line_gps = 20;

	public Toast toast = null;
	ProgressDialog pd = null;

	private String PW = "";
//	private boolean id_check = false;
//	private boolean pw_check = false;

	private boolean sound = false;
	private boolean shake = false;

	private ClipboardManager clipboard = null;
	
	private Socket socket = new Socket();
	
	private boolean login_code = false;
	private boolean break_code = false;
	private boolean heart_code = false;
	private boolean send_code = false;
	
	private boolean bus_code = false;
	private boolean gps_code = false;
	private boolean msg_code  = false;
	private boolean img_code = false;
	
	private boolean bus_push = false;
	private boolean msg_push  = false;
	private boolean img_push = false;

	private boolean bus_push_UnGet = false;
	private boolean msg_push_UnGet = false;
	private boolean img_push_UnGet = false;
	
	private boolean bus_change = true;
	private boolean msg_change  = true;
	private boolean img_change = true;
	private boolean all_change = false;
	
	private boolean check_push = true;
	
	public void time_change(){
		bus_change = true;
		msg_change = true;
		img_change = true;
	}

	/***********百度野***********/
	@Override
    public void onCreate() {
	    super.onCreate();
		mInstance = this;
	}
	public static GlobalID getInstance() {
		return mInstance;
	}
	
	/***********百度野***********/

//	public List<bussinessEntity> BusDataArrays = new ArrayList<bussinessEntity>();
//	public List<bussinessEntity> BusDeleteArrays = new ArrayList<bussinessEntity>();
	
	public List<BandWEntity> BandWArrays = new ArrayList<BandWEntity>();
//	public List<TestEntity> MsgDataArrays = new ArrayList<TestEntity>();
//	public List<TestEntity> MsgDeleteArrays = new ArrayList<TestEntity>();
//	public List<imageEntity> ImgDataArrays = new ArrayList<imageEntity>();
//	public List<imageEntity> ImgDeleteArrays = new ArrayList<imageEntity>();
	public List<OverlayEntity> GpsDataArrays = new ArrayList<OverlayEntity>();
	public List<OverlayEntity> GpsDeleteArrays = new ArrayList<OverlayEntity>();
	
	private int BusUpdateLines = 0;
	private int GpsUpdateLines = 0;
	private int MsgUpdateLines = 0;
	private int ImgUpdateLines = 0;
	
	private int current_code = -1;
	private int push = 0;

	int notificationRef = 1;
	Notification notification = null;
	NotificationManager notificationManager = null;
	// Text to display in the status bar when the notification is launched
	String tickerText = "后台接收数据";
	// Text to display in the extended status window
	String expandedText = "后台运行"; 
	// Title for the expanded status
	String expandedTitle = "岸上客户端";
	String svcName = Context.NOTIFICATION_SERVICE;
	Intent intent = new Intent();
	int icon = R.drawable.shop;
	Thread test_thread = new Thread();
	test b = new test();
	static int cnt = 0;

	/*
	 * 这个东东用来设置它是否需要后台通知的就是notification，任务栏那个
	 * 因为我是在onPause或者onSaveInstance这些地方写的，所以，有时候跳入下个界面的时候也会触发。
	 * 当然，也可以考虑写在onStop里面。
	 */	
	public boolean un_stop = false;
	
	public boolean is_sended = false;
	//pagerAdapter
	public com.example.main.NewMainActivity.MyPagerAdapter mpAdapter;
	
	private static String FILENAME = "data_info";
	
	public List<TalkEntity> TalkList = new ArrayList<TalkEntity>();
	
	public void showDialog(String title, String content,Context context) {
		// TODO Auto-generated method stub
		if(pd!=null)pd.dismiss();
		 new AlertDialog.Builder(context).  
			setTitle(title).setMessage(content).setPositiveButton("确定", new DialogInterface.OnClickListener() {  
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					}  
				}).show();
	}
	
	public void PD(Context context,String title, String content){
		pd = ProgressDialog.show(context, title, content);
	}
	
	public void cancelPD(){
		if(pd!=null)pd.dismiss();
	}
	
	public void showCopyed(Context context){
		if(toast != null)toast.cancel();
		toast = Toast.makeText(context, "已经复制到粘贴板", Toast.LENGTH_LONG);
		toast.show();
	}
	
	class test implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			ServiceConnection conn = new ServiceConnection() {
				
				@Override
				public void onServiceConnected(ComponentName name, IBinder service) {   
					Log.v("test", "onServiceConnected");   
					}   
				public void onServiceDisconnected(ComponentName name) {   
					Log.v("test", "onServiceDisconnected");   
					}
				};

				startService(new Intent(GlobalID.this, DemoService.class));
//				bindService(new Intent(GlobalID.this, DemoService.class), conn, BIND_AUTO_CREATE);
				
			while(true){
				try {
//					int test = (int)(Math.random()*5 + 2);
//					Thread.sleep((long)test*10000);
					Thread.sleep(60000);
					Log.v(TAG,"background running current_code: "+String.valueOf(current_code));
//					cnt++;
////					Context context = getApplicationContext();
////					notification.tickerText = tickerText;
////					notification.contentView.setTextViewText(1, expandedText);
//					
//					int j = test %3;
//					Log.v("j","j: "+String.valueOf(j));
//					switch(j){
//					case 0:
//						create_notification("新的1条新闻",String.valueOf(cnt)+"条信息",expandedTitle
//								,true,true,true);
//						current_code = 0;
//						setBus_push_UnGet(true);
//						break;
//					case 1:
//						create_notification("新的1条文字",String.valueOf(cnt)+"条信息",expandedTitle
//								,true,true,true);
//						current_code = 1;
//						setMsg_push_UnGet(true);
//						break;
//					case 2:
//						create_notification("新的1条图片",String.valueOf(cnt)+"条信息",expandedTitle
//								,true,true,true);
//						current_code = 2;
//						setImg_push_UnGet(true);
//						break;
//					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("a_thread","InterruptedException: "+e);
//					unbindService(conn);
					stopService(new Intent(GlobalID.this, DemoService.class));
					break;
				}
			}
			Log.v("test","over");
		}
	}
	
	@SuppressWarnings("deprecation")
	public void create_notification(String tickerText ,String expandedText,String expandedTitle
			,boolean light,boolean is_shake,boolean is_sound,String className){
		notificationManager = (NotificationManager)getSystemService(svcName);
		notificationManager.cancel(notificationRef);
		// Choose a drawable to display as the status bar icon 

		// The extended status bar orders notification in time order
		long when = System.currentTimeMillis();
		notification = new Notification(icon, tickerText, when);
		Context context = getApplicationContext();

		// Intent to launch an activity when the extended text is clicked
//		intent = new Intent(this, loginActivity.class);
		Log.v("notification","className = "+className);
		try {
			intent = new Intent(getApplicationContext(),

			        Class.forName(className)); // 加载类，如果直接通过类名，会在点击时重新加载页面，无法恢复最后页面状态。
			
			PendingIntent launchIntent = PendingIntent.getActivity(context, 0, intent, 0);
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.defaults |= Notification.DEFAULT_LIGHTS;
			if(light)notification.flags |= Notification.FLAG_SHOW_LIGHTS;
			if(is_shake && shake)notification.defaults |= Notification.DEFAULT_VIBRATE;
			if(is_sound && sound)notification.defaults |= Notification.DEFAULT_SOUND;
			notification.setLatestEventInfo(context, expandedTitle,expandedText,launchIntent); 
			notificationManager.notify(notificationRef, notification);
			} catch (ClassNotFoundException ex) {
				Log.v("notification","ClassNotFoundException:"+ex);
			}

		//test
		if(!test_thread.isAlive()){
			test_thread = new Thread (b);
			test_thread.start();
		}
	}
	
	public void cancel_notification(){
		notificationManager = (NotificationManager)getSystemService(svcName);
		notificationManager.cancel(notificationRef);
		notification = null;
		push = 0;
		
		//test
		if(test_thread.isAlive()){
			test_thread.interrupt();
			cnt = 0;
		}
	}
	
	public Socket getSocket() {
		if(socket != null)return socket;
		else{
			setSocket();
			Log.v("getSocket","here");
			if(socket != null){
				if(this.ID.equals("")){
					start(context);
				}
				try {
			        try {
			        	int Len_total_length = 4;
			        	int Cmd_id_length = 4;
			        	int Seq_id_length = 4;
			        	int Uid_length = 20;
//			        	int Uid_length = 4;
			        	int pwd_length = 20;
			        	int ver_length = 4;
			        	int flag_length = 1;
			        	int reserv_length = 8;
			        	
			        	int Len_total = Len_total_length
        	                       +Cmd_id_length
        	                       +Seq_id_length
        	                       +Uid_length
        	                       +pwd_length
        	                       +ver_length
        	                       +flag_length
        	                       +reserv_length;
			        	Log.v("Len_total",":"+String.valueOf(Len_total));
			        	int Cmd_id = 0x00040001;
			        	int Seq_id = 0;
			        	String Uid = new String(ID.getBytes("GB2312"),"8859_1");
			        	String pwd = new String(ID.getBytes("GB2312"),"8859_1");
			        	int ver = 0;
			        	char flag = '\0';
			        	String reserv = "";
			        	
			        	int length = 0;
			        	
			        	byte[] send = new byte[Len_total];
			        	length = send_head(length, send, Len_total_length, Len_total, Cmd_id_length, Cmd_id, Seq_id_length, Seq_id);
			        	
			        	Log.v("length","3:"+String.valueOf(length));
			            for(int i = 0;i<Uid_length;i++){
			            	if(i<Uid.length())
			            		send[length+i] = (byte)(Uid.charAt(i));
//		            			send[length+i] = (byte)(0 >> 24-i*8);
			            }
			            length += Uid_length;
			        	Log.v("length","4:"+String.valueOf(length));
			            for(int i = 0;i<pwd_length;i++){
			            	if(i<pwd.length())
			            		send[length+i] = (byte)(pwd.charAt(i));
			            }
			            length += pwd_length;
			        	Log.v("length","5:"+String.valueOf(length));
			            for(int i = 0;i<ver_length;i++){
				            send[length+i] = (byte)(ver >> 24-i*8);
			            }
			            length += ver_length;
			        	Log.v("length","6:"+String.valueOf(length));
			            for(int i = 0;i<flag_length;i++){
				            send[length+i] = (byte)(flag);
			            }
			            length += flag_length;
			        	Log.v("length","7:"+String.valueOf(length));
			            for(int i = 0;i<reserv_length;i++){
			            	if(i<reserv.length())
				            send[length+i] = (byte)(reserv.charAt(i));
			            }
			            length += reserv_length;
			        	Log.v("length","8:"+String.valueOf(length));
			            
			            OutputStream os = socket.getOutputStream();
                       	os.write(send);
                       	
                       	for(long exitTime = 0; exitTime < getTIMEOUT() ;exitTime += getM_rate()){
//				        	Log.v("login","for");
	                       	try {
								Thread.sleep(getM_rate());
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
				                Log.e("err2", "S: Error" + e);
							}
	                       	if(isLogin_code()){
	                       		break;
	                       	}
                       	}
                       	if(isLogin_code()){
	        				 heart();
                       	}
                       	else{
                       		return null;
                       	}
			        } catch(SocketTimeoutException e) {
		                Log.e("err0", "S: Error" + e);
		                return null;
		        }
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
	                Log.e("err1", "S: Error" + e1);
	                return null;
				}
				return socket;
			}
			else return null;
		}
	}
	
	public void setSocket() {
		Thread setsocket = new Thread(){
			public void run(){
				FuntionUtil.doSth();
				
				try {
					InetSocketAddress socketAdd = new InetSocketAddress(urlServer, SERVERPORT);
					
					socket.connect(socketAdd, TIMEOUT);
					Thread rec = new Thread(new recieve());
					rec.start();
					heart();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.v("global setSocket", "UnknownHostException: "+e);
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.v("global setSocket", "IOException: "+e);
					return;
				}
			}
		};
		setsocket.start();
	}
	
	public void heart(){
		Thread heart = new Thread(){
			int un_work = 0;
			public void run(){
				OutputStream os;
				try {
					os = socket.getOutputStream();
				while(true){
//					Log.v("heart", "beating");
					sleep(5000);
					
					if(heart_code){
//						Log.v("heart", "heart_code true");
						heart_code = false;
						continue;
					}

					else{
			            int Len_total_length = 4;
			        	int Cmd_id_length = 4;
			        	int Seq_id_length = 4;
			        	int Uid_length = 20;
			        	int Len_total = Len_total_length
        	                       +Cmd_id_length
        	                       +Seq_id_length
        	                       +Uid_length;
//			        	Log.v("heart","Len_total:"+String.valueOf(Len_total));
			        	int Cmd_id = 0x00040003;
			        	int Seq_id = 0;
			        	String Uid = new String(ID.getBytes("GB2312"),"8859_1");
			        	
			        	int length = 0;
			        	
			        	byte[] send = new byte[Len_total];
			        	length = send_head(length, send, Len_total_length, Len_total, Cmd_id_length, Cmd_id, Seq_id_length, Seq_id);
			        	
			        	for(int i = 0;i<Uid_length;i++){
			            	if(i<Uid.length())
			            		send[length+i] = (byte)(Uid.charAt(i));
			            }
				        length += Uid_length;
//				    	Log.v("heart","length final:"+String.valueOf(length));
				    	
		               	os.write(send);
		               	
		               	for(long exitTime = 0;exitTime < TIMEOUT;exitTime += m_rate){
			        		try {
//			               		Log.v("heart", "sleep");
								sleep(m_rate);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	                       	if(heart_code){
//			               		Log.v("heart", "heart_code true");
	                       		break;
	                       	}	
			        	}
		               	if(heart_code){
		               		un_work = 0;
		               		continue;
		               	}
		               	else{
		               		Log.v("heart", "die");
		               		un_work++;
		               		if(un_work>3){
		               			socket = null;
		               			break;
		               		}
		               		else continue;
		               	}
					}
				}

				} catch (IOException e1) {
					// TODO Auto-generated catch block
               		Log.v("heart", "die1" +e1);
					e1.printStackTrace();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
               		Log.v("heart", "die2" + e1);
					e1.printStackTrace();
				}
			}
		};
		heart.start();
	}
	
	public void closeSocket(){
		if(socket!=null)
			try {
				int Len_total_length = 4;
	        	int Cmd_id_length = 4;
	        	int Seq_id_length = 4;
	        	int Uid_length = 20;
	        	int reserv_length = 8;
	        	
	        	int Len_total = Len_total_length
	                       +Cmd_id_length
	                       +Seq_id_length
	                       +Uid_length
	                       +reserv_length;
	        	Log.v("Len_total",":"+String.valueOf(Len_total));
	        	int Cmd_id = 0x00040002;
	        	int Seq_id = 0;
	        	String Uid = new String(ID.getBytes("GB2312"),"8859_1");
	        	String reserv = "";

	        	int length = 0;
	        	byte[] send = new byte[Len_total];
	        	length = send_head(length, send, Len_total_length, Len_total, Cmd_id_length, Cmd_id, Seq_id_length, Seq_id);
	        	
	        	for(int i = 0;i<Uid_length;i++){
	            	if(i<Uid.length())
	            		send[length+i] = (byte)(Uid.charAt(i));
	            }
	            length += Uid_length;
	            
	            for(int i = 0;i<reserv_length;i++){
	            	if(i<reserv.length())
	            		send[length+i] = (byte)(reserv.charAt(i));
	            }
	            length += reserv_length;
	            
	            OutputStream os = socket.getOutputStream();
	            os.write(send);
	            
	            for( long exitTime = 0;exitTime < TIMEOUT/2 ;exitTime += m_rate){
					try {
						Thread.sleep(m_rate);
						Log.v("clear", "sleep");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			       	if(break_code){
						Log.v("clear", "break_code");
			       		break;
			       	}
				}
	            
				socket.close();
				socket = null;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.e(TAG,"close socket err: " + e1);
			}
	}
	
	/*****************recieve thread******************/
	public class recieve implements Runnable {
		
		private static final int AU_CONN_RESP = 0x80040001;
		private static final int AU_DISCONN_RESP = 0x80040002;
		private static final int AU_HB_RESP = 0x80040003;
		private static final int AU_MO_CUST_DATA_RESP = 0x80040004;
		private static final int AU_MT_CUST_DATA = 0x00040005;
		private static final int AU_SQL_RESP = 0x80040006;
		private static final int AU_SQL_RESULT = 0x00040007;
		private static final int AU_PUSH_TYPE = 0x00040008;
		
		private static final byte bussiness = 0x00;
		private static final byte weather = 0x01;
		private static final byte text = 0x02;
		private static final byte image = 0x03;
		
		private static final int HEAD_LENGTH = 12;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.v("recieve", "start");
			byte[] line = new byte[32];
			int length;
			int Cmd_id;
	    	int Seq_id;
			int cnt = 10;
			int un_work = 0;
			long un_work_time = 0;
	    	
			while(true){
				if(socket!=null){
					try{
						socket.setSoTimeout(TIMEOUT*5);
						InputStream is = socket.getInputStream();
						Log.v("recieve", "recieving");
						length = 0;
						Cmd_id = 0;
						Seq_id = 0;
						is.read(line,0,4);
						length = byte2int(line);
						Log.v("recieve", "length = "+String.valueOf(length));
						is.read(line,0,4);
						Cmd_id = byte2int(line);
						Log.v("recieve", "Cmd_id = "+String.valueOf(Integer.toHexString(Cmd_id)));
						is.read(line,0,4);
						Seq_id = byte2int(line);
						
						if(length < HEAD_LENGTH){
							Log.v("recieve", "HEAD_LENGTH err");
							Thread.sleep(cnt*m_rate);
							cnt += 10;
							continue;
						}
						if(cnt>0)cnt--;
						switch(Cmd_id){
						
						//connect resp
						case AU_CONN_RESP:{
							if(length == 16){
								is.read(line,0,4);
								int login_code = byte2int(line);
								if( login_code == 0 )setLogin_code(true);
								Log.v("recieve", "AU_CONN_RESP setLogin_code = "+String.valueOf(byte2int(line)));
							}
							else{
								Log.v("recieve", "AU_CONN_RESP error "+String.valueOf(Cmd_id));
							}
						}break;
						
						//close socket resp
						case AU_DISCONN_RESP:{
							if(length == 16){
								is.read(line,0,4);
								int break_code = byte2int(line);
								if( break_code == 0 )setBreak_code(true);
								Log.v("recieve", "AU_DISCONN_RESP setBreak_code = "+String.valueOf(byte2int(line)));
							}
							else{
								Log.v("recieve", "AU_DISCONN_RESP error "+String.valueOf(Cmd_id));
							}
						}break;
						
						//heart beat resp
						case AU_HB_RESP:{
							if(length == 16){
								is.read(line,0,4);
								int heart_code = byte2int(line);
								if( heart_code == 0 )setHeart_code(true);
								Log.v("recieve", "AU_HB_RESP setHeart_code = "+String.valueOf(heart_code));
							}
							else{
								Log.v("recieve", "AU_HB_RESP error "+String.valueOf(Cmd_id));
							}
						}break;

						//send resp
						case AU_MO_CUST_DATA_RESP:{
							if(length == 16){
								is.read(line,0,4);
								int send_code = byte2int(line);
								if( send_code == 0 )setSend_code(true);
								Log.v("recieve", "AU_MO_CUST_DATA_RESP setSend_code = "+String.valueOf(byte2int(line)));
							}
							else{
								Log.v("recieve", "AU_MO_CUST_DATA_RESP error "+String.valueOf(Cmd_id));
							}
						}break;
						
						//recieve push
						case AU_MT_CUST_DATA:{
							if(length > 12){
								Log.v("recieve", "AU_MT_CUST_DATA");
//									is.read(line,0,4);
							}
							else{
								Log.v("recieve", "AU_MT_CUST_DATA error "+String.valueOf(Cmd_id));
							}
						}break;
						
						//SQL resp
						case AU_SQL_RESP:{
							if(length > 12){
								Log.v("recieve", "AU_SQL_RESP");
								is.read(line,0,4);
								int code = byte2int(line);
								Log.v("AU_SQL_RESP", "code: " + String.valueOf(code));
							}
						}break;
						
						//SQL recieve
						case AU_SQL_RESULT:{
							if(length > 12){
								Log.v("recieve", "AU_SQL_RESULT");
								is.read(line,0,1);
								byte type = (byte) line[0];
								Log.v("recieve", "type = "+String.valueOf(type));
								int data_length = length - HEAD_LENGTH - 1;
								byte[] line1 = new byte[data_length];
								Log.v("AU_SQL_RESULT","data_length:"+String.valueOf(data_length));
								boolean Insert_Head = false;
								
								byte[] tmp = new byte[data_length];
				        		int start = 0;
//									while(true){
//					        			 if(start+32>data_length)break;
//					        			 is.read(line,0,32);
//					        			 copy(line,tmp,start,32);
//					        			 start += 32;
//									}
//									Log.v("AU_SQL_RESULT","start:"+String.valueOf(start));
//									Log.v("AU_SQL_RESULT","data_length%32:"+String.valueOf(data_length%32));
//					        		is.read(line, 0, data_length%32);
//					        		copy(line,tmp,start,data_length%32);
				        		int readcnt = 0;
				        		while(data_length>0){
				        			readcnt = is.read(line1,0,data_length);
				        			if(readcnt > 0){
					        			copy(line1,tmp,start,readcnt);
					        			data_length -= readcnt;
					        			start += readcnt;
				        			}
				        		}
				        		
				        		
//					        		for(int i = tmp.length-10;i<tmp.length;i++){
//					        			Log.v("reader",String.valueOf(i)+"tmp:"+tmp[i]);
//				        			 }

				        		String reader = new String (tmp,"GB2312");
				        		Log.v("AU_SQL_RESULT","reader: "+reader);
				        		
				        		if(Seq_id == 0) Insert_Head = true;
								
								switch(type){
								
//								//get bussiness
//								case bussiness:{
//					        		Log.v("AU_SQL_RESULT","bussiness: ");
//					        		synchronized(BusDataArrays){
//						        		BusJson(reader,Insert_Head,BusDataArrays,BusDeleteArrays);
//						        		BusDataArrays.notify();
//					        		}
//					        		setBus_code(true);
//								}break;
//								
//								//get weather
//								case weather:{
//					        		Log.v("AU_SQL_RESULT","weather: ");
//					        		synchronized(WeaDataArrays){
//						        		WeaJson(reader,Insert_Head,WeaDataArrays,WeaDeleteArrays);
//						        		WeaDataArrays.notify();
//					        		}
//					        		setWea_code(true);
//								}break;
//								
//								//get text
//								case text:{
//					        		Log.v("AU_SQL_RESULT","text: ");
//					        		synchronized (MsgDataArrays) {
//						        		MsgJson(reader,Insert_Head,MsgDataArrays,MsgDeleteArrays);
//						        		MsgDataArrays.notify();
//									}
//					        		setMsg_code(true);
//								}break;
//								
//								//get image
//								case image:{
//					        		Log.v("AU_SQL_RESULT","image: ");
//					        		synchronized (ImgDataArrays) {
//						        		ImgJson(reader,Insert_Head,ImgDataArrays,ImgDeleteArrays);
//						        		ImgDataArrays.notify();
//					        		}
//					        		setImg_code(true);
//								}break;
								
								default:{
									Log.v("AU_SQL_RESP", "type error "+String.valueOf(type));
									
								}break;
								}
							}
							else{
								Log.v("recieve", "AU_SQL_RESP error "+String.valueOf(Cmd_id));
							}
						}break;
						case AU_PUSH_TYPE:{
							if(length > 12){
								Log.v("recieve", "AU_PUSH_TYPE");
//									byte type = (byte) line[0];
								
								is.read(line, 0, 4);
								int type = byte2int(line);
								switch(type){
								//get bussiness
								case 0:{
					        		Log.v("AU_PUSH_TYPE","bussiness");
					        		setBus_push(true);
					        		if(notification != null){
						        		current_code = 0;
					        			push++;
					        			create_notification("新闻新信息", String.valueOf(push) + "条新信息", expandedTitle, true, true, true,NewMainActivity.class.getName());
					        		}
								}break;
								
								//get weather
								case 1:{
					        		Log.v("AU_PUSH_TYPE","weather");
//					        		setWea_push(true);
					        		if(notification != null){
						        		current_code = 1;
					        			push++;
					        			create_notification("天气新信息", String.valueOf(push) + "条新信息", expandedTitle, true, true, true,NewMainActivity.class.getName());
					        		}
								}break;
								
								//get text
								case 2:{
					        		Log.v("AU_PUSH_TYPE","text");
					        		setMsg_push(true);
					        		if(notification != null){
						        		current_code = 2;
					        			push++;
					        			create_notification("文字新信息", String.valueOf(push) + "条新信息", expandedTitle, true, true, true,NewMainActivity.class.getName());
					        		}
								}break;
								
								//get image
								case 3:{
					        		Log.v("AU_PUSH_TYPE","image");
					        		setImg_push(true);
					        		if(notification != null){
						        		current_code = 3;
					        			push++;
					        			create_notification("图片新信息", String.valueOf(push) + "条新信息", expandedTitle, true, true, true,NewMainActivity.class.getName());
					        		}
								}break;
								
								default:{
									Log.v("AU_PUSH_TYPE", "type error "+String.valueOf(type));
									
								}break;
								}
								
//									switch(type){
//									//get bussiness
//									case bussiness:{
//						        		Log.v("AU_PUSH_TYPE","bussiness");
//						        		setBus_push(true);
//									}break;
//									
//									//get weather
//									case weather:{
//						        		Log.v("AU_PUSH_TYPE","weather");
//						        		setWea_push(true);
//									}break;
//									
//									//get text
//									case text:{
//						        		Log.v("AU_PUSH_TYPE","text");
//						        		setMsg_push(true);
//									}break;
//									
//									//get image
//									case image:{
//						        		Log.v("AU_PUSH_TYPE","image");
//						        		setImg_push(true);
//									}break;
//									
//									default:{
//										Log.v("AU_PUSH_TYPE", "type error "+String.valueOf(type));
//										
//									}break;
//									}
							}
						}break;
						
						default:{
							Log.v("recieve", "Cmd_id error "+String.valueOf(Cmd_id));
							is.read(line);
							try {
								Thread.sleep(cnt*m_rate);
								cnt += 10;
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							}
						}
					}catch(SocketException e){
						e.printStackTrace();
						Log.v("recieve", "SocketException err " + e);
						try {
							Thread.sleep(cnt*m_rate);
							cnt += 10;
							if((System.currentTimeMillis()-un_work_time) < 2000){
								un_work_time = System.currentTimeMillis();
								un_work++;
								if(un_work>3)socket = null;
							}
							else{
								un_work_time = System.currentTimeMillis();
								un_work = 0;
							}
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							Log.v("recieve", "InterruptedException err " + e1);
							
							if((System.currentTimeMillis()-un_work_time) < 2000){
								un_work_time = System.currentTimeMillis();
								un_work++;
								if(un_work>3)socket = null;
							}
							else{
								un_work_time = System.currentTimeMillis();
								un_work = 0;
							}
						}
						continue;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.v("recieve", "IOException err " + e);
						
						if((System.currentTimeMillis()-un_work_time) < 2000){
							un_work_time = System.currentTimeMillis();
							un_work++;
							if(un_work>3)socket = null;
						}
						else{
							un_work_time = System.currentTimeMillis();
							un_work = 0;
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.v("recieve", "InterruptedException err " + e);
						
						if((System.currentTimeMillis()-un_work_time) < 2000){
							un_work_time = System.currentTimeMillis();
							un_work++;
							if(un_work>3)socket = null;
						}
						else{
							un_work_time = System.currentTimeMillis();
							un_work = 0;
						}
					}
				}
				else break;
			}
		}
		
		int byte2int (byte[] line){
			int length;
			length = line[3] & 0xFF;
			length |= ((line[2] << 8) & 0xFF00);
			length |= ((line[1] << 16) & 0xFF0000);
			length |= ((line[0] << 24) & 0xFF000000);
			return length;
		}
		
		/***********Json for bussiness list******************/
//	    private boolean BusJson(String resultObj,boolean Insert_Head,List<bussinessEntity> mDataArrays,List<bussinessEntity> mDataArrays2){
//	    	boolean insert = false;
//	    	BusUpdateLines = 0;
////	    	Log.v("bus json", ": "+ resultObj);
//			try{
//				JSONArray jsonArray = new JSONArray(resultObj);
//				if(jsonArray.length()==0){
//					return false;
//					}
//				else{
//						for(int i = 0 ; i < jsonArray.length() ; i++){
//							insert = false;
//							bussinessEntity entity = new bussinessEntity();
//							JSONObject jsonObject = jsonArray.getJSONObject(i);
//							entity.setId(jsonObject.optString("Id"));
//							entity.setBussiness_Title(jsonObject.optString("Title"));
//							entity.setBussiness_detail(jsonObject.optString("Content"));
//							entity.setBussiness_PostTime(jsonObject.optString("PostTime"));
//							entity.setBussiness_Poster(jsonObject.optString("Poster"));
////							Log.v("bus","json "+ entity.getBussiness_PostTime());
////							Log.v("bus","json "+ entity.getBussiness_detail());
//
//							for(int j = mDataArrays.size()-1 ; j>-1 ; j--){
//								if(entity.getId().equals(mDataArrays.get(j).getId())){
//									insert = true;
//									break;
//								}
//							}
//							for(int j = mDataArrays2.size()-1 ; j>-1 ; j--){
//								Log.v("bus", "delete " + String.valueOf(j));
//								if(entity.getId().equals(mDataArrays2.get(j).getId())){
//									insert = true;
//									break;
//								}
//							}
//							if(insert)continue;
//							if(Insert_Head)mDataArrays.add(0,entity);
//							else mDataArrays.add(entity);
//							BusUpdateLines++;
////							Log.v("bus json", "updateLines " + String.valueOf(BusUpdateLines));
//					}
//				}
//			}catch(JSONException e){
//				e.printStackTrace();
//				Log.v("bus json","JSONException " + e);
//				return false;
//			}
//			//set current time for next refresh
////			startTime = mDataArrays.get(mDataArrays.size()-1).getBussiness_PostTime();
//			Log.v("bus json", "done");
//			return true;
//		}
	    /***********Json for bussiness list******************/
	    
	    /***********Json for weathersiness list******************/
//	    private boolean WeaJson(String resultObj,boolean Insert_Head,List<weatherEntity> mDataArrays,List<weatherEntity> mDataArrays2){
//	    	boolean insert = false;
//	    	WeaUpdateLines = 0;
//			try{
//				JSONArray jsonArray = new JSONArray(resultObj);
//				if(jsonArray.length()==0){
//					return false;
//					}
//				else{
//					for(int i =  0; i < jsonArray.length() ; i++){
//					insert = false;
//					weatherEntity entity = new weatherEntity();
//					JSONObject jsonObject = jsonArray.getJSONObject(i);
//					entity.setId(jsonObject.optString("Id"));
//					entity.setType(jsonObject.optString("Type"));
//					entity.setTitle(jsonObject.optString("Title"));
//					entity.setMsg(jsonObject.optString("Msg"));
//					entity.setPostTime(jsonObject.optString("PostTime"));
//					String img = jsonObject.optString("Imagefile");
////					Log.v("line","img: "+img);
////					Log.v("line","img: "+img.length());
//					byte[] img_line = new byte[img.length()];
//					for(int j = 0;j<img.length();j++){
////		            	if(i<Uid.length())
//						img_line[j] = (byte)(img.charAt(j));
//		            }
////					Log.v("line","length: "+img_line.length);
//					byte[] img_line2 = new byte[img_line.length/2];
//	     			  for(int k = 0;k<img_line2.length;k++){
//	     				  img_line2[k] = (byte) (img_line[2*k]-(img_line[2*k]<'A'?'0':img_line[2*k]<'a'?'A'-10:'a'-10));
//
//	     				  img_line2[k] = (byte) ((img_line2[k] << 4) & 0xF0);
//	     				  img_line2[k] += img_line[2*k+1]-(img_line[2*k+1]<'A'?'0':img_line[2*k+1]<'a'?'A'-10:'a'-10);
//	     			  }
//	  					if(getBitmapFromByte(img_line2)!=null){
//	  						entity.setWeather_picture(getBitmapFromByte(img_line2));
//	  					}
//	  	  		     		   else{
//	 	  		 				Bitmap good  = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
//	 	  						entity.setWeather_picture(good);
//	 	  						}
////					Log.v("weather","json "+ entity.getPostTime());
//					
//					for(int j = mDataArrays.size()-1 ; j>-1 ; j--){
//						if(entity.getId().equals(mDataArrays.get(j).getId())){
//							insert = true;
//							break;
//						}
//					}
//					for(int j = mDataArrays2.size()-1 ; j>-1 ; j--){
//						if(entity.getId().equals(mDataArrays2.get(j).getId())){
//							insert = true;
//							break;
//						}
//					}
//					if(insert)continue;
//					if(Insert_Head)mDataArrays.add(0,entity);
//					else mDataArrays.add(entity);
////					WeaUpdateLines++;
//					}
//				}
//			}catch(JSONException e){
//				e.printStackTrace();
//				return false;
//			}
//			return true;
//		}
	    /***********Json for weathersiness list******************/
	    
	    /***********Json******************/
//	    private boolean MsgJson(String resultObj,boolean Insert_Head,List<TestEntity> mDataArrays,List<TestEntity> mDataArrays2){
//	    	boolean insert = false;
//	    	MsgUpdateLines = 0;
//			try{
//				JSONArray jsonArray = new JSONArray(resultObj);
//				if(jsonArray.length()==0){
//					return false;
//					}
//				else{
//					for(int i = 0 ; i < jsonArray.length()  ; i++){
//						insert = false;
//						TestEntity entity = new TestEntity();
//						JSONObject jsonObject = jsonArray.getJSONObject(i);
//						entity.setMsg_id(jsonObject.optString("msg_id"));
//						entity.setTest_detail(jsonObject.optString("msg"));
//						entity.setTest_PostTime(jsonObject.optString("datetime_send"));
//						entity.setTest_ark_id(jsonObject.optString("ark_id"));
//						entity.setSend_user_id(jsonObject.optString("user_id"));
//						Log.v("msg_id","msg_id: "+ jsonObject.optString("msg_id"));
//						for(int j = mDataArrays.size()-1 ; j>-1 ; j--){
//							if(entity.getMsg_id().equals(mDataArrays.get(j).getMsg_id())){
//								insert = true;
//								break;
//							}
//						}
//						for(int j = mDataArrays2.size()-1 ; j>-1 ; j--){
//							if(entity.getMsg_id().equals(mDataArrays2.get(j).getMsg_id())){
//								insert = true;
//								break;
//							}
//						}
//						if(insert)continue;
//						
//						if(Insert_Head)mDataArrays.add(0,entity);
//						else mDataArrays.add(entity);
//						MsgUpdateLines++;
//					}
//				}
//				
//			}catch(JSONException e){
//				e.printStackTrace();
//			}
//			return true;
//		}
	    /***********Json******************/
	    
	    /***********Json for image list******************/
//	    private boolean ImgJson(String resultObj,boolean Insert_Head,List<imageEntity> mDataArrays,List<imageEntity> mDataArrays2){
//	    	boolean insert = false;
//	    	ImgUpdateLines = 0;
//			try{
//				JSONArray jsonArray = new JSONArray(resultObj);
//				if(jsonArray.length()==0){
//					return false;
//					}
//				else{
//					for(int i = 0 ; i < jsonArray.length()  ; i++){
//						insert = false;
//						imageEntity entity = new imageEntity();
//						JSONObject jsonObject = jsonArray.getJSONObject(i);
//						entity.setImg_id(jsonObject.optString("img_id"));
//						entity.setImage_datetime_send(jsonObject.optString("datetime_send"));
//						
//						//set where to download picture
//						String pic = jsonObject.optString("img_name");
////						Log.v("pic","http://"+ getDBurl() +"/user/images" + pic);
//						byte[] pic_line = new byte[pic.length()];
//						for(int j = 0;j<pic.length();j++){
//							pic_line[j] = (byte)(pic.charAt(j));
//			            }
//						
//						byte[] pic_line2 = new byte[pic_line.length/2];
//		     			  for(int k = 0;k<pic_line2.length;k++){
//		     				  pic_line2[k] = (byte) (pic_line[2*k]-(pic_line[2*k]<'A'?'0':pic_line[2*k]<'a'?'A'-10:'a'-10));
//
//		     				  pic_line2[k] = (byte) ((pic_line2[k] << 4) & 0xF0);
//		     				  pic_line2[k] += pic_line[2*k+1]-(pic_line[2*k+1]<'A'?'0':pic_line[2*k+1]<'a'?'A'-10:'a'-10);
//		     			  }
//		  					if(getBitmapFromByte(pic_line2)!=null){
//		  						entity.setImage_picture(getBitmapFromByte(pic_line2));
//		  					}
//		  	  		     		   else{
//		 	  		 				Bitmap good  = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
//		 	  						entity.setImage_picture(good);
//		 	  						}
//
//						entity.setImage_ark_id(jsonObject.optString("ark_id"));
//						entity.setSend_user_id(jsonObject.optString("user_id"));
//						
//						for(int j = mDataArrays.size()-1 ; j>-1 ; j--){
//							if(entity.getImg_id().equals(mDataArrays.get(j).getImg_id())){
//								insert = true;
//								break;
//							}
//						}
//						for(int j = mDataArrays2.size()-1 ; j>-1 ; j--){
//							if(entity.getImg_id().equals(mDataArrays2.get(j).getImg_id())){
//								insert = true;
//								break;
//							}
//						}
//						if(insert)continue;
//						if(Insert_Head)mDataArrays.add(0,entity);
//						else mDataArrays.add(entity);
//						ImgUpdateLines++;
//						}
//					}
//			}catch(JSONException e){
//				e.printStackTrace();
//			}
//			return true;
//		}
	    /***********Json for image list******************/
	    
	    /**********************change byte[] to bitmap************************/
	    public Bitmap getBitmapFromByte(byte[] temp){   
	        if(temp != null){   
	            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);   
	            return bitmap;
	        }else{   
	            return null;   
	        }   
	    } 
	    /**********************change byte[] to bitmap************************/
	    
	    public boolean copy (byte[] src, byte[] dest,int start,int read_length){
	    	for(int i = 0;i<read_length;i++){
	    		dest[start+i] = src[i];
	    	}
			return true;
	    }

	}
	/*****************recieve thread******************/
	
	int send_head(int length,byte[] send,int Len_total_length,int Len_total,int Cmd_id_length,int Cmd_id,int Seq_id_length,int Seq_id){
		for(int i = 0;i<Len_total_length;i++){
            send[length + i] = (byte)(Len_total >> 24-i*8);
        }
        length += Len_total_length;
//    	Log.v("heart ","length1:"+String.valueOf(length));
        for(int i = 0;i<Cmd_id_length;i++){
            send[length + i] = (byte)(Cmd_id >> 24-i*8);
        }
        length += Cmd_id_length;
//    	Log.v("heart ","length2:"+String.valueOf(length));
        for(int i = 0;i<Seq_id_length;i++){
            send[length+i] = (byte)(Seq_id >> 24-i*8);
        }
        length += Seq_id_length;
//    	Log.v("heart ","length3:"+String.valueOf(length));
		return length;
	}
	
	public boolean setAll(String line) {
		try{
			all.clear();
			JSONArray jsonArray = new JSONArray(line);
			if(jsonArray.length()==0){
				setNow_ark(-1);
				return false;
				}
			else{
				for(int i = jsonArray.length()-1 ; i > -1  ; i--){
					Ark_lineEntity entity = new Ark_lineEntity();
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					entity.setArk_id(jsonObject.optString("ark_id"));
					entity.setArk_no(jsonObject.optString("ark_no"));
					all.add(0,entity);
					}
				}
			}catch(JSONException e){
				e.printStackTrace();
				}
		all_change = true;
		return true;
	}
	
//	public void clear() {
////		if(!this.id_check)this.setID("");
////		if(!this.pw_check){
////			this.setPW("");
//////			this.setID("");
////		}
////		this.setStartDate();
//		this.setEndDate();
////		this.all.clear();
////		this.setNow_ark("");
//		File fil = Environment.getExternalStorageDirectory();
//		String url = createFolder(fil.getParent()+ File.separator +fil.getName(),"wingsofark");
//		Properties prop = new Properties();
//		try {
//			//调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
//            //强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
//            OutputStream fos = new FileOutputStream(url+File.separator+"global.properties");
//            if(this.getID()!=null)prop.setProperty("ID", this.getID());
//            if(this.getPW()!=null)prop.setProperty("PW", this.getPW());
////            prop.setProperty("id_check", String.valueOf(this.isId_check()));
////            prop.setProperty("pw_check", String.valueOf(this.isPw_check()));
//            prop.setProperty("sound", String.valueOf(this.isSound()));
//            prop.setProperty("shake", String.valueOf(this.isShake()));
//            if(this.getStartDate()!=null)prop.setProperty("startDate", this.getStartDate());
//            if(this.getEndDate()!=null)prop.setProperty("endDate", this.getEndDate());
//            prop.setProperty("line", String.valueOf(5));
//            
//            if(this.getUrlServer()!=null)prop.setProperty("urlServer", this.getUrlServer());
//            if(this.getDBurl()!=null)prop.setProperty("DBurl", this.getDBurl());
//            prop.setProperty("SERVERPORT", String.valueOf(this.getSERVERPORT()));
//            prop.setProperty("TIMEOUT", String.valueOf(this.getTIMEOUT()));
//            //以适合使用 load 方法加载到 Properties 表中的格式，
//            //将此 Properties 表中的属性列表（键和元素对）写入输出流
//            prop.store(fos, "");
//            } catch (IOException e) {
//            	e.printStackTrace();
//    			Log.v(TAG, "out IOException" + e);
//            }
//	}
//	
//	public void start(){
//		
//		// 判断API>=11
//		if(Build.VERSION.SDK_INT >= 11){
//	        clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
//	        }
//
//		File fil = Environment.getExternalStorageDirectory();
//		File file = new File(fil.getParent()+ File.separator +fil.getName()+"/wingsofark/global.properties");
//		try {
//			FileInputStream fin = new FileInputStream(file);
//			Properties p = new Properties();
//			p.load(fin);
////			Log.v(TAG, "properties   " + p.getProperty("ID"));
////			Log.v(TAG, "properties   " + p.getProperty("PW"));
////			Log.v(TAG, "properties   " + p);
//			if(p.getProperty("ID")!=null)this.setID(p.getProperty("ID"));
//			if(p.getProperty("PW")!=null)this.setPW(p.getProperty("PW"));
////			if(p.getProperty("id_check")!=null)this.setId_check(Boolean.parseBoolean(p.getProperty("id_check")));
////			if(p.getProperty("pw_check")!=null)this.setPw_check(Boolean.parseBoolean(p.getProperty("pw_check")));
//			if(p.getProperty("sound")!=null)this.setSound(Boolean.parseBoolean(p.getProperty("sound")));
//			if(p.getProperty("shake")!=null)this.setShake(Boolean.parseBoolean(p.getProperty("shake")));
//			if(p.getProperty("startDate")!=null)this.setStartDate(p.getProperty("startDate"));
//			if(p.getProperty("endDate")!=null)this.setEndDate(p.getProperty("endDate"));
//			if(p.getProperty("line")!=null)this.setLine(Integer.parseInt(p.getProperty("line")));
//			
//			if(p.getProperty("urlServer")!=null)this.setUrlServer(p.getProperty("urlServer"));
//			if(p.getProperty("DBurl")!=null)this.setDBurl(p.getProperty("DBurl"));
//			if(p.getProperty("SERVERPORT")!=null)this.setSERVERPORT(Integer.parseInt(p.getProperty("SERVERPORT")));
//			if(p.getProperty("TIMEOUT")!=null)this.setTIMEOUT(Integer.parseInt(p.getProperty("TIMEOUT")));
////			Log.v(TAG, "properties  setId_check " + this.isId_check());
////			Log.v(TAG, "properties  setPw_check " + this.isPw_check());
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Log.v(TAG, "in FileNotFoundException"+ e);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Log.v(TAG, "in IOException"+ e);
//		}
//	}
	
	public void start(Context context){
		String TAG = "start(context)";
		
		if(Build.VERSION.SDK_INT >= 11){
	        clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
	        }
        
		try{
			ID = SpUtil.read(context,"ID");
		}catch(Exception e){
			e.printStackTrace();
			Log.v(TAG, "Exception "+ e);
		}

		try{
			PW = SpUtil.read(context,"PW");
		}catch(Exception e){
			e.printStackTrace();
			Log.v(TAG, "Exception "+ e);
		}
		
		try{
			sound = SpUtil.readBoolean(context,"sound");
		}catch(Exception e){
			e.printStackTrace();
			Log.v(TAG, "Exception "+ e);
		}

		try{
			shake = SpUtil.readBoolean(context,"shake");
		}catch(Exception e){
			e.printStackTrace();
			Log.v(TAG, "Exception "+ e);
		}
		
//		try{
//			String startDate = SpUtil.read(context,"startDate");
//			if(!startDate.equals("")){
//				this.startDate = startDate;
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//			Log.v(TAG, "Exception "+ e);
//		}
//
//		try{
//			String endDate = SpUtil.read(context,"endDate");
//			if(!endDate.equals("")){
//				this.endDate = endDate;
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//			Log.v(TAG, "Exception "+ e);
//		}

		try{
			String line = SpUtil.read(context,"line");
			if(line.equals("")){
				line = String.valueOf(StringUtil.LINE);
				SpUtil.write(context, line, "line");
			}
			this.line = Integer.parseInt(line);
		}catch(Exception e){
			e.printStackTrace();
			Log.v(TAG, "Exception "+ e);
		}

		try{
			getUrlServer(context);
		}catch(Exception e){
			e.printStackTrace();
			Log.v(TAG, "Exception "+ e);
		}

		try{
			getDBurl(context);
		}catch(Exception e){
			e.printStackTrace();
			Log.v(TAG, "Exception "+ e);
		}

		try{
			String SERVERPORT = SpUtil.read(context,"SERVERPORT");
			if(SERVERPORT.equals("")){
				SERVERPORT = String.valueOf(StringUtil.SERVERPORT);
				SpUtil.write(context, SERVERPORT, "SERVERPORT");
			}
			this.SERVERPORT = Integer.parseInt(SERVERPORT);
		}catch(Exception e){
			e.printStackTrace();
			Log.v(TAG, "Exception "+ e);
		}

		try{
			String TIMEOUT = SpUtil.read(context,"TIMEOUT");
			if(TIMEOUT.equals("")){
				TIMEOUT = String.valueOf(StringUtil.TIMEOUT);
				SpUtil.write(context, TIMEOUT, "TIMEOUT");
			}
			this.TIMEOUT = Integer.parseInt(TIMEOUT);
		}catch(Exception e){
			e.printStackTrace();
			Log.v(TAG, "Exception "+ e);
		}
	}
	
//	public String Bus2String(){
//		String encode = "[";
//		for(int i = 0 ; i < BusDataArrays.size();){
//			encode += "{";
//
//			encode += "\"Id\":\"" + BusDataArrays.get(i).getId() + "\";";
//			encode += "\"PostTime\":\"" + BusDataArrays.get(i).getBussiness_PostTime() + "\";";
//			encode += "\"Content\":\"" + BusDataArrays.get(i).getBussiness_detail() + "\";";
//			encode += "\"Title\":\"" + BusDataArrays.get(i).getBussiness_Title() + "\";";
//			encode += "\"Poster\":\"" + BusDataArrays.get(i).getBussiness_Poster() + "\"";
//			
//			encode += "}";
//			if(++i < BusDataArrays.size()){
////				Log.v("Bus2String","i++ = "+i+" BusDataArrays.size() = "+BusDataArrays.size());
//				encode += ",";
//			}
//		}
//		encode += "]";
////		Log.v("Bus2String","encode = "+encode);
//		return encode;		
//	}
	
//	public void setBusDataArrays(String decode) {
//		try{
//			JSONArray jsonArray = new JSONArray(decode);
//			if(jsonArray.length()==0){
//				return;
//				}
//			else{
//				for(int i = jsonArray.length()-1 ; i > -1  ; i--){
//					bussinessEntity entity = new bussinessEntity();
//					JSONObject jsonObject = jsonArray.getJSONObject(i);
//					entity.setId(jsonObject.optString("Id"));
//					entity.setBussiness_Title(jsonObject.optString("Title"));
//					entity.setBussiness_detail(jsonObject.optString("Content"));
//					entity.setBussiness_PostTime(jsonObject.optString("PostTime"));
//					entity.setBussiness_Poster(jsonObject.optString("Poster"));
//					BusDataArrays.add(entity);
//				}
//			}
//		}catch(JSONException e){
//			e.printStackTrace();
//			return;
//		}
//	}
	
//	public String Msg2String(){
//		String encode = "[";
//		for(int i = 0 ; i < MsgDataArrays.size();){
//			encode += "{";
//
//			encode += "\"Msg_id\":\"" + MsgDataArrays.get(i).getMsg_id() + "\";";
//			encode += "\"Send_user_id\":\"" + MsgDataArrays.get(i).getSend_user_id() + "\";";
//			encode += "\"Test_ark_id\":\"" + MsgDataArrays.get(i).getTest_ark_id() + "\";";
//			encode += "\"Test_detail\":\"" + MsgDataArrays.get(i).getTest_detail() + "\";";
//			encode += "\"Test_PostTime\":\"" + MsgDataArrays.get(i).getTest_PostTime() + "\"";
//			
//			encode += "}";
//			if(++i < MsgDataArrays.size()){
//				encode += ",";
//			}
//		}
//		encode += "]";
//		return encode;		
//	}

//	public void setMsgDataArrays(String decode) {
//		try{
//			JSONArray jsonArray = new JSONArray(decode);
//			if(jsonArray.length()==0){
//				return;
//				}
//			else{
//				for(int i = jsonArray.length()-1 ; i > -1  ; i--){
//					TestEntity entity = new TestEntity();
//					JSONObject jsonObject = jsonArray.getJSONObject(i);
//					entity.setMsg_id(jsonObject.optString("Msg_id"));
//					entity.setSend_user_id(jsonObject.optString("Send_user_id"));
//					entity.setTest_ark_id(jsonObject.optString("Test_ark_id"));
//					entity.setTest_detail(jsonObject.optString("Test_detail"));
//					entity.setTest_PostTime(jsonObject.optString("Test_PostTime"));
//					MsgDataArrays.add(entity);
//				}
//			}
//		}catch(JSONException e){
//			e.printStackTrace();
//			return;
//		}
//	}
	
//	public String Img2String(){
//		String encode = "[";
//		for(int i = 0 ; i < ImgDataArrays.size();){
//			encode += "{";
//
//			encode += "\"Img_id\":\"" + ImgDataArrays.get(i).getImg_id() + "\";";
//			encode += "\"Send_user_id\":\"" + ImgDataArrays.get(i).getSend_user_id() + "\";";
//			encode += "\"Image_ark_id\":\"" + ImgDataArrays.get(i).getImage_ark_id() + "\";";
//			encode += "\"Image_datetime_send\":\"" + ImgDataArrays.get(i).getImage_datetime_send() + "\";";
//			encode += "\"Pic\":\"" + ImgDataArrays.get(i).getPic() + "\"";
//			
//			encode += "}";
//			if(++i < ImgDataArrays.size()){
//				encode += ",";
//			}
//		}
//		encode += "]";
//		return encode;		
//	}

//	public void setImgDataArrays(final String decode) {
//		try{
//			JSONArray jsonArray = new JSONArray(decode);
//			if(jsonArray.length()==0){
//				return;
//				}
//			else{
//				for(int i = jsonArray.length()-1 ; i > -1  ; i--){
//					imageEntity entity = new imageEntity();
//					JSONObject jsonObject = jsonArray.getJSONObject(i);
//					entity.setImg_id(jsonObject.optString("Img_id"));
//					entity.setSend_user_id(jsonObject.optString("Send_user_id"));
//					entity.setImage_datetime_send(jsonObject.optString("Image_datetime_send"));
//					entity.setImage_ark_id(jsonObject.optString("Image_ark_id"));
//					String pic = jsonObject.optString("Pic");
//					entity.setPic(pic);
//					Bitmap bit = downloadPic("http://"+ getDBurl() +"/user/images" + pic);
//					entity.setImage_picture(bit);
//					ImgDataArrays.add(entity);
//				}
//			}
//		}catch(JSONException e){
//			e.printStackTrace();
//			return;
//		}
//	}
	
	/***********down picture from urlPic******************/
    private Bitmap downloadPic(String urlPic){
		try {
			URL url = new URL(urlPic);
			InputStream is = url.openStream();
			Bitmap bit = BitmapFactory.decodeStream(is);
			return bit ;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    /***********down picture from URL******************/
    

	public String Ark_line2String(){
		String encode = "[";
		for(int i = 0 ; i < all.size();){
			encode += "{";

			encode += "\"ark_id\":\"" + all.get(i).getArk_id() + "\";";
			encode += "\"ark_no\":\"" + all.get(i).getArk_no() + "\"";
			
			encode += "}";
			if(++i < all.size()){
//				Log.v("Bus2String","i++ = "+i+" BusDataArrays.size() = "+BusDataArrays.size());
				encode += ",";
			}
		}
		encode += "]";
//		Log.v("Bus2String","encode = "+encode);
		all_change = false;
		return encode;		
	}

	/*****************createFolder ******************/
	public static String createFolder(String createUrl,String name) {

        String createFileROOT3 = createUrl + File.separator + name;
        // 创建文件
        File file = new File(createUrl);
        if (file.exists()) {
        	File fileROOT3 = new File(createFileROOT3);
        	fileROOT3.mkdirs();
        }
        else {
        	File fileROOT3 = new File(createFileROOT3);
        	fileROOT3.mkdirs();
        	}
        System.out.println("Create documents directory success.");
        return createFileROOT3;
    }
	/*****************createFolder******************/

	public static void isExist(String path) {
	File file = new File(path);
	//判断文件夹是否存在,如果不存在则创建文件夹
	if (!file.exists()) {
		file.mkdir();
		}
	}

	public boolean isSound() {
		return sound;
	}

	public void setSound(boolean sound) {
		this.sound = sound;
	}

	public boolean isShake() {
		return shake;
	}

	public void setShake(boolean shake) {
		this.shake = shake;
	}

	public ClipboardManager getClipboard() {
		return clipboard;
	}

	public void setClipboard(ClipboardManager clipboard) {
		this.clipboard = clipboard;
	}

	public Toast getToast() {
		return toast;
	}

	public void setToast(Toast toast) {
		this.toast = toast;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getLine_gps() {
		return line_gps;
	}

	public void setLine_gps(int line_gps) {
		this.line_gps = line_gps;
	}

	public ArrayList<Ark_lineEntity> getAll() {
		return all;
	}

	public void setAll(ArrayList<Ark_lineEntity> all) {
		this.all = all;
	}
	
	public int getNow_ark() {
		return now_ark;
	}

	public void setNow_ark(int now_ark) {
		this.now_ark = now_ark;
	}


	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setEndDate() {
		this.endDate = StringUtil.SIMPLE_DATEFORMAT.format(eDate.getTime());
	}

	public void setStartDate() {
		this.startDate = "2013-01-01 00:00:00";
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getUrlServer(Context context) throws Exception {
//		if(urlServer.equals("")){
				String urlServer = SpUtil.read(context,"urlServer");
			if(urlServer.equals("")){
				urlServer = StringUtil.URLSERVER;
				SpUtil.write(context, urlServer, "urlServer");
				Log.v(TAG + "getUrlServer","write UrlServer");;
			}
			this.urlServer = urlServer;
//		}
		return urlServer;
	}
	
	public String getDBurl(Context context) throws Exception {
//		if(DBurl.equals("")){
			String DBurl = SpUtil.read(context,"DBurl");
			if(DBurl.equals("")){
				DBurl = StringUtil.DBURL;
				SpUtil.write(context, DBurl, "DBurl");
				Log.v(TAG + "getDBurl","write DBurl");;
			}
			this.DBurl = DBurl;
//		}
		return DBurl;
	}
	
	public String getUrlServer() {
		return urlServer;
	}

	public void setUrlServer(String urlServer) {
		this.urlServer = urlServer;
	}

	public int getSERVERPORT() {
		return SERVERPORT;
	}

	public void setSERVERPORT(int sERVERPORT) {
		SERVERPORT = sERVERPORT;
	}

	public String getDBurl() {
		return DBurl;
	}

	public void setDBurl(String dBurl) {
		DBurl = dBurl;
	}

	public int getTIMEOUT() {
		return TIMEOUT;
	}

	public void setTIMEOUT(int tIMEOUT) {
		TIMEOUT = tIMEOUT;
	}

	public String getPW() {
		return PW;
	}

	public void setPW(String pW) {
		PW = pW;
	}

	public int getM_rate() {
		return m_rate;
	}

	public void setM_rate(int m_rate) {
		this.m_rate = m_rate;
	}

	public boolean isLogin_code() {
		return login_code;
	}

	public void setLogin_code(boolean login_code) {
		this.login_code = login_code;
	}

	public boolean isBreak_code() {
		return break_code;
	}

	public void setBreak_code(boolean break_code) {
		this.break_code = break_code;
	}

	public boolean isHeart_code() {
		return heart_code;
	}

	public void setHeart_code(boolean heart_code) {
		this.heart_code = heart_code;
	}

	public boolean isSend_code() {
		return send_code;
	}

	public void setSend_code(boolean send_code) {
		this.send_code = send_code;
	}

	public boolean isBus_code() {
		return bus_code;
	}

	public void setBus_code(boolean bus_code) {
		this.bus_code = bus_code;
	}

	public boolean isGps_code() {
		return gps_code;
	}

	public void setGps_code(boolean gps_code) {
		this.gps_code = gps_code;
	}

	public boolean isMsg_code() {
		return msg_code;
	}

	public void setMsg_code(boolean msg_code) {
		this.msg_code = msg_code;
	}

	public boolean isImg_code() {
		return img_code;
	}

	public void setImg_code(boolean img_code) {
		this.img_code = img_code;
	}

	public boolean isBus_push() {
		return bus_push;
	}

	public void setBus_push(boolean bus_push) {
		this.bus_push = bus_push;
	}

	public boolean isMsg_push() {
		return msg_push;
	}

	public void setMsg_push(boolean msg_push) {
		this.msg_push = msg_push;
	}

	public boolean isImg_push() {
		return img_push;
	}

	public void setImg_push(boolean img_push) {
		this.img_push = img_push;
	}

	public boolean isBus_push_UnGet() {
		return bus_push_UnGet;
	}

	public void setBus_push_UnGet(boolean bus_push_UnGet) {
		this.bus_push_UnGet = bus_push_UnGet;
	}

	public boolean isMsg_push_UnGet() {
		return msg_push_UnGet;
	}

	public void setMsg_push_UnGet(boolean msg_push_UnGet) {
		this.msg_push_UnGet = msg_push_UnGet;
	}

	public boolean isImg_push_UnGet() {
		return img_push_UnGet;
	}

	public void setImg_push_UnGet(boolean img_push_UnGet) {
		this.img_push_UnGet = img_push_UnGet;
	}

//	public List<bussinessEntity> getBusDataArrays() {
//		return BusDataArrays;
//	}
//
//	public void setBusDataArrays(List<bussinessEntity> busDataArrays) {
//		BusDataArrays = busDataArrays;
//	}
//	
//	public List<bussinessEntity> getBusDeleteArrays() {
//		return BusDeleteArrays;
//	}
//
//	public void setBusDeleteArrays(List<bussinessEntity> busDeleteArrays) {
//		BusDeleteArrays = busDeleteArrays;
//	}

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

	public int getBusUpdateLines() {
		return BusUpdateLines;
	}

	public void setBusUpdateLines(int busUpdateLines) {
		BusUpdateLines = busUpdateLines;
	}

	public int getGpsUpdateLines() {
		return GpsUpdateLines;
	}

	public void setGpsUpdateLines(int gpsUpdateLines) {
		GpsUpdateLines = gpsUpdateLines;
	}

	public int getMsgUpdateLines() {
		return MsgUpdateLines;
	}

	public void setMsgUpdateLines(int msgUpdateLines) {
		MsgUpdateLines = msgUpdateLines;
	}

	public int getImgUpdateLines() {
		return ImgUpdateLines;
	}

	public void setImgUpdateLines(int imgUpdateLines) {
		ImgUpdateLines = imgUpdateLines;
	}

	public int getCurrent_code() {
		return current_code;
	}

	public void setCurrent_code(int current_code) {
		this.current_code = current_code;
	}

	public int getPush() {
		return push;
	}

	public void setPush(int push) {
		this.push = push;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public boolean isBus_change() {
		return bus_change;
	}

	public void setBus_change(boolean bus_change) {
		this.bus_change = bus_change;
	}

	public boolean isMsg_change() {
		return msg_change;
	}

	public void setMsg_change(boolean msg_change) {
		this.msg_change = msg_change;
	}

	public boolean isImg_change() {
		return img_change;
	}

	public void setImg_change(boolean img_change) {
		this.img_change = img_change;
	}

	public boolean isCheck_push() {
		return check_push;
	}

	public void setCheck_push(boolean check_push) {
		this.check_push = check_push;
	}

	public boolean isAll_change() {
		return all_change;
	}

	public void setAll_change(boolean all_change) {
		this.all_change = all_change;
	}

	public List<TalkEntity> getTalkList() {
		return TalkList;
	}

	public void setTalkList(List<TalkEntity> talkList) {
		TalkList = talkList;
	}

	public List<BandWEntity> getBandWArrays() {
		return BandWArrays;
	}

	public void setBandWArrays(List<BandWEntity> bandWArrays) {
		BandWArrays = bandWArrays;
	}
	
}
