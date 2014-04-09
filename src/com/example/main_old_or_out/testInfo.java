package com.example.main_old_or_out;
//package com.example.main_Info;
//
//import java.io.IOException;
//import java.net.UnknownHostException;
//
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
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.Window;
//import android.view.View.OnClickListener;
//import android.view.View.OnLongClickListener;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//public class testInfo extends Activity{
//	
//	private static TextView tv_testInfo_PostTime;
//	private static TextView tv_testInfo_Detail;
//	private static TextView tv_testInfo_Title;
//	private static TextView tv_testInfo_Poster;
//	private static LinearLayout ll_test_info;
//	
//	@Override 
//	public void onCreate(Bundle savedInstanceState) { 
//        final String[] ArkNames = {"粤台118881","粤台12828 ","粤茂81888 "};
//        
//		super.onCreate(savedInstanceState);
//		
//		//set no title
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.test_info);
//		
//		tv_testInfo_PostTime = (TextView)findViewById(R.id.tv_testInfo_PostTime);
//		tv_testInfo_Detail = (TextView)findViewById(R.id.tv_testInfo_Detail);
//		tv_testInfo_Title = (TextView)findViewById(R.id.tv_testInfo_Title);
//		tv_testInfo_Poster = (TextView)findViewById(R.id.tv_testInfo_Poster);
//		ll_test_info = (LinearLayout)findViewById(R.id.ll_test_info);
//		
//		Intent intent = getIntent();
//		Bundle data = intent.getExtras();
//		final String PostTime = (String)data.getSerializable("PostTime");
//		final String Content = (String)data.getSerializable("Content");
//		final String test_ark_id = (String)data.getSerializable("test_ark_id");
//		final String send_user_id = (String)data.getSerializable("send_user_id");
//		final String Colour = (String)data.getSerializable("colour");
//		
//		ll_test_info.setBackgroundColor(android.graphics.Color.parseColor(Colour));
//		tv_testInfo_PostTime.setText("发布时间:    \n"+PostTime);
//		tv_testInfo_Detail.setText("详细信息: "+Content);
//		if(Integer.parseInt(test_ark_id)<1){
//			Log.v("textInfo1", "equals "+test_ark_id);
//			tv_testInfo_Title.setText("发到海上的信息");
//		}
//		else {
//			Log.v("textInfo2", "equals "+test_ark_id);
//			tv_testInfo_Title.setText("船号:  ("+ test_ark_id + ")  "+ArkNames[Integer.parseInt(test_ark_id)-1]);
//		}
//		tv_testInfo_Poster.setText("发布者:  " + send_user_id);
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
//			if (tv_testInfo_PostTime.equals(v)) {
//				copy(PostTime);
//			}
//			 if (tv_testInfo_Detail.equals(v)) {
//				copy(Content);
//			}
//			 if (tv_testInfo_Title.equals(v)) {
//					if(Integer.parseInt(test_ark_id)<1||Integer.parseInt(test_ark_id)>3){
//						copy("发到海上的信息");
//					}
//					else{
//						copy("("+ test_ark_id + ")"+ArkNames[Integer.parseInt(test_ark_id)-1]);
//					}
//			}
//			 if (tv_testInfo_Poster.equals(v)) {
//				copy(send_user_id);
//			}
//			 }
//		};
//		
//		OnClickListener clickListener = new OnClickListener(){
// 			@Override
//			public void onClick(View v) {
// 					try {
//						SearchButtonProcess(v);
//					} catch (UnknownHostException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						Log.v("textInfo err", "UnknownHostException: "+e);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						Log.v("textInfo err", "IOException: "+e);
//					}
// 			}
//
//			private void SearchButtonProcess(View v) throws UnknownHostException, IOException {
//				// TODO Auto-generated method stub
//					if (tv_testInfo_PostTime.equals(v)) {		
//						new_dialog("发布时间",PostTime);
//						}
//					 if (tv_testInfo_Detail.equals(v)) {
//						 new_dialog("详细信息",Content);
//					}
//					 if (tv_testInfo_Title.equals(v)) {
//							if(Integer.parseInt(test_ark_id)<1||Integer.parseInt(test_ark_id)>3){
//								new_dialog("","发到海上的信息");
//							}
//							else{
//								new_dialog("船号","("+ test_ark_id + ")"+ArkNames[Integer.parseInt(test_ark_id)-1]);
//							}
//					}
//					 if (tv_testInfo_Poster.equals(v)) {
//						 new_dialog("发布者" , send_user_id);
//					}
//			}
//         };
//         
//         tv_testInfo_PostTime.setOnClickListener(clickListener); 
//         tv_testInfo_Detail.setOnClickListener(clickListener); 
//         tv_testInfo_Title.setOnClickListener(clickListener); 
//         tv_testInfo_Poster.setOnClickListener(clickListener);
//
//         tv_testInfo_PostTime.setOnLongClickListener(LongClickListener);
//         tv_testInfo_Detail.setOnLongClickListener(LongClickListener);
//         tv_testInfo_Title.setOnLongClickListener(LongClickListener);
//         tv_testInfo_Poster.setOnLongClickListener(LongClickListener);
//	}
//	
//	private void new_dialog (String title, String content){
//		new AlertDialog.Builder(testInfo.this).  
//		setTitle(title).setMessage(content).setPositiveButton("确定", new DialogInterface.OnClickListener() {  
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				}  
//			}).show();
//	}
//	
//	private void copy (final String copy){
//		new AlertDialog.Builder(testInfo.this).setPositiveButton("复制", new DialogInterface.OnClickListener() {  
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				GlobalID globalID = ((GlobalID)getApplication());
//		        ClipData textCd = ClipData.newPlainText("", copy);
//		        globalID.getClipboard().setPrimaryClip(textCd);
//				dialog.dismiss();
//				globalID.showCopyed(testInfo.this);
//				}  
//			}).show();
//	}
//}
