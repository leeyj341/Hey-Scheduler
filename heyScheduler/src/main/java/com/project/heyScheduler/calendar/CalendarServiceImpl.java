package com.project.heyScheduler.calendar;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CalendarServiceImpl implements CalendarService {
	@Autowired
	@Qualifier("CalendarDAO")
	CalendarDAO dao;
	
	@Override
	public List<PlanVO> selectPlan(PlanVO vo) {
		return dao.selectPlan(vo);
	}
	
	@Override
	public List<PlanVO> selectPlansOnDay(PlanVO vo) {
		return dao.selectPlansOnDay(vo);
	}
	
	@Override
	public PlanVO selectPlanDetail(String plan_no) {
		return dao.selectPlanDetail(plan_no);
	}
	
	@Override
	public int updatePlanDetail(PlanVO planItem) {
		return dao.updatePlanDetail(planItem);
	}

	@Override
	public int insertPlan(PlanVO vo) {
		return dao.insertPlan(vo);
	}
	
}
