package com.example.main_old_or_out;
//package com.example.main;
//
//import java.util.List;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//public class weatherAdapter extends BaseAdapter{
//
//	//define data
//	private List<weatherEntity> coll;
//	private Context ctx;
//	private LayoutInflater mInflater;
//	private final String TAG = "orda";
//
//	public weatherAdapter(Context context , List<weatherEntity> coll){
//		//get current page all data
//		ctx = context;
//		
//		//get current page link data array
//		this.coll = coll;
//		
//		//get current page layout
//		mInflater = LayoutInflater.from(context);
//	}
//	
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return coll.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		// TODO Auto-generated method stub
//		return coll.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		// TODO Auto-generated method stub
//		return position;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent){
//		
//		weatherEntity entity = coll.get(position);
//		
//		ViewHolder viewHolder = null;
//		
//		if(convertView == null){
//			
//			//set current Adapter layout
//			convertView = mInflater.inflate(R.layout.weather_list,null);
//			
//			//save item id in here for easy using
//			viewHolder = new ViewHolder();
//			
//			viewHolder.weather_picture = (ImageView)convertView.findViewById(R.id.iv_weather_list_picture);
//			viewHolder.weather_detail = (TextView)convertView.findViewById(R.id.tv_weather_list_detail);
//			
//			//set Tag for current layout
//			convertView.setTag(viewHolder);
//		}
//		else{
//			viewHolder = (ViewHolder)convertView.getTag();
//		}
//
//		viewHolder.weather_picture.setImageBitmap(entity.getWeather_picture());
//		viewHolder.weather_detail.setText(entity.getWeather_detail());
//		
//		return convertView;
//	}
//	
//	static class ViewHolder{
//		
//		public ImageView weather_picture;
//		
//		public TextView weather_detail;
//	}
//}
