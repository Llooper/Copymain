package com.example.main_Entity;

import android.graphics.Bitmap;


//define all data used in SendPre list
public class SendPreEntity {

	//数据库里面的id
	private int ark_id;
	
	private Bitmap SendPre_picture;
	
	private String SendPre_detail;
	private float SendPre_gps_la = 0;
	private float SendPre_gps_lo = 0;


	public Bitmap getSendPre_picture() {
		return SendPre_picture;
	}

	public void setSendPre_picture(Bitmap SendPre_picture) {
		this.SendPre_picture = SendPre_picture;
	}

	public String getSendPre_detail() {
		return SendPre_detail;
	}

	public void setSendPre_detail(String SendPre_detail) {
		this.SendPre_detail = SendPre_detail;
	}

	public float getSendPre_gps_la() {
		return SendPre_gps_la;
	}

	public void setSendPre_gps_la(float sendPre_gps_la) {
		SendPre_gps_la = sendPre_gps_la;
	}

	public float getSendPre_gps_lo() {
		return SendPre_gps_lo;
	}

	public void setSendPre_gps_lo(float sendPre_gps_lo) {
		SendPre_gps_lo = sendPre_gps_lo;
	}

	public int getArk_id() {
		return ark_id;
	}

	public void setArk_id(int ark_id) {
		this.ark_id = ark_id;
	}
}
