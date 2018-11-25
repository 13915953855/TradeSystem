package com.jason.trade.model;

import javax.persistence.*;

@Entity
@Table(name="contract_base_info")
public class ContractBaseInfo {
    @Id
    @GeneratedValue
    @Column(name="id")
    private Integer id;
    @Column(name="cargo_type")
    private String cargoType;
    @Column(name="eta")
    private String eta;
    @Column(name="etd")
    private String etd;
    @Column(name="lcno")
    private String lcno;
    @Column(name="qacertificate")
    private Integer qacertificate;
    @Column(name="added_value_tax")
    private Double addedValueTax;
    @Column(name="added_value_tax1")
    private Double addedValueTax1;
    @Column(name="added_value_tax2")
    private Double addedValueTax2;
    @Column(name="added_value_tax3")
    private Double addedValueTax3;
    @Column(name="added_value_tax4")
    private Double addedValueTax4;
    @Column(name="added_value_tax5")
    private Double addedValueTax5;
    @Column(name="added_value_tax6")
    private Double addedValueTax6;
    @Column(name="agent")
    private String agent;
    @Column(name="agent_pass_date")
    private String agentPassDate;
    @Column(name="agent_send_date")
    private String agentSendDate;

    @Column(name="container_no")
    private String containerNo;
    @Column(name="container_size")
    private String containerSize;
    @Column(name="contract_date")
    private String contractDate;
    @Column(name="contract_id")
    private String contractId;
    @Column(name="create_date_time")
    private String createDateTime;
    @Column(name="create_user")
    private String createUser;
    @Column(name="currency")
    private String currency;
    @Column(name="destination_port")
    private String destinationPort;
    @Column(name="expect_sailing_date")
    private String expectSailingDate;
    @Column(name="external_company")
    private String externalCompany;
    @Column(name="external_contract")
    private String externalContract;
    @Column(name="final_payment")
    private Double finalPayment;
    @Column(name="final_payment_date")
    private String finalPaymentDate;
    @Column(name="final_rate")
    private Float finalRate;
    @Column(name="inside_contract")
    private String insideContract;
    @Column(name="insurance_buy_date")
    private String insuranceBuyDate;
    @Column(name="insurance_company")
    private String insuranceCompany;
    @Column(name="insurance_money")
    private Double insuranceMoney;
    @Column(name="is_check_elec")
    private Integer isCheckElec;
    @Column(name="is_need_insurance")
    private Integer isNeedInsurance;
    @Column(name="issuing_bank")
    private String issuingBank;
    @Column(name="issuing_date")
    private String issuingDate;
    @Column(name="ladingbill_no")
    private String ladingbillNo;
    @Column(name="origin_country")
    private String originCountry;
    @Column(name="pay_type")
    private String payType;
    @Column(name="pre_payment")
    private Double prePayment;
    @Column(name="pre_pay_bank")
    private String prePayBank;
    @Column(name="final_pay_bank")
    private String finalPayBank;
    @Column(name="pre_payment_date")
    private String prePaymentDate;
    @Column(name="pre_rate")
    private Float preRate;
    @Column(name="price_condition")
    private String priceCondition;
    @Column(name="remark")
    private String remark;
    @Column(name="remittance_date")
    private String remittanceDate;
    @Column(name="remittance_rate")
    private Double remittanceRate;
    @Column(name="status")
    private String status;
    @Column(name="store_date")
    private String storeDate;
    @Column(name="tariff")
    private Double tariff;
    @Column(name="tariff1")
    private Double tariff1;
    @Column(name="tariff2")
    private Double tariff2;
    @Column(name="tariff3")
    private Double tariff3;
    @Column(name="tariff4")
    private Double tariff4;
    @Column(name="tariff5")
    private Double tariff5;
    @Column(name="tariff6")
    private Double tariff6;
    @Column(name="tariff_no")
    private String tariffNo;
    @Column(name="tax_pay_date")
    private String taxPayDate;
    @Column(name="total_boxes")
    private Integer totalBoxes;
    @Column(name="total_contract_amount")
    private Double totalContractAmount;
    @Column(name="total_contract_money")
    private Double totalContractMoney;
    @Column(name="total_invoice_amount")
    private Double totalInvoiceAmount;
    @Column(name="total_invoice_money")
    private Double totalInvoiceMoney;
    @Column(name="version")
    private Integer version;
    @Column(name="warehouse")
    private String warehouse;
    @Column(name="yahuidaoqi_date")
    private String yahuidaoqiDate;
    @Column(name="exchange_rate")
    private Double exchangeRate;
    @Column(name="hasbaoguan")
    private Integer hasbaoguan;
    @Column(name="zhixiangfei")
    private Double zhixiangfei;
    @Column(name="zhigangfei")
    private Double zhigangfei;
    @Column(name="is_yahui")
    private Integer isYahui;
    @Column(name="yahui_money")
    private Double yahuiMoney;
    @Column(name="yahui_year_rate")
    private Double yahuiYearRate;
    @Column(name="yahui_day_rate")
    private Double yahuiDayRate;
    @Column(name="is_financing")
    private Integer isFinancing;
    @Column(name="financing_money")
    private Double financingMoney;
    @Column(name="financing_rate")
    private Double financingRate;
    @Column(name="financing_daoqi")
    private String financingDaoqi;
    @Column(name="daoqi_rate")
    private Double daoqiRate;
    @Column(name="financing_bank")
    private String financingBank;
    @Column(name="owner_company")
    private String ownerCompany;
    @Column(name="baoguandan")
    private Integer baoguandan;
    @Column(name="storage_condition")
    private String storageCondition;
    @Column(name="cargo_match")
    private Integer cargoMatch;
    @Column(name="customer_name")
    private String customerName;
    @Column(name="expect_sale_date")
    private String expectSaleDate;
    @Column(name="expect_sale_unit_price")
    private Double expectSaleUnitPrice;
    @Column(name="expect_sale_weight")
    private Double expectSaleWeight;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getExpectSaleDate() {
        return expectSaleDate;
    }

