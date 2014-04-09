package com.example.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.platform.comapi.map.t;
import com.example.main.ChatListView.OnPullDownListener;
import com.example.main_Adapter.TalkAdapter;
import com.example.main_Entity.BandWEntity;
import com.example.main_Entity.TalkEntity;
import com.example.main_util.FuntionUtil;
import com.example.main_util.LogHelper;
import com.example.main_util.StringUtil;
import com.example.main_util.Tools;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.Service;
import android.app.AlertDialog.Builder;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.ClipboardManager;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;


public class MsgTalk extends ListActivity{

	private int time_out = 2000;
	private String TAG = "MsgTalk";
	private final static boolean log = true;
	
	private int ark_id;
	private Context context = MsgTalk.this;
    private Vibrator vibrator=null;
    private EditText et_sendmessage;
    private Button chatting_btn_send;
    private ImageButton title_bar_back,title_bar_gps;
    private String sendmessage;
    private Boolean isResume = false;
    private TextView title_bar_title;
    private RelativeLayout popupwindow_gps;
    private PopupWindow popupwindow;
	
    /*************Picture***************/
	private String[] items = new String[] {
			"选择本地图片" , "拍照"
	};
	
	private static final String IMAGE_FILE_NAME = "/faceImage.jpg";
	
	private static final int IMAGE_REQUEST_CODE = 0 ;
	private static final int CAMERA_REQUEST_CODE = 1 ;
	private static final int RESULT_REQUEST_CODE = 2 ;
	/**************Picture**************/
    
	InetAddress serverAddr;
	private ChatListView mListView;
	private TalkAdapter mAdapter;

	private String currentStart = "2013-01-01 00:00:00";
	private String currentEnd = "3000-12-31 23:59:59";
	private long exitTime = 0;
	private int currentLine = 5;
	private int updateLines = 0;
	
	private List<TalkEntity> TalkDataArrays = new ArrayList<TalkEntity>();
	
	private ImageButton chatting_picture_btn;
	
	ProgressDialog pd = null;
	private boolean work = false;

	Message msg = new Message();
	
	final Handler add_handler = new Handler(){
		@Override
		public void  handleMessage (Message msg){
			if(msg.what == 1){
				TalkDataArrays.add((TalkEntity)msg.obj);
			}
			else {
				TalkDataArrays.add(0,(TalkEntity)msg.obj);				
			}
		}
	};
	
	final Handler mhandler = new Handler(){
		@Override
		public void  handleMessage (Message msg){
			if(pd!=null)pd.dismiss();
			for(int i = TalkDataArrays.size()-1 ; i > -1 ; i-- ){
				if(TalkDataArrays.get(i).getMsg_id().equals("")
						&& TalkDataArrays.get(i).getState() == 1){
					TalkDataArrays.remove(i);
					break;
				}
			}
			mListView.requestLayout();
			mAdapter.notifyDataSetChanged();
			switch (msg.what){
			case 0:
				toast("查询错误");
				break;
			case 1:
				if(updateLines!=0){
					toast("该时段最近"+ updateLines +"条信息");
					break;
				}
			case 2:
				if(updateLines!=0){
					toast("刷新"+ updateLines +"条信息");
					break;
				}
			case 3:
				if(updateLines!=0){
					toast("加载"+ updateLines +"条历史信息");
					break;
				}
			case 4:
//				toast("没有找到信息");
				break;
			case 5:
				work = true;
				mAdapter.notifyDataSetChanged();
				refresh();
				break;
			case 6:
				((ChatListView) getListView()).onRefreshComplete();
				((ChatListView) getListView()).onMoreComplete();
				break;
				}
		}
	};
	
	final Handler Fail_Handler = new Handler(){
		@Override
		public void  handleMessage (final Message msg){
//			for(int i = TalkDataArrays.size()-1;i>0;i--){
//				if(TalkDataArrays.get(i).getDetail().equals(msg.obj))
//					msg.what = i;
//			}
			if(log)LogHelper.trace(String.valueOf(msg.what));
			AlertDialog.Builder builder = new Builder(MsgTalk.this);
			builder.setTitle("重发");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
//							Resend(msg.what);
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.setIcon(android.R.drawable.ic_dialog_info);
			builder.setMessage("确认重发该条消息");
			builder.create().show();
		}
	};
	final Handler ll_Handler = new Handler(){
		@Override
		public void  handleMessage (final Message msg){
//			for(int i = TalkDataArrays.size()-1;i>0;i--){
//				if(TalkDataArrays.get(i).getDetail().equals(msg.obj))
//					msg.arg1 = i;
//			}
			LogHelper.trace(String.valueOf(msg.arg1) + " " + String.valueOf(msg.arg2));
			final TalkEntity entity = TalkDataArrays.get(msg.arg1);
			if(msg.arg2 == StringUtil.LONG){
				if(entity.getState() == 2){
					AlertDialog.Builder builder = new Builder(MsgTalk.this);
					builder.setTitle("重发");
					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									if(log)Log.v(TAG,"msg.arg1: "+msg.arg1);
									Resend(entity);
								}
							});
					builder.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
					builder.setIcon(android.R.drawable.ic_dialog_info);
					builder.setMessage("确认重发该条消息");
					builder.create().show();
					return;
				}
			}
			if(msg.arg2 == StringUtil.FAIL){
				AlertDialog.Builder builder = new Builder(MsgTalk.this);
				builder.setTitle("重发");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								if(log)Log.v(TAG,"msg.arg1: "+msg.arg1);
								Resend(entity);
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
				builder.setIcon(android.R.drawable.ic_dialog_info);
				builder.setMessage("确认重发该条消息");
				builder.create().show();
				return;
			}
			if(entity.getIsMsg()){
				
				if(msg.arg2 == StringUtil.CLICK){
//					AlertDialog.Builder builder = new Builder(MsgTalk.this);
//					builder.setTitle("文字");
//					builder.setPositiveButton("复制",
//							new DialogInterface.OnClickListener() {
//								@Override
//								public void onClick(DialogInterface dialog, int which) {
//									GlobalID globalID = ((GlobalID)getApplication());
//							        ClipData textCd = ClipData.newPlainText("", TalkDataArrays.get(msg.arg1).getDetail());
//							        globalID.getClipboard().setPrimaryClip(textCd);
//									dialog.dismiss();
//									globalID.showCopyed(MsgTalk.this);
//									dialog.dismiss();
//								}
//							});
//					builder.create().show();
				}
				
				else if(msg.arg2 == StringUtil.LONG){

					AlertDialog.Builder builder = new Builder(MsgTalk.this);
//					builder.setTitle("文字");
					builder.setPositiveButton("复制",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									GlobalID globalID = (GlobalID)getApplication();
									String test = entity.getDetail();
									LogHelper.trace(test);
									// 判断API>=11
									if(Build.VERSION.SDK_INT >= 11){
								        ClipData textCd = ClipData.newPlainText("", test);
								        globalID.getClipboard().setPrimaryClip(textCd);
									}
									else {
										ClipboardManager clipboard ;
								        clipboard = (ClipboardManager)getSystemService(MsgTalk.CLIPBOARD_SERVICE);
								        clipboard.setText(test);
									}
									
									dialog.dismiss();
									globalID.showCopyed(MsgTalk.this);
									dialog.dismiss();
								}
							});
