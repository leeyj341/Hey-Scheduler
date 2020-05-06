package com.project.heyScheduler.calendar;

import java.util.ArrayList;

public class PlanVO {
	private String plan_no;
	private String title;
	private String startdatetime;
	private String loc_x;
	private String loc_y;
	private String content;
	private String location;
	private String enddatetime;
	private String color;
	private String host_id;
	private ArrayList<String> guest_ids;
	
	public PlanVO() {
		// TODO Auto-generated constructor stub
	}
		
	public PlanVO(String startdatetime) {
		super();
		this.startdatetime = startdatetime;
	}

	public PlanVO(String startdatetime, String enddatetime) {
		super();
		this.startdatetime = startdatetime;
		this.enddatetime = enddatetime;
	}	

	public PlanVO(String plan_no, String title, String startdatetime, String loc_x, String loc_y, String content,
			String location, String enddatetime, String color, String host_id) {
		super();
		this.plan_no = plan_no;
		this.title = title;
		this.startdatetime = startdatetime;
		this.loc_x = loc_x;
		this.loc_y = loc_y;
		this.content = content;
		this.location = location;
		this.enddatetime = enddatetime;
		this.color = color;
		this.host_id = host_id;
	}

	public PlanVO(String plan_no, String title, String startdatetime, String loc_x, String loc_y, String content,
			String location, String enddatetime, String color, String host_id, ArrayList<String> guest_ids) {
		super();
		this.plan_no = plan_no;
		this.title = title;
		this.startdatetime = startdatetime;
		this.loc_x = loc_x;
		this.loc_y = loc_y;
		this.content = content;
		this.location = location;
		this.enddatetime = enddatetime;
		this.color = color;
		this.host_id = host_id;
		this.guest_ids = guest_ids;
	}

	public String getPlan_no() {
		return plan_no;
	}

	public void setPlan_no(String plan_no) {
		this.plan_no = plan_no;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStartdatetime() {
		return startdatetime;
	}

	public void setStartdatetime(String startdatetime) {
		this.startdatetime = startdatetime;
	}

	public String getLoc_x() {
		return loc_x;
	}

	public void setLoc_x(String loc_x) {
		this.loc_x = loc_x;
	}

	public String getLoc_y() {
		return loc_y;
	}

	public void setLoc_y(String loc_y) {
		this.loc_y = loc_y;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getEnddatetime() {
		return enddatetime;
	}

	public void setEnddatetime(String enddatetime) {
		this.enddatetime = enddatetime;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getHost_id() {
		return host_id;
	}

	public void setHost_id(String host_id) {
		this.host_id = host_id;
	}

	public ArrayList<String> getGuest_ids() {
		return guest_ids;
	}

	public void setGuest_ids(ArrayList<String> guest_ids) {
		this.guest_ids = guest_ids;
	}

	@Override
	public String toString() {
		return "PlanVO [plan_no=" + plan_no + ", title=" + title + ", startdatetime=" + startdatetime + ", loc_x="
				+ loc_x + ", loc_y=" + loc_y + ", content=" + content + ", location=" + location + ", enddatetime="
				+ enddatetime + ", color=" + color + ", host_id=" + host_id + ", guest_ids=" + guest_ids + "]";
	}

	
	
}
