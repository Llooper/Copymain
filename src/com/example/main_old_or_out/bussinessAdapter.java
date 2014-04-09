package com.example.main_old_or_out;
//package com.example.main_Adapter;
//
//import java.util.List;
//
//import com.example.main.R;
//import com.example.main.R.drawable;
//import com.example.main.R.id;
//import com.example.main.R.layout;
//import com.example.main_Entity.bussinessEntity;
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
//public class bussinessAdapter extends BaseAdapter{
//
//	//define data
//	private List<bussinessEntity> coll;
//	private LayoutInflater mInflater;
//
//	public bussinessAdapter(Context context , List<bussinessEntity> coll){
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
//		bussinessEntity entity = coll.get(position);
//		
//		ViewHolder viewHolder = null;
//		
//		if(convertView == null){
//			
//			//set current Adapter layout
//			convertView = mInflater.inflate(R.layout.bussiness_list,null);
//			
//			//save item id in here for easy using
//			viewHolder = new ViewHolder();
//
//			//connect view id
//			viewHolder.bussiness_Title = (TextView)convertView.findViewById(R.id.tv_bussiness_list_Title);
//			viewHolder.bussiness_picture = (ImageView)convertView.findViewById(R.id.iv_bussiness_list_picture);
//			viewHolder.bussiness_detail = (TextView)convertView.findViewById(R.id.tv_bussiness_list_Detail);
//			viewHolder.ll_bussiness_list = (LinearLayout)convertView.findViewById(R.id.ll_bussiness_list);
//			//set Tag for current layout
//			convertView.setTag(viewHolder);
//		}
//		else{
//			viewHolder = (ViewHolder)convertView.getTag();
//		}
//
//		//set view item data
//		viewHolder.bussiness_Title.setText(entity.getBussiness_PostTime());
//		viewHolder.bussiness_picture.setImageBitmap(entity.getBussiness_picture());
//		viewHolder.bussiness_detail.setText(entity.getBussiness_detail());
//		viewHolder.ll_bussiness_list.setBackgroundResource(R.drawable.bus_content_new);
//		
//		return convertView;
//	}
//	
//	static class ViewHolder{
//		
//		public ImageView bussiness_picture;
//		
//		public TextView bussiness_detail;
//		public TextView bussiness_Title;
//		
//		public LinearLayout ll_bussiness_list;
//	}
//}
