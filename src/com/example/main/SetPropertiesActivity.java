package com.example.main;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;

import com.baidu.platform.comapi.map.f;
import com.example.main.GeoPre.MyGestureDetector;
import com.example.main_Entity.Ark_lineEntity;
import com.example.main_util.SpUtil;
import com.example.main_util.StringUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SetPropertiesActivity extends Activity {
	
	private static EditText et_set_url,et_set_DB,et_set_port;
	private static Button btn_sure,btn_back;
	private Context context = SetPropertiesActivity.this;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    	
	        super.onCreate(savedInstanceState);

			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
	        //no_title
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.set_properties);
	        
	        et_set_url = (EditText)findViewById(R.id.et_set_url);
	        et_set_DB = (EditText)findViewById(R.id.et_set_DB);
	        et_set_port = (EditText)findViewById(R.id.et_set_port);
	        
	        btn_sure = (Button)findViewById(R.id.btn_sure);
	        btn_back = (Button)findViewById(R.id.btn_back);
	        
	        try {
				et_set_url.setText(SpUtil.read(context, "urlServer"));
				et_set_DB.setText(SpUtil.read(context, "DBurl"));
				et_set_port.setText(SpUtil.read(context, "SERVERPORT"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        btn_sure.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					try {
						SpUtil.write(context, et_set_url.getText().toString(), "urlServer");
						SpUtil.write(context, et_set_DB.getText().toString(), "DBurl");
						SpUtil.write(context, et_set_port.getText().toString(), "SERVERPORT");

						GlobalID globalID = ((GlobalID)getApplication());
						globalID.start(context);
						
						SetPropertiesActivity.this.finish();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						toast("无法修改");
					}
				}
			});
	        
	        btn_back.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					et_set_url.setText(StringUtil.URLSERVER);
					et_set_DB.setText(StringUtil.DBURL);
					et_set_port.setText(String.valueOf(StringUtil.SERVERPORT));
					
					toast("还原成功!");
				}
			});
	}
	
	void toast(String msg){
		GlobalID globalID = ((GlobalID)getApplication());
		if(globalID.toast != null)globalID.toast.cancel();
		globalID.toast = Toast.makeText(SetPropertiesActivity.this, msg, Toast.LENGTH_LONG);
		globalID.toast.show();
    }
}
