package com.jason.trade.model;

import javax.persistence.*;

@Entity
@Table(name="cargo_info")
public class CargoInfo {
    @Id
    @GeneratedValue
    @Column(name="id")
    private Integer id;
    @Column(name="boxes")
    private Integer boxes;
    @Column(name="cargo_id")
    private String cargoId;
    @Column(name="cargo_name")
    private String cargoName;
    @Column(name="cargo_no")
    private String cargoNo;
    @Column(name="contract_amount")
    private Double contractAmount;
    @Column(name="contract_id")
    private String contractId;
    @Column(name="contract_money")
    private Double contractMoney;
    @Column(name="cost_price")
    private Double costPrice;
    @Column(name="create_date_time")
    private String createDateTime;
    @Column(name="create_user")
    private String createUser;
    @Column(name="expect_store_boxes")
    private Integer expectStoreBoxes;
    @Column(name="expect_store_weight")
    private Double expectStoreWeight;
    @Column(name="invoice_amount")
    private Double invoiceAmount;
    @Column(name="invoice_money")
    private Double invoiceMoney;
    @Column(name="level")
    private String level;
    @Column(name="real_store_boxes")
    private Integer realStoreBoxes;
    @Column(name="real_store_money")
    private Double realStoreMoney;
    @Column(name="real_store_weight")
    private Double realStoreWeight;
    @Column(name="status")
    private String status;
    @Column(name="unit_price")
    private Double unitPrice;
    @Column(name="business_mode")
    private String businessMode;
    @Column(name="company_no")
    private String companyNo;

    public String getBusinessMode() {
        return businessMode;
    }

    public void setBusinessMode(String businessMode) {
        this.businessMode = businessMode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo == null ? null : companyNo.trim();
    }

    public Integer getBoxes() {
        return boxes;
    }

    public void setBoxes(Integer boxes) {
        this.boxes = boxes;
    }

    public String getCargoId() {
        return cargoId;
    }

    public void setCargoId(String cargoId) {
        this.cargoId = cargoId == null ? null : cargoId.trim();
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName == null ? null : cargoName.trim();
    }

    public String getCargoNo() {
        return cargoNo;
    }

    public void setCargoNo(String cargoNo) {
        this.cargoNo = cargoNo == null ? null : cargoNo.trim();
    }

    public Double getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(Double contractAmount) {
        this.contractAmount = contractAmount;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId == null ? null : contractId.trim();
    }

    public Double getContractMoney() {
        return contractMoney;
    }

    public void setContractMoney(Double contractMoney) {
        this.contractMoney = contractMoney;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
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

    public Integer getExpectStoreBoxes() {
        return expectStoreBoxes;
    }

    public void setExpectStoreBoxes(Integer expectStoreBoxes) {
        this.expectStoreBoxes = expectStoreBoxes;
    }

    public Double getExpectStoreWeight() {
        return expectStoreWeight;
    }

    public void setExpectStoreWeight(Double expectStoreWeight) {
        this.expectStoreWeight = expectStoreWeight;
    }

    public Double getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(Double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public Double getInvoiceMoney() {
        return invoiceMoney;
    }

    public void setInvoiceMoney(Double invoiceMoney) {
        this.invoiceMoney = invoiceMoney;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public Integer getRealStoreBoxes() {
        return realStoreBoxes;
    }

    public void setRealStoreBoxes(Integer realStoreBoxes) {
        this.realStoreBoxes = realStoreBoxes;
    }

    public Double getRealStoreMoney() {
        return realStoreMoney;
    }

    public void setRealStoreMoney(Double realStoreMoney) {
        this.realStoreMoney = realStoreMoney;
    }

    public Double getRealStoreWeight() {
        return realStoreWeight;
    }

    public void setRealStoreWeight(Double realStoreWeight) {
        this.realStoreWeight = realStoreWeight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }
}