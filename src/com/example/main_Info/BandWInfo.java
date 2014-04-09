package com.example.main_Info;

import java.io.IOException;
import java.net.UnknownHostException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.main.BandWViewPagerActivity;
import com.example.main.GlobalID;
import com.example.main.R;
import com.example.main_Entity.BandWEntity;
import com.example.main_util.FuntionUtil;
import com.example.main_util.LogHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


//just for show BandW list in detail
public class BandWInfo extends Activity{
	
	private static String TAG = "BandWInfo";
	private static boolean log = false;
	private final Context context = BandWInfo.this;
	private static TextView tv_BandWInfo_Title;
	private static TextView tv_BandWInfo_PostTime;
	private static TextView tv_BandWInfo_Detail;
	private static LinearLayout ll_BandW_info;
	private static ImageView iv_BandWInfo_view;
	private static TextView talk_title;
	private static ImageButton title_bar_back,title_bar_gps;
	
	private boolean type = false;
	private String Title = "";
	private String PostTime = "";
	private String Detail = "";
	private String info = "";
	private Bitmap view;
	
	private int index = 0;
	private String Colour = "";
	
	@Override 
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);

		overridePendingTransition(R.anim.item_in, R.anim.list_out);
		
		//set no title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bandw_info);
		tv_BandWInfo_Title = (TextView)findViewById(R.id.tv_BandWInfo_Title);
		tv_BandWInfo_PostTime = (TextView)findViewById(R.id.tv_BandWInfo_PostTime);
		tv_BandWInfo_Detail = (TextView)findViewById(R.id.tv_BandWInfo_Detail);
		ll_BandW_info = (LinearLayout)findViewById(R.id.ll_BandW_info);
		iv_BandWInfo_view = (ImageView)findViewById(R.id.iv_BandWInfo_view);
		
		setTitle();
		final GlobalID globalID = ((GlobalID)getApplication());
		if(null != savedInstanceState){
			globalID.start(context);
			index = savedInstanceState.getInt("index");
			Colour = savedInstanceState.getString("Colour");
			type = savedInstanceState.getBoolean("type");
			Title = savedInstanceState.getString("Title");
			PostTime = savedInstanceState.getString("PostTime");
			Detail = savedInstanceState.getString("Detail");
			
			if(!type){
				String picName = savedInstanceState.getString("Pic");
				if(log)Log.v("picName: " , picName);
				view = FuntionUtil.downloadPic("http://"+ globalID.getDBurl() +"/admin/images/" + picName);
				if(view ==null){
					view =  BitmapFactory.decodeResource(getResources(),R.drawable.weather_preview);
				}
			}
			
			final String decode = savedInstanceState.getString("BandWList");
			Thread decode_thread = new Thread(){
				public void run(){
					getmArrays(decode);
				}
			};
			decode_thread.start();
		}
		else{
			Intent intent = getIntent();
			Bundle data = intent.getExtras();
			index = data.getInt("index");
			Colour = (String)data.getSerializable("colour");
			
			final BandWEntity entity = globalID.BandWArrays.get(index);
			type = entity.getList_type();
			Title = entity.getTitle();
			PostTime = entity.getTime();
			Detail = entity.getDetail();
			view = entity.getPic();
		}

		ll_BandW_info.setBackgroundColor(android.graphics.Color.parseColor(Colour));
		tv_BandWInfo_Title.setText("消息类型: " + Title);
		tv_BandWInfo_PostTime.setText("发布时间: "+PostTime);
		tv_BandWInfo_Detail.setText("详细信息: \n"+Detail);
		if(type){
			iv_BandWInfo_view.setVisibility(View.GONE);
		}
		else iv_BandWInfo_view.setImageBitmap(view);
		
		OnLongClickListener LongClickListener = new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				try {
					SearchLongProcess(v);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
		
		
		private void SearchLongProcess(View v) throws UnknownHostException, IOException {
			// TODO Auto-generated method stub
			if (tv_BandWInfo_Title.equals(v)) {
				copy(Title);
			}
			 if (tv_BandWInfo_PostTime.equals(v)) {
				copy(PostTime);
			}
			 if (tv_BandWInfo_Detail.equals(v)) {
				copy(Detail);
			}
			 }
		};
     
		
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

			private void SearchButtonProcess(View v) throws UnknownHostException, IOException {
				// TODO Auto-generated method stub
					if (tv_BandWInfo_Title.equals(v)) {
						new_dialog("消息类型",Title);
						}
					 if (tv_BandWInfo_PostTime.equals(v)) {
						 new_dialog("发布时间",PostTime);
					}
					 if (tv_BandWInfo_Detail.equals(v)) {
						 new_dialog("详细信息",Detail);
					}
					 if(iv_BandWInfo_view.equals(v)){
							Bundle bundle = new Bundle();
							bundle.putInt("i", index);
							GlobalID globalID = ((GlobalID)getApplication());
							globalID.un_stop = true;
							Intent intent = new Intent(BandWInfo.this,BandWViewPagerActivity.class);
							//post data
							intent.putExtras(bundle);
				            startActivity(intent);
					 }
			}
         };
         
         tv_BandWInfo_Title.setOnClickListener(clickListener);
         tv_BandWInfo_PostTime.setOnClickListener(clickListener);
         tv_BandWInfo_Detail.setOnClickListener(clickListener);
         iv_BandWInfo_view.setOnClickListener(clickListener);
         
         tv_BandWInfo_Title.setOnLongClickListener(LongClickListener);
         tv_BandWInfo_PostTime.setOnLongClickListener(LongClickListener);
         tv_BandWInfo_Detail.setOnLongClickListener(LongClickListener);
	}
	
	private void setTitle() {
		// TODO Auto-generated method stub
				// TODO Auto-generated method stub
			talk_title = (TextView)findViewById(R.id.talk_title);
	    	talk_title.setText(R.string.info_title);
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

	private void new_dialog (String title, String content){
		new AlertDialog.Builder(BandWInfo.this).  
		setTitle(title).setMessage(content).setPositiveButton("确定", new DialogInterface.OnClickListener() {  
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				}  
			}).show();
	}
	
	private void copy (final String copy){
		new AlertDialog.Builder(BandWInfo.this).setPositiveButton("复制", new DialogInterface.OnClickListener() {  
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(DialogInterface dialog, int which) {
				GlobalID globalID = ((GlobalID)getApplication());
				
				// 判断API>=11
				if(Build.VERSION.SDK_INT >= 11){
			        ClipData textCd = ClipData.newPlainText("", copy);
			        globalID.getClipboard().setPrimaryClip(textCd);
				}
				else {
					ClipboardManager clipboard ;
			        clipboard = (ClipboardManager)getSystemService(BandWInfo.CLIPBOARD_SERVICE);
			        clipboard.setText(copy);
				}
				
				dialog.dismiss();
				globalID.showCopyed(BandWInfo.this);
				}  
			}).show();
	}
	
	 @Override
	 public void onResume(){
		 super.onResume();
		 GlobalID globalID = ((GlobalID)getApplication());
		 globalID.cancel_notification();
		 globalID.un_stop = false;
	}

	@Override 
    public void onSaveInstanceState(Bundle savedInstanceState) {  
    	// Save away the original text, so we still have it if the activity   
    	// needs to be killed while paused.
    	super.onSaveInstanceState(savedInstanceState);  
    	if(log)Log.e(TAG, "onSaveInstanceState");

    	GlobalID globalID = ((GlobalID)getApplication());
    	savedInstanceState.putInt("index",index);
		savedInstanceState.putString("Colour",Colour);
		savedInstanceState.putBoolean("type",type);
		savedInstanceState.putString("Title",Title);
		savedInstanceState.putString("PostTime",PostTime);
		savedInstanceState.putString("Detail",Detail);
		savedInstanceState.putString("BandWList",List2String());
		if(!type){
			savedInstanceState.putString("Pic",globalID.BandWArrays.get(index).getPicName());			
		}
    	
    	if(globalID.un_stop)return;
    	globalID.create_notification("后台接收数据", "后台运行", "岸客户端", false, false, false,BandWInfo.class.getName());
	    if(globalID.toast != null)globalID.toast.cancel();
    	}
	
	private String List2String() {
		// TODO Auto-generated method stub
		GlobalID globalID = (GlobalID)getApplication();
    	BandWEntity entity = new BandWEntity();
    	String encode = "[";
		for(int i = 0 ; i < globalID.BandWArrays.size();){
			entity = globalID.BandWArrays.get(i);
			encode += "{";

			encode += "\"list_type\":\"" + entity.getList_type() + "\";";
			encode += "\"Id\":\"" + entity.getId() + "\";";
			encode += "\"Title\":\"" + entity.getTitle() + "\";";
			encode += "\"Typeid\":\"" + entity.getTypeid() + "\";";
			encode += "\"Detail\":\"" + entity.getDetail() + "\";";
			encode += "\"Pic\":\"" + entity.getPicName() + "\";";
			encode += "\"Time\":\"" + entity.getTime() + "\"";
			
			encode += "}";
			if(++i < globalID.BandWArrays.size()){
				encode += ",";
			}
		}
		encode += "]";
		if(log)LogHelper.trace(TAG, encode);
		return encode;
	}

    private void getmArrays(String decode) {
		// TODO Auto-generated method stub
    	GlobalID globalID = (GlobalID)getApplication();
		try{
			JSONArray jsonArray = new JSONArray(decode);
			if(jsonArray.length()==0){
				return;
				}
			else{
				for(int i = 0 ; i < jsonArray.length() ; i++){
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					
					BandWEntity entity = new BandWEntity(
							jsonObject.optString("list_type").equals("1")
							, jsonObject.optString("Id")
							, jsonObject.optString("Title")
							, jsonObject.optString("Typeid")
							, Html.fromHtml(jsonObject.optString("Detail")).toString()
							, jsonObject.optString("Pic")
							, jsonObject.optString("Time"));
					
					if(!entity.getList_type()){
						String pic = entity.getPicName();
						if(log)Log.v("pic: " , pic);
						Bitmap bit = FuntionUtil.downloadPic("http://"+ globalID.getDBurl() +"/admin/images/" + pic);
						if(bit!=null){
							entity.setPic(bit);
	  					}
						else{
 	  		 				Bitmap good  = BitmapFactory.decodeResource(getResources(),R.drawable.weather_preview);
							entity.setPic(good);
 	  						}
					}
					
					globalID.BandWArrays.add(entity);
				}
			}
		}catch(JSONException e){
			e.printStackTrace();
			return;
		}
	}

	@Override
	 public void finish(){
		 super.finish();
		 if(log)Log.v(TAG,"finish");
		 overridePendingTransition(R.anim.list_in, R.anim.item_out);
//		 GlobalID globalID = ((GlobalID)getApplication());
//		 globalID.un_stop = true;
	 }
}
