<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/inc/header.jsp"%>
<title></title>
</head>
<body>
	<div class="container">
		<div class="row col-md-8 col-md-offset-2">
			<div class="page-header">
				<h3>이메일인증</h3>
			</div>
			<table class="table">	
				<tr>
					<td>
					<input type="text" name="authKey" class="form-control input-sm" placeholder="인증번호를 입력하세요">
						<button onclick="return false;" type="button" id="authKeyCompare">확인</button>
					</td>
				</tr>
			</table>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(document).ready(function(){
		$("#authKeyCompare").on("click",function(e){
			e.preventDefault(); //걍 심심해서씀.   form태그 없어서 사실 필요없음
			var mail=opener.document.getElementsByName("memMail")[0].value;
			var authKey=$("input[name='authKey']").val();
			$.ajax({
				url : "<c:url value='authKeyCompare.wow' />"
				,type : "POST"
				,data : {"mail" : mail, "authKey" :authKey }
				,success : function(data){
					if(data=="authKeySame"){
						window.close();
					} else{
						alert("인증번호달라");
					}
						
				}, error :function(req,status,err){
					console.log(req);
				}
			});//ajax
			
		}); //확인버튼클릭
	}); //ready
	
</script>
</html>