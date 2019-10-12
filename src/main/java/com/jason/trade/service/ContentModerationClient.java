package com.jason.trade.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @ClassName ContentModerationClient
 * @Description 调用内容安全微服务
 * @Author 王亮
 * @Date 2019/5/28 13:52
 */
@FeignClient(value = "CONTENT-MODERATION-SERVICE",fallback = ContentModerationClientFallback.class)
public interface ContentModerationClient {

    /**
     * 字符串敏感词校验
     * @param jsonBody {"aimString": "需要校验的内容"}
     * @return (10001, " Contains violations ")包含敏感词汇
     * (10002, "Do not Contains violations")不包含敏感词汇
     */
    @PostMapping("/wordMatch")
    JSONObject wordMatch(@RequestBody JSONObject jsonBody);

}
