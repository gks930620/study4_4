package com.study.free.web;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.study.attach.dao.IAttachDao;
import com.study.attach.vo.AttachVO;
import com.study.code.service.CommCodeServiceImpl;
import com.study.code.service.ICommCodeService;
import com.study.code.vo.CodeVO;
import com.study.common.util.StudyAttachUtils;
import com.study.common.valid.Modify;
import com.study.common.vo.ResultMessageVO;
import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.exception.BizPasswordNotMatchedException;
import com.study.free.service.FreeBoardServiceImpl;
import com.study.free.service.IFreeBoardService;
import com.study.free.vo.FreeBoardSearchVO;
import com.study.free.vo.FreeBoardVO;

@Controller
public class FreeBoardController {

	@Inject
	IFreeBoardService freeBoardService;

	@Autowired // inject랑 똑같다~~~~
	ICommCodeService codeService;
	
	@Autowired
	StudyAttachUtils studyAttachUtils;
	
	

	@ModelAttribute("cateList")
	public List<CodeVO> cateList(){
		return codeService.getCodeListByParent("BC00");
	}
	

	@RequestMapping("/free/freeList.wow")
	public String freeList(Model model, @ModelAttribute("searchVO") FreeBoardSearchVO searchVO) {
		List<FreeBoardVO> freeBoardList = freeBoardService.getBoardList(searchVO);
		model.addAttribute("freeBoardList", freeBoardList);
		return "free/freeList";
	}

	@RequestMapping("/free/freeView.wow")
	public String freeView(Model model, @RequestParam(required = true) int boNo) {
		try {
			FreeBoardVO freeBoard = freeBoardService.getBoard(boNo);
			model.addAttribute("freeBoard", freeBoard);
			freeBoardService.increaseHit(boNo);
		} catch (BizNotFoundException enf) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			resultMessageVO.messageSetting(false, "글 찾기 실패", "글을 찾는데 실패했습니다. 해당 글이없습니다.", "/free/freeList.wow", "목록으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		} catch (BizNotEffectedException ene) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			resultMessageVO.messageSetting(false, "조회수증가실패", "조회수증가실패했습니다.", "/free/freeList.wow", "목록으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		}
		return "free/freeView";
	}

	@RequestMapping(value = "/free/freeEdit.wow", params = { "boNo" })
	public String freeEdit(Model model, int boNo) {
		try {
			FreeBoardVO freeBoard = freeBoardService.getBoard(boNo);
			model.addAttribute("freeBoard", freeBoard);
		} catch (BizNotFoundException enf) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			resultMessageVO.messageSetting(false, "글 찾기 실패", "글을 찾는데 실패했습니다. 해당 글이없습니다.", "/free/freeList.wow", "목록으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		}
		return "free/freeEdit";
	}
	
	
	@PostMapping("/free/freeModify.wow")
	public String freeModify(Model model
				,@ModelAttribute("freeBoard")
				 @Validated(value = {Modify.class,Default.class})FreeBoardVO freeBoard
				,BindingResult error 
				, @RequestParam(value = "boFiles",required = false)MultipartFile[] boFiles) throws IOException {
		if(error.hasErrors()) {
			return "free/freeEdit";
		}
		try {
			if(boFiles!=null) {
				List<AttachVO> attaches=
						studyAttachUtils.getAttachListByMultiparts(boFiles, "FREE", "free");
				//실제로 파일경로에 선택된 파일 올리고 List<AttachVO> return
				freeBoard.setAttaches(attaches);
			}
			
			
			freeBoardService.modifyBoard(freeBoard);
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			resultMessageVO.messageSetting(true, "글 수정 성공", "글을 수정했습니다.", "/free/freeList.wow", "목록으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		} catch (BizNotFoundException enf) {
			// 에러가 났을 때 freeView에 있는 너무 간단한 화면말고 message.jsp로 이동하자
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			resultMessageVO.messageSetting(false, "글 찾기 실패", "글을 찾는데 실패했습니다. 해당 글이없습니다.", "/free/freeList.wow", "목록으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		} catch (BizPasswordNotMatchedException epm) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			resultMessageVO.messageSetting(false, "글 수정 실패 ", "글을 수정하는데 실패했습니다. 비밀번호가 달라요", "/free/freeList.wow",
					"목록으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		} catch (BizNotEffectedException ene) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			resultMessageVO.messageSetting(false, "수정실패", "수정에 실패했습니다.", "/free/freeList.wow", "목록으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		}
	}


	@PostMapping("/free/freeDelete.wow")
	public String freeDelete(Model model, FreeBoardVO freeBoard) {
		try {
			freeBoardService.removeBoard(freeBoard);
			// message.jsp로 가도록
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			resultMessageVO.messageSetting(true, "글 삭제 성공", "글을 삭제했습니다.", "/free/freeList.wow", "목록으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		} catch (BizNotFoundException enf) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			resultMessageVO.messageSetting(false, "글 찾기 실패", "글을 찾는데 실패했습니다. 해당 글이없습니다.", "/free/freeList.wow", "목록으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		} catch (BizPasswordNotMatchedException epm) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			resultMessageVO.messageSetting(false, "글 수정 실패 ", "글을 수정하는데 실패했습니다. 비밀번호가 달라요", "/free/freeList.wow",
					"목록으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		} catch (BizNotEffectedException ene) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			resultMessageVO.messageSetting(false, "조회수증가실패", "조회수증가실패했습니다.", "/free/freeList.wow", "목록으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		}
	}

	@GetMapping("/free/freeForm.wow")
	public String freeForm(Model model
			,@ModelAttribute("freeBoard") FreeBoardVO freeBoard) {
		return "free/freeForm";
	}

	@PostMapping("/free/freeRegist.wow")
	public String freeRegist(Model model
			,@ModelAttribute("freeBoard")@Validated(Default.class)
			FreeBoardVO freeBoard
			,BindingResult error
			,@RequestParam(value = "boFiles",required = false)MultipartFile[] boFiles) throws IOException {
		if(error.hasErrors()) {
			return "free/freeForm";
		}
		
		try {
			if(boFiles!=null) {
				List<AttachVO> attaches=
						studyAttachUtils.getAttachListByMultiparts(boFiles, "FREE", "free");
				//실제로 파일경로에 선택된 파일 올리고 List<AttachVO> return  (파일업로드)
				freeBoard.setAttaches(attaches);
			}
			freeBoardService.registBoard(freeBoard);   //이미 파일은 업로드 됨. 업로드 된 파일정보를  DB에 AttachVO로 등록.  
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			resultMessageVO.messageSetting(true, "글 등록 성공", "글을 등록했습니다.", "/free/freeList.wow", "목록으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		} catch (BizNotEffectedException ebe) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			resultMessageVO.messageSetting(false, "등록실패", "등록에 실패했습니다.", "/free/freeList.wow", "목록으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		}
	}
	
	
}
