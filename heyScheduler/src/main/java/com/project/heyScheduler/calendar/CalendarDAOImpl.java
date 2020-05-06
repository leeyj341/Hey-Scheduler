package com.project.heyScheduler.calendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		List<String> guest_ids = session.selectList("com.project.heyScheduler.calendar.selectGuests", plan_no);
		PlanVO vo = session.selectOne("com.project.heyScheduler.calendar.selectPlanDetail", plan_no);
		vo.setGuest_ids((ArrayList<String>)guest_ids);
		return vo;
	}
	
	@Override
	public int updatePlanDetail(PlanVO planItem) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("plan_no", planItem.getPlan_no());
		map.put("list", planItem.getGuest_ids());
		String plan_no = planItem.getPlan_no();
		session.delete("com.project.heyScheduler.calendar.deleteGuests", plan_no);
		session.insert("com.project.heyScheduler.calendar.insertGuests", map);
		//session.update("com.project.heyScheduler.calendar.updateGuests", map);
		return session.update("com.project.heyScheduler.calendar.updatePlanDetail", planItem);
	}

	@Override
	public int insertPlan(PlanVO planItem) {
		session.insert("com.project.heyScheduler.calendar.insertPlan", planItem);
		/*List<GuestVO> guestlist = new ArrayList<GuestVO>();
		for(int i = 0; i<planItem.getGuest_ids().size();i++){
			GuestVO guest = new GuestVO(planItem.getPlan_no(), planItem.getGuest_ids().get(i)); 
			guestlist.add(guest);
		}*/
		session.insert("com.project.heyScheduler.calendar.insertGuest", planItem.getGuest_ids());
		return 0;
	}
}
