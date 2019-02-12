package com.jason.trade.util;

import com.jason.trade.entity.ContractParam;

/**
 * @Author: 徐森
 * @CreateDate: 2019/1/22
 * @Description:
 */
public class CommonUtil {
    public static String revertStatus(String status) {
        //0-作废，1-已下单，2-已装船，3-已到港，4-已入库, 5-已售完
        if (status.indexOf("全部") >= 0) {
            status = "";
        } else {
            status = status.replaceAll("已下单", "1");
            status = status.replaceAll("已装船", "2");
            status = status.replaceAll("已到港", "3");
            status = status.replaceAll("已入库", "4");
            status = status.replaceAll("已售完", "5");
        }
        return status;
    }

    public static String revertStatusToCHN(String status) {
        //0-作废，1-已下单，2-已装船，3-已到港，4-已入库, 5-已售完
        status = status.replaceAll("1","已下单");
        status = status.replaceAll("2", "已装船");
        status = status.replaceAll("3", "已到港");
        status = status.replaceAll("4", "已入库");
        status = status.replaceAll("5", "已售完");
        return status;
    }
}
