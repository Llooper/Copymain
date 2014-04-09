package com.example.main_old_or_out;
//package com.example.main;
//
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.util.Log;
//
//public class TimeOut extends Thread{
//	public Handler mHandler;
////	private ProgressDialog pd;
//	
//	/***每个多少毫秒检测一次**/ 
//	protected int m_rate = 100; 
//	/***超时时间长度毫秒计算**/ 
//	private int m_length; 
//	/** 已经运行的时间*/
//	private int m_elapsed;
//	
//	private Context ctx;
//	/***构造函数*
//	 ** @param length*Length of time before timeout occurs 
//	 * @return */
//	public TimeOut(int length,Context ct) {
//		// Assign to member variable
//		System.out.println("start timeOut");
//		Log.v("start ","timeOut");
//		ctx = ct;
////		pd= ProgressDialog.show(ctx, "连接", "正在连接服务器…");
////		m_length = length;
////		// Set time elapsed
////		m_elapsed = 0;
//		}
//	/***重新计时**/
////	public synchronized void reset() { 
////		m_elapsed = 0; 
////		System.out.println("reset timer"); 
////		} 
//	/***故意设置为超时,可以在服务器有返回,但是错误返回的时候直接调用这个,当成超时处理**/
////	public synchronized void setTimeOut(){
////		m_elapsed = m_length+1; 
////		} 
//	/***/ 
//	@SuppressWarnings("deprecation")
//	public void run() { 
//		Looper.prepare();
//		
////		ProgressDialog pd = new ProgressDialog(ctx);
////		pd.show();
//		//循环
////		System.out.println("timer running");
////		for (;;) {
//////			System.out.println("start timeOut2");
//////			Log.v("start ","timeOut2");
////			// Put the timer to sleep
//			try {
//				Thread.sleep(2);
//				} catch (InterruptedException ioe) {
////					pd.dismiss();
////					break;
//					} 
////			
////			synchronized (this) {
////				// Increment time remaining 
////				m_elapsed += m_length+1; 
////				// Check to see if the time has been exceeded
////				if (m_elapsed>m_length) { //isConnActive 为全局变量
////					// Trigger a timeout
////					pd.dismiss();
////					timeout();
////					break;
////					}
////				} 
////		}
//		Looper.loop();
//		}
////	/*** 超时时候的处理**/
////	public void timeout() {
////		Log.v("timeOut","TimeOut");
////		 new AlertDialog.Builder(ctx).  
////			setTitle("错误").setMessage("连接超时").setPositiveButton("确定", new DialogInterface.OnClickListener() {  
////				public void onClick(DialogInterface dialog, int which) {
////					dialog.dismiss();
////					} 
////				}).show();
////		 }
//}
