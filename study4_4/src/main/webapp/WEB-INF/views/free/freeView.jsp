<%@page import="com.study.exception.BizNotEffectedException"%>
<%@page import="com.study.exception.BizNotFoundException"%>
<%@page import="com.study.free.service.FreeBoardServiceImpl"%>
<%@page import="com.study.free.service.IFreeBoardService"%>
<%@page import="com.study.free.vo.FreeBoardVO"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.SQLException"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/inc/header.jsp"%>
<title>자유게시판 - 글 보기</title>
</head>
<body>
	<%@ include file="/WEB-INF/inc/top.jsp"%>
	
		<div class="container">
			<div class="page-header">
				<h3>
					자유게시판 - <small>글 보기</small>
				</h3>
			</div>
			<table class="table table-striped table-bordered">
				<tbody>
					<tr>
						<th>글번호</th>
						<td>${freeBoard.boNo }</td>
					</tr>
					<tr>
						<th>글제목</th>
						<td>${freeBoard.boTitle }</td>
					</tr>
					<tr>
						<th>글분류</th>
						<td>${freeBoard.boCategoryNm }</td>
					</tr>
					<tr>
						<th>작성자명</th>
						<td>${freeBoard.boWriter }</td>
					</tr>
					<!-- 비밀번호는 보여주지 않음  -->
					<tr>
						<th>내용</th>
						<td><textarea rows="10" name="boContent" class="form-control input-sm" readonly="readonly">${freeBoard.boContent }</textarea></td>
					</tr>
					<tr>
						<th>등록자 IP</th>
						<td>${freeBoard.boIp }</td>
					</tr>
					<tr>
						<th>조회수</th>
						<td>${freeBoard.boHit }</td>
					</tr>
					<tr>
						<th>최근등록일자</th>
						<td>${freeBoard.boModDate eq null  ? freeBoard.boRegDate : freeBoard.boModDate}</td>
					</tr>
					<tr>
						<th>삭제여부</th>
						<td>${freeBoard.boDelYn }</td>
					</tr>
					
					<tr>
						<th>첨부파일</th>
						<td>
							<c:forEach var="f" items="${freeBoard.attaches}" varStatus="st">
							<div> 파일 ${st.count} <a href="<c:url value='/attach/download/${f.atchNo}' />" target="_blank"> 
							<span class="glyphicon glyphicon-save" aria-hidden="true"></span> ${f.atchOriginalName}
							</a> Size : ${f.atchFancySize} Down : ${f.atchDownHit}
							</div>
							</c:forEach>
						</td>
					</tr>
					
					<tr>
						<td colspan="2">
							<div class="pull-left">
								<a href="freeList.wow" class="btn btn-default btn-sm"> <span class="glyphicon glyphicon-list" aria-hidden="true"></span> &nbsp;&nbsp;목록
								</a>
							</div>
							<div class="pull-right">
								<a href="freeEdit.wow?boNo=${freeBoard.boNo }" class="btn btn-success btn-sm"> <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> &nbsp;&nbsp;수정
								</a>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- container -->

	<div class="container"><!-- reply container -->
		<!-- // START : 댓글 등록 영역  -->
		<div class="panel panel-default">
			<div class="panel-body form-horizontal">
				<form name="frm_reply" action="<c:url value='/reply/replyRegist' />"
					method="post" onclick="return false;">
					<input type="hidden" name="reParentNo" value="${freeBoard.boNo}">
					<input type="hidden" name="reCategory" value="FREE">
					<input type="hidden" name="reMemId" value="${USER_INFO.userId }">
					<input type="hidden" name="reIp"
						value="<%=request.getRemoteAddr()%>">
					
					<div class="form-group">
						<label class="col-sm-2  control-label">댓글</label>
						<div class="col-sm-8">
							<textarea rows="3" name="reContent" class="form-control" 
							 ${USER_INFO eq null ?   "readonly='readonly'" : "" }></textarea>
						</div>
						<div class="col-sm-2">
							<button id="btn_reply_regist" type="button"
								class="btn btn-sm btn-info">등록</button>
						</div>
					</div>
				</form>
			</div>
		</div>
		<!-- // END : 댓글 등록 영역  -->


		<!-- // START : 댓글 목록 영역  -->
		<div id="id_reply_list_area">
			<div class="row">
				<div class="col-sm-2 text-right">홍길동</div>
				<div class="col-sm-6">
					<pre>내용</pre>
				</div>
				<div class="col-sm-2">12/30 23:45</div>
				<div class="col-sm-2">
					<button name="btn_reply_edit" type="button"
						class=" btn btn-sm btn-info">수정</button>
					<button name="btn_reply_delete" type="button"
						class="btn btn-sm btn-danger">삭제</button>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-2 text-right">김판서</div>
				<div class="col-sm-6">
					<pre> 롤링롤링롤링롤링</pre>
				</div>
				<div class="col-sm-2">11/25 12:45</div>
				<div class="col-sm-2"></div>
			</div>
		</div>

		<div class="row text-center" id="id_reply_list_more">
			<a id="btn_reply_list_more"
				class="btn btn-sm btn-default col-sm-10 col-sm-offset-1"> <span
				class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span>
				더보기
			</a>
		</div>
		<!-- // END : 댓글 목록 영역  -->


		<!-- START : 댓글 수정용 Modal -->
		<div class="modal fade" id="id_reply_edit_modal" role="dialog">
			<div class="modal-dialog">
				<!-- Modal content-->
				<div class="modal-content">
					<form name="frm_reply_edit"
						action="<c:url value='/reply/replyModify' />" method="post"
						onclick="return false;">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">×</button>
							<h4 class="modal-title">댓글수정</h4>
						</div>
						<div class="modal-body">
							<input type="hidden" name="reNo" value="">
							<input type="hidden" name="reMemId" value="${USER_INFO.userId }">
							<textarea rows="3" name="reContent" class="form-control"></textarea>
						</div>
						<div class="modal-footer">
							<button id="btn_reply_modify" type="button"
								class="btn btn-sm btn-info">저장</button>
							<button type="button" class="btn btn-default btn-sm"
								data-dismiss="modal">닫기</button>
						</div>
					</form>
				</div>
			</div>
		</div>
		<!-- END : 댓글 수정용 Modal -->
	</div><!-- reply container -->