    public void setExpectSaleDate(String expectSaleDate) {
        this.expectSaleDate = expectSaleDate;
    }

    public Double getExpectSaleUnitPrice() {
        return expectSaleUnitPrice;
    }

    public void setExpectSaleUnitPrice(Double expectSaleUnitPrice) {
        this.expectSaleUnitPrice = expectSaleUnitPrice;
    }

    public Double getExpectSaleWeight() {
        return expectSaleWeight;
    }

    public void setExpectSaleWeight(Double expectSaleWeight) {
        this.expectSaleWeight = expectSaleWeight;
    }

    public String getCargoType() {
        return cargoType;
    }

    public void setCargoType(String cargoType) {
        this.cargoType = cargoType;
    }

    public Double getAddedValueTax1() {
        return addedValueTax1;
    }

    public void setAddedValueTax1(Double addedValueTax1) {
        this.addedValueTax1 = addedValueTax1;
    }

    public Double getAddedValueTax2() {
        return addedValueTax2;
    }

    public void setAddedValueTax2(Double addedValueTax2) {
        this.addedValueTax2 = addedValueTax2;
    }

    public Double getAddedValueTax3() {
        return addedValueTax3;
    }

    public void setAddedValueTax3(Double addedValueTax3) {
        this.addedValueTax3 = addedValueTax3;
    }

    public Double getAddedValueTax4() {
        return addedValueTax4;
    }

    public void setAddedValueTax4(Double addedValueTax4) {
        this.addedValueTax4 = addedValueTax4;
    }

    public Double getAddedValueTax5() {
        return addedValueTax5;
    }

    public void setAddedValueTax5(Double addedValueTax5) {
        this.addedValueTax5 = addedValueTax5;
    }

    public Double getAddedValueTax6() {
        return addedValueTax6;
    }

    public void setAddedValueTax6(Double addedValueTax6) {
        this.addedValueTax6 = addedValueTax6;
    }

    public Double getTariff1() {
        return tariff1;
    }

    public void setTariff1(Double tariff1) {
        this.tariff1 = tariff1;
    }

    public Double getTariff2() {
        return tariff2;
    }

    public void setTariff2(Double tariff2) {
        this.tariff2 = tariff2;
    }

    public Double getTariff3() {
        return tariff3;
    }

    public void setTariff3(Double tariff3) {
        this.tariff3 = tariff3;
    }

    public Double getTariff4() {
        return tariff4;
    }

    public void setTariff4(Double tariff4) {
        this.tariff4 = tariff4;
    }

