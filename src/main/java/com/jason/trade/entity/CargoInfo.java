package com.jason.trade.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class CargoInfo implements Serializable{
    @Id
    @GeneratedValue
    private Integer id;//序号

    private String cargoName;//产品名称
    private String externalCompany;//外商
    private double amount;//数量
    private double unitPrice;//单价
    private double contractValue;//合同金额
    private double invoiceNumber;//发票数量
    private double invoiceValue;//发票金额
    private Date elecSendDate;//电子版发送日期

    public CargoInfo(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Date getElecSendDate() {
        return elecSendDate;
    }

    public void setElecSendDate(Date elecSendDate) {
        this.elecSendDate = elecSendDate;
    }
}
