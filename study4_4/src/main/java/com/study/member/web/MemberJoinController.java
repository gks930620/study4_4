package com.study.member.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;
import javax.print.DocFlavor.STRING;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.study.code.service.ICommCodeService;
import com.study.code.vo.CodeVO;
import com.study.common.util.CookieUtils;
import com.study.common.valid.Step1;
import com.study.common.valid.Step2;
import com.study.common.valid.Step3;
import com.study.common.vo.ResultMessageVO;
import com.study.exception.BizDuplicateKeyException;
import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.free.vo.FreeBoardVO;
import com.study.member.service.IMemberService;
import com.study.member.service.MailSendService;
import com.study.member.vo.MailAuthVO;
import com.study.member.vo.MemberVO;

@Controller
@SessionAttributes("member")
public class MemberJoinController {
	// 메소드 내에서 @modelAttribute("member")는 session에 있는 model에 "member" 가
	// 있으면 새로 생성안하고 있는거 사용
	// 없으면 에러
	// @SessionAttributes("member")는 Controller안에서만 유지되는 session

	// step1.wow 처음 들어가기전에 모델에 "member" 저장 , session Attribute에 member 저장
	@ModelAttribute("member")
	public MemberVO member() {
		return new MemberVO();
	}

	@Inject
	IMemberService memberService;

	@Inject
	ICommCodeService codeService;

	@ModelAttribute("jobList")
	public List<CodeVO> jobList() {
		return codeService.getCodeListByParent("JB00");
	}

	@ModelAttribute("hobbyList")
	public List<CodeVO> hobbyList() {
		return codeService.getCodeListByParent("HB00");
	}

	@RequestMapping("/join/step1.wow")
	public String step1(@ModelAttribute("member") MemberVO member) {
		return "join/step1";
	}

	@RequestMapping("/join/step2.wow")
	public String step2(@ModelAttribute("member") @Validated(value = { Step1.class }) MemberVO member,
			BindingResult error) {
		if (error.hasErrors()) {
			return "join/step1";
		}

		return "join/step2";
	}

	@RequestMapping("/join/step3.wow")
	public String step3(@ModelAttribute("member") @Validated(value = { Step2.class }) MemberVO member,
			BindingResult error) {
		if (error.hasErrors()) {
			return "join/step2";
		}

		return "join/step3";
	}

	@RequestMapping("/join/regist.wow")
	public String regist(Model model, @ModelAttribute("member") @Validated(value = { Step3.class }) MemberVO member,
			BindingResult error, SessionStatus sessionStatus) {
		if (error.hasErrors()) {
			return "join/step3";
		}
		// 검사통과 => insert하면 됩니다.
		try {
			memberService.registMember(member);
			sessionStatus.setComplete();

			ResultMessageVO resultMessageVO = new ResultMessageVO();
			resultMessageVO.messageSetting(true, "회원 등록 성공 ", "회원을 등록했습니다.", "/member/memberList.wow", "목록으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		} catch (BizNotEffectedException ene) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			resultMessageVO.messageSetting(false, "회원 삭제 실패", "회원을 삭제하는데 실패했습니다.", "/member/memberList.wow", "목록으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		} catch (BizDuplicateKeyException ede) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			resultMessageVO.messageSetting(false, "회원 등록 실패", "회원아이디가 이미 존재합니다.", "/member/memberList.wow", "목록으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		}
	}

	@RequestMapping("/join/cancel")
	public String cancel(SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		
		return "home";
	}

	@RequestMapping(value = "/join/idCheck.wow", produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String idCheck(String id) {
		MemberVO member = null;
		try {
			member = memberService.getMember(id);
			return "중복";
		} catch (BizNotFoundException e) {
			return "사용가능";
		}
	}

	
	
	//3분제한은 쿠키로 하면 될텐데.. 너무 어렵다.
	
	@Inject
	MailSendService mailSendService;

	@RequestMapping("/join/mailAuth.wow")
	@ResponseBody
	public String mailAuth(String mail, HttpServletResponse resp) throws Exception {
		String authKey = mailSendService.sendAuthMail(mail); //사용자가 입력한 메일주소로 메일을 보냄
		memberService.registMailAuth(mail, authKey);	//메일보냄과 동시에 DB저장	
		return authKey;
	}
	

	
	// 자식창 생성
	@RequestMapping("/join/mailWindow.wow")
	public String mailWindow(String mail, HttpServletResponse resp) throws Exception {
		return "join/mailWindow";  //step2.jsp화면에서 mail인증하기 버튼 누르면 자식창생김. 거기서 사용자는 authKey입력
	}
	//자식창에서 authKey맞는지확인
	@RequestMapping(value="/join/authKeyCompare.wow", produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String authKeyCompare(MailAuthVO mailAuth) {
		try{
			memberService.authKeyCompare(mailAuth); //authKey는 사용자가 입력한 값
			return "authKeySame";
		} catch (Exception e) {
			return "somethingWrong";
		}
	}
	//다음버튼눌렀을 때 isAuth=1인지 확인
	@RequestMapping(value="/join/isMailAuthed.wow" ,produces = "text/plain;charset=utf-8")
	@ResponseBody
	public String isMailAuthed(String mail) {
		int isAuth= memberService.isMailAuthed(mail);
		if(isAuth==1) return "메일인증완";
		else return "인증안됨";
	}

}
