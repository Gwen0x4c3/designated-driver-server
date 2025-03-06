package com.atguigu.daijia.customer.service;

import com.atguigu.daijia.model.form.customer.UpdateWxPhoneForm;
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

    /**
     * 更新用户微信手机号
     *
     * @param updateWxPhoneForm updateWxPhoneForm
     * @return
     */
    Boolean updateWxPhoneNumber(UpdateWxPhoneForm updateWxPhoneForm);
}
