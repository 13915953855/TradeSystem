package com.jason.trade.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledService {
    private static final Logger log = LoggerFactory.getLogger(ScheduledService.class.getName());
    @Autowired
    private TradeService tradeService;

    @Scheduled(cron = "0 0 1 * * *")
    //@Scheduled(cron = "0 0/1 * * * *")
    public void scheduled(){
        doInternalScheduledTask();
    }

    private void doInternalScheduledTask() {
        log.info("开始执行定时任务=================");
        tradeService.autoUpdateContractStatus();
    }
}
