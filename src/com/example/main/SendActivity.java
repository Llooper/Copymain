package com.example.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import com.example.main_util.FuntionUtil;
import com.example.main_util.Tools;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class SendActivity extends ContactActivity{
	private String TAG = "Send";
	private Context context = SendActivity.this;
    private Vibrator vibrator=null;
	
	//define spinner for kind
//	private Spinner 
////	sp_send_Kind,
//	sp_send_ark_id,sp_send_level;
	
	//define other data
//	private static TextView tv_send_Title;
	private ImageView iv_send_HeadPhoto;
	private EditText et_send_Id , et_send_Detail;
	private static Button btn_send_search,btn_send_delete,send_btn;
	private static TextView send_title;
	private static ImageButton send_title_bar_back;
	
//	private static tryscroll sv_send_sv;
//	private static InnerScrollView sv_send_Detail;
	
	/*************Picture***************/
	private String[] items = new String[] {
			"选择本地图片" , "拍照"
	};
	
	private static final String IMAGE_FILE_NAME = "/faceImage.jpg";
	
	private static final int IMAGE_REQUEST_CODE = 0 ;
	private static final int CAMERA_REQUEST_CODE = 1 ;
	private static final int RESULT_REQUEST_CODE = 2 ;
	/**************Picture**************/
	
	
//	ProgressDialog pd = null;

	@SuppressWarnings("unused")
	private List<String> list;
	
	String[] Send_level = {"特急"
			,"加急"
			,"尽快"
			,"正常"
			,"可缓"
			};
	
	private String ark_id = "";
	private int ark_num = 0;

	Message msg = new Message();
	@SuppressLint("HandlerLeak")
	final Handler mhandler = new Handler(){
		@Override
		public void  handleMessage (Message msg){
			 final GlobalID globalID = ((GlobalID)getApplication());
			send_btn.setClickable(true);
			switch (msg.what){
			case -3:
				Log.v("mhandler", "case -3");
				globalID.cancelPD();
				new AlertDialog.Builder(SendActivity.this).
       		 	setTitle("错误").setMessage("发送失败，连接服务器失败").setPositiveButton("确定", new DialogInterface.OnClickListener() {
       			 @Override
				public void onClick(DialogInterface dialog, int which) {
       				 dialog.dismiss();
       				 }
       			 }).show();
				break;
			case -2:
				Log.v("mhandler", "case -2");
				globalID.cancelPD();
				new AlertDialog.Builder(SendActivity.this).
       		 	setTitle("错误").setMessage("发送失败1").setPositiveButton("确定", new DialogInterface.OnClickListener() {
       			 @Override
				public void onClick(DialogInterface dialog, int which) {
       				 dialog.dismiss();
       				 }
       			 }).show();
				break;
			case -1:
				Log.v("mhandler", "case -1");
				globalID.cancelPD();
				new AlertDialog.Builder(SendActivity.this).
				setTitle("错误").setMessage("发送失败").setPositiveButton("确定", new DialogInterface.OnClickListener() {
       			 @Override
				public void onClick(DialogInterface dialog, int which) {
       				 dialog.dismiss();
       				 }
       			 }).show();
				break;
			case 0:
				Log.v("mhandler", "case 0");
				globalID.cancelPD();
				new AlertDialog.Builder(SendActivity.this).  
				setTitle("成功").setMessage("成功发送图片信息").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						SendActivity.this.finish();
						globalID.is_sended = true;
						}  
					}).show();
				break;
			case 1:
				Log.v("mhandler", "case 1");
				globalID.cancelPD();
				new AlertDialog.Builder(SendActivity.this).  
				setTitle("成功").setMessage("成功发送文字信息").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						SendActivity.this.finish();
						globalID.is_sended = true;
						}  
					}).show();
				break;
			case 2:
				Log.v("mhandler", "case 2");
				globalID.cancelPD();
				new AlertDialog.Builder(SendActivity.this).  
				setTitle("成功").setMessage("成功发送所有信息").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						SendActivity.this.finish();
						globalID.is_sended = true;
						}  
					}).show();
				break;
			case 3:
				try{
					globalID.PD(context, "连接", "正在努力发送…");					
				}catch(Exception e){
					if(log)Log.e(TAG,"pd Exception: "+ e);
				}
				break;
			}
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final GlobalID globalID = ((GlobalID)getApplication());
		if(null != savedInstanceState){
	    	globalID.setBus_change(savedInstanceState.getBoolean("Bus_change")); 
	    	globalID.setMsg_change(savedInstanceState.getBoolean("Msg_change")); 
	    	globalID.setImg_change(savedInstanceState.getBoolean("Img_change"));

			String decode = savedInstanceState.getString("All"); 
			Log.e(TAG, "onCreate get the savedInstanceState + All = "+decode);
			globalID.setAll(decode);
			ark_num = savedInstanceState.getInt("ark_num");
			globalID.start(context);
			}
		else{
			Intent intent = getIntent();
			Bundle data = intent.getExtras();
			try{
				ark_num = data.getInt("ark_num");			
			}catch(Exception e){
				Log.v("send","Exception "+e);
			}
			Log.v("send","ark_id onCreate " + ark_id);
		}