//					builder.setNegativeButton("删除",
//							new DialogInterface.OnClickListener() {
//								@Override
//								public void onClick(DialogInterface dialog, int which) {
//									dialog.dismiss();
//								}
//							});
					builder.create().show();
				}
			}
			
			if(!entity.getIsMsg() && msg.arg2 == StringUtil.CLICK){
				isResume = true;
				Bundle bundle = new Bundle();
				
//				//set picture
//				Bitmap Image_picture = entity.getImage_picture();
//				if(Image_picture==null){
//					bundle.putByteArray("Image_picture", null);
//					}
//				else{
//					ByteArrayOutputStream image_picture = new ByteArrayOutputStream();
//					Image_picture.compress(Bitmap.CompressFormat.PNG, 100, image_picture);
//					bundle.putByteArray("Image_picture", image_picture.toByteArray());
//					
//				}
				
				bundle.putInt("i", msg.arg1);
				GlobalID globalID = ((GlobalID)getApplication());
				globalID.setTalkList(TalkDataArrays);
				globalID.un_stop = true;
				Intent intent = new Intent(MsgTalk.this,ViewPagerActivity.class);
				//post data
				intent.putExtras(bundle);
	            startActivity(intent);
			}
		}
	};
	
	final Handler mhandler3 = new Handler(){
		@Override
		public void  handleMessage (Message msg){
			final GlobalID globalID = ((GlobalID)getApplication());
			chatting_btn_send.setClickable(true);
			switch (msg.what){
			case -3:
				if(log)Log.v("mhandler3", "case -3");
				if(pd!=null)pd.dismiss();
				new AlertDialog.Builder(context).
       		 	setTitle("错误").setMessage("发送失败，连接服务器失败").setPositiveButton("确定", new DialogInterface.OnClickListener() {
       			 @Override
				public void onClick(DialogInterface dialog, int which) {
       				 dialog.dismiss();
       				 }
       			 }).show();
				break;
			case -2:
				if(log)Log.v("mhandler3", "case -2");
				if(pd!=null)pd.dismiss();
				new AlertDialog.Builder(context).
       		 	setTitle("错误").setMessage("发送失败1").setPositiveButton("确定", new DialogInterface.OnClickListener() {
       			 @Override
				public void onClick(DialogInterface dialog, int which) {
       				 dialog.dismiss();
       				 }
       			 }).show();
				break;
			case -1:
				if(log)Log.v("mhandler3", "case -1");
//				if(pd!=null)pd.dismiss();
//				new AlertDialog.Builder(context).
//				setTitle("错误").setMessage("发送失败").setPositiveButton("确定", new DialogInterface.OnClickListener() {
//       			 @Override
//				public void onClick(DialogInterface dialog, int which) {
//       				 dialog.dismiss();
//       				 }
//       			 }).show();
				TalkDataArrays.get(msg.arg1).setState(2);
				break;
			case 0:
				if(log)Log.v("mhandler3", "case 0");
				if(pd!=null)pd.dismiss();
				new AlertDialog.Builder(context).  
				setTitle("成功").setMessage("成功发送图片信息").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						((Activity) context).finish();
						globalID.is_sended = true;
						}  
					}).show();
				break;
			case 1:
				if(log)Log.v("mhandler3", "case 1");
