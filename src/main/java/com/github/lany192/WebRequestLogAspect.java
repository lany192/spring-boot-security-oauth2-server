package com.github.lany192;

import com.github.lany192.utils.ClientIpUtil;
import com.github.lany192.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
@Slf4j
@Aspect
@Component
public class WebRequestLogAspect {

    @Pointcut("execution(public * com.github.lany192.controller.*.*(..))")
    public void wsLog() {
    }

    /**
     * 接收到请求，记录请求内容
     *
     * @param joinPoint
     * @throws Throwable
     */
    @Before("wsLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        if (log.isInfoEnabled()) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                Map<String, String[]> parameters = request.getParameterMap();

                try {
                    String parametersString = null;
                    String requestBody = null;
                    if (parameters != null) {
                        parametersString = JsonUtil.multiValueMapToJsonString(parameters);
                    }
                    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                    //获取被拦截的方法
                    Method method = signature.getMethod();
                    Object object = getAnnotatedParameterValueRequestBody(method, joinPoint.getArgs());
                    if (object != null) {
                        requestBody = JsonUtil.objectToJsonString(object);
                    }
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("\nRequest from = ");
                    stringBuffer.append(ClientIpUtil.getIpAddress(request));
                    stringBuffer.append(";\n");
                    stringBuffer.append("uri = ");
                    stringBuffer.append(request.getRequestURL().toString());
                    stringBuffer.append(";\n");
                    stringBuffer.append("request method = ");
                    stringBuffer.append(request.getMethod());
                    stringBuffer.append(";\n");
                    stringBuffer.append("content type = ");
                    stringBuffer.append(request.getContentType());
                    stringBuffer.append(";\n");
                    stringBuffer.append("request parameters = ");
                    stringBuffer.append(parametersString);
                    stringBuffer.append(";\n");
                    stringBuffer.append("request body = ");
                    stringBuffer.append(requestBody);
                    stringBuffer.append(";\n");

                    log.info(stringBuffer.toString());

                } catch (Exception e) {
                    log.info("log http request Exception: ", e);
                }
            }
        }
    }

    @AfterReturning(returning = "ret", pointcut = "wsLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        if (log.isInfoEnabled()) {
            try {
                log.info("Response from server : \n" + JsonUtil.objectToJsonString(ret));
            } catch (Exception e) {
                log.info("log http response Exception:\n ", e);
            }
        }
    }

    private Object getAnnotatedParameterValueRequestBody(Method method, Object[] args) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        ///Parameter[] parameters = method.getParameters();

        int i = 0;
        for (Annotation[] annotations : parameterAnnotations) {
            Object arg = args[i];
            ///String name = parameters[i++].getDeclaringExecutable().getName();
            for (Annotation annotation : annotations) {
                if (annotation instanceof RequestBody) {
                    return arg;
                }
            }
        }
        return null;
    }


}