//		Cursor c = getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,null); 
//
//		startManagingCursor(c);   
//
//		ListAdapter adapter = new SimpleCursorAdapter(this,
//		                            android.R.layout.simple_list_item_2,c,
//		                            new String[]{Phone.DISPLAY_NAME,Phone.NUMBER},  
//		                            new int[]{android.R.id.text1,android.R.id.text2});


		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
		//set no title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.send);
		
		vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
		
		//set all items
//		sp_send_Kind = (Spinner)findViewById(R.id.sp_send_Kind);
//		sp_send_ark_id = (Spinner)findViewById(R.id.sp_send_ark_id);
//		sp_send_level = (Spinner)findViewById(R.id.sp_send_level);
		setTitle();
		
		ark_id = globalID.getAll().get(ark_num).getArk_id();
		
		iv_send_HeadPhoto = (ImageView)findViewById(R.id.iv_send_HeadPhoto);
		et_send_Id = (EditText)findViewById(R.id.et_send_Id);
		et_send_Detail = (EditText)findViewById(R.id.et_send_Detail);
		btn_send_search = (Button)findViewById(R.id.btn_send_search);
		btn_send_delete = (Button)findViewById(R.id.btn_send_delete);
		
//		sv_send_sv = (tryscroll)findViewById(R.id.sv_send_sv);
		
		edtSelectedContact = this.et_send_Id;
		imvSelectedContact = this.iv_send_HeadPhoto;
		
		WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

		et_send_Id.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			@Override
			public void afterTextChanged(Editable s) {
				if(s.length() > 0){
					btn_send_delete.setVisibility(View.VISIBLE);
				}else{
					btn_send_delete.setVisibility(View.GONE);
				}
			}
		});
		
		btn_send_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				et_send_Id.setText("");
			}
		});
		
		Display display = windowManager.getDefaultDisplay();

		et_send_Detail.setWidth(display.getWidth());
		Log.v("send","display.getWidth() = "+display.getWidth());
		et_send_Detail.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub

				//do something when press ENTER
				if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
					Log.v("onEditorAction","KeyEvent.KEYCODE_ENTER");
					return true;
				}
				else return false;
			}
            });
		
		btn_send_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				globalID.un_stop = true;
				startContact();
			}
		});
		
		et_send_Id.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
//					startContact();
//					readContacts();
				}
				else{
//					getFuzzyQueryByName(et_send_Id.getText().toString());
				}
			}
		});
		
		
		//no ScrollBar
//		sv_send_sv.setVerticalScrollBarEnabled(false);
		
		
		iv_send_HeadPhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				globalID.un_stop = true;
//				showDialog();
			}
		});
		
		//set button listener
		send_btn.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				globalID.un_stop = true;
				send();
			}
		});
		//set spinner