    public Double getTariff5() {
        return tariff5;
    }

    public void setTariff5(Double tariff5) {
        this.tariff5 = tariff5;
    }

    public Double getTariff6() {
        return tariff6;
    }

    public void setTariff6(Double tariff6) {
        this.tariff6 = tariff6;
    }

    public Integer getCargoMatch() {
        return cargoMatch == null ? 0 : cargoMatch;
    }

    public void setCargoMatch(Integer cargoMatch) {
        this.cargoMatch = cargoMatch;
    }

    public String getStorageCondition() {
        return storageCondition;
    }

    public void setStorageCondition(String storageCondition) {
        this.storageCondition = storageCondition;
    }

    public Integer getBaoguandan() {
        return baoguandan == null ? 0 : baoguandan;
    }

    public void setBaoguandan(Integer baoguandan) {
        this.baoguandan = baoguandan;
    }

    public String getOwnerCompany() {
        return ownerCompany;
    }

    public void setOwnerCompany(String ownerCompany) {
        this.ownerCompany = ownerCompany;
    }

    public String getPrePayBank() {
        return prePayBank;
    }

    public void setPrePayBank(String prePayBank) {
        this.prePayBank = prePayBank;
    }

    public String getFinalPayBank() {
        return finalPayBank;
    }

    public void setFinalPayBank(String finalPayBank) {
        this.finalPayBank = finalPayBank;
    }

    public String getFinancingBank() {
        return financingBank;
    }

    public void setFinancingBank(String financingBank) {
        this.financingBank = financingBank;
    }

    public Integer getIsYahui() {
        return isYahui == null ? 0 : isYahui;
    }

    public void setIsYahui(Integer isYahui) {
        this.isYahui = isYahui;
    }

    public Double getYahuiMoney() {
        return yahuiMoney == null ? 0.0 : yahuiMoney;
    }

    public void setYahuiMoney(Double yahuiMoney) {
        this.yahuiMoney = yahuiMoney;
    }

    public Double getYahuiYearRate() {
        return yahuiYearRate == null ? 0.0 : yahuiYearRate;
    }

    public void setYahuiYearRate(Double yahuiYearRate) {
        this.yahuiYearRate = yahuiYearRate;
    }

    public Double getYahuiDayRate() {
        return yahuiDayRate == null ? 0.0 : yahuiDayRate;
    }

    public void setYahuiDayRate(Double yahuiDayRate) {
        this.yahuiDayRate = yahuiDayRate;
    }

    public Integer getIsFinancing() {
        return isFinancing == null ? 0 : isFinancing;
    }

    public void setIsFinancing(Integer isFinancing) {
        this.isFinancing = isFinancing;
    }

    public Double getFinancingMoney() {
        return financingMoney == null ? 0.0 : financingMoney;
    }

    public void setFinancingMoney(Double financingMoney) {
        this.financingMoney = financingMoney;
    }

    public Double getFinancingRate() {
        return financingRate == null ? 0.0 : financingRate;
    }

    public void setFinancingRate(Double financingRate) {
        this.financingRate = financingRate;
    }

    public String getFinancingDaoqi() {
        return financingDaoqi;
    }

    public void setFinancingDaoqi(String financingDaoqi) {
        this.financingDaoqi = financingDaoqi;
    }

    public Double getDaoqiRate() {
        return daoqiRate == null ? 0.0 : daoqiRate;
    }

    public void setDaoqiRate(Double daoqiRate) {
        this.daoqiRate = daoqiRate;
    }

    public Double getZhixiangfei() {
        return zhixiangfei == null ? 0.0 : zhixiangfei;
    }

    public void setZhixiangfei(Double zhixiangfei) {
        this.zhixiangfei = zhixiangfei;
    }

    public Double getZhigangfei() {
        return zhigangfei == null ? 0.0 : zhigangfei;
    }

    public void setZhigangfei(Double zhigangfei) {
        this.zhigangfei = zhigangfei;
    }

    public Integer getHasbaoguan() {
        return hasbaoguan == null ? 0 : hasbaoguan;
    }

    public void setHasbaoguan(Integer hasbaoguan) {
        this.hasbaoguan = hasbaoguan;
    }

