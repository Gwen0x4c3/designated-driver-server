package com.atguigu.daijia.customer.service;

public interface CustomerService {

    /**
     * 小程序授权登录
     *
     * @param code
     * @return
     */
    String wxLogin(String code);
}