//		String[] Send_Type = {"图文信息"
//				,"图片信息"
//				,"文字信息"
//				};

//		String[] ark_id = new String[globalID.getAll().size()];
//		for(int i = 0;i<ark_id.length;i++){
//			ark_id[i] = globalID.getAll().get(i).getArk_id()+": "+globalID.getAll().get(i).getArk_no();
//		}
//		String[] ark_id = {"1： 粤台118881"
//				,"2： 粤台12828"
//				,"3： 粤茂81888"
//				};
		
//		if(ark_id.length == 0){
//			if(globalID.toast != null)globalID.toast.cancel();
//			globalID.toast = Toast.makeText(SendActivity.this, "没有船号信息", Toast.LENGTH_LONG);
//			globalID.toast.show();
//		}
		
		//android.R.layout.browser_link_context_header no use??
//		SpinnerAdapter aa_kind = new SpinnerAdapter(this,android.R.layout.browser_link_context_header,Send_Type);
//		SpinnerAdapter aa_ark_id = new SpinnerAdapter(this,android.R.layout.two_line_list_item,ark_id);
//		SpinnerAdapter aa_level = new SpinnerAdapter(this,android.R.layout.simple_dropdown_item_1line,Send_level);
		
//		sp_send_Kind.setAdapter(aa_kind);
//		sp_send_ark_id.setAdapter(aa_ark_id);
//		sp_send_level.setAdapter(aa_level);
	}
	
	private void setTitle() {
		// TODO Auto-generated method stub
				// TODO Auto-generated method stub
		GlobalID globalID = (GlobalID)getApplication();
		send_title = (TextView)findViewById(R.id.send_title);
//	    send_title.setText(globalID.getAll().get(ark_num).getArk_no());
		send_title.setText("紧急发送");
	    send_title_bar_back = (ImageButton)findViewById(R.id.send_title_bar_back);
	    send_title_bar_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	    send_btn = (Button)findViewById(R.id.send_btn);
	}

	@Override
    public void onPause(){
    	super.onPause();
    	Log.v(TAG,"onPause");
    	GlobalID globalID = ((GlobalID)getApplication());
	    if(globalID.un_stop)return;
	    else{
//	    	globalID.setCurrent_code(4);
	    	globalID.create_notification("后台接收数据", "后台运行", "岸客户端", false, false, false,SendActivity.class.getName());
			if(globalID.toast != null)globalID.toast.cancel();
//			globalID.clear();
	    }
    }
	
    @Override
    public void finish(){
    	super.finish();
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
		GlobalID globalID = ((GlobalID)getApplication());
		globalID.un_stop = true;
    }
    
    @Override
	 public void onResume(){
		 super.onResume();
		 Log.v(TAG,"onResume");
		 GlobalID globalID = ((GlobalID)getApplication());
		 if(get_result)globalID.un_stop = false;
		 globalID.cancel_notification();
	 }
	
	/*********************Spinner Settings**************************/
