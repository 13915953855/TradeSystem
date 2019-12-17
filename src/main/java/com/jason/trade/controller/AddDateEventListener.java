package com.jason.trade.controller;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @Author: 徐森
 * @CreateDate: 2019/12/16
 * @Description:
 */
@Component
public class AddDateEventListener {

    @EventListener
    public void handleAddEvent(AddDateEvent event) {
        System.out.println("发布啦！");

    }
    @EventListener
    public void handleAddEvent2(AddDateEvent event) {
        System.out.println("222发布啦！");

    }
}
