<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/inc/header.jsp" %>
<title></title>
</head>
<body>
<%@include file="/WEB-INF/inc/top.jsp" %>

<ul class="nav nav-tabs" role="tablist" id="myTab">
  <li role="presentation" class="active"><a href="chProfile" aria-controls="chProfile" role="tab" data-toggle="tab">창희프로필</a></li>
  <li role="presentation"><a href="chAlcohol" aria-controls="chAlcohol" role="tab" data-toggle="tab">창희의술</a></li>
  <li role="presentation"><a href="chPiano" aria-controls="chPiano" role="tab" data-toggle="tab">창희의 피아노</a></li>
</ul>

<div class="tab-content">
	
</div>

</body>
<script>
	$(document).ready(function(){
		//처음 시작할 땐 chProfile이 나와야한다.
		$(".tab-content").load("<c:url value='chProfile.wow' />");
		
		//탭 클릭시마다 
		$("#myTab li a").on("click", function(e){
			e.preventDefault();
			var page=$(this).attr("href");
			$(".tab-content").load("<c:url value='"+page+".wow' />");
		});
	});
</script>
</html>