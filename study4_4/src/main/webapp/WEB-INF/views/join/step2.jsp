<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@include file="/WEB-INF/inc/header.jsp"%>
<title>회원가입 2단계</title>
</head>
<body>
	<%@include file="/WEB-INF/inc/top.jsp"%>
	<div class="container">
		<form:form modelAttribute="member" action="step3.wow" method="post">
			<div class="row col-md-8 col-md-offset-2">
				<div class="page-header">
					<h3>회원가입 2단계</h3>
				</div>
				<table class="table">
					<colgroup>
						<col width="20%" />
						<col />
					</colgroup>
					<tr>
						<th>ID</th>
						<td><form:input path="memId" cssClass="form-control input-sm" />
							<form:errors path="memId" />
							<button onclick="return false;" type="button" id="idCheck">아이디중복확인</button>
						</td>
					</tr>
					<tr>
						<th>비밀번호</th>
						<td><form:password path="memPass"
								cssClass="form-control input-sm" /> <form:errors path="memPass" />
						</td>
					</tr>

					<tr class="form-group-sm">
						<th>회원명</th>
						<td><form:input path="memName"
								cssClass="form-control input-sm" /> <form:errors path="memName" />
						</td>
					</tr>
					<tr class="form-group-sm">
						<th>이메일</th>
						<td><form:input path="memMail" 	cssClass="form-control input-sm" />
							<form:errors path="memMail" />
							<button onclick="return false;" type="button" id="mailAuth">mail인증하기</button>
						</td>		 
					</tr>
					<tr>
						<td colspan="2">
							<div class="pull-left">
								<a href="cancel" class="btn btn-sm btn-default"> <span
									class="glyphicon glyphicon-remove" aria-hidden="true"></span>
									&nbsp;&nbsp;취 소
								</a>
							</div>
							<div class="pull-right">
								<button type="submit" class="btn btn-sm btn-primary" >
									<span class="glyphicon glyphicon-chevron-right"
										aria-hidden="true"></span> &nbsp;&nbsp;다 음
								</button>
							</div>
						</td>
					</tr>
				</table>

			</div>
		</form:form>
	</div>
	<!-- END : 메인 콘텐츠  컨테이너  -->
</body>
<script type="text/javascript">
	$(document).ready(function(){
		var isIdChecked=false;
		var isMailAuthed=false;   //DB상관없이 무조건   mail인증하기버튼 누르도록 하기위해..
		
		$("#idCheck").on("click", function(e){
			e.preventDefault();   //form태그안의 있는 버튼이라 그냥 submit되는거 방지
			$.ajax({
				url : "<c:url value='idCheck.wow'  />" 
				,data : {"id" : $("input[name='memId']").val()}
				,success :function(data){
					if(data=="사용가능") isIdChecked=true;
					alert(data);
				},error : function(req,status,err){
					console.log(req);
				}
			}); //ajax
		});// idCheck
		$("input[name='memId']").on("change", function(e){
			isIdChecked=false;
		});//memId값 바뀔 때마다
		
		
		
		//form태그내의 버튼 클릭   (일부러 idCheck, email인증 버튼은 type을 button으로 했음. 어찌됐든 다음버튼 클릭 이벤트가 되도록 jquery 잘 쓰기)
		$("button[type=submit]").click(function(e) {
			e.preventDefault();
			if(isIdChecked){
				if(isMailAuthed){  // 인증확인 버튼은 무조건 눌러야 된다. 맨 처음에..  vaildation에 의해 돌아왔어도 버튼은 누르도록하자.
					//ajax로 email 인증됐나 확인
					$.ajax({
						url :"<c:url value='isMailAuthed.wow' />"
						,type : "POST"
						,data : {"mail" : $("input[name=memMail]").val()}
						,success: function(data){
							//자식창에서 인증번호 잘 눌러서 DB에 is_auth=1로 되어있는지 확인
							if(data=="메일인증완")$("form").submit(); 
							else alert("mail인증해주세요");    
						},error : function(req,st,err){
							
						}
					});//ajax
				}else{
					alert("메일인증해주세요")
				}
			}else {
				alert("id 중복체크를 해주세요");
			}
		}); //다음버튼
	
		//mail인증하기 버튼 클릭 
		$("#mailAuth").on("click",function(e){
			isMailAuthed=true;  //다음버튼 누를 수 있게.
			$.ajax({
				url : "<c:url value='mailAuth.wow' />" 
				,data : {"mail" : $("input[name='memMail']").val()}
				,success: function(data){ //메일보내고 DB에 넣었으면 자식창 띄우기 //팝업해제해야된다..
					var mailWindow=window.open("<c:url value='mailWindow.wow' />","메일인증","_blank, width=500px, height=200px,left=500px,top=200px");
				},error : function(req,status,err){
					console.log(req);
				}
			});//ajax
		});//mailCheck
	}); //ready
	
</script>


</html>



