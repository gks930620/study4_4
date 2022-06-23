package com.study.reply.vo;

import com.study.common.vo.PagingVO;

//검색보다는 더 보기 누를때마다 10개씩 추가하는용도+ 
// 어떤글의 댓글인지 확인하기위한용도
public class ReplySearchVO  extends  PagingVO{
	private String reCategory;
	private int reParentNo;
	public String getReCategory() {
		return reCategory;
	}
	public void setReCategory(String reCategory) {
		this.reCategory = reCategory;
	}
	public int getReParentNo() {
		return reParentNo;
	}
	public void setReParentNo(int reParentNo) {
		this.reParentNo = reParentNo;
	}
	
}
