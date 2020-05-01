package com.project.heyScheduler.member;

public interface MemberDAO {
	public int insert(MemberVO member);
	public MemberVO select(MemberVO member);
}
