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
<h1>창희가 먹어본 술 <small>- 술 구매는 코스트코가 최고입니다</small></h1>
<table class="table table-striped table-bordered">
	<tr>
		<th>번호</th>
		<th>술이름</th>
		<th>도수</th>
		<th>분류</th>
	</tr>
	<c:forEach items="${alcoholList }" var="alcohol"> 
		<tr>
			<td>${alcohol.alNo }</td>
			<td>${alcohol.alName }</td>
			<td>${alcohol.alPercent }</td>
			<td>${alcohol.alCategory }</td>
		</tr>
	</c:forEach>
</table>
</body>
</html>