    public Double getExchangeRate() {
        return exchangeRate == null ? 0.0 : exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        if(exchangeRate == null){
            this.exchangeRate = 0.0;
        }else{
            this.exchangeRate = exchangeRate;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta == null ? null : eta.trim();
    }

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd == null ? null : etd.trim();
    }

    public String getLcno() {
        return lcno;
    }

    public void setLcno(String lcno) {
        this.lcno = lcno == null ? null : lcno.trim();
    }

    public Integer getQacertificate() {
        return qacertificate == null ? 0 : qacertificate;
    }

    public void setQacertificate(Integer qacertificate) {
        this.qacertificate = qacertificate;
    }

    public Double getAddedValueTax() {
        return addedValueTax == null ? 0.0 : addedValueTax;
    }

    public void setAddedValueTax(Double addedValueTax) {
        this.addedValueTax = addedValueTax;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent == null ? null : agent.trim();
    }

    public String getAgentPassDate() {
        return agentPassDate;
    }

    public void setAgentPassDate(String agentPassDate) {
        this.agentPassDate = agentPassDate == null ? null : agentPassDate.trim();
    }

    public String getAgentSendDate() {
        return agentSendDate;
    }

    public void setAgentSendDate(String agentSendDate) {
        this.agentSendDate = agentSendDate == null ? null : agentSendDate.trim();
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo == null ? null : containerNo.trim();
    }

    public String getContainerSize() {
        return containerSize;
    }

    public void setContainerSize(String containerSize) {
        this.containerSize = containerSize == null ? null : containerSize.trim();
    }

    public String getContractDate() {
        return contractDate;
    }

