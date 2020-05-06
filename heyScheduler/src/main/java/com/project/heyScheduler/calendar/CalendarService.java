package com.project.heyScheduler.calendar;

import java.util.List;

public interface CalendarService {
	public List<PlanVO> selectPlan(PlanVO vo);
	public List<PlanVO> selectPlansOnDay(PlanVO vo);
	public PlanVO selectPlanDetail(String plan_no);
	public int updatePlanDetail(PlanVO planItem);
	public int insertPlan(PlanVO vo);
}
