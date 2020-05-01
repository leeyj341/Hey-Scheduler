package com.project.heyScheduler.member;

import java.io.IOException;

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
public class MemberController {
	@Autowired
	MemberService service;
	
	@RequestMapping(value="/insert.do", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String insertTest(@RequestBody String memberInfo) {
		ObjectMapper mapper = new ObjectMapper();
		MemberVO member = null;
		try {
			System.out.println(memberInfo);
			member = mapper.readValue(memberInfo, MemberVO.class);
			System.out.println(member);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int resultInt = service.insert(member);
		String result = "";
		if(resultInt == 1) {
			result = "성공";
		} else if (resultInt == 0) {
			result = "실패";
		}
		
		return result;
	}
	
	@RequestMapping(value="/select.do", method=RequestMethod.POST, produces="applicaion/json;charset=UTF-8")
	public @ResponseBody String selectTest(@RequestBody String memberInfo) {
		ObjectMapper mapper = new ObjectMapper();
		MemberVO member = null;
		String result = "";
		try {
			member = mapper.readValue(memberInfo, MemberVO.class);
			MemberVO resultVO = service.select(member);
			result = mapper.writeValueAsString(resultVO);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return result;
	}
}
