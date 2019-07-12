package com.jason.trade.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CargoStoreInfo implements Serializable {
    private String cargo_name;
    private String level;
    private String company_no;
    private String cargo_no;
    private String external_contract;
    private String inside_contract;
    private String container_no;
    private String ladingbill_no;
    private String store_date;
    private String warehouse;
    private String invoice_amount;
    private String boxes;
    private String baoguandan;
    private String qacertificate;
    private String jyzqfrq;
    private String bgdcjrq;
}
