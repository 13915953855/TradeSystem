package com.jason.trade.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class SaleInfo implements Serializable{
    @Id
    @GeneratedValue
    private Integer saleId;
    private String cargoId;
    //private double pickupWeight;
    //private Integer pickupBoxes;
    private String pickupDate;
    private String pickupUser;//销售经理
    private String saleContractNo;
    private String customerName;
    private double expectSaleUnitPrice;
    private double expectSaleWeight;
    private Integer expectSaleBoxes;
    private double expectSaleMoney;
    private String expectSaleDate;
    private double realSaleUnitPrice;//实际销售单价
    private double realSaleWeight;
    private Integer realSaleBoxes;
    private double realSaleMoney;
    private String realSaleDate;
    private String customerPayDate;
    private double customerPayMoney;
    private String createUser;
    private String createDateTime;
    private String status;
    private double paymentDiff;//货款差额
    private String moneyClear;//货款是否已结清
    private double profit;//利润
    private String remark;

    public SaleInfo(){}

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public double getRealSaleUnitPrice() {
        return realSaleUnitPrice;
    }

    public void setRealSaleUnitPrice(double realSaleUnitPrice) {
        this.realSaleUnitPrice = realSaleUnitPrice;
    }

    public double getPaymentDiff() {
        return paymentDiff;
    }

    public void setPaymentDiff(double paymentDiff) {
        this.paymentDiff = paymentDiff;
    }

    public String getMoneyClear() {
        return moneyClear;
    }

    public void setMoneyClear(String moneyClear) {
        this.moneyClear = moneyClear;
    }

    public double getExpectSaleUnitPrice() {
        return expectSaleUnitPrice;
    }

    public void setExpectSaleUnitPrice(double expectSaleUnitPrice) {
        this.expectSaleUnitPrice = expectSaleUnitPrice;
    }

    public Integer getExpectSaleBoxes() {
        return expectSaleBoxes;
    }

    public void setExpectSaleBoxes(Integer expectSaleBoxes) {
        this.expectSaleBoxes = expectSaleBoxes;
    }

    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    public String getCargoId() {
        return cargoId;
    }

    public void setCargoId(String cargoId) {
        this.cargoId = cargoId;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getPickupUser() {
        return pickupUser;
    }

    public void setPickupUser(String pickupUser) {
        this.pickupUser = pickupUser;
    }

    public String getSaleContractNo() {
        return saleContractNo;
    }

    public void setSaleContractNo(String saleContractNo) {
        this.saleContractNo = saleContractNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getExpectSaleWeight() {
        return expectSaleWeight;
    }

    public void setExpectSaleWeight(double expectSaleWeight) {
        this.expectSaleWeight = expectSaleWeight;
    }

    public double getExpectSaleMoney() {
        return expectSaleMoney;
    }

    public void setExpectSaleMoney(double expectSaleMoney) {
        this.expectSaleMoney = expectSaleMoney;
    }

    public String getExpectSaleDate() {
        return expectSaleDate;
    }

    public void setExpectSaleDate(String expectSaleDate) {
        this.expectSaleDate = expectSaleDate;
    }

    public double getRealSaleWeight() {
        return realSaleWeight;
    }

    public void setRealSaleWeight(double realSaleWeight) {
        this.realSaleWeight = realSaleWeight;
    }

    public Integer getRealSaleBoxes() {
        return realSaleBoxes;
    }

    public void setRealSaleBoxes(Integer realSaleBoxes) {
        this.realSaleBoxes = realSaleBoxes;
    }

    public double getRealSaleMoney() {
        return realSaleMoney;
    }

    public void setRealSaleMoney(double realSaleMoney) {
        this.realSaleMoney = realSaleMoney;
    }

    public String getRealSaleDate() {
        return realSaleDate;
    }

    public void setRealSaleDate(String realSaleDate) {
        this.realSaleDate = realSaleDate;
    }

    public String getCustomerPayDate() {
        return customerPayDate;
    }

    public void setCustomerPayDate(String customerPayDate) {
        this.customerPayDate = customerPayDate;
    }

    public double getCustomerPayMoney() {
        return customerPayMoney;
    }

    public void setCustomerPayMoney(double customerPayMoney) {
        this.customerPayMoney = customerPayMoney;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
