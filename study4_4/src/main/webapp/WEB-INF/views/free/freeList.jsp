
<%@page import="com.study.code.vo.CodeVO"%>
<%@page import="com.study.code.service.CommCodeServiceImpl"%>
<%@page import="com.study.code.service.ICommCodeService"%>
<%@page import="com.study.free.service.FreeBoardServiceImpl"%>
<%@page import="com.study.free.service.IFreeBoardService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.study.free.vo.FreeBoardVO"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.SQLException"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<%@include file="/WEB-INF/inc/header.jsp"%>
<%request.setCharacterEncoding("utf-8"); %>
</head>
<body>
	<%@ include file="/WEB-INF/inc/top.jsp"%>
	${searchVO }
	<hr>
	
	<div class="container">
		<!-- START : 검색 폼  -->
		<div class="panel panel-default">
			<div class="panel-body">
				<form name="search" action="freeList.wow" method="post"
					class="form-horizontal">
					<input type="hidden" name="curPage" value="${searchVO.curPage }">
					 <input type="hidden" name="rowSizePerPage" value="${searchVO.rowSizePerPage }">
					<div class="form-group">
						<label for="id_searchType" class="col-sm-2 control-label">검색</label>
						<div class="col-sm-2">
							<select id="id_searchType" name="searchType"
								class="form-control input-sm">
								<option value="T"  ${searchVO.searchType eq "T" ? "selected='selected'" : "" }>제목</option>
								<option value="W" ${searchVO.searchType eq "W" ? "selected='selected'" : ""} >작성자</option>
								<option value="C" ${searchVO.searchType eq "C" ? "selected='selected'" : ""}>내용</option>
							</select>
						</div>
						<div class="col-sm-2">
							<input type="text" name="searchWord"
								class="form-control input-sm" value="${searchVO.searchWord }" placeholder="검색어">
						</div>
						<label for="id_searchCategory"
							class="col-sm-2 col-sm-offset-2 control-label">분류</label>
						<div class="col-sm-2">
							<select id="id_searchCategory" name="searchCategory"
								class="form-control input-sm">
								<option value="">-- 전체 --</option>
								<c:forEach items="${cateList}" var="code">
									<option value="${code.commCd}" 
									 ${searchVO.searchCategory eq code.commCd  ?  "selected='selected'" : ""} >${code.commNm}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-2 col-sm-offset-9 text-right">
							<button type="button" id="id_btn_reset"
								class="btn btn-sm btn-default">
								<i class="fa fa-sync"></i> &nbsp;&nbsp;초기화
							</button>
						</div>
						<div class="col-sm-1 text-right">
							<button type="submit" class="btn btn-sm btn-primary ">
								<i class="fa fa-search"></i> &nbsp;&nbsp;검 색
							</button>
						</div>
					</div>
				</form>

			</div>
		</div>
		<!-- END : 검색 폼  -->

		<!-- START : 목록건수 및 새글쓰기 버튼  -->
		<div class="row" style="margin-bottom: 10px;">
			<div class="col-sm-3 form-inline">
				전체 ${searchVO.totalRowCount }건 조회 <select id="id_rowSizePerPage" name="rowSizePerPage"
					class="form-control input-sm">
					<c:forEach var="i" begin="10" end="50" step="10">
						<option value="${i }" 
						${searchVO.rowSizePerPage eq i ? "selected='selected'" : "" }>${i }</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<!-- END : 목록건수 및 새글쓰기 버튼  -->




		<div class="page-header">
			<h3>
				자유게시판 - <small>글 목록</small>
			</h3>
		</div>
		<div class="row">
		<div class="col-sm-2 col-sm-offset-10 text-right">
			<a href="<c:url value='/free/excelDown' />" class="btn  btn-sm btn-default" target="_blank" id="excelDown">excelDown</a>
		</div>
			<div class="col-sm-2 col-sm-offset-10 text-right"
				style="margin-bottom: 5px;">
				<a href="freeForm.wow" class="btn btn-primary btn-sm"> <span
					class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>
					&nbsp;새글쓰기
				</a>
			</div>
		</div>
		<table class="table table-striped table-bordered table-hover">
			<colgroup>
				<col width="10%" />
				<col width="15%" />
				<col />
				<col width="10%" />
				<col width="15%" />
				<col width="10%" />
			</colgroup>
			<thead>
				<tr>
					<th>글번호</th>
					<th>분류</th>
					<th>제목</th>
					<th>작성자</th>
					<th>등록일</th>
					<th>조회수</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${freeBoardList }" var="freeBoard">
					<tr class="text-center">

						<td>${freeBoard.boNo }</td>
						<td>${freeBoard.boCategoryNm }</td>
						<td class="text-left"><a
							href="freeView.wow?boNo=${freeBoard.boNo }">
								${freeBoard.boTitle } </a></td>
						<td>${freeBoard.boWriter }</td>
						<td>${freeBoard.boModDate eq null  ? freeBoard.boRegDate : freeBoard.boModDate}</td>
						<td>${freeBoard.boHit }</td>
					</tr>

				</c:forEach>

			</tbody>
		</table>
		
		<!-- START : 페이지네이션  -->
		<nav class="text-center">
			<ul class="pagination">
				<!-- 첫 페이지  -->
				<li><a href="freeList.wow?curPage=1" data-page="1"><span
						aria-hidden="true">&laquo;</span></a></li>
				<!-- 이전 페이지 -->
				<c:if test="${searchVO.firstPage!=1 }">
					<li><a href="freeList.wow?curPage=${searchVO.firstPage-1 }"
						data-page="${searchVO.firstPage-1 }"><span aria-hidden="true">&lt;</span></a></li>
				</c:if>

				<!-- 페이지 넘버링  -->
				<c:forEach begin="${searchVO.firstPage }"
					end="${searchVO.lastPage }" var="i">
					<c:if test="${searchVO.curPage !=i }">
						<li><a href="freeList.wow?curPage=${i }" data-page="${i }">${i }</a></li>
					</c:if>
					<c:if test="${searchVO.curPage ==i }">
						<li class="active"><a href="#">${i }</a></li> 
					</c:if>
				</c:forEach>

				<!-- 다음  페이지  -->
				<c:if test="${searchVO.lastPage!=searchVO.totalPageCount }">
					<li><a href="freeList.wow?curPage=${searchVO.lastPage+1 }" data-page="${searchVO.lastPage+1 }"><span
							aria-hidden="true">&gt;</span></a></li>
				</c:if>

				<!-- 마지막 페이지 -->
				<li><a href="freeList.wow?curPage=${searchVO.totalPageCount }" data-page="${searchVO.totalPageCount }"><span
						aria-hidden="true">&raquo;</span></a></li>
					
			</ul>
		</nav>
		<!-- END : 페이지네이션  -->
	</div>
	<!-- container -->
