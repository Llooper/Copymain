package com.example.main_Adapter;

import java.text.DecimalFormat;
import java.util.List;

import com.example.main.R;
import com.example.main.R.id;
import com.example.main.R.layout;
import com.example.main_Entity.SendPreEntity;
import com.example.main_util.StringUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SendPreAdapter extends BaseAdapter{

	//define data
	private List<SendPreEntity> coll;
	private LayoutInflater mInflater;
	private Handler ll_Handler;

	public SendPreAdapter(Context context , List<SendPreEntity> coll,Handler ll_Handler){
		//get current page link data array
		this.coll = coll;
		this.ll_Handler = ll_Handler;
		//get current page layout
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return coll.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return coll.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		
		final SendPreEntity entity = coll.get(position);
		
		ViewHolder viewHolder = null;
		
		if(convertView == null){
			
			//set current Adapter layout
			convertView = mInflater.inflate(R.layout.sendpre_list,null);
			
			//save item id in here for easy using
			viewHolder = new ViewHolder();

			//connect view id
			viewHolder.ll_SendPre_list = (RelativeLayout)convertView.findViewById(R.id.ll_SendPre_list);
			viewHolder.SendPre_picture = (ImageView)convertView.findViewById(R.id.iv_SendPre_list_picture);
			viewHolder.SendPre_Detail = (TextView)convertView.findViewById(R.id.tv_SendPre_list_Detail);
			viewHolder.SendPre_GPS = (TextView)convertView.findViewById(R.id.tv_SendPre_list_GPS);
			//set Tag for current layout
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder = (ViewHolder)convertView.getTag();
		}

		//set view item data
		viewHolder.SendPre_picture.setImageBitmap(entity.getSendPre_picture());
		viewHolder.SendPre_Detail.setText(entity.getSendPre_detail());
		
		if(entity.getSendPre_gps_la() == 0 && entity.getSendPre_gps_lo() == 0)
			viewHolder.SendPre_GPS.setText("没有最近位置");
		else viewHolder.SendPre_GPS.setText(String.format("纬:"+StringUtil.fnum.format(entity.getSendPre_gps_la()/1e6)+
				"\n经: "+ StringUtil.fnum.format(entity.getSendPre_gps_lo()/1e6)));

		viewHolder.ll_SendPre_list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.arg1 = entity.getArk_id();
				msg.arg2 = StringUtil.CLICK;
				ll_Handler.sendMessage(msg);
			}
		});
		
		return convertView;
	}
	
	static class ViewHolder{
		
		public RelativeLayout ll_SendPre_list;
		
		public ImageView SendPre_picture;
		
		public TextView SendPre_Detail;
		public TextView SendPre_GPS;
	}
}
