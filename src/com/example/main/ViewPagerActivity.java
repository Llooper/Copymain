/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.example.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.senab.photoview.PhotoView;

import com.example.main.BandWViewPagerActivity.SamplePagerAdapter;
import com.example.main_Entity.BandWEntity;
import com.example.main_Entity.TalkEntity;
import com.example.main_Info.BandWInfo;
import com.example.main_util.FuntionUtil;
import com.example.main_util.LogHelper;
import com.example.main_util.Tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class ViewPagerActivity extends Activity {

	private final Context context = ViewPagerActivity.this;
	private final static boolean log = false;
	private final static String TAG = "ViewPagerActivity";
	
	private List<TalkEntity> ALL_List = new ArrayList<TalkEntity>();
	private List<TalkEntity> IMG_List = new ArrayList<TalkEntity>();

	private SamplePagerAdapter samplePagerAdapter = null;
	private ViewPager mViewPager = null;
	
	private int item = 0;
	private int index = 0;
	private boolean work = false;
	
	final Handler mhandler = new Handler(){
		@Override
		public void  handleMessage (Message msg){
			GlobalID globalID = ((GlobalID)getApplication());
			globalID.cancelPD();
			work = true;
		}
	};
	
	final Handler add_handler = new Handler(){
		@Override
		public void  handleMessage (Message msg){
			if(msg.what == 1){
				ALL_List.add((TalkEntity)msg.obj);
			}
			else {
				ALL_List.add(0,(TalkEntity)msg.obj);				
			}
		}
	};
	
	final Handler add_img_handler = new Handler(){
		@Override
		public void  handleMessage (Message msg){
			IMG_List.add((TalkEntity)msg.obj);
			if(msg.what == index)item = IMG_List.size()-1;
			samplePagerAdapter.notifyDataSetChanged();
			mViewPager.setCurrentItem(item);
		}
	};
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final GlobalID globalID = (GlobalID)getApplication();
		overridePendingTransition(R.anim.item_in, R.anim.list_out);
		
        setContentView(R.layout.activity_view_pager);
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
		setContentView(mViewPager);
		work = false;
		
		if(null != savedInstanceState){
			final String decode = savedInstanceState.getString("ALL_List");
			index = savedInstanceState.getInt("index");
	    	if(log)Log.e(TAG, "null != savedInstanceState ALL_List = "+ decode);
	    	globalID.start(context);
	    	if(globalID.BandWArrays.isEmpty()){
		    	globalID.PD(context, "获取数据", "努力中...");
		    	Thread decode_thread = new Thread(){
					public void run(){
						getTalkDataArrays(decode);
						Message msg = new Message();
						globalID.TalkList = ALL_List;
						mhandler.sendMessage(msg);
					}
				};
				decode_thread.start();
	    	}
		}
		else{
			Intent intent = getIntent();
			Bundle data = intent.getExtras();
			index = data.getInt("i");
			
			ALL_List = globalID.TalkList;
			work = true;
		}

		samplePagerAdapter = new SamplePagerAdapter(ViewPagerActivity.this, IMG_List);
		mViewPager.setAdapter(samplePagerAdapter);
		samplePagerAdapter.notifyDataSetChanged();
		
		Thread check = new Thread(){
			public void run(){
				while(!work){
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						continue;
					}
				}
				
				for(int i = 0;i<ALL_List.size();i++){
					TalkEntity entity = ALL_List.get(i);
					if(!entity.getIsMsg()){
						Message msg = new Message();
						msg.what = i;
						msg.obj = entity;
						add_img_handler.sendMessage(msg);
					}
				}			
			}
		};		
		check.start();
	}

	static class SamplePagerAdapter extends PagerAdapter {
		
		private Context context;
		private List<TalkEntity> coll;

		@Override
		public int getCount() {
			return coll.size();
		}
		
		public SamplePagerAdapter(Context context , List<TalkEntity> coll){
			this.coll = coll;
			this.context = context;
		}
		
		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(context);
			photoView.setImageBitmap(coll.get(position).getImage_picture());

			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}
	
	 @Override
	 public void onResume(){
		 super.onResume();
		 GlobalID globalID = ((GlobalID)getApplication());
		 globalID.cancel_notification();
	}

	@Override 
    public void onSaveInstanceState(Bundle savedInstanceState) {  
    	// Save away the original text, so we still have it if the activity   
    	// needs to be killed while paused.
    	super.onSaveInstanceState(savedInstanceState);
    	GlobalID globalID = ((GlobalID)getApplication());
    	savedInstanceState.putString("ALL_List", Talk2String());
    	savedInstanceState.putInt("index", index);
    	globalID.create_notification("后台接收数据", "后台运行", "岸客户端", false, false, false,ViewPagerActivity.class.getName());
	    if(globalID.toast != null)globalID.toast.cancel();
    	}

	private String Talk2String() {
		// TODO Auto-generated method stub
		GlobalID globalID = (GlobalID)getApplication();
    	TalkEntity entity = new TalkEntity();
    	String encode = "[";
		for(int i = 0 ; i < globalID.TalkList.size();){
			entity = globalID.TalkList.get(i);
			encode += "{";

			encode += "\"isMsg\":\"" + entity.getIsMsg() + "\";";
			encode += "\"isSend\":\"" + entity.getIsSend() + "\";";
			encode += "\"detail\":\"" + entity.getDetail() + "\";";
			encode += "\"PostTime\":\"" + entity.getPostTime() + "\";";
			encode += "\"ark_id\":\"" + entity.getArk_id() + "\";";
			encode += "\"send_user_id\":\"" + entity.getSend_user_id() + "\";";
			encode += "\"msg_id\":\"" + entity.getMsg_id() + "\";";
			encode += "\"state\":\"" + entity.getState() + "\"";
			
			encode += "}";
			if(++i < globalID.TalkList.size()){
				encode += ",";
			}
		}
		encode += "]";
		if(log)LogHelper.trace(TAG, encode);
		return encode;
	}
	
	private void getTalkDataArrays(String decode) {
		// TODO Auto-generated method stub
		try{
			JSONArray jsonArray = new JSONArray(decode);
			if(jsonArray.length()==0){
				return;
				}
			else{
				for(int i = 0 ; i < jsonArray.length() ; i++){
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					
					TalkEntity entity = new TalkEntity(Boolean.valueOf(jsonObject.optString("isMsg"))
							, Boolean.valueOf(jsonObject.optString("isSend"))
							
							, Html.fromHtml(jsonObject.optString("detail")).toString()
							, jsonObject.optString("PostTime")
							, jsonObject.optString("ark_id")
							, jsonObject.optString("send_user_id")
							, jsonObject.optString("msg_id"));
					
					entity.setState(Integer.valueOf(jsonObject.optString("state")));

					LogHelper.trace(jsonObject.optString("detail"));
					LogHelper.trace(Html.fromHtml(jsonObject.optString("detail")).toString());
					
					if(jsonObject.optString("isMsg").equals("false")){
						String pic = jsonObject.optString("detail");
						GlobalID globalID = ((GlobalID)getApplication());
						Bitmap bit = FuntionUtil.downloadPic("http://"+ globalID.getDBurl() +"/user/images" + pic);
						if(bit!=null){
							entity.setImage_picture(bit);
	  					}
						else{
 	  		 				Bitmap default_Img  = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
							entity.setImage_picture(default_Img);
 	  					}
					}
					
//					TalkDataArrays.add(entity);
					Message msg = new Message();
					msg.what = 1;
					msg.obj = entity;
					add_handler.sendMessage(msg);
				}
			}
		}catch(JSONException e){
			e.printStackTrace();
			return;
		}
	}
}
