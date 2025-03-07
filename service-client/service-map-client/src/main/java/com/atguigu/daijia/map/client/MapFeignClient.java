package com.atguigu.daijia.map.client;

import com.atguigu.daijia.model.form.map.CalculateDrivingLineForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "service-map")
public interface MapFeignClient {

    /**
     * 计算驾驶线路
     *
     * @param calculateDrivingLineForm
     */
    @PostMapping("/map/calculateDrivingLine")
    void calculateDrivingLine(@RequestBody CalculateDrivingLineForm calculateDrivingLineForm);
}