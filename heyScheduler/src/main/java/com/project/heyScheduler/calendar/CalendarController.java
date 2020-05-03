package com.project.heyScheduler.calendar;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class CalendarController {
	@Autowired
	CalendarService service;

	@RequestMapping(value="/calendar/select.do", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String selectPlans(@RequestBody String dateInfo) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			PlanVO vo = mapper.readValue(dateInfo, PlanVO.class);
			ArrayList<PlanVO> list = (ArrayList<PlanVO>) service.selectPlan(vo);
			System.out.println(list);
			json = mapper.writeValueAsString(list);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch blockcm,
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(json);
		
		return json;
	}
}
