package com.study.temp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.free.vo.FreeBoardVO;
@Controller
public class TempController {
	@RequestMapping(value = "/idcheck.wow",produces ="text/plain;charset=UTF-8" )
	@ResponseBody //@은 순서 상관없다
	public String idCheck(String id) {
		return "이제 한글도 보낼수 있다.";
	}
	
	
	
	
}
