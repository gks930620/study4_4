package com.study.member.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.study.exception.BizDuplicateKeyException;
import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.exception.BizPasswordNotMatchedException;
import com.study.member.dao.IMemberDao;
import com.study.member.vo.MailAuthVO;
import com.study.member.vo.MemberSearchVO;
import com.study.member.vo.MemberVO;

@Service
public class MemberServiceImpl implements IMemberService {

	@Inject
	IMemberDao memberDao;

	@Override
	public List<MemberVO> getMemberList(MemberSearchVO searchVO) {
		int totalRowCount = memberDao.getTotalRowCount(searchVO);
		searchVO.setTotalRowCount(totalRowCount);
		searchVO.pageSetting();
		return memberDao.getMemberList(searchVO);
	}

	@Override
	public MemberVO getMember(String memId) throws BizNotFoundException {
		MemberVO vo = memberDao.getMember(memId);
		if (vo == null) {
			throw new BizNotFoundException();
		}
		return vo;
	}

	@Override
	public void modifyMember(MemberVO member) throws BizNotEffectedException, BizNotFoundException {
		MemberVO vo = memberDao.getMember(member.getMemId());
		if (vo == null) {
			throw new BizNotFoundException();
		}
		int cnt = memberDao.updateMember(member);
		if (cnt == 0)
			throw new BizNotEffectedException();
	}

	@Override
	public void removeMember(MemberVO member) throws BizNotEffectedException, BizNotFoundException {
		MemberVO vo = memberDao.getMember(member.getMemId());
		if (vo == null) {
			throw new BizNotFoundException();
		}
		int cnt = memberDao.deleteMember(member);
		if (cnt == 0)
			throw new BizNotEffectedException();
	}

	
	//context-datasource와 @Trasactional만 지정하면 Service단에 Trasaction 메소드 단위로 거는건 간단^^
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void registMember(MemberVO member) throws BizNotEffectedException, BizDuplicateKeyException {
		MemberVO vo = memberDao.getMember(member.getMemId());
		if (vo != null) {
			throw new BizDuplicateKeyException();
		}
		int cnt = memberDao.insertMember(member);
		if (cnt == 0)
			throw new BizNotEffectedException();
		int cnt2= memberDao.insertUserRole(member.getMemId());
		if(cnt2==0) throw new BizNotEffectedException();
		
		
		
	}
	
	
	
	@Override
	public void registMailAuth(String mail, String authKey) throws BizNotEffectedException {
		MailAuthVO mailAuth= memberDao.getMailAuth(mail);
		if(mailAuth==null)memberDao.insertMailAuth(mail, authKey);
		else {
			memberDao.updateMailAuth(mail, authKey);
		}
		
	}
	
	
	@Override
	public void authKeyCompare(MailAuthVO mailAuth) throws BizNotEffectedException, BizPasswordNotMatchedException {
		MailAuthVO vo=memberDao.getMailAuth(mailAuth.getMail()); //왠만하면 null안나옴. 자식창에서 확인바로누르면.
		//vo는 DB에 있는 거, mailAuth는 자식창에서 입력한 값
		if(!vo.getAuthKey().equals(mailAuth.getAuthKey())) {
			throw new BizPasswordNotMatchedException();
			//자식창에서 입력한 authKey가 다르면
		}
		if(vo.getAuthKey().equals(mailAuth.getAuthKey())) {  //인증번호 제대로 입력했으면
			int cnt=memberDao.completeAuth(mailAuth.getMail());
			if(cnt==0) throw new BizNotEffectedException();
		}
	}
	
	@Override
	public int isMailAuthed(String mail) {
		MailAuthVO mailAuth=memberDao.getMailAuth(mail);
		if(mailAuth==null) return 0;
		return mailAuth.getIsAuth();
	}
	
	
	

}
