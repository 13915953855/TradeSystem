package com.jason.trade.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;

@Entity
public class ContractBaseInfo implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;//序号
    private String contractDate;//合同日期
    private String externalContract;//外合同
    private String insideContract;//内合同
    private String businessMode;//业务模式
    private String companyNo;//厂号
    private String warehouse;//仓库
    private String storeDate;//入库日期
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
    private String prePaymentDate;//付款日期
    private float preRate;//汇率
    private double prePaymentRMB;//小计
    private double finalPayment;//付款金额
    private String finalPaymentDate;//付款日期
    private float finalRate;//汇率
    private double finalPaymentRMB;//小计
    /**
     * 船期
     */
    private String etd;
    private String eta;
    /**
     * 外商单据
     */
    private Integer isCheckElec;//是否已核对电子版：0-是，1-否
    private String insuranceBuyDate;//保险购买日期
    private String insuranceSendDate;//寄出日期
    private String insuranceSignDate;//签收日期
    private String containerNo;//柜号
    private String ladingbillNo;//提单号
    /**
     * 货代报关报检
     */
    private String agent;//货代
    private String agentSendDate;//单据寄给货代日期
    private String agentPassDate;//放行日期

    /**
     * 税款
     */
    private String taxDeductibleParty;//税票抵扣方
    private double tariff;//关税
    private double addedValueTax;//增值税
    private String taxPayDate;//付税日期
    private String taxSignDate;//税票签收日期

    private String status;//状态
    private String remark;//备注

    public ContractBaseInfo(){}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContractDate() {
        return contractDate;
    }

    public void setContractDate(String contractDate) {
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

    public String getStoreDate() {
        return storeDate;
    }

    public void setStoreDate(String storeDate) {
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

    public String getPrePaymentDate() {
        return prePaymentDate;
    }

    public void setPrePaymentDate(String prePaymentDate) {
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

    public String getFinalPaymentDate() {
        return finalPaymentDate;
    }

    public void setFinalPaymentDate(String finalPaymentDate) {
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

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public Integer getIsCheckElec() {
        return isCheckElec;
    }

    public void setIsCheckElec(Integer isCheckElec) {
        this.isCheckElec = isCheckElec;
    }

    public String getInsuranceBuyDate() {
        return insuranceBuyDate;
    }

    public void setInsuranceBuyDate(String insuranceBuyDate) {
        this.insuranceBuyDate = insuranceBuyDate;
    }

    public String getInsuranceSendDate() {
        return insuranceSendDate;
    }

    public void setInsuranceSendDate(String insuranceSendDate) {
        this.insuranceSendDate = insuranceSendDate;
    }

    public String getInsuranceSignDate() {
        return insuranceSignDate;
    }

    public void setInsuranceSignDate(String insuranceSignDate) {
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

    public String getAgentSendDate() {
        return agentSendDate;
    }

    public void setAgentSendDate(String agentSendDate) {
        this.agentSendDate = agentSendDate;
    }

    public String getAgentPassDate() {
        return agentPassDate;
    }

    public void setAgentPassDate(String agentPassDate) {
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

    public String getTaxPayDate() {
        return taxPayDate;
    }

    public void setTaxPayDate(String taxPayDate) {
        this.taxPayDate = taxPayDate;
    }

    public String getTaxSignDate() {
        return taxSignDate;
    }

    public void setTaxSignDate(String taxSignDate) {
        this.taxSignDate = taxSignDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