//				if(pd!=null)pd.dismiss();
//				new AlertDialog.Builder(context).  
//				setTitle("成功").setMessage("成功发送文字信息").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						((Activity) context).finish();
//						globalID.is_sended = true;
//						}  
//					}).show();
				TalkDataArrays.get(msg.arg1).setState(1);
				break;
			case 2:
				if(log)Log.v("mhandler3", "case 2");
				if(pd!=null)pd.dismiss();
				new AlertDialog.Builder(context).  
				setTitle("成功").setMessage("成功发送所有信息").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						((Activity) context).finish();
						globalID.is_sended = true;
						}  
					}).show();
				break;
			}
			if(log)LogHelper.trace(TAG,"here?");
			mListView.requestLayout();
			mAdapter.notifyDataSetChanged();
		}
	};
		@Override 
		public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		final GlobalID globalID = ((GlobalID)getApplication());
		overridePendingTransition(R.anim.item_in, R.anim.list_out);
		if(null != savedInstanceState){
	    	globalID.setAll(savedInstanceState.getString("All"));
	    	globalID.setNow_ark(savedInstanceState.getInt("Now_ark"));
			globalID.start(context);
			final String StrTest = savedInstanceState.getString("TalkDataArrays");
			ark_id = savedInstanceState.getInt("ark_id");
	    	if(log)Log.e(TAG, "null != savedInstanceState TalkDataArrays = "+ StrTest);
	    	globalID.start(context);
	    	Thread getImg = new Thread(){
				public void run(){
					getTalkDataArrays(StrTest);
					if(msg!=null)msg = new Message();
					msg.what = 5;
					mhandler.sendMessage(msg);
				}
			};
			getImg.start();
			}
		else{
			work = true;
			Intent intent = getIntent();
			Bundle data = intent.getExtras();
			try{
				ark_id = data.getInt("ark_num");
				globalID.setNow_ark(ark_id);		
			}catch(Exception e){
				if(log)Log.v(TAG,"Exception "+e);
			}
//			ark_id = Integer.valueOf(globalID.getAll().get(ark_id).getArk_id());
			if(log)Log.v(TAG,"ark_id onCreate " + ark_id);
			GetDataTask talk = new GetDataTask();
			talk.startTime = currentStart;
			talk.endTime = currentEnd;
			talk.line = currentLine;
			talk.Case = 1;
			talk.Insert_Back = false;
//			talk.setMsg_id = "!= -1";
			talk.DESC = "DESC";
			isResume = true;
			pd = ProgressDialog.show(MsgTalk.this, "连接", "正在获取数据…");
			talk.execute();
		}
		
		//set no title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chatting_main);
		
		vibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
		title_bar_title = (TextView)findViewById(R.id.talk_title);
//		title_bar_title.requestFocus();
		
		initView();
		et_sendmessage = (EditText)findViewById(R.id.et_sendmessage);
		
		WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		et_sendmessage.setWidth(display.getWidth());
		Log.v(TAG,"display.getWidth() = "+display.getWidth());
		et_sendmessage.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub

				//do something when press ENTER
				if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
					Log.v("onEditorAction","KeyEvent.KEYCODE_ENTER");
					
					SendMSG_Pre();					
					return true;
				}
				else return false;
			}
            });
		
		chatting_btn_send = (Button)findViewById(R.id.chatting_btn_send);
		chatting_btn_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				chatting_btn_send.setClickable(false);
				SendMSG_Pre();
				chatting_btn_send.setClickable(true);
			}
		});
		
		chatting_picture_btn = (ImageButton)findViewById(R.id.chatting_picture_btn);
		chatting_picture_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				chatting_picture_btn.setClickable(false);
				globalID.un_stop = true;
//				showDialog();
			}
		});
		
		title_bar_back = (ImageButton)findViewById(R.id.title_bar_back);
		title_bar_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MsgTalk.this.finish();
			}
		});
		
		title_bar_gps = (ImageButton)findViewById(R.id.title_bar_gps);	
		title_bar_gps.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				globalID.un_stop = true;
//			Intent intent = new Intent(MsgTalk.this,GeoCoderDemo.class);	
				getPopupWindow();
				// 这里是位置显示方式,在屏幕的左侧
//				popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, 10, -50);
				
				ColorDrawable cd = new ColorDrawable(-0000);
				popupwindow.setBackgroundDrawable(cd);
				
				popupwindow.showAsDropDown(v, 0, 1);
			}
		});
		
		currentStart = globalID.getStartDate();
		currentEnd = globalID.getEndDate();
		currentLine = globalID.getLine();
		
