package com.project.heyScheduler.member;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("MemberDAO")
public class MemberDAOImpl implements MemberDAO {
	@Autowired
	SqlSession session;
	
	@Override
	public int insert(MemberVO member) {
		return session.insert("com.project.heyScheduler.member.insertMember", member);
	}

	@Override
	public MemberVO select(MemberVO member) {
		return session.selectOne("com.project.heyScheduler.member.selectMember", member);
	}
}
