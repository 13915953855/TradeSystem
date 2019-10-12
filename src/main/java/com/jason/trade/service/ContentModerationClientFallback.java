package com.jason.trade.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: 徐森
 * @CreateDate: 2019/10/10
 * @Description:
 */
@Slf4j
@Component
public class ContentModerationClientFallback implements ContentModerationClient {
    @Override
    public JSONObject wordMatch(JSONObject jsonBody) {
        log.info("Hystrix error:{}",jsonBody.toJSONString());
        return new JSONObject();
    }
}
