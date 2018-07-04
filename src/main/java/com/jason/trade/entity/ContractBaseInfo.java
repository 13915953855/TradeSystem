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
    private String contractId;//合同编号
    private String contractDate;//合同日期
    private String externalContract;//外合同
    private String insideContract;//内合同
    private String externalCompany;//外商
    private String originCountry;//原产地
    private String companyNo;//厂号
    //private String shipmentPort;//起运港
    private String destinationPort;//目的港
    private String priceCondition;//价格条件
    private String payType;//付款方式
    private String currency;//币种
    private String expectSailingDate;//预计船期
    private String businessMode;//业务模式
    private double totalContractAmount;//合同总数量
    private double totalContractMoney;//合同总金额
    private double totalInvoiceAmount;//发票总数量
    private double totalInvoiceMoney;//发票总金额
    private Integer totalBoxes;//箱数总计
    /*private double tariffRate;//关税税率
    private double exchangeRate;//汇率
    private double taxRate;//增值税率*/

    /**
     * 付款信息（L/C）
     */
    private String issuingBank;//开证行
    private String issuingDate;//开证日期
    private String LCNo;//LC NO.
    private String bankDaodanDate;//银行到单日
    private String remittanceDate;//付汇日
    private String yahuidaoqiDate;//押汇到期日
    private double remittanceRate;//付汇汇率

    /**
     * 付款信息（TT）
     */
    private double prePayment;//付款金额
    private String prePaymentDate;//付款日期
    private float preRate;//汇率
    private double finalPayment;//付款金额
    private String finalPaymentDate;//付款日期
    private float finalRate;//汇率

    /**
     * 单据信息
     */
    private String containerNo;//柜号
    private String ladingbillNo;//提单号
    //private String shipCompany;//船公司
    private String containerSize;//货柜尺寸
    private Integer isNeedInsurance;//是否需要购买保险：0-是，1-否
    private String insuranceBuyDate;//保险购买日期
    private double insuranceMoney;//保险费用
    private String insuranceCompany;//保险公司
    private String ETD;
    private String ETA;
    private Integer isCheckElec;//是否已核对电子版：0-是，1-否
    //private String elecSendDate;//电子版发送日期
    //private String exCompanySendBillDate;//外商邮寄正本单据日期
    //private String billSignDate;//正本单据签收日期
    private String agent;//货代
    private String agentSendDate;//单据寄给货代日期
    private double tariff;//关税
    private double addedValueTax;//增值税
    private String tariffNo;//报关单号
    private String taxPayDate;//付税日期
    //private String taxSignDate;//税票签收日期
    //private String taxDeductibleParty;//税票抵扣方
    private String agentPassDate;//放行日期
    private String warehouse;//仓库
    private String storeDate;//入库日期
    //private double delayFee;//滞箱滞报费
    private String status;//状态  0-作废，1-已下单，2-已装船，3-已到港，4-已入库
    private String remark;//备注
    private Integer version;
    private String createUser;
    private String createDateTime;

    public ContractBaseInfo(){}

    public String getTariffNo() {
        return tariffNo;
    }

    public void setTariffNo(String tariffNo) {
        this.tariffNo = tariffNo;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    /*public double getTariffRate() {
        return tariffRate;
    }

    public void setTariffRate(double tariffRate) {
        this.tariffRate = tariffRate;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
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

    public String getExternalCompany() {
        return externalCompany;
    }

    public void setExternalCompany(String externalCompany) {
        this.externalCompany = externalCompany;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    public String getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(String destinationPort) {
        this.destinationPort = destinationPort;
    }

    public String getPriceCondition() {
        return priceCondition;
    }

    public void setPriceCondition(String priceCondition) {
        this.priceCondition = priceCondition;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExpectSailingDate() {
        return expectSailingDate;
    }

    public void setExpectSailingDate(String expectSailingDate) {
        this.expectSailingDate = expectSailingDate;
    }

    public String getBusinessMode() {
        return businessMode;
    }

    public void setBusinessMode(String businessMode) {
        this.businessMode = businessMode;
    }

    public double getTotalContractAmount() {
        return totalContractAmount;
    }

    public void setTotalContractAmount(double totalContractAmount) {
        this.totalContractAmount = totalContractAmount;
    }

    public double getTotalContractMoney() {
        return totalContractMoney;
    }

    public void setTotalContractMoney(double totalContractMoney) {
        this.totalContractMoney = totalContractMoney;
    }

    public double getTotalInvoiceAmount() {
        return totalInvoiceAmount;
    }

    public void setTotalInvoiceAmount(double totalInvoiceAmount) {
        this.totalInvoiceAmount = totalInvoiceAmount;
    }

    public double getTotalInvoiceMoney() {
        return totalInvoiceMoney;
    }

    public void setTotalInvoiceMoney(double totalInvoiceMoney) {
        this.totalInvoiceMoney = totalInvoiceMoney;
    }

    public Integer getTotalBoxes() {
        return totalBoxes;
    }

    public void setTotalBoxes(Integer totalBoxes) {
        this.totalBoxes = totalBoxes;
    }

    public String getIssuingBank() {
        return issuingBank;
    }

    public void setIssuingBank(String issuingBank) {
        this.issuingBank = issuingBank;
    }

    public String getIssuingDate() {
        return issuingDate;
    }

    public void setIssuingDate(String issuingDate) {
        this.issuingDate = issuingDate;
    }

    public String getLCNo() {
        return LCNo;
    }

    public void setLCNo(String LCNo) {
        this.LCNo = LCNo;
    }

    public String getBankDaodanDate() {
        return bankDaodanDate;
    }

    public void setBankDaodanDate(String bankDaodanDate) {
        this.bankDaodanDate = bankDaodanDate;
    }

    public String getRemittanceDate() {
        return remittanceDate;
    }

    public void setRemittanceDate(String remittanceDate) {
        this.remittanceDate = remittanceDate;
    }

    public String getYahuidaoqiDate() {
        return yahuidaoqiDate;
    }

    public void setYahuidaoqiDate(String yahuidaoqiDate) {
        this.yahuidaoqiDate = yahuidaoqiDate;
    }

    public double getRemittanceRate() {
        return remittanceRate;
    }

    public void setRemittanceRate(double remittanceRate) {
        this.remittanceRate = remittanceRate;
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

    public String getContainerSize() {
        return containerSize;
    }

    public void setContainerSize(String containerSize) {
        this.containerSize = containerSize;
    }

    public Integer getIsNeedInsurance() {
        return isNeedInsurance;
    }

    public void setIsNeedInsurance(Integer isNeedInsurance) {
        this.isNeedInsurance = isNeedInsurance;
    }

    public String getInsuranceBuyDate() {
        return insuranceBuyDate;
    }

    public void setInsuranceBuyDate(String insuranceBuyDate) {
        this.insuranceBuyDate = insuranceBuyDate;
    }

    public double getInsuranceMoney() {
        return insuranceMoney;
    }

    public void setInsuranceMoney(double insuranceMoney) {
        this.insuranceMoney = insuranceMoney;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getETD() {
        return ETD;
    }

    public void setETD(String ETD) {
        this.ETD = ETD;
    }

    public String getETA() {
        return ETA;
    }

    public void setETA(String ETA) {
        this.ETA = ETA;
    }

    public Integer getIsCheckElec() {
        return isCheckElec;
    }

    public void setIsCheckElec(Integer isCheckElec) {
        this.isCheckElec = isCheckElec;
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

    public String getAgentPassDate() {
        return agentPassDate;
    }

    public void setAgentPassDate(String agentPassDate) {
        this.agentPassDate = agentPassDate;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