//	private class SpinnerAdapter extends ArrayAdapter<String> {
//
//	    Context context;
//	    String[] items = new String[] {};
//
//	    public SpinnerAdapter(final Context context,
//	            final int textViewResourceId, final String[] objects) {
//	        super(context, textViewResourceId, objects);
//	        this.items = objects;
//	        this.context = context;
//	    }
//
//
//	    @Override
//	    public View getDropDownView(int position, View convertView,
//	            ViewGroup parent) {
//	        if (convertView == null) {
//	            LayoutInflater inflater = LayoutInflater.from(context);
//	            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
//	        }
//
//	        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
//	        tv.setText(items[position]);
//	        tv.setTextColor(Color.BLUE);
//	        tv.setTextSize(40);
//	        return convertView;
//	    }
//
//	    @Override
//	    public View getView(int position, View convertView, ViewGroup parent) {
//	        if (convertView == null) {
//	            LayoutInflater inflater = LayoutInflater.from(context);
//	            convertView = inflater.inflate(
//	                    android.R.layout.simple_spinner_item, parent, false);
//	        }
//
//	        // android.R.id.text1 is default text view in resource of the android.
//
//	        // android.R.layout.simple_spinner_item is default layout in resources of android.
//
//	        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
//	        tv.setText(items[position]);
//	        tv.setTextColor(Color.RED);
//	        tv.setTextSize(18);
//	        return convertView;
//	    }
//	}
	/*********************Spinner Settings**************************/
	
	/*********************Picture*********************************/
	private void showDialog(){
		new AlertDialog.Builder(this).setTitle("选择图片").setItems(items, new
				DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
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
	

//	@Override
//	protected void onActivityResult(int requestCode , int resultCode , Intent data){
//		if(resultCode != RESULT_CANCELED){
//		switch (requestCode){
//		case IMAGE_REQUEST_CODE:
//			startPhotoZoom(data.getData());
//			break;
//		case CAMERA_REQUEST_CODE:
//			if(Tools.hasSdcard()){
//				File tempFile = new File(
//						Environment.getExternalStorageDirectory() + IMAGE_FILE_NAME);
//				startPhotoZoom(Uri.fromFile(tempFile));
////				startPhotoZoom(Uri.fromFile(new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME)));
//			}else{
//				Toast.makeText(SendActivity.this, "未找到存储卡，无法存储照片", Toast.LENGTH_LONG).show();
//			}
//			break;
//		case RESULT_REQUEST_CODE:
//			if(data != null){
//				getImageToView(data);
//			}
//			break;
//		}
//	}
//		super.onActivityResult(requestCode, resultCode, data);
//	}
//	
//	public void startPhotoZoom(Uri uri){
//		Intent intent = new Intent("com.android.camera.action.CROP");
//		intent.setDataAndType(uri, "image/*");
//		
//		intent.putExtra("crop", true);
//		intent.putExtra("aspectX", 0.1);
//		intent.putExtra("aspectY", 0.1);
//		
//		intent.putExtra("outputX", 90);
//		intent.putExtra("outputY", 90);	
//		intent.putExtra("return-data", true);
//		startActivityForResult(intent , 2);
//	}
//
//	private void getImageToView(Intent data){
//		Bundle extras = data.getExtras();
//		if(extras != null ){
//			Bitmap photo = extras.getParcelable("data");
//			@SuppressWarnings("deprecation")
//			Drawable drawable = new BitmapDrawable(photo);
//			iv_send_HeadPhoto.setImageDrawable(drawable);
//			Log.v("send", "has picture");
//			no_picture = false;
//		}
//	}
	/*********************Picture*********************************/
	
	
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
//		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
//			be = (int) (newOpts.outWidth / ww);
//		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
//			be = (int) (newOpts.outHeight / hh);
//		}
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
	

//    
//    /**
//	 * 启动通讯录界面
//	 */
//	public void startContact(){
//		Intent intent = new Intent(); 
//		intent.setAction(Intent.ACTION_PICK); 
//		intent.setData(ContactsContract.Contacts.CONTENT_URI);
//		startActivityForResult(intent, 0);
//		list = new ArrayList<String>();
//	}
    
	/*******************Search phone number**********************/
//    /**
//     * 查询所有联系人姓名及电话号码
//     */
//	private void readContacts(){
//      StringBuilder sb = new StringBuilder();
//      ContentResolver cr = getContentResolver();
//      
//      // select * from contacts
//      Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, 
//          null, null, null, null);
//      while(cursor.moveToNext()){
//        String id = cursor.getString(
//            cursor.getColumnIndex(ContactsContract.Contacts._ID));
//        String name = cursor.getString(
//            cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//        int iHasPhoneNum = Integer.parseInt(cursor.getString(
//            cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
//        sb.append(name + " (");
//        
//        if(iHasPhoneNum > 0){
//          Cursor numCursor = cr.query(
//              ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
//              null, 
//              ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, 
//              null, null);
//          while(numCursor.moveToNext()){
//            String number = numCursor.getString(
//                numCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//            sb.append(number + ")");
//          }
//          numCursor.close();
//        }
//        sb.append("\r\n");
//      }
//      cursor.close();
//      
//      if(!sb.toString().isEmpty()){
//        Log.d("send", "联系人:\r\n" + sb.toString());
//      }
//    }
//    
//    /**
//     * 根据名字中的某一个字进行模糊查询
//     * @param key
//     */
//	private void getFuzzyQueryByName(String key){
//      
//      StringBuilder sb = new StringBuilder();
//      ContentResolver cr = getContentResolver();
//      String[] projection = {ContactsContract.PhoneLookup.DISPLAY_NAME,
//              ContactsContract.CommonDataKinds.Phone.NUMBER};
//      Cursor cursor = cr.query(
//          ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
//          projection, 
//          ContactsContract.Contacts.DISPLAY_NAME + " like " + "'%" + key + "%'", 
//          null, null);
//      while(cursor.moveToNext()){
//        String name = cursor.getString(
//            cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//        String number = cursor.getString(
//            cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//        sb.append(name + " (").append(number + ")").append("\r\n");
//      }
//      cursor.close();
//      
//      if(!sb.toString().isEmpty()){
//        Log.d("send", "查询联系人:\r\n" + sb.toString());
//      }
//    }
	/*******************Search phone number**********************/
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	menu.add(0,0,0,"发送");
        return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.login, menu);
		
		switch(item.getItemId()){
		//菜单项1被选择
		case 0 :
			send();
			break;
		}
		return true;
	}
	
    public void send(){
		final GlobalID globalID = ((GlobalID)getApplication());

		send_btn.setClickable(false);

		final String send_id = et_send_Id.getText().toString();
		final String detail = et_send_Detail.getText().toString();
		
		if(send_id.equals(globalID.getID())){
//			new AlertDialog.Builder(SendActivity.this).  
//			setTitle("错误").setMessage("不能发送信息给自己").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					send_btn.setClickable(true);
//					dialog.dismiss();
//					et_send_Id.requestFocus();
//					}  
//				}).show();

			globalID.toast = Toast.makeText(SendActivity.this, "不能发送信息给自己", Toast.LENGTH_LONG);
			globalID.toast.show();
			et_send_Id.requestFocus();
			send_btn.setClickable(true);
			return;
		}
		
		else{
//			int pre_kind = (int)sp_send_Kind.getSelectedItemId();
//			if(no_picture)pre_kind = 2;
			
			if(no_picture && detail.length() == 0){
				globalID.toast = Toast.makeText(SendActivity.this, "没有要发送的信息", Toast.LENGTH_LONG);
				globalID.toast.show();
				et_send_Id.requestFocus();
				send_btn.setClickable(true);
				return;
			}
		}
		
		AlertDialog.Builder builder1 = new AlertDialog.Builder(SendActivity.this);
		if(globalID.getNow_ark() == -1){
			if(globalID.toast != null)globalID.toast.cancel();
			globalID.toast = Toast.makeText(SendActivity.this, "没有船号信息", Toast.LENGTH_LONG);
			globalID.toast.show();
			return;
		}
		
		int pre_kind = 0;
		
		if(no_picture){
			Log.v("send","no img");
			pre_kind = 2;
		}
		else{
			if(detail.length() == 0){
				Log.v("send","no msg");
				pre_kind = 1;
			}
		}
		
		
		final int kind = pre_kind;
		
		final String level = String.valueOf(11);
		
//		Log.v("ark_id","id:  "+length.toString());
		Message msg1 = new Message();
		msg1.what = 3;
		mhandler.sendMessage(msg1);
		
		Thread a_thread = new Thread(){
			@Override
			public void run(){
				if(msg != null) msg = new Message();
		        msg.what = 0;
		try {

			String url = globalID.getUrlServer();
			InetAddress serverAddr = InetAddress.getByName(url);
			int SERVERPORT = globalID.getSERVERPORT();
//			Socket socket=new Socket(serverAddr,SERVERPORT);
			Socket socket = new Socket();
			InetSocketAddress socketAdd = new InetSocketAddress(serverAddr, SERVERPORT);
			socket.connect(socketAdd, globalID.getTIMEOUT());

//			TimeOut timer = new TimeOut(globalID.getTIMEOUT(), SendActivity.this);
//			timer.start();
			
			PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
			
			
			switch(kind){
			case 0:{
				
			}
			case 1:{
				//from bitmap to byte[]
				iv_send_HeadPhoto.setDrawingCacheEnabled(true);
				Bitmap send_photo = iv_send_HeadPhoto.getDrawingCache();
				Bitmap image = ((BitmapDrawable)iv_send_HeadPhoto.getDrawable()).getBitmap();
				iv_send_HeadPhoto.setDrawingCacheEnabled(false);
				Log.v("send_photo", "photo  " + send_photo.toString());
				Log.v("image", "photo2  " + image.toString());
				ByteArrayOutputStream change = new ByteArrayOutputStream();
				
				//compress
				image = comp(image);
				image.compress(Bitmap.CompressFormat.JPEG,90,change);
				
				final byte[] send_p = change.toByteArray();
				
				final byte[] length = new byte[4];
				length[0] = (byte)(send_p.length);
				length[1] = (byte)(send_p.length >> 8);
				length[2] = (byte)(send_p.length >> 16);
				length[3] = (byte)(send_p.length >> 24);
				
//				String sql = "insert into tbl_img values ('" + globalID.getID() + "',"+ ark_id
//						+ ", 1 , "+level+", (getdate()),NULL,'";
//				Log.v("sql", "RIGHT: "+sql);
				
				FuntionUtil.doSth();
				
//	            if(kind == 0){
//	            	Log.v("sql1", "RIGHT: "+sql);
//	            	PrintWriter out2 = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
//
//	            	out2.println(10);
//	            	out2.println(sql);
//	            	out2.println(send_id);
//	            	Log.v("sql2", "RIGHT: "+sql);
//	            }
	           
//	            else{
		            out.println(12);
	            	out.println(globalID.getID());
	            	out.println(ark_id);
	            	out.println(level);
	            	out.println(send_id);
//	            }
	            	
//		            socket = new Socket(serverAddr,SERVERPORT);
	            	socket = new Socket();
//	            	InetSocketAddress socketAdd = new InetSocketAddress(serverAddr, SERVERPORT);
					socket.connect(socketAdd, globalID.getTIMEOUT());
		            socket.setSoTimeout(globalID.getTIMEOUT());
		            OutputStream os = socket.getOutputStream();
		            byte[] code_p = new byte[send_p.length+4];
		            System.arraycopy(length, 0, code_p, 0, 4);
		            System.arraycopy(send_p, 0, code_p, 4 , send_p.length);
		            os.write(code_p);
//		            Log.v("sql3", "RIGHT: "+sql);
	            
//	            for(int i = 0;i<send_p.length;i += 1024){
//	            	if(i > send_p.length-1024){
//			            System.arraycopy(send_p, i, code_p, 0 , send_p.length%1024);
//	            	}
//	            	else System.arraycopy(send_p, i, code_p, 0 , 1024);
//	            	
//                 	Log.v("byte "+i, String.valueOf(code_p[0]));
//	            	
//		            os.write(code_p);
//		            }
	            
//	            for(int i1 = 0;i1<code_p.length;i1+=1024){
//                 	Log.v("byte "+i1, String.valueOf(code_p[i1]));
//             	}
	            
//	            os.write(code_p);
	            
	            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GB2312"));
	            String line = reader.readLine();
	            Log.v("id_checked",line);
	            
//	            if(kind == 1)timer.interrupt();

            	if(socket!=null)socket.close();
	            if(line.equals("1")){
	            	if(kind == 1){
        				 if(globalID.isShake()){
        					 Log.v("login","shake");
        					 vibrator.vibrate(new long[]{1000,50,50,100,50}, -1);
        					 }
		            	mhandler.sendMessage(msg);
		            	break;
	            	}
	            }
	            else{
        			 msg.what = -1;
        			 mhandler.sendMessage(msg);
	            }
	            
//	            int i = Integer.parseInt(line);
//	            if(i == 1 && kind == 1){
//	            	mhandler.sendMessage(msg);
//	            	if(socket!=null)socket.close();
//	            	break;
//	            	}
//	        	 else {
//	        		 if(i != 1){
//	        			 msg.what = -1;
//	        			 mhandler.sendMessage(msg);
//	        		 }
//	        		 if(socket!=null)socket.close();
//	        	 }
			}
			case 2:{
				FuntionUtil.doSth();
				
				if(kind == 0){
//					socket = new Socket(serverAddr,SERVERPORT);
					socket = new Socket();
//					InetSocketAddress socketAdd = new InetSocketAddress(serverAddr, SERVERPORT);
					socket.connect(socketAdd, globalID.getTIMEOUT());
//	            	Log.v("sql1", "RIGHT: "+sql);
	            	PrintWriter out2 = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);

	            	out2.println(11);
	            	out2.println(globalID.getID());
	            	out2.println(ark_id);
	            	out2.println(level);
	            	out2.println(detail);
	            	out2.println(send_id);
//	            	Log.v("sql2", "RIGHT: "+sql);
	            }
				
				else{
		            out.println(11);
	            	out.println(globalID.getID());
	            	out.println(ark_id);
	            	out.println(level);
	            	out.println(detail);
	            	out.println(send_id);
				}
				
	            socket.setSoTimeout(globalID.getTIMEOUT());
	            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GB2312"));
	            String line = reader.readLine();
	            Log.v("id_checked",line);

//	            timer.interrupt();
	            
//	            int i = Integer.parseInt(line);
	            if(line.equals("1")){
            		if(msg != null) msg = new Message();
	            	if(kind == 0){
        				 if(globalID.isShake()){
        					 Log.v("login","shake");
        					 vibrator.vibrate(new long[]{1000,50,50,100,50}, -1);
        					 }
		            	msg.what = 2;
		            	mhandler.sendMessage(msg);
		            }
	            	else {
        				 if(globalID.isShake()){
        					 Log.v("login","shake");
        					 vibrator.vibrate(new long[]{1000,50,50,100,50}, -1);
        					 }
		            	msg.what = 1;
	            		mhandler.sendMessage(msg);
	            	}
	            	}
	        	 else{
	        		 msg.what = -1;
	        		 mhandler.sendMessage(msg);
	        	 }
	            if(socket!=null)socket.close();
			}
				break;
				default:
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			Log.v("UnknownHostException","err "+e);
    		if(msg != null) msg = new Message();
			msg.what = -2;
			mhandler.sendMessage(msg);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.v("IOException","err "+e);
    		if(msg != null) msg = new Message();
			msg.what = -3;
			mhandler.sendMessage(msg);
			e.printStackTrace();
		}
			}
			};
			a_thread.start();
		send_btn.setClickable(true);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
    	super.onSaveInstanceState(savedInstanceState);
  		GlobalID globalID = ((GlobalID)getApplication());
    	Log.v(TAG,"onSaveInstanceState");
        savedInstanceState.putBoolean("Bus_change", globalID.isBus_change());
        savedInstanceState.putBoolean("Msg_change", globalID.isMsg_change());
        savedInstanceState.putBoolean("Img_change", globalID.isImg_change());
        savedInstanceState.putString("All", globalID.Ark_line2String());
        savedInstanceState.putInt("ark_num", ark_num);
    }    
    
    @Override  
    public void onRestoreInstanceState(Bundle savedInstanceState) {  
    	super.onRestoreInstanceState(savedInstanceState);
		GlobalID globalID = ((GlobalID)getApplication());
    	Log.v(TAG, "Now_ark = "+ globalID.getNow_ark());
    	} 
    
