package com.example.main_old_or_out;
//package com.example.main_Adapter;
//
//import java.util.List;
//
//import com.example.main.R;
//import com.example.main.R.drawable;
//import com.example.main.R.id;
//import com.example.main.R.layout;
//import com.example.main_Entity.imageEntity;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//public class imageAdapter extends BaseAdapter{
//
//	//define data
//	private List<imageEntity> coll;
//	private LayoutInflater mInflater;
//
//	public imageAdapter(Context context , List<imageEntity> coll){
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
//		imageEntity entity = coll.get(position);
//		
//		ViewHolder viewHolder = null;
//		
//		if(convertView == null){
//			
//			//set current Adapter layout
//			convertView = mInflater.inflate(R.layout.image_list,null);
//			
//			//save item id in here for easy using
//			viewHolder = new ViewHolder();
//
//			viewHolder.image_datetime_send = (TextView)convertView.findViewById(R.id.tv_image_list_Title);
//			viewHolder.image_picture = (ImageView)convertView.findViewById(R.id.iv_image_list_picture);
//			viewHolder.image_user_id = (TextView)convertView.findViewById(R.id.tv_image_list_Detail);
//			viewHolder.ll_img_list = (LinearLayout)convertView.findViewById(R.id.ll_img_list);
//			
//			//set Tag for current layout
//			convertView.setTag(viewHolder);
//		}
//		else{
//			viewHolder = (ViewHolder)convertView.getTag();
//		}
//
//		viewHolder.image_datetime_send.setText(entity.getImage_datetime_send());
//		viewHolder.image_picture.setImageBitmap(entity.getImage_picture());
//		viewHolder.image_user_id.setText(entity.getSend_user_id());
//		viewHolder.ll_img_list.setBackgroundResource(R.drawable.bus_content_new);
//		
//		return convertView;
//	}
//	
//	static class ViewHolder{
//		
//		public ImageView image_picture;
//		
//		public TextView image_user_id;
//		public TextView image_datetime_send;
//		
//		public LinearLayout ll_img_list;
//	}
//}
