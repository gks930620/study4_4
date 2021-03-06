package com.study.free.service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import com.study.attach.dao.IAttachDao;
import com.study.attach.vo.AttachVO;
import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.exception.BizPasswordNotMatchedException;
import com.study.free.dao.IFreeBoardDao;
import com.study.free.vo.FreeBoardSearchVO;
import com.study.free.vo.FreeBoardVO;

@Service
public class FreeBoardServiceImpl implements IFreeBoardService {

	@Inject
	IFreeBoardDao freeBoardDao;
	
	@Inject
	IAttachDao attachDao;
	

	@Override
	public List<FreeBoardVO> getBoardList(FreeBoardSearchVO searchVO) {
		int totalRowCount = freeBoardDao.getTotalRowCount(searchVO);
		searchVO.setTotalRowCount(totalRowCount);
		searchVO.pageSetting();
		return freeBoardDao.getBoardList(searchVO);

	}

	@Override
	public FreeBoardVO getBoard(int boNo) throws BizNotFoundException {
		FreeBoardVO freeBoard = freeBoardDao.getBoard(boNo);
		if (freeBoard == null) {
			throw new BizNotFoundException();
		}
		// resultMap하면 이미 attaches 가 세팅되어있기 때문에 필요없다.
//		List<AttachVO> attaches = attachDao.getAttachListByParent(boNo,"FREE");
//		freeBoard.setAttaches(attaches);
		return freeBoard;
	}

	@Override
	public void increaseHit(int boNo) throws BizNotEffectedException {
		int cnt = freeBoardDao.increaseHit(boNo); // update된 행 수가 return 됨
		if (cnt == 0) { // 업데이트가 제대로 안됬다.. 근데 사실 일어날수가 없는 일인데..
			throw new BizNotEffectedException();
		}
	}

	@Override
	public void modifyBoard(FreeBoardVO freeBoard)
			throws BizNotFoundException, BizPasswordNotMatchedException, BizNotEffectedException {
		FreeBoardVO vo = freeBoardDao.getBoard(freeBoard.getBoNo());
		// vo는 DB에 있는 데이터
		if (vo == null)
			throw new BizNotFoundException();
		if (freeBoard.getBoPass().equals(vo.getBoPass())) {
			int cnt = freeBoardDao.updateBoard(freeBoard);
			if (cnt == 0)
				throw new BizNotEffectedException();
			List<AttachVO> attaches= freeBoard.getAttaches();
			if(attaches !=null) {
				for(AttachVO attach : attaches) {
					attach.setAtchParentNo(freeBoard.getBoNo());
					attachDao.insertAttach(attach);
				}
			}
			
			
			// 휴지통버튼 누른 첨부파일들 DB에서 삭제
			int delAtchNos[] = freeBoard.getDelAtchNos(); // 애초에 파라미터로 올때 세팅이됩니다.
			if (delAtchNos != null && delAtchNos.length >0) {
				attachDao.deleteAttaches(delAtchNos);  
			}
			
			
		} else {
			throw new BizPasswordNotMatchedException();
		}

	}

	@Override
	public void removeBoard(FreeBoardVO freeBoard)
			throws BizNotFoundException, BizPasswordNotMatchedException, BizNotEffectedException {
		FreeBoardVO vo = freeBoardDao.getBoard(freeBoard.getBoNo());
		if (vo == null)
			throw new BizNotFoundException();
		if (freeBoard.getBoPass().equals(vo.getBoPass())) {
			int cnt = freeBoardDao.deleteBoard(freeBoard);
			if (cnt == 0)
				throw new BizNotEffectedException();
		} else {
			throw new BizPasswordNotMatchedException();
		}

	}

	@Override
	public void registBoard(FreeBoardVO freeBoard) throws BizNotEffectedException {
		int cnt = freeBoardDao.insertBoard(freeBoard);
		if (cnt == 0)
			throw new BizNotEffectedException();
		
		//여기서하는이유는 regist했을 때 atchParentNo에 아직 boNo가 없기 때문이지....
		List<AttachVO> attaches = freeBoard.getAttaches();
		if (attaches != null) {
			for (AttachVO attach : attaches) {
				attach.setAtchParentNo(freeBoard.getBoNo());
				attachDao.insertAttach(attach);
			}
		}
	}
}
