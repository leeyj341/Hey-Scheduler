package com.project.heyScheduler.member;

public class MemberVO {
	private String id;
	private String pass;
	private String birthday;
	private String token;
	
	public MemberVO() {
		
	}
	
	public MemberVO(String id, String pass) {
		super();
		this.id = id;
		this.pass = pass;
	}

	public MemberVO(String id, String pass, String birthday, String token) {
		super();
		this.id = id;
		this.pass = pass;
		this.birthday = birthday;
		this.token = token;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "MemberVO [id=" + id + ", pass=" + pass + ", birthday=" + birthday + ", token=" + token + "]";
	}
	
	
}
