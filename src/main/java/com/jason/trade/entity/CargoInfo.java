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
    private String cargoNo;//库号
    private String cargoName;//产品名称
    private String level;//级别
    private String specification;//规格
    private double unitPrice;//单价
    private double contractAmount;//合同数量
    private double contractMoney;//合同金额
    private double invoiceAmount;//发票数量
    private double invoiceMoney;//发票金额
    private Integer boxes;//箱数
    private String status;

    public CargoInfo(){}

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

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(double contractAmount) {
        this.contractAmount = contractAmount;
    }

    public double getContractMoney() {
        return contractMoney;
    }

    public void setContractMoney(double contractMoney) {
        this.contractMoney = contractMoney;
    }

    public double getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public double getInvoiceMoney() {
        return invoiceMoney;
    }

    public void setInvoiceMoney(double invoiceMoney) {
        this.invoiceMoney = invoiceMoney;
    }

    public Integer getBoxes() {
        return boxes;
    }

    public void setBoxes(Integer boxes) {
        this.boxes = boxes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
