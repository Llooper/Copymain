package com.example.main_Entity;

import android.graphics.Bitmap;

public class BandWEntity {
	//是否新闻信息
	private Boolean list_type = true;
	private String Id;
	private String Title;
	private String Typeid;
	private String Detail;
	private String PicName;
	private Bitmap Pic;
	private String Time;
	
//	public int index = -1;
	
	public BandWEntity(){
		super();
	}
	
	public BandWEntity(Boolean list_type ,String Id
			,String Title ,String Typeid ,String Detail
			,String Pic
			,String Time){
		super();
		setAll(list_type, Id, Title, Typeid, Detail,Pic, null, Time);
	}
	
	public BandWEntity(Boolean list_type ,String Id
			,String Title ,String Typeid ,String Detail
			,Bitmap Pic ,String Time){
		super();
		setAll(list_type, Id, Title, Typeid, Detail,"", Pic, Time);
	}
	
	public void setAll(Boolean list_type ,String Id
			,String Title ,String Typeid ,String Detail
			,String PicName
			,Bitmap Pic ,String Time){
		this.list_type = list_type;
		this.Id = Id;
		this.Title = Title;
		this.Typeid = Typeid;
		this.Detail = Detail;
		this.PicName = PicName;
		this.Pic = Pic;
		this.Time = Time;
	}

	public Boolean getList_type() {
		return list_type;
	}

	public void setList_type(Boolean list_type) {
		this.list_type = list_type;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getTypeid() {
		return Typeid;
	}

	public void setTypeid(String typeid) {
		Typeid = typeid;
	}

	public String getDetail() {
		return Detail;
	}

	public void setDetail(String detail) {
		Detail = detail;
	}

	public String getPicName() {
		return PicName;
	}

	public void setPicName(String picName) {
		PicName = picName;
	}

	public Bitmap getPic() {
		return Pic;
	}

	public void setPic(Bitmap pic) {
		Pic = pic;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

}
