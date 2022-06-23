package com.study.about.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AlcoholVO {
	private int alNo;                       
	private String alName;                  
	private int alPercent;                  
	private String alCategory;              
	
	 @Override 
	  public String toString() {
		  return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE); 
	}

	public int getAlNo() {
		return alNo;
	}

	public void setAlNo(int alNo) {
		this.alNo = alNo;
	}

	public String getAlName() {
		return alName;
	}

	public void setAlName(String alName) {
		this.alName = alName;
	}

	public int getAlPercent() {
		return alPercent;
	}

	public void setAlPercent(int alPercent) {
		this.alPercent = alPercent;
	}

	public String getAlCategory() {
		return alCategory;
	}

	public void setAlCategory(String alCategory) {
		this.alCategory = alCategory;
	}
	 
	 
}
