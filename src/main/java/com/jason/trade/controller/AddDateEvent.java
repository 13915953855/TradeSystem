package com.jason.trade.controller;

import org.springframework.context.ApplicationEvent;

/**
 * @Author: 徐森
 * @CreateDate: 2019/12/16
 * @Description:
 */
public class AddDateEvent extends ApplicationEvent {
    public AddDateEvent(Object source) {
        super(source);
    }
}
