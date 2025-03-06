package com.atguigu.daijia.customer.service;

import com.atguigu.daijia.model.vo.customer.CustomerLoginVo;

public interface CustomerService {

    /**
     * 小程序授权登录
     *
     * @param code
     * @return
     */
    String wxLogin(String code);

    /**
     * 获取客户登录信息
     *
     * @param userId 用户ID
     * @return
     */
    CustomerLoginVo getCustomerLoginInfo(Long userId);
}
