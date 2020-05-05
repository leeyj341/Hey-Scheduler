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

	@Override
	public List<PlanVO> selectPlansOnDay(PlanVO vo) {
		return session.selectList("com.project.heyScheduler.calendar.selectPlansOnDay", vo);
	}
	
	@Override
	public PlanVO selectPlanDetail(String plan_no) {
		return session.selectOne("com.project.heyScheduler.calendar.selectPlanDetail", plan_no);
	}
	
	@Override
	public int updatePlanDetail(PlanVO planItem) {
		return session.update("com.project.heyScheduler.calendar.updatePlanDetail", planItem);
	}
}
