package com.example.main_Adapter;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.main.R;
import com.example.main_Entity.TalkEntity;
import com.example.main_util.LogHelper;
import com.example.main_util.StringUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TalkAdapter extends BaseAdapter{
	private static final boolean log = false;
	private String TAG = "TalkAdapter";

	//define data
	@SuppressWarnings("unused")
	private Context ctx;
	private List<TalkEntity> coll;
	private LayoutInflater mInflater;
	private Handler Fail_Handler,ll_Handler;
	private String userId;

	public TalkAdapter(Context context , List<TalkEntity> coll 
			, Handler Fail_Handler
			, Handler ll_Handler
			,String userId){
		//get current page link data array
		this.coll = coll;
		this.ctx = context;
		this.Fail_Handler = Fail_Handler;
		this.ll_Handler = ll_Handler;
		this.userId = userId;
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

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		
		final TalkEntity entity = coll.get(position);
//		entity.index = position;
		if(log)Log.v(TAG, "position "+String.valueOf(position));
		if(log)Log.v(TAG, "entity.detail "+String.valueOf(position));
		
		ViewHolder viewHolder = null;
		
		if(convertView == null){
			
			//set current Adapter layout
			convertView = mInflater.inflate(R.layout.chatting_msg_item,null);
			
			//save item id in here for easy using
			viewHolder = new ViewHolder();

			viewHolder.tv_sendtime = (TextView)convertView.findViewById(R.id.tv_sendtime);
			viewHolder.tv_chatcontent_right = (TextView)convertView.findViewById(R.id.tv_chatcontent_right);
			viewHolder.tv_right = (TextView)convertView.findViewById(R.id.tv_right);
			viewHolder.tv_chatcontent_left = (TextView)convertView.findViewById(R.id.tv_chatcontent_left);
			viewHolder.tv_left = (TextView)convertView.findViewById(R.id.tv_left);
			
			viewHolder.ll2 = (LinearLayout)convertView.findViewById(R.id.ll2);
			viewHolder.ll4 = (LinearLayout)convertView.findViewById(R.id.ll4);
			
			viewHolder.chatting_layout_right = (RelativeLayout)convertView.findViewById(R.id.chatting_layout_right);
			viewHolder.chatting_layout_left = (RelativeLayout)convertView.findViewById(R.id.chatting_layout_left);

			viewHolder.avatar_chat_right = (ImageView)convertView.findViewById(R.id.avatar_chat_right);
			viewHolder.iv_right = (ImageView)convertView.findViewById(R.id.iv_right);
			viewHolder.avatar_chat_left = (ImageView)convertView.findViewById(R.id.avatar_chat_left);
			viewHolder.iv_left = (ImageView)convertView.findViewById(R.id.iv_left);
			viewHolder.msg_state_fail_resend = (ImageView)convertView.findViewById(R.id.msg_state_fail_resend);
			
			viewHolder.tv_progressBar = (ProgressBar)convertView.findViewById(R.id.tv_progressBar);
			
			//set Tag for current layout
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder = (ViewHolder)convertView.getTag();
		}


		viewHolder.ll2.setOnClickListener(new OnClickListener() {				
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.arg1 = (int) getItemId(position);
				msg.arg2 = StringUtil.CLICK;
				ll_Handler.sendMessage(msg);
			}
		});
		
		viewHolder.ll2.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.arg1 = (int) getItemId(position);
				msg.arg2 = StringUtil.LONG;
				ll_Handler.sendMessage(msg);
				return true;
			}
		});
		
		viewHolder.ll4.setOnClickListener(new OnClickListener() {				
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.arg1 = (int) getItemId(position);
				msg.arg2 = StringUtil.CLICK;
				ll_Handler.sendMessage(msg);
			}
		});
		
		viewHolder.ll4.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.arg1 = (int) getItemId(position);
				msg.arg2 = StringUtil.LONG;
				ll_Handler.sendMessage(msg);
				return true;
			}
		});
		
		//重发
		viewHolder.msg_state_fail_resend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.arg1 = (int) getItemId(position);
				msg.arg2 = StringUtil.FAIL;
				ll_Handler.sendMessage(msg);
			}
		});
		
		try {
			viewHolder.tv_sendtime.setText(StringUtil.YYMMDD.format(StringUtil.YYMMDD.parseObject(entity.getPostTime())));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (true) {
			try {
				Date d2 = (Date) StringUtil.YYMMDD.parseObject(entity.getPostTime());
				Date d3 = Calendar.getInstance().getTime();
				long Year = Math.abs(d2.getYear() - d3.getYear());
				long Month = Math.abs(d2.getMonth() - d3.getMonth());
				long Date = Math.abs(d2.getDate() - d3.getDate());
				long Day = Math.abs(d3.getDay());
//				LogHelper.trace(TAG + " d2", d2.toString());
//				LogHelper.trace(TAG + " d3", d3.toString());
				if(position == 0)viewHolder.tv_sendtime.setVisibility(View.VISIBLE);
				else {
					Date d1 = (Date) StringUtil.YYMMDD
							.parseObject(coll.get(position - 1).getPostTime());

					// 当这条消息距上条消息的时间小于两分钟时，不显示时间信息
					if (Math.abs(d1.getTime() - d2.getTime()) <= 5*60 * 1000) {
						viewHolder.tv_sendtime.setVisibility(View.GONE);
					}
					else viewHolder.tv_sendtime.setVisibility(View.VISIBLE);
				}
				/**
				 * JavaScript中的getDay()与getDate()方法的区别
				 * getDay()返回一星期中的某一天（0-6）
				 * getDate()返回一个月中的某一天（1-31）
				 */
				
				if(Year > 0 || Month > 0 || Date > Day + 7)viewHolder.tv_sendtime.setText("更久 " + StringUtil.YYMMDD.format(d2));
				else if (Date > Day && Date < Day + 7)viewHolder.tv_sendtime.setText("上周 " + StringUtil.MMDD.format(d2));
					else if (Date > 1 && Date < Day)viewHolder.tv_sendtime.setText("本周 " + StringUtil.MMDD.format(d2));
						else if (Date == 1)viewHolder.tv_sendtime.setText("昨天 " + StringUtil.HHMM.format(d2));
							else viewHolder.tv_sendtime.setText("今天 " + StringUtil.HHMM.format(d2));
				
				
//				if(Month > 0){
//					viewHolder.tv_sendtime.setText("更久 " + StringUtil.YYMMDD.format(d2));					
//				}
//				else{
//					if(Date > 6 && Date < 12){
//						viewHolder.tv_sendtime.setText("上周 " + StringUtil.MMDD.format(d2));							
//					}
//					else{
//						if(Date > 1){
//							viewHolder.tv_sendtime.setText("本周 " + StringUtil.MMDD.format(d2));	
//						}
//						if(Date == 1){
//							viewHolder.tv_sendtime.setText("昨天 " + StringUtil.HHMM.format(d2));	
//						}
//						if(Date == 0){
//							viewHolder.tv_sendtime.setText("今天 " + StringUtil.HHMM.format(d2));	
//						}
//					}
//				}
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		//发送的信息
		if(entity.getIsSend()){
			viewHolder.chatting_layout_left.setVisibility(View.GONE);
			viewHolder.chatting_layout_right.setVisibility(View.VISIBLE);
			viewHolder.tv_right.setText(entity.getSend_user_id());
			//文本信息
			if(entity.getIsMsg()){
				viewHolder.tv_chatcontent_right.setText(entity.getDetail());
				viewHolder.tv_chatcontent_right.setVisibility(View.VISIBLE);
				viewHolder.iv_right.setVisibility(View.GONE);
			}
			else{
				viewHolder.iv_right.setImageBitmap(entity.getImage_picture());
				viewHolder.iv_right.setVisibility(View.VISIBLE);
				viewHolder.tv_chatcontent_right.setVisibility(View.GONE);
			}
			
			if (entity.getState() == TalkEntity.MSG_SENDING) {
				viewHolder.tv_progressBar.setVisibility(View.VISIBLE);
				viewHolder.msg_state_fail_resend.setVisibility(View.GONE);
			} else if (entity.getState() == TalkEntity.MSG_SENT) {
				viewHolder.tv_progressBar.setVisibility(View.GONE);
				viewHolder.msg_state_fail_resend.setVisibility(View.GONE);
			} else {
				viewHolder.tv_progressBar.setVisibility(View.GONE);
				viewHolder.msg_state_fail_resend.setVisibility(View.VISIBLE); // 显示发送失败的图标
			}
		}
		else{
			viewHolder.chatting_layout_right.setVisibility(View.GONE);
			viewHolder.chatting_layout_left.setVisibility(View.VISIBLE);
			viewHolder.tv_left.setText(entity.getSend_user_id());
			
			if(entity.getIsMsg()){
				viewHolder.tv_chatcontent_left.setText(entity.getDetail());
				viewHolder.tv_chatcontent_left.setVisibility(View.VISIBLE);	
				viewHolder.iv_left.setVisibility(View.GONE);		
			}
			else{
				viewHolder.iv_left.setImageBitmap(entity.getImage_picture());
				viewHolder.iv_left.setVisibility(View.VISIBLE);
				viewHolder.tv_chatcontent_left.setVisibility(View.GONE);	
			}			
		}		
		return convertView;
	}
	
	static class ViewHolder{

		public TextView tv_sendtime;
		public TextView tv_chatcontent_right;
		public TextView tv_right;
		public TextView tv_chatcontent_left;
		public TextView tv_left;
		
		public LinearLayout ll2;
		public LinearLayout ll4;
		
		public RelativeLayout chatting_layout_right;
		public RelativeLayout chatting_layout_left;
		
		public ImageView iv_right;
		public ImageView avatar_chat_right;
		public ImageView iv_left;
		public ImageView avatar_chat_left;
		public ImageView msg_state_fail_resend;
		
		public ProgressBar tv_progressBar;
		
		public int index;
	}
}
