package com.atguigu.daijia.driver.service;

import com.atguigu.daijia.model.entity.driver.DriverInfo;
import com.atguigu.daijia.model.form.driver.DriverFaceModelForm;
import com.atguigu.daijia.model.form.driver.UpdateDriverAuthInfoForm;
import com.atguigu.daijia.model.vo.driver.DriverAuthInfoVo;
import com.atguigu.daijia.model.vo.driver.DriverLoginVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DriverInfoService extends IService<DriverInfo> {

    /**
     * 小程序授权登录
     *
     * @param code
     * @return
     */
    Long login(String code);

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

    /**
     * 创建司机人脸模型
     *
     * @param form
     * @return
     */
    Boolean createDriverFaceModel(DriverFaceModelForm form);
}
