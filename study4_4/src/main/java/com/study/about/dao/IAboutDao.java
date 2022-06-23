package com.study.about.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.study.about.vo.AlcoholVO;
import com.study.about.vo.PianoVO;

@Mapper
public interface IAboutDao {
	public List<AlcoholVO> getAlcoholList();
	public List<PianoVO> getPianoList();
}



