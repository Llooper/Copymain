package com.example.main_old_or_out;
//package com.example.main_Adapter;
//
//import java.util.List;
//
//import com.example.main.R;
//import com.example.main.R.drawable;
//import com.example.main.R.id;
//import com.example.main.R.layout;
//import com.example.main_Entity.TestEntity;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//public class TestAdapter extends BaseAdapter{
//
//	//define data
//	private List<TestEntity> coll;
//	private LayoutInflater mInflater;
//
//	public TestAdapter(Context context , List<TestEntity> coll){
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
//		TestEntity entity = coll.get(position);
//		
//		ViewHolder viewHolder = null;
//		
//		if(convertView == null){
//			
//			//set current Adapter layout
//			convertView = mInflater.inflate(R.layout.test_list,null);
//			
//			//save item id in here for easy using
//			viewHolder = new ViewHolder();
//
//			viewHolder.Test_PostTime = (TextView)convertView.findViewById(R.id.tv_test_list_PostTime);
//			viewHolder.Test_detail = (TextView)convertView.findViewById(R.id.tv_test_list_Detail);
//			viewHolder.Test_ll = (LinearLayout)convertView.findViewById(R.id.ll_test_list);
//			
//			//set Tag for current layout
//			convertView.setTag(viewHolder);
//		}
//		else{
//			viewHolder = (ViewHolder)convertView.getTag();
//		}
//
//		viewHolder.Test_PostTime.setText(entity.getTest_PostTime());
//		viewHolder.Test_detail.setText(entity.getTest_detail());
//		viewHolder.Test_ll.setBackgroundResource(R.drawable.bus_content_new);
//		
//		return convertView;
//	}
//	
//	static class ViewHolder{
//		
//		public TextView Test_detail;
//		public TextView Test_PostTime;
//		
//		public LinearLayout Test_ll;
//	}
//}