</body>
<script type="text/javascript">
	
var params={"curPage":1, "rowSizePerPage" : 10
		,"reCategory" : "FREE", "reParentNo": ${freeBoard.boNo} };
	
function fn_reply_list(){
	//아작스 호출해서 DB에 있는 reply 데이터 가지고 옵니다.
	//가지고오면(success)하면 댓글 div 만들어줍니다. 
	//list를 가지고오니까 jquery 반복문 써서 div 여러개 만들어주면되겠죠?
	$.ajax({
		url : "<c:url value='/reply/replyList.wow' />"
		,type: "POST"
		,data : params
		,dataType: 'JSON'     //받을 때 data를 어떻게 받을지  
		, success: function(data){
			console.log(data);
			$.each(data.data, function(index, element) {
				var str="";
				 str=str+'<div class="row" data-re-no="'+ element.reNo +'">'
			        +'<div class="col-sm-2 text-right" >'+element.reMemName+ '</div>'
			        +'<div class="col-sm-6"><pre>'+element.reContent+ '</pre></div>'
			        +'<div class="col-sm-2" >'+element.reRegDate +'</div>'
			        +'<div class="col-sm-2">';	
			        if(element.reMemId=="${USER_INFO.userId}"){		
			          str=str+   '<button name="btn_reply_edit" type="button" class=" btn btn-sm btn-info" >수정</button>'
			                 +  '<button name="btn_reply_delete" type="button" class="btn btn-sm btn-danger" >삭제</button>';
			        	}
			       str=str+'</div>'
			            +'</div>';
			       $('#id_reply_list_area').append(str);
			});
			       params.curPage+=1;
		}//success
	});	//ajax
}//function fn_reply_list