</body>




<script type="text/javascript">
		// 변수 정의
		$form=$("form[name='search']");
		$curPage=$form.find("input[name='curPage']");
		


		// 각 이벤트 등록
		// 페이지 링크 클릭, event전파방지, data속성값읽고 form태그 내의 input name=curPage값 바꾸기 
		//submit
		$('ul.pagination li a[data-page]').click(function(e) {
			//a링크를 누르면 get방식으로 감 => 파라미터가 curPage 1개만 넘어가요.
			//searchVO에 해당하는 데이터(search *, rowSizePerPage 등 +curPage )==form태그 안에 input들 
			//데이터가 같이넘어가도록 get방식 대신에  form태그 post submit하도록 변경
			//검색도 했고 현재 5번글이다. ==> 8번 글을 눌렀다 
			// 검색,rowSizePerPage이 유지되면서 curPage만 8로 바뀌면 됩니다.
			
			//기본이벤트 막기, curPage 변경(현재 내가 누른 a태그의 data-page에 있어요) 
			//, form submit
			e.preventDefault();
			$curPage.val($(this).data("page"));
			$form.submit();
			
			
		}); // ul.pagination li a[data-page]

		
		//form태그내의 버튼 클릭
		//이벤트전파방지, curPage 값 1로
		//submit
		$form.find("button[type=submit]").click(function(e) {
			e.preventDefault();
			$curPage.val(1);
			$form.submit();
		});
		
		

		
		// 목록 갯수 변경
		// id_rowSizePerPage 변경되었을 때
		// 페이지 1, 목록수 = 현재값 으로 변경 후 서브밋
		$('#id_rowSizePerPage').change(function(e) {
			$curPage.val(1);
			$form.find("input[name='rowSizePerPage']").val($(this).val());
			$form.submit();
		}); // '#id_rowSizePerPage'.change

		// 초기화 버튼 클릭
		$('#id_btn_reset').click(function() {
			$curPage.val(1);
			$form.find("input[name='searchWord']").val("");
			$form.find("select[name='searchType'] option:eq(0)").attr("selected", "selected");
			$form.find("select[name='searchCategory'] option:eq(0)").attr("selected", "selected");
		}); // #id_btn_reset.click 
		
		
		
		$("#excelDown").click( function(e){
			var $form=$("form[name='search']");
			$(this).attr("href","<c:url value='/free/excelDown' />"+"?"+$form.serialize() );
		}); //excelDown click
		
		
	</script>


</html>






