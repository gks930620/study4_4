package com.study.common.interceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.study.login.vo.UserVO;
public class LoginCheckInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String ajax= request.getHeader("X-requested-with"); //요청이 ajax인지아닌지
		
		HttpSession session=request.getSession();  //getSession()을  통해 session이 언제생기는지에 대해 복습하자. 
		UserVO user=(UserVO)session.getAttribute("USER_INFO");
		if(user==null) {
			if(ajax!=null) { //ajax요청일 경우

				response.sendError(401, "로그인안했어요"); //ajax error함수에서 login으로 이동하게 할거임.
				return false;
			}
			
			response.sendRedirect(request.getContextPath()+"/login/login.wow");
			return false;
		}
		return true;
	}
}


//String reContent=request.getParameter("reContent");
//session.setAttribute("RECONTENT", reContent);  이제 댓글 쓰다가 로그인버튼 누르는게 아니라 로그인 안하면 애초에  등록버튼 못 누르니까 이건 필요 없을 ㄷ스 