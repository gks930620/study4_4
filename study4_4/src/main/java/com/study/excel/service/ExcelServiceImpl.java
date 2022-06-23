package com.study.excel.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.study.excel.dao.IExcelDao;
import com.study.free.vo.FreeBoardSearchVO;
import com.study.free.vo.FreeBoardVO;
import com.study.member.vo.MemberSearchVO;
import com.study.member.vo.MemberVO;

@Service
public class ExcelServiceImpl  implements IExcelService{
	@Inject
	IExcelDao excelDao;
	
	@Override
	public List<FreeBoardVO> getBoardList(FreeBoardSearchVO searchVO) {
		return excelDao.getBoardList(searchVO);
	}


}
