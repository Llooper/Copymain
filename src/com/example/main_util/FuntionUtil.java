package com.example.main_util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.StrictMode;

public class FuntionUtil {
	
	public static String toHTMLString(String in) {
        StringBuffer out = new StringBuffer();
        for (int i = 0; in != null && i < in.length(); i++) {
            char c = in.charAt(i);
            if (c == '\'')
                out.append("&#039;");
            else if (c == '\"')
                out.append("&#034;");
            else if (c == '<')
                out.append("&lt;");
            else if (c == '>')
                out.append("&gt;");
            else if (c == '&')
                out.append("&amp;");
            else if (c == ' ')
                out.append("&nbsp;");
            else if (c == '\n')
                out.append("<br/>");
            else
                out.append(c);
        }
        return out.toString();
    }
	
	public static void doSth(){
		
		// ÅÐ¶ÏAPI>=11
		if(Build.VERSION.SDK_INT >= 11){
			/********************very important for android 4.0(and above) to connect internet************************/
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
			.detectDiskReads().detectDiskWrites().detectNetwork()
			.penaltyLog().build());
			
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
			.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
			.build());
			/********************very important for android 4.0(and above) to connect internet************************/
	        }
	}
	
	/***********down picture from urlPic******************/
    public static Bitmap downloadPic(String urlPic){
		try {
			doSth();
			URL url = new URL(urlPic);
			InputStream is = url.openStream();
			Bitmap bit = BitmapFactory.decodeStream(is);
			return bit ;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    /***********down picture from URL******************/
    
    /***********dp 2 px **********/
    public static int dip2px(Context context, float dipValue){
    	final float scale = context.getResources().getDisplayMetrics().density;
    	return (int)(dipValue * scale +0.5f);
    }
    	public static int px2dip(Context context, float pxValue){
    	final float scale = context.getResources().getDisplayMetrics().density;
    	return (int)(pxValue / scale +0.5f);
    }
    /***********dp 2 px **********/
    	
    	public int send_head(int length,byte[] send,int Len_total_length,int Len_total,int Cmd_id_length,int Cmd_id,int Seq_id_length,int Seq_id){
    		for(int i = 0;i<Len_total_length;i++){
                send[length + i] = (byte)(Len_total >> 24-i*8);
            }
            length += Len_total_length;
//        	Log.v("heart ","length1:"+String.valueOf(length));
            for(int i = 0;i<Cmd_id_length;i++){
                send[length + i] = (byte)(Cmd_id >> 24-i*8);
            }
            length += Cmd_id_length;
//        	Log.v("heart ","length2:"+String.valueOf(length));
            for(int i = 0;i<Seq_id_length;i++){
                send[length+i] = (byte)(Seq_id >> 24-i*8);
            }
            length += Seq_id_length;
//        	Log.v("heart ","length3:"+String.valueOf(length));
    		return length;
    	}
}