//	/*********************InnerScrollView**************************************/
//	public class InnerScrollView extends ScrollView { 
//		/** 
//		*/ 
//		public ScrollView parentScrollView; 
//		public InnerScrollView(Context context, AttributeSet attrs) { 
//		super(context, attrs); 
//		} 
//		private int lastScrollDelta = 0; 
//		public void resume() { 
//			overScrollBy(0, -lastScrollDelta, 0, getScrollY(), 0, getScrollRange(), 0, 0, true); 
//			lastScrollDelta = 0; 
//		} 
//		
//		int mTop = 10; 
//		/** 
//		* 将targetView滚到最顶端 
//		*/ 
//		
//		public void scrollTo(View targetView) {
//			int oldScrollY = getScrollY(); 
//			int top = targetView.getTop() - mTop; 
//			int delatY = top - oldScrollY; 
//			lastScrollDelta = delatY; 
//			overScrollBy(0, delatY, 0, getScrollY(), 0, getScrollRange(), 0, 0, true); 
//			}
//		
//		private int getScrollRange() { 
//			int scrollRange = 0;
//			if (getChildCount() > 0) { 
//				View child = getChildAt(0); 
//				scrollRange = Math.max(0, child.getHeight() - (getHeight())); 
//				} 
//			return scrollRange; 
//			} 
//		
//		int currentY; 
//		
//		@Override 
//		public boolean onInterceptTouchEvent(MotionEvent ev) { 
//			if (parentScrollView == null) { 
//				return super.onInterceptTouchEvent(ev); 
//				} 
//			else { 
//				if (ev.getAction() == MotionEvent.ACTION_DOWN) { 
//					// 将父scrollview的滚动事件拦截 
//					currentY = (int)ev.getY(); 
//					setParentScrollAble(false); 
//					return super.onInterceptTouchEvent(ev);
//					} 
//				else if (ev.getAction() == MotionEvent.ACTION_UP) { 
//					// 把滚动事件恢复给父Scrollview 
//					setParentScrollAble(true); 
//					} 
//				else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
//					} 
//				} 
//			return super.onInterceptTouchEvent(ev); 
//		} 
//		
//		@Override 
//		public boolean onTouchEvent(MotionEvent ev) {
//			View child = getChildAt(0); 
//			if (parentScrollView != null) { 
//				if (ev.getAction() == MotionEvent.ACTION_MOVE) { 
//					int height = child.getMeasuredHeight(); 
//					height = height - getMeasuredHeight(); 
//					
//					// System.out.println("height=" + height); 
//					int scrollY = getScrollY(); 
//					
//					// System.out.println("scrollY" + scrollY); 
//					int y = (int)ev.getY(); 
//					
//					// 手指向下滑动 
//					if (currentY < y) { 
//						if (scrollY <= 0) { 
//							// 如果向下滑动到头，就把滚动交给父Scrollview 
//							setParentScrollAble(true); 
//							return false; 
//							} 
//						else { 
//							setParentScrollAble(false); 
//							} 
//						} 
//					else if (currentY > y) { 
//						if (scrollY >= height) { 
//							// 如果向上滑动到头，就把滚动交给父Scrollview 
//							setParentScrollAble(true); 
//							return false; 
//							} 
//						else { 
//							setParentScrollAble(false); 
//							} 
//						} 
//					currentY = y; 
//					} 
//				} 
//			return super.onTouchEvent(ev); 
//			} 
//		
//		/** 
//		* 是否把滚动事件交给父scrollview 
//		* 
//		* @param flag 
//		*/ 
//		private void setParentScrollAble(boolean flag) { 
//		parentScrollView.requestDisallowInterceptTouchEvent(!flag); 
//		} 
//	}
//	/*********************InnerScrollView**************************************/
}
