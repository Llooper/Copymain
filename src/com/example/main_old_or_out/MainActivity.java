package com.example.main_old_or_out;
//package com.example.main;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//
//import com.example.main_util.LogHelper;
//import com.example.main_util.SpUtil;
//
//import android.R.drawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.os.Parcelable;
//import android.os.Vibrator;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.app.LocalActivityManager;
//import android.app.ProgressDialog;
//import android.app.Service;
//import android.app.TabActivity;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.res.Configuration;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.graphics.Matrix;
//import android.graphics.drawable.BitmapDrawable;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v4.view.ViewPager.OnPageChangeListener;
//import android.text.TextUtils;
//import android.text.method.ScrollingMovementMethod;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.GestureDetector;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.GestureDetector.SimpleOnGestureListener;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup.LayoutParams;
//import android.view.Window;
//import android.view.WindowManager;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.view.animation.TranslateAnimation;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.Button;
//import android.widget.RelativeLayout;
//import android.widget.TabHost;
//import android.widget.TabWidget;
//import android.widget.TextView;
//import android.widget.Toast;
//
//@SuppressWarnings("deprecation")
//public class MainActivity extends Activity{
//	
//	private boolean destroy = false;
//	
//	private static boolean log = false;
//	private Context context = MainActivity.this;
//	private String TAG = "main";
//	//Badge View
////	private BadgeView badge0,badge1,badge2;
//	
//	//popup Window
//	private PopupWindow mpopupWindow;
//	
//	/************use for view pager***************/
//	private ViewPager mPager;//页卡内容
//	private List<View> listViews; //Tab页面列表
////	private ImageView cursor;//动画图片
//	private int offset = 0;//动画图片偏移量
//	private int currIndex = 0;//当前页卡编号
//	private int bmpW;//动画图片宽度
//	private LocalActivityManager manager = null;
//	/************use for view pager***************/
//	
//    private Vibrator vibrator=null;
//    private long exitTime = 0;
//	
//	//set calendar
//	private Calendar calendar = Calendar.getInstance();
//	
//	//set menu ID
//	private static final int ITEM1 = Menu.FIRST ;
//	private static final int ITEM2 = Menu.FIRST+1 ;
//	private static final int ITEM3 = Menu.FIRST+2 ;
//	private static final int ITEM4 = Menu.FIRST+3 ;
//	private static final int ITEM5 = Menu.FIRST+4 ;
////	private TabHost tabHost = null;
////	private TabWidget tabWidget = null;
//	private Button sTime = null; 
//	private Button eTime = null;
//	private Button selectAll = null;
//
////	private static final int SWIPE_MIN_DISTANCE = 120 ;
////	private static final int SWIPE_MAX_OFF_PATH = 250 ;
////	//pulling speed
////	private static final int SWIPE_THRESHOLD_VELOCITY = 200 ;
//	
//	//define data for button and textview
////	private static Button btn_main_send;
////	private static TextView tv_main_Title;
////	private static ImageView btn_main_send,btn_main_setting;
//	
//	private static Button btn_main_news
//	,btn_main_text,btn_main_picture
//	,btn_main_setting
//	;
//	
//	//
////	private GestureDetector gestureDetector ;
//	
//	View.OnTouchListener gestureListener ;
//	
////	int currentView = 0 ;
////	private static int maxTabIndex = 2 ;
//	private boolean check = false;
//
//	Message msg = new Message();
//
//	ProgressDialog pd = null;
//	public Handler mhandler = new Handler(){
////		GlobalID globalID = ((GlobalID)getApplication());
//		public void  handleMessage (Message msg){
//			switch (msg.what){
//			case 0:
//				shake();
////				btn_main_news.setBackgroundResource(R.drawable.main_bus_button_push);
//				if(log)Log.v(TAG,"mhandler case 0");
//				toast("有新的新闻信息");
////		        globalID.setBus_push(false);
//				break;
////			case 1:
////				shake();
//////				btn_main_text.setBackgroundResource(R.drawable.main_wea_button_push);
//////				if(log)Log.v(TAG,"mhandler case 1");
//////		        globalID.setWea_push(false);
////				toast("有新的天气信息");
////				break;
//			case 1:
//				shake();
////				btn_main_text.setBackgroundResource(R.drawable.main_msg_button_push);
////				if(log)Log.v(TAG,"mhandler case 2");
////		        globalID.setMsg_push(false);
//				toast("有新的文字信息");
//				break;
//			case 2:
//				shake();
////				btn_main_picture.setBackgroundResource(R.drawable.main_img_button_push);
////				if(log)Log.v(TAG,"mhandler case 3");
////		        globalID.setImg_push(false);
//				toast("有新的图片信息");
//				break;
//			case 4:
////				if(log)Log.v(TAG,"mhandler case 4");
//				if(pd == null)pd = ProgressDialog.show(context, "刷新", "正在努力刷新…");
//				break;
//			case 5:
//				shake();
////				if(log)Log.v(TAG,"mhandler case 5");
//				if(pd != null)pd.dismiss();
//				break;
//			default:
//				if(log)Log.v(TAG,"mhandler case default");
//				break;
//				}
//		}
//	};
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		final GlobalID globalID = ((GlobalID)getApplication());
//		if(null != savedInstanceState){
//			currIndex = savedInstanceState.getInt("currIndex"); 
////			Log.e(TAG, "onCreate get the savedInstanceState + currIndex = "+currIndex);
//	    	globalID.setBus_change(savedInstanceState.getBoolean("Bus_change"));
//	    	globalID.setBusDataArrays(savedInstanceState.getString("BusDataArrays"));
//	    	
//	    	globalID.setAll(savedInstanceState.getString("All"));
//	    	globalID.setNow_ark(savedInstanceState.getInt("Now_ark"));
//			globalID.start(context);
//			} 
//		//no title
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.activity_main);
//		if(log)Log.v(TAG,"onCreate");
//
//		if(manager == null)manager = new LocalActivityManager(this, true);
//		if(savedInstanceState == null)LogHelper.trace(TAG,"savedInstanceState.isEmpty()");
//		manager.dispatchCreate(savedInstanceState);
//		//set button
////		btn_main_send = (Button)findViewById(R.id.btn_main_send);
//		btn_main_setting = (Button)findViewById(R.id.btn_main_setting);
//		btn_main_news = (Button)findViewById(R.id.btn_main_news);
////		badge0 = new BadgeView(context, btn_main_news);
////    	badge0.setText("New!");
////    	badge0.setTextColor(Color.BLACK);
////    	badge0.setBackgroundColor(Color.BLUE);
////    	badge0.setTextSize(10);
//    	btn_main_news.clearAnimation();
//    	btn_main_text = (Button)findViewById(R.id.btn_main_text);
////		badge1 = new BadgeView(this, btn_main_text);
////    	badge1.setBackgroundResource(R.drawable.badge_ifaux);
////    	badge1.setHeight(2);
////    	badge1.setWidth(2);
//		btn_main_picture = (Button)findViewById(R.id.btn_main_picture);
////		badge2 = new BadgeView(this, btn_main_picture);
////    	badge2.setText("");
////    	badge2.setBackgroundColor(Color.RED);
////    	badge2.setTextSize(8);
//		
////		tv_main_Title = (TextView)findViewById(R.id.tv_main_Title);
////		tv_main_Title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
////		tv_main_Title.setSingleLine(true);
//////		tv_main_Title.setMarqueeRepeatLimit(6);
////		tv_main_Title.setMovementMethod(ScrollingMovementMethod.getInstance()); 
////		tv_main_Title.setHorizontalScrollBarEnabled(false);
//
////		btn_main_send.setBackgroundResource(R.drawable.main_send_up);
////		btn_main_setting.setBackgroundResource(R.drawable.main_more_up);
////		btn_main_news.setBackgroundResource(R.drawable.main_bus_button_down);
////		btn_main_text.setBackgroundResource(R.drawable.main_msg_button_up);
////		btn_main_picture.setBackgroundResource(R.drawable.main_img_button_up);
//
//		//for shake
//		vibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
//
////		btn_main_news.setOnClickListener(new OnClickListener() {
////			
////			@Override
////			public void onClick(View arg0) {
////				// TODO Auto-generated method stub
////				btn_main_news.setBackgroundResource(R.drawable.main_bus_button_down);
//////				btn_main_text.setBackgroundResource(R.drawable.main_msg_button_up);
//////				btn_main_picture.setBackgroundResource(R.drawable.main_img_button_up);
////				
////				switch(currentView){
////				case 1:
////					btn_main_text.setBackgroundResource(R.drawable.main_msg_button_up);
//////					getCurrentFocus().startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_right_in));
////				break;
////				case 2:
////					btn_main_picture.setBackgroundResource(R.drawable.main_img_button_up);
//////					getCurrentFocus().startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_left_in));
////				break;
////				}
////				
////				currentView = 0;
////				globalID.setCurrent_code(currentView);
////				tabHost.setCurrentTab(currentView);
////			}
////		});
////
////		btn_main_text.setOnClickListener(new OnClickListener() {
////			
////			@Override
////			public void onClick(View arg0) {
////				// TODO Auto-generated method stub
//////				btn_main_news.setBackgroundResource(R.drawable.main_bus_button_up);
////				btn_main_text.setBackgroundResource(R.drawable.main_msg_button_down);
//////				btn_main_picture.setBackgroundResource(R.drawable.main_img_button_up);
////
////				switch(currentView){
////				case 0:
////					btn_main_news.setBackgroundResource(R.drawable.main_bus_button_up);
//////					getCurrentFocus().startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_right_in));
////				break;
////				case 2:
////					btn_main_picture.setBackgroundResource(R.drawable.main_img_button_up);
//////					getCurrentFocus().startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_left_in));
////				break;
////				}
////				currentView = 1;
////				globalID.setCurrent_code(currentView);
////				tabHost.setCurrentTab(currentView);
////			}
////		});
////		
////		btn_main_picture.setOnClickListener(new OnClickListener() {
////			
////			@Override
////			public void onClick(View arg0) {
////				// TODO Auto-generated method stub
//////				btn_main_news.setBackgroundResource(R.drawable.main_bus_button_up);
//////				btn_main_text.setBackgroundResource(R.drawable.main_msg_button_up);
////				btn_main_picture.setBackgroundResource(R.drawable.main_img_button_down);
////				switch(currentView){
////				case 0:
////					btn_main_news.setBackgroundResource(R.drawable.main_bus_button_up);
//////					getCurrentFocus().startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_right_in));
////				break;
////				case 1:
////					btn_main_text.setBackgroundResource(R.drawable.main_msg_button_up);
//////					getCurrentFocus().startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_left_in));
////				break;
////				}
////
////				currentView = 2;
////				globalID.setCurrent_code(currentView);
////				tabHost.setCurrentTab(currentView);
////			}
////		});
//		
////		btn_main_send.setOnClickListener(new OnClickListener() {
////			
////			@Override
////			public void onClick(View v) {
////				// TODO Auto-generated method stub
//////				btn_main_send.setBackgroundResource(R.drawable.main_send_up);
////				globalID.un_stop = true;
////				Intent intent = new Intent(context,MsgTalkPre.class);
////                startActivity(intent);
////			}
////		});
//		
//		btn_main_setting.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				btn_main_setting.setBackgroundResource(R.drawable.main_more_up);
//				openOptionsMenu();
//			}
//		});
//		
//		
//		//set Text and Activity for tabHost
////		tabHost = getTabHost();
////		tabWidget = tabHost.getTabWidget();
//		
////		tabHost.addTab(tabHost.newTabSpec("0")
////				.setIndicator("新闻")
////				.setContent(new Intent(this , bussiness_message.class)));
////		
////		tabHost.addTab(tabHost.newTabSpec("1")
////				.setIndicator("文字")
////				.setContent(new Intent(this , MsgTalkPre.class)));
////		
////		tabHost.addTab(tabHost.newTabSpec("2")
////				.setIndicator("图片")
////				.setContent(new Intent(this , ImgTalkPre.class)));
//		
////		View mView = tabHost.getTabWidget().getChildAt(1);//0是代表第一个Tab
////
////		ImageView imageView = (ImageView)mView.findViewById(android.R.id.icon);//获取控件imageView 
////
////		imageView .setImageDrawable(getResources().getDrawable(R.drawable.main_news)); //改变我们需要的图标
////		for(int i = 0; i<tabWidget.getChildCount();i++){
////			TextView tv = (TextView)tabWidget.getChildAt(i).findViewById(android.R.id.title);
////			tv.setTextColor(this.getResources().getColorStateList(android.R.color.white));
////            ImageView iv=(ImageView)tabWidget.getChildAt(i).findViewById(android.R.id.icon);
////            
////            }
////		}
//		
////		tabHost.setCurrentTab(currentView);
////		tabWidget.setBackground(getResources().getDrawable(R.drawable.main_news));
////		
////		tabHost.setOnTabChangedListener(new OnTabChangeListener(){
////            @Override
////            public void onTabChanged(String tabId) {
////                // TODO Auto-generated method stub
////        		final String j = tabHost.getCurrentTabTag();
////            	switch(Integer.parseInt(j)){
////            	case 0:
////            		tabWidget.setBackground(getResources().getDrawable(R.drawable.main_news));
////            		break;
////                case 1:
////            		tabWidget.setBackground(getResources().getDrawable(R.drawable.main_text));
////            		break;
////                case 2:
////            		tabWidget.setBackground(getResources().getDrawable(R.drawable.main_picture));
////            		break;
////                default:
////            		tabWidget.setBackground(getResources().getDrawable(R.drawable.main_bottom));
////            		break;
////            	}
////            }            
////        });
////		
////		gestureDetector = new GestureDetector(new MyGestureDetector());
////		gestureListener = new View.OnTouchListener() {
////			
////			@Override
////			public boolean onTouch(View v, MotionEvent event) {
////				// TODO Auto-generated method stub
////				if(gestureDetector.onTouchEvent(event)){
////					return true ;
////				}
////				return false;
////			}
////		}; 
//		
//		InitImageView();
//		InitBottonListener();
//		InitViewPager();
//	}
//	
//	@Override
//	protected void onResume(){
//		super.onResume();
//		//set title
//		if(log)Log.v(TAG,"onResume");
//		
//		GlobalID globalID = ((GlobalID)getApplication());
//		globalID.cancel_notification();
////		if(globalID.getID().length() == 0) {
////			globalID.start(context);
////			try {
////				if(log)Log.v(TAG +" Create","urlServer = " + SpUtil.read(context, "urlServer"));
////				Log.v("main onResume","ID = " + SpUtil.read(context, "ID"));
////				Log.v("main onResume","PW = " + SpUtil.read(context, "PW"));
////				Log.v("main onResume","startDate = " + SpUtil.read(context, "startDate"));
////				Log.v("main onResume","endDate = " + SpUtil.read(context, "endDate"));
////				Log.v("main onResume","line = " + SpUtil.read(context, "line"));
////				Log.v("main onResume","DBurl = " + SpUtil.read(context, "DBurl"));
////				Log.v("main onResume","SERVERPORT = " + SpUtil.read(context, "SERVERPORT"));
////				Log.v("main onResume","TIMEOUT = " + SpUtil.read(context, "TIMEOUT"));
////			} catch (Exception e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////		}
//		tv_main_Title.setText(""+globalID.getID());
//		if(globalID.getCurrent_code() != -1)currIndex = globalID.getCurrent_code();
//		else{
//			globalID.setCurrent_code(currIndex);
//			if(log)Log.v(TAG,"onResume currendIndex = "+currIndex);
//		}
//		globalID.un_stop = false;
//		check = false;
//		switch(currIndex){
//		case 0:{
//			btn_main_news.setBackgroundResource(R.drawable.main_bus_button_down);
//			btn_main_text.setBackgroundResource(R.drawable.main_msg_button_up);
//			btn_main_picture.setBackgroundResource(R.drawable.main_img_button_up);
//		}
//		break;
//		case 1:{
//			btn_main_news.setBackgroundResource(R.drawable.main_bus_button_up);
//			btn_main_text.setBackgroundResource(R.drawable.main_msg_button_down);
//			btn_main_picture.setBackgroundResource(R.drawable.main_img_button_up);
//		}
//		break;
//		case 2:{
//			btn_main_news.setBackgroundResource(R.drawable.main_bus_button_up);
//			btn_main_text.setBackgroundResource(R.drawable.main_msg_button_up);
//			btn_main_picture.setBackgroundResource(R.drawable.main_img_button_down);
//		}
//		break;
//		}
////		tabHost.setCurrentTab(currIndex);
//		mPager.setCurrentItem(currIndex);
////		Activity currentActivity = getCurrentActivity();
////		if(globalID.getCurrent_code() == 0)((bussiness_message) currentActivity).onResume();
//		
////		if(globalID.isBus_push_UnGet()){
////			if(log)Log.v(TAG,"bus_push");
////			if (currentActivity instanceof bussiness_message) {
////				if(log)Log.v(TAG,"this is bus");
////				if(pd == null)pd = ProgressDialog.show(context, "刷新", "正在努力刷新…");
////				((bussiness_message) currentActivity).onOtherResume();
////				if(pd != null)pd.dismiss();
////				}
////			else {
////				if(log)Log.v(TAG,"bus change picture");
////				btn_main_news.setBackgroundResource(R.drawable.main_bus_button_push);
////			}
////		}
////		if(globalID.isMsg_push_UnGet()){
////			if(log)Log.v(TAG,"msg push");
////			if (currentActivity instanceof Text_information) {
////				if(log)Log.v(TAG,"this is msg");
////				if(pd == null)pd = ProgressDialog.show(context, "刷新", "正在努力刷新…");
////				((Text_information) currentActivity).onOtherResume();
////				if(pd != null)pd.dismiss();
////		        }
////			else {
////				if(log)Log.v(TAG,"msg change picture");
//////				btn_main_text.setBackgroundResource(R.drawable.main_msg_button_push);
////			}
////		}
////		if(globalID.isImg_push_UnGet()){
////			if(log)Log.v(TAG,"img push");
////			if (currentActivity instanceof image_information) {
////				if(log)Log.v(TAG,"this is img");
////				if(pd == null)pd = ProgressDialog.show(context, "刷新", "正在努力刷新…");
////				((image_information) currentActivity).onOtherResume();
////				if(pd != null)pd.dismiss();
////				}
////			else {
////				if(log)Log.v(TAG,"img change picture");
//////				btn_main_picture.setBackgroundResource(R.drawable.main_img_button_push);
////			}
////		}
//
//		globalID.mpAdapter.notifyDataSetChanged();
//		
//		//start change button Thread
//		if(globalID.isCheck_push()){
//			globalID.setCheck_push(false);
//			Thread check_push = new Thread(){
//				public void run(){
//					GlobalID globalID = ((GlobalID)getApplication());
//					int work = 0;
//					int time = 5;
//					int rate = 1000;
//					while(true){
//						if(log)Log.v(TAG,"check_push running...");
//						if(check){
//							if(log)Log.v(TAG,"check_push break");
//							globalID.setCheck_push(true);
//							return;
//						}
//						if(globalID.test_thread.isAlive()){
//							if(log)Log.v(TAG,"test_thread isAlive");
//							globalID.setCheck_push(true);
//							return;
//						}
//						if(time <= 0)time = 1;
//						if(time > 10*60*1000)time = 10*60*1000;
//						try {
//							work = 0;
//							Thread.sleep(globalID.getM_rate()*time);
//							
//							if(globalID.isBus_push_UnGet()){
//								Thread.sleep(globalID.getM_rate()*10);
//								time += rate;
//								work++;
//							}
//							
//							if(globalID.isMsg_push_UnGet()){
//								Thread.sleep(globalID.getM_rate()*10);
//								time += rate;
//								work++;
//							}
//							
//							if(globalID.isImg_push_UnGet()){
//								Thread.sleep(globalID.getM_rate()*10);
//								time += rate;
//								work++;
//							}
//							
//							
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//
////						Activity currentActivity = getCurrentActivity();
////					
////						if(globalID.isBus_push()){
////							time -= rate;
////							msg = new Message();
////							globalID.setBus_push(false);
////				       		Log.v("bus", "isBus_code = " + String.valueOf(globalID.isBus_code()));
////							if (currentActivity instanceof bussiness_message) {
////								msg.what = 4;
////						        mhandler.sendMessage(msg);
////								try {
////									globalID.setBus_push_UnGet(true);
////									
////									Thread.sleep(globalID.getM_rate()*14);
////				    				((bussiness_message) currentActivity).onOtherResume();
////									msg = new Message();
////									msg.what = 5;
////							        mhandler.sendMessage(msg);
////				    				if(work > 1){
////										Thread.sleep(globalID.getM_rate()*50);
////										continue;
////										}
////									} catch (InterruptedException e) {
////										// TODO Auto-generated catch block
////										e.printStackTrace();
////									}
////			    				}
////							else {
////								if(!globalID.isBus_push_UnGet()){
////							         msg.what = 0;
////							         mhandler.sendMessage(msg);
////							         globalID.setBus_push_UnGet(true);
////								}
////							}
////						 }
////						else{
////							time += rate;
////						}
////						if(globalID.isWea_push()){
////							if(msg!=null)msg = new Message();
////							globalID.setWea_push(false);
////			    			if (currentActivity instanceof weather) {
////								msg.what = 4;
////						        mhandler.sendMessage(msg);
////								try {
////									globalID.setWea_push_UnGet(true);
////									
////									Thread.sleep(globalID.getM_rate()*14);
////				    				((weather) currentActivity).onOtherResume();
////									msg = new Message();
////									msg.what = 5;
////							        mhandler.sendMessage(msg);
////				    				if(work > 2){
////										Thread.sleep(globalID.getM_rate()*50);
////										continue;
////										}
////									} catch (InterruptedException e) {
////										// TODO Auto-generated catch block
////										e.printStackTrace();
////									}
////						        }
////							else {
////								if(!globalID.isWea_push_UnGet()){
////							        msg.what = 1;
////							        mhandler.sendMessage(msg);
////									globalID.setWea_push_UnGet(true);
////								}
////							}
////						 }
//						if(globalID.isMsg_push()){
//							time -= rate;
//							msg = new Message();
//							globalID.setMsg_push(false);
////			    			if (currentActivity instanceof Text_information) {
////								msg.what = 4;
////						        mhandler.sendMessage(msg);
////								try {
////									globalID.setMsg_push_UnGet(true);
////									
////									Thread.sleep(globalID.getM_rate()*14);
////				    				((Text_information) currentActivity).onOtherResume();
////									msg = new Message();
////									msg.what = 5;
////							        mhandler.sendMessage(msg);
////				    				if(work > 1){
////										Thread.sleep(globalID.getM_rate()*50);
////										continue;
////										}
////									} catch (InterruptedException e) {
////										// TODO Auto-generated catch block
////										e.printStackTrace();
////									}
////						        }
////							else {
////								if(!globalID.isMsg_push_UnGet()){
////									msg.what = 2;
////									mhandler.sendMessage(msg);
////									globalID.setMsg_push_UnGet(true);
////								}
////							}
//							
////					         globalID.setMsg_push(false);
////				    			 Log.v("err", mDataArrays2.get(0).getBussiness_PostTime());
//						 }
//						else{
//							time += rate;
//						}
//						if(globalID.isImg_push()){
//							time -= rate;
//							msg = new Message();
//							globalID.setImg_push(false);
////				       		Log.v("bus", "isBus_code = " + String.valueOf(globalID.isBus_code()));
////							if (currentActivity instanceof image_information) {
////								msg.what = 4;
////						        mhandler.sendMessage(msg);
////								try {
////									globalID.setImg_push_UnGet(true);
////									Thread.sleep(globalID.getM_rate()*14);
////				    				((image_information) currentActivity).onOtherResume();
////									msg = new Message();
////									msg.what = 5;
////							        mhandler.sendMessage(msg);
////				    				if(work > 1){
////										Thread.sleep(globalID.getM_rate()*50);
////										continue;
////										} 
////									}catch (InterruptedException e) {
////										// TODO Auto-generated catch block
////										e.printStackTrace();
////									}
////			    				}
////							else {
////								if(!globalID.isImg_push_UnGet()){
////							         msg.what = 3;
////							         mhandler.sendMessage(msg);
////							         globalID.setImg_push_UnGet(true);
////								}
////							}
//							
////					         globalID.setImg_push(false);
////				    			 Log.v("err", mDataArrays2.get(0).getBussiness_PostTime());
//						 }
//						else{
//							time += rate;
//						}
//					}
//				}
//			};
//			check_push.start();
//		}		
//		return;
//	}
//	
////	/**********************MotionEvent*************************/
////	class MyGestureDetector extends SimpleOnGestureListener{
////		@Override
////		public boolean onFling(MotionEvent e1 , MotionEvent e2 , float velocityX , 
////				float velocityY){
////			TabHost tabHost = getTabHost();
////			boolean left = true;
////			final GlobalID globalID = ((GlobalID)getApplication());
////			try{
////				if(Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
////					return false ;
////				
////				//向左手势
////				if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
////						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY ){
////					if(currentView == maxTabIndex){
////						currentView = 0 ;
////					}else{
////						currentView++ ;
////					}
//////					return true;	
////				}else if(e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
////						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
////					left = false;
////					if(currentView == 0){
////						currentView = maxTabIndex ;
////					}else{
////						currentView-- ;
////					}
////					globalID.setCurrent_code(currentView);
////				}
////					switch(currentView){
////					case 0:{
////						btn_main_news.setBackgroundResource(R.drawable.main_bus_button_down);
////						if(left)
////							btn_main_picture.setBackgroundResource(R.drawable.main_img_button_up);
////						else 
////							btn_main_text.setBackgroundResource(R.drawable.main_msg_button_up);
////					}
////					break;
////					case 1:{
////						btn_main_text.setBackgroundResource(R.drawable.main_msg_button_down);
////						if(left)
////							btn_main_news.setBackgroundResource(R.drawable.main_bus_button_up);
////						else 
////							btn_main_picture.setBackgroundResource(R.drawable.main_img_button_up);
////					}
////					break;
////					case 2:{
////						btn_main_picture.setBackgroundResource(R.drawable.main_img_button_down);
////						if(left)
////							btn_main_text.setBackgroundResource(R.drawable.main_msg_button_up);
////						else 
////							btn_main_news.setBackgroundResource(R.drawable.main_bus_button_up);
////					}
////					break;				
////					}
////					tabHost.setCurrentTab(currentView);
////					return true;
////				
////			}catch(Exception e){
////				//noting
////				return false;
////				}
////		}
////	}
////	
////	public boolean dispatchTouchEvent(MotionEvent event){
////		if(gestureDetector.onTouchEvent(event)){
////			event.setAction(MotionEvent.ACTION_CANCEL);
////		}
////		return super.dispatchTouchEvent(event);
////	}
////	/**********************MotionEvent*************************/
//	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
////		getMenuInflater().inflate(R.menu.main, menu);
//		
////		showPopMenu();
////		return false;
//		
//		//add menu item
//		menu.add(0,ITEM1,0,"获取更多信息");
//		menu.add(0,ITEM2,0,"GPS信息");
//		menu.add(0,ITEM3,0,"按时间搜索");
//		menu.add(0,ITEM5,0,"设置");
//		menu.add(0,ITEM4,0,"注销登录");
//		if(log)Log.v(TAG,"createMenu");
//		return true;
//	}
//	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		//getMenuInflater().inflate(R.menu.login, menu);
//		
//		final GlobalID globalID = ((GlobalID)getApplication());
//		
//		switch(item.getItemId()){
//		//菜单项1被选择
//		case ITEM1 :
//			AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
//            builder1.setTitle("查询最近N条记录");
//            final String[] Lines = {"5条","10条","15条","20条","所有记录"};
//            int k = globalID.getLine()/5-1;
//            if(k==19)k = 4;
//			builder1.setSingleChoiceItems(Lines,k, new DialogInterface.OnClickListener() {
//                @Override
//				public void onClick(DialogInterface dialog, int which) {
//                	if(globalID.getLine() != (which+1)*5){
//                    	globalID.setLine((which+1)*5);
//                    	if(which == 4)globalID.setLine(100);
////                    	if(Integer.parseInt(j) == 0)
////                    		tabHost.setCurrentTab(1);
////                		else 
////                			tabHost.setCurrentTab(Integer.parseInt(j)-1);
////            			tabHost.setCurrentTab(Integer.parseInt(j));
//            			
//            			globalID.time_change();
////            			Activity currentActivity = getCurrentActivity();
////            			if (currentActivity instanceof bussiness_message) {
////            				((bussiness_message) currentActivity).onResume();
////            				}
////            			if (currentActivity instanceof Text_information) {
////     			           ((Text_information) currentActivity).onResume();
////     			           }
////            			if (currentActivity instanceof image_information) {
////     			           ((image_information) currentActivity).onResume();
////     			           }
//                	}
//        			dialog.dismiss();
//                }
//            });
//			builder1.setIcon(android.R.drawable.ic_dialog_info);
//            builder1.create().show();
//			break;
//		case ITEM2 :
//			globalID.un_stop = true;
//			Intent intent1 = new Intent(context,GeoPre.class);
//            startActivity(intent1);
//			break;
//		case ITEM3 :
//			
//			AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            LayoutInflater factory = LayoutInflater.from(context);
//            final View textEntryView = factory.inflate(R.layout.inquire, null);
////                builder.setIcon(R.drawable.ic_launcher);
//                builder.setTitle("选择查询范围");
//                builder.setView(textEntryView);
//                
//                selectAll = (Button) textEntryView.findViewById(R.id.btn_inquire_selectAll);
//                
//                sTime = (Button) textEntryView.findViewById(R.id.btn_inquire_startTime); 
//                eTime = (Button) textEntryView.findViewById(R.id.btn_inquire_endTime);
//                
//                sTime.setText(globalID.getStartDate());
//                eTime.setText(globalID.getEndDate());
//                
//                selectAll.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						sTime.setText("2013-01-01");
//						Calendar eDate = Calendar.getInstance();
//						eTime.setText(eDate.get(Calendar.YEAR)+"-"+(eDate.get(Calendar.MONTH)+1)+"-"+eDate.get(Calendar.DATE));
//					}
//				});
//                
//                sTime.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View arg0) {
//						// TODO Auto-generated method stub
//						showDialog(0);
//					}
//				});
//                
//                eTime.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View arg0) {
//						// TODO Auto-generated method stub
//						showDialog(1);
//					}
//				});
//                
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//					public void onClick(DialogInterface dialog, int whichButton) {
//                    		                    
////                    	if(（sTime.getText().toString())>(eTime.getText().toString())){
////                    		new AlertDialog.Builder(context).  
////            				setTitle("错误").setMessage("输入日期有误").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
////            					public void onClick(DialogInterface dialog, int which) {
////            						dialog.dismiss();
////            						}  
////            					}).show();
////                    	}
////                    	TV_sign.setText(et_asSign.getText().toString());
////                    	else{
//                    		globalID.setStartDate(sTime.getText().toString());
//                    		globalID.setEndDate(eTime.getText().toString());
//                    		dialog.dismiss();
//            			
//                    		//refresh listview by change currentTab
////                    		Log.v("j","j: "+j);
////                        	if(Integer.parseInt(j) == 0)
////                        		tabHost.setCurrentTab(1);
////                    		else 
////                    			tabHost.setCurrentTab(Integer.parseInt(j)-1);
////                			tabHost.setCurrentTab(Integer.parseInt(j));
//
//                			globalID.time_change();
////                			Activity currentActivity = getCurrentActivity();
////                			if (currentActivity instanceof bussiness_message) {
////                				((bussiness_message) currentActivity).onResume();
////                				}
////                			if (currentActivity instanceof Text_information) {
////         			           ((Text_information) currentActivity).onResume();
////         			           }
////                			if (currentActivity instanceof image_information) {
////         			           ((image_information) currentActivity).onResume();
////         			           }
//                			
//            				globalID.mpAdapter.notifyDataSetChanged();
////                    		}
//                    	}
//                    });
//
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//					public void onClick(DialogInterface dialog, int whichButton) {
//                    	dialog.dismiss();
//                    }
//                });
//                builder.create().show();
//			break;
//		//菜单项2被选择
//		case ITEM4 :
////			if(globalID.getNow_ark().equals("-1")){
////				new AlertDialog.Builder(context).  
////				setTitle("错误").setMessage("对不起，您权限被限制").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
////					public void onClick(DialogInterface dialog, int which) {
////						dialog.dismiss();
////						}  
////					}).show();
////			}
////			
////			else{
////				final ArrayList<String> Items = new ArrayList<String>();
////				Items.addAll(globalID.getAll());
////				
////				Log.v("ark","ark:"+Items.size());
////				AlertDialog.Builder builder11 = new AlertDialog.Builder(context);
////	            builder11.setTitle("船号选择框");
////	            final String[] ArkItems = new String[Items.size()];
////	            final String[] ArkNames = {"粤台118881","粤台12828 ","粤茂81888 "};
////	            ArkItems[0] = Items.get(0);
////	            for(int i  = 0;i<Items.size();i++){
////	            	ArkItems[i] = Items.get(i)+":  "+ ArkNames[Integer.parseInt(Items.get(i))-1];
////	            }
////				builder11.setSingleChoiceItems(ArkItems,Integer.parseInt(globalID.getNow_ark())-1, new DialogInterface.OnClickListener() {
////	                public void onClick(DialogInterface dialog, int which) {
////	                	globalID.setNow_ark(Items.get(which));
////	        			tv_main_Title.setText("用户:"+globalID.getID()+"船号:("+globalID.getNow_ark()+")"+ArkNames[Integer.parseInt(globalID.getNow_ark())-1]);
////	                	if(Integer.parseInt(j) == 0)
////	                		tabHost.setCurrentTab(1);
////	            		else 
////	            			tabHost.setCurrentTab(Integer.parseInt(j)-1);
////	        			tabHost.setCurrentTab(Integer.parseInt(j));
////	        			dialog.dismiss();
////	                }
////	            });
////				builder11.setIcon(android.R.drawable.ic_dialog_info);
////	            builder11.create().show();
////			}
//			check = true;
//            globalID.un_stop = true;
//			globalID.setCurrent_code(-2);
//			Intent intent = new Intent(context,loginActivity.class);
//            startActivity(intent);
//            globalID.clear();
//            globalID.time_change();
//            globalID.BusDataArrays.clear();
//            globalID.BusDeleteArrays.clear();
////            globalID.MsgDataArrays.clear();
////            globalID.MsgDeleteArrays.clear();
////            globalID.ImgDataArrays.clear();
////            globalID.ImgDeleteArrays.clear();
//            
//            destroy = true;
//            this.finish();
//			break;
//		case ITEM5 :
//            globalID.un_stop = true;
//			Intent intent4 = new Intent(context,SettingActivity.class);
//            startActivity(intent4);
//			break;
//		}
//		return true;
//	}	
//
////	/***************double touch for finish**************************/
////    private long exitTime = 0;
////
////	@SuppressLint("ShowToast")
////	@Override
////	public void finish(){
////		GlobalID globalID = ((GlobalID)getApplication());
////		if((System.currentTimeMillis()-exitTime) > 2000){
////			if(globalID.toast != null)globalID.toast.cancel();
////			globalID.toast = Toast.makeText(context, "再按一次退出程序", 2000);
////			globalID.toast.show();
////			exitTime = System.currentTimeMillis();
////			return;
////			}
////		else {
////			globalID.clear();
//////			finish();
////			System.exit(0);
////            }
////	}
////	/***************double touch for finish**************************/
//	
//	/******************creat dialog for time selecting*******************************/
//    @Override
//    protected Dialog onCreateDialog(int id) {
//    	Dialog dialog = null;
//    	switch (id) {
//    	case 0:
//    		dialog = new DatePickerDialog(
//            this,R.style.DialogStyle,
//            new DatePickerDialog.OnDateSetListener() {
//                @Override
//				public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
//                	if(month<9){
//                    	if(dayOfMonth<10){
//                        	sTime.setText(year + "-0" + (month+1) + "-0" + dayOfMonth);
//                    	}
//                    	else sTime.setText(year + "-0" + (month+1) + "-" + dayOfMonth);
//                	}
//                	else {
//                		if(dayOfMonth<10){
//                        	sTime.setText(year + "-" + (month+1) + "-0" + dayOfMonth);
//                    	}
//                		else sTime.setText(year + "-" + (month+1) + "-" + dayOfMonth);
//                	}
//                }
//            }, 
//            calendar.get(Calendar.YEAR), // 传入年份
//            calendar.get(Calendar.MONTH), // 传入月份
//            calendar.get(Calendar.DAY_OF_MONTH) // 传入天数
//        );
//    		dialog.setTitle("选择开始日期");
//        break;
//    case 1:
//        dialog = new DatePickerDialog(
//            this,R.style.DialogStyle,
//            new DatePickerDialog.OnDateSetListener() {
//                @Override
//				public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
//                	if(month<9){
//                    	if(dayOfMonth<10){
//                        	eTime.setText(year + "-0" + (month+1) + "-0" + dayOfMonth);
//                    	}
//                    	else eTime.setText(year + "-0" + (month+1) + "-" + dayOfMonth);
//                	}
//                	else eTime.setText(year + "-" + (month+1) + "-" + dayOfMonth);
//                }
//            },
//            calendar.get(Calendar.YEAR), // 传入年份
//            calendar.get(Calendar.MONTH), // 传入月份
//            calendar.get(Calendar.DAY_OF_MONTH) // 传入天数
//        );
//        dialog.setTitle("选择结束日期");
//        break;
//        }
//    	return dialog;
//    }
//    /******************creat dialog for time selecting*******************************/
//    
//    
//    /***************************onConfigurationChanged************************************/
////    @Override
////    public void onConfigurationChanged(Configuration newConfig) {
////        super.onConfigurationChanged(newConfig);
////        Log.i("--Main--", "onConfigurationChanged");
////		GlobalID globalID = ((GlobalID)getApplication());
////        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
////        	new AlertDialog.Builder(context).  
////			setTitle(TAG).setMessage("横屏").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
////				public void onClick(DialogInterface dialog, int which) {
////					dialog.dismiss();
////					}  
////				}).show();
////        }else{
////        	new AlertDialog.Builder(context).  
////			setTitle(TAG).setMessage("竖屏").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
////				public void onClick(DialogInterface dialog, int which) {
////					dialog.dismiss();
////					}  
////				}).show();
////        }
////    }
//    /***************************onConfigurationChanged************************************/
//    
//    void toast(String msg){
//		GlobalID globalID = ((GlobalID)getApplication());
//		if(globalID.toast != null)globalID.toast.cancel();
//		globalID.toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
//		globalID.toast.show();
//    }
//    
//    void shake(){
//		GlobalID globalID = ((GlobalID)getApplication());
//		if(globalID.isShake()){
//			if((System.currentTimeMillis()-exitTime) > 2000){
//				vibrator.vibrate(1000);
//				exitTime = System.currentTimeMillis();
//            }
//		}
//    }
//    @Override
//    public void onPause(){
//    	super.onPause();
//    	if(log)Log.v(TAG,"onPause");
//    	GlobalID globalID = ((GlobalID)getApplication());
//		check = true;
//    	if(globalID.un_stop)return;
//    	else{
//    		globalID.setCurrent_code(currIndex);
//    		globalID.create_notification("后台接收数据", "后台运行", "岸客户端", false, false, false,MainActivity.class.getName());
//			if(globalID.toast != null)globalID.toast.cancel();
//			globalID.clear();
////	    	context.finish();
//    	}
//    }
//    @Override
//    public void onStop(){
//    	super.onStop();
//    	if(log)Log.v(TAG,"stop");
////		GlobalID globalID = ((GlobalID)getApplication());
////		check = true;
////    	if(globalID.un_stop)return;
////    	else{
////    		globalID.setCurrent_code(currIndex);
////    		globalID.create_notification("后台接收数据", "后台运行", "岸客户端", false, false, false,MainActivity.class.getName());
////			if(globalID.toast != null)globalID.toast.cancel();
////			globalID.clear();
//////	    	context.finish();
////    	}
//    }    
//
//    @Override
//    public void onBackPressed() { 
//    	//实现Home键效果 
//    	//super.onBackPressed();这句话一定要注掉,不然又去调用默认的back处理方式了 
//    	Intent intent= new Intent(Intent.ACTION_MAIN); 
//    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
//    	intent.addCategory(Intent.CATEGORY_HOME); 
//    	startActivity(intent);  
//    	if(log)Log.v(TAG,"onBackPressed()");
//    	}
//    
//    @Override
//    public void onDestroy(){
//    	if(log)Log.v(TAG,"onDestroy");    	
//    	super.onDestroy();
//    }
//    @Override
//    public void onRestart(){
//    	super.onRestart();
//    	if(log)Log.v(TAG,"onRestart");
//    	onResume();
//    }
//    
//    @Override
//    public void finish(){
//    	Log
//    	.v(TAG,"finish()");
//    	//好有可能会出问题
//    	if(!destroy)return;
//    	super.finish();
//    }
//    
//    /*******************View Pager********************/
//    /**
//	 * 初始化头标	 */
//	private void InitBottonListener() {
//		btn_main_news.setOnClickListener(new MyOnClickListener(0));
//		btn_main_text.setOnClickListener(new MyOnClickListener(1));
//		btn_main_picture.setOnClickListener(new MyOnClickListener(2));
//	}
//    
//    /** 初始化ViewPager*/
//    private void InitViewPager() {
//		mPager = (ViewPager) findViewById(R.id.vPager);
//		listViews = new ArrayList<View>();
//		final GlobalID globalID = ((GlobalID)getApplication());
//		globalID.mpAdapter = new MyPagerAdapter(listViews);
//		Intent intent = new Intent(context, bussiness_message.class);
//		Intent intent1 = new Intent(context, MsgTalkPre.class);
//		Intent intent2 = new Intent(context, ImgTalkPre.class);
//		listViews.add(getView("bus", intent));
//		listViews.add(getView("tex", intent1));
//		listViews.add(getView("img", intent2));
//		mPager.setAdapter(globalID.mpAdapter);
//		mPager.setCurrentItem(0);
//		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
//	}
//    
//    /**
//	 * 初始化动画 */
//	private void InitImageView() {
////		cursor = (ImageView) findViewById(R.id.cursor);
//		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
//				.getWidth();//获取图片宽度
//		DisplayMetrics dm = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(dm);
//		int screenW = dm.widthPixels;// 获取分辨率宽度	
//		offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
//		Matrix matrix = new Matrix();
//		matrix.postTranslate(offset, 0);
////		cursor.setImageMatrix(matrix);// 设置动画初始位置
//	}
//	
//	/**
//	 * ViewPager适配器	 */
//	public class MyPagerAdapter extends PagerAdapter {
//		public List<View> mListViews;
//		private int mChildCount = 0;
//
//		public MyPagerAdapter(List<View> mListViews){
//			this.mListViews = mListViews;
//		}
//
//		@Override
//	     public void notifyDataSetChanged(){
//			mChildCount = getCount();
////			Log.v("MyPagerAdapter","notifyDataSetChanged "+mChildCount);
//			super.notifyDataSetChanged();
//	     }
//
//	     @Override
//	     public int getItemPosition(Object object){
//	           if ( mChildCount > 0) {
////	  	    	 Log.v("MyPagerAdapter","getItemPosition > 0");
//	           mChildCount --;
//	           return POSITION_NONE;
//	           }
////		    	 Log.v("MyPagerAdapter","getItemPosition");
//	           return super.getItemPosition(object);
//	     }
//
//	     
//		@Override
//		public void destroyItem(View arg0, int arg1, Object arg2) {
//			((ViewPager) arg0).removeView(mListViews.get(arg1));
//		}
//
//		@Override
//		public void finishUpdate(View arg0) {
//		}
//
//		@Override
//		public int getCount() {
//			return mListViews.size();
//		}
//
//		@Override
//		public Object instantiateItem(View arg0, int arg1) {
//			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
//			return mListViews.get(arg1);
//		}
//
//		@Override
//		public boolean isViewFromObject(View arg0, Object arg1) {
//			return arg0 == (arg1);
//		}
//
//		@Override
//		public void restoreState(Parcelable arg0, ClassLoader arg1) {
//		}
//
//		@Override
//		public Parcelable saveState() {
//			return null;
//		}
//
//		@Override
//		public void startUpdate(View arg0) {
//		}
//	}
//	
//	/**
//	 * 头标点击监听
//	 */
//	public class MyOnClickListener implements View.OnClickListener {
//		private int index = 0;
//
//		public MyOnClickListener(int i) {
//			index = i;
//		}
//
//		@Override
//		public void onClick(View v) {
//			mPager.setCurrentItem(index);
//		}
//	};
//	
//	/**
//	 * 页卡切换监听
//	 */
//	public class MyOnPageChangeListener implements OnPageChangeListener {
//
////		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
////		int two = one * 2;// 页卡1 -> 页卡3 偏移量
//		@Override
//		public void onPageSelected(int arg0) {
////			Animation animation = null;
//			switch (arg0) {
//			case 0:
//				btn_main_news.setBackgroundResource(R.drawable.main_bus_button_down);
////				badge0.toggle(true);
//				if (currIndex == 1) {
//					btn_main_text.setBackgroundResource(R.drawable.main_msg_button_up);
////					animation = new TranslateAnimation(one, 0, 0, 0);
//				} else if (currIndex == 2) {
////					animation = new TranslateAnimation(two, 0, 0, 0);
//					btn_main_picture.setBackgroundResource(R.drawable.main_img_button_up);
//				}
//				break;
//			case 1:
//				btn_main_text.setBackgroundResource(R.drawable.main_msg_button_down);
////				badge1.toggle(true);
//				if (currIndex == 0) {
//					btn_main_news.setBackgroundResource(R.drawable.main_bus_button_up);
////					animation = new TranslateAnimation(offset, one, 0, 0);
//				} else if (currIndex == 2) {
//					btn_main_picture.setBackgroundResource(R.drawable.main_img_button_up);
////					animation = new TranslateAnimation(two, one, 0, 0);
//				}
//				break;
//			case 2:
//				btn_main_picture.setBackgroundResource(R.drawable.main_img_button_down);
////				badge2.toggle(true);
//				if (currIndex == 0) {
//					btn_main_news.setBackgroundResource(R.drawable.main_bus_button_up);
////					animation = new TranslateAnimation(offset, two, 0, 0);
//				} else if (currIndex == 1) {
//					btn_main_text.setBackgroundResource(R.drawable.main_msg_button_up);
////					animation = new TranslateAnimation(one, two, 0, 0);
//				}
//				break;
//			}
//			currIndex = arg0;
//
//			final GlobalID globalID = ((GlobalID)getApplication());
//			globalID.setCurrent_code(currIndex);
//			if(log)Log.v(TAG,"currIndex = "+globalID.getCurrent_code());
////			tabHost.setCurrentTab(currIndex);
//			
////			animation.setFillAfter(true);// True:鍥剧墖鍋滃湪鍔ㄧ敾缁撴潫浣嶇疆
////			animation.setDuration(300);
////			cursor.startAnimation(animation);
////			Activity currentActivity = getCurrentActivity();
////			switch(currIndex){
////			case 0:
////				if(log)Log.v(TAG,"here");
////				((bussiness_message) currentActivity).onResume();
////				break;
////			case 1:
////				if(log)Log.v(TAG,"here1");
////				((Text_information) currentActivity).onResume();
////				break;
////			case 2:
////				if(log)Log.v(TAG,"here2");
////				((image_information) currentActivity).onResume();
////				break;
////			}
//
////			if (currentActivity instanceof bussiness_message) {
////				if(log)Log.v(TAG,"here");
////				((bussiness_message) currentActivity).onResume();
////				}
////			if (currentActivity instanceof Text_information) {
////				if(log)Log.v(TAG,"here1");
////		           ((Text_information) currentActivity).onResume();
////		           }
////			if (currentActivity instanceof image_information) {
////				if(log)Log.v(TAG,"here2");
////		           ((image_information) currentActivity).onResume();
////		           }
//		}
//
//		@Override
//		public void onPageScrolled(int arg0, float arg1, int arg2) {
//		}
//
//		@Override
//		public void onPageScrollStateChanged(int arg0) {
//		}
//	}
//	
//	private View getView(String id,Intent intent)
//	{
//		return manager.startActivity(id, intent).getDecorView();
//	}
//    /*******************View Pager********************/
//
//    /*******************Pop Menu********************/
//	private void showPopMenu() {
//		View view = View.inflate(getApplicationContext(), R.layout.share_popup_menu, null);
//		RelativeLayout rl_weixin = (RelativeLayout) view.findViewById(R.id.rl_weixin);
//		RelativeLayout rl_weibo = (RelativeLayout) view.findViewById(R.id.rl_weibo);
//		RelativeLayout rl_duanxin = (RelativeLayout) view.findViewById(R.id.rl_duanxin);
//		Button bt_cancle = (Button) view.findViewById(R.id.bt_cancle);
//
//		rl_weixin.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				mpopupWindow.dismiss();
//			}
//		});
//		rl_weibo.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				mpopupWindow.dismiss();
//			}
//		});
//		rl_duanxin.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				mpopupWindow.dismiss();
//			}
//		});
//		bt_cancle.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				mpopupWindow.dismiss();
//			}
//		});
//		
//		view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
//		LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
//		ll_popup.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_bottom_in));
//		
//		if(mpopupWindow==null){
//			mpopupWindow = new PopupWindow(this);
//			mpopupWindow.setWidth(LayoutParams.MATCH_PARENT);
//			mpopupWindow.setHeight(LayoutParams.MATCH_PARENT);
//			mpopupWindow.setBackgroundDrawable(new BitmapDrawable());
//
//			mpopupWindow.setFocusable(true);
//			mpopupWindow.setOutsideTouchable(true);
//		}
//		
//		mpopupWindow.setContentView(view);
//		mpopupWindow.showAtLocation(btn_main_news, Gravity.BOTTOM, 0, 0);
//		mpopupWindow.update();
//	}
//    /*******************Pop Menu********************/
//	@Override 
//    public void onSaveInstanceState(Bundle savedInstanceState) {  
//    	// Save away the original text, so we still have it if the activity   
//    	// needs to be killed while paused.
//		GlobalID globalID = ((GlobalID)getApplication());
//    	savedInstanceState.putInt("currIndex", currIndex);
//    	
//    	savedInstanceState.putBoolean("Bus_change", globalID.isBus_change());
//    	savedInstanceState.putString("BusDataArrays", globalID.Bus2String());
//    	
//    	savedInstanceState.putString("All", globalID.Ark_line2String());
//    	savedInstanceState.putInt("Now_ark", globalID.getNow_ark());
//    	
//    	super.onSaveInstanceState(savedInstanceState);  
//    	Log.e(TAG, "onSaveInstanceState");  
////    	if(log)Log.v(TAG, "Now_ark = "+ globalID.getNow_ark());
//    	}  
//    
//    @Override  
//    public void onRestoreInstanceState(Bundle savedInstanceState) {  
//    	super.onRestoreInstanceState(savedInstanceState);
////		GlobalID globalID = ((GlobalID)getApplication());
////    	currIndex = savedInstanceState.getInt("currIndex");
////    	globalID.setAll(savedInstanceState.getString("All"));
////    	globalID.setNow_ark(savedInstanceState.getInt("Now_ark"));
////    	Log.e(TAG, "onRestoreInstanceState + currIndex = "+ currIndex);
////    	if(log)Log.v(TAG, "Now_ark = "+ globalID.getNow_ark());
//    	}  
//}
