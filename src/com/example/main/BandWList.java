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

	//设置list的距离
	private int left = 13; //dp
	private int top = 4;
	private int right = 13;
	private int bottom = 0;
	
	private int divider = 20;
	
	/**
	 * 设置是否打印
	 */
	private static boolean log = true;
	/**
	 * BandWList
	 */
	private String TAG = "BandWList";
	
	//设置背景颜色
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
	//设置每次从数据库提取的最大数目
	private int currentLine = 5;
	//记录 每次更新的数目
	private int updateLines = 0;
	//防止刷新太频繁
	private long exitTime = 0;
	
	private List<BandWEntity> mArrays = new ArrayList<BandWEntity>();
//	private List<bussinessEntity> mArrays2 = new ArrayList<bussinessEntity>();

//	ProgressDialog pd = null;

	Message msg = new Message();
	//在列表里面添加数据，现在用了一个handler来处理
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
				toast("查询错误");
				break;
			case 1:
//				if(log)Log.v("mhandler", "case 1");
				if(updateLines!=0){
					toast("该时段最近"+ updateLines +"条新信息");
					break;
				}
			case 2:
//				if(log)Log.v("mhandler", "case 2");
				if(updateLines!=0){
					toast("刷新"+ updateLines +"条新信息");
					break;
				}
			case 3:
//				if(log)Log.v("mhandler", "case 3");
				if(updateLines!=0){
					toast("加载"+ updateLines +"条历史信息");
					break;
				}
			case 4:
