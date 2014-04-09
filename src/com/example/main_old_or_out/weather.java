package com.example.main_old_or_out;
//package com.example.main;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.example.main.MsgListView.OnRefreshListener;
//import com.example.main.weatherAdapter;
//
//import android.app.ListActivity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.Window;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//
//
//public class weather extends ListActivity{
//	
//	//test data
//	static int k = 0;
//	
//	private final String TAG = "order";
//	private MsgListView mListView ;
//	private weatherAdapter mAdapter;
//	
//	private List<weatherEntity> mDataArrays = new ArrayList<weatherEntity>();
//
//	@Override 
//	public void onCreate(Bundle savedInstanceState) { 
//	super.onCreate(savedInstanceState); 
//
//	//set no title
//	requestWindowFeature(Window.FEATURE_NO_TITLE);
//	setContentView(R.layout.msglistview);
//
//	
//	initView();
//	
//	mListView = (MsgListView)findViewById(android.R.id.list);
//	mListView.setOnItemClickListener(new OnItemClickListener(){
//		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                long arg3) {
//			
//			//set data to inner activity
////			String GID = ((weatherEntity)mDataArrays.get(arg2-1)).getgID();
////			String CID = ((weatherEntity)mDataArrays.get(arg2-1)).getcID();
//////			Log.v(TAG, String.valueOf(mDataArrays.size()));
////			Bundle bundle = new Bundle();
////			bundle.putString("GID", GID);
////			bundle.putString("CID", CID);
//			
//			Intent intent = new Intent(weather.this,weatherInfo.class);
//			
//			//post data
////			intent.putExtras(bundle);
//            startActivity(intent);
//	}
//	});
//	
//	
//	
//	
//	}
//
//	private void initView() {
//		// TODO Auto-generated method stub
//		mListView = (MsgListView)findViewById(android.R.id.list);
//		
//		mListView.setBackgroundColor(android.graphics.Color.parseColor("#00CCFF"));
//		
//		mAdapter = new weatherAdapter(this , mDataArrays);
//		mListView.setAdapter(mAdapter);
//		mListView.setonRefreshListener(new OnRefreshListener(){
//			public void onRefresh(){
////				new GetDataTask().execute();
//				initData(k++);
//				((MsgListView) getListView()).onRefreshComplete();
//			}
//		});
//	}
//	
//	private void initData(int k) {
//		// TODO Auto-generated method stub
//
//		weatherEntity entity = new weatherEntity();
//		entity.setWeather_detail(String.valueOf(k));
//		Bitmap good  = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
//		entity.setWeather_picture(good);
//		
//		//set the last to the top
//			if(mDataArrays.isEmpty()){
//				mDataArrays.add(0,entity);
//				return;
//				}
//			else{
//				mDataArrays.add(mDataArrays.get(mDataArrays.size()-1));
//				
//				for(int i = mDataArrays.size()-3;i>-1;i--){
//					mDataArrays.set(i+1, mDataArrays.get(i));
//					}
//
//				mDataArrays.set(0,entity);
//				mAdapter.notifyDataSetChanged();
//			}
//	}
//}
