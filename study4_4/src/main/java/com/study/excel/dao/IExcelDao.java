package com.study.excel.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.study.free.vo.FreeBoardSearchVO;
import com.study.free.vo.FreeBoardVO;
import com.study.member.vo.MemberSearchVO;
import com.study.member.vo.MemberVO;

@Mapper
public interface IExcelDao {
	public List<FreeBoardVO> getBoardList(FreeBoardSearchVO searchVO);
}
