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
//	/***ÿ�����ٺ�����һ��**/ 
//	protected int m_rate = 100; 
//	/***��ʱʱ�䳤�Ⱥ������**/ 
//	private int m_length; 
//	/** �Ѿ����е�ʱ��*/
//	private int m_elapsed;
//	
//	private Context ctx;
//	/***���캯��*
//	 ** @param length*Length of time before timeout occurs 
//	 * @return */
//	public TimeOut(int length,Context ct) {
//		// Assign to member variable
//		System.out.println("start timeOut");
//		Log.v("start ","timeOut");
//		ctx = ct;
////		pd= ProgressDialog.show(ctx, "����", "�������ӷ�������");
////		m_length = length;
////		// Set time elapsed
////		m_elapsed = 0;
//		}
//	/***���¼�ʱ**/
////	public synchronized void reset() { 
////		m_elapsed = 0; 
////		System.out.println("reset timer"); 
////		} 
//	/***��������Ϊ��ʱ,�����ڷ������з���,���Ǵ��󷵻ص�ʱ��ֱ�ӵ������,���ɳ�ʱ����**/
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
//		//ѭ��
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
////				if (m_elapsed>m_length) { //isConnActive Ϊȫ�ֱ���
////					// Trigger a timeout
////					pd.dismiss();
////					timeout();
////					break;
////					}
////				} 
////		}
//		Looper.loop();
//		}
////	/*** ��ʱʱ��Ĵ���**/
////	public void timeout() {
////		Log.v("timeOut","TimeOut");
////		 new AlertDialog.Builder(ctx).  
////			setTitle("����").setMessage("���ӳ�ʱ").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {  
////				public void onClick(DialogInterface dialog, int which) {
////					dialog.dismiss();
////					} 
////				}).show();
////		 }
//}
