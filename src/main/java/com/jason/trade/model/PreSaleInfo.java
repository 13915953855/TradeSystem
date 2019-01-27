package com.jason.trade.model;

import javax.persistence.*;

@Entity
@Table(name="presale_info")
public class PreSaleInfo {
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
    @Column(name="expect_sale_date")
    private String expectSaleDate;
    @Column(name="pickup_user")
    private String pickupUser;
    @Column(name="expect_sale_unit_price")
    private Double expectSaleUnitPrice;
    @Column(name="expect_sale_weight")
    private Double expectSaleWeight;
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

    public String getExpectSaleDate() {
        return expectSaleDate;
    }

    public void setExpectSaleDate(String expectSaleDate) {
        this.expectSaleDate = expectSaleDate == null ? null : expectSaleDate.trim();
    }

    public String getPickupUser() {
        return pickupUser;
    }

    public void setPickupUser(String pickupUser) {
        this.pickupUser = pickupUser == null ? null : pickupUser.trim();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}