    public void setContractDate(String contractDate) {
        this.contractDate = contractDate == null ? null : contractDate.trim();
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId == null ? null : contractId.trim();
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime == null ? null : createDateTime.trim();
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    public String getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(String destinationPort) {
        this.destinationPort = destinationPort == null ? null : destinationPort.trim();
    }

    public String getExpectSailingDate() {
        return expectSailingDate;
    }

    public void setExpectSailingDate(String expectSailingDate) {
        this.expectSailingDate = expectSailingDate == null ? null : expectSailingDate.trim();
    }

    public String getExternalCompany() {
        return externalCompany;
    }

    public void setExternalCompany(String externalCompany) {
        this.externalCompany = externalCompany == null ? null : externalCompany.trim();
    }

    public String getExternalContract() {
        return externalContract;
    }

    public void setExternalContract(String externalContract) {
        this.externalContract = externalContract == null ? null : externalContract.trim();
    }

    public Double getFinalPayment() {
        return finalPayment == null ? 0 : finalPayment;
    }

    public void setFinalPayment(Double finalPayment) {
        this.finalPayment = finalPayment;
    }

    public String getFinalPaymentDate() {
        return finalPaymentDate;
    }

    public void setFinalPaymentDate(String finalPaymentDate) {
        this.finalPaymentDate = finalPaymentDate == null ? null : finalPaymentDate.trim();
    }

    public Float getFinalRate() {
        return finalRate == null ? 0 : finalRate;
    }

    public void setFinalRate(Float finalRate) {
        this.finalRate = finalRate;
    }

    public String getInsideContract() {
        return insideContract;
    }

    public void setInsideContract(String insideContract) {
        this.insideContract = insideContract == null ? null : insideContract.trim();
    }

    public String getInsuranceBuyDate() {
        return insuranceBuyDate;
    }

    public void setInsuranceBuyDate(String insuranceBuyDate) {
        this.insuranceBuyDate = insuranceBuyDate == null ? null : insuranceBuyDate.trim();
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany == null ? null : insuranceCompany.trim();
    }

    public Double getInsuranceMoney() {
        return insuranceMoney == null ? 0.0 : insuranceMoney;
    }

    public void setInsuranceMoney(Double insuranceMoney) {
        this.insuranceMoney = insuranceMoney;
    }

    public Integer getIsCheckElec() {
        return isCheckElec == null ? 0 : isCheckElec;
    }

    public void setIsCheckElec(Integer isCheckElec) {
        this.isCheckElec = isCheckElec;
    }

    public Integer getIsNeedInsurance() {
        return isNeedInsurance == null ? 0 : isNeedInsurance;
    }

    public void setIsNeedInsurance(Integer isNeedInsurance) {
        this.isNeedInsurance = isNeedInsurance;
    }

    public String getIssuingBank() {
        return issuingBank;
    }

    public void setIssuingBank(String issuingBank) {
        this.issuingBank = issuingBank == null ? null : issuingBank.trim();
    }

    public String getIssuingDate() {
        return issuingDate;
    }

    public void setIssuingDate(String issuingDate) {
        this.issuingDate = issuingDate == null ? null : issuingDate.trim();
    }

    public String getLadingbillNo() {
        return ladingbillNo;
    }

    public void setLadingbillNo(String ladingbillNo) {
        this.ladingbillNo = ladingbillNo == null ? null : ladingbillNo.trim();
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry == null ? null : originCountry.trim();
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType == null ? null : payType.trim();
    }

    public Double getPrePayment() {
        return prePayment == null ? 0.0 : prePayment;
    }

    public void setPrePayment(Double prePayment) {
        this.prePayment = prePayment;
    }

    public String getPrePaymentDate() {
        return prePaymentDate;
    }

    public void setPrePaymentDate(String prePaymentDate) {
        this.prePaymentDate = prePaymentDate == null ? null : prePaymentDate.trim();
    }

    public Float getPreRate() {
        return preRate == null ? 0 : preRate;
    }

    public void setPreRate(Float preRate) {
        this.preRate = preRate;
    }

    public String getPriceCondition() {
        return priceCondition;
    }

    public void setPriceCondition(String priceCondition) {
        this.priceCondition = priceCondition == null ? null : priceCondition.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getRemittanceDate() {
        return remittanceDate;
    }

    public void setRemittanceDate(String remittanceDate) {
        this.remittanceDate = remittanceDate == null ? null : remittanceDate.trim();
    }

    public Double getRemittanceRate() {
        return remittanceRate == null ? 0.0 : remittanceRate;
    }

    public void setRemittanceRate(Double remittanceRate) {
        this.remittanceRate = remittanceRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getStoreDate() {
        return storeDate;
    }

    public void setStoreDate(String storeDate) {
        this.storeDate = storeDate == null ? null : storeDate.trim();
    }

    public Double getTariff() {
        return tariff == null ? 0.0 : tariff;
    }

    public void setTariff(Double tariff) {
        this.tariff = tariff;
    }

    public String getTariffNo() {
        return tariffNo;
    }

    public void setTariffNo(String tariffNo) {
        this.tariffNo = tariffNo == null ? null : tariffNo.trim();
    }

    public String getTaxPayDate() {
        return taxPayDate;
    }

    public void setTaxPayDate(String taxPayDate) {
        this.taxPayDate = taxPayDate == null ? null : taxPayDate.trim();
    }

    public Integer getTotalBoxes() {
        return totalBoxes == null ? 0 : totalBoxes;
    }

    public void setTotalBoxes(Integer totalBoxes) {
        this.totalBoxes = totalBoxes;
    }

    public Double getTotalContractAmount() {
        return totalContractAmount == null ? 0.0 : totalContractAmount;
    }

    public void setTotalContractAmount(Double totalContractAmount) {
        this.totalContractAmount = totalContractAmount;
    }

    public Double getTotalContractMoney() {
        return totalContractMoney == null ? 0.0 : totalContractMoney;
    }

    public void setTotalContractMoney(Double totalContractMoney) {
        this.totalContractMoney = totalContractMoney;
    }

    public Double getTotalInvoiceAmount() {
        return totalInvoiceAmount == null ? 0.0 : totalInvoiceAmount;
    }

    public void setTotalInvoiceAmount(Double totalInvoiceAmount) {
        this.totalInvoiceAmount = totalInvoiceAmount;
    }

    public Double getTotalInvoiceMoney() {
        return totalInvoiceMoney == null ? 0.0 : totalInvoiceMoney;
    }

    public void setTotalInvoiceMoney(Double totalInvoiceMoney) {
        this.totalInvoiceMoney = totalInvoiceMoney;
    }

    public Integer getVersion() {
        return version == null ? 0 : version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse == null ? null : warehouse.trim();
    }

    public String getYahuidaoqiDate() {
        return yahuidaoqiDate;
    }

    public void setYahuidaoqiDate(String yahuidaoqiDate) {
        this.yahuidaoqiDate = yahuidaoqiDate == null ? null : yahuidaoqiDate.trim();
    }
}