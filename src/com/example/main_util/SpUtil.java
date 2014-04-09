package com.example.main_util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SpUtil {
	
	public static boolean write(Context context, String data, String tagname) throws Exception{
		if(context == null){
			return false;
		}
		SharedPreferences sharedPreferences = context.
		                   getSharedPreferences(StringUtil.FILENAME, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(tagname, data);
		editor.commit();
		return true;
    }
	
	public static boolean write(Context context, boolean data, String tagname){
		if(context == null){
			return false;
		}
		SharedPreferences sharedPreferences = context.
		                   getSharedPreferences(StringUtil.FILENAME, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(tagname, data);
		editor.commit();
		return true;
    }
	
	public static String read(Context context,String tagname) throws Exception {
		SharedPreferences sharedPreferences = context.
		                   getSharedPreferences(StringUtil.FILENAME, Context.MODE_PRIVATE);
		String data = sharedPreferences.getString(tagname, "");
		return data;
    }
	
	public static boolean readBoolean(Context context,String tagname) {
		SharedPreferences sharedPreferences = context.
		                   getSharedPreferences(StringUtil.FILENAME, Context.MODE_PRIVATE);
		boolean data = sharedPreferences.getBoolean(tagname, false);
		return data;
    }
	
}
