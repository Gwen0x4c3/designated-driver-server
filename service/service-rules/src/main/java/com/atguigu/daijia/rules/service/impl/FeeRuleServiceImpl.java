package com.atguigu.daijia.rules.service.impl;

import com.atguigu.daijia.model.entity.rule.FeeRule;
import com.atguigu.daijia.model.form.rules.FeeRuleRequest;
import com.atguigu.daijia.model.form.rules.FeeRuleRequestForm;
import com.atguigu.daijia.model.vo.rules.FeeRuleResponse;
import com.atguigu.daijia.model.vo.rules.FeeRuleResponseVo;
import com.atguigu.daijia.rules.mapper.FeeRuleMapper;
import com.atguigu.daijia.rules.service.FeeRuleService;
import com.atguigu.daijia.rules.tools.DroolsHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class FeeRuleServiceImpl implements FeeRuleService {

    @Resource
    private FeeRuleMapper feeRuleMapper;

    @Override
    public FeeRuleResponseVo calculateOrderFee(FeeRuleRequestForm calculateOrderFeeForm) {
        FeeRuleRequest request = new FeeRuleRequest();
        request.setDistance(calculateOrderFeeForm.getDistance());
        request.setStartTime(new DateTime(calculateOrderFeeForm.getStartTime()).toString("HH:mm:ss"));
        request.setWaitMinute(calculateOrderFeeForm.getWaitMinute());
        log.info("传入参数: {}", request);

        // 获取最新订单费用规则
        FeeRule feeRule = feeRuleMapper.selectOne(new LambdaQueryWrapper<FeeRule>()
                .orderByDesc(FeeRule::getId).last("limit 1"));
        KieSession session = DroolsHelper.loadForRule(feeRule.getRule());

        FeeRuleResponse response = new FeeRuleResponse();
        session.setGlobal("feeRuleResponse", response);
        session.insert(request);
        session.fireAllRules();
        session.dispose();
        log.info("计算结果: {}", session);

        FeeRuleResponseVo responseVo = new FeeRuleResponseVo();
        BeanUtils.copyProperties(response, responseVo);
        return responseVo;
    }
}
