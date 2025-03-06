package com.atguigu.daijia.customer.service;

import com.atguigu.daijia.model.entity.customer.CustomerInfo;
import com.atguigu.daijia.model.form.customer.UpdateWxPhoneForm;
import com.atguigu.daijia.model.vo.customer.CustomerLoginVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CustomerInfoService extends IService<CustomerInfo> {

    /**
     * 小程序授权登录
     *
     * @param code
     * @return
     */
    Long login(String code);

    /**
     * 获取客户登录信息
     *
     * @param customerId
     * @return
     */
    CustomerLoginVo getCustomerLoginInfo(Long customerId);

    /**
     * 更新客户微信手机号码
     *
     * @param updateWxPhoneForm updateWxPhoneForm
     * @return
     */
    Boolean updateWxPhoneNumber(UpdateWxPhoneForm updateWxPhoneForm);
}
