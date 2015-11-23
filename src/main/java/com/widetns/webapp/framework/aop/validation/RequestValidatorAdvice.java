package com.widetns.webapp.framework.aop.validation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
public class RequestValidatorAdvice {
    /**
     * @param joinPoint joinPoint
     * @throws ParameterLengthException , RequireParameterException
     */
    public void invoke(JoinPoint joinPoint) throws ParameterLengthException {
        ValidatorParam validatorAttr = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(ValidatorParam.class);

        if (validatorAttr == null) {
            return;
        }

        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (sra == null || sra.getRequest() == null) {
            return;
        }
        HttpServletRequest request = sra.getRequest();

        RequestValidator validator = new RequestValidator(request);
        validator.checkRequired(validatorAttr.required(), false);
        validator.checkFormat(validatorAttr.format());
    }
}