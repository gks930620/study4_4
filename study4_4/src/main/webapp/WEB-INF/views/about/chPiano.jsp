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
	<h1>이제 그만 발라드1번을 치고 싶어요</h1>
	<table class="table table-striped table-bordered">
		<tr>
			<th>번호</th>
			<th>술이름</th>
			<th>도수</th>
			<th>분류</th>
		</tr>
		<c:forEach items="${pianoList }" var="piano"> 
			<tr>
				<td>${piano.pNo }</td>
				<td>${piano.pTitle }</td>
				<td>${piano.pDate }</td>
				<td>${piano.pComposer }</td>
				<td>${piano.pCategory }</td>
			</tr>
		</c:forEach>
	</table>
	</body>
	</html>