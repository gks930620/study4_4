package com.study.about.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PianoVO {
	private int pNo;                        
	private String pTitle;                  
	private String pDate;                   
	private String pComposer;               
	private String pCategory;
	
	 @Override 
	  public String toString() {
		  return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE); 
	}

	public int getpNo() {
		return pNo;
	}

	public void setpNo(int pNo) {
		this.pNo = pNo;
	}

	public String getpTitle() {
		return pTitle;
	}

	public void setpTitle(String pTitle) {
		this.pTitle = pTitle;
	}

	public String getpDate() {
		return pDate;
	}

	public void setpDate(String pDate) {
		this.pDate = pDate;
	}

	public String getpComposer() {
		return pComposer;
	}

	public void setpComposer(String pComposer) {
		this.pComposer = pComposer;
	}

	public String getpCategory() {
		return pCategory;
	}

	public void setpCategory(String pCategory) {
		this.pCategory = pCategory;
	}
	 
	 
	 
}