//				if(log)Log.v("mhandler", "case 4");
				toast("没有找到新信息");
				break;
			case 5:
				GlobalID globalID = ((GlobalID)getApplication());
				globalID.cancelPD();
				break;
				}
		}
	};
	
	//列表的点击事件，代替onItemClick
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
		 * 当界面有保存的信息时
		 * 先把保存的信息反序列化
		 */
		if(null != savedInstanceState){
			final String decode = savedInstanceState.getString("mArrays");
	    	if(log)Log.e(TAG, "null != savedInstanceState mArrays = "+ decode);
	    	globalID.start(context);
	    	/**
	    	 * 涉及到图片的下载，需要开线程下载。
	    	 * 以后能做成本地数据库和本地的图片，就不需要了
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
		 * 去掉一个title，可以试着把它注释掉，就看到效果了，基本每个Acitivity都会用到。
		 * 下面一行是设置连接的界面，msglistview为连接的xml文件名。
		 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.msglistview);
		if(log)Log.v(TAG,"onCreate");

		//这种东西，就很适合按F3过去看看是什么了
		initView();
		
		if(globalID.getID() == null||globalID.getStartDate() == null||globalID.getEndDate() == null){
			globalID.start(context);
		}
			currentStart = globalID.getStartDate();
			currentEnd = globalID.getEndDate();
			currentLine = globalID.getLine();
					
			mArrays.clear();
			/*
			 * 我用的ListView的Demo是这样用的
			 */
			GetDataTask bus = new GetDataTask();
			bus.startTime = currentStart;
			bus.endTime = currentEnd;
			bus.line = currentLine;
			bus.Case = 1;
			bus.Insert_Head = false;
//			bus.setId = "!= -1";
			bus.DESC = "DESC";

//			globalID.PD(context, "连接", "正在获取数据…");
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
//					pd = ProgressDialog.show(BandWList.this, "连接", "正在获取数据…");
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
			 * 这里有两个OnItemClickListener，曾经的方法，如果看到文档就知道为什么没有沿用了。
			 * 可以参考一下，以后或许有用。
			 * 但是注意用了OnItemClickListener以后，好像是会屏蔽子View的事件的。
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
//					setTitle("删除信息").setMessage("确定删除？").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							dialog.dismiss();
//							globalID.BusDeleteArrays.add(mArrays.get(arg2-1));
//							mArrays.remove(arg2-1);
//							mAdapter.notifyDataSetChanged();
//							
//							toast("删除一条信息");
//							}
//						}).show();
//					return true;
//				}
//			});
			
			/*
			 * 上面一句式设置背景，颜色，BackgroundColor，显然的
			 * 下面一句呢是设置图片的，小弟不才，之前就是直接设置一个背景上去的。。。
			 * 设置背景图片有两种方法 setBackgroundResource和setBackgroundDrawable，收藏夹里面有
			 * 好像其中一种在何老师的三星手机里会崩的。。。下次，注意点。。。
			 */
			mListView.setBackgroundColor(android.graphics.Color.parseColor(background_colour));
//			mListView.setBackgroundResource(R.drawable.text_background);
			
			/*
			 * 获取dp对应的px值，按F3查看具体函数，我也是百度下来的
			 * 下面的就是设置了，设置边距，设置Adapter（适配器）
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
			

			//设置可以自动获取更多 滑到最后一个自动获取  改成false将禁用自动获取更多
			/*
			 * 上面这一句是它本来的Demo里面有的，但实际上，，这部分我现在没有处理好。
			 * 欢迎重新查看ListViewPull，要Open project的
			 */
			mListView.enableAutoFetchMore(false, 1);
//			//隐藏 并禁用尾部
//			mListView.setHideFooter();
//			//显示并启用自动获取更多
//			mListView.setShowFooter();
			
			/*
			 * 两个接口的实现，刷新和更多
			 */
			mListView.setOnPullDownListener(new OnPullDownListener(){
				/**更多事件接口  这里要注意的是获取更多完 要关闭 更多的进度条 notifyDidMore()**/

				@Override
				public void onRefresh() {
					// TODO Auto-generated method stub
					/*
					 * 稍微设置一下，不让它刷新得太频繁
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
								 * 记得，线程里面是不能做界面的操作的！！！
								 * 必须使用Handler
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
		 * 应该是一个曾经用到的函数而已，，，
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
		 * 刷新函数
		 */
		public void refresh(){
			GlobalID globalID = ((GlobalID)getApplication());
			GetDataTask bus = new GetDataTask();
			
			/*
			 * 当数组不为空时，获取第一个数据的时间，然后进行刷新。
			 * 在Talk里面，数组的摆放方法好像不一样，所以获取的是最后一个。
			 * 记得在Android2.几的系统里面，String.isEmpty()还是String.equals()，会出错，注意一下。
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
		 * onResume是系统的流程之一，百度看看Acitivity的流程
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
//				pd = ProgressDiaif(log)Log.show(bussiness_message.this, "连接", "正在获取数据…");
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
//						pd = ProgressDiaif(log)Log.show(bussiness_message.this, "连接", "正在获取数据…");
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
		 * 这个就是我用的下拉刷新的Demo，doInBackground是处理数据，等于开一个线程来做的事情（不能处理界面！！）
		 * 下面一个onPostExecute有点像Handler，这里才能处理界面的事情
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
						//头顶那个事以前的做法，直接连接，下面的方法是可以设置连接的时间的。推荐用下面
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
				             * 这里的sql只是用来打印看看的，没有实际用途
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
				        	//这一句是设置读取超时，注意，是读取，不是连接。
				            socket.setSoTimeout(globalID.getTIMEOUT());
				        	 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GB2312"));
				        	 String line = "";
				        	 String line2 = "";
				        	 
				        	 /*
				        	  * 读取数据？while无法判断？？
				        	  * 好像是因为数据库插入的数据有回车的关系，所以，，必须这样读，Talk那边其实也需要的
				        	  * 如果是改成了长连接的话呢，可以参考ship2里面global的recieve方法，里面就有不停读取的地方了
				        	  */				        	 
				        	 while((line2 = reader.readLine())!=null){
				        		 line += line2;
				        		 if(line2.charAt(line2.length()-1)==']')break;
				        	 }				        	 
				        	 
				        	 //解析json，后面的参数是是否插入在数组的头部，因为有刷新和更多两种
				        	 if(parseJson(line,Insert_Head)){
//			        			 if(!mArrays2.isEmpty())mArrays2.removeAll(mArrays2);
//				        		 if(log)Log.v("err", "S: Error");
//				        		 for(int j = 0; j < mArrays.size() ; j++){
//				        			 bussinessEntity bentity = new bussinessEntity();
//				        			 bentity = mArrays.get(mArrays.size()-j-1);
//				        			 mArrays2.add(bentity);
//				        		 }
				        		 
				        		 /*
				        		  * 这个msg有个麻烦的地方，一个msg，只能被send一次，所以，当它不为空，就new一个,
				        		  * 其实可以直接在这里new一个就好了。。。
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
	     * tosat吐司，就是一个黑色的框弹出信息 那种，可以设置位置，也可以有其它样式的。
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
	     * 这个就厉害咯，保存信息用的，就是说呢一个应用放在后台很久，你的那些数组肯定就没有掉的
	     * 所以需要这个函数来保存信息，关于序列化和反序列化可以百度一下。我的理解呢，就是，把它变成一个String。。。。
	     * 刚好，上面的json就是一个数组解析出来的，所以只要依样画葫芦，重新把他们组成一个String就可以了。
	     * 仔细看看，发现这里没有做这个，还是看看NewMainActivity里面的吧。
	     * 至于怎么调用呢？就是在onCreate的时候，判断一下null != savedInstanceState就可以了
	     * 还有就是怎么把程序荡掉，或者是上面说的“放太久”呢？不可能让你一直等的，在DDMS（右上角）里面可以设置的，一个stop的按钮
	     * 还是在文档里写吧，截个图
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
			 //实现Home键效果 
			 //super.onBackPressed();这句话一定要注掉,不然又去调用默认的back处理方式了 
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
	     * 这里还有个序列化
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
	     * 反序列化
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
