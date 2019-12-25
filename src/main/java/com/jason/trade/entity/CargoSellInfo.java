package com.jason.trade.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

@Data
public class CargoSellInfo implements Serializable {
    @Excel(name="内合同号")
    private String inside_contract;
    @Excel(name="外商")
    private String external_company;
    @Excel(name="库号")
    private String cargo_no;
    @Excel(name="厂号")
    private String company_no;
    @Excel(name="商品")
    private String cargo_name;
    @Excel(name="级别")
    private String level;
    @Excel(name="仓库")
    private String warehouse;
    @Excel(name="单价")
    private String unit_price;
    @Excel(name="成本单价")
    private String cost_price;
    @Excel(name="库存重量")
    private String real_store_weight;
    @Excel(name="库存成本")
    private String real_store_money;
    @Excel(name="柜号")
    private String container_no;
    @Excel(name="提单号")
    private String ladingbill_no;
    @Excel(name="出库时间")
    private String real_sale_date;
    @Excel(name="客户名称")
    private String customer_name;
    @Excel(name="实售重量")
    private String real_sale_weight;
    @Excel(name="实售箱数")
    private String real_sale_boxes;
    @Excel(name="实售单价")
    private String real_sale_unit_price;
    @Excel(name="实售金额")
    private String real_sale_money;
    @Excel(name="定金")
    private String deposit;
    @Excel(name="客户来款金额")
    private String customer_pay_money;
    @Excel(name="利润")
    private String profit;
    @Excel(name="发票",replace = { "是_1", "否_0" })
    private String kaifapiao;

}
