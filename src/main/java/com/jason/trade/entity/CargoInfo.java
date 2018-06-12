package com.jason.trade.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class CargoInfo implements Serializable{
    @Id
    @GeneratedValue
    private Integer id;//序号
    private String cargoId;//产品序号
    private String contractId;//合同序号
    private String externalCompany;//外商
    private String cargoNo;//库号
    private String cargoName;//产品名称
    private double amount;//数量
    private double unitPrice;//单价
    private double contractValue;//合同金额
    private String saleCustomer;//销售客户
    private double unitPrePayAmount;//来款金额
    private String unitPrePayDate;//来款日期
    private double unitFinalPayAmount;//尾款金额
    private String unitFinalPayDate;//来款日期
    private double invoiceNumber;//发票数量
    private double invoiceValue;//发票金额
    private String elecSendDate;//电子版发送日期
    private double hystereticFee;//滞报费
    private String status;

    public CargoInfo(){}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCargoId() {
        return cargoId;
    }

    public void setCargoId(String cargoId) {
        this.cargoId = cargoId;
    }

    public double getUnitPrePayAmount() {
        return unitPrePayAmount;
    }

    public void setUnitPrePayAmount(double unitPrePayAmount) {
        this.unitPrePayAmount = unitPrePayAmount;
    }

    public String getUnitPrePayDate() {
        return unitPrePayDate;
    }

    public void setUnitPrePayDate(String unitPrePayDate) {
        this.unitPrePayDate = unitPrePayDate;
    }

    public double getUnitFinalPayAmount() {
        return unitFinalPayAmount;
    }

    public void setUnitFinalPayAmount(double unitFinalPayAmount) {
        this.unitFinalPayAmount = unitFinalPayAmount;
    }

    public String getUnitFinalPayDate() {
        return unitFinalPayDate;
    }

    public void setUnitFinalPayDate(String unitFinalPayDate) {
        this.unitFinalPayDate = unitFinalPayDate;
    }

    public String getSaleCustomer() {
        return saleCustomer;
    }

    public void setSaleCustomer(String saleCustomer) {
        this.saleCustomer = saleCustomer;
    }

    public double getHystereticFee() {
        return hystereticFee;
    }

    public void setHystereticFee(double hystereticFee) {
        this.hystereticFee = hystereticFee;
    }

    public String getCargoNo() {
        return cargoNo;
    }

    public void setCargoNo(String cargoNo) {
        this.cargoNo = cargoNo;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public String getExternalCompany() {
        return externalCompany;
    }

    public void setExternalCompany(String externalCompany) {
        this.externalCompany = externalCompany;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getContractValue() {
        return contractValue;
    }

    public void setContractValue(double contractValue) {
        this.contractValue = contractValue;
    }

    public double getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(double invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public double getInvoiceValue() {
        return invoiceValue;
    }

    public void setInvoiceValue(double invoiceValue) {
        this.invoiceValue = invoiceValue;
    }

    public String getElecSendDate() {
        return elecSendDate;
    }

    public void setElecSendDate(String elecSendDate) {
        this.elecSendDate = elecSendDate;
    }
}
