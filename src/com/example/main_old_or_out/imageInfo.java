package com.example.main_old_or_out;
//package com.example.main_Info;
//
//import java.io.IOException;
//import java.net.UnknownHostException;
//
//import com.example.main.DetailPicture;
//import com.example.main.GlobalID;
//import com.example.main.R;
//import com.example.main.R.id;
//import com.example.main.R.layout;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.ClipData;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.Window;
//import android.view.View.OnClickListener;
//import android.view.View.OnLongClickListener;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
////just for show image list in detail
//public class imageInfo extends Activity{
//	
//	private static TextView tv_imageInfo_Title;
//	private static TextView tv_imageInfo_PostTime;
//	private static TextView tv_imageInfo_Poster;
//	private static ImageView iv_Image_picture;
//	private static LinearLayout ll_image_info;
//	
//	@Override 
//	public void onCreate(Bundle savedInstanceState) {
//        final String[] ArkNames = {"粤台118881","粤台12828 ","粤茂81888 "};
//        
//		super.onCreate(savedInstanceState);
//		
//		//set no title
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.image_info);
//		
//		tv_imageInfo_Title = (TextView)findViewById(R.id.tv_imageInfo_Title);
//		tv_imageInfo_PostTime = (TextView)findViewById(R.id.tv_imageInfo_PostTime);
//		tv_imageInfo_Poster = (TextView)findViewById(R.id.tv_imageInfo_Poster);
//		iv_Image_picture = (ImageView)findViewById(R.id.iv_imageInfo_view);
//		ll_image_info = (LinearLayout)findViewById(R.id.ll_image_info);
//		
//		Intent intent = getIntent();
//		Bundle data = intent.getExtras();
//		final String ark_id = (String)data.getSerializable("ark_id");
//		final String Image_datetime_send = (String)data.getSerializable("Image_datetime_send");
//		final String send_user_id = (String)data.getSerializable("send_user_id");
//		final byte[]  Image_picture = (byte[])data.getSerializable("Image_picture");
//		final String Colour = (String)data.getSerializable("colour");
//		
//		ll_image_info.setBackgroundColor(android.graphics.Color.parseColor(Colour));
//		if(Integer.parseInt(ark_id)<1){
//			Log.v("imageInfo1", "equals "+ark_id);
//			tv_imageInfo_Title.setText("发到海上的信息");
//		}
//		else {
//			Log.v("imageInfo2", "equals "+ark_id);
//			tv_imageInfo_Title.setText("船号:  ("+ ark_id + ")  "+ArkNames[Integer.parseInt(ark_id)-1]);
//		}
//		tv_imageInfo_PostTime.setText("发布时间:    \n"+Image_datetime_send);
//		tv_imageInfo_Poster.setText("发布者:  " + send_user_id);
//		if(Image_picture != null)iv_Image_picture.setImageBitmap(Bytes2Bimap(Image_picture));
//		
//		
//		OnLongClickListener LongClickListener = new OnLongClickListener() {
//			
//			@Override
//			public boolean onLongClick(View v) {
//				// TODO Auto-generated method stub
//				try {
//					SearchLongProcess(v);
//				} catch (UnknownHostException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return false;
//			}
//		
//		
//		private void SearchLongProcess(View v) throws UnknownHostException, IOException {
//			// TODO Auto-generated method stub
//			if (tv_imageInfo_Title.equals(v)) {
//				if(Integer.parseInt(ark_id)<1||Integer.parseInt(ark_id)>3){
//					copy("发到海上的信息");
//					}
//				else{
//					copy("("+ ark_id + ") "+ArkNames[Integer.parseInt(ark_id)-1]);
//					}
//			}
//			 if (tv_imageInfo_PostTime.equals(v)) {
//				copy(Image_datetime_send);
//			}
//			 if (tv_imageInfo_Poster.equals(v)) {
//				copy(send_user_id);
//			}
//			 }
//		};
//		
//		
//		OnClickListener clickListener = new OnClickListener(){
// 			@Override
//			public void onClick(View v) {
// 					try {
//						SearchButtonProcess(v);
//					} catch (UnknownHostException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						Log.v("imageInfo err", "UnknownHostException: "+e);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						Log.v("imageInfo err", "IOException: "+e);
//					}
// 			}
//
//			private void SearchButtonProcess(View v) throws UnknownHostException, IOException {
//				// TODO Auto-generated method stub
//				if(iv_Image_picture.equals(v)){
//					Bundle bundle = new Bundle();
//					bundle.putByteArray("Image_picture", Image_picture);
//					Intent intent = new Intent(imageInfo.this,DetailPicture.class);
//					//post data
//					intent.putExtras(bundle);
//		            startActivity(intent);
//				}
//				if (tv_imageInfo_Title.equals(v)) {	
//					if(Integer.parseInt(ark_id)<1||Integer.parseInt(ark_id)>3){
//						new_dialog("","发到海上的信息");
//						}
//					else{
//						new_dialog("船号","("+ ark_id + ") "+ArkNames[Integer.parseInt(ark_id)-1]);
//						}
//					}
//				if (tv_imageInfo_PostTime.equals(v)) {
//					 new_dialog("发布时间",Image_datetime_send);
//				}
//				if (tv_imageInfo_Poster.equals(v)) {
//					 new_dialog("发布者",send_user_id);
//				}
//				}
//			};
//
//         tv_imageInfo_Title.setOnClickListener(clickListener); 
//         tv_imageInfo_PostTime.setOnClickListener(clickListener); 
//         tv_imageInfo_Poster.setOnClickListener(clickListener); 
//         iv_Image_picture.setOnClickListener(clickListener); 
//
//         tv_imageInfo_Title.setOnLongClickListener(LongClickListener);
//         tv_imageInfo_PostTime.setOnLongClickListener(LongClickListener);
//         tv_imageInfo_Poster.setOnLongClickListener(LongClickListener);
//	}
//	
//	
//	private void new_dialog (String title, String content){
//		new AlertDialog.Builder(imageInfo.this).  
//		setTitle(title).setMessage(content).setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				}  
//			}).show();
//	}
//	
//	public Bitmap Bytes2Bimap(byte[] b) {
//        if (b.length != 0) {
//            return BitmapFactory.decodeByteArray(b, 0, b.length);
//        } else {
//            return null;
//        }
//    }
//	
//	private void copy (final String copy){
//		new AlertDialog.Builder(imageInfo.this).setPositiveButton("复制", new DialogInterface.OnClickListener() {  
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				GlobalID globalID = ((GlobalID)getApplication());
//		        ClipData textCd = ClipData.newPlainText("", copy);
//		        globalID.getClipboard().setPrimaryClip(textCd);
//				dialog.dismiss();
//				globalID.showCopyed(imageInfo.this);
//				}  
//			}).show();
//	}
//}
