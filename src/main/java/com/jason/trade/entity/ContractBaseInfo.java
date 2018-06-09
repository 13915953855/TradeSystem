package com.jason.trade.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class ContractBaseInfo implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;//序号
    private Date contractDate;//合同日期
    private String externalContract;//外合同
    private String insideContract;//内合同
    private String businessMode;//业务模式
    private String companyNo;//厂号
    private String warehouse;//仓库
    private Date storeDate;//入库日期
    /**
     * 采购信息
     */
    private String specification;//规格
    private String originCountry;//原产地
    private String priceCondition;//价格条件
    private String shipmentPort;//起运港
    private String destinationPort;//目的港
    /**
     * 预付款&&尾款
     */
    private double prePayment;//付款金额
    private Date prePaymentDate;//付款日期
    private float preRate;//汇率
    private double prePaymentRMB;//小计
    private double finalPayment;//付款金额
    private Date finalPaymentDate;//付款日期
    private float finalRate;//汇率
    private double finalPaymentRMB;//小计
    /**
     * 船期
     */
    private Date etd;
    private Date eta;
    /**
     * 外商单据
     */
    private Integer isCheckElec;//是否已核对电子版：0-是，1-否
    private Date insuranceBuyDate;//保险购买日期
    private Date insuranceSendDate;//寄出日期
    private Date insuranceSignDate;//签收日期
    private String containerNo;//柜号
    private String ladingbillNo;//提单号
    /**
     * 货代报关报检
     */
    private String agent;//货代
    private Date agentSendDate;//单据寄给货代日期
    private Date agentPassDate;//放行日期

    /**
     * 税款
     */
    private String taxDeductibleParty;//税票抵扣方
    private double tariff;//关税
    private double addedValueTax;//增值税
    private Date taxPayDate;//付税日期
    private Date taxSignDate;//税票签收日期

    private String remark;//备注

    public ContractBaseInfo(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public String getExternalContract() {
        return externalContract;
    }

    public void setExternalContract(String externalContract) {
        this.externalContract = externalContract;
    }

    public String getInsideContract() {
        return insideContract;
    }

    public void setInsideContract(String insideContract) {
        this.insideContract = insideContract;
    }

    public String getBusinessMode() {
        return businessMode;
    }

    public void setBusinessMode(String businessMode) {
        this.businessMode = businessMode;
    }

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public Date getStoreDate() {
        return storeDate;
    }

    public void setStoreDate(Date storeDate) {
        this.storeDate = storeDate;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getPriceCondition() {
        return priceCondition;
    }

    public void setPriceCondition(String priceCondition) {
        this.priceCondition = priceCondition;
    }

    public String getShipmentPort() {
        return shipmentPort;
    }

    public void setShipmentPort(String shipmentPort) {
        this.shipmentPort = shipmentPort;
    }

    public String getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(String destinationPort) {
        this.destinationPort = destinationPort;
    }

    public double getPrePayment() {
        return prePayment;
    }

    public void setPrePayment(double prePayment) {
        this.prePayment = prePayment;
    }

    public Date getPrePaymentDate() {
        return prePaymentDate;
    }

    public void setPrePaymentDate(Date prePaymentDate) {
        this.prePaymentDate = prePaymentDate;
    }

    public float getPreRate() {
        return preRate;
    }

    public void setPreRate(float preRate) {
        this.preRate = preRate;
    }

    public double getPrePaymentRMB() {
        return prePaymentRMB;
    }

    public void setPrePaymentRMB(double prePaymentRMB) {
        this.prePaymentRMB = prePaymentRMB;
    }

    public double getFinalPayment() {
        return finalPayment;
    }

    public void setFinalPayment(double finalPayment) {
        this.finalPayment = finalPayment;
    }

    public Date getFinalPaymentDate() {
        return finalPaymentDate;
    }

    public void setFinalPaymentDate(Date finalPaymentDate) {
        this.finalPaymentDate = finalPaymentDate;
    }

    public float getFinalRate() {
        return finalRate;
    }

    public void setFinalRate(float finalRate) {
        this.finalRate = finalRate;
    }

    public double getFinalPaymentRMB() {
        return finalPaymentRMB;
    }

    public void setFinalPaymentRMB(double finalPaymentRMB) {
        this.finalPaymentRMB = finalPaymentRMB;
    }

    public Date getEtd() {
        return etd;
    }

    public void setEtd(Date etd) {
        this.etd = etd;
    }

    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }

    public Integer getIsCheckElec() {
        return isCheckElec;
    }

    public void setIsCheckElec(Integer isCheckElec) {
        this.isCheckElec = isCheckElec;
    }

    public Date getInsuranceBuyDate() {
        return insuranceBuyDate;
    }

    public void setInsuranceBuyDate(Date insuranceBuyDate) {
        this.insuranceBuyDate = insuranceBuyDate;
    }

    public Date getInsuranceSendDate() {
        return insuranceSendDate;
    }

    public void setInsuranceSendDate(Date insuranceSendDate) {
        this.insuranceSendDate = insuranceSendDate;
    }

    public Date getInsuranceSignDate() {
        return insuranceSignDate;
    }

    public void setInsuranceSignDate(Date insuranceSignDate) {
        this.insuranceSignDate = insuranceSignDate;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getLadingbillNo() {
        return ladingbillNo;
    }

    public void setLadingbillNo(String ladingbillNo) {
        this.ladingbillNo = ladingbillNo;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public Date getAgentSendDate() {
        return agentSendDate;
    }

    public void setAgentSendDate(Date agentSendDate) {
        this.agentSendDate = agentSendDate;
    }

    public Date getAgentPassDate() {
        return agentPassDate;
    }

    public void setAgentPassDate(Date agentPassDate) {
        this.agentPassDate = agentPassDate;
    }

    public String getTaxDeductibleParty() {
        return taxDeductibleParty;
    }

    public void setTaxDeductibleParty(String taxDeductibleParty) {
        this.taxDeductibleParty = taxDeductibleParty;
    }

    public double getTariff() {
        return tariff;
    }

    public void setTariff(double tariff) {
        this.tariff = tariff;
    }

    public double getAddedValueTax() {
        return addedValueTax;
    }

    public void setAddedValueTax(double addedValueTax) {
        this.addedValueTax = addedValueTax;
    }

    public Date getTaxPayDate() {
        return taxPayDate;
    }

    public void setTaxPayDate(Date taxPayDate) {
        this.taxPayDate = taxPayDate;
    }

    public Date getTaxSignDate() {
        return taxSignDate;
    }

    public void setTaxSignDate(Date taxSignDate) {
        this.taxSignDate = taxSignDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
