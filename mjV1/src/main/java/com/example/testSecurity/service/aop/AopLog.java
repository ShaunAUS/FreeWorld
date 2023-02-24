package com.example.testSecurity.service.aop;

import java.lang.reflect.Method;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AopLog {

    //service.. 서브패키지의 클래스까지 포함
    //service.  서브패키지 미포함
    @Pointcut("execution(* com.example.testSecurity.service.service..*.*(..))")
    private void cut() {
    }

    @Before("cut()")
    public void beforeParameterLog(JoinPoint joinPoint) throws Throwable {

        //메서드 이름
        Method method = getMethod(joinPoint);
        log.info("==== method = {} ====", method.getName());

        //파라미터
        Object[] args = joinPoint.getArgs();
        if (args.length == 0) {
            log.info("method has no parameter");
        }
        for (Object arg : args) {
            log.info("parameter type = {}", arg.getClass().getSimpleName());
            log.info("parameter value = {}", arg);
        }
    }

    // Poincut에 의해 필터링된 경로로 들어오는 경우 메서드 리턴 후에 적용
    @AfterReturning(value = "cut()", returning = "returnObj")
    public void afterReturnLog(JoinPoint joinPoint, Object returnObj) {

        // 메서드 정보 받아오기
        Method method = getMethod(joinPoint);
        log.info("======= method name = {} =======", method.getName());

        Optional.ofNullable(returnObj).ifPresent(obj -> {
            log.info("return type = {}", obj.getClass().getSimpleName());
            log.info("return value = {}", obj);
        });


    }

    // JoinPoint로 메서드 정보 가져오기
    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

}


