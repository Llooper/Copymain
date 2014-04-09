package com.example.main_Adapter;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.main.R;
import com.example.main_Entity.BandWEntity;
import com.example.main_Entity.BandWEntity;
import com.example.main_util.LogHelper;
import com.example.main_util.StringUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
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

public class BandWAdapter extends BaseAdapter{
	private final static boolean log = false;
	private String TAG = "BandWAdapter";

	//define data
	@SuppressWarnings("unused")
	private Context ctx;
	private List<BandWEntity> coll;
	private LayoutInflater mInflater;
	private Handler llHandler;

	public BandWAdapter(Context context , List<BandWEntity> coll 
			,Handler llHandler){
		//get current page link data array
		this.ctx = context;
		this.coll = coll;
		this.llHandler = llHandler;
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
		
		final BandWEntity entity = coll.get(position);
//		entity.index = position;
		if(log)Log.v(TAG, "position "+String.valueOf(position));
		if(log)Log.v(TAG, "entity.detail "+String.valueOf(position));
		
		ViewHolder viewHolder = null;
		
		/*
		 * �����ڿ�ʱ����Ҫ���ӿؼ���xml�ļ�
		 */
		if(convertView == null){
			
			//set current Adapter layout
			convertView = mInflater.inflate(R.layout.bus_and_wea_list,null);
			
			//save item id in here for easy using
			viewHolder = new ViewHolder();

			viewHolder.ll_list = (LinearLayout)convertView.findViewById(R.id.ll_list);
			
			viewHolder.list_out_title = (TextView)convertView.findViewById(R.id.list_out_title);
			viewHolder.list_time = (TextView)convertView.findViewById(R.id.list_time);
			
			viewHolder.list_pic = (ImageView)convertView.findViewById(R.id.list_pic);
			viewHolder.list_title = (TextView)convertView.findViewById(R.id.list_title);
			viewHolder.list_detail = (TextView)convertView.findViewById(R.id.list_detail);
			viewHolder.list_more = (ImageView)convertView.findViewById(R.id.list_more);
			viewHolder.list_title_pic = (ImageView)convertView.findViewById(R.id.list_title_pic);
			//set Tag for current layout
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		//�Ƿ�������Ϣ
		if(entity.getList_type()){
			viewHolder.list_pic.setVisibility(View.GONE);
			viewHolder.list_out_title.setText("��������");
			viewHolder.list_title_pic.setBackgroundResource(R.drawable.list_title_bus);
		}
		else{
			viewHolder.list_pic.setVisibility(View.VISIBLE);
			viewHolder.list_pic.setImageBitmap(entity.getPic());
			viewHolder.list_out_title.setText("����Ԥ��");
			viewHolder.list_title_pic.setBackgroundResource(R.drawable.list_title_wea);
		}
		
		try {
			viewHolder.list_time.setText(StringUtil.YYMMDD.format(StringUtil.YYMMDD.parseObject((entity.getTime()))));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		viewHolder.list_title.setText(entity.getTitle());
		TextPaint tp = viewHolder.list_title.getPaint();
		tp.setFakeBoldText(true);
		
		viewHolder.list_detail.setText(entity.getDetail());
		
		//û�ҵ���ʲô��������֪����û��ʡ�Ժš������������óɣ�û��ͼƬ����Ϣʱ��������Ϣ��
		//������԰������޶�ȥ����Ȼ����ʾȫ����Ϣ��
		if((entity.getList_type() && entity.getDetail().length()>30)
				|| !entity.getList_type() && entity.getDetail().length()>20)
			viewHolder.list_more.setVisibility(View.VISIBLE);
		else viewHolder.list_more.setVisibility(View.GONE);

		viewHolder.ll_list.setOnClickListener(new OnClickListener() {				
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.arg1 = (int) getItemId(position);
				msg.arg2 = StringUtil.CLICK;
				llHandler.sendMessage(msg);
			}
		});
		return convertView;
	}
	
	/**
	 * 
	 * @author Administrator
	 * ������Ӧ�ؼ�
	 */
	static class ViewHolder{

		public LinearLayout ll_list;
		
		public TextView list_out_title;
		public TextView list_time;

		public ImageView list_pic;
		public TextView list_title;
		public TextView list_detail;
		
		public ImageView list_more;
		public ImageView list_title_pic;
	}
}
