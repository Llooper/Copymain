package com.example.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.main.MsgListView.OnPullDownListener;
import com.example.main_Adapter.BandWAdapter;
import com.example.main_Entity.BandWEntity;
import com.example.main_Info.BandWInfo;
import com.example.main_util.FuntionUtil;
import com.example.main_util.LogHelper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.StrictMode;
import android.text.ClipboardManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class BandWList extends ListActivity{
	
	private int time_out = 2000;
	private Context context = BandWList.this;

	//����list�ľ���
	private int left = 13; //dp
	private int top = 4;
	private int right = 13;
	private int bottom = 0;
	
	private int divider = 20;
	
	/**
	 * �����Ƿ��ӡ
	 */
	private static boolean log = true;
	/**
	 * BandWList
	 */
	private String TAG = "BandWList";
	
	//���ñ�����ɫ
	private String background_colour = "#D4DEE8";
	
	//connect address
	InetAddress serverAddr;
	
	//test data
//	static int k = 0;
	
	//MsgListView and Adapter
	private MsgListView mListView ;
	private BandWAdapter mAdapter;
	
	private String currentStart = "2013-01-01 00:00:00";
	private String currentEnd = "3000-12-31 23:59:59";
	//����ÿ�δ����ݿ���ȡ�������Ŀ
	private int currentLine = 5;
	//��¼ ÿ�θ��µ���Ŀ
	private int updateLines = 0;
	//��ֹˢ��̫Ƶ��
	private long exitTime = 0;
	
	private List<BandWEntity> mArrays = new ArrayList<BandWEntity>();
//	private List<bussinessEntity> mArrays2 = new ArrayList<bussinessEntity>();

//	ProgressDialog pd = null;

	Message msg = new Message();
	//���б�����������ݣ���������һ��handler������
	final Handler add_handler = new Handler(){
		@Override
		public void  handleMessage (Message msg){
			if(msg.what == 1){
				mArrays.add((BandWEntity) msg.obj);
			}
			else {
				mArrays.add(0,(BandWEntity) msg.obj);				
			}
		}
	};
	
	final Handler mhandler = new Handler(){
		@Override
		public void  handleMessage (Message msg){
			mListView.requestLayout();
			mAdapter.notifyDataSetChanged();
			switch (msg.what){
			case -2:
				((MsgListView) getListView()).onRefreshComplete();
				break;
			case -1:
				((MsgListView) getListView()).onMoreComplete();
				break;
			case 0:
//				if(log)Log.v("mhandler", "case 0");
				toast("��ѯ����");
				break;
			case 1:
//				if(log)Log.v("mhandler", "case 1");
				if(updateLines!=0){
					toast("��ʱ�����"+ updateLines +"������Ϣ");
					break;
				}
			case 2:
//				if(log)Log.v("mhandler", "case 2");
				if(updateLines!=0){
					toast("ˢ��"+ updateLines +"������Ϣ");
					break;
				}
			case 3:
//				if(log)Log.v("mhandler", "case 3");
				if(updateLines!=0){
					toast("����"+ updateLines +"����ʷ��Ϣ");
					break;
				}
			case 4:
//				if(log)Log.v("mhandler", "case 4");
				toast("û���ҵ�����Ϣ");
				break;
			case 5:
				GlobalID globalID = ((GlobalID)getApplication());
				globalID.cancelPD();
				break;
				}
		}
	};
	
	//�б�ĵ���¼�������onItemClick
	final Handler ll_Handler = new Handler(){
		@Override
		public void  handleMessage (final Message msg){
			
			LogHelper.trace(String.valueOf(msg.arg1) + " " + String.valueOf(msg.arg2));
			GlobalID globalID = ((GlobalID)getApplication());
			globalID.setBandWArrays(mArrays);
			globalID.un_stop = true;
			Bundle bundle = new Bundle();
			bundle.putInt("index", msg.arg1);
			bundle.putString("colour", background_colour);
			
			Intent intent = new Intent(BandWList.this,BandWInfo.class);
			
			//post data
			intent.putExtras(bundle);
            startActivity(intent);
		}
	};
	
		@Override 
		public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		GlobalID globalID = ((GlobalID)getApplication());
		
		/******
		 * �������б������Ϣʱ
		 * �Ȱѱ������Ϣ�����л�
		 */
		if(null != savedInstanceState){
			final String decode = savedInstanceState.getString("mArrays");
	    	if(log)Log.e(TAG, "null != savedInstanceState mArrays = "+ decode);
	    	globalID.start(context);
	    	/**
	    	 * �漰��ͼƬ�����أ���Ҫ���߳����ء�
	    	 * �Ժ������ɱ������ݿ�ͱ��ص�ͼƬ���Ͳ���Ҫ��
	    	 */
	    	if(globalID.BandWArrays.isEmpty()){
		    	Thread getImg = new Thread(){
					public void run(){
						getmArrays(decode);
					}
				};
				getImg.start();	    	
			}
	    	else mArrays = globalID.BandWArrays;
		}
		
		/*
		 * ȥ��һ��title���������Ű���ע�͵����Ϳ���Ч���ˣ�����ÿ��Acitivity�����õ���
		 * ����һ�����������ӵĽ��棬msglistviewΪ���ӵ�xml�ļ�����
		 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.msglistview);
		if(log)Log.v(TAG,"onCreate");

		//���ֶ������ͺ��ʺϰ�F3��ȥ������ʲô��
		initView();
		
		if(globalID.getID() == null||globalID.getStartDate() == null||globalID.getEndDate() == null){
			globalID.start(context);
		}
			currentStart = globalID.getStartDate();
			currentEnd = globalID.getEndDate();
			currentLine = globalID.getLine();
					
			mArrays.clear();
			/*
			 * ���õ�ListView��Demo�������õ�
			 */
			GetDataTask bus = new GetDataTask();
			bus.startTime = currentStart;
			bus.endTime = currentEnd;
			bus.line = currentLine;
			bus.Case = 1;
			bus.Insert_Head = false;
//			bus.setId = "!= -1";
			bus.DESC = "DESC";

//			globalID.PD(context, "����", "���ڻ�ȡ���ݡ�");
			bus.execute();
		
//		if(globalID.isBus_change()){
//			if(log)Log.v(TAG,"isBus_change");
//			globalID.setBus_change(false);
//                exitTime = System.currentTimeMillis();
//			try{
//				if(globalID.getID() == null||globalID.getStartDate() == null||globalID.getEndDate() == null){
//					globalID.start();
//				}
//					currentStart = globalID.getStartDate();
//					currentEnd = globalID.getEndDate();
//					currentLine = globalID.getLine();
//							
//					mArrays.removeAll(mArrays);
//					GetDataTask bus = new GetDataTask();
//					bus.startTime = currentStart;
//					bus.endTime = currentEnd;
//					bus.line = currentLine;
//					bus.Case = 1;
//					bus.Insert_Head = false;
//					bus.setId = "!= -1";
//					bus.DESC = "DESC";
//
//					pd = ProgressDialog.show(BandWList.this, "����", "���ڻ�ȡ���ݡ�");
//					bus.execute();
//				
//			}
//			catch(Exception e){
//				if(log)Log.v(TAG,"Exception" + e);
//			}
//		}
		
		}

		private void initView() {
			// TODO Auto-generated method stub
			final GlobalID globalID = ((GlobalID)getApplication());
			mListView = (MsgListView)findViewById(android.R.id.list);
			
			/*
			 * ����������OnItemClickListener�������ķ�������������ĵ���֪��Ϊʲôû�������ˡ�
			 * ���Բο�һ�£��Ժ�������á�
			 * ����ע������OnItemClickListener�Ժ󣬺����ǻ�������View���¼��ġ�
			 */
			
//			mListView.setOnItemClickListener(new OnItemClickListener(){
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//		                long arg3) {
//					globalID.un_stop = true;
//					
//					//set picture
//					
////					if(log)Log.v(TAG, String.valueOf(mArrays.size()));
//					Bundle bundle = new Bundle();
//					bundle.putInt("index", arg2-1);
//					bundle.putString("colour", background_colour);
//					
//					Intent intent = new Intent(BandWList.this,BandWInfo.class);
//					
//					//post data
//					intent.putExtras(bundle);
//		            startActivity(intent);
//			}
//			});
//			
//			mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//				@Override
//				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//						final int arg2, long arg3) {
//					// TODO Auto-generated method stub
//					
//					new AlertDialog.Builder(BandWList.this).  
//					setTitle("ɾ����Ϣ").setMessage("ȷ��ɾ����").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {  
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							dialog.dismiss();
//							globalID.BusDeleteArrays.add(mArrays.get(arg2-1));
//							mArrays.remove(arg2-1);
//							mAdapter.notifyDataSetChanged();
//							
//							toast("ɾ��һ����Ϣ");
//							}
//						}).show();
//					return true;
//				}
//			});
			
			/*
			 * ����һ��ʽ���ñ�������ɫ��BackgroundColor����Ȼ��
			 * ����һ����������ͼƬ�ģ�С�ܲ��ţ�֮ǰ����ֱ������һ��������ȥ�ġ�����
			 * ���ñ���ͼƬ�����ַ��� setBackgroundResource��setBackgroundDrawable���ղؼ�������
			 * ��������һ���ں���ʦ�������ֻ������ġ������´Σ�ע��㡣����
			 */
			mListView.setBackgroundColor(android.graphics.Color.parseColor(background_colour));
//			mListView.setBackgroundResource(R.drawable.text_background);
			
			/*
			 * ��ȡdp��Ӧ��pxֵ����F3�鿴���庯������Ҳ�ǰٶ�������
			 * ����ľ��������ˣ����ñ߾࣬����Adapter����������
			 */
			left = FuntionUtil.dip2px(BandWList.this, left);
			top = FuntionUtil.dip2px(BandWList.this, top);
			right = FuntionUtil.dip2px(BandWList.this, right);
			bottom = FuntionUtil.dip2px(BandWList.this, bottom);
			
			divider = FuntionUtil.dip2px(BandWList.this, divider);
			mListView.setPadding(left, top, right, bottom);
			mListView.setDividerHeight(divider);
			
			mAdapter = new BandWAdapter(context,mArrays,ll_Handler);
			mListView.setAdapter(mAdapter);
			

			//���ÿ����Զ���ȡ���� �������һ���Զ���ȡ  �ĳ�false�������Զ���ȡ����
			/*
			 * ������һ������������Demo�����еģ���ʵ���ϣ����ⲿ��������û�д���á�
			 * ��ӭ���²鿴ListViewPull��ҪOpen project��
			 */
			mListView.enableAutoFetchMore(false, 1);
//			//���� ������β��
//			mListView.setHideFooter();
//			//��ʾ�������Զ���ȡ����
//			mListView.setShowFooter();
			
			/*
			 * �����ӿڵ�ʵ�֣�ˢ�º͸���
			 */
			mListView.setOnPullDownListener(new OnPullDownListener(){
				/**�����¼��ӿ�  ����Ҫע����ǻ�ȡ������ Ҫ�ر� ����Ľ����� notifyDidMore()**/

				@Override
				public void onRefresh() {
					// TODO Auto-generated method stub
					/*
					 * ��΢����һ�£�������ˢ�µ�̫Ƶ��
					 */
					if((System.currentTimeMillis()-exitTime) < time_out){
						Thread sleep_thread = new Thread(){
							public void run(){
								try {
									sleep(time_out);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								/*
								 * �ǵã��߳������ǲ���������Ĳ����ģ�����
								 * ����ʹ��Handler
								 */
								Message msg = new Message();
								msg.what = -2;
								mhandler.sendMessage(msg);
							}
						};
						sleep_thread.start();
		            }
					else{
						refresh();
					}
				}

				@Override
				public void onMore() {
					// TODO Auto-generated method stub
					if((System.currentTimeMillis()-exitTime) < time_out){
						Thread sleep_thread = new Thread(){
							public void run(){
								try {
									sleep(time_out);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Message msg = new Message();
								msg.what = -1;
								mhandler.sendMessage(msg);
							}
						};
						sleep_thread.start();
		            }
					else{
					GetDataTask bus = new GetDataTask();
					if(!mArrays.isEmpty()){
						if(log)Log.v(TAG,"mArrays.size()-1:" + (mArrays.size()-1));
						if(mArrays.size()-1 < 0){
							bus.endTime = currentStart;
//							bus.setId = "!= -1";							
						}
						else{
							bus.endTime = mArrays.get(mArrays.size()-1).getTime();
//							bus.setId = "< "+ mArrays.get(mArrays.size()-1).getId();
							if(log)Log.v("onmore", "startTime = "+ mArrays.get(mArrays.size()-1).getTime());							
						}
					}
					else {
						bus.endTime = currentStart;
//						bus.setId = "!= -1";
					}
					bus.startTime = "0-0-0";
					bus.execute();
					bus.Case = 3;
					bus.line = currentLine;
					bus.Insert_Head = false;
					bus.DESC = "DESC";
	                exitTime = System.currentTimeMillis();
					}
				}
			});
		}
		/*
		 * Ӧ����һ�������õ��ĺ������ѣ�����
		 */
		public void onOtherResume(){
			super.onResume();
			if(log)Log.v(TAG,"bus_onOtherResume");
			GlobalID globalID = ((GlobalID)getApplication());
			globalID.un_stop = false;
			if(globalID.isBus_push_UnGet()){
//				if(log)Log.v(TAG,"bus_push");
				refresh();
				globalID.setBus_push_UnGet(false);
			}
		}
		
		/**
		 * ˢ�º���
		 */
		public void refresh(){
			GlobalID globalID = ((GlobalID)getApplication());
			GetDataTask bus = new GetDataTask();
			
			/*
			 * �����鲻Ϊ��ʱ����ȡ��һ�����ݵ�ʱ�䣬Ȼ�����ˢ�¡�
			 * ��Talk���棬����İڷŷ�������һ�������Ի�ȡ�������һ����
			 * �ǵ���Android2.����ϵͳ���棬String.isEmpty()����String.equals()�������ע��һ�¡�
			 */
			if(!mArrays.isEmpty()){
				bus.startTime = mArrays.get(0).getTime();
//				bus.setId = "> "+mArrays.get(0).getId();
//				if(log)Log.v("refresh", "startTime = "+ mArrays2.get(0).getBussiness_PostTime());
			}
			else {
				Calendar sDate = Calendar.getInstance();
				sDate.add(Calendar.DATE,-1);
				bus.startTime = sDate.get(Calendar.YEAR)+"-"+(sDate.get(Calendar.MONTH)+1)+"-"+sDate.get(Calendar.DATE);
//				bus.setId = "!= -1";
			}
			Calendar eDate = Calendar.getInstance();
			bus.endTime = eDate.get(Calendar.YEAR)+"-"+(eDate.get(Calendar.MONTH)+1)+"-"+eDate.get(Calendar.DATE);
			bus.Case = 2;
			bus.line = currentLine;
			bus.Insert_Head = true;
			bus.DESC = "ASC";
            exitTime = System.currentTimeMillis();
			bus.execute();
		}
		
		/**
		 * (non-Javadoc)
		 * @see android.app.Activity#onResume()
		 * onResume��ϵͳ������֮һ���ٶȿ���Acitivity������
		 */
		@Override
		protected void onResume(){
			super.onResume();
			if(log)Log.v(TAG,"onResume");
//			mListView.headView.setPadding(0, -1 * mListView.headContentHeight, 0, 0);
//			//refresh headView...not a good idea
////			if((System.currentTimeMillis()-exitTime) < time_out){
////				return;
////            }
////			else{
//			GlobalID globalID = ((GlobalID)getApplication());
//			if(globalID.getCurrent_code() != 0){
////				if(log)Log.v(TAG,"getCurrent_code != 0 ?");
//				return;
//			}
//			if(globalID.isBus_push_UnGet()){
//				if(log)Log.v(TAG,"bus_push_unget");
//				pd = ProgressDiaif(log)Log.show(bussiness_message.this, "����", "���ڻ�ȡ���ݡ�");
//				refresh();
//				globalID.setBus_push_UnGet(false);
//				return;
//			}
//			if(globalID.isBus_change()){
//				if(log)Log.v(TAG,"isBus_change");
//				globalID.setBus_change(false);
//	                exitTime = System.currentTimeMillis();
//				try{
//					if(globalID.getID() == null||globalID.getStartDate() == null||globalID.getEndDate() == null){
//						globalID.start();
//					}
//						currentStart = globalID.getStartDate();
//						currentEnd = globalID.getEndDate();
//						currentLine = globalID.getLine();
//								
//						mArrays.removeAll(mArrays);
//						GetDataTask bus = new GetDataTask();
//						bus.startTime = currentStart;
//						bus.endTime = currentEnd;
//						bus.line = currentLine;
//						bus.Case = 1;
//						bus.Insert_Head = false;
//						bus.setId = "!= -1";
//						bus.DESC = "DESC";
//
//						pd = ProgressDiaif(log)Log.show(bussiness_message.this, "����", "���ڻ�ȡ���ݡ�");
//						bus.execute();
//					
//				}
//				catch(Exception e){
//					if(log)Log.v(TAG,"Exception" + e);
//				}
//			}
			
//			}
			return;
			
		}
		
		/*********************test data**************************/
//		private void initData(int k) {
//			// TODO Auto-generated method stub
//
//			bussinessEntity entity = new bussinessEntity();
//			entity.setBussiness_detail(String.valueOf(k));
//			Bitmap good  = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
//			entity.setBussiness_picture(good);
//			
//			//set the last to the top
//				if(mArrays.isEmpty()){
//					mArrays.add(0,entity);
//					mArrays.add(entity);
//					return;
//					}
//				else{
//					mArrays.add(mArrays.get(mArrays.size()-1));
//					for(int i = mArrays.size()-3;i>-1;i--){
//						mArrays.set(i+1, mArrays.get(i));
//						}
//
//					mArrays.set(0,entity);
//					mAdapter.notifyDataSetChanged();
					//complete the refresh picture
//					((MsgListView) getListView()).onRefreshComplete();
//				}
//		}
		/*********************test data**************************/
		
		/**
		 * ����������õ�����ˢ�µ�Demo��doInBackground�Ǵ������ݣ����ڿ�һ���߳����������飨���ܴ�����棡����
		 * ����һ��onPostExecute�е���Handler��������ܴ�����������
		 */
		private class GetDataTask extends AsyncTask<Void , Void , Void>{

			public String startTime;
			public String endTime;
			public int line;
			public int Case;
			public boolean Insert_Head;
//			public String setId;
			public String DESC;
			
			@SuppressLint("NewApi")
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				if(msg != null) msg = new Message();
				updateLines = 0;
				try {
					GlobalID globalID = ((GlobalID)getApplication());
					String url = globalID.getUrlServer();
					serverAddr = InetAddress.getByName(url);
					int SERVERPORT = globalID.getSERVERPORT();
//					if(startTime != globalID.getStartDate()||endTime != globalID.getEndDate()){
//						startTime = globalID.getStartDate();
//						endTime = globalID.getEndDate();
//					}
					Socket socket = new Socket();
					try {
						FuntionUtil.doSth();
						
//						socket = new Socket(serverAddr,SERVERPORT);
						//ͷ���Ǹ�����ǰ��������ֱ�����ӣ�����ķ����ǿ����������ӵ�ʱ��ġ��Ƽ�������
						InetSocketAddress socketAdd = new InetSocketAddress(serverAddr, SERVERPORT);
						socket.connect(socketAdd, globalID.getTIMEOUT());
						
				        try {
				            PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);

				            out.println(17);
//				            out.println(startTime);
//				            out.println(endTime);
				            out.println(startTime);
				            out.println(endTime);
				            out.println(line);
				            out.println(DESC);
				            
				            /*
				             * �����sqlֻ��������ӡ�����ģ�û��ʵ����;
				             */
				            String sql = "select TOP "+line+" bw.* from ( " +
									"SELECT 1 as list_type, " +
									"Id, Title,'' as Typeid, Content as Detail" +
									",'' as Pic, PostTime as Time FROM Board " +
									" UNION ALL " +
									"SELECT 0 as list_type, " +
									"Id, Title, Typeid, msg as Detail " +
									",ImageFile as Pic, PostTime as Time FROM Goods ) bw " +
									"where Convert(varchar(100),bw.Time,120)>='"+startTime+"'"+
									" and Convert(varchar(100),bw.Time,120)<='"+endTime+"'"+
									"ORDER BY bw.Time "+DESC+", bw.list_type";
				            if(log)Log.v(TAG,"sql: "+sql);
				            
				        } catch(Exception e) {
				                if(log)Log.v(TAG, "Exception  "+e);
				        }
				        finally {
				        	//��һ�������ö�ȡ��ʱ��ע�⣬�Ƕ�ȡ���������ӡ�
				            socket.setSoTimeout(globalID.getTIMEOUT());
				        	 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GB2312"));
				        	 String line = "";
				        	 String line2 = "";
				        	 
				        	 /*
				        	  * ��ȡ���ݣ�while�޷��жϣ���
				        	  * ��������Ϊ���ݿ����������лس��Ĺ�ϵ�����ԣ���������������Talk�Ǳ���ʵҲ��Ҫ��
				        	  * ����Ǹĳ��˳����ӵĻ��أ����Բο�ship2����global��recieve������������в�ͣ��ȡ�ĵط���
				        	  */				        	 
				        	 while((line2 = reader.readLine())!=null){
				        		 line += line2;
				        		 if(line2.charAt(line2.length()-1)==']')break;
				        	 }				        	 
				        	 
				        	 //����json������Ĳ������Ƿ�����������ͷ������Ϊ��ˢ�º͸�������
				        	 if(parseJson(line,Insert_Head)){
//			        			 if(!mArrays2.isEmpty())mArrays2.removeAll(mArrays2);
//				        		 if(log)Log.v("err", "S: Error");
//				        		 for(int j = 0; j < mArrays.size() ; j++){
//				        			 bussinessEntity bentity = new bussinessEntity();
//				        			 bentity = mArrays.get(mArrays.size()-j-1);
//				        			 mArrays2.add(bentity);
//				        		 }
				        		 
				        		 /*
				        		  * ���msg�и��鷳�ĵط���һ��msg��ֻ�ܱ�sendһ�Σ����ԣ�������Ϊ�գ���newһ��,
				        		  * ��ʵ����ֱ��������newһ���ͺ��ˡ�����
				        		  */
				        		 if(msg != null) msg = new Message();
				        		 msg.what = Case;
//			        			 if(log)Log.v("err", mArrays2.get(0).getBussiness_PostTime());
				        	 }
				        	 else{
				        		 if(msg != null) msg = new Message();
				                 msg.what = 4;
				        	 }
				        }
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						if(msg != null) msg = new Message();
		                msg.what = 0;
						e1.printStackTrace();
						if(log)Log.v(TAG, "IOException "+e1);
					}  
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					if(msg != null) msg = new Message();
	                msg.what = 0;
					e.printStackTrace();
					if(log)Log.v(TAG, "UnknownHostException "+e);
				}
				return null;
			}
			@Override
			protected void onPostExecute(Void result){
				//notify refresh data
				final GlobalID globalID = ((GlobalID)getApplication());
				globalID.mpAdapter.notifyDataSetChanged();
				if(Insert_Head)mListView.setSelection(updateLines);
				else {
					int select = mArrays.size() < mArrays.size()-updateLines ? 
							mArrays.size() : mArrays.size()-updateLines;
					mListView.setSelection(select);
				}
				if(log)Log.v(TAG,"get date finish");
				//complete the refresh picture
				((MsgListView) getListView()).onRefreshComplete();
				((MsgListView) getListView()).onMoreComplete();
                mhandler.sendMessage(msg);
				super.onPostExecute(result);
			}
			
		}
		
	    /***********Json for bussiness list******************/
	    private boolean parseJson(String resultObj,boolean Insert_Head){
	    	if(log)Log.v(TAG,"resultObj: "+resultObj);
	    	boolean insert = false;
	    	updateLines = 0;
			final GlobalID globalID = ((GlobalID)getApplication());
			try{
				JSONArray jsonArray = new JSONArray(resultObj);
				if(jsonArray.length()==0){
					return false;
					}
				else{
						for(int i = jsonArray.length()-1 ; i > -1  ; i--){
							insert = false;
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							BandWEntity entity = new BandWEntity(
									jsonObject.optString("list_type").equals("1")
									, jsonObject.optString("Id")
									, jsonObject.optString("Title")
									, jsonObject.optString("Typeid")
									, Html.fromHtml(jsonObject.optString("Detail")).toString()
									, jsonObject.optString("Pic")
									, jsonObject.optString("Time"));
							if(!entity.getList_type()){
								String pic = entity.getPicName();
								if(log)Log.v("pic: " , pic);
								Bitmap bit = FuntionUtil.downloadPic("http://"+ globalID.getDBurl() +"/admin/images/" + pic);
								if(bit!=null){
									entity.setPic(bit);
			  					}
								else{
		 	  		 				Bitmap good  = BitmapFactory.decodeResource(getResources(),R.drawable.weather_preview);
									entity.setPic(good);
		 	  						}
								
							}
							
							if(mArrays.size()-1 < 0 )
								if(log)Log.v(TAG,"mArrays.size()-1: " + (mArrays.size()-1));
							for(int j = mArrays.size()-1 ; j>-1 ; j--){
								if(entity.getId().equals(mArrays.get(j).getId())
										&& entity.getList_type() == mArrays.get(j).getList_type()){
									insert = true;
									break;
								}
							}
							if(insert)continue;
							if(Insert_Head){
								msg = new Message();
								msg.what = 0;
								msg.obj = entity;
								add_handler.sendMessage(msg);
//								mArrays.add(0,entity);
							}
							else {
								msg = new Message();
								msg.what = 1;
								msg.obj = entity;
								add_handler.sendMessage(msg);
//								mArrays.add(entity);
							}
							updateLines++;
//								insert = false;
//							}
//						}
					}
				}
			}catch(JSONException e){
				e.printStackTrace();
				return false;
			}
			//set current time for next refresh
//			startTime = mArrays.get(mArrays.size()-1).getBussiness_PostTime();
			return true;
		}
	    /***********Json for bussiness list******************/
	    
	    /**
	     * tosat��˾������һ����ɫ�Ŀ򵯳���Ϣ ���֣���������λ�ã�Ҳ������������ʽ�ġ�
	     */
	    void toast(String msg){
			GlobalID globalID = ((GlobalID)getApplication());
			if(globalID.pd!=null)globalID.pd.dismiss();
			if(globalID.toast != null)globalID.toast.cancel();
			globalID.toast = Toast.makeText(BandWList.this, msg, Toast.LENGTH_LONG);
			globalID.toast.show();
	    }

	    /**
	     * (non-Javadoc)
	     * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	     * 
	     * �������������������Ϣ�õģ�����˵��һ��Ӧ�÷��ں�̨�ܾã������Щ����϶���û�е���
	     * ������Ҫ���������������Ϣ���������л��ͷ����л����԰ٶ�һ�¡��ҵ�����أ����ǣ��������һ��String��������
	     * �պã������json����һ��������������ģ�����ֻҪ��������«�����°��������һ��String�Ϳ����ˡ�
	     * ��ϸ��������������û������������ǿ���NewMainActivity����İɡ�
	     * ������ô�����أ�������onCreate��ʱ���ж�һ��null != savedInstanceState�Ϳ�����
	     * ���о�����ô�ѳ��򵴵�������������˵�ġ���̫�á��أ�����������һֱ�ȵģ���DDMS�����Ͻǣ�����������õģ�һ��stop�İ�ť
	     * �������ĵ���д�ɣ��ظ�ͼ
	     */
	    @Override 
	    public void onSaveInstanceState(Bundle savedInstanceState) {  
	    	// Save away the original text, so we still have it if the activity   
	    	// needs to be killed while paused.
//			GlobalID globalID = ((GlobalID)getApplication());
//	    	savedInstanceState.putString("BandWArrays", globalID.Bus2String());

	    	savedInstanceState.putString("mArrays", List2String());
	    	super.onSaveInstanceState(savedInstanceState);  
	    	if(log)Log.e(TAG, "onSaveInstanceState");  
	    	}  
	    
	    @Override  
	    public void onRestoreInstanceState(Bundle savedInstanceState) {  
	    	super.onRestoreInstanceState(savedInstanceState);
//			GlobalID globalID = ((GlobalID)getApplication());
//	    	String StrTest = savedInstanceState.getString("BandWArrays"); 
	    	if(log)Log.e(TAG, "onRestoreInstanceState");  
	    	}  
	    
	    @Override
		 public void onBackPressed() {
			 //ʵ��Home��Ч�� 
			 //super.onBackPressed();��仰һ��Ҫע��,��Ȼ��ȥ����Ĭ�ϵ�back����ʽ�� 
			 Intent intent= new Intent(Intent.ACTION_MAIN); 
			 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			 intent.addCategory(Intent.CATEGORY_HOME); 
			 startActivity(intent);  
			 if(log)Log.v(TAG,"onBackPressed()");
		 }
	    @Override
	    public void onPause(){
	    	super.onPause();
	    	if(log)Log.v(TAG,"onPause");
	    }
	    
	    @Override
	    public void onStop(){
	    	super.onStop();
	    	if(log)Log.v(TAG,"onStop");
	    }
	    
	    /**
	     * ���ﻹ�и����л�
	     */
	    private String List2String() {
			// TODO Auto-generated method stub
	    	BandWEntity entity = new BandWEntity();
	    	String encode = "[";
			for(int i = 0 ; i < mArrays.size();){
				entity = mArrays.get(i);
				encode += "{";

				encode += "\"list_type\":\"" + entity.getList_type() + "\";";
				encode += "\"Id\":\"" + entity.getId() + "\";";
				encode += "\"Title\":\"" + entity.getTitle() + "\";";
				encode += "\"Typeid\":\"" + entity.getTypeid() + "\";";
				encode += "\"Detail\":\"" + entity.getDetail() + "\";";
				encode += "\"Pic\":\"" + entity.getPicName() + "\";";
				encode += "\"Time\":\"" + entity.getTime() + "\"";
				
				encode += "}";
				if(++i < mArrays.size()){
					encode += ",";
				}
			}
			encode += "]";
			if(log)LogHelper.trace(TAG, encode);
			return encode;
		}

	    /**
	     * �����л�
	     */
	    private void getmArrays(String decode) {
			// TODO Auto-generated method stub
	    	GlobalID globalID = (GlobalID)getApplication();
			try{
				JSONArray jsonArray = new JSONArray(decode);
				if(jsonArray.length()==0){
					return;
					}
				else{
					for(int i = 0 ; i < jsonArray.length() ; i++){
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						
						BandWEntity entity = new BandWEntity(
								jsonObject.optString("list_type").equals("1")
								, jsonObject.optString("Id")
								, jsonObject.optString("Title")
								, jsonObject.optString("Typeid")
								, Html.fromHtml(jsonObject.optString("Detail")).toString()
								, jsonObject.optString("Pic")
								, jsonObject.optString("Time"));
						
						if(!entity.getList_type()){
							String pic = entity.getPicName();
							if(log)Log.v("pic: " , pic);
							Bitmap bit = FuntionUtil.downloadPic("http://"+ globalID.getDBurl() +"/admin/images/" + pic);
							if(bit!=null){
								entity.setPic(bit);
		  					}
							else{
	 	  		 				Bitmap good  = BitmapFactory.decodeResource(getResources(),R.drawable.weather_preview);
								entity.setPic(good);
	 	  						}
						}
						
//						mArrays.add(entity);
						msg = new Message();
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
