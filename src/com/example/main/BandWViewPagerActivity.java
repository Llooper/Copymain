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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.senab.photoview.PhotoView;

import com.example.main_Entity.BandWEntity;
import com.example.main_Entity.TalkEntity;
import com.example.main_util.FuntionUtil;
import com.example.main_util.LogHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;

public class BandWViewPagerActivity extends Activity {

	private final Context context = BandWViewPagerActivity.this;
	private final static boolean log = false;
	private final static String TAG = "BnWViewPager";
	
	private List<BandWEntity> ALL_List = new ArrayList<BandWEntity>();
	private List<BandWEntity> IMG_List = new ArrayList<BandWEntity>();
	
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
				ALL_List.add((BandWEntity)msg.obj);
			}
			else {
				ALL_List.add(0,(BandWEntity)msg.obj);				
			}
		}
	};
	
	final Handler add_img_handler = new Handler(){
		@Override
		public void  handleMessage (Message msg){
			IMG_List.add((BandWEntity)msg.obj);
			if(msg.what == index)item = IMG_List.size()-1;
			samplePagerAdapter.notifyDataSetChanged();
			mViewPager.setCurrentItem(item);
		}
	};
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final GlobalID globalID = ((GlobalID)getApplication());
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
						getmArrays(decode);
						Message msg = new Message();
						globalID.BandWArrays = ALL_List;
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
			
			ALL_List = globalID.BandWArrays;
			work = true;
		}

		samplePagerAdapter = new SamplePagerAdapter(BandWViewPagerActivity.this, IMG_List);
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
					BandWEntity entity = ALL_List.get(i);
					if(!entity.getList_type()){
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
		private List<BandWEntity> coll;

		@Override
		public int getCount() {
			return coll.size();
		}
		
		public SamplePagerAdapter(Context context , List<BandWEntity> coll){
			this.coll = coll;
			this.context = context;
		}
		
		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(context);
			photoView.setImageBitmap(coll.get(position).getPic());

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
//		 globalID.un_stop = false;
	}

	@Override 
   public void onSaveInstanceState(Bundle savedInstanceState) {  
   	// Save away the original text, so we still have it if the activity   
   	// needs to be killed while paused.
   	super.onSaveInstanceState(savedInstanceState);
	savedInstanceState.putString("ALL_List", List2String());
	savedInstanceState.putInt("index", index);
	
   	GlobalID globalID = ((GlobalID)getApplication());
   	globalID.create_notification("后台接收数据", "后台运行", "岸客户端", false, false, false,BandWViewPagerActivity.class.getName());
	    if(globalID.toast != null)globalID.toast.cancel();
   	}

	@Override  
    public void onRestoreInstanceState(Bundle savedInstanceState) {  
    	super.onRestoreInstanceState(savedInstanceState);
//		GlobalID globalID = ((GlobalID)getApplication());
//    	String StrTest = savedInstanceState.getString("BandWArrays"); 
    	if(log)Log.e(TAG, "onRestoreInstanceState");
		GlobalID globalID = ((GlobalID)getApplication());
    	globalID.un_stop = false;
    }
	
	private String List2String() {
		// TODO Auto-generated method stub
    	BandWEntity entity = new BandWEntity();
    	String encode = "[";
		for(int i = 0 ; i < ALL_List.size();){
			entity = ALL_List.get(i);
			encode += "{";

			encode += "\"list_type\":\"" + entity.getList_type() + "\";";
			encode += "\"Id\":\"" + entity.getId() + "\";";
			encode += "\"Title\":\"" + entity.getTitle() + "\";";
			encode += "\"Typeid\":\"" + entity.getTypeid() + "\";";
			encode += "\"Detail\":\"" + entity.getDetail() + "\";";
			encode += "\"Pic\":\"" + entity.getPicName() + "\";";
			encode += "\"Time\":\"" + entity.getTime() + "\"";
			
			encode += "}";
			if(++i < ALL_List.size()){
				encode += ",";
			}
		}
		encode += "]";
		if(log)LogHelper.trace(TAG, encode);
		return encode;
	}
	
	private void getmArrays(String decode) {
		// TODO Auto-generated method stub
		try{
			GlobalID globalID = (GlobalID)getApplication();
			JSONArray jsonArray = new JSONArray(decode);
			if(jsonArray.length()==0){
				return;
				}
			else{
				for(int i = 0 ; i < jsonArray.length() ; i++){
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					
					BandWEntity entity = new BandWEntity(
							Boolean.valueOf(jsonObject.optString("list_type"))
							, jsonObject.optString("Id")
							, jsonObject.optString("Title")
							, jsonObject.optString("Typeid")
							, Html.fromHtml(jsonObject.optString("Detail")).toString()
							, jsonObject.optString("Pic")
							, jsonObject.optString("Time"));
					
					if(!entity.getList_type()){
						String pic = entity.getPicName();
						if(log)Log.v(i+" pic: " , pic);
						Bitmap bit = FuntionUtil.downloadPic("http://"+ globalID.getDBurl() +"/admin/images/" + pic);
						if(bit!=null){
							entity.setPic(bit);
	  					}
						else{
 	  		 				Bitmap good  = BitmapFactory.decodeResource(getResources(),R.drawable.weather_preview);
							entity.setPic(good);
 	  						}
					}
					
//					mArrays.add(entity);
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
