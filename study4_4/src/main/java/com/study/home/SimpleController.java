package com.study.home;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SimpleController {
	@RequestMapping("/abcd/eee")
	public String eee(Model model,HttpServletRequest req) {
		System.out.println("여기 실행됩니다");
		model.addAttribute("eee","이거슨 eee에서 보내는 데이터입니다.");
		req.setAttribute("url", req.getRequestURL());
		//앞으로 데이터 여기서 담을때는 model에 담습니다.
		return "eee";
	}	
	
	@Inject
	SimpleServiceImpl simpleService;
	
	@RequestMapping("/abcd/fff")
	public String fff(Model model) {
		System.out.println("여기 실행됩니다");
		String data=simpleService.getSimple();
		model.addAttribute("data",data);
		return "fff";
	}	
	
	
	
}






