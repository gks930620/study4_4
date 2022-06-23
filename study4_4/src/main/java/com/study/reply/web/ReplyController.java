package com.study.reply.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartResolver;

import com.study.exception.BizAccessFailException;
import com.study.exception.BizNotFoundException;
import com.study.reply.service.IReplyService;
import com.study.reply.vo.ReplySearchVO;
import com.study.reply.vo.ReplyVO;
import com.sun.media.jfxmedia.Media;

@RestController
public class ReplyController {
	
	@Inject
	IReplyService replyService;
	
	
	@RequestMapping(value = "/reply/replyList.wow")
	public Map<String,Object> replyList(ReplySearchVO searchVO){
		List<ReplyVO> replyList=replyService.getReplyListByParent(searchVO);
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("result", true);
		map.put("data", replyList);
		map.put("size", replyList.size());
		return map;
	}
	
	@RequestMapping(value = "/reply/replyRegist.wow" )
	public Map<String,Object> replyRegist(ReplyVO reply){
		System.out.println(reply);
		replyService.registReply(reply);
		
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("result", true);
		map.put("msg", "등록성공했어요");
		return map;
	}
	
	@RequestMapping(value = "/reply/replyModify.wow")
	public Map<String,Object> replyModify(ReplyVO reply){
		Map<String, Object> map=new HashMap<String, Object>();
		try{
			replyService.modifyReply(reply);
			map.put("result", true);
			map.put("msg","수정성공");
		} catch (BizAccessFailException e) {
			map.put("result", false);
			map.put("msg","당신은 댓글을 쓴 사람이 아닙니다.");
		}catch (BizNotFoundException e) {
			map.put("result", false);
			map.put("msg","댓글이 없습니다.");
		}
		return map;
	}
	
	@RequestMapping(value = "/reply/replyDelete.wow")
	public Map<String,Object> replyDelete(ReplyVO reply){
		Map<String, Object> map=new HashMap<String, Object>();
		try{
			replyService.removeReply(reply);
			map.put("result", true);
			map.put("msg","삭제성공");
		} catch (BizAccessFailException e) {
			map.put("result", false);
			map.put("msg","당신은 댓글을 쓴 사람이 아닙니다.");
		}catch (BizNotFoundException e) {
			map.put("result", false);
			map.put("msg","댓글이 없습니다.");
		}
		return map;
	}
}
