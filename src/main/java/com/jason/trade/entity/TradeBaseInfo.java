package com.jason.trade.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class TradeBaseInfo implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;//序号
    private Date contractDate;//合同日期
    private String externalContract;//外合同
    private String insideContract;//内合同
    private String businessMode;//业务模式
    private String companyNo;//厂号
    /**
     * 采购信息
     */
    private String specification;//规格
    private String originCountry;//原产地
    private String priceCondition;//价格条件
    private String shipmentPort;//起运港
    private String destinationPort;//目的港
    private double amount;//数量(kg)
    private double unitPrice;//单价($)
    private double contractValue;//合同价格
    /**
     * 预付款&&尾款
     */
    private double payment;//付款金额
    private Date paymentDate;//付款日期
    private float rate;//汇率
    private double paymentRMB;//小计
    /**
     * 船期
     */
    private Date etd;
    private Date eta;


    private String tradeName;

    public TradeBaseInfo(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }
}