$(document).ready(function(){ //documnet가 준비될 때 
	fn_reply_list();  //freeView처음에 댓글 10개 보여주기
	// 등록버튼,     수정,삭제버튼,  모달의 등록버튼
	//더보기 버튼
	$("#id_reply_list_more").on("click",function(e){
		e.preventDefault();
		fn_reply_list();
	});
	
	//등록버튼
	$("#btn_reply_regist").on("click",function(e){
		e.preventDefault();
		$form=$(this).closest("form[name='frm_reply']");
		$.ajax({
			url:"<c:url value='/reply/replyRegist.wow'/>"
			,type : "POST"
			,dataType :"JSON"
			,data : $form.serialize()
			,success: function(data){
				console.log(data);
				$form.find("textarea[name='reContent']").val('');
				$("#id_reply_list_area").html('');
				params.curPage=1;
				fn_reply_list();
			}
			,error : function(req,st,err){
				if(req.status==401){
					location.href="<c:url value='/login/login.wow'  />";				
				}
			}
		});//ajax 
	});//등록버튼
	
	//수정버튼  function(){}은 동적으로 생긴 태그에도 적용이 되는거같아.. 
	//$().on("click") 동적으로생긴 태그에 적용됨
	$("#id_reply_list_area").on("click", 'button[name="btn_reply_edit"]'
			,function(e){
		//modal 아이디=id_reply_edit_modal
		//현재 버튼의  상위 div(하나의 댓글 전체)를 찾으세요
		// 그 div에서 현재 댓글의 내용 =modal에 있는 textarea에 복사 
		// 그 div태그의 data-re-no에 있는 값   $().data('re-no')
		//=modal에 있는  < input name=reNo>태그에 값으로 복사  
		//2개 복사했으면   $('#id_reply_edit_modal').modal('show')
		$btn=$(this);  //수정버튼
		$div=$btn.closest('div.row');   //버튼의 댓글 div
		$modal=$('#id_reply_edit_modal'); //modal div 
		$pre=$div.find('pre'); 
		 var content=$pre.html(); 
		 $textarea=$modal.find('textarea'); 
		
		 $textarea.val(content);  
		 var reNo=$div.data('re-no');	
		 $modal.find('input[name=reNo]').val(reNo);
		 $modal.modal('show');
	});//수정버튼
	

	//모달창 저장 버튼
	$("#btn_reply_modify").on("click", function(e){
		e.preventDefault(); 
		$form= $(this).closest('form[name="frm_reply_edit"]');
		$.ajax({
			url : "<c:url value='/reply/replyModify.wow' />"
			,type : "POST"
			,data : $form.serialize()
			,dataType : "JSON"
			,success: function(){
				$modal=$('#id_reply_edit_modal'); 
				$modal.modal('hide');
				
				var reNo=$modal.find('input[name=reNo]').val();
				var reContent=$modal.find('textarea').val();
				$("#id_reply_list_area").find("div[data-re-no='"+reNo+"']").find("pre").html(reContent);
				console.log($("#id_reply_list_area").find("div[data-re-no='"+reNo+"']").find("pre")[0]);
			}
		});//ajax
	});//모달창 저장버튼
	
	
	//삭제버튼
	$("#id_reply_list_area").on("click", 'button[name="btn_reply_delete"]'
			,function(e){
		e.preventDefault();
		$div=$(this).closest('.row');
		reNo=$div.data('re-no');
		reMemId="${USER_INFO.userId}";
		$.ajax({
			url : "<c:url value='/reply/replyDelete.wow' />"
			,type : "POST"
			,data : {"reNo" : reNo, "reMemId" : reMemId}
			,dataType : 'JSON'
			,success : function(){
				$div.remove();
			}
		});//ajax
	}); //삭제버튼
	
	
});

</script>
</html>






