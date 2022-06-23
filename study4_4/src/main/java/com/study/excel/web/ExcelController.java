package com.study.excel.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.study.common.util.SimpleExcelFile;
import com.study.excel.service.IExcelService;
import com.study.free.vo.FreeBoardSearchVO;
import com.study.free.vo.FreeBoardVO;
import com.study.member.vo.MemberSearchVO;
import com.study.member.vo.MemberVO;

@Controller
public class ExcelController {
	@Inject
	IExcelService excelService;

		@RequestMapping("/free/excelDown")
		public void excelDownload(HttpServletResponse response, @ModelAttribute("searchVO")FreeBoardSearchVO searchVO) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
			//엑셀에 실제로 담을 데이터  페이징 없이 전체 데이터를  list로 
			List<FreeBoardVO> freeBoardList=excelService.getBoardList(searchVO);
				
			String sheetName="";
			if( StringUtils.isNotBlank(searchVO.getSearchWord())        ) {
				sheetName=searchVO.getSearchType()+"_"+searchVO.getSearchWord();
			}
			if( StringUtils.isNotBlank(searchVO.getSearchCategory()) ) {
				if(StringUtils.isNotBlank(searchVO.getSearchWord())) {
					sheetName=sheetName+"_"+searchVO.getSearchCategory();
				}else {
					sheetName=sheetName+searchVO.getSearchCategory();
				}
			}
			if(StringUtils.isBlank(searchVO.getSearchCategory()) && StringUtils.isBlank(searchVO.getSearchWord())) {
				sheetName="noSearch";
			}
			
			
			SimpleExcelFile simpleExcelFile= new SimpleExcelFile(sheetName, FreeBoardVO.class, freeBoardList);
			//생성자에서 sheet이름, columnName, Body생성
			 
				
			// 컨텐츠 타입과 파일명 지정
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=freeBoard.xlsx");
			simpleExcelFile.write(response.getOutputStream());
		}
		
		

}

//@RequestMapping("/member/excelDown")
//public void excelMemberDown(HttpServletResponse response, @ModelAttribute("searchVO")MemberSearchVO searchVO) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
//	//엑셀에 실제로 담을 데이터  페이징 없이 전체 데이터를  list로 
//	List<MemberVO> memberList=excelService.getMemberList(searchVO); 
//	SimpleExcelFile simpleExcelFile= new SimpleExcelFile("memberList", MemberVO.class, memberList);
//	//생성자에서 sheet이름, columnName, Body생성
//	
//		
//	// 컨텐츠 타입과 파일명 지정
//	response.setContentType("ms-vnd/excel");
//	//response.setHeader("Content-Disposition", "attachment;filename=example.xls");
//	response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");
//	simpleExcelFile.write(response.getOutputStream());
//}
