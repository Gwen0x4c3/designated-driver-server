package com.atguigu.daijia.common.login;

import com.atguigu.daijia.common.execption.GuiguException;
import com.atguigu.daijia.common.result.ResultCodeEnum;
import com.atguigu.daijia.common.util.AuthContextHolder;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Component
@Aspect
@Order(100)
public class GuiguLoginAspect {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 切面处理
     *
     * @param joinPoint  切入点
     * @param guiguLogin 注解
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.atguigu.daijia.*.controller.*.*(..)) && @annotation(guiguLogin)")
    public Object process(ProceedingJoinPoint joinPoint, GuiguLogin guiguLogin) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String token = request.getHeader("token");

        if (StringUtils.isBlank(token)) {
            throw new GuiguException(ResultCodeEnum.LOGIN_AUTH);
        }
        String userId = (String) redisTemplate.opsForValue().get(token);
        if (userId != null) {
            AuthContextHolder.setUserId(Long.parseLong(userId));
        }
        return joinPoint.proceed();
    }
}
