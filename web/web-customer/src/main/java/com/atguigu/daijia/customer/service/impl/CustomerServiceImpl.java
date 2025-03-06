package com.atguigu.daijia.customer.service.impl;

import com.atguigu.daijia.common.constant.RedisConstant;
import com.atguigu.daijia.common.execption.GuiguException;
import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.common.result.ResultCodeEnum;
import com.atguigu.daijia.customer.client.CustomerInfoFeignClient;
import com.atguigu.daijia.customer.service.CustomerService;
import com.atguigu.daijia.model.vo.customer.CustomerLoginVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerInfoFeignClient customerInfoFeignClient;
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public String wxLogin(String code) {
        // 获取 openid
        Result<Long> result = customerInfoFeignClient.login(code);
        if (result.getCode().intValue() != ResultCodeEnum.SUCCESS.getCode()) {
            throw new GuiguException(result.getCode(), result.getMessage());
        }
        Long customerId = result.getData();
        if (customerId == null) {
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(RedisConstant.USER_LOGIN_KEY_PREFIX + token, customerId.toString(), RedisConstant.USER_LOGIN_KEY_TIMEOUT, TimeUnit.SECONDS);
        return token;
    }

    @Override
    public CustomerLoginVo getCustomerLoginInfo(Long userId) {
        if (userId == null) {
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }
        String customerId = (String) redisTemplate.opsForValue().get(RedisConstant.USER_LOGIN_KEY_PREFIX + token);
        Result<CustomerLoginVo> result = customerInfoFeignClient.getCustomerLoginInfo(Long.parseLong(customerId));
        if (result.getCode().intValue() != ResultCodeEnum.SUCCESS.getCode()) {
            throw new GuiguException(result.getCode(), result.getMessage());
        }
        CustomerLoginVo data = result.getData();
        if (data == null) {
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }
        return data;
    }
}
