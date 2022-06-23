package com.study.member.service;

import java.util.List;

import com.study.exception.BizDuplicateKeyException;
import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.exception.BizPasswordNotMatchedException;
import com.study.member.vo.MailAuthVO;
import com.study.member.vo.MemberSearchVO;
import com.study.member.vo.MemberVO;

public interface IMemberService {
	
	public List<MemberVO> getMemberList(MemberSearchVO searchVO);
	public MemberVO getMember(String memId) throws BizNotFoundException ;
	public void modifyMember(MemberVO member) throws BizNotEffectedException, BizNotFoundException ;	
	public void removeMember(MemberVO member) throws BizNotEffectedException, BizNotFoundException;
	public void registMember(MemberVO member) throws BizNotEffectedException,BizDuplicateKeyException;
	
	public void registMailAuth(String mail, String authKey) throws BizNotEffectedException;//부모창 mail 확인하기, 확인하기버튼 누르는 순간 인증키랑 mail 저장
	public void authKeyCompare(MailAuthVO mailAuth) throws BizNotEffectedException ,BizPasswordNotMatchedException;
	//자식창 확인버튼, 사용자가 메일에서 인증번호 입력하면  DB에 있는 내용이랑 비교. 같으면 is_auth=1로 바꿔서 인증
	public int isMailAuthed(String mail) ; // 부모창 다음버튼, is_auth=1이면 1리턴해서 인증되었다는걸 알림
	
}
