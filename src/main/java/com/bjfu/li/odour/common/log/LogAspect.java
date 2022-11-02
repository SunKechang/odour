package com.bjfu.li.odour.common.log;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.bjfu.li.odour.common.token.JWTUtils;
import com.bjfu.li.odour.log.po.LogUploadReview;
import com.bjfu.li.odour.log.service.UploadReviewService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import java.text.ParseException;

@Aspect
@Component
public class LogAspect {
    @Resource
    UploadReviewService logService;

    @Pointcut("@annotation(com.bjfu.li.odour.common.log.LogAop)")
    public void logPointCut() {
    }
    @AfterReturning("logPointCut()")
    public void saveSysLog(JoinPoint joinPoint) throws ParseException {

        LogUploadReview logOperation = new LogUploadReview();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        //获取操作
        LogAop myLog = method.getAnnotation(LogAop.class);
        if (myLog != null) {
            //operation
            StringBuilder operation = new StringBuilder(myLog.operation());
            logOperation.setOperation(operation.toString());
        }else
            return;



        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //ip
        logOperation.setIp(request.getRemoteAddr());

        String token = request.getHeader("Authorization");
        DecodedJWT jwt = JWTUtils.verify(token);
        String email = jwt.getClaim("email").asString();
        logOperation.setEmail(email);
        logService.insertLog(logOperation);
    }
}
