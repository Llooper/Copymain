package com.example.main;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;

import com.example.main_Entity.Ark_lineEntity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class GeoPre extends Activity{
	private String TAG = "GeoPre";
	private static Button 
//	btn_pre_geocode_ark_id,
//	btn_pre_geocode_start,btn_pre_geocode_end,btn_pre_geocode_line,
	btn_pre_geocode;
	private static TextView btn_pre_geocode_ark_id
	,btn_pre_geocode_start,btn_pre_geocode_end,btn_pre_geocode_line
//	,btn_pre_geocode
	;
	private static TextView talk_title;
	private static ImageButton title_bar_back,title_bar_gps;
	
	private Button selectAll = null;
	
	//set menu ID
	private static final int ITEM1 = Menu.FIRST ;
	private static final int ITEM2 = Menu.FIRST+1 ;
	private static final int ITEM3 = Menu.FIRST+2 ;
	private static final int ITEM4 = Menu.FIRST+3 ;
	private static final int ITEM5 = Menu.FIRST+4 ;
	private Calendar calendar = Calendar.getInstance();
//	final String[] ArkNames = {"粤台118881","粤台12828 ","粤茂81888 "};
	
	private static final int SWIPE_MIN_DISTANCE = 120 ;
	private static final int SWIPE_MAX_OFF_PATH = 250 ;
	//pulling speed
	private static final int SWIPE_THRESHOLD_VELOCITY = 200 ;
	//
	private GestureDetector gestureDetector ;
	View.OnTouchListener gestureListener ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final GlobalID globalID = ((GlobalID)getApplication());
		
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
        //no_title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pre_geo);
        
        btn_pre_geocode_ark_id = (TextView)findViewById(R.id.btn_pre_geocode_ark_id);
        btn_pre_geocode_start = (TextView)findViewById(R.id.btn_pre_geocode_start);
        btn_pre_geocode_end = (TextView)findViewById(R.id.btn_pre_geocode_end);
        btn_pre_geocode_line = (TextView)findViewById(R.id.btn_pre_geocode_line);
        btn_pre_geocode = (Button)findViewById(R.id.btn_pre_geocode);
        talk_title = (TextView)findViewById(R.id.talk_title);
        talk_title.setText(R.string.geo_pre_title);
        title_bar_back = (ImageButton)findViewById(R.id.title_bar_back);
        title_bar_gps = (ImageButton)findViewById(R.id.title_bar_gps);
        
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

         btn_pre_geocode_ark_id.setOnClickListener(clickListener);
         btn_pre_geocode_start.setOnClickListener(clickListener);
         btn_pre_geocode_end.setOnClickListener(clickListener);
         btn_pre_geocode_line.setOnClickListener(clickListener);
         btn_pre_geocode.setOnClickListener(clickListener);
         title_bar_back.setOnClickListener(clickListener);
         title_bar_gps.setOnClickListener(clickListener);
         
         btn_pre_geocode_start.setText(globalID.getStartDate());
         btn_pre_geocode_end.setText(globalID.getEndDate());
         btn_pre_geocode_line.setText("查询 "+globalID.getLine() + " 条记录");
         
//         btn_pre_geocode.setOnTouchListener(new OnTouchListener() {
// 			
// 			@Override
// 			public boolean onTouch(View arg0, MotionEvent arg1) {
// 				// TODO Auto-generated method stub
//// 				btn_pre_geocode.setBackgroundColor(android.graphics.Color.parseColor("#FFFFFF"));
// 				btn_pre_geocode.setBackgroundResource(R.drawable.gps_button_down);
// 				return false;
// 			}
// 		});
//         
//         btn_pre_geocode_ark_id.setOnTouchListener(new OnTouchListener() {
// 			
// 			@Override
// 			public boolean onTouch(View arg0, MotionEvent arg1) {
// 				// TODO Auto-generated method stub
// 				btn_pre_geocode_ark_id.setBackgroundResource(R.drawable.gps_ark_down);
// 				return false;
// 			}
// 		});
//         
//         btn_pre_geocode_start.setOnTouchListener(new OnTouchListener() {
//  			
//  			@Override
//  			public boolean onTouch(View arg0, MotionEvent arg1) {
//  				// TODO Auto-generated method stub
//  				btn_pre_geocode_start.setBackgroundResource(R.drawable.gps_start_down);
//  				return false;
//  			}
//  		});
//         
//         btn_pre_geocode_end.setOnTouchListener(new OnTouchListener() {
//  			
//  			@Override
//  			public boolean onTouch(View arg0, MotionEvent arg1) {
//  				// TODO Auto-generated method stub
//  				btn_pre_geocode_end.setBackgroundResource(R.drawable.gps_end_down);
//  				return false;
//  			}
//  		});
//         
//         btn_pre_geocode_line.setOnTouchListener(new OnTouchListener() {
//  			
//  			@Override
//  			public boolean onTouch(View arg0, MotionEvent arg1) {
//  				// TODO Auto-generated method stub
//  				btn_pre_geocode_line.setBackgroundResource(R.drawable.gps_line_down);
//  				return false;
//  			}
//  		});
         
//         Button btnSend = (Button) findViewById(R.id.btnSend);
         
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
	
	
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	void SearchButtonProcess(View v) throws UnknownHostException, IOException {
		if (btn_pre_geocode_ark_id.equals(v)) {
//			btn_pre_geocode_ark_id.setBackgroundResource(R.drawable.gps_ark_up);
			btn_pre_geocode_ark_id.setClickable(false);
			selectArk_id();
		}
		else if (btn_pre_geocode_start.equals(v)) {
//			btn_pre_geocode_start.setBackgroundResource(R.drawable.gps_start_up);
			showDialog(0);
		}
		else if (btn_pre_geocode_end.equals(v)) {
//			btn_pre_geocode_end.setBackgroundResource(R.drawable.gps_end_up);
			showDialog(1);
		}
		else if (btn_pre_geocode_line.equals(v)) {
//			btn_pre_geocode_line.setBackgroundResource(R.drawable.gps_line_up);
			btn_pre_geocode_line.setClickable(false);
			selectLine();
		}
		else if (btn_pre_geocode.equals(v)||title_bar_gps.equals(v)) {
//			btn_pre_geocode.setBackgroundColor(android.graphics.Color.parseColor("#FE9B21"));
//			btn_pre_geocode.setBackgroundResource(R.drawable.gps_button_up);
			GlobalID globalID = ((GlobalID)getApplication());
			globalID.un_stop = true;
			if(globalID.getNow_ark() != -1 && globalID.getAll().size() > 0){
            	globalID.setStartDate(btn_pre_geocode_start.getText().toString());
    			globalID.setEndDate(btn_pre_geocode_end.getText().toString());
				Intent intent = new Intent(GeoPre.this,GeoCoderDemo.class);
	            startActivity(intent);
			}
			else {
    			String strInfo = String.format("没有船号信息");
				if(globalID.toast != null)globalID.toast.cancel();
				globalID.toast = Toast.makeText(GeoPre.this, strInfo, Toast.LENGTH_LONG);
				globalID.toast.show();
			}
		}
		else if(title_bar_back.equals(v)){
			this.finish();
		}
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		GlobalID globalID = ((GlobalID)getApplication());
		globalID.cancel_notification();
		globalID.un_stop = false;
        Log.v("pre ","now ark:"+String.valueOf(globalID.getNow_ark()));
		if(globalID.getNow_ark() != -1 && globalID.getAll().size() > 0){
			btn_pre_geocode_ark_id.setText(globalID.getAll().get(globalID.getNow_ark()).getArk_no());
        }
        else btn_pre_geocode_ark_id.setText("没有获取船号权限");
        btn_pre_geocode_start.setText(globalID.getStartDate());
        btn_pre_geocode_end.setText(globalID.getEndDate());
        if(globalID.getLine() == 100){
    		btn_pre_geocode_line.setText("查询所有记录");
    	}
    	else btn_pre_geocode_line.setText("查询 "+globalID.getLine() + " 条记录");
		return;
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		
		//add menu item
		menu.add(0,ITEM1,0,"获取更多信息");
		menu.add(0,ITEM2,0,"北斗信息");
		menu.add(0,ITEM3,0,"按时间搜索");
		menu.add(0,ITEM4,0,"按船号搜索");
		menu.add(0,ITEM5,0,"设置");
		
		return true;
	}
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.login, menu);
		switch(item.getItemId()){
		//菜单项1被选择
		case ITEM1 :
			selectLine();
			break;
		case ITEM2 :
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
			Intent intent4 = new Intent(GeoPre.this,SettingActivity.class);
            startActivity(intent4);
			break;
		}
		return true;
	}
    
    private void selectDate(){
    	final GlobalID globalID = ((GlobalID)getApplication());
    	AlertDialog.Builder builder = new AlertDialog.Builder(GeoPre.this);
        LayoutInflater factory = LayoutInflater.from(GeoPre.this);
        final View textEntryView = factory.inflate(R.layout.inquire, null);
//            builder.setIcon(R.drawable.ic_launcher);
            builder.setTitle("选择查询范围");
            builder.setView(textEntryView);
            
            selectAll = (Button) textEntryView.findViewById(R.id.btn_inquire_selectAll);
            
            btn_pre_geocode_start = (Button) textEntryView.findViewById(R.id.btn_inquire_startTime); 
            btn_pre_geocode_end = (Button) textEntryView.findViewById(R.id.btn_inquire_endTime);
            
            btn_pre_geocode_start.setText(globalID.getStartDate());
            btn_pre_geocode_end.setText(globalID.getEndDate());
            
            selectAll.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					btn_pre_geocode_start.setText("2013-01-01");
					Calendar eDate = Calendar.getInstance();
					btn_pre_geocode_end.setText(eDate.get(Calendar.YEAR)+"-"+(eDate.get(Calendar.MONTH)+1)+"-"+eDate.get(Calendar.DATE));
				}
			});
            
            btn_pre_geocode_start.setOnClickListener(new OnClickListener() {
				
				@SuppressWarnings("deprecation")
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					showDialog(0);
				}
			});
            
            btn_pre_geocode_end.setOnClickListener(new OnClickListener() {
				
				@SuppressWarnings("deprecation")
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					showDialog(1);
				}
			});
            
            
            
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
				public void onClick(DialogInterface dialog, int whichButton) {
//                	TV_sign.setText(et_asSign.getText().toString());
//                TV_sign.setText(et_asSign.getText().toString());
                	globalID.setStartDate(btn_pre_geocode_start.getText().toString());
        			globalID.setEndDate(btn_pre_geocode_end.getText().toString());
        			dialog.dismiss();
        	        btn_pre_geocode_start = (TextView)findViewById(R.id.btn_pre_geocode_start);
        	        btn_pre_geocode_end = (TextView)findViewById(R.id.btn_pre_geocode_end);
        	        btn_pre_geocode_start.setText(globalID.getStartDate());
        	        btn_pre_geocode_end.setText(globalID.getEndDate());
                }
            });

            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
				public void onClick(DialogInterface dialog, int whichButton) {
                	dialog.dismiss();
        	        btn_pre_geocode_start = (TextView)findViewById(R.id.btn_pre_geocode_start);
        	        btn_pre_geocode_end = (TextView)findViewById(R.id.btn_pre_geocode_end);
        	        btn_pre_geocode_start.setText(globalID.getStartDate());
        	        btn_pre_geocode_end.setText(globalID.getEndDate());
                }
            });
            builder.create().show();
    }
    
    private void selectArk_id(){
    	final GlobalID globalID = ((GlobalID)getApplication());
    	if(globalID.getNow_ark() == -1){
			new AlertDialog.Builder(GeoPre.this).  
			setTitle("错误").setMessage("对不起，您权限被限制").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					}  
				}).show();
		}
		
		else{
			final ArrayList<Ark_lineEntity> Items = new ArrayList<Ark_lineEntity>();
			Items.addAll(globalID.getAll());
			Log.v("ark","ark:"+Items.size());
			AlertDialog.Builder builder1 = new AlertDialog.Builder(GeoPre.this);
            builder1.setTitle("船号选择框");
            final String[] ArkItems = new String[Items.size()];
//            ArkItems[0] = Items.get(0).getArk_no();
            for(int i  = 0;i<Items.size();i++){
            	ArkItems[i] = Items.get(i).getArk_id() + ":  "+ Items.get(i).getArk_no();
            }
            builder1.setSingleChoiceItems(ArkItems,globalID.getNow_ark(), new DialogInterface.OnClickListener() {
                @Override
				public void onClick(DialogInterface dialog, int which) {
                	globalID.setNow_ark(which);
                	btn_pre_geocode_ark_id.setText(globalID.getAll().get(globalID.getNow_ark()).getArk_no());                	
        			dialog.dismiss();
                }
            });
            builder1.create().show();
		}
		btn_pre_geocode_ark_id.setClickable(true);
    }
    
    private void selectLine(){
    	final GlobalID globalID = ((GlobalID)getApplication());
    	AlertDialog.Builder builder1 = new AlertDialog.Builder(GeoPre.this);
        builder1.setTitle("查询最近N条记录");
        final String[] Lines = {"5条","10条","15条","20条","所有记录"};
        int k = globalID.getLine()/5-1;
        if(k==19)k = 4;
		builder1.setSingleChoiceItems(Lines,k, new DialogInterface.OnClickListener() {
            @Override
			public void onClick(DialogInterface dialog, int which) {
            	globalID.setLine((which+1)*5);
            	if(which == 4){
            		globalID.setLine(100);
            		btn_pre_geocode_line.setText("查询所有记录");
            	}
            	else btn_pre_geocode_line.setText("查询 "+globalID.getLine() + " 条记录");
    			dialog.dismiss();
            }
        });
		builder1.setIcon(android.R.drawable.ic_dialog_info);
        builder1.create().show();
		btn_pre_geocode_line.setClickable(true);
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
                    		btn_pre_geocode_start.setText(year + "-0" + (month+1) + "-0" + dayOfMonth);
                    	}
                    	else btn_pre_geocode_start.setText(year + "-0" + (month+1) + "-" + dayOfMonth);
                	}
                	else btn_pre_geocode_start.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                }
            }, 
            calendar.get(Calendar.YEAR), // 传入年份
            calendar.get(Calendar.MONTH), // 传入月份
            calendar.get(Calendar.DAY_OF_MONTH) // 传入天数
        );
    		dialog.setTitle("选择开始日期");
        break;
    case 1:
        dialog = new DatePickerDialog(
            this,
            new DatePickerDialog.OnDateSetListener() {
                @Override
				public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
                	if(month<9){
                    	if(dayOfMonth<10){
                    		btn_pre_geocode_end.setText(year + "-0" + (month+1) + "-0" + dayOfMonth);
                    	}
                    	else btn_pre_geocode_end.setText(year + "-0" + (month+1) + "-" + dayOfMonth);
                	}
                	else btn_pre_geocode_end.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                }
            }, 
            calendar.get(Calendar.YEAR), // 传入年份
            calendar.get(Calendar.MONTH), // 传入月份
            calendar.get(Calendar.DAY_OF_MONTH) // 传入天数
        );
        dialog.setTitle("选择结束日期");
        break;
    }
    return dialog;
}
    /******************creat dialog for time selecting*******************************/
    
    @Override
    public void onStop(){
    	super.onStop();
    }
    
    @Override
    public void finish(){
    	super.finish();
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
		GlobalID globalID = ((GlobalID)getApplication());
		globalID.un_stop = true;
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
				GeoPre.this.finish();
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
	
	 @Override
	 public void onPause(){
		super.onPause();
	    Log.v(TAG,"onPause");
	    GlobalID globalID = ((GlobalID)getApplication());
		if(globalID.un_stop)return;
		else{
//		globalID.setCurrent_code(4);
		globalID.create_notification("后台接收数据", "后台运行", "岸客户端", false, false, false,GeoPre.class.getName());
		if(globalID.toast != null)globalID.toast.cancel();
//		globalID.clear();
		}
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
    	Log.v(TAG, "Now_ark = "+ globalID.getNow_ark());
    	}  
    
    @Override  
    public void onRestoreInstanceState(Bundle savedInstanceState) {  
    	super.onRestoreInstanceState(savedInstanceState);
		GlobalID globalID = ((GlobalID)getApplication());
    	globalID.setAll(savedInstanceState.getString("All"));
    	globalID.setNow_ark(savedInstanceState.getInt("Now_ark"));
    	Log.v(TAG, "Now_ark = "+ globalID.getNow_ark());
    	}  
}
