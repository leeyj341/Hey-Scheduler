package com.project.heyScheduler.calendar;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
	
	@RequestMapping(value="/calendar/insert.do", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String insertPlans(@RequestBody String planData){
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			PlanVO planItem = mapper.readValue(planData, PlanVO.class);
			
			int resultInt = service.insertPlan(planItem);
			String result = "";
			if(resultInt == 1) {
				result = "삽입에 성공했습니다.";
			} else {
				result = "삽입에 실패했습니다.";
			}
			json = mapper.writeValueAsString(result);
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
		
		return json;
	}
	

	@RequestMapping(value="/calendar/select.do", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String selectPlans(@RequestBody String dateInfo) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			PlanVO vo = mapper.readValue(dateInfo, PlanVO.class);
			ArrayList<PlanVO> list = (ArrayList<PlanVO>) service.selectPlan(vo);
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
		
		return json;
	}
	
	@RequestMapping(value="/calendar/selectPlansOnDay.do", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String selectPlansOnDay(@RequestBody String dateInfo) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			PlanVO vo = mapper.readValue(dateInfo, PlanVO.class);
			ArrayList<PlanVO> list = (ArrayList<PlanVO>) service.selectPlansOnDay(vo);;
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
		
		return json;
	}
	
	@RequestMapping(value="/calendar/selectPlanDetail.do", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String selectPlanDetail(@RequestBody String plan_no) {
		JSONParser jsonParser = new JSONParser();
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			JSONObject jsonObj = (JSONObject) jsonParser.parse(plan_no);
			PlanVO vo = service.selectPlanDetail(jsonObj.get("plan_no").toString());
			json = mapper.writeValueAsString(vo);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch blockcm,
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(json);
		
		return json;
	}
	
	@RequestMapping(value="/calendar/modify.do", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String updatePlanDetail(@RequestBody String planData) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			PlanVO planItem = mapper.readValue(planData, PlanVO.class);
			int resultInt = service.updatePlanDetail(planItem);
			String result = "";
			if(resultInt == 1) {
				result = "수정에 성공했습니다.";
			} else {
				result = "수정에 실패했습니다.";
			}
			json = mapper.writeValueAsString(result);
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
		
		return json;
	}
}
