package com.study.member.dao;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import com.study.member.vo.MailAuthVO;
import com.study.member.vo.MemberSearchVO;
import com.study.member.vo.MemberVO;
@Mapper
public interface IMemberDao {

	public int getTotalRowCount(MemberSearchVO searchVO);
	public List<MemberVO> getMemberList(MemberSearchVO searchVO);
	public MemberVO getMember(String memId);
	public int updateMember(MemberVO member);
	public int deleteMember(MemberVO member);
	public int insertMember(MemberVO member);
	
	public String getUserRoleByMemId(String memId);
	public int insertUserRole(String memId);
	
	
	public int insertMailAuth(@Param("mail")String mail,@Param("authKey")String authKey);
	public MailAuthVO getMailAuth(String mail);   //여기저기서 쓰임. 인증되었는지 확인하는데에도 쓰임
	public int updateMailAuth(@Param("mail")String mail,@Param("authKey")String authKey); //인증키 재발급+같은메일로다시가입할때
	//부모창 mail인증하기 버튼
	
	public int completeAuth(String mail);  //인증완, 자식창 확인버튼
	
	

}
