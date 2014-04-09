package com.example.main_Entity;

public class Ark_lineEntity {
	
	//数据库里面的id
	private String ark_id;
	
	//船的名字
	private String ark_no;
	
	//经纬度
	private float SendPre_gps_la = 0;
	private float SendPre_gps_lo = 0;
	
	public String getArk_id() {
		return ark_id;
	}
	public void setArk_id(String ark_id) {
		this.ark_id = ark_id;
	}
	public String getArk_no() {
		return ark_no;
	}
	public void setArk_no(String ark_no) {
		this.ark_no = ark_no;
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
}
