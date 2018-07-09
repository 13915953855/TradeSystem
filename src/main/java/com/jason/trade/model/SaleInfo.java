package com.jason.trade.model;

import javax.persistence.*;

@Entity
@Table(name="sale_info")
public class SaleInfo {
    @Id
    @GeneratedValue
    @Column(name="sale_id")
    private Integer saleId;
    @Column(name="cargo_id")
    private String cargoId;
    @Column(name="create_date_time")
    private String createDateTime;
    @Column(name="create_user")
    private String createUser;
    @Column(name="customer_name")
    private String customerName;
    @Column(name="customer_pay_date")
    private String customerPayDate;
    @Column(name="customer_pay_money")
    private Double customerPayMoney;
    @Column(name="expect_sale_boxes")
    private Integer expectSaleBoxes;
    @Column(name="expect_sale_date")
    private String expectSaleDate;
    @Column(name="expect_sale_money")
    private Double expectSaleMoney;
    @Column(name="expect_sale_unit_price")
    private Double expectSaleUnitPrice;
    @Column(name="expect_sale_weight")
    private Double expectSaleWeight;
    @Column(name="money_clear")
    private Integer moneyClear;
    @Column(name="payment_diff")
    private Double paymentDiff;
    @Column(name="pickup_date")
    private String pickupDate;
    @Column(name="pickup_user")
    private String pickupUser;
    @Column(name="profit")
    private Double profit;
    @Column(name="real_sale_boxes")
    private Integer realSaleBoxes;
    @Column(name="real_sale_date")
    private String realSaleDate;
    @Column(name="real_sale_money")
    private Double realSaleMoney;
    @Column(name="real_sale_unit_price")
    private Double realSaleUnitPrice;
    @Column(name="real_sale_weight")
    private Double realSaleWeight;
    @Column(name="remark")
    private String remark;
    @Column(name="sale_contract_no")
    private String saleContractNo;
    @Column(name="status")
    private String status;

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
        this.cargoId = cargoId == null ? null : cargoId.trim();
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName == null ? null : customerName.trim();
    }

    public String getCustomerPayDate() {
        return customerPayDate;
    }

    public void setCustomerPayDate(String customerPayDate) {
        this.customerPayDate = customerPayDate == null ? null : customerPayDate.trim();
    }

    public Double getCustomerPayMoney() {
        return customerPayMoney;
    }

    public void setCustomerPayMoney(Double customerPayMoney) {
        this.customerPayMoney = customerPayMoney;
    }

    public Integer getExpectSaleBoxes() {
        return expectSaleBoxes;
    }

    public void setExpectSaleBoxes(Integer expectSaleBoxes) {
        this.expectSaleBoxes = expectSaleBoxes;
    }

    public String getExpectSaleDate() {
        return expectSaleDate;
    }

    public void setExpectSaleDate(String expectSaleDate) {
        this.expectSaleDate = expectSaleDate == null ? null : expectSaleDate.trim();
    }

    public Double getExpectSaleMoney() {
        return expectSaleMoney;
    }

    public void setExpectSaleMoney(Double expectSaleMoney) {
        this.expectSaleMoney = expectSaleMoney;
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

    public Integer getMoneyClear() {
        return moneyClear;
    }

    public void setMoneyClear(Integer moneyClear) {
        this.moneyClear = moneyClear;
    }

    public Double getPaymentDiff() {
        return paymentDiff;
    }

    public void setPaymentDiff(Double paymentDiff) {
        this.paymentDiff = paymentDiff;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate == null ? null : pickupDate.trim();
    }

    public String getPickupUser() {
        return pickupUser;
    }

    public void setPickupUser(String pickupUser) {
        this.pickupUser = pickupUser == null ? null : pickupUser.trim();
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Integer getRealSaleBoxes() {
        return realSaleBoxes;
    }

    public void setRealSaleBoxes(Integer realSaleBoxes) {
        this.realSaleBoxes = realSaleBoxes;
    }

    public String getRealSaleDate() {
        return realSaleDate;
    }

    public void setRealSaleDate(String realSaleDate) {
        this.realSaleDate = realSaleDate == null ? null : realSaleDate.trim();
    }

    public Double getRealSaleMoney() {
        return realSaleMoney;
    }

    public void setRealSaleMoney(Double realSaleMoney) {
        this.realSaleMoney = realSaleMoney;
    }

    public Double getRealSaleUnitPrice() {
        return realSaleUnitPrice;
    }

    public void setRealSaleUnitPrice(Double realSaleUnitPrice) {
        this.realSaleUnitPrice = realSaleUnitPrice;
    }

    public Double getRealSaleWeight() {
        return realSaleWeight;
    }

    public void setRealSaleWeight(Double realSaleWeight) {
        this.realSaleWeight = realSaleWeight;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getSaleContractNo() {
        return saleContractNo;
    }

    public void setSaleContractNo(String saleContractNo) {
        this.saleContractNo = saleContractNo == null ? null : saleContractNo.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}