package com.study.common.util;


import java.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import com.study.free.web.FreeBoardController;

@Aspect
public class CommonControllerParameter {
	
	private Logger logger=LoggerFactory.getLogger(this.getClass());
		
	@Pointcut("execution(public * com.study.*.web..*(*,..))")     
	// public *,  com.study.*.web 에 모든 메소드 중 (*,..)  파라미터가 1개 이상인 메소드
	//com.study.*.service. context-main에 빈등록 했을 땐  모든 서비스단에 모든 메소드는 됐소  근데  왜  com.study.*.web은 안될까?
	// 여기서 또 중요한게 빈을 context-main에 하면  contextLoaderLister 빈은 
	// dispatcher 서블릿 빈인 @Controller에  접근 못해서... 그래서 aop controller단에 적용할거면 mvc-servlet에 
	private void publicTarget() {
		
	}


    // 또 생각 할게 proceed 전이냐 후에 따라 파라미터 값이 달라질 수 있음.  참조 잘 생각해보자.  메소드안에서 파라미터인 searchVO의 값을 set으로 바꿀 때 
    // 해당 메소드의 파라미터인 searchVO 값을  메소드 실행전에 콘솔에 찍냐, 메소드 실행후에 콘솔에 찍냐는 다르겠지
	@Around("publicTarget()")
	public Object parameter(ProceedingJoinPoint joinPoint) throws Throwable {
		Signature sig = joinPoint.getSignature();
		logger.info("실행 클래스 : {} " ,joinPoint.getTarget().getClass().getSimpleName());
		logger.info("실행 메소드 : {} " ,sig.getName());
		Object[] arr= joinPoint.getArgs();    //그 메소드의 파라미터들 
		// joinPoint.getArgs()은  파라미터가 없어도 null을 return하지않음.  길이가 0인 배열 return==> null체크 안해줘도 됨 
		for(Object a : arr) {
			//근데 a가 null일수도 (파라미터가 null값이 올 수도 있잖아..
			if(a!=null&&!a.getClass().getSimpleName().equals("BindingAwareModelMap")) {     //BindingAwareModelMap은  Model 객체의 구현클래스이름
				logger.info("메소드의 파라미터 : {}" , a.toString());
			}
		}
		try {
			Object result = joinPoint.proceed();
			return result;
		} finally {		
		}
	}

}
