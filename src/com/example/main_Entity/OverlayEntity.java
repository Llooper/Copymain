package com.example.main_Entity;

import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class OverlayEntity extends OverlayItem{

	private String ark_id
	,ark_captain
	,datetime;
	
	
	public String getArk_id() {
		return ark_id;
	}


	public void setArk_id(String ark_id) {
		this.ark_id = ark_id;
	}


	public String getArk_captain() {
		return ark_captain;
	}


	public void setArk_captain(String ark_captain) {
		this.ark_captain = ark_captain;
	}


	public String getDatetime() {
		return datetime;
	}


	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}


	public OverlayEntity(GeoPoint arg0, String arg1, String arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
		this.setDatetime(arg2);
	}
	
	
}