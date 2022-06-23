package com.study.member.vo;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.xml.ws.BindingType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.study.common.valid.Modify;
import com.study.common.valid.Step1;
import com.study.common.valid.Step2;
import com.study.common.valid.Step3;

public class MemberVO {

	@NotEmpty(groups = {Modify.class,Step2.class},message = "id는 필수입니다.")
	private String memId;         /* 회원 아이디 */
	@NotEmpty(groups = {Modify.class,Step2.class}, message = "비밀번호는 필수입니다.")
	private String memPass;       /* 회원 비밀번호 */
	@NotEmpty(groups= {Modify.class,Step2.class}, message = "이름은 필수입니다")
	private String memName;       /* 회원 이름 */
	
	@NotEmpty(groups = {Modify.class,Step2.class},message = "이메일은 필수입니다.")
	@Email(groups = {Modify.class,Step2.class}, message = "이메일형식에 맞춰주세요" )
	private String memMail;       /* 이메일 */
	
	
	@NotEmpty(groups = {Modify.class,Step3.class}, message = "생일은 필수입니다.")
	private String memBir;        /* 회원 생일 */
	@NotEmpty(groups = {Modify.class,Step3.class}, message = "우편번호는 필수입니다.")
	private String memZip;        /* 우편번호 */
	@NotEmpty(groups = {Modify.class,Step3.class}, message = "주소는 필수입니다.")
	private String memAdd1;       /* 주소 */
	@NotEmpty(groups = {Modify.class,Step3.class}, message = "상세주소는 필수입니다.")
	private String memAdd2;       /* 상세주소 */
	
	@NotEmpty(groups = {Modify.class,Step3.class},message = "연락처는 필수입니다.")
	@Pattern(groups = {Modify.class,Step3.class}
	,regexp = "^\\d{3}-\\d{3,4}-\\d{4}$"
	,message = "연락처형태가 다릅니다")
	private String memHp;         /* 연락처 */
	  
	
	@NotEmpty(groups = {Modify.class,Step3.class}, message = "직업은 필수입니다.")
	private String memJob;        /* 직업 코드 */
	@NotEmpty(groups = {Modify.class,Step3.class}, message = "취미는 필수입니다.")
	private String memHobby;       /* 취미 코드 */ 
	
	private int memMileage;       /* 마일리지 */
	private String memDelYn;     /* 탈퇴여부 */
	private String memJobNm;
	private String memHobbyNm;
	
	//join
	
	@NotEmpty(groups = {Step1.class}, message ="이용약관 동의는 필수입니다." )
	private String agreeYn;
	@NotEmpty(groups = {Step1.class}, message ="개인정보이용 동의는 필수입니다." )
	private String privacyYn;
	private String eventYn;  
	// DB에 저장, Y이면 가끔 문자 보내주고 N이면 문자 안보내고.. 근데 난 안함
	
	
	
	
	
	
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	public String getMemDelYn() {
		return memDelYn;
	}
	public void setMemDelYn(String memDelYn) {
		this.memDelYn = memDelYn;
	}
	public String getMemId() {
		return memId;
	}
	public void setMemId(String memId) {
		this.memId = memId;
	}
	public String getMemPass() {
		return memPass;
	}
	public void setMemPass(String memPass) {
		this.memPass = memPass;
	}
	public String getMemName() {
		return memName;
	}
	public void setMemName(String memName) {
		this.memName = memName;
	}
	public String getMemBir() {
		return memBir;
	}
	public void setMemBir(String memBir) {
		this.memBir = memBir;
	}
	public String getMemZip() {
		return memZip;
	}
	public void setMemZip(String memZip) {
		this.memZip = memZip;
	}
	public String getMemAdd1() {
		return memAdd1;
	}
	public void setMemAdd1(String memAdd1) {
		this.memAdd1 = memAdd1;
	}
	public String getMemAdd2() {
		return memAdd2;
	}
	public void setMemAdd2(String memAdd2) {
		this.memAdd2 = memAdd2;
	}
	public String getMemHp() {
		return memHp;
	}
	public void setMemHp(String memHp) {
		this.memHp = memHp;
	}
	public String getMemMail() {
		return memMail;
	}
	public void setMemMail(String memMail) {
		this.memMail = memMail;
	}
	public String getMemJob() {
		return memJob;
	}
	public void setMemJob(String memJob) {
		this.memJob = memJob;
	}
	public String getMemHobby() {
		return memHobby;
	}
	public void setMemHobby(String memHobby) {
		this.memHobby = memHobby;
	}
	public int getMemMileage() {
		return memMileage;
	}
	public void setMemMileage(int memMileage) {
		this.memMileage = memMileage;
	}
	public String getMemJobNm() {
		return memJobNm;
	}
	public void setMemJobNm(String memJobNm) {
		this.memJobNm = memJobNm;
	}
	public String getMemHobbyNm() {
		return memHobbyNm;
	}
	public void setMemHobbyNm(String memHobbyNm) {
		this.memHobbyNm = memHobbyNm;
	}
	public String getAgreeYn() {
		return agreeYn;
	}
	public void setAgreeYn(String agreeYn) {
		this.agreeYn = agreeYn;
	}

	
	
	public String getPrivacyYn() {
		return privacyYn;
	}
	public void setPrivacyYn(String privacyYn) {
		this.privacyYn = privacyYn;
	}
	public String getEventYn() {
		return eventYn;
	}
	public void setEventYn(String eventYn) {
		this.eventYn = eventYn;
	}



	
	
	
	
	
}
