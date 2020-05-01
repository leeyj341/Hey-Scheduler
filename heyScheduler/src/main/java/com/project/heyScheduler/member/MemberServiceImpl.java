package com.project.heyScheduler.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
	@Autowired
	@Qualifier("MemberDAO")
	MemberDAO dao;
	
	@Override
	public int insert(MemberVO member) {
		return dao.insert(member);
	}
	
	@Override
	public MemberVO select(MemberVO member) {
		return dao.select(member);
	}

}
