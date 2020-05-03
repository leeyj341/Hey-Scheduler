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
}
