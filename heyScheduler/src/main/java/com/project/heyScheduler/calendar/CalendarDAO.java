package com.project.heyScheduler.calendar;

import java.util.List;

public interface CalendarDAO {
	public List<PlanVO> selectPlan(PlanVO vo);
	public List<PlanVO> selectPlansOnDay(PlanVO vo);
	public PlanVO selectPlanDetail(String plan_no);
}
