package com.atguigu.daijia.driver.service;

import com.atguigu.daijia.model.form.driver.UpdateDriverAuthInfoForm;
import com.atguigu.daijia.model.vo.driver.DriverAuthInfoVo;
import com.atguigu.daijia.model.vo.driver.DriverLoginVo;

public interface DriverService {

    /**
     * 小程序授权登录
     *
     * @param code
     * @return
     */
    String login(String code);

    /**
     * 获取司机登录信息
     *
     * @param driverId
     * @return
     */
    DriverLoginVo getDriverLoginInfo(Long driverId);

    /**
     * 获取司机认证信息
     *
     * @param driverId
     * @return
     */
    DriverAuthInfoVo getDriverAuthInfo(Long driverId);

    /**
     * 更新司机认证信息
     *
     * @param form
     * @return
     */
    Boolean updateDriverAuthInfo(UpdateDriverAuthInfoForm form);

}
