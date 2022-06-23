package com.study.exception;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Example {
	public static void main(String[] args) {
		// 1. 예외 블랙홀...    : 여러분들 회사 그만두고 싶을 때 
		try {
			//예외가 발생하는 부분
			 String a=null;
			 a.substring(0);
		}catch (Exception e) {
		}
		
		//2. 아무것도 처리안하고 보기만하자.  
		// 주기적으로 콘솔은 보는 관리자가 있지 않으면 좋은방법이 아닙니다.
		try {
			 String a=null;
			 a.substring(0);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//3. 에러났을 때 적절히 처리
		try {
			 String a=null;
			 a.substring(0);
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("a가 null입니다 a에게 객체를 전달해주세요");
			String a="적절히 생성된 객체";
			a.substring(0);
		}
		
		//3.1 에러 변환
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			conn=DriverManager.getConnection("연결");
			pstmt=conn.prepareStatement
					(" Select bo_no,bo_title from member ");
			rs=pstmt.executeQuery();
			while(rs.next()) {
				rs.getInt("bo_no"); rs.getString("boTitle");
			}
		}catch (SQLException  e) {
			// e.printStatckTtrace()하고 뒷내용이 실행되는게 아니라
			// 프로그램 자체가 멈췄으면 좋겠어요...  에러를 던져서 프로그램을 멈추자
			//SQLExcpetion에서 던질 에러로 DaoException을 만들었습니다...
			throw new DaoException("DAO단 에러",e);
			
		}
		
		//3.1 에러변환 
		/*try {
			//입력한 비밀번호랑 DB에 있는 비밀번호가 다를 경우
			 if(free.getBoPass.equals( vo.getBoPass())) {
				 //코드상 에러가 아니고 개발자가 생각하기에 문제가있는경우-service단
				 //에러를 임시로 발생시킬수도 있다
				 // 그때 던지는 exception으로 BizException을 만들었어요.
				 //throw new BizException();
				 throw new BizPasswordNotMatchedException();
			 }*/
		}
	
}
