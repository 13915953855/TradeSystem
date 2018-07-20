package com.jason.trade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledService {
    @Autowired
    private TradeService tradeService;

    @Scheduled(cron = "0 0 1 * * *")
    //@Scheduled(cron = "0 0/1 * * * *")
    public void scheduled(){
        doInternalScheduledTask();
    }

    private void doInternalScheduledTask() {
        tradeService.autoUpdateContractStatus();
    }
}
