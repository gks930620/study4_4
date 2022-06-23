package com.study.excel.service;

import java.util.List;


import com.study.free.vo.FreeBoardSearchVO;
import com.study.free.vo.FreeBoardVO;
import com.study.member.vo.MemberSearchVO;
import com.study.member.vo.MemberVO;


public interface IExcelService {
	public List<FreeBoardVO> getBoardList(FreeBoardSearchVO searchVO);
	
}
