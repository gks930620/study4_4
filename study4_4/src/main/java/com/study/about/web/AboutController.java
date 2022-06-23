package com.study.about.web;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.study.about.service.AboutServiceImpl;
import com.study.about.vo.AlcoholVO;
import com.study.about.vo.PianoVO;

@Controller
public class AboutController {
	@Inject
	AboutServiceImpl aboutService;
	
	@RequestMapping(value="/about/aboutHome.wow")
	public String aboutHome() {
		return "about/aboutHome";
	}
	
	//jsp가 그대로 jqueryLoad 대상태그에 그대로
	@RequestMapping(value="/about/chProfile.wow")
	public String aboutChProfile(Model model) {
		return "about/chProfile";
	}
	@RequestMapping(value="/about/chAlcohol.wow")
	public String aboutChAlchohol(Model model) {
		List<AlcoholVO> alcoholList=aboutService.chAlcohol();
		model.addAttribute("alcoholList",alcoholList);
		return "about/chAlcohol";
	}
	
	@RequestMapping(value="/about/chPiano.wow")
	public String aboutChPiano(Model model) {
		List<PianoVO> pianoList=aboutService.chPiano();
		model.addAttribute("pianoList",pianoList);
		return "about/chPiano";
	}
	
	
}