//		title_bar_title.requestFocus();
		
	}

		@Override
		protected void onResume(){
			super.onResume();
			if(log)Log.v(TAG,"onResume");
//			title_bar_title.requestFocus();
			GlobalID globalID = ((GlobalID)getApplication());
			globalID.cancel_notification();
			globalID.un_stop = false;
			if(!isResume && work)refresh();
			return;
		}

		private void initView() {
			// TODO Auto-generated method stub
			mListView = (ChatListView)findViewById(android.R.id.list);
			
			mListView.setBackgroundResource(R.drawable.text_background);

			final GlobalID globalID = ((GlobalID)getApplication());
			mAdapter = new TalkAdapter(context,TalkDataArrays,Fail_Handler,ll_Handler,globalID.getID());
			mListView.setAdapter(mAdapter);
			
			//设置可以自动获取更多 滑到最后一个自动获取  改成false将禁用自动获取更多
			mListView.enableAutoFetchMore(false, 1);
			mListView.mFooterTextView.setText("点击刷新");
			mListView.setOnPullDownListener(new OnPullDownListener(){
				/**更多事件接口  这里要注意的是获取更多完 要关闭 更多的进度条 notifyDidMore()**/

				@Override
				public void onRefresh() {
					if((System.currentTimeMillis()-exitTime) < time_out){
						Thread sleep_thread = new Thread(){
							public void run(){
								try {
									sleep(time_out);
									if(msg!=null)msg = new Message();
									msg.what = 6;
									mhandler.sendMessage(msg);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						};
						sleep_thread.start();
		            }
					else{
						GetDataTask talk = new GetDataTask();
						if(TalkDataArrays.size() != 0){
							talk.endTime = TalkDataArrays.get(0).getPostTime();
//							talk.setMsg_id = "< " + TalkDataArrays.get(0).getMsg_id();							
						}
						else {
							talk.endTime = currentStart;
//							talk.setMsg_id = "!= -1";
						}
						talk.startTime = "0-0-0";
						talk.line = currentLine;
						talk.Case = 3;
						talk.Insert_Back = false;
						talk.DESC = "DESC";
						exitTime = System.currentTimeMillis();
						talk.execute();
					}
				}

				@Override
				public void onMore() {
					if((System.currentTimeMillis()-exitTime) < time_out){
						Thread sleep_thread = new Thread(){
							public void run(){
								try {
									sleep(time_out);
									if(msg!=null)msg = new Message();
									msg.what = 6;
									mhandler.sendMessage(msg);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						};
						sleep_thread.start();
		            }
					else{
						refresh();
					}
				}
//				}
			});
		}
		
		public void refresh(){
			GetDataTask talk = new GetDataTask();
			if(TalkDataArrays.size() != 0){
				talk.startTime = TalkDataArrays.get(TalkDataArrays.size()-1).getPostTime();
//				talk.setMsg_id = "> " + TalkDataArrays.get(TalkDataArrays.size()-1).getMsg_id();
			}
			else {
				Calendar sDate = Calendar.getInstance();
				sDate.add(Calendar.DATE, -1);
				talk.startTime = sDate.get(Calendar.YEAR)+"-"+(sDate.get(Calendar.MONTH)+1)+"-"+sDate.get(Calendar.DATE)
						+ " " + sDate.get(Calendar.HOUR)+":"+sDate.get(Calendar.MINUTE)+":"+sDate.get(Calendar.SECOND);
//				talk.setMsg_id = "!= -1";
			}
			Calendar eDate = Calendar.getInstance();
			talk.endTime = eDate.get(Calendar.YEAR)+"-"+(eDate.get(Calendar.MONTH)+1)+"-"+eDate.get(Calendar.DATE)
					+ " " + eDate.get(Calendar.HOUR)+":"+eDate.get(Calendar.MINUTE)+":"+eDate.get(Calendar.SECOND);
			talk.line = currentLine ;
			talk.Case = 2;
			talk.Insert_Back = true;
			talk.DESC = "ASC";
            exitTime = System.currentTimeMillis();
			talk.execute();
		}
		
		
		private class GetDataTask extends AsyncTask<Void , Void , Void>{

			public String startTime;
			public String endTime;
			public String user_id;
			public int line;
			public int Case;
			public boolean Insert_Back;
//			public String setMsg_id;
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
					user_id = globalID.getID();
					
					Socket socket = new Socket();
					try {
						FuntionUtil.doSth();
						
						InetSocketAddress socketAdd = new InetSocketAddress(serverAddr, SERVERPORT);
						socket.connect(socketAdd, globalID.getTIMEOUT());
				        try {
				            PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
				            out.println(16);
				            out.println(startTime);
				            out.println(endTime);
				            out.println(user_id);
				            out.println(line);
				            out.println(DESC);
				            out.println(ark_id);

							String sql = "select TOP "+line+" aa.* from ( SELECT 0 as msg_type, " +
									"a.msg_id as id, a.user_id, a.ark_id, a.type, a.grade, a.datetime_send, " +
									"a.datetime_recv, a.msg, b.id as senderId , " +
									"b.Real_Name as senderName,c.ark_id as recvArkId,c.user_id as recvUid " +
									"FROM tbl_msg a, tbl_Users b, tbl_msg_to_user c " +
									"WHERE " +
//									" a.type=1 AND a.isSend=0 AND " +
									"a.user_id=b.UserId AND a.msg_id=c.msg_id " +
									"UNION ALL SELECT  1 as msg_type, a.img_id as id, a.user_id, a.ark_id, " +
									"a.type, a.grade, a.datetime_send, a.datetime_recv, a.img_name as msg, " +
									"b.id as senderId,b.Real_Name as senderName,c.ark_id as recvArkId,c.user_id as recvUid " +
									"FROM tbl_img a, tbl_Users b, tbl_img_to_user c " +
									"WHERE " +
//									"a.type=1 AND a.isSend=0 AND " +
									"a.user_id=b.UserId AND a.img_id=c.img_id) aa " +
									"where Convert(varchar(100),datetime_send,120)>='"+startTime+"'"+
									" and Convert(varchar(100),datetime_send,120)<='"+endTime+"'"+
									//还需要用user_id吗？只是群聊的时候
									" and (aa.recvArkId = '"+ark_id+"' or aa.ark_id = '"+ark_id+"')"+
									"ORDER BY aa.datetime_send "+DESC+", aa.msg_type, aa.id, aa.ark_id";
				            
				            if(log)Log.v(TAG,"sql: "+sql);
				            
				        } catch(Exception e) {
				                if(log)Log.e("err", "S: Error", e);
				        }
				        finally {
				             socket.setSoTimeout( 2 * globalID.getTIMEOUT());
				        	 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GB2312"));
				        	 String line = reader.readLine();
				        	 if(parseJson(line,Insert_Back)){
				        		 if(msg != null) msg = new Message();
				                 msg.what = Case;
				                 mhandler.sendMessage(msg);
				        	 }
				        	 else{
				        		 if(msg != null) msg = new Message();
				                 msg.what = 4;
				                 mhandler.sendMessage(msg);
				        	 }
				        }
					} catch (IOException e1) {
						if(msg != null) msg = new Message();
			            msg.what = 0;
			            mhandler.sendMessage(msg);
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}  
				} catch (UnknownHostException e) {
					if(msg != null) msg = new Message();
		            msg.what = 0;
		            mhandler.sendMessage(msg);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result){
				if(log)Log.v(TAG,"get date finish");
				((ChatListView) getListView()).onRefreshComplete();
				((ChatListView) getListView()).onMoreComplete();
				if(Insert_Back)mListView.setSelection(TalkDataArrays.size());
				else {
					int select = TalkDataArrays.size() < updateLines ? 
							TalkDataArrays.size() : updateLines;
					mListView.setSelection(select);
				}
				if(isResume){
					mListView.setSelection(TalkDataArrays.size());
					isResume = false;
				}
				super.onPostExecute(result);
			}
			
		}
		
	    /***********Json******************/
	    private boolean parseJson(String resultObj,boolean Insert_Back){
	    	boolean insert = false;
	    	updateLines = 0;
	    	final GlobalID globalID = ((GlobalID)getApplication());
	    	if(log)Log.v("resultObj: ",resultObj);
			try{
				JSONArray jsonArray = new JSONArray(resultObj);
				if(jsonArray.length()==0){
					return false;
					}
				else{
						for(int i = jsonArray.length()-1 ; i > -1  ; i--){
							insert = false;
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							
							TalkEntity entity = new TalkEntity(
									//表示船消息类型，0表示文字，1表示图片
									jsonObject.optString("msg_type").equals("0")
									//表示船消息方向，0表示船往岸发数据，1表示岸往船发数据
									, jsonObject.optString("type").equals("1")
									
									//从html转换为string
									, Html.fromHtml(jsonObject.optString("msg")).toString()
									, jsonObject.optString("datetime_send")
									, jsonObject.optString("ark_id")
									, jsonObject.optString("user_id")
									, jsonObject.optString("msg_id"));
							if(!entity.getIsMsg()){
								String pic = entity.getDetail();
								if(log)Log.v("pic: " , pic);
								Bitmap bit = FuntionUtil.downloadPic("http://"+ globalID.getDBurl() +"/user/images" + pic);
								if(bit!=null){
									entity.setImage_picture(bit);
			  					}
								else{
		 	  		 				Bitmap good  = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
									entity.setImage_picture(good);
		 	  						}
							}
							
							for(int j = TalkDataArrays.size()-1 ; j>-1 ; j--){
								if(entity.getMsg_id().equals(TalkDataArrays.get(j).getMsg_id()) && 
										entity.getIsMsg() == TalkDataArrays.get(j).getIsMsg()){
									insert = true;
									break;
								}
							}
							if(insert)continue;
							if(!Insert_Back){
								msg = new Message();
								msg.what = 0;
								msg.obj = entity;
								add_handler.sendMessage(msg);
//								TalkDataArrays.add(0,entity);
							}
							else {
								msg = new Message();
								msg.what = 1;
								msg.obj = entity;
								add_handler.sendMessage(msg);
//								TalkDataArrays.add(entity);
							}
							
							updateLines++;
					}
				}
				
			}catch(JSONException e){
				e.printStackTrace();
			}
			return true;
		}
	    /***********Json******************/
	    
	    void toast(String msg){
			GlobalID globalID = ((GlobalID)getApplication());
			if(pd!=null)pd.dismiss();
			if(globalID.toast != null)globalID.toast.cancel();
			globalID.toast = Toast.makeText(MsgTalk.this, msg, Toast.LENGTH_LONG);
			globalID.toast.show();
	    }
	    
	    @Override 
	    public void onSaveInstanceState(Bundle savedInstanceState) {  
	    	// Save away the original text, so we still have it if the activity   
	    	// needs to be killed while paused.
	    	savedInstanceState.putString("TalkDataArrays", Talk2String());
	    	savedInstanceState.putInt("ark_id", ark_id);
	    	super.onSaveInstanceState(savedInstanceState);  
	    	if(log)Log.e(TAG, "onSaveInstanceState");

	    	GlobalID globalID = ((GlobalID)getApplication());
	    	savedInstanceState.putString("All", globalID.Ark_line2String());
	    	savedInstanceState.putInt("Now_ark", globalID.getNow_ark());
	    	if(globalID.un_stop)return;
	    	else{
		    	globalID.create_notification("后台接收数据", "后台运行", "岸客户端", false, false, false,MsgTalk.class.getName());
		    	if(globalID.toast != null)globalID.toast.cancel();
		    	}
	    	}
	    
	    @Override
	    public void onPause(){
	    	super.onPause();
	    	if(log)Log.e(TAG, "onPause");
	    }
	    
	    private String Talk2String() {
			// TODO Auto-generated method stub
	    	TalkEntity entity = new TalkEntity();
	    	String encode = "[";
			for(int i = 0 ; i < TalkDataArrays.size();){
				entity = TalkDataArrays.get(i);
				encode += "{";

				encode += "\"isMsg\":\"" + entity.getIsMsg() + "\";";
				encode += "\"isSend\":\"" + entity.getIsSend() + "\";";
				encode += "\"detail\":\"" + entity.getDetail() + "\";";
				encode += "\"PostTime\":\"" + entity.getPostTime() + "\";";
				encode += "\"ark_id\":\"" + entity.getArk_id() + "\";";
				encode += "\"send_user_id\":\"" + entity.getSend_user_id() + "\";";
				encode += "\"msg_id\":\"" + entity.getMsg_id() + "\";";
				encode += "\"state\":\"" + entity.getState() + "\"";
				
				encode += "}";
				if(++i < TalkDataArrays.size()){
					encode += ",";
				}
			}
			encode += "]";
			if(log)LogHelper.trace(TAG, encode);
			return encode;
		}
	    
	    @Override  
	    public void onRestoreInstanceState(Bundle savedInstanceState) {  
	    	super.onRestoreInstanceState(savedInstanceState);
//	    	final String StrTest = savedInstanceState.getString("TalkDataArrays");
//	    	if(log)Log.e(TAG, "onRestoreInstanceState + TalkDataArrays = "+ StrTest);
//	    	Thread getImg = new Thread(){
//				public void run(){
//					getTalkDataArrays(StrTest);
//					if(msg!=null)msg = new Message();
//					msg.what = 5;
//					mhandler.sendMessage(msg);
//				}
//			};
//			getImg.start();
	    	}  
	    
	    private void getTalkDataArrays(String decode) {
			// TODO Auto-generated method stub
			try{
				JSONArray jsonArray = new JSONArray(decode);
				if(jsonArray.length()==0){
					return;
					}
				else{
					for(int i = 0 ; i < jsonArray.length() ; i++){
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						
						TalkEntity entity = new TalkEntity(Boolean.valueOf(jsonObject.optString("isMsg"))
								, Boolean.valueOf(jsonObject.optString("isSend"))
								
								, Html.fromHtml(jsonObject.optString("detail")).toString()
								, jsonObject.optString("PostTime")
								, jsonObject.optString("ark_id")
								, jsonObject.optString("send_user_id")
								, jsonObject.optString("msg_id"));
						
						entity.setState(Integer.valueOf(jsonObject.optString("state")));

						LogHelper.trace(jsonObject.optString("detail"));
						LogHelper.trace(Html.fromHtml(jsonObject.optString("detail")).toString());
						
						if(jsonObject.optString("isMsg").equals("false")){
							String pic = jsonObject.optString("detail");
							GlobalID globalID = ((GlobalID)getApplication());
							Bitmap bit = FuntionUtil.downloadPic("http://"+ globalID.getDBurl() +"/user/images" + pic);
							if(bit!=null){
								entity.setImage_picture(bit);
		  					}
							else{
	 	  		 				Bitmap default_Img  = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
								entity.setImage_picture(default_Img);
	 	  					}
						}
						
//						TalkDataArrays.add(entity);
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
	    
		/*********************Picture*********************************/
		private void showDialog(){
			chatting_picture_btn.setClickable(true);
			new AlertDialog.Builder(this).setTitle("选择图片").setItems(items, new
					DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							GlobalID globalID = (GlobalID)getApplication();
							globalID.un_stop = true;
							switch(which){
							case 0 :
								//跳转相册
								Intent intentFromGallery = new Intent();
								intentFromGallery.setType("image/*");
								//catch nothing
								if(intentFromGallery.setAction(Intent.ACTION_GET_CONTENT)==null)break;
								
								startActivityForResult(intentFromGallery , IMAGE_REQUEST_CODE );
								break;
							case 1:
								
								//跳转摄像机
								Intent intentFromCapture = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								if(Tools.hasSdcard()){
									intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT
											,Uri.fromFile(new File(Environment.getExternalStorageDirectory()
													,IMAGE_FILE_NAME)) );
								}
								startActivityForResult(intentFromCapture , CAMERA_REQUEST_CODE);
								break;
							}
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					}).show();
		}		

		@Override
		protected void onActivityResult(int requestCode , int resultCode , Intent data){
			if(resultCode != RESULT_CANCELED){
			switch (requestCode){
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				if(Tools.hasSdcard()){
					File tempFile = new File(
							Environment.getExternalStorageDirectory() + IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
//					startPhotoZoom(Uri.fromFile(new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME)));
				}else{
					Toast.makeText(context, "未找到存储卡，无法存储照片", Toast.LENGTH_LONG).show();
				}
				break;
			case RESULT_REQUEST_CODE:
				if(data != null){
					getImageToView(data);
				}
				break;
			}
		}
			super.onActivityResult(requestCode, resultCode, data);
		}
		
		public void startPhotoZoom(Uri uri){
			GlobalID globalID = (GlobalID)getApplication();
			globalID.un_stop = true;
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			
			intent.putExtra("crop", true);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			
			intent.putExtra("outputX", 90);
			intent.putExtra("outputY", 90);
			intent.putExtra("return-data", true);
			intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			intent.putExtra("noFaceDetection", true); 
			startActivityForResult(intent , RESULT_REQUEST_CODE);
		}
	
		private void getImageToView(Intent data){
			Bundle extras = data.getExtras();
			GlobalID globalID = ((GlobalID)getApplication());
			globalID.un_stop = true;
			if(extras != null ){
				Bitmap photo = extras.getParcelable("data");
//				@SuppressWarnings("deprecation")
//				Drawable drawable = new BitmapDrawable(photo);
				
				TalkEntity entity = new TalkEntity(false
						, true
						, ""
						, StringUtil.YYMMDD.format(Calendar.getInstance().getTime())
						, String.valueOf(ark_id)
						, globalID.getID()
						, "");
				entity.setState(0);
				entity.setImage_picture(photo);
				TalkDataArrays.add(entity);
				mListView.requestLayout();
				mAdapter.notifyDataSetChanged();
				SendIMG(TalkDataArrays.size()-1,photo);
//				iv_send_HeadPhoto.setImageDrawable(drawable);
//				if(log)Log.v(TAG, "has picture");
//				no_picture = false;
			}
		}
		/*********************Picture*********************************/

		private void SendMSG_Pre(){
			sendmessage = et_sendmessage.getText().toString();
			if(sendmessage.length() == 0){
				chatting_btn_send.setClickable(true);
				return;
			}
			else{
				GlobalID globalID = ((GlobalID)getApplication());				
				TalkEntity entity = new TalkEntity(true
						, true
						, sendmessage
						, StringUtil.YYMMDD.format(Calendar.getInstance().getTime())
						, String.valueOf(ark_id)
						, globalID.getID()
						, "");
				if(log)LogHelper.trace(TAG,"  "+ entity.getPostTime());
				entity.setState(0);
				TalkDataArrays.add(entity);
				mListView.requestLayout();
				mAdapter.notifyDataSetChanged();
				mListView.setSelection(TalkDataArrays.size());
				et_sendmessage.setText("");
				
				//转换成html格式
				SendMSG(TalkDataArrays.size()-1,FuntionUtil.toHTMLString(entity.getDetail()));
			}
		}

		private void SendMSG(final int position,final String sendmsg) {
			// TODO Auto-generated method stub
			final String level = "41";//发送等级 41表示正常
//			pd = ProgressDialog.show(MsgTalk.this, "连接", "正在连接服务器…");
			
			Thread send_thread = new Thread(){
				@Override
				public void run(){
					if(msg != null) msg = new Message();
					msg.what = 0;
					GlobalID globalID = ((GlobalID)getApplication());

					FuntionUtil.doSth();
					
					String url = globalID.getUrlServer();
					InetAddress serverAddr = null;
					
					try {
						serverAddr = InetAddress.getByName(url);
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						SthWrong(position);
						return;
					}
					
					int SERVERPORT = globalID.getSERVERPORT();
//					Socket socket=new Socket(serverAddr,SERVERPORT);
					Socket socket = new Socket();
					InetSocketAddress socketAdd = new InetSocketAddress(serverAddr, SERVERPORT);
					
					try {
						socket.connect(socketAdd, globalID.getTIMEOUT());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						SthWrong(position);
						return;
					}
					PrintWriter out = null;
					try {
						out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						SthWrong(position);
						return;
					}
					
					out.println(11);
	            	out.println(globalID.getID());
	            	out.println(ark_id);
	            	out.println(level);
	            	out.println(sendmsg);
	            	out.println("");
	            	
	            	BufferedReader reader = null;
	            	try {
						reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GB2312"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						SthWrong(position);
						return;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						SthWrong(position);
						return;
					}
	            	
	            	String line = null;
					try {
						line = reader.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						SthWrong(position);
						return;
					}
		            if(log)Log.v("id_checked",line);
		            
		            if(line.equals("1")){
	            		if(msg != null) msg = new Message();
//	        				 if(globalID.isShake()){
//	        					 if(log)Log.v("login","shake");
//	        					 vibrator.vibrate(new long[]{1000,50,50,100,50}, -1);
//	        					 }
			            	msg.what = 1;
			            	msg.arg1 = position;
		            		mhandler3.sendMessage(msg);
		            	}
		        	 else{
		        		 msg.what = -1;
			             msg.arg1 = position;
		        		 mhandler3.sendMessage(msg);
		        	 }
		            
		            if(socket!=null)
						try {
							socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            	
				}
			};
			send_thread.start();
		}
		
		private void SendIMG(final int position,final Bitmap img) {
			// TODO Auto-generated method stub
			final String level = "3";//发送等级 3表示正常
			
			Thread send_thread = new Thread(){
				@Override
				public void run(){
					if(msg != null) msg = new Message();
					msg.what = 0;
					
					ByteArrayOutputStream change = new ByteArrayOutputStream();
					//compress
					Bitmap image = comp(img);
					image.compress(Bitmap.CompressFormat.JPEG,90,change);
					
					final byte[] send_p = change.toByteArray();

					final byte[] length = new byte[4];
					length[0] = (byte)(send_p.length);
					length[1] = (byte)(send_p.length >> 8);
					length[2] = (byte)(send_p.length >> 16);
					length[3] = (byte)(send_p.length >> 24);
					
					GlobalID globalID = ((GlobalID)getApplication());

					FuntionUtil.doSth();
					
					String url = globalID.getUrlServer();
					InetAddress serverAddr = null;
					
					try {
						serverAddr = InetAddress.getByName(url);
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						SthWrong(position);
						return;
					}
					
					int SERVERPORT = globalID.getSERVERPORT();
//					Socket socket=new Socket(serverAddr,SERVERPORT);
					Socket socket = new Socket();
					InetSocketAddress socketAdd = new InetSocketAddress(serverAddr, SERVERPORT);
					
					try {
						socket.connect(socketAdd, globalID.getTIMEOUT());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						SthWrong(position);
						return;
					}
					PrintWriter out = null;
					try {
						out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						SthWrong(position);
						return;
					}
					
		            out.println(12);
	            	out.println(globalID.getID());
	            	out.println(ark_id);
	            	out.println(level);
	            	out.println("");
	            	
	            	socket = new Socket();
					try {
						socket.connect(socketAdd, globalID.getTIMEOUT());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						SthWrong(position);
						return;
					}
					
		            try {
						socket.setSoTimeout(globalID.getTIMEOUT());
					} catch (SocketException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						SthWrong(position);
						return;
					}
		            
		            OutputStream os = null;
		            
					try {
						os = socket.getOutputStream();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						SthWrong(position);
						return;
					}
					
		            byte[] code_p = new byte[send_p.length+4];
		            System.arraycopy(length, 0, code_p, 0, 4);
		            System.arraycopy(send_p, 0, code_p, 4 , send_p.length);
		            
		            try {
						os.write(code_p);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						SthWrong(position);
						return;
					}
	            	
	            	BufferedReader reader = null;
	            	try {
						reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GB2312"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						SthWrong(position);
						return;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						SthWrong(position);
						return;
					}
	            	
	            	String line = null;
					try {
						line = reader.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						SthWrong(position);
						return;
					}
		            if(log)Log.v("id_checked",line);
		            
		            if(line.equals("1")){
	            		if(msg != null) msg = new Message();
//	        				 if(globalID.isShake()){
//	        					 if(log)Log.v("login","shake");
//	        					 vibrator.vibrate(new long[]{1000,50,50,100,50}, -1);
//	        					 }
			            	msg.what = 1;
			            	msg.arg1 = position;
		            		mhandler3.sendMessage(msg);
		            	}
		        	 else{
		        		 msg.what = -1;
			             msg.arg1 = position;
		        		 mhandler3.sendMessage(msg);
		        	 }
		            
		            if(socket!=null)
						try {
							socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }
			};
			send_thread.start();			
		}		

		private Bitmap comp(Bitmap image) {
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();		
			image.compress(Bitmap.CompressFormat.JPEG, 90, baos);
			if( baos.toByteArray().length / 1024>100) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出	
				baos.reset();//重置baos即清空baos
				image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
			}
			ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			//开始读入图片，此时把options.inJustDecodeBounds 设回true了
			newOpts.inJustDecodeBounds = true;
			Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
			newOpts.inJustDecodeBounds = false;
			int w = newOpts.outWidth;
			int h = newOpts.outHeight;
			//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
			float hh = 800f;//这里设置高度为800f
			float ww = 480f;//这里设置宽度为480f
			//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
			int be = 1;//be=1表示不缩放
//			if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
//				be = (int) (newOpts.outWidth / ww);
//			} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
//				be = (int) (newOpts.outHeight / hh);
//			}
			be = (int) ((w / ww + h/ hh) / 2);
			if (be <= 0)
				be = 1;
			newOpts.inSampleSize = be;//设置缩放比例
			newOpts.inPreferredConfig = Config.RGB_565;
			//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
			isBm = new ByteArrayInputStream(baos.toByteArray());
			bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
			return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
		}
		
		private Bitmap compressImage(Bitmap image) {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			int options = 100;
			while ( baos.toByteArray().length / 1024>100) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩		
				options -= 10;//每次都减少10
				baos.reset();//重置baos即清空baos
				image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			}
			ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
			Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
			return bitmap;
		}
		
//		private void Resend(int position){
//			isResume = true;
//			TalkDataArrays.get(position).setState(0);
//			mAdapter.notifyDataSetChanged();
//			if(TalkDataArrays.get(position).getIsMsg()){
//				SendMSG(position, TalkDataArrays.get(position).getDetail());
//			}
//			else{
//				SendIMG(position, TalkDataArrays.get(position).getImage_picture());
//			}
//		}

		private void Resend(TalkEntity entity){
			isResume = true;
			TalkEntity new_entity = entity;
			
			TalkDataArrays.remove(entity);
			new_entity.setState(0);
			TalkDataArrays.add(new_entity);
			int position = TalkDataArrays.size()-1;
			if(log)Log.v(TAG,"position: "+position);
			if(position < 0 || position >= TalkDataArrays.size())return;
			mListView.requestLayout();
			mAdapter.notifyDataSetChanged();
			if(new_entity.getIsMsg()){
				SendMSG(position, FuntionUtil.toHTMLString(new_entity.getDetail()));
			}
			else{
				SendIMG(position, entity.getImage_picture());
			}
		}
		
		private void SthWrong(int position){
    		if(msg != null) msg = new Message();
			msg.what = -1;
			msg.arg1 = position;
			mhandler3.sendMessage(msg);
		}
		
		@Override
		 public void finish(){
			 super.finish();
			 if(log)Log.v(TAG,"finish");
			 overridePendingTransition(R.anim.list_in, R.anim.item_out);
//			 GlobalID globalID = ((GlobalID)getApplication());
//			 globalID.un_stop = true;
		 }
		
		private void getPopupWindow() {
			if(null != popupwindow) {
				popupwindow.dismiss();
			}
			else 
				initPopopWindow();
		}

		private void initPopopWindow() {
			// TODO Auto-generated method stub
			View popupWindow_view = getLayoutInflater().inflate(R.layout.popupwindow, null, false);
			
			popupwindow = new PopupWindow(popupWindow_view, FuntionUtil.dip2px(context, 250), LayoutParams.MATCH_PARENT, true);
			popupwindow.setAnimationStyle(R.style.AppTheme);
			popupwindow.setOutsideTouchable(true);
			
			popupwindow_gps = (RelativeLayout)popupWindow_view.findViewById(R.id.popupwindow_gps);
			popupwindow_gps.setOnClickListener(new OnClickListener() {
				GlobalID globalID = (GlobalID)getApplication();
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					globalID.un_stop = true;
					Intent intent = new Intent(MsgTalk.this,GeoCoderDemo.class);
		            startActivity(intent);
		            if(null != popupwindow && popupwindow.isShowing()) {
		            	popupwindow.dismiss();
		            	popupwindow = null;
		            }
				}
			});
		}


}

