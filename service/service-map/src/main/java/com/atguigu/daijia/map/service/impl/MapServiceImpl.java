package com.atguigu.daijia.map.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.daijia.common.execption.GuiguException;
import com.atguigu.daijia.common.result.ResultCodeEnum;
import com.atguigu.daijia.map.service.MapService;
import com.atguigu.daijia.model.form.map.CalculateDrivingLineForm;
import com.atguigu.daijia.model.vo.map.DrivingLineVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class MapServiceImpl implements MapService {

    @Resource
    private RestTemplate restTemplate;
    @Value("${tencent.map.key}")
    private String key;

    @Override
    public DrivingLineVo calculateDrivingLine(CalculateDrivingLineForm calculateDrivingLineForm) {
        String url = "https://apis.map.qq.com/ws/direction/v1/driving/?from={from}&to={to}&key={key}";
        Map<String, String> map = new HashMap<>();
        map.put("from", calculateDrivingLineForm.getStartPointLatitude() + "," + calculateDrivingLineForm.getStartPointLongitude());
        map.put("to", calculateDrivingLineForm.getEndPointLatitude() + "," + calculateDrivingLineForm.getEndPointLongitude());
        map.put("key", key);
        JSONObject result = restTemplate.getForObject(url, JSONObject.class, map);
        if (result.getIntValue("status") != 0) {
            log.error("调用腾讯地图API失败，错误信息：{}", result.getString("message"));
            throw new GuiguException(ResultCodeEnum.MAP_FAIL);
        }
        log.info("调用腾讯地图API成功，返回结果：{}", result);
        // 返回第一条最佳路线
        JSONObject route = result.getJSONObject("result").getJSONArray("routes").getJSONObject(0);
        DrivingLineVo drivingLineVo = new DrivingLineVo();
        drivingLineVo.setDistance(route.getBigDecimal("distance").divide(new BigDecimal(1000)).setScale(2, BigDecimal.ROUND_HALF_UP));
        drivingLineVo.setDuration(route.getBigDecimal("duration"));
        drivingLineVo.setPolyline(route.getJSONArray("polyline"));
        return drivingLineVo;
    }
}