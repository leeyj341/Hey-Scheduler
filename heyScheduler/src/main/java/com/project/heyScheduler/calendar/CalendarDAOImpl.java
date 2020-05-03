package com.project.heyScheduler.calendar;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("CalendarDAO")
public class CalendarDAOImpl implements CalendarDAO {
	@Autowired
	SqlSession session;

	@Override
	public List<PlanVO> selectPlan(PlanVO vo) {
		return session.selectList("com.project.heyScheduler.calendar.selectPlans", vo);
	}

}
