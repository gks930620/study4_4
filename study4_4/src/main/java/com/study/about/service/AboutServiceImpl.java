package com.study.about.service;
import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import com.study.about.dao.IAboutDao;
import com.study.about.vo.AlcoholVO;
import com.study.about.vo.PianoVO;

@Service
public class AboutServiceImpl {
	@Inject
	IAboutDao aboutDao;
	public List<AlcoholVO> chAlcohol(){
		return aboutDao.getAlcoholList();
	}
	
	public List<PianoVO> chPiano(){
		return aboutDao.getPianoList();
	}
}
