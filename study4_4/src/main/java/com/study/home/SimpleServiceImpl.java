package com.study.home;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

@Service
public class SimpleServiceImpl {

	@Inject
	SimpleDaoOracle simpleDao;
	
	public String getSimple() {
		return simpleDao.getData();
	}
	
}
